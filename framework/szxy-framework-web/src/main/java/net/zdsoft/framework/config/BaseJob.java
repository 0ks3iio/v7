package net.zdsoft.framework.config;

public abstract class BaseJob {

    abstract protected void run();

    protected void start() {
        run();
    }

}
