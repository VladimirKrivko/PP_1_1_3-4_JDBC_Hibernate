package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory;

    {
        sessionFactory = Util.getSessionFactory();
    }

    public UserDaoHibernateImpl() {

    }


    @Override
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

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Query<User> query = session.createSQLQuery(sqlCreateTable).addEntity(User.class);
            query.executeUpdate();

            session.getTransaction().commit();
        }
    }

    @Override
    public void dropUsersTable() {
        String sqlDropTableUser = "DROP TABLE IF EXISTS `user`";
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            NativeQuery<?> query = session.createNativeQuery(sqlDropTableUser).addEntity(User.class);
            query.executeUpdate();

            session.getTransaction().commit();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User savedUser = new User(name, lastName, age);
        try (Session session = sessionFactory.openSession()) {
            session.save(savedUser);
        }
    }

    @Override
    public void removeUserById(long id) {
        String sqlRemoveUserById = "delete from User u where u.id = :id";
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<User> query = session.createNativeQuery(sqlRemoveUserById, User.class);
            query.setParameter("id", id);
            query.executeUpdate();

            session.getTransaction().commit();
        }
    }

    @Override
    public List<User> getAllUsers() {
        String sqlGetAllUsers = "from User";
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery(sqlGetAllUsers, User.class);
            return query.getResultList();
        }
    }

    @Override
    public void cleanUsersTable() {
        dropUsersTable();
        createUsersTable();
    }
}
