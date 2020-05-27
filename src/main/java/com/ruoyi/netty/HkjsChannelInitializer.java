package com.ruoyi.netty;

import com.ruoyi.netty.handler.FeederLinkFrameDecoder;
import com.ruoyi.netty.handler.NettyDataHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.LineEncoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HkjsChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final NettyDataHandler handler;
    private final FeederLinkFrameDecoder flfDecoder;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

//        pipeline.addLast(new LineEncoder());
//        pipeline.addLast(new StringEncoder());

        pipeline.addLast(new FixedLengthFrameDecoder(168));
        pipeline.addLast(flfDecoder);
        pipeline.addLast(handler);

    }
}
