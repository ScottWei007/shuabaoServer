
package com.shuabao.socketServer.tcpSocket.excutor;



import com.codahale.metrics.Timer;
import com.shuabao.core.manager.EnvironmentManager;
import com.shuabao.socketServer.command.Command;
import com.shuabao.core.rpc.bean.MessageWrapper;
import com.shuabao.socketServer.tcpSocket.payload.MyRequest;
import com.shuabao.socketServer.tcpSocket.payload.RequestPayload;
import com.shuabao.socketServer.tcpSocket.payload.ResponsePayload;
import com.shuabao.socketServer.tcpSocket.processor.IChannel;
import com.shuabao.socketServer.tcpSocket.processor.IFutureListener;
import com.shuabao.socketServer.tcpSocket.processor.Metrics;
import com.shuabao.socketServer.tcpSocket.protocal.Status;
import com.shuabao.socketServer.tcpSocket.serialization.InputBuf;
import com.shuabao.socketServer.tcpSocket.serialization.OutputBuf;
import com.shuabao.socketServer.tcpSocket.serialization.Serializer;
import com.shuabao.socketServer.tcpSocket.serialization.SerializerFactory;
import com.shuabao.socketServer.util.SystemClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


/**
 *

 * org.jupiter.rpc.provider.processor.task
 */
public class MessageTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MessageTask.class);

    private static final boolean METRIC_NEEDED = false;

    private final IChannel channel;
    private final MyRequest request;

    public MessageTask(IChannel channel, MyRequest request) {
        this.channel = channel;
        this.request = request;
    }

    @Override
    public void run() {
        // stack copy
        final MyRequest _request = request;
        try {
            // 在业务线程中反序列化, 减轻IO线程负担
            Serializer serializer = SerializerFactory.getInstance();
            RequestPayload _requestPayload = _request.payload();
            InputBuf inputBuf = _requestPayload.getInputBuf();
            MessageWrapper msg = serializer.readObject(inputBuf, MessageWrapper.class);
            _request.message(msg);
            _requestPayload.clear();
            //處理業務
            process(_request);
        } catch (Exception e) {
            logger.error(">>>>>>>>>MessageTask.run>>>>>>>>>cause:{}",e.getMessage());
        }
    }

    private final void process(MyRequest _request) {
        ///////////////////////
        System.out.println(_request.message());


        //處理業務,根據invokeId命令號去尋找對於的業務(bean的name)
        long invokeId = _request.invokeId();
        Command command = (Command) EnvironmentManager.getApplicationContext().getBean(String.valueOf(invokeId));
        if(invokeId == Integer.valueOf(Command.AUTHUSERCOMMAND) || invokeId == Integer.valueOf(Command.RECONNECTCOMMAND)) {//用戶認證,斷綫重鏈需要用到channel
            _request.message().putData("channel", channel);
        }
        MessageWrapper resultMessage = command.executeCommand(_request.message());
        Serializer serializer = SerializerFactory.getInstance();
        OutputBuf outputBuf = serializer.writeObject(channel.allocOutputBuf(), resultMessage);
        ResponsePayload responsePayload = new ResponsePayload(invokeId);
        responsePayload.setOutputBuf(outputBuf);
        responsePayload.status(Status.OK.value());
        handleWriteResponse(responsePayload);
    }

    private final void handleWriteResponse(ResponsePayload response) {
        channel.write(response, new IFutureListener<IChannel>() {
            @Override
            public void operationSuccess(IChannel channel) throws Exception {
                if (METRIC_NEEDED) {
                    long duration = SystemClock.millisClock().now() - request.timestamp();
                    MetricsHolder.processingTimer.update(duration, TimeUnit.MILLISECONDS);
                }
            }
            @Override
            public void operationFailure(IChannel channel, Throwable cause) throws Exception {
                long duration = SystemClock.millisClock().now() - request.timestamp();
                logger.error("Response sent failed, invokeId: {}, duration: {} millis, channel: {}, cause: {}.", request.invokeId(), duration, channel, cause);
            }
        });
    }

    // - Metrics -------------------------------------------------------------------------------------------------------
    static class MetricsHolder {
        // 请求处理耗时统计(从request被解码开始, 到response数据被刷到OS内核缓冲区为止)
        static final Timer processingTimer = Metrics.timer("processing");
    }
}
