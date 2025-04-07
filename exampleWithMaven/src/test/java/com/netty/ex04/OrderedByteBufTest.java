package com.netty.ex04;

import static org.junit.Assert.assertEquals;

import java.nio.ByteOrder;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class OrderedByteBufTest {

    @Test
    public void pooledHeapBufferTest(){
        ByteBuf buf = Unpooled.buffer();
        assertEquals(ByteOrder.BIG_ENDIAN, buf.order());

        buf.writeShort(1);;

        buf.markReaderIndex();
        assertEquals(1, buf.readShort());

        buf.resetReaderIndex();     //reset readerIndex to the marked position

        ByteBuf littleEndianBuf = buf.order(ByteOrder.LITTLE_ENDIAN);       //littleEndianBuf share same memory and index with buf
        assertEquals(256, littleEndianBuf.readShort());
    }
}
