package jm.task.core.jdbc.dao;

public enum SqlQuery {
    CREATE_USER_TABLES("""
            CREATE TABLE IF NOT EXISTS `user`
            (
                `id`        bigint       NOT NULL AUTO_INCREMENT,
                `name`      varchar(255) NOT NULL,
                `last_name` varchar(255) NOT NULL,
                `age`       tinyint      NOT NULL,
                PRIMARY KEY (`id`)
            )"""),
    DROP_USER_TABLE("DROP TABLE IF EXISTS `user`"),
    SAVE_USER("INSERT INTO `user`(name, last_name, age) VALUE (?, ?, ?)"),
    REMOVE_USER_BY_ID("DELETE u FROM `user` u WHERE u.id=?"),
    GET_ALL_USERS("SELECT id, name, last_name, age FROM `user`"),
    CLEAR_USERS_TABLE("TRUNCATE TABLE `user`");

    private final String query;

    SqlQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
