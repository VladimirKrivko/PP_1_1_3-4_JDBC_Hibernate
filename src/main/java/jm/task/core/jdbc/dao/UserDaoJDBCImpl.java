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
import java.util.Objects;

public class UserDaoJDBCImpl implements UserDao {
    private static final UserDaoJDBCImpl INSTANCE = new UserDaoJDBCImpl();
    private static final Connection CONNECTION = Util.getJdbcConnection();

    private UserDaoJDBCImpl() {

    }

    public static UserDaoJDBCImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public void createUsersTable() {
        try (Statement statement = CONNECTION.createStatement()) {
            statement.execute(SqlQuery.CREATE_USER_TABLES.getQuery());
        } catch (SQLException e) {
            throw new ConnectionDatabaseException(e);
        }
    }

    @Override
    public void dropUsersTable() {
        try (Statement statement = CONNECTION.createStatement()) {
            statement.execute(SqlQuery.DROP_USER_TABLE.getQuery());
        } catch (SQLException e) {
            throw new ConnectionDatabaseException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = CONNECTION.prepareStatement(SqlQuery.SAVE_USER.getQuery());
            CONNECTION.setAutoCommit(false);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            CONNECTION.commit();
        } catch (Exception ex) {
            try {
                CONNECTION.rollback();
            } catch (SQLException e) {
                throw new ConnectionDatabaseException(e);
            }
        } finally {
            if (Objects.nonNull(preparedStatement)) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new ConnectionDatabaseException(e);
                }
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = CONNECTION.prepareStatement(SqlQuery.REMOVE_USER_BY_ID.getQuery());
            CONNECTION.setAutoCommit(false);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            CONNECTION.commit();
        } catch (Exception ex) {
            try {
                CONNECTION.rollback();
            } catch (SQLException e) {
                throw new ConnectionDatabaseException(e);
            }
        } finally {
            if (Objects.nonNull(preparedStatement)) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new ConnectionDatabaseException(e);
                }
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Statement statement = CONNECTION.createStatement();
             ResultSet resultSet = statement.executeQuery(SqlQuery.GET_ALL_USERS.getQuery())) {
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

    @Override
    public void cleanUsersTable() {
        try (Statement statement = CONNECTION.createStatement()) {
            statement.execute(SqlQuery.CLEAR_USERS_TABLE.getQuery());
        } catch (SQLException e) {
            throw new ConnectionDatabaseException(e);
        }
    }
}
