package io.netty.tutorial.discardserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServer {

    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            // Esta clase auxiliar que nos ayudara a configurar y lanzar el servidor.
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            // Cada vez que una nueva conexion se convierta en nuevo channel, esta instancia
            // de esta clase se encargara de instalar lso distintos handles que se
            // encadenaran para transformar lso datos.
            ChannelHandler channelHandler = new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new DiscardServerHandler());
                }
            };

            // Se asignan los Event loops multithread para el servidor.
            serverBootstrap.group(bossGroup, workerGroup);

            // Se configura la clase encargada de instancual los nuevos canales
            // para cad nueva conexion.
            serverBootstrap.channel(NioServerSocketChannel.class);

            // Se instalar el handler que se encargara de configurar cada nuevo channel
            serverBootstrap.childHandler(channelHandler);

            // Configura el numero maximo de peticiones pendientes en el ServerSocket
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);

            // Activa el KeepAlive en cada una de las conexiones que se establezcan
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

            // Se enlaza el server socket con el puerto indicado (y en este caso
            // por todos los interfaces de red)
            ChannelFuture channelFuture = serverBootstrap.bind(port);

            // Se espera hasta que el el bind() se terminado (y dispara
            // cualquier excepcion que se produzca durante el proceso).
            channelFuture.sync();

            // Wait until the server socket is closed.
            channelFuture.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new DiscardServer(port).run();
    }

}
