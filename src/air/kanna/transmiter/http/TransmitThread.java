package air.kanna.transmiter.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import air.kanna.transmiter.tools.ByteBuffer;

/**
 * Socket链接线程
 * 将source的socket的input链接到direct的socket的output中
 * @author kan-na
 *
 */
public class TransmitThread extends BaseThread {
    private static final Logger logger = Logger.getLogger(TransmitThread.class);

    private Socket source;
    private Socket direct;
    private ByteBuffer prevDates;

    private InputStream ins = null;
    private OutputStream ous = null;
    
    public TransmitThread(String id, Socket src, Socket dit) {
        super(id);
        if(src == null || dit == null) {
            throw new NullPointerException("source or direct Socket is null");
        }
        source = src;
        direct = dit;
    }
    
    @Override
    public void run() {
        if(source.isInputShutdown()) {
            throw new IllegalArgumentException("source Socket input is Shutdown");
        }
        if(direct.isOutputShutdown()) {
            throw new IllegalArgumentException("direct Socket output is Shutdown");
        }
        
        logger.info(id + " is start transmit");
        int data = 0;
        
        try {
            ins = source.getInputStream();
            ous = direct.getOutputStream();
        }catch(Exception e) {
            logger.error(id + " Init Input or Output Stream error.", e);
            closeAll();
            return;
        }
        
        if(prevDates != null && prevDates.getLength() > 0) {
            try {
                ous.write(prevDates.getBuffer());
            }catch(Exception e) {
                logger.error(id + " Write Stream error.", e);
                listener.onSocketError(e, direct);
                closeAll();
                return;
            }
        }
        
        while(!isBreak) {
            try {
                data = ins.read();
            }catch(Exception e) {
                logger.error(id + " Read Stream error.", e);
                listener.onSocketError(e, source);
                closeAll();
                return;
            }
            
            try {
                ous.write(data);
            }catch(Exception e) {
                logger.error(id + " Write Stream error.", e);
                listener.onSocketError(e, direct);
                closeAll();
                return;
            }
            
            if(data < 0) {
                break;
            }
        }

        closeAll();
        logger.info(id + " is finish transmit");
    }
    
    private void closeAll() {
        synchronized(source) {
            try {
                if(ins != null) {
                    ins.close();
                }
                closeSocket(source);
            }catch(Exception e) {
                logger.error(id, e);
            }
        }
        synchronized(direct) {
            try {
                if(ous != null) {
                    ous.flush();
                    ous.close();
                }
                closeSocket(direct);
            }catch(Exception e) {
                logger.error(id, e);
            }
        }
    }
    
    private void closeSocket(Socket socket)throws Exception{
        if(socket.isInputShutdown() && socket.isOutputShutdown()) {
            socket.close();
            listener.onSocketEnd(socket);
        }
    }

    public ByteBuffer getPrevDates() {
        return prevDates;
    }

    public void setPrevDates(ByteBuffer prevDates) {
        this.prevDates = prevDates;
    }
}
