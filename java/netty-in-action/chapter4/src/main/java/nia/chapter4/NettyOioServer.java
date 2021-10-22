package nia.chapter4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Blocking networking with Netty
 */
public class NettyOioServer {

    public void server(int port) throws Exception {

        final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));

        EventLoopGroup group = new OioEventLoopGroup();

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
                .channel(OioServerSocketChannel.class)
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

