package air.kanna.transmiter.http;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import org.apache.log4j.Logger;

import air.kanna.transmiter.config.HttpServerConfig;

/**
 * 监听外部的链接请求
 * 如有请求，则从与客户端的链接池中取一个链接
 * 最后把这两个链接连在一起
 * @author kan-na
 *
 */
public class ListenThread extends ConnectThread {
    private static final Logger logger = Logger.getLogger(ListenThread.class);
    
    public ListenThread(String id, Vector<Socket> pool, HttpServerConfig config) {
        super(id, pool, config);
    }
    
    @Override
    public void run() {
        ServerSocket server = null;
        
        logger.info(id + " create connect server at port: " + config.getPort());
        try {
            server = new ServerSocket(config.getPort());
        }catch(Exception e) {
            logger.error("Create ServerSocket for listen Error at port: " + config.getPort(), e);
            return;
        }
        
        for(int i=1; !isBreak; i++) {//是否结束
            try {
              //监听并创建与外部的链接
                Socket connect = server.accept();
                Socket direct = null;
                logger.info(id + " create connect server at " + connect.getInetAddress());
                
              //成功创建后，从链接池取一个与客户端的链接
                synchronized(pool) {
                  //链接池空了就等待
                    while(pool.size() <= 0) {
                        logger.info(id + " Vector pool's size is 0, waiting...");
                        pool.wait();
                    }
                    direct = pool.get(0);
                    pool.remove(0);
                    pool.notifyAll();
                }
                logger.info(id + " start transmit with " + connect.getInetAddress());
                //创建输入输出对接线程，对接两个Socket
                TransmitThread from = new TransmitThread(("TRAN_FROM_LIST_" + i), connect, direct);
                TransmitThread to = new TransmitThread(("TRAN_TO_LIST_" + i), direct, connect);
                from.start();
                to.start();
            }catch(Exception e) {
                logger.error("get connection for listen error", e);
            }
        }
        
        logger.info(id + " server closing");
        try {
            server.close();
        }catch(Exception e) {
            logger.error("close ServerSocket for listen error", e);
        }
        logger.info(id + " server closed");
    }
}
