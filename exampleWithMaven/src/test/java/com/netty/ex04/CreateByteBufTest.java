package com.netty.ex04;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

public class CreateByteBufTest {
    // Buffered Pools prevent JVM to run garbage collector unnecessarily
    // ByteBuf does not require flip method unlike ByteBuffer


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

        assertEquals(0, buf.readableBytes());
        assertEquals(BUFFER_SIZE, buf.writableBytes());

    }
}
