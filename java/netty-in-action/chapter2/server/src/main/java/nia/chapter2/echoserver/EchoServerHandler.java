package nia.chapter2.echoserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

@Sharable
// Este handler es compartido asi que la misma instancia sera utilizada para
// todos los canales.
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // Se reciben datos desde un cliente
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Recibido en el servidor: " + in.toString(CharsetUtil.UTF_8));
        // Lo mismo que se recibe desde el cliente se le envia a el de nuevo
        ctx.write(in);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // Este evento indica que ya no se reciben mas datos en la misma remesa.
        // Yo interpreto que se ha recibido aguna ocsa y despues de un tiempo
        // ya no se ha recibido nada mas.
        //
        // Se envia NADA pero se aprovache para hacer un flush y finalmente cerrar el canal.
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Cualquier error llega por ese evento
        cause.printStackTrace();
        // Se cierra el canal
        ctx.close();
    }

}
