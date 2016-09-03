import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Singleton to use for Bungie login, cookiestore remains the same for the session with cookies stored
 * @author MilkazarDevs
 *
 *
 *
 */
public class authentication {

    private final static String X_API_KEY = "cb0245faf3734c5286db4ccde99f2076";

    private final static int TYPE_XBOX = 1;
    private final static int TYPE_PSN = 2;

    private final static String BURL = "https://www.bungie.net/en/User/SignIn/";
    private final static String BURL_NO_LOC = "/User/SignIn/";

    private final static String PSN = "Psnid";
    private final static String PSN_LOGIN_URL = "https://auth.api.sonyentertainmentnetwork.com/login.do";

    private final static String XBOX = "Xuid";

    private final static String USERAGENT = "Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.125 Safari/537.36";
    private final static String BUNGLED_COOKIE_NAME = "bungled";
    private final static String BUNGLEME_COOKIE_NAME = "bungleme";

    private static authentication mInstance;
    private int type;

    private boolean loggedIn = false;
    private HttpURLConnection urlConnection;
    private final CookieManager cookieManager;
    private String X_CSRF = null;
    private String ACCOUNT_ID = null;

    public authentication(){
        cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
    }

    public static synchronized authentication getInstance(){
        if (mInstance == null) {
            mInstance = new authentication();
        }
        return mInstance;
    }

    public boolean checkLogin(String uname, String pword, int provider) throws IOException{
        this.type = provider;
        //check login
        checkLoginFlow(uname, pword, provider);

        if(loggedIn){
            //force cookiestore population
            urlConnection.getHeaderFields();
            //save header values
            readCookiesForHeaderValues();
            log("X_CSRF: "+X_CSRF);
            log("ACC_ID: "+ACCOUNT_ID);
        }

        printHeaders();
        printCookies();

        testLogin();

        return loggedIn;
    }

    private void checkLoginFlow(String uname, String pword, int provider) throws IOException{

        log("***********checkloginFlow started");
        //form url based on provider
        String urlProvider = provider==TYPE_XBOX ? BURL+XBOX : BURL+PSN;
        //check cookie validity
        urlConnection = (HttpURLConnection) new URL(urlProvider).openConnection();
        urlConnection.setInstanceFollowRedirects(false);
        urlConnection.addRequestProperty("User-Agent", USERAGENT);

        String newLoc = urlConnection.getHeaderField("Location");
        log("Loc1: "+newLoc);

        //Bungie cookies still valid (also check if there's a locale redirect (/en/User/SignIn/ could become fr/User/SignIn, it/User/Signin etc)
        if(newLoc==null || newLoc.endsWith(BURL_NO_LOC+(provider==TYPE_XBOX ? XBOX : PSN))){
            loggedIn = true;
            return;
        }

        urlConnection = (HttpURLConnection) new URL(newLoc).openConnection();
        urlConnection.setInstanceFollowRedirects(false);
        urlConnection.addRequestProperty("User-Agent", USERAGENT);

        if(urlConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
            //XBOX doesn't redirect, get current url
            newLoc = urlConnection.getURL().toString();
        }else{
            //psn redirects get redirection url
            newLoc = urlConnection.getHeaderField("Location");
        }
        log("Loc2: "+newLoc);
        //if it contains ?code, it's authenticated
        if(!newLoc.contains(urlProvider+"?code")){
            loggedIn = false;
            //no need to try login with uname or pword empty
            if(uname.equals("") || pword.equals("")){
                return;
            }

            switch(provider){
                case TYPE_XBOX:
                    //get xbox auth url, passing right params
                    String xboxLoginUrl = "https://login.live.com/ppsecure/post.srf?" + newLoc.substring(newLoc.indexOf("?")+1);
                    log("XboxLoginUrl: "+xboxLoginUrl);
                    //read content, need to look for input value with id i0327 to pass as parameter
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuffer content = new StringBuffer();
                    String inputLine;
                    while((inputLine = in.readLine()) != null)
                        content.append(inputLine);
                    in.close();
                    //search ppftvalue
                    Matcher matcher = Pattern.compile("id\\=\"i0327\" value\\=\"(.*?)\"\\/").matcher(content);
                    log("Content: \n"+content.toString());
                    if(matcher.find()){
                        String ppft = matcher.group(1);
                        log("Value ppft: "+ppft);

                        matcher = Pattern.compile("urlPost:'(.*?)'").matcher(content);
                        //something changed, it wasn't working no more, with this fix it works again
                        if(matcher.find()){
                            xboxLoginUrl = matcher.group(1);
                        }
                        //do request
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("PPFT", ppft);
                        map.put("login", uname);
                        map.put("passwd", pword);
                        map.put("KMSI","1");
                        String qParams = createURLEncodedQueryString(map);

                        urlConnection = (HttpURLConnection) new URL(xboxLoginUrl).openConnection();
                        urlConnection.addRequestProperty("User-Agent", USERAGENT);
                        urlConnection.setInstanceFollowRedirects(true);
                        urlConnection.setRequestMethod("POST");
                        //urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        urlConnection.setDoOutput(true);
                        urlConnection.setFixedLengthStreamingMode(qParams.getBytes().length);
                        PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                        out.print(qParams);
                        out.close();

                        //follow redirects seems not to always work here
                        newLoc = urlConnection.getHeaderField("Location");
                        if(newLoc==null){
                            newLoc = urlConnection.getURL().toString();
                        }else{
                            //do the redirect manually
                            urlConnection = (HttpURLConnection) new URL(newLoc).openConnection();
                            urlConnection.setInstanceFollowRedirects(true);
                            urlConnection.addRequestProperty("User-Agent", USERAGENT);

                            newLoc = urlConnection.getURL().toString();
                        }
                        log("XboxLoc: "+newLoc);

                        if(newLoc.contains(urlProvider+"?code")){
                            loggedIn = true;
                            return;
                        }

                        loggedIn = false;
                        return;

                    }
                    log("No match found. No login");
                    loggedIn = false;
                    return;

                case TYPE_PSN:


                    Map<String, String> map = new HashMap<String, String>();
                    map.put("params", "cmVxdWVzdF9sb2NhbGU9ZW5fVVMmcmVxdWVzdF90aGVtZT1saXF1aWQ=");
                    map.put("j_username", uname);
                    map.put("j_password", pword);
                    map.put("rememberSignIn","1");
                    String qParams = createURLEncodedQueryString(map);

                    urlConnection = (HttpURLConnection) new URL(PSN_LOGIN_URL).openConnection();
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    urlConnection.setInstanceFollowRedirects(false);
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.addRequestProperty("User-Agent", USERAGENT);
                    urlConnection.setFixedLengthStreamingMode(qParams.getBytes().length);
                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(qParams);
                    out.close();

                    newLoc = urlConnection.getHeaderField("Location");
                    log("Loc3: "+newLoc);
                    if(newLoc.contains("authentication_error")){
                        log("Auth error psn");
                        loggedIn = false;
                        return;
                    }

                    urlConnection = (HttpURLConnection) new URL(newLoc).openConnection();
                    urlConnection.setInstanceFollowRedirects(true);
                    urlConnection.addRequestProperty("User-Agent", USERAGENT);
                    urlConnection.setRequestMethod("GET");

                    newLoc = urlConnection.getHeaderField("Location");
                    log("Loc4: "+newLoc);

                    break;
            }

            if(newLoc==null){
                newLoc = urlConnection.getURL().toString();
                log("Loc4.1: "+newLoc);
            }

            if(newLoc.toLowerCase().contains("register")){
                loggedIn = false;
                return;
            }

            loggedIn = newLoc.indexOf(urlProvider) == 0;
            return;
        }

        urlConnection = (HttpURLConnection) new URL(newLoc).openConnection();
        urlConnection.setInstanceFollowRedirects(false);
        urlConnection.addRequestProperty("User-Agent", USERAGENT);

        urlConnection.connect();

        loggedIn = true;
    }

    private void readCookiesForHeaderValues() {

        Iterator<HttpCookie> cooks = cookieManager.getCookieStore().getCookies().iterator();
        while(cooks.hasNext()/* && X_CSRF==null && ACCOUNT_ID==null*/){
            HttpCookie cook = cooks.next();
            log(cook.toString());
            if(cook.getName().equals(BUNGLED_COOKIE_NAME)){
                X_CSRF = cook.getValue();
            }
            if(cook.getName().equals(BUNGLEME_COOKIE_NAME)){
                ACCOUNT_ID = cook.getValue();
            }
        }
    }

    //TODO remove, just for testing
    private void log(String message){
        System.out.println(message);
    }
    //TODO remove, just for testing
    private void printCookies(){
        System.out.println("******Cookies******* ");
        for(HttpCookie co : cookieManager.getCookieStore().getCookies()){
            log("\t"+co.getName()
                    + "\t"+co.getValue()
                    + "\t"+co.getMaxAge());
        }
    }
    //TODO remove, just for testing
    private void printHeaders(){
        Set<Entry<String, List<String>>> entrySet = urlConnection.getHeaderFields().entrySet();
        System.out.println("******Headers******* ");
        for(Entry<String, List<String>> entry : entrySet){
            System.out.println("\t"+entry.getKey()+"\t"+entry.getValue().toString());
        }
    }

    private static final char PARAMETER_DELIMITER = '&';
    private static final char PARAMETER_EQUALS_CHAR = '=';
    /**
     *
     * @param parameters a Map containing Key,Value pairs
     * @return a String representing the url encoded querystring
     * @throws UnsupportedEncodingException
     */
    public static String createURLEncodedQueryString(Map<String, String> parameters) throws UnsupportedEncodingException {
        StringBuilder parametersAsQueryString = new StringBuilder();
        if (parameters != null) {
            boolean firstParameter = true;

            for (String parameterName : parameters.keySet()) {
                if (!firstParameter) {
                    parametersAsQueryString.append(PARAMETER_DELIMITER);
                }
                parametersAsQueryString.append(parameterName)
                        .append(PARAMETER_EQUALS_CHAR)
                        .append(URLEncoder.encode(
                                parameters.get(parameterName), "UTF-8"));

                firstParameter = false;
            }
        }
        return parametersAsQueryString.toString();
    }

    private void testLogin() throws IOException{
        String url = "https://www.bungie.net/Platform/Destiny/"+type+"/MyAccount/Vault/";

        log(url);

        urlConnection = (HttpURLConnection) new URL(url).openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.addRequestProperty("User-Agent", USERAGENT);
        urlConnection.addRequestProperty("X-API-Key", X_API_KEY);
        urlConnection.addRequestProperty("X-CSRF", X_CSRF);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(urlConnection.getInputStream())
        );

        String inputLine;
        StringBuffer sbTmp = new StringBuffer();
        while ((inputLine = in.readLine()) != null)
            sbTmp.append(inputLine);
        in.close();

        JSONObject json = null; // convert it to JSON object
        try {
            json = new JSONObject(sbTmp.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            log(json.toString(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
