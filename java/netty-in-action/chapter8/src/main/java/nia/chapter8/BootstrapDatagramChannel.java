package nia.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

public class BootstrapDatagramChannel {

    public static class MySimpleChannelInboundHandler extends SimpleChannelInboundHandler<DatagramPacket> {

        @Override
        public void channelRead0(ChannelHandlerContext ctx,DatagramPacket msg) throws Exception {
        }

    }

    public void bootstrap() {

        Bootstrap bootstrap = new Bootstrap();

        bootstrap
            .group(new NioEventLoopGroup())
            .channel(NioDatagramChannel.class)
            .handler(new MySimpleChannelInboundHandler());

        ChannelFuture future = bootstrap.bind(new InetSocketAddress(0));

        future.addListener((ChannelFutureListener) channelFuture -> {
               if (channelFuture.isSuccess()) {
                   System.out.println("Channel bound");
               } else {
                   System.err.println("Bind attempt failed");
                   channelFuture.cause().printStackTrace();
               }
            }
        );

    }

}
