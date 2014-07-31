package np.dheeraj.sachan.videoTranscoder;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import np.dheeraj.sachan.Events.*;
import np.dheeraj.sachan.statusUpdater.TranscodeStatusUpdater;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Aurus
 * Date: 4/2/14
 * Time: 3:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class QueueEncoderThread extends Thread {
    private static final Logger logger = LoggerFactory
            .getLogger(QueueEncoderThread.class);
    private EventBus eventBus;
    private boolean runThread = true;
    private List<String> deleteSessionIdsList = new ArrayList<String>();
    private String runningSessionId = null;
    private String deleteFileName = null;

    public QueueEncoderThread() {
        this.eventBus = eventBus;
        this.eventBus.register(this);
        start();
    }

    @Subscribe
    public void stopReceived(StopEverthingEvent event) {
        this.runThread = false;
    }


    @Override
    public void run() {
    }

}
