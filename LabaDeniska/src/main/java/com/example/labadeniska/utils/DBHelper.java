package com.example.labadeniska.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBHelper {
    URL url = this
            .getClass()
            .getResource("/statements.properties");
    static Properties property = new Properties();
    FileInputStream fis;

    {
        try {
            fis = new FileInputStream(url.getFile());
            try {
                property.load(fis);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static String URL = "jdbc:postgresql://127.0.0.1:5432/lab_denis";
    private static String LOGIN = "postgres";
    private static String PASSWORD = "password";

    public  static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, LOGIN, PASSWORD);
    }
}
