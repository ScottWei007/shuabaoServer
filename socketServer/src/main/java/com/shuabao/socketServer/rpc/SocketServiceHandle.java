package com.shuabao.socketServer.rpc;

import com.shuabao.core.rpc.bean.MessageWrapper;
import com.shuabao.core.rpc.handler.SocketServiceHandler;
import com.shuabao.socketServer.manager.ClientChannelManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Scott Wei on 9/13/2018.
 */
public class SocketServiceHandle extends UnicastRemoteObject implements SocketServiceHandler {

    protected SocketServiceHandle() throws RemoteException {
    }

    @Override
    public boolean pushMessage(int uid, MessageWrapper messageWrapper) throws RemoteException {
        return ClientChannelManager.getInstance().notifyOneOnline(uid, messageWrapper);
    }
}
