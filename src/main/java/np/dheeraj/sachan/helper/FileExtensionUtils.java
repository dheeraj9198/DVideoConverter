package np.dheeraj.sachan.helper;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: dheeraj
 * Date: 12/3/13
 * Time: 10:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileExtensionUtils {

    private FileExtensionUtils() {
    }

    public static String stripFileExtension(String fileName) {
        int dotInd = fileName.lastIndexOf('.');
        // if dot is in the first position,
        // we are dealing with a hidden file rather than an extension
        return (dotInd > 0) ? fileName.substring(0, dotInd) : fileName;
    }

    public static String getFilePathPrefix(String filepath) {
        int separatorIndex = filepath.lastIndexOf(File.separator);
        // if dot is in the first position,
        // we are dealing with a hidden file rather than an extension
        return (separatorIndex > 0) ? filepath.substring(0, separatorIndex) : ".";
    }

    /*Strip query params from streamname
     *
     * eg: input = abc.flv?courseId=4&name=hello
     * output = abc.flv
     * */
    public static String removeQueryParams(String outputRtmpStream) {
        int separatorIndex = outputRtmpStream.indexOf("?");
        // if dot is in the first position,
        // we are dealing with a hidden file rather than an extension
        return (separatorIndex > 0) ? outputRtmpStream.substring(0, separatorIndex) : outputRtmpStream;
    }
}
