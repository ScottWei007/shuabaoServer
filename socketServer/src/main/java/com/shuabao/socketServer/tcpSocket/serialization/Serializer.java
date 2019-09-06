

package com.shuabao.socketServer.tcpSocket.serialization;


public abstract class Serializer {

    public abstract byte code();

    public abstract <T> OutputBuf writeObject(OutputBuf outputBuf, T obj);

    public abstract <T> byte[] writeObject(T obj);

    public abstract <T> T readObject(InputBuf inputBuf, Class<T> clazz);

    public abstract <T> T readObject(byte[] bytes, int offset, int length, Class<T> clazz);

    public <T> T readObject(byte[] bytes, Class<T> clazz) {
        return readObject(bytes, 0, bytes.length, clazz);
    }
}
