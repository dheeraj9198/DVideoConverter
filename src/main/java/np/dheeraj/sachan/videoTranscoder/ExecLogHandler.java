package np.dheeraj.sachan.videoTranscoder;

import org.apache.commons.exec.LogOutputStream;
import org.apache.log4j.Level;
import org.slf4j.Logger;

/**
 * Created by sujeet on 12/16/13.
 */
public class ExecLogHandler extends LogOutputStream {
    private Logger logger;

    public ExecLogHandler(Logger logger, Level level) {
        super(level.toInt());
        this.logger = logger;
    }

    @Override
    protected void processLine(String line, int level) {
        switch (level) {
            case Level.ERROR_INT:
                logger.info(line);
                break;
            case Level.INFO_INT:
            default:
                logger.error(line);
                break;
        }


    }
}
