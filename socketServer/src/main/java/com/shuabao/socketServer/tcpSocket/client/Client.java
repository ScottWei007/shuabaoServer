package com.shuabao.socketServer.tcpSocket.client;

import com.shuabao.socketServer.tcpSocket.serialization.SerializerFactory;
import com.shuabao.core.rpc.bean.MessageWrapper;
import com.shuabao.socketServer.tcpSocket.payload.RequestPayload;
import com.shuabao.socketServer.tcpSocket.processor.IChannel;
import com.shuabao.socketServer.tcpSocket.processor.NettyChannel;
import com.shuabao.socketServer.tcpSocket.protocal.ProtocolHeader;
import com.shuabao.socketServer.tcpSocket.protocal.Status;
import com.shuabao.socketServer.tcpSocket.serialization.OutputBuf;
import com.shuabao.socketServer.tcpSocket.serialization.Serializer;
import com.shuabao.socketServer.tcpSocket.server.LowCopyProtocolDecoder;
import com.shuabao.socketServer.tcpSocket.server.LowCopyProtocolEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.epoll.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by Scott Wei on 8/8/2018.
 */
public class Client {

    private Bootstrap bootstrap;
    private EventLoopGroup worker;
    private final int workerThreads = Runtime.getRuntime().availableProcessors() << 1;
    private static final int workerIoRatio = 100;
    private final ClientIdleStateTrigger clientIdleStateTrigger = new ClientIdleStateTrigger();
    private final LowCopyProtocolEncoder lowCopyProtocolEncoder = new LowCopyProtocolEncoder();
    private final ClientHandler clientHandler = new ClientHandler();

    public void start(String host, int port, final int uid){
        try {
            ThreadFactory workerTF = new DefaultThreadFactory("shuabao.netty.worker",Thread.MAX_PRIORITY);
            if(Epoll.isAvailable()) {
                EpollEventLoopGroup workerEpollEventLoopGroup = new EpollEventLoopGroup(workerThreads, workerTF);
                workerEpollEventLoopGroup.setIoRatio(workerIoRatio);
                worker = workerEpollEventLoopGroup;
            }else {
                NioEventLoopGroup workerNioEventLoopGroup = new NioEventLoopGroup(workerThreads, workerTF);
                workerNioEventLoopGroup.setIoRatio(workerIoRatio);
                worker = workerNioEventLoopGroup;
            }
            bootstrap = new Bootstrap();
            bootstrap.group(worker)
                    .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new IdleStateHandler(0,30,0));
                            pipeline.addLast(clientIdleStateTrigger);
                            pipeline.addLast(new LowCopyProtocolDecoder());
                            pipeline.addLast(lowCopyProtocolEncoder);
                            pipeline.addLast(clientHandler);
                        }
                    });
            bootstrap.option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(512 * 1024, 1024 * 1024))
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .option(ChannelOption.ALLOW_HALF_CLOSURE, false);
            if(Epoll.isAvailable()) {
                bootstrap.option(EpollChannelOption.TCP_CORK, false)
                        .option(EpollChannelOption.TCP_QUICKACK, true)
                        .option(EpollChannelOption.IP_TRANSPARENT, false)
                        .option(EpollChannelOption.EPOLL_MODE, EpollMode.EDGE_TRIGGERED);
            }

            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(host, port)).sync();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if(channelFuture.isSuccess()) {
                        System.out.println("uid:"+uid+" connect to host:" + host + " port:" + port);

                        IChannel iChannel = NettyChannel.attachChannel(channelFuture.channel());
                        MessageWrapper messageWrapper = new MessageWrapper();
                        messageWrapper.putData("uid", uid);
                        messageWrapper.putData("token","token");
                        Serializer serializer = SerializerFactory.getInstance();
                        OutputBuf outputBuf = serializer.writeObject(iChannel.allocOutputBuf(), messageWrapper);
                        RequestPayload requestPayload = new RequestPayload(1000);
                        requestPayload.setOutputBuf(outputBuf);
                        long invokeId = requestPayload.getInvokeId();
                        ByteBuf byteBuf = (ByteBuf) requestPayload.getOutputBuf().backingObject();
                        int length = byteBuf.readableBytes();
                        byteBuf.markWriterIndex();
                        byteBuf.writerIndex(byteBuf.writerIndex() - length);
                        byteBuf.writeShort(ProtocolHeader.MAGIC)
                                .writeByte(ProtocolHeader.AUTH)
                                .writeByte(Status.REQUEST.value())//請求狀態
                                .writeLong(invokeId)
                                .writeInt(length - ProtocolHeader.HEADER_SIZE);

                        byteBuf.resetWriterIndex();
                        channelFuture.channel().writeAndFlush(byteBuf);
                        /*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(512);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                        objectOutputStream.writeObject(messageWrapper);
                        objectOutputStream.flush();
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        int length = bytes.length;
                        ByteBuf byteBuf = Unpooled.buffer(ProtocolHeader.HEADER_SIZE);
                        byteBuf.writeShort(ProtocolHeader.MAGIC)
                                .writeByte(ProtocolHeader.AUTH)
                                .writeByte(Status.REQUEST.value())//請求狀態
                                .writeLong(requestPayload.getInvokeId())
                                .writeInt(length)
                                .writeBytes(bytes);
                        channelFuture.channel().writeAndFlush(byteBuf);*/
                        taskSend(channelFuture.channel(),uid);

                    }else {
                        System.out.println("Client errror host:" + host + " port:" + port);
                    }
                }
            });
//            channelFuture.channel().closeFuture().sync();
        }catch (Exception e) {
            System.out.println("Client errror : " + e);
        }finally {
//            worker.shutdownGracefully();
        }

    }

    private void taskSend(Channel channel,int uid) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                IChannel iChannel = NettyChannel.attachChannel(channel);
                MessageWrapper messageWrapper = new MessageWrapper();
                messageWrapper.putData("msg","重鏈");
                messageWrapper.putData("uid",uid);
                Serializer serializer = SerializerFactory.getInstance();
                OutputBuf outputBuf = serializer.writeObject(iChannel.allocOutputBuf(), messageWrapper);
                RequestPayload requestPayload = new RequestPayload(1010);
                requestPayload.setOutputBuf(outputBuf);
                iChannel.write(requestPayload);
            }
        };
        worker.scheduleAtFixedRate(runnable,0,120, TimeUnit.SECONDS);
    }


    public static void main(String[] args) throws Exception{
        boolean isTest = false;
        String host = isTest ? "47.106.105.52" : "localhost";
        for(int i = 1; i <= 1; i ++) {
            Client client = new Client();
            client.start(host,28888, 7777);
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
