package np.dheeraj.sachan.videoTranscoder;

import com.google.common.eventbus.EventBus;
import org.apache.commons.exec.*;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: sujeet
 * Date: 12/4/13
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */

public class FfmpegCommandLine {
   /* private EventBus eventBus;
    private Device device;

    public void setEventBus(EventBus eventBus)
    {
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    public void setDevice(Device device)
    {
        this.device = device;
    }
    //public static final String FMLE = "FMLECmd.exe";
    public static final long SEC_TO_NANO_SEC = 1000000000L;
    private static final Logger logger = LoggerFactory.getLogger(FfmpegCommandLine.class);
    //private ExecuteWatchdog startCmdWatchdog;// = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
    private StartCmdResultHandler startCmdResultHandler;

    public void startFfmpegCommand(String command) throws IOException {
        eventBus.post(new TranscodingFileEvent(device.getBareFileName()));
        logger.info("Running start ffmpeg {}", command);
        CommandLine cmdLine = CommandLine.parse(command);

        DefaultExecutor executor = new DefaultExecutor();
        ExecuteWatchdog startCmdWatchdog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
        startCmdResultHandler = new StartCmdResultHandler(startCmdWatchdog);

        PumpStreamHandler psh = new PumpStreamHandler(new ExecLogHandler(logger, Level.INFO), new ExecLogHandler(logger, Level.ERROR));
        executor.setStreamHandler(psh);
        executor.execute(cmdLine, startCmdResultHandler);
        //resultHandler.waitFor();
    }

    public void stopFfmpegCommand(){
        logger.info("Running stop ffmpeg command {}");
       *//* CommandLine cmdLine = CommandLine.parse(command);

        DefaultExecutor executor = new DefaultExecutor();
        ExecuteWatchdog sotpCmdWatchdog = new ExecuteWatchdog(15000);
        int exitValue = executor.execute(cmdLine);
        *//**//*Now we should wait for FMLECmd Process to stop
          NOTE : FMLECmd is started by another commandline. So we have to wait for another process
         *//**//*
        long startTime = System.nanoTime();
        long elapsedTime = 0L;
        long timeoutInNano = 90L * SEC_TO_NANO_SEC;   //
        while (!startCmdResultHandler.hasResult()) {
            Thread.sleep(1000);
            elapsedTime = System.nanoTime() - startTime;
            if (elapsedTime > timeoutInNano) {
                break;
            }
        }

        if (startCmdResultHandler.hasResult()) {
            //FMLE stopped normally
        } else {
            startCmdResultHandler.destroyProcess();
        }*//*
        startCmdResultHandler.destroyProcess();
    }

    public boolean isStartCmdRunning() {
        if (startCmdResultHandler == null) return false;
        return (!startCmdResultHandler.hasResult());
    }

    private class StartCmdResultHandler extends DefaultExecuteResultHandler {
        private ExecuteWatchdog watchdog;

        private StartCmdResultHandler(ExecuteWatchdog watchdog) {
            this.watchdog = watchdog;
        }

        public void destroyProcess() {
            watchdog.destroyProcess();
        }


        public void onProcessComplete(int exitValue) {
            super.onProcessComplete(exitValue);
            logger.info("ffmpeg command line completed");
            eventBus.post(new TrancodeCompleteEvent(device));
        }

        public void onProcessFailed(ExecuteException e) {
            super.onProcessFailed(e);
            if (watchdog != null && watchdog.killedProcess()) {
                logger.info("ffmpeg cmd line {} timed out.");
            } else {
                logger.error("ffmpeg cmd line {} stopped because of exception. {}", e);
            }
        }
    }*/
}
