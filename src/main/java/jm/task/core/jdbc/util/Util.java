package jm.task.core.jdbc.util;

import jm.task.core.jdbc.exception.ConnectionDatabaseException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {

    private Util() {
        throw new IllegalStateException("Utility class");
    }

    public static Connection getJdbcConnection() throws ClassNotFoundException, SQLException {
        Properties properties = new Properties();
        try (FileReader readerProperties = new FileReader("src/main/resources/jdbc.properties")) {
            properties.load(readerProperties);

            String driver = properties.getProperty("db.driver");
            String url = properties.getProperty("db.url");
            String userName = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");

            Class.forName(driver);
            return DriverManager.getConnection(url, userName, password);
        } catch (IOException e) {
            throw new ConnectionDatabaseException(e);
        }
    }
}
