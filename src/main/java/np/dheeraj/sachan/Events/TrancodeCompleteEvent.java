package np.dheeraj.sachan.Events;


/**
 * Created with IntelliJ IDEA.
 * User: Aurus
 * Date: 2/17/14
 * Time: 12:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrancodeCompleteEvent {
    private String outPutFile;

    public TrancodeCompleteEvent(String outPutFile) {
        this.outPutFile = outPutFile;
    }

    public String getOutPutFile() {
        return outPutFile;
    }
}
