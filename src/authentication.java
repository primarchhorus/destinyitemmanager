/**
 * Created by benjamingarner on 10/07/2016.
 */

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


public class authentication {

    private  String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10; rv:33.0) Gecko/20100101 Firefox/33.0";
    private  static String BURL = "https://www.bungie.net/en/User/SignIn/";
    private  static String BURL_NO_LOC = "/User/SignIn/";
    private  static int TYPE_XBOX = 1;
    private  static int TYPE_PSN = 2;
    private  static String XBOX = "Xuid";
    private  int provider = 2;
    private final static String PSN = "Psnid";

    public void sendGet() throws Exception {



        String url = provider==TYPE_XBOX ? BURL+XBOX : BURL+PSN;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setInstanceFollowRedirects(false);
        String newLoc = con.getHeaderField("Location");
        int responseCode = con.getResponseCode();
        System.out.println(newLoc);

        URL obj2 = new URL(newLoc);
        HttpURLConnection con2 = (HttpURLConnection) obj2.openConnection();
        con2.setInstanceFollowRedirects(false);
        con2.addRequestProperty("User-Agent", USER_AGENT);

        String newLoc2 = "";
        if(!(con2.getResponseCode()==HttpURLConnection.HTTP_OK))
        {
            newLoc2 = con2.getHeaderField("Location");
            System.out.println(newLoc2);
        }

        if(!newLoc2.contains(url+"?code"))
        {
            System.out.println("no code");
        }

//        switch(provider) {
//            case TYPE_XBOX:
//                System.out.println("xbox");
//            case TYPE_PSN:
//                System.out.println("psn");
//        }

        // https://auth.api.sonyentertainmentnetwork.com/login.jsp?request_locale=en_US&request_theme=liquid

//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuffer response = new StringBuffer();
//
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();

        //print result
//        System.out.println(response.toString());

    }
}
