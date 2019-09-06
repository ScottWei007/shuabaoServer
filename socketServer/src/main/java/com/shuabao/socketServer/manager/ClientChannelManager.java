package com.shuabao.socketServer.manager;

import com.shuabao.socketServer.tcpSocket.serialization.SerializerFactory;
import com.shuabao.core.rpc.bean.MessageWrapper;
import com.shuabao.socketServer.tcpSocket.payload.RequestPayload;
import com.shuabao.socketServer.tcpSocket.processor.IChannel;
import com.shuabao.socketServer.tcpSocket.processor.IChannelGroup;
import com.shuabao.socketServer.tcpSocket.processor.NettyChannel;
import com.shuabao.socketServer.tcpSocket.serialization.OutputBuf;
import com.shuabao.socketServer.tcpSocket.serialization.Serializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Scott Wei on 8/9/2018.
 */
public class ClientChannelManager {
    //多綫程單利模式
    private static volatile ClientChannelManager instance;
    private static final Lock lock = new ReentrantLock();

    public static ClientChannelManager getInstance() {
        if(Objects.isNull(instance)) {
            try{
                lock.lock();
                if(Objects.isNull(instance)) {
                    instance = new ClientChannelManager();
                }
            }finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public ClientChannelManager() {
        userChannels = new ConcurrentHashMap<>();// 縂登陸的
        userGroup = new ConcurrentHashMap<>();
    }

    private final ConcurrentMap<Integer,NettyChannel> userChannels;
    private final ConcurrentMap<Integer, IChannelGroup> userGroup;

    public int totalOnline() {
        return userChannels.size();
    }

    public void addUserChannels(int uid,  NettyChannel nettyChannel) {
        userChannels.put(uid, nettyChannel);
    }

    public void remove(int uid) {
        //刪除uid對應的channel
        userChannels.remove(uid);
    }

    public void notifyAllOnline(MessageWrapper messageWrapper) {
        final List<NettyChannel> channels = new ArrayList<>(userChannels.values());
        IChannel iChannel;
        for(int i = 0, size = channels.size() ; i < size; i ++) {
            iChannel = channels.get(i);
            if(iChannel.isWritable()) {
                Serializer serializer = SerializerFactory.getInstance();
                OutputBuf outputBuf = serializer.writeObject(iChannel.allocOutputBuf(), messageWrapper);
                RequestPayload requestPayload = new RequestPayload(88888);
                requestPayload.setOutputBuf(outputBuf);
                iChannel.write(requestPayload);
            }
        }
    }

    public boolean notifyOneOnline(int uid, MessageWrapper messageWrapper) {
        IChannel iChannel = userChannels.get(uid);
        if(Objects.nonNull(iChannel) && iChannel.isWritable()) {
            Serializer serializer = SerializerFactory.getInstance();
            OutputBuf outputBuf = serializer.writeObject(iChannel.allocOutputBuf(), messageWrapper);
            RequestPayload requestPayload = new RequestPayload(66666);
            requestPayload.setOutputBuf(outputBuf);
            iChannel.write(requestPayload);
            return true;
        }
        return false;
    }
}
