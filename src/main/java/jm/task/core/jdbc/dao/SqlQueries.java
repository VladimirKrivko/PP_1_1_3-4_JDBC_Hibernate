package jm.task.core.jdbc.dao;

public interface SqlQueries {
    String CREATE_USER_TABLES = """
                CREATE TABLE IF NOT EXISTS `user`
                (
                    `id`        bigint         NOT NULL AUTO_INCREMENT,
                    `name`      varchar(255) NOT NULL,
                    `last_name` varchar(255) NOT NULL,
                    `age`       tinyint      NOT NULL,
                    PRIMARY KEY (`id`)
                )""";
    String DROP_USER_TABLE = "DROP TABLE IF EXISTS `user`";
    String SAVE_USER = "INSERT INTO `user`(name, last_name, age) VALUE (?, ?, ?)";
    String REMOVE_USER_BY_ID = "DELETE u FROM `user` u WHERE u.id=?";
    String GET_ALL_USERS = "SELECT id, name, last_name, age FROM `user`";
}
