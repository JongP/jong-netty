package com.netty.ex04;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;

import org.junit.Test;

public class ByteBufferTest {
    @Test
    public void craeteTest(){
        CharBuffer heapBuffer = CharBuffer.allocate(11);
        assertEquals(11, heapBuffer.capacity());
        assertEquals(false, heapBuffer.isDirect());

        ByteBuffer directBuffer = ByteBuffer.allocateDirect(11);
        assertEquals(11, directBuffer.capacity());
        assertEquals(true, directBuffer.isDirect());

        int[] arry = {1,2,3,4,5,6,7,8,9,0,0};
        IntBuffer intHeapBuffer = IntBuffer.wrap(arry);
        assertEquals(11, intHeapBuffer.capacity());
        assertEquals(false, intHeapBuffer.isDirect());

    }

    @Test
    public void byteBufferTest() {
        ByteBuffer firstBuffer = ByteBuffer.allocate(11);
        System.out.println("firstBuffer: " + firstBuffer); //java.nio.HeapByteBuffer[pos=0 lim=11 cap=11]

        byte[] source = "Hello World".getBytes();
        firstBuffer.put(source);
        System.out.println("firstBuffer after put: " + firstBuffer); // java.nio.HeapByteBuffer[pos=11 lim=11 cap=11]
    }

    //@Test
    public void byteBufferTest2() {
        ByteBuffer firstBuffer = ByteBuffer.allocate(11);
        System.out.println("firstBuffer: " + firstBuffer); //java.nio.HeapByteBuffer[pos=0 lim=11 cap=11]

        byte[] source = "Hello World!".getBytes();
        firstBuffer.put(source);                                     //java.nio.BufferOverflowException
        System.out.println("firstBuffer after put: " + firstBuffer); //
    }

    @Test
    public void byteBufferTestWrong3() {
        ByteBuffer firstBuffer = ByteBuffer.allocate(11);
        System.out.println("firstBuffer: " + firstBuffer); //java.nio.HeapByteBuffer[pos=0 lim=11 cap=11]

        firstBuffer.put((byte) 1);
        System.out.println(firstBuffer.get()); //0
        System.out.println(firstBuffer); //java.nio.HeapByteBuffer[pos=2 lim=11 cap=11]

        //position increments after put and get method
        //get method return value of positon which was 1
    }

    @Test
    public void byteBufferTestRight3() {
        ByteBuffer firstBuffer = ByteBuffer.allocate(11);
        System.out.println("firstBuffer: " + firstBuffer); //java.nio.HeapByteBuffer[pos=0 lim=11 cap=11]

        firstBuffer.put((byte) 1);
        firstBuffer.put((byte) 2);
        assertEquals(2, firstBuffer.position());

        firstBuffer.rewind();                                           //reset position to 0
        assertEquals(0, firstBuffer.position());

        assertEquals(1,firstBuffer.get());
        assertEquals(1,firstBuffer.position());

        System.out.println(firstBuffer);
    }


    @Test
    public void writeTest(){
        ByteBuffer firstBuffer = ByteBuffer.allocateDirect(11);
        assertEquals(0,firstBuffer.position());
        assertEquals(11,firstBuffer.limit());

        firstBuffer.put((byte) 1);
        firstBuffer.put((byte) 2);
        firstBuffer.put((byte) 3);
        firstBuffer.put((byte) 4);
        assertEquals(4,firstBuffer.position());
        assertEquals(11, firstBuffer.limit());

        firstBuffer.flip();                                     //limit is set to the current last position and position is set to 0
        assertEquals(0,firstBuffer.position());
        assertEquals(4, firstBuffer.limit());

    }

    @Test
    public void readTest(){
        byte[] tempArry = {1,2,3,4,5,0,0,0,0,0,0};
        ByteBuffer firstBuffer = ByteBuffer.wrap(tempArry);
        assertEquals(0, firstBuffer.position());
        assertEquals(11, firstBuffer.limit());

        assertEquals(1, firstBuffer.get());
        assertEquals(2, firstBuffer.get());
        assertEquals(3, firstBuffer.get());
        assertEquals(4,firstBuffer.get());
        assertEquals(4,firstBuffer.position());
        assertEquals(11, firstBuffer.limit());

        firstBuffer.flip();     //flip method should be could after read or write task.
                                //this means ByteBuffer in Java should seprate read and write task.
                                //this leads ByteBuffer to be thread-unsafe
                                //this is byte byte buffer in netty has read and write indexes
        assertEquals(0,firstBuffer.position());
        assertEquals(4, firstBuffer.limit());

        firstBuffer.get(3);

        assertEquals(0,firstBuffer.position());

    }
}
