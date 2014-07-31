//mvn com.zenjava:javafx-maven-plugin:2.0:fix-classpath
package DesktopTranscoder;

import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import np.dheeraj.sachan.transcoder.*;

public class App extends Application {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private TranscoderController transcoderController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.info("---------------------------------------------------------------------");
        logger.info("-------------------- App started ------------------------------------");
        logger.info("---------------------------------------------------------------------");
        primaryStage.resizableProperty().setValue(Boolean.FALSE);
        transcoderController =  new TranscoderController();
        transcoderController.redirectHome(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("Window Closed ,stopping running session");
        transcoderController.onWindowClosed();
        logger.info("---------------------------------------------------------------------");
        logger.info("-------------------- App stopped ------------------------------------");
        logger.info("---------------------------------------------------------------------");
    }

    public static void main(String[] args) {
        launch(args);
    }
}