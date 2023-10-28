package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.exception.ConnectionDatabaseException;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection;

    {
        try {
            this.connection = Util.getJdbcConnection();
        } catch (ClassNotFoundException | SQLException e) {
            throw new ConnectionDatabaseException(e);
        }
    }

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        String sqlCreateTable = """
                CREATE TABLE IF NOT EXISTS `user`
                (
                    `id`        bigint         NOT NULL AUTO_INCREMENT,
                    `name`      varchar(255) NOT NULL,
                    `last_name` varchar(255) NOT NULL,
                    `age`       tinyint      NOT NULL,
                    PRIMARY KEY (`id`)
                );
                """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlCreateTable);
        } catch (SQLException e) {
            throw new ConnectionDatabaseException(e);
        }
    }

    public void dropUsersTable() {
        String sqlDropTableUser = "DROP TABLE IF EXISTS `user`";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlDropTableUser);
        } catch (SQLException e) {
            throw new ConnectionDatabaseException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sqlSaveUser = """
                INSERT INTO `user`(name, last_name, age)
                VALUE (?, ?, ?)
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSaveUser)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ConnectionDatabaseException(e);
        }
    }

    public void removeUserById(long id) {
        String sqlRemoveUserById = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlRemoveUserById)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ConnectionDatabaseException(e);
        }
    }

    public List<User> getAllUsers() {
        String sqlGetAll = "SELECT id, name, last_name, age FROM user";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlGetAll)) {
            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new ConnectionDatabaseException(e);
        }
    }

    public void cleanUsersTable() {
        dropUsersTable();
        createUsersTable();
    }
}
