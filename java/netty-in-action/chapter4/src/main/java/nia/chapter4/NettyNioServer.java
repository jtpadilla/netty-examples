package nia.chapter4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * Asynchronous networking with Netty
 */
public class NettyNioServer {

    public void server(int port) throws Exception {

        final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", StandardCharsets.UTF_8));

        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            ChannelInitializer<SocketChannel> channelInitializer = new ChannelInitializer<>() {
                @Override
                public void initChannel(SocketChannel ch) {

                    ChannelInboundHandlerAdapter channelInboundHandlerAdapter = new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) {
                            ctx.writeAndFlush(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);
                        }
                    };

                    ch.pipeline().addLast(channelInboundHandlerAdapter);

                }

            };

            // Se ensambla el ServerBootstrap
            serverBootstrap
                    .group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(channelInitializer);

            // Se ejecute el Binding del ServerBootstrap, se obtiene el Future del Binding y esperamos que finalice.
            ChannelFuture f = serverBootstrap.bind().sync();

            // Se obtiene el future del Close de canal (el close no se ejecuta aqui) y esperamos que se cierre.
            f.channel().closeFuture().sync();

        } finally {
            // Se liberan los thread del grupo y los demas recursos
            group.shutdownGracefully().sync();

        }

    }

}

