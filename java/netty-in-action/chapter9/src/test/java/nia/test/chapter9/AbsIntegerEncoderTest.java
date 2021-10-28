package nia.test.chapter9;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import nia.chapter9.AbsIntegerEncoder;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbsIntegerEncoderTest {

    @Test
    public void testEncoded() {

        // Se prepara el buffer con los datos a transmitir
        ByteBuf buf = Unpooled.buffer();
        for (int i = 1; i < 10; i++) {
            buf.writeInt(i * -1);
        }

        // Se cera el canal con el encoder
        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());

        // Se transmiten los datos que tenemos en el buffer
        assertTrue(channel.writeOutbound(buf));
        assertTrue(channel.finish());

        // Se leen lo valores y se verifican que son los esperados.
        for (int i = 1; i < 10; i++) {
            Integer intValue  = channel.readOutbound();
            assertEquals(i, intValue.intValue());
        }

        // Ahora la lectura tiene que retornar un null
        assertNull(channel.readOutbound());

    }

}
