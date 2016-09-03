import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.util.Map;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import org.json.simple.parser.JSONParser;

/**
 * Created by benjamingarner on 20/08/2016.
 */
public class applicationconfigmanagement {

    utilities util = new utilities();
    identificationitems items = new identificationitems();
    Logger logger = LogManager.getLogger(manifestretrieval.class);

    public boolean createConfigFile() {

        File f = null;
        boolean success = true;
        if(!checkConfigFileExists()) {
            try {
                f = new File(items.getConfigLocation() + items.getConfigFileName());
                success = f.createNewFile();
            } catch (IOException e) {
                logger.error("Failed to create destiny manager configuration file", e);
                success = false;

            }
        }
        return success;
    }

    public boolean writeConfigFile(JSONObject updatedJson) throws IOException {
        boolean success = false;
        if(checkConfigFileExists())
        {
            FileWriter file = new FileWriter(items.getConfigLocation()+items.getConfigFileName());
            try {
                file.write(updatedJson.toString());
            }
            catch(IOException ex)
            {
                logger.error("Failed to write data into destiny manager configuration file",ex);
            }
            finally {
                file.flush();
                file.close();
                success = true;

            }
        }
        else
        {
            logger.error("Failed to write destiny manager configuration file");
        }
        return success;
    }

    public boolean checkConfigFileExists()
    {
        boolean success = util.checkFileExists(items.getConfigLocation()+items.getConfigFileName());
        return success;
    }

    public String getValueFromConfigFile(String key)
    {
        JSONObject readIn = readInConfigFile();
        String returnVal = "";
        try {
            returnVal = readIn.getString(key);
        } catch (JSONException e) {
            logger.error("Failed to extract manifest version from read in config data", e);
        }
        return returnVal;
    }

    String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    public JSONObject readInConfigFile() {
        JSONObject jobj = null;
        if(checkConfigFileExists()) {
            String result = null;
            try {
                result = readFile(items.getConfigLocation()+items.getConfigFileName());
            } catch (IOException e) {
                logger.error("Failed to read in destiny manager configuration file data", e);
            }
            if(!result.isEmpty())
            {
                try {
                    jobj = new JSONObject(result);
                } catch (Exception e) {
                    logger.error("Failed to read in destiny manager configuration file data", e);
                }
            }

        }
        return jobj;
    }

    public boolean updateConfigFile (String key, String value) throws JSONException
    {
        boolean success = false;
        JSONObject readInData = readInConfigFile();
        if(readInData != null)
        {
            readInData.put(key,value);
            try {
                writeConfigFile(readInData);
            } catch (IOException e) {
                e.printStackTrace();
            }
            success = true;
        }
        else
        {

            JSONObject blankFix = new JSONObject();
            blankFix.put(key,value);
            try {
                writeConfigFile(blankFix);
            } catch (IOException e) {
                e.printStackTrace();
            }
            success = true;
        }
        return success;
    }

    public void manageConfigUpdate(String key, String value)
    {
        createConfigFile();
        try {
            updateConfigFile (key, value);
        } catch (JSONException e) {
            logger.error("Failed to update manager configuration file", e);
        }
    }

}
