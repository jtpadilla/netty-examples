package nia.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

public class BootstrapClientWithOptionsAndAttrs {

    static public class MySimpleChannelInboundHandler extends SimpleChannelInboundHandler<io.netty.buffer.ByteBuf> {

        final private AttributeKey<Integer> id;

        public MySimpleChannelInboundHandler(AttributeKey<Integer> id) {
            this.id = id;
        }

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) {
            Integer idValue = ctx.channel().attr(id).get();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
            System.out.println("Received data");
        }

    };

    public void bootstrap() {

        final AttributeKey<Integer> id = AttributeKey.newInstance("ID");

        Bootstrap bootstrap = new Bootstrap();

        bootstrap
            .group(new NioEventLoopGroup())
            .channel(NioSocketChannel.class)
            .handler(new MySimpleChannelInboundHandler(id));

        bootstrap
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);

        bootstrap.attr(id, 123456);

        ChannelFuture future = bootstrap.connect(new InetSocketAddress("www.manning.com", 80));

        future.syncUninterruptibly();

    }

}
