package nia.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class BootstrapSharingEventLoopGroup {

    static public class MySimpleChannelInboundHandler extends SimpleChannelInboundHandler<io.netty.buffer.ByteBuf> {

        ChannelFuture connectFuture;

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class).handler(
                new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
                        System.out.println("Received data");
                    }
                }
            );
            bootstrap.group(ctx.channel().eventLoop());
            connectFuture = bootstrap.connect(new InetSocketAddress("www.manning.com", 80));
        }

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
            if (connectFuture.isDone()) {
            }
        }

    }
    public void bootstrap() {

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap
            .group(new NioEventLoopGroup(), new NioEventLoopGroup())
            .channel(NioServerSocketChannel.class)
            .childHandler(new MySimpleChannelInboundHandler());

        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));

        future.addListener((ChannelFutureListener) channelFuture -> {
                if (channelFuture.isSuccess()) {
                    System.out.println("Server bound");
                } else {
                    System.err.println("Bind attempt failed");
                    channelFuture.cause().printStackTrace();
                }
            }
        );

    }
}
