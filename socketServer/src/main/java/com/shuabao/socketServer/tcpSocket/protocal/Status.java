

package com.shuabao.socketServer.tcpSocket.protocal;

/**
 * Response status.

 */
public enum Status {
    REQUEST                     ((byte) 10, "REQUEST"),                   //请求
    OK                          ((byte) 20, "OK"),                        // 正常 - 请求已完成
    CLIENT_ERROR                ((byte) 30, "CLIENT_ERROR"),              // 内部错误 — 因为意外情况, 客户端不能发送请求
    CLIENT_TIMEOUT              ((byte) 31, "CLIENT_TIMEOUT"),            // 超时 - 客户端超时
    SERVER_TIMEOUT              ((byte) 32, "SERVER_TIMEOUT"),            // 超时 - 服务端超时
    BAD_REQUEST                 ((byte) 40, "BAD_REQUEST"),               // 错误请求 — 请求中有语法问题, 或不能满足请求
    SERVICE_NOT_FOUND           ((byte) 44, "SERVICE_NOT_FOUND"),         // 找不到 - 指定服务不存在
    SERVER_ERROR                ((byte) 50, "SERVER_ERROR"),              // 内部错误 — 因为意外情况, 服务器不能完成请求
    SERVER_BUSY                 ((byte) 51, "SERVER_BUSY"),               // 内部错误 — 服务器太忙, 无法处理新的请求
    SERVICE_EXPECTED_ERROR      ((byte) 52, "SERVICE_EXPECTED_ERROR"),    // 服务错误 - 服务执行时出现预期内的异常
    SERVICE_UNEXPECTED_ERROR    ((byte) 53, "SERVICE_UNEXPECTED_ERROR"),  // 服务错误 - 服务执行意外出错
    APP_FLOW_CONTROL            ((byte) 54, "APP_FLOW_CONTROL"),          // 服务错误 - App级别服务限流
    PROVIDER_FLOW_CONTROL       ((byte) 55, "PROVIDER_FLOW_CONTROL"),     // 服务错误 - Provider级别服务限流
    DESERIALIZATION_FAIL        ((byte) 60, "DESERIALIZATION_FAIL"),      // 客户端反序列化错误
    UNAUTH                      ((byte) 90, "UNAUTH");                    // 未登陸認證

    Status(byte value, String description) {
        this.value = value;
        this.description = description;
    }

    private byte value;
    private String description;

    public static Status parse(byte value) {
        for (Status s : values()) {
            if (s.value == value) {
                return s;
            }
        }
        return null;
    }

    public byte value() {
        return value;
    }

    public String description() {
        return description;
    }

    @Override
    public String toString() {
        return description();
    }
}
