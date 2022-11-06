package io.flybird.platform.logger;

import java.util.logging.Logger;

public abstract class BasicLogger {

    private final Logger logger;

    public Logger getBukkitLogger() {
        return logger;
    }

    public BasicLogger(Logger logger){
        this.logger = logger;
    }

    abstract void info(Object log);
    abstract void info(Object log, Object... replace);

    abstract void success(Object log);
    abstract void success(Object log, Object... replace);

    abstract void error(Object log);
    abstract void error(Object log, Object... replace);

    abstract void warn(Object log);
    abstract void warn(Object log, Object... replace);
}
