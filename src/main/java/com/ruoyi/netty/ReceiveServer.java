package com.ruoyi.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Slf4j
@RequiredArgsConstructor
@Component
public class ReceiveServer {

    private final ServerBootstrap serverBootstrap;

    private final InetSocketAddress addr;

    private Channel serverChannel;

    @Async
    public void start() {
        try {
            ChannelFuture f = serverBootstrap.bind(addr).sync();
            log.info("航空监视数据接收服务已经启动： 端口 {}" , addr.getPort());
            serverChannel = f.channel().closeFuture().sync().channel();
        } catch (InterruptedException e) {
            throw new NettyServerStartFailedException(e);
        }
    }

    @PreDestroy
    public void stop() {
        if( serverChannel != null) {
            serverChannel.close();
            serverChannel.parent().close();
        }
    }
}
