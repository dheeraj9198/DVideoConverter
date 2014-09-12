package np.dheeraj.sachan.transcoder;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: windows 7
 * Date: 7/29/14
 * Time: 6:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConversionTask implements Comparable, Serializable, Cloneable {
    private String fileName;
    private String videoBitrate;
    private String audioBitrate;
    private String frameSize;
    private String outPutFile;
    private String crf;
    private boolean crfEnabled;

    public ConversionTask(String fileName, String videoBitrate, String audioBitrate, String frameSize, String outPutFile, String crf, boolean crfEnabled) {
        this.fileName = fileName;
        this.videoBitrate = videoBitrate;
        this.audioBitrate = audioBitrate;
        this.frameSize = frameSize;
        this.outPutFile = outPutFile;
        this.crf = crf;
        this.crfEnabled = crfEnabled;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return "ConversionTask{" +
                "fileName='" + fileName + '\'' +
                ", videoBitrate='" + videoBitrate + '\'' +
                ", audioBitrate='" + audioBitrate + '\'' +
                ", frameSize='" + frameSize + '\'' +
                ", outPutFile='" + outPutFile + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getCommandToExecute() {
        return "";
    }
}
