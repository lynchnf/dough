package norman.dough.exception;

import org.slf4j.Logger;

public class InconceivableException extends Exception {
    public InconceivableException(Logger logger, String message) {
        super(message);
        logger.error(message);
    }

    public InconceivableException(Logger logger, String message, Throwable cause) {
        super(message, cause);
        logger.error(message, cause);
    }
}
