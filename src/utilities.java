/**
 * Created by benjamingarner on 6/08/2016.
 */

import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.logging.Logger;

public class utilities {

    private String manifestLocation = "manifest_version/"; // move to identification items
    org.apache.logging.log4j.Logger logger = LogManager.getLogger(manifestretrieval.class);

    public boolean setupManifestLocation() {
        boolean returnval = false;
        File manifestDir = new File(manifestLocation);
        if (!manifestDir.exists()) {
            if(manifestDir.mkdir()){
                returnval = true;
            }
        }
        else{
            returnval = true;
        }
        return returnval;
    }

    public boolean createDirectory(String dir) {
        boolean returnval = false;
        File manifestDir = new File(dir);
        if (!manifestDir.exists()) {
            if(manifestDir.mkdir()){
                returnval = true;
            }
        }
        else{
            returnval = true;
        }
        return returnval;
    }


    public String renameFile
            (String source, String newFileName)
    {
        File oldName = new File(source);
        String[] name = source.split("/");
        String[] filePath = Arrays.copyOf(name, name.length-1);
        String newNameFullPath = toString(filePath,"/")+"/"+newFileName;
        System.out.println(newNameFullPath);
        //create destination File object
        File newName = new File(newNameFullPath);
        oldName.renameTo(newName);
        return newNameFullPath;
    }

    public static String toString(String[] strings, String separator) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strings.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(strings[i]);
        }
        return sb.toString();
    }

    public String getFileExtension(String f) {
        String ext = "";
        int i = f.lastIndexOf('.');
        if (i > 0 &&  i < f.length() - 1) {
            ext = f.substring(i + 1).toLowerCase();
        }
        return ext;
    }


    public boolean checkFileExists(String fileName){
        File fileToCheck = new File(fileName);

        return fileToCheck.exists();
    }

    public void print(Object out)
    {
        System.out.println(out);
    }

    public String readUrlResponse(HttpURLConnection con)
    {
        InputStream in = null;
        try {
            in = new BufferedInputStream(con.getInputStream());
        } catch (IOException e) {
            logger.error("IOException encountered when reading url response",e);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        //Loop over buffer add to string for json conversion
        StringBuilder result = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            logger.error("IOException encountered when reading url response",e);
        }

        return result.toString();
    }
}
