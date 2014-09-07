package np.dheeraj.sachan.Events;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Aurus
 * Date: 7/8/14
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ForceAddEvent {
    private String fileName;
    private File file;

    public ForceAddEvent(String fileName, File file) {
        this.fileName = fileName;
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }
}
