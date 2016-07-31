import java.util.HashMap;
import java.util.Map;
import java.net.HttpURLConnection;
import org.json.JSONObject;

/**
 * Created by benjamingarner on 30/07/2016.
 */
public class manifestretrieval {
    private Map<String,String> properties = new HashMap<String, String>();
    networkingcalls netCaller = new networkingcalls();
    netidentificationitems netItems = new netidentificationitems();

    public void updateManifest() throws Exception {

        HttpURLConnection con = netCaller.initiateHttpGet(netItems.getManifestUrl());
        con.addRequestProperty("X-API-Key",netItems.getApiKey());
        JSONObject manifestResponse = netCaller.convertResponseToJson(con);
        String version = manifestResponse.getJSONObject("Response").get("version").toString();
        System.out.println(version);
        String worldSqlContentLoc = manifestResponse.getJSONObject("Response").getJSONObject("mobileWorldContentPaths").get("en").toString();
        System.out.println(worldSqlContentLoc);
    }


}
