package nia.chapter2.echoclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class EchoClient {

    static final private int PORT = 7080;
    static final private String HOST = "localhost";

    public static void main(String[] args) throws Exception {
        new EchoClient(HOST, PORT).start();
    }

    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {

        final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        final ChannelInitializer<SocketChannel> channelInitializer = new ChannelInitializer<>() {
            @Override
            public void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(new EchoClientHandler());
            }
        };

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host, port))
                .handler(channelInitializer);

            ChannelFuture channelFuture = bootstrap.connect().sync();

            channelFuture.channel().closeFuture().sync();

        } finally {
            eventLoopGroup.shutdownGracefully().sync();
        }

    }

}

