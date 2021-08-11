package com.github.tavenyin.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    Object object = new Object();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println(object);
        System.out.println("channelRead currentThread: " + Thread.currentThread().getName());
        ByteBuf in = (ByteBuf) msg;
        System.out.println(
                "Server received: " + in.toString(CharsetUtil.UTF_8));
        ctx.write(in).addListener(future -> {
            System.out.println("write callback, currentThread: " + Thread.currentThread().getName());
        });
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("channelReadComplete currentThread: " + Thread.currentThread().getName());
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
