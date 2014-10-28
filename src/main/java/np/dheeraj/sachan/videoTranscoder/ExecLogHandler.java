package np.dheeraj.sachan.videoTranscoder;

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
    public ExecLogHandler(Logger logger, Level level) {
        super(level.toInt());
        this.logger = logger;
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
        System.out.println("^^" + line);
    }

    @Override
    protected void processLine(String line) {
        //logger.info(line);
        if(line.contains(time))
        {
            try{
                start = line.lastIndexOf(time)+time.length();
          calc = line.substring(start,start+12);
          logger.info("*+*"+calc);
                //TODO got time here parse and convert to actual time in seconds and send realtime time events
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
