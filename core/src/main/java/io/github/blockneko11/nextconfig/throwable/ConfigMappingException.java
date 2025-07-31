package io.github.blockneko11.nextconfig.throwable;

public class ConfigMappingException extends ConfigException {
    public ConfigMappingException() {
        super();
    }

    public ConfigMappingException(String message) {
        super(message);
    }

    public ConfigMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigMappingException(Throwable cause) {
        super(cause);
    }
}
