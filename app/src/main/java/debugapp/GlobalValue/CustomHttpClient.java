package debugapp.GlobalValue;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CustomHttpClient {

    /**
     * The time it takes for our client to timeout
     */

    // public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds
    private static final int HTTP_TIMEOUT = 5 * 60 * 1000; // milliseconds

    /**
     * Single instance of our HttpClient
     */
    private static HttpClient mHttpClient;

    /**
     * Get our single instance of our HttpClient object.
     *
     * @return an HttpClient object with connection parameters set
     * snsoftindore
     */

    private static HttpClient getHttpClient() {

        if (mHttpClient == null) {
            mHttpClient = new DefaultHttpClient();

            final HttpParams params = mHttpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
            ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
        }
        return mHttpClient;
    }

    public static String executeHttpPost(String url) throws Exception {
        BufferedReader in = null;
        try {
            HttpClient client = getHttpClient();
            HttpPost request = new HttpPost(url);
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer();
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            String result = sb.toString();
            return result;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Performs an HTTP Post request to the specified url with the specified
     * parameters.
     *
     * @param url            The web address to post the request to
     * @param postParameters The parameters to send via the request
     * @return The result of the request
     * @throws Exception
     */
    public static String executeHttpPost(String url, ArrayList<NameValuePair> postParameters) throws Exception {

        BufferedReader in = null;

        try {

            HttpClient client = getHttpClient();

            HttpPost request = new HttpPost(url);

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);

            request.setEntity(formEntity);

            HttpResponse response = client.execute(request);

            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer();

            String line = "";

            String NL = System.getProperty("line.separator");

            while ((line = in.readLine()) != null) {

                sb.append(line + NL);

            }

            in.close();


            String result = sb.toString();

            return result;

        } finally {

            if (in != null) {

                try {

                    in.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }

    }

    public static String executeHttpPost1(String url, ArrayList<NameValuePair> postParameters) throws Exception {

        Log.d("output_exec", "check");

        BufferedReader reader = null;

        JSONObject jObj = new JSONObject();
        String result = "";

        System.out.println("URL comes in jsonparser class is:  " + url);
        Log.e("url is....", url+"");
        Log.e("params  is....", postParameters+"");

        try {

            int TIMEOUT_MILLISEC = 150000; // = 12 seconds
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams,
                    TIMEOUT_MILLISEC);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            int status = httpResponse.getStatusLine().getStatusCode();

            InputStream is = httpResponse.getEntity().getContent();
            reader = new BufferedReader(new InputStreamReader(
                    is, StandardCharsets.ISO_8859_1), 8);

            StringBuilder sb = new StringBuilder();

            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");

            }


            is.close();

            result = sb.toString();

            Log.e("IMAGE","&&&"+result);

            return result;

        } finally {

            if (reader != null) {

                try {

                    reader.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }

    }

}
