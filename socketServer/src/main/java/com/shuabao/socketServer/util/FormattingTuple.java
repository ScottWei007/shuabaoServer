
package com.shuabao.socketServer.util;

/**
 * Holds the results of formatting done by {@link MessageFormatter}.
 *
 * Forked from <a href="https://github.com/netty/netty">Netty</a>.
 */
class FormattingTuple {

    private final String message;
    private final Throwable throwable;

    FormattingTuple(String message) {
        this(message, null);
    }

    FormattingTuple(String message, Throwable throwable) {
        this.message = message;
        this.throwable = throwable;
    }

    static Object[] trimmedCopy(Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            throw new IllegalStateException("Empty or null argument array");
        }
        final int trimmedLen = argArray.length - 1;
        Object[] trimmed = new Object[trimmedLen];
        System.arraycopy(argArray, 0, trimmed, 0, trimmedLen);
        return trimmed;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
