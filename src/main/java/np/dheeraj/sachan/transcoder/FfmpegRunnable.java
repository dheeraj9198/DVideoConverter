package np.dheeraj.sachan.transcoder;
import com.google.common.eventbus.EventBus;
import np.dheeraj.sachan.Events.TrancodeCompleteEvent;
import np.dheeraj.sachan.Events.TranscodeFailEvent;
import np.dheeraj.sachan.Events.TranscodingFileEvent;
import np.dheeraj.sachan.videoTranscoder.ExecLogHandler;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: windows 7
 * Date: 9/13/14
 * Time: 10:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class FfmpegRunnable implements Runnable {
    private ArrayBlockingQueue<ConversionTask> conversionTasks;
    private EventBus eventBus;
    private static final Logger logger = LoggerFactory.getLogger(FfmpegRunnable.class);
    public FfmpegRunnable(ArrayBlockingQueue<ConversionTask> tasks,EventBus eventBus) {
        this.conversionTasks = tasks;
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Override
    public void run()
    {

        if(conversionTasks.isEmpty())
        {
            throw new RuntimeException("tasks finished");
        }
        String outputFile = "";
        String inputFile = "";
        ConversionTask conversionTask = null;
        try{
            conversionTask = conversionTasks.take();
            String command = conversionTask.getCommandToExecute();
            inputFile = conversionTask.getFileName();
            eventBus.post(new TranscodingFileEvent(inputFile));
            CommandLine cmdLine = CommandLine.parse(command);
            logger.info("Executing FFMPEG command " + command);
            DefaultExecutor executor = new DefaultExecutor();
                PumpStreamHandler psh = new PumpStreamHandler(new ExecLogHandler(logger, Level.INFO), new ExecLogHandler(logger, Level.ERROR));
                executor.setStreamHandler(psh);
                executor.execute(cmdLine);
        }
        catch (Exception e)
        {
            logger.error("caught exception "+e);
        }  finally {
        File file = new File(outputFile);
        if (file.exists() && file.length() > 200) {
            //transcode success
            eventBus.post(new TrancodeCompleteEvent(inputFile));
        } else {
            logger.error("FFMPEG CONVERSION FAILED FOR FILE " + outputFile);
            //transocde failed
            eventBus.post(new TranscodeFailEvent(inputFile));
        }
    }
    }
}
