package air.kanna.transmiter.http;

import java.io.InputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import air.kanna.transmiter.config.HttpClientConfig;
import air.kanna.transmiter.tools.ByteBuffer;
import air.kanna.transmiter.tools.Tools;

/**
 * 数据等待线程
 * 当socket有数据进来（就是服务器端有数据了），则立刻创建目的端的Socket，之后链接这两个socket
 * @author kan-na
 *
 */
public class WaitingThread extends BaseThread {
    private static final Logger logger = Logger.getLogger(WaitingThread.class);
    
    private HttpClientConfig config;
    private Socket source;
    
    public WaitingThread(String id, Socket source, HttpClientConfig config) {
        super(id);
        if(source == null) {
            throw new NullPointerException("Source Socket is null");
        }
        if(config == null) {
            throw new NullPointerException("HttpClientConfig is null");
        }
        Tools.checkConfig(config);
        
        this.source = source;
        this.config = config;
    }
    
    @Override
    public void run() {
        InputStream orgIns = null;
        Socket direct = null;
        int data = -1;
        
        logger.info(id + " is start waiting.");
        
        try {
            orgIns = source.getInputStream();
        }catch(Exception e) {
            logger.error("Cannot get Stream in source", e);
            return;
        }
        
        try {
            data = orgIns.read();
        }catch(Exception e) {
            logger.error("Cannot get Stream in source", e);
            return;
        }
        
        logger.info(id + " get transfer: " + data);
        
        if(data < 0) {
            return;
        }
        
        try {
            direct = new Socket(config.getDirectIpAddress(), config.getDirectPort());
        }catch(Exception e) {
            logger.error("Cannot connect direct server", e);
            if(direct != null) {
                try {
                    direct.close();
                }catch(Exception ex) {
                    logger.error("Cannot close direct connect", ex);
                }
            }
            return;
        }
        
        ByteBuffer buffer = new ByteBuffer(4);
        TransmitThread from = new TransmitThread(
                ("TRAN_FORM_" + id), source, direct);
        TransmitThread to = new TransmitThread(
                ("TRAN_TO_" + id), direct, source);
        
        buffer.addByte((byte)data);
        from.setPrevDates(buffer);
        from.setListener(listener);
        to.setListener(listener);
        
        from.start();
        to.start();
        
        logger.info(id + " is finish waiting.");
    }
}
