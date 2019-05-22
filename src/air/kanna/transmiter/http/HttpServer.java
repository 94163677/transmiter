package air.kanna.transmiter.http;

import java.net.Socket;
import java.util.Vector;

import org.apache.log4j.Logger;

import air.kanna.transmiter.config.HttpServerConfig;
import air.kanna.transmiter.tools.Tools;

/**
 * 服务端
 * 会先生成一个与客户端的链接池，用于与客户端链接
 * 之后会根据配置开启一个监听外部的ServerSocket
 * 当外部有链接接入，则把外部链接的Socket和与客户端链接的某个Socket链接起来
 * 
 * @author kan-na
 */
public class HttpServer{
    private static final Logger logger = Logger.getLogger(HttpServer.class);
    
    private HttpServerConfig config;
    private Vector<Socket> pool;
    
    public HttpServer(HttpServerConfig config) {
        if(config == null) {
            throw new NullPointerException("HttpServerConfig is null");
        }
        
        Tools.checkConfig(config);
        
        this.config = config;
        pool = new Vector<>();
    }
    
    public void start() {
        pool.clear();
        
        ConnectThread connect = new ConnectThread("CONN_THREAD", pool, config);
        ListenThread listen = new ListenThread("LIST_THREAD", pool, config);
        
        connect.start();
        listen.start();
    }
}
