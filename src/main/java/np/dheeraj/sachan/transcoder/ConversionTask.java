package np.dheeraj.sachan.transcoder;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: windows 7
 * Date: 7/29/14
 * Time: 6:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConversionTask implements Comparable,Serializable,Cloneable{
    private String fileName;
    private String videoBitrate;
    private String audioBitrate;
    private String frameSize;
    private String outPutFolder;

    public String getOutPutFolder() {
        return outPutFolder;
    }

    public String getFileName() {
        return fileName;
    }

    public String getVideoBitrate() {
        return videoBitrate;
    }

    public String getAudioBitrate() {
        return audioBitrate;
    }

    public String getFrameSize() {
        return frameSize;
    }

    public ConversionTask(String fileName, String videoBitrate, String audioBitrate, String frameSize,String outPutFolder) {

        this.fileName = fileName;
        this.videoBitrate = videoBitrate;
        this.audioBitrate = audioBitrate;
        this.frameSize = frameSize;
        this.outPutFolder = outPutFolder;
    }

    @Override
    public String toString() {
        return "ConversionTask{" +
                "fileName='" + fileName + '\'' +
                ", videoBitrate='" + videoBitrate + '\'' +
                ", audioBitrate='" + audioBitrate + '\'' +
                ", frameSize='" + frameSize + '\'' +
                ", outPutFolder='" + outPutFolder + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
