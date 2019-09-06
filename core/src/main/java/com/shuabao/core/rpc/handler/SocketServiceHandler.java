package com.shuabao.core.rpc.handler;

import com.shuabao.core.rpc.bean.MessageWrapper;

import java.rmi.RemoteException;

/**
 * Created by Scott Wei on 9/13/2018.
 */
public interface SocketServiceHandler extends RpcHandler{

    boolean pushMessage(int uid, MessageWrapper messageWrapper) throws RemoteException;

}
