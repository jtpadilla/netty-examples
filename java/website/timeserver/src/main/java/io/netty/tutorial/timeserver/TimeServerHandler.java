package io.netty.tutorial.timeserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.function.Consumer;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        // Se prepara el buffer
        final ByteBuf time = ctx.alloc().buffer(4);

        // Se carga el buffer con el resultado
        int timeData = (int) (System.currentTimeMillis() / 1000L + 2208988800L);
        time.writeInt(timeData);

        // Se evia al resultado
        final ChannelFuture channelFuture = ctx.writeAndFlush(time);

        // Opcion 1
        channelFuture.addListener(ChannelFutureListener.CLOSE);

        // Opcion 2
/*
        channelFuture.addListener((ChannelFutureListener) channelFutureParam -> {
            assert channelFuture == channelFutureParam;
            ctx.close();
        });
*/

        // Opcion 3
/*
        final ChannelFutureListener close = c -> {
            ctx.close();
        };
        channelFuture.addListener(close);
 */

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
