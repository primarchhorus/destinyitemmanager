import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.FileOutputStream;
import java.util.*;

/**
 * Created by benjamingarner on 30/07/2016.
 */
public class networkingcalls {

    public JSONObject connectAndReadHttpUrlConnection(String urlString, HashMap<String,String> properties) throws Exception {
        //Start url connection
        URL obj = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        for(String key: properties.keySet()){
            con.addRequestProperty(key,properties.get(key));
        }
        con.setRequestMethod("GET");
        JSONObject jsonObj;

        if(con.getResponseCode() != HttpURLConnection.HTTP_OK)
        {
            System.out.println(con.getResponseCode());
            return null;
        }
        InputStream in = new BufferedInputStream(con.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        //Loop over buffer add to string for json conversion
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        //Convert to json object and return
        jsonObj = new JSONObject(result.toString());
        return jsonObj;
    }

    public void httpPost() throws Exception {

    }

    public boolean downloadManifest(String manifestUrl) throws Exception {
        boolean returnVal = false;
        URL url = new URL(manifestUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        int responseCode = con.getResponseCode();

        if(responseCode == HttpURLConnection.HTTP_OK)
        {
            System.out.println(con.getHeaderFields());
            System.out.println(con.getHeaderField("X-SelfUrl"));
            System.out.println(con.getContentLength());
            System.out.println(con.getContentType());
//            System.out.println(convertResponseToJson(con));

            // opens input stream from the HTTP connection
            InputStream inputStream = con.getInputStream();
            String saveFilePath = "testfile.content";

            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead;
            byte[] buffer = new byte[con.getContentLength()];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();
            returnVal = true;
        }
        return returnVal;
    }
}
