package air.kanna.transmiter.http;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import org.apache.log4j.Logger;

import air.kanna.transmiter.config.HttpServerConfig;
import air.kanna.transmiter.tools.Tools;

/**
 * 实际上是生成一个跟客户端的连接池
 * 链接池没有封装，用Vector加上添加和删除的锁操作来实现
 * 类似生产者消费者模式
 * @author kan-na
 *
 */
public class ConnectThread extends BaseThread {
    private static final Logger logger = Logger.getLogger(ConnectThread.class);
    
    HttpServerConfig config;
    Vector<Socket> pool;
    
    public ConnectThread(String id, Vector<Socket> pool, HttpServerConfig config) {
        super(id);
        if(pool == null) {
            throw new NullPointerException("Connection pool is null");
        }
        if(config == null) {
            throw new NullPointerException("HttpClientConfig is null");
        }
        
        Tools.checkConfig(config);
        this.pool = pool;
        this.config = config;
    }
    
    @Override
    public void run() {
        ServerSocket server = null;
        
        logger.info(id + " create connect server at port: " + config.getClientPort());
        try {
            server = new ServerSocket(config.getClientPort());
        }catch(Exception e) {
            logger.error("Create ServerSocket from client Error at port: " + config.getClientPort(), e);
            return;
        }
        
        for(;!isBreak;) {//是否结束
            try {
              //监听并创建与客户端的链接
                Socket connect = server.accept();
                logger.info(id + " create connect server at " + connect.getInetAddress());
                //成功创建后放入链接池
                synchronized(pool) {
                    //链接池满了就等待
                    while(pool.size() >= config.getMaxAccept()) {
                        logger.info(id + " Vector pool's is full, waiting...");
                        pool.wait();
                    }
                    //链接池有空位则放进池中，并唤醒其他线程
                    pool.add(connect);
                    pool.notifyAll();
                }
            }catch(Exception e) {
                logger.error("get connection from client error", e);
            }
        }
        
        logger.info(id + " server closing");
        //先关闭与客户端的链接
        for(Socket socket : pool) {
            try {
                socket.close();
            }catch(Exception e) {
                logger.error("close connection from client error", e);
            }
        }
        //清空链接池
        pool.clear();
        //关闭监听
        try {
            server.close();
        }catch(Exception e) {
            logger.error("close ServerSocket from client error", e);
        }
        
        logger.info(id + " server closed");
    }
}
