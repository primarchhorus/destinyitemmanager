/**
 * Created by benjamingarner on 10/07/2016.
 */
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

public class main {
    public static void main(String[] args) {

        manifestretrieval mani = new manifestretrieval();
        databasecalls dbthing = new databasecalls();
        identificationitems items = new identificationitems();
        Logger logger = LogManager.getLogger(manifestretrieval.class);
        networkingcalls net = new networkingcalls();

        try {
            net.loginProviderAuthentication();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            mani.updateManifest();
//        }
//        catch(JSONException ex) {
//            logger.error("JSON exception encountered when attempting to update manifest",ex);
//        }
//
//        try {
//            String databaseLocation = items.getManifestDirectoryLoc()+ items.getDatabaseFileName();
//            dbthing.connectToDatabase(databaseLocation);
//        }
//        catch(ClassNotFoundException ex) {
//            logger.error("Class not found when attempting to connect to sql database",ex);
//        }
//
//
//        logger.info(dbthing.getTableDataFromManifest("Class"));
//        dbthing.disconnectFromDatabase();
//



    }
}
