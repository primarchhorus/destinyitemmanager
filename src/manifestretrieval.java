import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/**
 * Created by benjamingarner on 30/07/2016.
 */
public class manifestretrieval {
    private Map<String,String> properties = new HashMap<String, String>();
    networkingcalls netCaller = new networkingcalls();
    netidentificationitems netItems = new netidentificationitems();

    public void updateManifest() throws Exception {
        properties.put("User-Agent", netItems.getUserAgent());
        properties.put("X-API-KEY", netItems.getApiKey());
        JSONObject manifestResponse = netCaller.httpGet(netItems.getManifestUrl(),properties).toString());
    }


}
