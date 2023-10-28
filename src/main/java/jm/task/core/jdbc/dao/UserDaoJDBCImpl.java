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
        try (Statement statement = connection.createStatement()) {
            statement.execute(SqlQueries.CREATE_USER_TABLES);
        } catch (SQLException e) {
            throw new ConnectionDatabaseException(e);
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(SqlQueries.DROP_USER_TABLE);
        } catch (SQLException e) {
            throw new ConnectionDatabaseException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SAVE_USER)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ConnectionDatabaseException(e);
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.REMOVE_USER_BY_ID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ConnectionDatabaseException(e);
        }
    }

    public List<User> getAllUsers() {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SqlQueries.GET_ALL_USERS)) {
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
