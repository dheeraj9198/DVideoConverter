package np.dheeraj.sachan.transcoder;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import VideoConverter.AppConfig;
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
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.io.FilenameUtils;

/**
 * Created by dheeraj on 5/2/14.
 */
public class TranscoderController {

    public volatile ConcurrentLinkedQueue<ConversionTask> videoConversionTaskQueue = new ConcurrentLinkedQueue<ConversionTask>();
    private static final String selectcourse = "Select ...";
    private static final String selectFile = "Select File";
    private static final String chooseLecture = "Choose lecture to remove from queue";
    private static final String[] allowedExts = {"mp4", "flv", "webm", "f4v", "avi", "mkv", "wmv", "mov", "mpeg", "wav", "asf", "mjpeg", "m2p", "m4p", "mpg", "vob", "m2ts", "mts"};
    private static final String appName = "DVideoConverter";
    private Parent parent;
    private Scene scene;
    private Stage stage;
    private static final Logger logger = LoggerFactory
            .getLogger(TranscoderController.class);
    private static final EventBus eventBus = new EventBus();
    private volatile String inputFileName = null;
    private volatile String outPutFileName = null;
    private ConcurrentHashMap<String, String> inputToOutputMap = new ConcurrentHashMap<String, String>();
    private volatile int transcodePendingJobNumberInQueue = 0;
    private volatile boolean updateOnce = false;
    private AppConfig appConfig = new AppConfig();
    private volatile File browsedFolder = null;
    private volatile int totalInQueueInt = 0;

    @FXML
    private Text welcomeText;
    @FXML
    private TextField lectureNameTextField;
    @FXML
    private ComboBox primaryVideoBitrateComboBox;
    @FXML
    private ComboBox primaryAudioBitrateComboBox;
    @FXML
    private Text messageText;
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
    private ComboBox<String> taskListComboBox;
    @FXML
    private Text totalInQueueText;
    @FXML
    private TextField outputFileTextField;
    @FXML
    private Button outputFileButton;
    @FXML
    private CheckBox crfBox;
    @FXML
    private ComboBox<String> crfComboBox;
    @FXML
    private ComboBox<String> videoCodecComboBox;
    @FXML
    private ComboBox<String> audioCodecComboBox;
    @FXML
    private ComboBox<String> extensionCombobox;

    public TranscoderController() {
        eventBus.register(this);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/transcoder.fxml"));
        fxmlLoader.setController(this);
        try {
            parent = (Parent) fxmlLoader.load();
            //scene = new Scene(parent, 600, 550, Color.SKYBLUE);
            parent.setId("pane");
            scene = new Scene(parent, 750, 500);
            scene.getStylesheets().addAll(getClass().getResource("/fxml/style.css").toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void redirectHome(Stage stageLogin) {
        messageText.setFont(Font.font("Verdana",FontWeight.BOLD,15));
        this.totalInQueueText.setText("" + totalInQueueInt);
        //check for update
        List<NameValuePair> postData = new ArrayList<NameValuePair>(1);
        postData.add(new BasicNameValuePair("name", "name"));
        //String response = HttpAgent.post(appConfig.getUpdateUrl(), postData);
        //logger.info("check update response" + response);
        this.stage = stageLogin;
        this.stage.resizableProperty().setValue(Boolean.FALSE);
        selectPrimaryVideoButton.setText(selectFile);
        this.removeFromQueueButton.setDisable(true);
        stage.setTitle(appName + "--" + appConfig.getVersion());
        stage.setScene(scene);
        stage.hide();
        stage.show();
        outputFileButton.setText("Output File");
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
                "1000 kbit/s",
                "1250 kbit/s",
                "1500 kbit/s",
                "1750 kbit/s",
                "2000 kbit/s"};

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
        this.primaryVideoBitrateComboBox.setValue("500 kbit/s");

        this.primaryAudioBitrateComboBox.getItems().addAll(audioBitrateArray);
        this.primaryAudioBitrateComboBox.setValue("128 kbit/s");

        this.primaryFrameSizeComboBox.getItems().addAll(frameSizeArray);
        this.primaryFrameSizeComboBox.setValue("1280x720");

        this.selectPrimaryVideoButton.setText(selectFile);
        this.transcodeProgressBar.setMinHeight(15);

        ArrayList<String> crfStrings = new ArrayList<String>();
        for (int a = 20; a < 50; a++) {
            crfStrings.add(a + "");
        }

        crfComboBox.getItems().addAll(crfStrings);
        crfComboBox.setValue("25");
        crfComboBox.setDisable(true);

        String[] extensionArray = {"mkv", "mp4"};
        extensionCombobox.getItems().addAll(extensionArray);
        extensionCombobox.setValue("mp4");

        String[] videoCodecs = {"H.264(x264)", "MPEG-4(ffmpeg)", "MPEG-2(ffmpeg)"};
        videoCodecComboBox.getItems().addAll(videoCodecs);
        videoCodecComboBox.setValue("H.264(x264)");

        String[] audioCodecs = {"MP3(lame)", "AAC(ffmpeg)"};
        audioCodecComboBox.getItems().addAll(audioCodecs);
        audioCodecComboBox.setValue("MP3(lame)");
    }

    private void clearFields() {
        this.inputFileName = null;
        this.selectPrimaryVideoButton.setText(selectFile);
    }

    @FXML
    protected void crfCheckBoxActionListener(ActionEvent event) {
        if (crfBox.isSelected()) {
            if (crfComboBox.isDisabled()) {
                crfComboBox.setDisable(false);
            }
            if (!primaryVideoBitrateComboBox.isDisabled()) {
                primaryVideoBitrateComboBox.setDisable(true);
            }
        }  else{
            if (!crfComboBox.isDisabled()) {
                crfComboBox.setDisable(true);
            }
            if (primaryVideoBitrateComboBox.isDisabled()) {
                primaryVideoBitrateComboBox.setDisable(false);
            }
        }
    }

    @FXML
    protected void addInQueue(ActionEvent e) {

        logger.info("input file "+inputFileName);
        logger.info("output file "+outPutFileName);

        if(inputFileName == null )
        {
            messageText.setFill(Color.RED);
            messageText.setText("No input file selected");
        }else if(outPutFileName == null)
        {
            messageText.setFill(Color.RED);
            messageText.setText("No output file selected");
        }

        ConversionTask conversionTask = new ConversionTask("\"" + inputFileName + "\"", primaryVideoBitrateComboBox.getValue().toString().replace("kbit/s", ""),
                primaryAudioBitrateComboBox.getValue().toString().replace("kbit/s", ""),
                primaryFrameSizeComboBox.getValue().toString(),
                outPutFileName+"."+extensionCombobox.getValue()+"\"",crfComboBox.getValue(),crfBox.isSelected());
        try {
            if (videoConversionTaskQueue.add(conversionTask)) {
                logger.info("Task successfully added in queue");
                taskListComboBox.getItems().add(inputFileName);
                if (videoConversionTaskQueue.size() > 0 && removeFromQueueButton.isDisabled()) {
                    removeFromQueueButton.setDisable(false);
                }
                inputFileName = null;
                outPutFileName = null;
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

    @FXML
    protected void selectOutputVideo(ActionEvent event) throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Primary Video File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("mkv", "*.mkv"),
                new FileChooser.ExtensionFilter("mp4", "*.mp4")
        );
        if (browsedFolder != null) {
            fileChooser.setInitialDirectory(browsedFolder);
        }
        File file = fileChooser.showSaveDialog(stage);
        outPutFileName = "\""+file.getAbsolutePath();
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
                this.inputFileName = "\""+fileName+"\"";
                selectPrimaryVideoButton.setText(file.getName());
                logger.info("Primary file name" + fileName);
            } else {
                showErrorPopup(fileName, file);
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
        this.inputFileName = "\"" + forceAddEvent.getFileName() + "\"";
        selectPrimaryVideoButton.setText(forceAddEvent.getFile().getName());
        logger.info("Force add Primary file name" + forceAddEvent.getFileName());
    }

    private void showErrorPopup(final String fileName, final File file) {
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
                eventBus.post(new ForceAddEvent(fileName, file));
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
    }


    @Subscribe
    public void onTranscodeComplete(TrancodeCompleteEvent event) {
    }


    @Subscribe
    public void onTranscodeFail(TranscodeFailEvent event) {

    }

    public void onQueuecompleteEvent(TranscodeQueueCompleteEvent event) {
        this.transcodePendingJobNumberInQueue = 0;
        if (this.taskListComboBox.isDisable()) {
            this.taskListComboBox.setDisable(false);
        }
        this.compressionsPendingText.setText(Integer.toString(0));
        this.inputToOutputMap = new ConcurrentHashMap<String, String>();
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
        if (taskListComboBox.getValue() != null && !taskListComboBox.getValue().toString().replace(" ", "").equals("")) {
            for (ConversionTask conversionTask : videoConversionTaskQueue) {
                if (conversionTask.getFileName().equals("\"" + taskListComboBox.getValue() + "\"")) {
                    videoConversionTaskQueue.remove(conversionTask);
                    taskListComboBox.getItems().remove(taskListComboBox.getValue());
                }
            }

            if(videoConversionTaskQueue.isEmpty())
            {
                if(!removeFromQueueButton.isDisabled())
                {
                removeFromQueueButton.setDisable(true);
                }
            }
        }
    }
}
