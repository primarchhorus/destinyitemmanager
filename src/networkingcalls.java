import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Created by benjamingarner on 30/07/2016.
 */
public class networkingcalls {

    public JSONObject httpGet(String urlString, Map<String,String> properties) throws Exception {
        //Start url connection
        URL obj = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        // Loop over the properties map and set the request propetries
        for (String key : properties.keySet()) {
            con.setRequestProperty(key,properties.get(key));
        }
        //Prepare stream buffers to red in response and convert to json
        InputStream in = new BufferedInputStream(con.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        //Loop over buffer add to string fro json conversion
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        //Convert to json object and return
        JSONObject jsonObj = new JSONObject(result.toString());
        return jsonObj;
    }

    public void httpPost() throws Exception {

    }
}
