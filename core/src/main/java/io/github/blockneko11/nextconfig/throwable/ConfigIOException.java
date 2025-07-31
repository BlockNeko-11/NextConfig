package io.github.blockneko11.nextconfig.throwable;

public class ConfigIOException extends ConfigException {
    public ConfigIOException() {
        super();
    }

    public ConfigIOException(String message) {
        super(message);
    }

    public ConfigIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigIOException(Throwable cause) {
        super(cause);
    }
}
