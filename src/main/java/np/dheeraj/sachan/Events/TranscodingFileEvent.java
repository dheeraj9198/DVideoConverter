package np.dheeraj.sachan.Events;


/**
 * Created with IntelliJ IDEA.
 * User: Aurus
 * Date: 2/17/14
 * Time: 1:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class TranscodingFileEvent {
    private String inPutFile;

    public TranscodingFileEvent(String inPutFile) {
        this.inPutFile = inPutFile;
    }

    public String getInPutFile() {
        return inPutFile;
    }
}
