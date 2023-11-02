package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();

        userService.saveUser("Walter", "White", (byte) 50);
        userService.saveUser("Hank", "Schrader", (byte) 47);
        userService.saveUser("Saul", "Goodman", (byte) 40);
        userService.saveUser("Tuco", "Salamanca", (byte) 36);

        userService.getAllUsers().forEach(user -> logger.info(String.valueOf(user)));

        userService.cleanUsersTable();
        userService.dropUsersTable();

        Util.closeJdbcConnection();
    }
}
