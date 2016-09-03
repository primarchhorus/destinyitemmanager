import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;


/**
 * Created by benjamingarner on 30/07/2016.
 */
public class networkingcalls {

    identificationitems inedtItems = new identificationitems();
    Logger logger = LogManager.getLogger(manifestretrieval.class);
    utilities util = new utilities();

    public JSONObject connectAndReadHttpUrlConnection(String urlString, HashMap<String,String> properties) throws IOException, JSONException {
        //Start url connection
        URL obj = new URL(urlString);
        logger.info(urlString);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        for(String key: properties.keySet()){
            con.addRequestProperty(key,properties.get(key));
        }
        con.setRequestMethod("GET");
        JSONObject jsonObj = null;


        if(con.getResponseCode() != HttpURLConnection.HTTP_OK)
        {
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

        util.print(result);
        //Convert to json object and return
//        jsonObj = new JSONObject(result.toString());
        return jsonObj;
    }

//    public void httpPost() throws Exception {
//
//    }


    public HashMap downloadManifest(String manifestUrl) throws IOException {
        String saveFilePath = null;
        HashMap fileNames = new HashMap<String,String>();

        if(util.setupManifestLocation())
        {
            URL url = new URL(manifestUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            int responseCode = con.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                // extract file name from header
                String fileNamePath = con.getHeaderField("X-SelfUrl");
                String[] selfUrlHeaderField = fileNamePath.split("/");
                String fileNameContent = selfUrlHeaderField[selfUrlHeaderField.length - 1];

                // opens input stream from the HTTP connection
                InputStream inputStream = con.getInputStream();
                saveFilePath = inedtItems.getManifestDirectoryLoc()+fileNameContent+".zip";

                fileNames.put("unzipped", inedtItems.getManifestDirectoryLoc()+fileNameContent);
                fileNames.put("zipped",saveFilePath);
                // opens an output stream to save into file
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                int bytesRead;
                byte[] buffer = new byte[con.getContentLength()];
                long timeoutExpiredMs = System.currentTimeMillis() + 120000;
                boolean timedOutDownload = false;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    if (System.currentTimeMillis() >= timeoutExpiredMs) {
                        timedOutDownload = true;
                        break;
                    }
                }
                if(timedOutDownload)
                {
                    logger.warn("download timed out");
                    throw new IOException();
                }

                outputStream.close();
                inputStream.close();

            }
            else{
                logger.warn("Http response code not OK");
            }
        }
        return fileNames;
    }

    public void loginProviderAuthentication() throws IOException {

        URL obj = new URL(inedtItems.getBungoLoginUrl()+inedtItems.getPsnIdString());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setInstanceFollowRedirects(false);
        con.addRequestProperty("User-Agent", inedtItems.getUserAgent());

        con.setRequestMethod("GET");
        String newLoc = con.getHeaderField("Location");
        util.print(newLoc);

        con = (HttpURLConnection) new URL(newLoc).openConnection();
        con.setInstanceFollowRedirects(false);
        con.addRequestProperty("User-Agent", inedtItems.getUserAgent());
        newLoc = con.getHeaderField("Location");
        util.print(newLoc);

    }
}
