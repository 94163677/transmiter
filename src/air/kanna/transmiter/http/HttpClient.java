package air.kanna.transmiter.http;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import air.kanna.transmiter.config.HttpClientConfig;
import air.kanna.transmiter.listener.SocketListener;
import air.kanna.transmiter.tools.Tools;

/**
 * 客户端
 * 客户端会根据配置用复数的链接连上服务端的客户端端口
 * 之后等待服务端传来数据
 * 服务端传来数据后，客户端再创建一个链接目的端的Socket
 * 最后将服务端和目的端的两个链接接上
 * 
 * @author kan-na
 */
public class HttpClient implements SocketListener{
    private static final Logger logger = Logger.getLogger(HttpClient.class);
    
    private HttpClientConfig config;
    private List<Socket> connSocket;
    
    public HttpClient(HttpClientConfig config) {
        if(config == null) {
            throw new NullPointerException("HttpClientConfig is null");
        }
        Tools.checkConfig(config);
        this.config = config;
        connSocket = new ArrayList<>();
    }
    
    public void start() throws UnknownHostException, IOException {
        for(int i=0; i<config.getMaxAccept(); i++) {
            Socket socket = new Socket(config.getIpAddress(), config.getPort());
            connSocket.add(socket);
            WaitingThread waiting = new WaitingThread(("WAIT_" + i),socket, config);
            waiting.setListener(this);
            waiting.start();
        }
    }
    
    public void onSocketError(Exception exception, Socket socket) {
        logger.error("Socket trans error", exception);
        onSocketEnd(socket);
    }
    
    public void onSocketEnd(Socket socket) {
        synchronized(this) {
            int index = connSocket.indexOf(socket);
            if(index < 0) {
                return;
            }
            if(!socket.isClosed()) {
                try {
                    socket.close();
                }catch(Exception e) {
                    logger.error("Cannot close socket at index: " + index);
                }
            }
            
            try {
                socket = new Socket(config.getIpAddress(), config.getPort());
                connSocket.set(index, socket);
            }catch(Exception e) {
                logger.error("Cannot Create socket at index: " + index);
            }
            new WaitingThread(("WAIT_" + index), socket, config).start();
        }
    }
}
