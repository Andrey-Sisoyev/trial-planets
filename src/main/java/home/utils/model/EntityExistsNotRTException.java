package home.utils.model;

public class EntityExistsNotRTException extends javax.persistence.PersistenceException {
    // ================================
    // CONSTRUCTORS

    public EntityExistsNotRTException() {
    }

    public EntityExistsNotRTException(String message) {
        super(message);
    }

    public EntityExistsNotRTException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityExistsNotRTException(Throwable cause) {
        super(cause);
    }
}

