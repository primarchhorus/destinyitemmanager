/**
 * Created by benjamingarner on 6/08/2016.
 */
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;


public class databasecalls {

    destinytabledefinitions definitions = new destinytabledefinitions();
    Connection connection = null;
    Logger logger = LogManager.getLogger(manifestretrieval.class);

    public void connectToDatabase(String databaseUrl) throws ClassNotFoundException{
        Class.forName("org.sqlite.JDBC");
        try {
            // db parameters
            String url = "jdbc:sqlite:"+databaseUrl;
            // create a connection to the database
            connection = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            logger.error("SQLException encountered when connecting to database", e);
        }
    }

    public void disconnectFromDatabase()
    {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            logger.error("SQLException encountered when disconnecting from database", ex);
        }
    }

    public JSONArray getTableDataFromManifest(String definition)
    {
        JSONArray tableData = new JSONArray();
        JSONObject rowData = new JSONObject();
        if(definitions.getDefinition(definition) != null)
        {
            String sql = "SELECT json from "+definitions.getDefinition(definition).toString();

            try {
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                int rowIndex = 0;
                while (rs.next())
                {
                    try {
                        rowData.put("row_"+Integer.toString(rowIndex),rs.getString(1));
                    } catch (JSONException e) {
                        logger.error("JSONException encountered when extracting table data from database", e);
                    }

                }
                tableData.put(rowData);
                System.out.println(tableData.toString());
            } catch (SQLException e) {
                logger.error("SQLException encountered when executing select query for table data "+definitions.getDefinition(definition).toString()+" from database", e);
            }
        }
        return tableData;
    }
}
