package com.esri.geoevent.processor;

import com.esri.ges.framework.i18n.BundleLogger;
import com.esri.ges.framework.i18n.BundleLoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.microsoft.sqlserver.jdbc.*;

public class WheresFidoDB {

    //TODO: add dll file on linux
    // https://stackoverflow.com/questions/11707056/no-sqljdbc-auth-in-java-library-path

    private static final BundleLogger LOGGER = BundleLoggerFactory.getLogger(WheresFidoProcessor.class);
    private SQLServerDataSource ds;
    private Connection conn;

    private Connection getConnection() {
        try {
            ds = new SQLServerDataSource();
            ds.setIntegratedSecurity(true);
            ds.setServerName("MININT-BTM4NR3.esri.com");
            ds.setPortNumber(1433);
            ds.setDatabaseName("WheresFido");
            return ds.getConnection();
        } catch (Exception e) {
            LOGGER.info("COULDN'T CONNECT TO DB");
        }

        return null;
    }

    public List<User> getUsersWithNotificationsOn() {
        try {
            // List to return
            List<User> usersOut = new ArrayList<>();

            // Initialize connection
            conn = getConnection();
            if (conn != null) {
                // Create query
                String query = "SELECT Email, Lat, Long FROM \"User\" WHERE Notifications = 1";
                Statement stmt = conn.createStatement();
                ResultSet results = stmt.executeQuery(query);

                // Create new user objects for each user returned
                while (results.next()) {
                    String email = results.getString("Email");
                    double lat = results.getDouble("Lat");
                    double lon = results.getDouble("Long");

                    // Add user to usersOut list
                    User u = new User(email, lat, lon);
                    usersOut.add(u);
                }

                return usersOut;
            }
        } catch  (Exception e) {
            LOGGER.info("ERROR IN GETUSERSWITHNOTIFICATIONSON: " + e.getMessage());
        }
        return null;
    }

    public boolean addToWatching(int searchID, String userEmail) {
        try {
            // Initialize connection
            conn = getConnection();
            if (conn != null) {
                // Create query
                String query = "INSERT INTO \"Watching\" VALUES (" + userEmail + ", " + searchID + ");";
                Statement stmt = conn.createStatement();
                int result = stmt.executeUpdate(query);

                if (result > 0) {
                    // Success!
                    return true;
                }

                // Didn't update :(
                return false;
            }
        } catch  (Exception e) {
            LOGGER.info("ERROR IN ADDTOWATCHING: " + e.getMessage());
        }
        return false;
    }

    public boolean checkIfUserIsWatching(int searchID, String userEmail) {
        try {
            // Initialize connection
            conn = getConnection();
            if (conn != null) {
                // Create query
                String query = "SELECT * FROM \"Watching\" WHERE SearchID = " + searchID  + " AND UserEmail = " + userEmail + ";";
                Statement stmt = conn.createStatement();
                ResultSet results = stmt.executeQuery(query);

                if (results.next()) {
                    // User is watching the search
                    return true;
                }

                // User is not watching
                return false;
            }
        } catch  (Exception e) {
            LOGGER.info("ERROR IN ADDTOWATCHING: " + e.getMessage());
        }
        return false;
    }

}
