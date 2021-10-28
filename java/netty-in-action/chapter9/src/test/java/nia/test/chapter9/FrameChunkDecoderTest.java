package nia.test.chapter9;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import nia.chapter9.FrameChunkDecoder;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class FrameChunkDecoderTest {

    @Test
    public void testFramesDecoded() {

        // Se prepara un buffer con 9 bytes
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }

        // Se genera un duplicado del buffer (compartiendo el buffer subyacente)
        ByteBuf input = buf.duplicate();

        // Se crea un canal con un encoder que limita los bloques a un maximo de 3 bytes
        EmbeddedChannel channel = new EmbeddedChannel(new FrameChunkDecoder(3));

        // Se transmite un bloque de 2 bytes y toddo tiene que ir bien.
        assertTrue(channel.writeInbound(input.readBytes(2)));

        // Se transmite un bloque de 4 bytes y se espera una excepcion
        try {
            channel.writeInbound(input.readBytes(4));
            Assert.fail();
        } catch (TooLongFrameException e) {
            // expected exception
        }

        // Se transmite un bloque de 3 bytes y toddo tiene que ir bien.
        assertTrue(channel.writeInbound(input.readBytes(3)));
        assertTrue(channel.finish());

        // Se lee el primer bloque de 2 bytes y se compara con el original
        ByteBuf read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(2), read);
        read.release();

        // Hay retira del original los 4 bytes que no entraton
        // y se leen los 3 bytes que se pusieron despues.
        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.skipBytes(4).readSlice(3), read);
        read.release();

        // Se libera el buffer
        buf.release();

    }
}
