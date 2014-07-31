package np.dheeraj.sachan.helper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Aurus
 * Date: 1/21/14
 * Time: 3:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class MakeDirIfNotExists {

    private static final Logger logger = LoggerFactory.getLogger(MakeDirIfNotExists.class);

    public static void makeDirIfNotExists(String dirName)
    {
        if (StringUtils.isBlank(dirName)) {
            logger.error("blank recording folder name");
            return;
        }

        File directory = new File(dirName);
        if (!directory.exists()) {
            try {
                if (directory.mkdirs()) {
                    logger.info("local recording folder was not existing ,  but created successfully");
                }
            } catch (SecurityException e) {
                logger.error(" Local Recording Folder Does Not Exist And Program Cannot Create " + e);
            }
        }

    }
}
