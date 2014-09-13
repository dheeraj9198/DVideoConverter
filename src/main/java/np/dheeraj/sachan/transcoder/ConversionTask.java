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
    private String videoCodec;
    private String audioCodec;

    public ConversionTask(String fileName, String videoBitrate, String audioBitrate, String frameSize, String outPutFile, String crf, boolean crfEnabled,String videoCodec,String audioCodec) {
        this.fileName = fileName;
        this.videoBitrate = videoBitrate;
        this.audioBitrate = audioBitrate;
        this.frameSize = frameSize;
        this.outPutFile = outPutFile;
        this.crf = crf;
        this.crfEnabled = crfEnabled;
        this.videoCodec = videoCodec;
        this.audioCodec = audioCodec;
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
                ", crf='" + crf + '\'' +
                ", crfEnabled=" + crfEnabled +
                ", videoCodec='" + videoCodec + '\'' +
                ", audioCodec='" + audioCodec + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getCommandToExecute() {
        if(crfEnabled)
        {
            return "\"C:\\Program Files\\DVideoConverter\\dheeraj.exe\" -i "+fileName+" -vcodec "+videoCodec+" -acodec "+audioCodec+" -b:a "+audioBitrate.replace(" ","")+"k -s " +frameSize+" -crf "+crf+" -y "+outPutFile;
        }   else{
            return "\"C:\\Program Files\\DVideoConverter\\dheeraj.exe\" -i "+fileName+" -vcodec "+videoCodec+" -acodec "+audioCodec+" -b:a "+audioBitrate.replace(" ","")+"k -s " +frameSize+" -b:v "+videoBitrate+"k -y "+outPutFile;
        }
    }
}
