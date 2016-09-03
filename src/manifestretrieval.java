import java.io.IOException;
import java.util.HashMap;
import org.apache.logging.log4j.*;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by benjamingarner on 30/07/2016.
 */
public class manifestretrieval {
    networkingcalls netCaller = new networkingcalls();
    identificationitems items = new identificationitems();
    Logger logger = LogManager.getLogger(manifestretrieval.class);
    applicationconfigmanagement configger = new applicationconfigmanagement();
    authentication psnAuth = new authentication();

    public void updateManifest() throws JSONException{
        HashMap properties = new HashMap<String,String>();
        properties.put("X-API-Key", items.getApiKey().toString());
        JSONObject manifestResponse = null;
        utilities util = new utilities();

        try{
            manifestResponse = netCaller.connectAndReadHttpUrlConnection(items.getManifestUrl(),properties);
        } catch(IOException ioex) {
            logger.error("IOException caught when attempting to read from url in manifestretrieval::updateManifest",ioex);
        }
        catch (JSONException joex){
            logger.error("JSONException caught when attempting to read from url in manifestretrieval::updateManifest",joex);
        }

        if(manifestResponse.getJSONObject("Response") != null)
        {
            HashMap maniName = null;
            String worldSqlContentLoc = manifestResponse.getJSONObject("Response").getJSONObject("mobileWorldContentPaths").get("en").toString();
            if(!checkCurrnetManifestVersion(worldSqlContentLoc))
            {

                try{
                    maniName = netCaller.downloadManifest(items.getBaseBungoUrl()+worldSqlContentLoc);
                }catch(IOException ioex) {
                    logger.error("IOException caught when attempting to download manifest in manifestretrieval::updateManifest",ioex);
                }

                if(maniName != null)
                {
                    if(extractManifestDB(maniName.get("zipped").toString(),maniName.get("unzipped").toString()))
                    {
                        util.renameFile(maniName.get("unzipped").toString(),"destiny_sqlite_database.db");
                        configger.manageConfigUpdate("manifest_version",worldSqlContentLoc);
                    }
                    else
                    {
                        logger.error("Unzipped manifest file "+maniName.get("unzipped").toString()+" not found");
                    }
                }
                else
                {
                    logger.error("Manifest data not accessed from url");
                }
            }
            else
            {
                logger.info("Manifest all ready current version");
            }
        }
    }

    public boolean extractManifestDB(String zippedFileName, String unzippedFilename){
        utilities util = new utilities();

        if(util.checkFileExists(zippedFileName))
        {
            try {
                ZipFile zipFile = new ZipFile(zippedFileName);
                zipFile.extractAll(items.getManifestDirectoryLoc());
            } catch (ZipException e) {
                logger.error("ZipException caught when attempting unzip manifest file in manifestretrieval::extractManifestDB",e);
            }

            logger.info("Manifest file unzipped");

        }
        else
        {
            logger.error(zippedFileName+" file not found");
        }
        return util.checkFileExists(unzippedFilename);
    }


    public boolean checkCurrnetManifestVersion(String version){
        return version.equals(configger.getValueFromConfigFile("manifest_version"));
    }
}
