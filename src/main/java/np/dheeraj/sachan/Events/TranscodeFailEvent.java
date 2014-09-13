package np.dheeraj.sachan.Events;


/**
 * Created with IntelliJ IDEA.
 * User: Aurus
 * Date: 4/3/14
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class TranscodeFailEvent {
    private String outPutFile;
    public TranscodeFailEvent(String outPutFile)
    {
        this.outPutFile = outPutFile;
    }

    public String getOutPutFile() {
        return outPutFile;
    }
}
