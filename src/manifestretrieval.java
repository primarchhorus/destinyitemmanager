import java.util.HashMap;
import java.util.Map;
import java.net.HttpURLConnection;
import org.json.JSONObject;

/**
 * Created by benjamingarner on 30/07/2016.
 */
public class manifestretrieval {
    networkingcalls netCaller = new networkingcalls();
    netidentificationitems netItems = new netidentificationitems();

    public void updateManifest() throws Exception {
        HashMap properties = new HashMap<String,String>();
        properties.put("X-API-Key", netItems.getApiKey().toString());
        JSONObject manifestResponse = new JSONObject();
        try{
            manifestResponse = netCaller.connectAndReadHttpUrlConnection(netItems.getManifestUrl(),properties);
        } catch(Exception ex) {
            System.out.println("failed to initiate manifest update");
//            ex.printStackTrace();
        }

        if(manifestResponse != null)
        {
            String version = manifestResponse.getJSONObject("Response").get("version").toString();
            System.out.println(version);
            String worldSqlContentLoc = manifestResponse.getJSONObject("Response").getJSONObject("mobileWorldContentPaths").get("en").toString();
            System.out.println(worldSqlContentLoc);
            netCaller.downloadManifest(netItems.getBaseBungoUrl()+worldSqlContentLoc);
        }
    }
}
