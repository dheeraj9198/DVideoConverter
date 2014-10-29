package np.dheeraj.sachan.Events;

/**
 * Created with IntelliJ IDEA.
 * User: windows 7
 * Date: 10/29/14
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class TranscodeStatusUpdateEvent {
    private double percent;

    public TranscodeStatusUpdateEvent(double percent) {
        this.percent = percent;
    }

    public double getPercent() {
        return percent;
    }
}
