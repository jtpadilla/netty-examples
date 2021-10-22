package nia.chapter2.echoserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {

    static final private int PORT = 7080;

    public static void main(String[] args) throws Exception {
        new EchoServer(PORT).start();
    }

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {

        final EchoServerHandler serverHandler = new EchoServerHandler();
        final EventLoopGroup group = new NioEventLoopGroup();

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();

            // Se prepara una instancia de ChannelInitializer es un tipo de ChannelHandler
            // (implementa ChannelHandler, ChannelInboundHandler).
            // Dicha instancoa sera utilizada para invocar el metodo 'initChannel(SocketChannel)'
            // cada vez que se abre un nuevo canel.
            // En el objeto de tipo SocketChannel se podran configurar su PipeLine
            // colocando los distintos ChannelHandlers encadenados...
            ChannelInitializer<SocketChannel> channelInitializer = new ChannelInitializer<>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(serverHandler);
                }
            };

            serverBootstrap
                    .group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(channelInitializer);

            // El 'bind' enlaza el servidor por el puerto y IP indicada
            // y 'sync' espera a que dicha operacion termine.
            ChannelFuture f = serverBootstrap.bind().sync();
            System.out.println(EchoServer.class.getName() + " started and listening for connections on " + f.channel().localAddress());

            // El ChannelFuture que no da 'closeFuture()' nos permite esperar a que
            // se cierre el server socket.
            f.channel().closeFuture().sync();

        } finally {
            // detiene todos los threads y liberar los recursos.
            group.shutdownGracefully().sync();
        }

    }
}
