

package com.shuabao.socketServer.tcpSocket.processor;


public class ControlResult {

    public static final ControlResult ALLOWED = new ControlResult(true);

    private final boolean allowed;
    private final String message;

    public ControlResult(boolean allowed) {
        this.allowed = allowed;
        this.message = null;
    }

    public ControlResult(boolean allowed, String message) {
        this.allowed = allowed;
        this.message = message;
    }

    @SuppressWarnings("all")
    public boolean isAllowed() {
        return allowed;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ControlResult{" +
                "allowed=" + allowed +
                ", message='" + message + '\'' +
                '}';
    }
}
