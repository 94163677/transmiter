package air.kanna.transmiter.config;

/**
 * 服务端的配置项
 * @author kan-na
 *
 */
public class HttpServerConfig extends BaseConfig{

    private int clientPort = DEFAULT_CLIENT_PORT;//客户端连接端口号

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }
}
