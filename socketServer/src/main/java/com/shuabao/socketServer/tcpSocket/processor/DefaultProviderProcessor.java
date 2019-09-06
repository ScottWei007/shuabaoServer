package com.shuabao.socketServer.tcpSocket.processor;

import com.shuabao.core.base.ReturnCode;
import com.shuabao.socketServer.tcpSocket.excutor.CloseableExecutor;
import com.shuabao.socketServer.tcpSocket.excutor.MessageTask;
import com.shuabao.socketServer.tcpSocket.payload.RequestPayload;
import com.shuabao.socketServer.tcpSocket.protocal.Status;
import com.shuabao.socketServer.tcpSocket.serialization.OutputBuf;
import com.shuabao.socketServer.tcpSocket.serialization.Serializer;
import com.shuabao.socketServer.tcpSocket.serialization.SerializerFactory;
import com.shuabao.socketServer.util.StackTraceUtil;
import com.shuabao.core.rpc.bean.MessageWrapper;
import com.shuabao.socketServer.tcpSocket.payload.MyRequest;
import com.shuabao.socketServer.tcpSocket.payload.ResponsePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Scott Wei on 8/4/2018.
 */
public class DefaultProviderProcessor implements ProviderProcessor{
    private static final Logger logger = LoggerFactory.getLogger(DefaultProviderProcessor.class);
    private final CloseableExecutor executor;

    public DefaultProviderProcessor() {
        this(ProviderExecutors.executor());
    }

    public DefaultProviderProcessor(CloseableExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void handleRequest(IChannel channel, RequestPayload requestPayload) throws Exception {
        MessageTask task = new MessageTask(channel, new MyRequest(requestPayload));
        if (executor == null) {
            task.run();
        } else {
            executor.execute(task);
        }
    }

    @Override
    public void handleException(IChannel channel, RequestPayload request) {
        MessageWrapper message = new MessageWrapper();
        message.setReturnInfo(ReturnCode.ERROR, message.getLang());
        Serializer serializer = SerializerFactory.getInstance();
        OutputBuf outputBuf = serializer.writeObject(channel.allocOutputBuf(), message);
        ResponsePayload response = new ResponsePayload(request.getInvokeId());
        response.setOutputBuf(outputBuf);
        response.status(Status.SERVER_ERROR.value());
        channel.write(response, new IFutureListener<IChannel>() {
            @Override
            public void operationSuccess(IChannel channel) throws Exception {
                logger.debug("Service error message sent out: {}.", channel);
            }
            @Override
            public void operationFailure(IChannel channel, Throwable cause) throws Exception {
                if (logger.isWarnEnabled()) {
                    logger.warn("Service error message sent failed: {}, {}.", channel, StackTraceUtil.stackTrace(cause));
                }
            }
        });
    }


    @Override
    public void shutdown() {
        if (executor != null) {
            executor.shutdown();
        }
    }


}
