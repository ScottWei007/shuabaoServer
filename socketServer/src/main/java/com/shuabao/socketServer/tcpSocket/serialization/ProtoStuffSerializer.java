

package com.shuabao.socketServer.tcpSocket.serialization;

import com.shuabao.socketServer.util.ThrowUtil;
import io.protostuff.Input;
import io.protostuff.LinkedBuffer;
import io.protostuff.Output;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;


import java.io.IOException;

/**
 * Protostuff的序列化/反序列化实现,默认的实现.
 *

 */
public final class ProtoStuffSerializer extends Serializer {

    @Override
    public byte code() {
        return 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> OutputBuf writeObject(OutputBuf outputBuf, T obj) {
        Schema<T> schema = RuntimeSchema.getSchema((Class<T>) obj.getClass());

        Output output = Outputs.getOutput(outputBuf);
        try {
            schema.writeTo(output, obj);
        } catch (IOException e) {
            ThrowUtil.throwException(e);
        }

        return outputBuf;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> byte[] writeObject(T obj) {
        Schema<T> schema = RuntimeSchema.getSchema((Class<T>) obj.getClass());

        LinkedBuffer buf = LinkedBuffers.getLinkedBuffer();
        Output output = Outputs.getOutput(buf);
        try {
            schema.writeTo(output, obj);
            return Outputs.toByteArray(output);
        } catch (IOException e) {
            ThrowUtil.throwException(e);
        } finally {
            LinkedBuffers.resetBuf(buf); // for reuse
        }

        return null; // never get here
    }

    @Override
    public <T> T readObject(InputBuf inputBuf, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T msg = schema.newMessage();

        Input input = Inputs.getInput(inputBuf);
        try {
            schema.mergeFrom(input, msg);
            Inputs.checkLastTagWas(input, 0);
        } catch (IOException e) {
            ThrowUtil.throwException(e);
        } finally {
            inputBuf.release();
        }

        return msg;
    }

    @Override
    public <T> T readObject(byte[] bytes, int offset, int length, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T msg = schema.newMessage();

        Input input = Inputs.getInput(bytes, offset, length);
        try {
            schema.mergeFrom(input, msg);
            Inputs.checkLastTagWas(input, 0);
        } catch (IOException e) {
            ThrowUtil.throwException(e);
        }

        return msg;
    }

    @Override
    public String toString() {
        return "proto_stuff:(code=" + code() + ")";
    }
}
