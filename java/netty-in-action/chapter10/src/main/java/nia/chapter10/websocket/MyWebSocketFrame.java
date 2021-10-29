package nia.chapter10.websocket;

import io.netty.buffer.ByteBuf;

public class MyWebSocketFrame {

    private final MyWebSocketFrameType type;
    private final ByteBuf data;

    public MyWebSocketFrame(MyWebSocketFrameType type, ByteBuf data) {
        this.type = type;
        this.data = data;
    }

    public MyWebSocketFrameType getType() {
        return type;
    }

    public ByteBuf getData() {
        return data;
    }

}
