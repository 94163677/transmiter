package air.kanna.transmiter.tools;

/**
 * 很简单的一个字节缓存，只能添加和读取，不能做其他操作
 * @author kan-na
 *
 */
public class ByteBuffer {
    public static final int DEFAULT_SIZE = 2048;
    
    private byte[] buffer;
    private int length = 0;
    
    public ByteBuffer() {
        buffer = new byte[DEFAULT_SIZE];
    }
    
    public ByteBuffer(int size) {
        if(size <= 0) {
            throw new java.lang.IllegalArgumentException("Size must > 0");
        }
        buffer = new byte[DEFAULT_SIZE];
    }
    
    public boolean addByte(byte toAdd) {
        if(length >= buffer.length) {
            return false;
        }
        
        buffer[length++] = toAdd;
        return true;
    }

    public byte[] getBuffer() {
        byte[] copy = new byte[length];
        for(int i=0; i<length; i++) {
            copy[i] = buffer[i];
        }
        return copy;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
