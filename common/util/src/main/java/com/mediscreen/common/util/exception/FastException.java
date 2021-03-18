package com.mediscreen.common.util.exception;

/**
 * A faster variant of {@link Exception} with no synchronization/stack-traces.
 * To be used for services exceptions that are not unexpected.
 * <p>
 * You can re-enable stacktrace for debugging purposes by using: {@code <code>-Dcom.mediscreen.debugFastException=true</code>}
 */
public class FastException extends Exception {
    static boolean DEBUG = "true".equalsIgnoreCase(System.getProperty("com.mediscreen.debugFastException", System.getenv("MEDISCREEN_DEBUGFASTEXCEPTION")));

    public FastException() {
    }

    public FastException(String message) {
        super(message);
    }

    public FastException(String message, Throwable cause) {
        super(message, cause);
    }

    public FastException(Throwable cause) {
        super(cause);
    }

    @Override
    public Throwable initCause(Throwable throwable) {
        return DEBUG ? super.initCause(throwable) : this;
    }

    @Override
    public Throwable fillInStackTrace() {
        return DEBUG ? super.fillInStackTrace() : this;
    }
}
