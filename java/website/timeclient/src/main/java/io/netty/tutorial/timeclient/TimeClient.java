package io.netty.tutorial.timeclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {

    public static void main(String[] args) throws Exception {

        final String host = "localhost";
        final int port = 8080;

        ChannelHandler channelHandler = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new TimeDecoder(), new TimeClientHandler());
            }
        };

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(channelHandler);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

            // Se inicia el client
            ChannelFuture f = bootstrap.connect(host, port).sync();

            // Se espera a que se cierre la conexion
            f.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
        }

    }

}
