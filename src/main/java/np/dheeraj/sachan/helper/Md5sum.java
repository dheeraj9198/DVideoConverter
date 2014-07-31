package np.dheeraj.sachan.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Created with IntelliJ IDEA.
 * User: dhirajs
 * Date: 24/10/13
 * Time: 9:52 PM
 */
public class Md5sum {
    private static final Logger logger = LoggerFactory.getLogger(Md5sum.class);
    private static byte[] createChecksum(String filename) throws Exception {
        InputStream fis =  new FileInputStream(filename);

        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }

    public static String getMD5Checksum(String absolutePath) {

        try{
        byte[] b = createChecksum(absolutePath);
        String result = "";

        for (byte aB : b) {
            result += Integer.toString((aB & 0xff) + 0x100, 16).substring(1);
        }
        return result;
        }catch (Exception e)
        {
            logger.error("Caught Exception "+e);
            return "";
        }
    }
}
