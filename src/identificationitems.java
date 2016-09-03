/**
 * Created by benjamingarner on 30/07/2016.
 */
public class identificationitems {
    static private String MANIFEST_URL = "https://www.bungie.net/Platform/Destiny/Manifest/";
    static private String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10; rv:33.0) Gecko/20100101 Firefox/33.0";
    static private String X_API_KEY = "cb0245faf3734c5286db4ccde99f2076";
    static private String PSN_BASE = "https://auth.api.sonyentertainmentnetwork.com/login.do";
    static private String BUNGO_LOGIN_URL = "https://www.bungie.net/en/User/SignIn/";
    static private String BASE_BUNGO_URL = "http://www.bungie.net/";
    static private String BURL_NO_LOC = "/User/SignIn/";
    static private String manifestLocation = "manifest_version/";
    static private String databseFileName = "destiny_sqlite_database.db";
    static private String configFileName = "destiny_manager_config.json";
    static private String configLocation = "config/";
    static private int TYPE_XBOX = 1;
    static private int TYPE_PSN = 2;
    static private String XBOX = "Xuid";
    static private int PROVIDER = 2;
    static private String PSN = "Psnid";

    public String getUserAgent()            { return USER_AGENT; }
    public String getApiKey()               { return X_API_KEY; }
    public String getManifestUrl()          { return MANIFEST_URL;}
    public String getBungoLoginUrl()        { return BUNGO_LOGIN_URL;}
    public String getXboxIdString()         { return XBOX; }
    public String getPsnIdString()          { return PSN; }
    public String getBaseBungoUrl()         { return BASE_BUNGO_URL; }
    public int getPsnIdNumber()             { return TYPE_PSN; }
    public int getXboxIdNumber()            { return TYPE_XBOX; }
    public int getProviderId()              { return PROVIDER; }
    public String getManifestDirectoryLoc() { return manifestLocation; }
    public String getDatabaseFileName()     { return databseFileName; }
    public String getConfigFileName()       { return configFileName; }
    public String getConfigLocation()       { return configLocation; }
    public String getPsnLoginUrl()          { return PSN_BASE; }
}
