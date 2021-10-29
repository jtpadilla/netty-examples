package nia.chapter10.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.*;

import java.util.List;

@Sharable
public class WebSocketConvertHandler extends MessageToMessageCodec<WebSocketFrame, MyWebSocketFrame> {

     @Override
     protected void encode(ChannelHandlerContext ctx, MyWebSocketFrame msg, List<Object> out) throws Exception {

         ByteBuf payload = msg.getData().duplicate().retain();

         switch (msg.getType()) {

             case BINARY:
                 out.add(new BinaryWebSocketFrame(payload));
                 break;

             case TEXT:
                 out.add(new TextWebSocketFrame(payload));
                 break;

             case CLOSE:
                 out.add(new CloseWebSocketFrame(true, 0, payload));
                 break;

             case CONTINUATION:
                 out.add(new ContinuationWebSocketFrame(payload));
                 break;

             case PONG:
                 out.add(new PongWebSocketFrame(payload));
                 break;

             case PING:
                 out.add(new PingWebSocketFrame(payload));
                 break;

             default:
                 throw new IllegalStateException("Unsupported websocket msg " + msg);

         }

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {

        ByteBuf payload = msg.content().duplicate().retain();

        if (msg instanceof BinaryWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrameType.BINARY, payload));

        } else if (msg instanceof CloseWebSocketFrame) {
            out.add(new MyWebSocketFrame (MyWebSocketFrameType.CLOSE, payload));

        } else if (msg instanceof PingWebSocketFrame) {
            out.add(new MyWebSocketFrame (MyWebSocketFrameType.PING, payload));

        } else if (msg instanceof PongWebSocketFrame) {
            out.add(new MyWebSocketFrame (MyWebSocketFrameType.PONG, payload));

        } else if (msg instanceof TextWebSocketFrame) {
            out.add(new MyWebSocketFrame (MyWebSocketFrameType.TEXT, payload));

        } else if (msg instanceof ContinuationWebSocketFrame) {
            out.add(new MyWebSocketFrame (MyWebSocketFrameType.CONTINUATION, payload));

        } else {
            throw new IllegalStateException("Unsupported websocket msg " + msg);

        }

    }

}

