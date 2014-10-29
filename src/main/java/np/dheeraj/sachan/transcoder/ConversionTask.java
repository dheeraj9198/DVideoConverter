package np.dheeraj.sachan.transcoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: windows 7
 * Date: 7/29/14
 * Time: 6:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConversionTask implements Comparable, Serializable, Cloneable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConversionTask.class);
    private String fileName;
    private String videoBitrate;
    private String audioBitrate;
    private String frameSize;
    private String outPutFile;
    private String crf;
    private boolean crfEnabled;
    private String videoCodec;
    private String audioCodec;

    private double duration;


    public ConversionTask(String fileName, String videoBitrate, String audioBitrate, String frameSize, String outPutFile, String crf, boolean crfEnabled, String videoCodec, String audioCodec) {
        this.fileName = fileName;
        this.videoBitrate = videoBitrate;
        this.audioBitrate = audioBitrate;
        this.frameSize = frameSize;
        this.outPutFile = outPutFile;
        this.crf = crf;
        this.crfEnabled = crfEnabled;
        this.videoCodec = videoCodec;
        this.audioCodec = audioCodec;
        this.duration = getDuration(fileName);
    }

    public double getDuration()
    {
        return duration;
    }

    private static double getDuration(String fileName) {
        try {
            ProcessBuilder pb = new ProcessBuilder("\"C:\\Program Files\\DVideoConverter\\dheeraj.exe\"", "-i", fileName);
            final Process p = pb.start();
            Scanner sc = new Scanner(p.getErrorStream());
            // Find duration
            Pattern durPattern = Pattern.compile("(?<=Duration: )[^,]*");
            String dur = sc.findWithinHorizon(durPattern, 0);
            if (dur == null) {
                return Double.MAX_VALUE;
            }
            String[] hms = dur.split(":");
            Double duration = Integer.parseInt(hms[0]) * 3600
                    + Integer.parseInt(hms[1]) * 60
                    + Double.parseDouble(hms[2]);

            return duration;

        } catch (IOException e) {
            LOGGER.error("Caught IOException while estimating fileSize");
            return Double.MAX_VALUE;
        }

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

    public String getOutPutFile() {

        return outPutFile;
    }

    @Override
    public int compareTo(Object o) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getCommandToExecute() {
        if (crfEnabled) {
            return "\"C:\\Program Files\\DVideoConverter\\dheeraj.exe\" -i " + fileName + " -vcodec " + videoCodec + " -acodec " + audioCodec + " -b:a " + audioBitrate.replace(" ", "") + "k -s " + frameSize + " -crf " + crf + " -y " + outPutFile;
        } else {
            return "\"C:\\Program Files\\DVideoConverter\\dheeraj.exe\" -i " + fileName + " -vcodec " + videoCodec + " -acodec " + audioCodec + " -b:a " + audioBitrate.replace(" ", "") + "k -s " + frameSize + " -b:v " + videoBitrate.replace(" ", "") + "k -y " + outPutFile;
        }
    }
}
