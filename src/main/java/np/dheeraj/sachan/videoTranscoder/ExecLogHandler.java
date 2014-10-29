package np.dheeraj.sachan.videoTranscoder;

import com.google.common.eventbus.EventBus;
import javafx.scene.control.ProgressBar;
import np.dheeraj.sachan.Events.TranscodeStatusUpdateEvent;
import org.apache.commons.exec.LogOutputStream;
import org.apache.log4j.Level;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dheeraj on 12/16/13.
 */
public class ExecLogHandler extends LogOutputStream {

    private Logger logger;
    private String calc;
    int start = 0;
    private final String time = "time=";

    private double totalDuration;
    private EventBus eventBus;
    private ProgressBar progressBar;

    public ExecLogHandler(Logger logger, Level level,double duration,EventBus eventBus1,ProgressBar progressBar1) {
        super(level.toInt());
        this.logger = logger;
        this.totalDuration =  duration;
        this.eventBus = eventBus1;
        this.progressBar = progressBar1;
        this.eventBus.register(this);
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
                //TODO got time here parse and convert to actual time in seconds and send realtime time events


                String[] hms = calc.split(":");
                Double duration = Integer.parseInt(hms[0]) * 3600
                        + Integer.parseInt(hms[1]) * 60
                        + Double.parseDouble(hms[2]);

                eventBus.post(new TranscodeStatusUpdateEvent(duration/totalDuration));
               // progressBar.setProgress(duration/totalDuration);
            }catch (Exception e){
                logger.error("caught here "+e.getMessage());
            }
        }
    }


 /*   @Override
    public void write(final int cc) throws IOException {
        //final byte c = (byte) cc;
       // System.out.println("***********"+cc+"");

        final byte c = (byte) cc;
        if ((c == '\n') || (c == '\r') || (c == ' ')) {
            processLine(buffer.toString());
            buffer.reset();
        } else {
            buffer.write(cc);
        }
    }*/
}
