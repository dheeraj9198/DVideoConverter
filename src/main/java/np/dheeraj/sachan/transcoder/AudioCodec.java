package np.dheeraj.sachan.transcoder;

/**
 * Created with IntelliJ IDEA.
 * User: windows 7
 * Date: 9/13/14
 * Time: 1:24 AM
 * To change this template use File | Settings | File Templates.
 */
public enum AudioCodec {
    MP3("libmp3lame"),
    AAC("aac -strict -2 ");

    private String audioCodec;

    AudioCodec(String audioCodec)
    {
        this.audioCodec = audioCodec;
    }

    public String getVideoCodec()
    {
        return audioCodec;
    }


    public static String  getCodec(String codec)
    {
        for(AudioCodec audioCodec1 : AudioCodec.values())
        {
            if(audioCodec1.name().equals(codec))
            {
                return  audioCodec1.audioCodec;
            }
        }
        return MP3.audioCodec;
    }



}
