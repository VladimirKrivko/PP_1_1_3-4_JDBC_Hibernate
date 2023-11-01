package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.exception.ConnectionDatabaseException;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Objects;

public class UserDaoHibernateImpl implements UserDao {
    private static final UserDaoHibernateImpl INSTANCE = new UserDaoHibernateImpl();
    private final SessionFactory sessionFactory = Util.getSessionFactory();

    private UserDaoHibernateImpl() {
    }

    public static UserDaoHibernateImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<User> query = session.createNativeQuery(SqlQuery.CREATE_USER_TABLES.getQuery(), User.class);
            query.executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<User> query = session.createNativeQuery(SqlQuery.DROP_USER_TABLE.getQuery(), User.class);
            query.executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                session.persist(new User(name, lastName, age));
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new ConnectionDatabaseException("error saving user name=%s, lastname=%s, age=%s%n".formatted(name, lastName, age), e);
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                User deletableUser = session.get(User.class, id);
                if (Objects.nonNull(deletableUser)) {
                    session.delete(deletableUser);
                    session.flush();
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new ConnectionDatabaseException("error removing user with id=%d%n".formatted(id), e);
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<User> query = session.createNativeQuery(SqlQuery.CLEAN_USERS_TABLE.getQuery(), User.class);
            query.executeUpdate();
            session.getTransaction().commit();
        }
    }
}
