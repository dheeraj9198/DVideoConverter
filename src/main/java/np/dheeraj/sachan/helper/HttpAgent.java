package np.dheeraj.sachan.helper;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sujeet
 * Date: 11/29/13
 * Time: 12:59 AM
 * To change this template use File | Settings | File Templates.
 */
public final class HttpAgent {
    private static final Logger logger = LoggerFactory
            .getLogger(HttpAgent.class);

    private HttpAgent() {
    }

    /**
     * makes a get request to a url
     *
     * @param url URL
     * @return String response from the url
     */
    public static final String get(String url) {
        String response = null;
        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        try {
            HttpGet httpGet = new HttpGet(url);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response = httpClient.execute(httpGet, responseHandler);
        } catch (Exception e) {
            logger.error("HttpClient Exception:" + e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        logger.debug("response from api on get: " + response + ", url=" + url);
        return response;
    }

    /**
     * makes a post request to a url with some data
     *
     * @param url    String url
     * @param params List of name:value pairs to sent with the request as post data
     * @return String response of the post request
     */
    public static final String post(String url, List<NameValuePair> params) {
        String response = null;
        logger.info("sending post reqeust={}, params={}", url, params);
        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response = httpClient.execute(httpPost, responseHandler);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        logger.debug("response from api on post: " + response + ", url=" + url);
        return response;
    }
}

