package com.github.tavenyin.http.server;

import com.github.tavenyin.echo.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.Date;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class NettyHttpClient {
    private static final Bootstrap bootstrap;

    static {
        EventLoopGroup group = new NioEventLoopGroup(1);
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress("localhost", 7777))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws Exception {
                        ch.pipeline().addLast(
                                new EchoClientHandler());
                    }
                });
    }

    public static void send(ChannelHandlerContext context) {
        System.out.println("Netty Server forward, date:" + new Date() +" Thread:" + Thread.currentThread().getName());
        ChannelFuture f = bootstrap.connect();
        // 请求读完之后的回调
        f.channel().closeFuture().addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("writeandpush, date:"+new Date() +" Thread:" + Thread.currentThread().getName());
                ByteBuf byteBuf = Unpooled.copiedBuffer("forward", CharsetUtil.UTF_8);
                FullHttpResponse httpResponse = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, byteBuf);
                httpResponse.headers()
                        .set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
                httpResponse.headers()
                        .setInt(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content()
                                .readableBytes());
                context.writeAndFlush(httpResponse);
            } else {

            }
        });

    }

}
