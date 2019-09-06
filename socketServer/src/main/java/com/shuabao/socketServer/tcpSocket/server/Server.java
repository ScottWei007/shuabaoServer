package com.shuabao.socketServer.tcpSocket.server;

import com.shuabao.socketServer.tcpSocket.processor.ProviderProcessor;
import com.shuabao.socketServer.tcpSocket.processor.DefaultProviderProcessor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Scott Wei on 8/3/2018.
 */
@Component
public class Server {
    private final Logger logger = LoggerFactory.getLogger(Server.class);
    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private static final int bossThreads = 1;
    private static final int workerThreads = Runtime.getRuntime().availableProcessors() << 1;
    private static final int bossiIoRatio = 100;
    private static final int workerIoRatio = 100;
    private ServerBootstrap bootstrap;
    private final LowCopyProtocolEncoder lowCopyProtocolEncoder = new LowCopyProtocolEncoder();
    private final ProviderProcessor providerProcessor = new DefaultProviderProcessor();
    private final BussinessHandler bussinessHandler = new BussinessHandler(providerProcessor);

    @Value("${socketserver.port}")
    private int port;

    //初始化bean時實行該方法
    @PostConstruct
    public void start() {
        try {
            initEventLoopGroup();
            bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .childHandler(new MyChannelInitializer(lowCopyProtocolEncoder, bussinessHandler))
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(512 * 1024, 1024 * 1024))
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .childOption(ChannelOption.ALLOW_HALF_CLOSURE, false);
            if(Epoll.isAvailable()) {
                bootstrap.option(EpollChannelOption.SO_REUSEPORT, false)
                        .option(EpollChannelOption.IP_FREEBIND, false)
                        .option(EpollChannelOption.IP_TRANSPARENT, false)
                        .option(EpollChannelOption.EPOLL_MODE, EpollMode.EDGE_TRIGGERED)
                        .childOption(EpollChannelOption.TCP_CORK, false)
                        .childOption(EpollChannelOption.TCP_QUICKACK, true)
                        .childOption(EpollChannelOption.IP_TRANSPARENT, false)
                        .childOption(EpollChannelOption.EPOLL_MODE, EpollMode.EDGE_TRIGGERED);
            }
            ChannelFuture future = bootstrap.bind(new InetSocketAddress(port)).sync();
            logger.info("=============>tcp server start.");
            // wait until the server socket is closed,這裏阻塞住了
//            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("=============>tcp server error: " + e);
            e.printStackTrace();
        }
    }

    //關閉時釋放資源
    @PreDestroy
    public void destroy(){
        boss.shutdownGracefully().syncUninterruptibly();
        worker.shutdownGracefully().syncUninterruptibly();
        providerProcessor.shutdown();//關閉資源
    }

    private final void initEventLoopGroup() {
        ThreadFactory boosTF = new DefaultThreadFactory("shuabao.netty.boss",Thread.MAX_PRIORITY);
        ThreadFactory workerTF = new DefaultThreadFactory("shuabao.netty.worker",Thread.MAX_PRIORITY);
        if(Epoll.isAvailable()) {
            EpollEventLoopGroup bossEpollEventLoopGroup = new EpollEventLoopGroup(bossThreads, boosTF);
            bossEpollEventLoopGroup.setIoRatio(bossiIoRatio);
            boss = bossEpollEventLoopGroup;
            EpollEventLoopGroup workerEpollEventLoopGroup = new EpollEventLoopGroup(workerThreads, workerTF);
            workerEpollEventLoopGroup.setIoRatio(workerIoRatio);
            worker = workerEpollEventLoopGroup;
        }else {
            NioEventLoopGroup bossNioEventLoopGroup = new NioEventLoopGroup(bossThreads, boosTF);
            bossNioEventLoopGroup.setIoRatio(bossiIoRatio);
            boss = bossNioEventLoopGroup;
            NioEventLoopGroup workerNioEventLoopGroup = new NioEventLoopGroup(workerThreads, workerTF);
            workerNioEventLoopGroup.setIoRatio(workerIoRatio);
            worker = workerNioEventLoopGroup;
        }
    }
}
