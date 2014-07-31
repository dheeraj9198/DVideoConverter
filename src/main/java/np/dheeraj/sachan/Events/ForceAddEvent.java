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
    private int index;
    private String fileName;
    private File file;

    public ForceAddEvent(int index, String fileName, File file) {
        this.index = index;
        this.fileName = fileName;
        this.file = file;
    }

    public int getIndex() {
        return index;
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }
}
