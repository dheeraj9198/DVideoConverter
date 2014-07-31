package np.dheeraj.sachan.statusUpdater;

import np.dheeraj.sachan.Events.StopTranscoderStatusUpdaterEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Aurus
 * Date: 2/14/14
 * Time: 7:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class TranscodeStatusUpdater extends Thread {
    private static final Logger logger = LoggerFactory
            .getLogger(TranscodeStatusUpdater.class);
    private String fileName;
    private EventBus eventBus;
    private Long fileSize;
    private boolean runThread = true;

    public TranscodeStatusUpdater(String fileName, EventBus eventBus, Long fileSize) {
        this.fileName = fileName;
        this.eventBus = eventBus;
        this.eventBus.register(this);
        if (fileSize == 0L) {
            this.fileSize = 1L;
        } else {
            this.fileSize = fileSize;
        }
        start();
    }

    @Subscribe
    public void stopThread(StopTranscoderStatusUpdaterEvent stopTranscoderStatusUpdaterEvent) {
        /*logger.info("stop thread received******************************************");
        logger.info("stop thread received******************************************");
        logger.info("stop thread received******************************************");*/
        this.runThread = false;
    }


    @Override
    public void run() {
        logger.info("transcoder status updater run called ");
        while (true) {
            if (!this.runThread) {
                return;
            }
            Long size = 0L;
            File file = new File(fileName);
            if (file.exists()) {
                size = file.length();
            }
            Double status = size.doubleValue()/fileSize.doubleValue();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.error("InterruptedException while Thread.sleep " + e);
            }
        }
    }
}
