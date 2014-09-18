package VideoConverter;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Aurus
 * Date: 2/4/14
 * Time: 7:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppConfig {
    private final String loginUrl = "desktopTranscoder/login";
    private final String addLectureUrl = "desktopTranscoder/addLecture";
    private final String onTranscodeStartUrl = "desktopTranscoder/onTranscodeStart";
    private final String getOnTranscodeCompleteUrl = "desktopTranscoder/onTranscodeComplete";
    private final String onUploadStartUrl = "studio/onUploadStart";
    private final String onUploadCompleteUrl = "studio/onUploadComplete";
    private final String updateUrl = "desktopTranscoder/checkUpdate";
    private final String deleteLectureUrl = "desktopTranscoder/deleteLecture";
    private final String deleteUnfinishedLecturesUrl = "desktopTranscoder/deleteUnfinishedLectures";
    private final String errorTranscodingUrl = "desktopTranscoder/errorTranscoding";


    public static final String videoExtension  = ".mp4";
    public static final String version = "1.0-iteen";

    private String apiUrl;
    private String commonFolder;

    public String getTempDir()
    {
      return commonFolder + File.separator + "AurusTemp" +File.separator;
    }

    public String getFfmpegPath()
    {
      return "\""+commonFolder +File.separator + "dheeraj.exe\"";
    }

    public String getVersion()
    {
        return version;
    }

    public String getErrorTranscodingUrl()
    {
        return apiUrl + errorTranscodingUrl;
    }

    public String getDeleteLectureUrl()
    {
        return apiUrl + deleteLectureUrl;
    }
    public String getDeleteUnfinishedLecturesUrl()
    {
        return apiUrl + deleteUnfinishedLecturesUrl;
    }

    public String getAddLectureUrl() {
        return apiUrl + addLectureUrl;
    }

    public String getUpdateUrl()
    {
        return apiUrl + updateUrl;
    }

    public String getFtpUrl()
    {
        String str = apiUrl.replace("http", "ftp");
        return str;
    }

    public String getOnTranscodeStartUrl() {
        return apiUrl+onTranscodeStartUrl;
    }

    public String getGetOnTranscodeCompleteUrl() {
        return apiUrl+getOnTranscodeCompleteUrl;
    }

    public String getOnUploadStartUrl() {
        return apiUrl+onUploadStartUrl;
    }

    public String getOnUploadCompleteUrl() {
        return apiUrl+onUploadCompleteUrl;
    }

    public String getCommonFolder() {
        return commonFolder;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getLoginUrl() {
        return this.apiUrl + this.loginUrl;
    }

    public void setCommonFolder(String commonFolder) {
        this.commonFolder = commonFolder;
    }

}
