package com.netty.ex04;

import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

public class ReadWriteByteBufTest {
    final int BUFFER_SIZE = 11;
    @Test
    public void createUnpooledHeapBufferTest(){
        ByteBuf buf = Unpooled.buffer(BUFFER_SIZE);

        testBuffer(buf, false);
    }

    @Test
    public void createUnpooledDirectBufferTest(){
        ByteBuf buf = Unpooled.directBuffer(BUFFER_SIZE);

        testBuffer(buf, true);
    }

    @Test
    public void createPooledHeapBufferTest(){
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.heapBuffer(BUFFER_SIZE);

        testBuffer(buf, false);
    }

    @Test
    public void createPooledDirectBufferTest() {
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(BUFFER_SIZE);

        testBuffer(buf, true);
    }

    private void testBuffer(ByteBuf buf, boolean isDirect) {
        assertEquals(BUFFER_SIZE, buf.capacity());

        assertEquals(isDirect, buf.isDirect());

        buf.writeInt(65537);
        assertEquals(4, buf.readableBytes());
        assertEquals(7, buf.writableBytes());

        assertEquals(1, buf.readShort());
        assertEquals(2, buf.readableBytes());
        assertEquals(7, buf.writableBytes());

        assertEquals(true, buf.isReadable());

        buf.clear();                                                //initialze readerIndex and writerIndex

        assertEquals(0, buf.readableBytes());
        assertEquals(11, buf.writableBytes());

        buf.capacity(13);
        assertEquals(0, buf.readableBytes());
        assertEquals(13, buf.writableBytes());


    }
}
