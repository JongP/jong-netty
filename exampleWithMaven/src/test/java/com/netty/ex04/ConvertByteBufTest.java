package com.netty.ex04;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ConvertByteBufTest {
    final String source = "Hello World!";

    @Test
    public void convertByteBufToByteBufferTest() {
        ByteBuf buf = Unpooled.buffer(11);;

        buf.writeBytes(source.getBytes());
        assertEquals(source,buf.toString(Charset.defaultCharset()));

        ByteBuffer nioByteBuffer = buf.nioBuffer();         //share same array with buf
        assertNotNull(nioByteBuffer);
        assertEquals(source, new String(nioByteBuffer.array(), nioByteBuffer.arrayOffset(), nioByteBuffer.remaining()));

    }


    @Test
    public void convertByteBufferToByteBuf(){
        ByteBuffer byteBuffer = ByteBuffer.wrap(source.getBytes());
        ByteBuf buf = Unpooled.wrappedBuffer(byteBuffer);   //share same array with byteBuffer

        assertEquals(source, buf.toString(Charset.defaultCharset()));
    }
}
