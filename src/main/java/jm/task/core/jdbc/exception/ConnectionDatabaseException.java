package jm.task.core.jdbc.exception;

public class ConnectionDatabaseException extends RuntimeException {

    public ConnectionDatabaseException() {
    }

    public ConnectionDatabaseException(String message) {
        super(message);
    }

    public ConnectionDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionDatabaseException(Throwable cause) {
        super(cause);
    }
}
