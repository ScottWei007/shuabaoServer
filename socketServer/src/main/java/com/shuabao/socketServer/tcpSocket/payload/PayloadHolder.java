package com.shuabao.socketServer.tcpSocket.payload;

import com.shuabao.socketServer.tcpSocket.serialization.OutputBuf;
import com.shuabao.socketServer.tcpSocket.serialization.InputBuf;

/**
 * Created by Scott Wei on 8/4/2018.
 */
public abstract class PayloadHolder {
    private byte[] bytes;
    private InputBuf inputBuf;
    private OutputBuf outputBuf;

    public void clear() {
        bytes = null;
        inputBuf = null;
        outputBuf = null;
    }

    public int size() {
        return (bytes == null ? 0 : bytes.length)
                + (inputBuf == null ? 0 : inputBuf.size())
                + (outputBuf == null ? 0 : outputBuf.size());
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public InputBuf getInputBuf() {
        return inputBuf;
    }

    public void setInputBuf(InputBuf inputBuf) {
        this.inputBuf = inputBuf;
    }

    public OutputBuf getOutputBuf() {
        return outputBuf;
    }

    public void setOutputBuf(OutputBuf outputBuf) {
        this.outputBuf = outputBuf;
    }
}
