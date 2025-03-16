package com.tyza66.nettyweb;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;

public class NettyHttpServer {
    private final int port;

    public NettyHttpServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // 用于接收连接
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // 用于处理连接
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpServerCodec()); // HTTP 编解码器
                            pipeline.addLast(new HttpObjectAggregator(65536)); // 聚合器
                            pipeline.addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
                                    String uri = request.uri(); // 获取请求路径
                                    FullHttpResponse response;

                                    if ("/".equals(uri)) { // 检查路径是否是 /
                                        response = new DefaultFullHttpResponse(
                                                HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                                        response.content().writeBytes("Welcome to Netty!".getBytes());
                                        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                                        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
                                    }
                                    else if ("/hello".equals(uri)) { // 检查路径是否是 /hello
                                        response = new DefaultFullHttpResponse(
                                                HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                                        response.content().writeBytes("world".getBytes());
                                        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                                        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
                                    } else { // 处理其他路径
                                        response = new DefaultFullHttpResponse(
                                                HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
                                        response.content().writeBytes("Not Found".getBytes());
                                        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                                        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
                                    }
                                    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                                }
                            });
                        }
                    });

            ChannelFuture future = bootstrap.bind(port).sync(); // 绑定端口并启动
            System.out.println("Server started at port " + port);
            future.channel().closeFuture().sync(); // 阻塞直到服务器关闭
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

// 单响应
//public class NettyHttpServer {
//    private final int port;
//
//    public NettyHttpServer(int port) {
//        this.port = port;
//    }
//
//    public void start() throws Exception {
//        EventLoopGroup bossGroup = new NioEventLoopGroup(); // 用于接收连接
//        EventLoopGroup workerGroup = new NioEventLoopGroup(); // 用于处理连接
//        try {
//            ServerBootstrap bootstrap = new ServerBootstrap();
//            bootstrap.group(bossGroup, workerGroup)
//                    .channel(NioServerSocketChannel.class)
//                    .childHandler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        protected void initChannel(SocketChannel ch) {
//                            ChannelPipeline pipeline = ch.pipeline();
//                            pipeline.addLast(new HttpServerCodec()); // HTTP 编解码器
//                            pipeline.addLast(new HttpObjectAggregator(65536)); // 聚合器
//                            pipeline.addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
//                                @Override
//                                protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
//                                    // 构建 HTTP 响应
//                                    FullHttpResponse response = new DefaultFullHttpResponse(
//                                            HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
//                                    response.content().writeBytes("Hello, Netty!".getBytes());
//                                    response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
//                                    response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
//                                    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
//                                }
//                            });
//                        }
//                    });
//
//            ChannelFuture future = bootstrap.bind(port).sync(); // 绑定端口并启动
//            System.out.println("Server started at port " + port);
//            future.channel().closeFuture().sync(); // 阻塞直到服务器关闭
//        } finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
//    }
//
//}
