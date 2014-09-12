package np.dheeraj.sachan.transcoder;

/**
 * Created with IntelliJ IDEA.
 * User: windows 7
 * Date: 9/13/14
 * Time: 1:18 AM
 * To change this template use File | Settings | File Templates.
 */
public enum VideoCodec {
    H264("libx264"),
    MPEG2("mpeg2video"),
    MPEG4("mpeg4");

    private String videoCodec;

    VideoCodec(String videoCodec)
    {
        this.videoCodec = videoCodec;
    }

    public String getVideoCodec()
    {
        return videoCodec;
    }

    public static String  getCodec(String codec)
    {
        for(VideoCodec videoCodec1 : VideoCodec.values())
        {
            if(videoCodec1.name().equals(codec))
            {
                return  videoCodec1.videoCodec;
            }
        }
        return H264.videoCodec;
    }
}
