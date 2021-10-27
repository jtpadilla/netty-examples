package nia.test.chapter9;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AbsIntegerEncoderTest.class,
        FixedLengthFrameDecoderTest.class,
        FrameChunkDecoderTest.class,
})
public class Chapter09Tests {
}
