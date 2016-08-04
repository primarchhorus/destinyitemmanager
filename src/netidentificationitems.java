/**
 * Created by benjamingarner on 30/07/2016.
 */
public class netidentificationitems {
    private String MANIFEST_URL = "https://www.bungie.net/Platform/Destiny/Manifest/";
    private String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10; rv:33.0) Gecko/20100101 Firefox/33.0";
    private String X_API_KEY = "cb0245faf3734c5286db4ccde99f2076";
    private String BUNGO_LOGIN_URL = "https://www.bungie.net/en/User/SignIn/";
    private String BASE_BUNGO_URL = "http://www.bungie.net/";
    private String BURL_NO_LOC = "/User/SignIn/";
    private int TYPE_XBOX = 1;
    private int TYPE_PSN = 2;
    private String XBOX = "Xuid";
    private int PROVIDER = 2;
    private String PSN = "Psnid";

    public String getUserAgent()     { return USER_AGENT; }
    public String getApiKey()        { return X_API_KEY; }
    public String getManifestUrl()   { return MANIFEST_URL;}
    public String getBungoLoginUrl() { return BUNGO_LOGIN_URL;}
    public String getXboxIdString()  { return XBOX; }
    public String getPsnIdString()   { return PSN; }
    public String getBaseBungoUrl()  { return BASE_BUNGO_URL; }
    public int getPsnIdNumber()      { return TYPE_PSN; }
    public int getXboxIdNumber()     { return TYPE_XBOX; }
    public int getProviderId()       { return PROVIDER; }
}
