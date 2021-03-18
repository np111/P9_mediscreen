package com.mediscreen.common.util.exception;

import static com.mediscreen.common.util.exception.FastException.DEBUG;

/**
 * A faster variant of {@link RuntimeException} with no synchronization/stack-traces.
 * To be used for services exceptions that are not unexpected.
 * <p>
 * You can re-enable stack-traces for debugging purposes by using: {@code <code>-Dcom.mediscreen.debugFastException=true</code>}
 */
public abstract class FastRuntimeException extends RuntimeException {
    public FastRuntimeException() {
    }

    public FastRuntimeException(String message) {
        super(message);
    }

    public FastRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FastRuntimeException(Throwable cause) {
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
