package com.tyza66.nettyweb;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

public class TestFetch {
    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpClientCodec()); // HTTP 编解码
                            pipeline.addLast(new HttpObjectAggregator(8192)); // 聚合 HTTP 消息
                            pipeline.addLast(new SimpleChannelInboundHandler<FullHttpResponse>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse response) {
                                    System.out.println("Response:");
                                    System.out.println(response.content().toString(io.netty.util.CharsetUtil.UTF_8));
                                }
                            });
                        }
                    });

            // 创建请求
            Channel channel = bootstrap.connect("www.example.com", 80).sync().channel();
            FullHttpRequest request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.GET, "/");
            request.headers().set(HttpHeaderNames.HOST, "www.example.com");
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);

            // 发送请求
            channel.writeAndFlush(request).sync();
            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
