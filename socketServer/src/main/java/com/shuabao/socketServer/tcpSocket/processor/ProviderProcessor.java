package com.shuabao.socketServer.tcpSocket.processor;

import com.shuabao.socketServer.tcpSocket.payload.RequestPayload;

/**
 * Created by Scott Wei on 8/4/2018.
 */
public interface ProviderProcessor {

    void handleRequest(IChannel iChannel, RequestPayload requestPayload) throws Exception;

    void handleException(IChannel iChannel, RequestPayload requestPayload);

    void shutdown();
}
