package np.dheeraj.sachan.transcoder;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import np.dheeraj.sachan.Events.*;
import np.dheeraj.sachan.videoTranscoder.ExecLogHandler;
import org.apache.commons.exec.*;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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
    private ProgressBar txProgressBar;

    private volatile double progressPecentage = 0.0;

    private ScheduledExecutorService scheduledExecutorService;

    private ExecuteWatchdog executeWatchdog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);

    public FfmpegRunnable(ArrayBlockingQueue<ConversionTask> tasks, EventBus eventBus,ProgressBar progressBar) {
        this.conversionTasks = tasks;
        this.txProgressBar = progressBar;
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void killAll(StopEverthingEvent stopEverthingEvent)
    {
        executeWatchdog.destroyProcess();
    }

    @Override
    public void run() {

        if (conversionTasks.isEmpty()) {
            throw new RuntimeException("tasks finished");
        }
        String outputFile = "";
        String inputFile = "";
        ConversionTask conversionTask = null;
        try {
            conversionTask = conversionTasks.take();
            String command = conversionTask.getCommandToExecute();
            outputFile = conversionTask.getOutPutFile();
            inputFile = conversionTask.getFileName();
            eventBus.post(new TranscodingFileEvent(inputFile));
            final CommandLine cmdLine = CommandLine.parse(command);
            logger.info("Executing FFMPEG command " + command);

            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleWithFixedDelay(new TranscoderStatusUpdator(),100,1000,TimeUnit.MILLISECONDS);
            final DefaultExecutor executor = new DefaultExecutor();
            PumpStreamHandler psh = new PumpStreamHandler(new ExecLogHandlerInner(conversionTask.getDuration())/*, new ExecLogHandler(logger, Level.ERROR)*/);
            executor.setStreamHandler(psh);
            executor.setWatchdog(executeWatchdog);
            try {
                executor.execute(cmdLine);
            } catch (Exception e) {
                logger.error("INSIDE EXCEPTION " + e.getMessage());
            }
            executor.wait();
        } catch (Exception e) {
            logger.error("caught exception " + e);
        } finally {
            try{
            scheduledExecutorService.shutdown();
            scheduledExecutorService.awaitTermination(10,TimeUnit.MILLISECONDS);
            }catch (Exception e)
            {
                //do nothing
            }
            scheduledExecutorService = null;
            File file = new File(outputFile.replace("\"", ""));
            if (file.exists() && file.length() > 200) {
                logger.error("FFMPEG CONVERSION SUCCESS FOR FILE " + outputFile);
                final String test = inputFile;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        eventBus.post(new TrancodeCompleteEvent(test));
                    }
                });
            } else {
                logger.error("FFMPEG CONVERSION FAILED FOR FILE " + outputFile);
                //transocde failed
                final String test = inputFile;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        eventBus.post(new TranscodeFailEvent(test));
                    }
                });
            }
        }
    }



    private class TranscoderStatusUpdator implements Runnable {

        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    eventBus.register(this);
                    eventBus.post(new TranscodeStatusUpdateEvent(progressPecentage));                }
            });
        }
    }

    public class ExecLogHandlerInner extends MyLogOutPutStream {

        private String calc;
        int start = 0;
        private final String time = "time=";
        private double totalDuration;

        public ExecLogHandlerInner(double duration){
            this.totalDuration =  duration;
        }

        @Override
        protected void processLine(String line, int level) {
       /* switch (level) {
            case Level.ERROR_INT:
                logger.info(line);
                break;
            case Level.INFO_INT:
            default:
                logger.error(line);
                break;
        }*/
            //System.out.println("^^" + line);
        }

        @Override
        protected void processLine(String line) {
            logger.info(line);
            if(line.contains(time))
            {
                try{
                    start = line.lastIndexOf(time)+time.length();
                    calc = line.substring(start,start+12);
                    // got time here parse and convert to actual time in seconds and send realtime time events


                    String[] hms = calc.split(":");
                    Double duration = Integer.parseInt(hms[0]) * 3600
                            + Integer.parseInt(hms[1]) * 60
                            + Double.parseDouble(hms[2]);

                    progressPecentage = duration/totalDuration;
                }catch (Exception e){
                    logger.error("caught here "+e.getMessage());
                }
            }
        }
    }





}
