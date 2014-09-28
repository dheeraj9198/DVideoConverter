package np.dheeraj.sachan.videoTranscoder;

import org.apache.commons.exec.LogOutputStream;
import org.apache.log4j.Level;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * Created by dheeraj on 12/16/13.
 */
public class ExecLogHandler extends LogOutputStream {
    private Logger logger;
    private boolean skip;
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
        System.out.println(line);
    }

    @Override
    public void write(final int cc) throws IOException {
        final byte c = (byte) cc;
        System.out.println("***********"+cc+"");
    }
}
