package np.dheeraj.sachan.transcoder;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import DesktopTranscoder.AppConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import np.dheeraj.sachan.Events.*;
import np.dheeraj.sachan.helper.HttpAgent;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.commons.io.FilenameUtils;

/**
 * Created by dheeraj on 5/2/14.
 */
public class TranscoderController {

    public volatile Queue<ConversionTask> videoConversionTaskQueue = new ConcurrentLinkedQueue<ConversionTask>();
    private static final String selectcourse = "Select ...";
    private static final String selectFile = "Select File";
    private static final String chooseLecture = "Choose lecture to remove from queue";
    private static final String[] allowedExts = {"mp4", "flv", "webm", "f4v", "avi", "mkv", "wmv", "mov", "mpeg", "wav", "asf", "mjpeg", "m2p", "m4p", "mpg", "vob", "m2ts", "mts"};
    private static final int primaryIndex = 0;
    private static final int secondaryIndex = 1;
    private static final String appName = "DVideoConverter";
    private Parent parent;
    private Scene scene;
    private Stage stage;
    private static final Logger logger = LoggerFactory
            .getLogger(TranscoderController.class);
    private static final EventBus eventBus = new EventBus();
    private Map<String, Integer> courseList;
    private boolean isCheckBoxSelected = false;
    private String primaryFileName = null;
    private String secondaryFileName = null;
    private Map<String, String> lectureNameToSessionIdMap = new HashMap<String, String>();
    private int transcodePendingJobNumberInQueue = 0;
    private boolean updateOnce = false;
    private AppConfig appConfig = new AppConfig();
    private boolean enableCompression = true;
    private File browsedFolder = null;
    private static final String courseLectureSeperator = "--";
    private int totalInQueueInt = 0;

    @FXML
    private Text welcomeText;
    @FXML
    private ComboBox courseComboBox;
    @FXML
    private TextField lectureNameTextField;
    @FXML
    private ComboBox primaryVideoBitrateComboBox;
    @FXML
    private ComboBox primaryAudioBitrateComboBox;
    @FXML
    private Text actionTargetText;
    @FXML
    private ComboBox primaryFrameSizeComboBox;
    @FXML
    private ProgressBar transcodeProgressBar;
    @FXML
    private ProgressBar uploadProgressBar;
    @FXML
    private javafx.scene.control.CheckBox selectSecondaryCheckBox;
    @FXML
    private javafx.scene.control.CheckBox enableCompressionCheckBox;
    @FXML
    private Button selectPrimaryVideoButton;
    @FXML
    private Label selectSecondaryVideoLabel;
    @FXML
    private Label secondaryVideoBitrateLabel;
    @FXML
    private Label secondaryAudioBitrateLabel;
    @FXML
    private Label secondaryFrameSizeLabel;
    @FXML
    private Button selectSecondaryVideoButton;
    @FXML
    private ComboBox secondaryVideoBitrateComboBox;
    @FXML
    private ComboBox secondaryAudioBitrateComboBox;
    @FXML
    private ComboBox secondaryFrameSizeComboBox;
    @FXML
    private Label compressingLabel;
    @FXML
    private Text compressingFileNameText;
    @FXML
    private Label compressionsPendingLabel;
    @FXML
    private Text compressionsPendingText;
    @FXML
    private Label uploadingLabel;
    @FXML
    private Text uploadingFileText;
    @FXML
    private Label uploadsPendingLabel;
    @FXML
    private Text uploadsPendingText;
    @FXML
    private TextField lectureDescritionTextField;
    @FXML
    private Text updateText;
    @FXML
    private Text updateDownloadedText;
    @FXML
    private javafx.scene.control.Button downloadUpdateButton;
    @FXML
    private javafx.scene.control.Button applyUpdateButton;
    @FXML
    private javafx.scene.control.Button startButton;
    @FXML
    private javafx.scene.control.Button stopButton;
    @FXML
    private javafx.scene.control.Button addInQueueButton;
    @FXML
    private javafx.scene.control.Button removeFromQueueButton;
    @FXML
    private ComboBox lectureListComboBox;
    @FXML
    private Text totalInQueueText;

    public TranscoderController() {
        eventBus.register(this);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/transcoder.fxml"));
        fxmlLoader.setController(this);
        try {
            parent = (Parent) fxmlLoader.load();
            scene = new Scene(parent, 600, 550, Color.SKYBLUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void redirectHome(Stage stageLogin) {
        this.totalInQueueText.setText("" + totalInQueueInt);
        //check for update
        List<NameValuePair> postData = new ArrayList<NameValuePair>(1);
        postData.add(new BasicNameValuePair("name", "name"));
        String response = HttpAgent.post(appConfig.getUpdateUrl(), postData);
        logger.info("check update response" + response);
        this.stage = stageLogin;
        this.stage.resizableProperty().setValue(Boolean.FALSE);
        selectPrimaryVideoButton.setText(selectFile);
        this.removeFromQueueButton.setDisable(true);
        stage.setTitle(appName + "--" + appConfig.getVersion());
        stage.setScene(scene);
        stage.hide();
        stage.show();
        String[] videoBitrateArray = {"50 kbit/s",
                "75 kbit/s",
                "100 kbit/s",
                "150 kbit/s",
                "200 kbit/s",
                "250 kbit/s",
                "300 kbit/s",
                "350 kbit/s",
                "400 kbit/s",
                "450 kbit/s",
                "500 kbit/s",
                "600 kbit/s",
                "700 kbit/s",
                "800 kbit/s",
                "900 kbit/s",
                "1000 kbit/s"};

        String[] audioBitrateArray = {
                "18 kbit/s",
                "32 kbit/s",
                "48 kbit/s",
                "64 kbit/s",
                "96 kbit/s",
                "128 kbit/s",
                "160 kbit/s",
                "192 kbit/s",
                "224 kbit/s"
        };
        String[] frameSizeArray = {
                "160x120",
                "640x360",
                "852x480",
                "960x540",
                "1024x576",
                "1280x720",
                "1364x768",
                "1600x900",
                "1920x1080"
        };

        this.primaryVideoBitrateComboBox.getItems().addAll(videoBitrateArray);
        this.primaryVideoBitrateComboBox.setValue("200 kbit/s");

        this.primaryAudioBitrateComboBox.getItems().addAll(audioBitrateArray);
        this.primaryAudioBitrateComboBox.setValue("32 kbit/s");

        this.primaryFrameSizeComboBox.getItems().addAll(frameSizeArray);
        this.primaryFrameSizeComboBox.setValue("960x540");

        this.selectPrimaryVideoButton.setText(selectFile);
    }

    private void clearFields() {
        this.lectureNameTextField.clear();
        this.lectureDescritionTextField.clear();
        this.primaryFileName = null;
    }

    @FXML
    protected void addInQueue(ActionEvent e) {

        ConversionTask conversionTask = new ConversionTask(primaryFileName, primaryVideoBitrateComboBox.getValue().toString().replace("kbit/s", ""), primaryAudioBitrateComboBox.getValue().toString().replace("kbit/s", ""), primaryFrameSizeComboBox.getValue().toString());
        try {
            if (videoConversionTaskQueue.add(conversionTask)) {
                logger.info("Task successfully added in queue");
            } else {
                logger.error("Unable to add task in queue");
            }
        } catch (Exception e1) {
            logger.error("Unable to add task in queue due to exception " + e1.getMessage());

        }
    }

    @FXML
    protected void handleStartButtonAction(ActionEvent event) {
    }

    @FXML
    protected void handleStopButtonAction(ActionEvent event) {
        onWindowClosed();
    }

    public void onWindowClosed() {
        eventBus.post(new StopEverthingEvent());
    }


    public void close() {

    }

    @FXML
    protected void selectPrimaryVideo(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Primary Video File");
        if (browsedFolder != null) {
            fileChooser.setInitialDirectory(browsedFolder);
        }
        File file = fileChooser.showOpenDialog(stage);
        try {
            String fileName = file.getAbsolutePath();
            browsedFolder = new File(file.getParent());
            String extension = FilenameUtils.getExtension(fileName).toLowerCase();
            if (Arrays.asList(allowedExts).contains(extension)) {
                this.primaryFileName = "\"" + fileName + "\"";
                selectPrimaryVideoButton.setText(file.getName());
                logger.info("Primary file name" + fileName);
            } else {
                showErrorPopup(primaryIndex, fileName, file);
            }
        } catch (Exception e1) {
            logger.info("Caught exception " + e1.getMessage());
        }
    }

    private String getAllowedExts() {
        String test = "";
        for (int i = 0; i < allowedExts.length; i++) {
            if (i == allowedExts.length - 1) {
                test = test + allowedExts[i];
            } else {
                test = test + allowedExts[i] + ",";
            }
        }
        return test;
    }

    @Subscribe
    public void onForceAddEvent(ForceAddEvent forceAddEvent) {
        if (forceAddEvent.getIndex() == primaryIndex) {
            this.primaryFileName = "\"" + forceAddEvent.getFileName() + "\"";
            selectPrimaryVideoButton.setText(forceAddEvent.getFile().getName());
            logger.info("Force add Primary file name" + forceAddEvent.getFileName());
        } else if (forceAddEvent.getIndex() == secondaryIndex) {
            this.secondaryFileName = "\"" + forceAddEvent.getFileName() + "\"";
            selectSecondaryVideoButton.setText(forceAddEvent.getFile().getName());
            logger.info("Force add Secondary file name" + forceAddEvent.getFileName());
        }
    }

    private void showErrorPopup(final int index, final String fileName, final File file) {
        final Stage dialogStage = new Stage();
        dialogStage.setTitle("Error");
        Text text = new Text("Allowed file types are " + getAllowedExts());
        text.setFont(Font.font(null, FontWeight.SEMI_BOLD, 20));
        text.setFill(Color.BLACK);

        HBox hBox = new HBox(20);
        hBox.setAlignment(Pos.CENTER);
        Button yesButton = new Button("CANCEL");
        Button addAnyway = new Button("ADD ANYWAY");
        hBox.getChildren().addAll(yesButton, addAnyway);
        yesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                dialogStage.close();
            }
        });
        addAnyway.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                eventBus.post(new ForceAddEvent(index, fileName, file));
                dialogStage.close();
            }
        });

        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setScene(new Scene(VBoxBuilder.create().
                //children(text, yesButton, addAnyway).
                        children(text, hBox).
                alignment(Pos.CENTER).padding(new Insets(5)).build(), Color.GRAY));
        dialogStage.show();
    }

    @Subscribe
    public void onTranscodingFile(TranscodingFileEvent event) {
        compressingFileNameText.setFill(Color.GREEN);
        //update lecture status on webapp
        List<NameValuePair> postData = new ArrayList<NameValuePair>(1);
        String reply = HttpAgent.post(appConfig.getOnTranscodeStartUrl(), postData);
    }


    @Subscribe
    public void onTranscodeComplete(TrancodeCompleteEvent event) {
    }


    @Subscribe
    public void onTranscodeFail(TranscodeFailEvent event) {

    }

    public void onQueuecompleteEvent(TranscodeQueueCompleteEvent event) {
        logger.info("############################################################### transcode queue complete event received");
        this.transcodePendingJobNumberInQueue = 0;
        if (this.lectureListComboBox.isDisable())
            this.lectureListComboBox.setDisable(false);

        this.compressionsPendingText.setText(Integer.toString(0));

        this.lectureNameToSessionIdMap = new HashMap<String, String>();
        this.transcodeProgressBar.setProgress(0);
        this.compressingFileNameText.setText("none");
        this.compressionsPendingText.setFill(Color.GREEN);
        this.compressionsPendingText.setText(Integer.toString(0));
        if (this.startButton.isDisable())
            this.startButton.setDisable(false);
        if (this.addInQueueButton.isDisable())
            this.addInQueueButton.setDisable(false);
        if (this.removeFromQueueButton.isDisable())
            this.removeFromQueueButton.setDisable(false);
        totalInQueueInt = 0;
        totalInQueueText.setText("" + totalInQueueInt);
    }

    public void removeFromQueue(ActionEvent e) {
    }
}
