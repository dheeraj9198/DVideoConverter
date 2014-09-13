package np.dheeraj.sachan.Events;


/**
 * Created with IntelliJ IDEA.
 * User: Aurus
 * Date: 4/3/14
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class TranscodeFailEvent {
    private String inPutFile;

    public TranscodeFailEvent(String inPutFile) {
        this.inPutFile = inPutFile;
    }

    public String getInPutFile() {
        return inPutFile;
    }
}
