package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

import static org.hibernate.resource.transaction.spi.TransactionStatus.ACTIVE;
import static org.hibernate.resource.transaction.spi.TransactionStatus.COMMITTED;
import static org.hibernate.resource.transaction.spi.TransactionStatus.MARKED_ROLLBACK;

public class UserDaoHibernateImpl implements UserDao {
    public static final Logger logger = LoggerFactory.getLogger(UserDaoHibernateImpl.class);
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
                logger.warn("failed to save user due to error - %s".formatted(e.getMessage()));
                if (session.getTransaction().getStatus() == ACTIVE
                    || session.getTransaction().getStatus() == MARKED_ROLLBACK) {

                    session.getTransaction().rollback();
                }
            }
            if (session.getTransaction().getStatus() == COMMITTED) {
                logger.info("User with name – %s added to the database".formatted(name));
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
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                logger.warn("failed to removed user by id = %d due to error - %s".formatted(id, e.getMessage()));
                if (session.getTransaction().getStatus() == ACTIVE
                    || session.getTransaction().getStatus() == MARKED_ROLLBACK) {

                    session.getTransaction().rollback();
                }
            }
            if (session.getTransaction().getStatus() == COMMITTED) {
                logger.info("User with id – %d removed from database".formatted(id));
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
            int numberOfUsersInDatabase = 0;
            try {
                session.beginTransaction();
                Query<?> query = session.createQuery("delete from User");
                numberOfUsersInDatabase = query.executeUpdate();
                session.getTransaction().commit();
            } catch (Exception ex) {
                logger.warn("failed to clean users table due to error - %s".formatted(ex.getMessage()));
                if (session.getTransaction().getStatus() == ACTIVE
                    || session.getTransaction().getStatus() == MARKED_ROLLBACK) {

                    session.getTransaction().rollback();
                }
            }
            if (session.getTransaction().getStatus() == COMMITTED) {
                logger.info("Users table was cleaned. Total of %d users have been deleted.".formatted(numberOfUsersInDatabase));
            }
        }
    }
}
