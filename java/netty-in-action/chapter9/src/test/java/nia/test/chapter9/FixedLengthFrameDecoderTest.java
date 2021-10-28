package nia.test.chapter9;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import nia.chapter9.FixedLengthFrameDecoder;
import org.junit.Test;

import static org.junit.Assert.*;

public class FixedLengthFrameDecoderTest {

    @Test
    public void testFramesDecoded() {

        // Se prepara un buffer con 9 bytes
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }

        // Se genera un duplicado del buffer (compartiendo el buffer subyacente)
        ByteBuf input = buf.duplicate();

        // Se crea un canal con un encoder que traspasa los bytes en bloques de 3
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));

        // Se escriben los 9 bytes de 'input'
        assertTrue(channel.writeInbound(input.retain()));
        assertTrue(channel.finish());

        // Se lee un buffer de 3 bytes (y se verifica con el buffer original)
        ByteBuf read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        // Se lee un buffer de 3 bytes (y se verifica con el buffer original)
        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        // Se lee un buffer de 3 bytes (y se verifica con el buffer original)
        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        // Ahora la lectura tiene que retornar un null
        assertNull(channel.readInbound());
        buf.release();

    }

    @Test
    public void testFramesDecoded2() {

        // Se prepara un buffer con 9 bytes
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }

        // Se genera un duplicado del buffer (compartiendo el buffer subyacente)
        ByteBuf input = buf.duplicate();

        // Se crea un canal con un encoder que traspasa los bytes en bloques de 3
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));

        // Primero se escriben 2 bytes
        assertFalse(channel.writeInbound(input.readBytes(2)));

        // Despues los 7 bytes restantes
        assertTrue(channel.writeInbound(input.readBytes(7)));

        // Se cierra el canal.
        assertTrue(channel.finish());

        // Se lee un buffer de 3 bytes (y se verifica con el buffer original)
        ByteBuf read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        // Se lee un buffer de 3 bytes (y se verifica con el buffer original)
        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        // Se lee un buffer de 3 bytes (y se verifica con el buffer original)
        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        // Ahora la lectura tiene que retornar un null
        assertNull(channel.readInbound());
        buf.release();

    }

}
