package com.akanwkasa.dcic.nssf.helpers;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.Discriminator;

/**
 * Makes it possible to use logger name to determine file/folder name
 */
public class LogDiscriminator implements Discriminator<ILoggingEvent> {
    private static final String KEY = "loggerName";
    private boolean started;

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getDiscriminatingValue(ILoggingEvent iLoggingEvent) {
        return iLoggingEvent.getLoggerName();
    }

    @Override
    public void stop() {
        started = false;
    }

    @Override
    public void start() {
        started = true;
    }

    @Override
    public boolean isStarted() {
        return started;
    }
}
