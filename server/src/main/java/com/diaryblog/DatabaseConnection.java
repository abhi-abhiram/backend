package com.diaryblog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");

                // Update the URL format for ElephantSQL
                String url = "jdbc:postgresql://kiouni.db.elephantsql.com:5432/zcqmlwkc";
                String username = "zcqmlwkc";
                String password = "k3gddfkoJPav3G0pzFpD6ykpixLdU3uW";

                connection = DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException e) {
                // Log or throw the exception based on your application's error handling
                // strategy
                e.printStackTrace();
                throw new SQLException("Database driver not found.", e);
            }

        }
        return connection;
    }
}
