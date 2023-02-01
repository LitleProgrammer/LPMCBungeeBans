package de.littleprogrammer.lpmcbans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private final String HOST = "localhost";
    private final int PORT = 3306;
    private final String DATABASE = "lpmc_db";
    private final String USERNAME = "LpmcBans";
    private final String PASSWORD = "MDo1Lf]LMPvEq)[[";

    private Connection connection;

    public void connect() throws SQLException {

        connection = DriverManager.getConnection(
                "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?autoReconnect=true&interactiveClient=true",
                USERNAME,
                PASSWORD);

    }

    public boolean isConnected(){ return connection != null; }

    public Connection getConnection() {
        return connection;
    }

    public void disconnect(){
        if (isConnected()){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
