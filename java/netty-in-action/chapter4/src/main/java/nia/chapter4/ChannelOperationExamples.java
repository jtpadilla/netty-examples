package nia.chapter4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChannelOperationExamples {

    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    /**
     * Escribiendo en un canal
     */
    public static void writingToChannel() {

        // Se obtiene un canal desde ....
        Channel channel = CHANNEL_FROM_SOMEWHERE;

        // Se carga un buffer
        ByteBuf buf = Unpooled.copiedBuffer("your data", CharsetUtil.UTF_8);

        // Se escribe en el canal y se obtiene el Future para saber cuano se ha ejecutado la escritura
        ChannelFuture cf = channel.writeAndFlush(buf);

        ChannelFutureListener channelFutureListener = (ChannelFuture future) -> {
            if (future.isSuccess()) {
                // La escritura ha sido correcta
                System.out.println("Write successful");

            } else {
                // La escritura produjo un error
                System.err.println("Write error");
                future.cause().printStackTrace();

            }
        };

        // Se instala un listener en el future para recibir la notificacion del resultado
        cf.addListener(channelFutureListener);
    }

    /**
     * Utilizando un canal desde multipes threads
     */
    public static void writingToChannelFromManyThreads() {

        // Se obtiene un canal desde ....
        final Channel channel = CHANNEL_FROM_SOMEWHERE;

        // Se carga un buffer
        final ByteBuf buf = Unpooled.copiedBuffer("your data", CharsetUtil.UTF_8);

        // Se prepara un Runable para poder lanzarlo (normalmente) desde otro thread
        Runnable writer = ()-> {
            channel.write(buf.duplicate());
        };

        // Se prepara un Executor con multiples threads
        Executor executor = Executors.newCachedThreadPool();

        // Se escribe desde un thread
        executor.execute(writer);

        // Se escribe desde otro thread
        executor.execute(writer);

        //...

    }

}
