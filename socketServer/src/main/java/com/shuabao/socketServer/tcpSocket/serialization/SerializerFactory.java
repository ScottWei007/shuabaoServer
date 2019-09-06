package com.shuabao.socketServer.tcpSocket.serialization;

/**
 * Created by Scott Wei on 8/8/2018.
 */
public class SerializerFactory {
    private static final Serializer serializer = new ProtoStuffSerializer();

    public static final Serializer getInstance() {
        return serializer;
    }
}
