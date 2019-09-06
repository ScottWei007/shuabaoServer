

package com.shuabao.socketServer.exception;


public class FlowControlException extends RuntimeException {

    private static final long serialVersionUID = 3478741195763320940L;

    public FlowControlException() {}

    public FlowControlException(String message) {
        super(message);
    }

    public FlowControlException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlowControlException(Throwable cause) {
        super(cause);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
