package jm.task.core.jdbc.util;

import jm.task.core.jdbc.exception.ConnectionDatabaseException;
import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private static Connection jdbcConnection;
    private static SessionFactory sessionFactory;

    private Util() {
        throw new IllegalStateException("Utility class");
    }

    public static SessionFactory getSessionFactory() {
        sessionFactory = new Configuration()
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        sessionFactory.close();
    }

    public static Connection getJdbcConnection() {
        Properties properties = new Properties();
        try (FileReader readerProperties = new FileReader("src/main/resources/jdbc.properties")) {
            properties.load(readerProperties);

            String driver = properties.getProperty("db.driver");
            String url = properties.getProperty("db.url");
            String userName = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");

            Class.forName(driver);
            jdbcConnection = DriverManager.getConnection(url, userName, password);
            return jdbcConnection;
        } catch (ClassNotFoundException | IOException | SQLException e) {
            throw new ConnectionDatabaseException(e);
        }
    }

    public static void closeJdbcConnection() {
        try {
            jdbcConnection.close();
        } catch (SQLException e) {
            throw new ConnectionDatabaseException("error closing database connection", e);
        }
    }

//    private static SessionFactory initialSessionFactory() {
//        return new Configuration()
//                .addAnnotatedClass(User.class)
//                .buildSessionFactory();
//    }

//    private static Connection initialConnection() {
//        Properties properties = new Properties();
//        try (FileReader readerProperties = new FileReader("src/main/resources/jdbc.properties")) {
//            properties.load(readerProperties);
//
//            String driver = properties.getProperty("db.driver");
//            String url = properties.getProperty("db.url");
//            String userName = properties.getProperty("db.username");
//            String password = properties.getProperty("db.password");
//
//            Class.forName(driver);
//            return DriverManager.getConnection(url, userName, password);
//        } catch (ClassNotFoundException | IOException | SQLException e) {
//            throw new ConnectionDatabaseException(e);
//        }
//    }
}
