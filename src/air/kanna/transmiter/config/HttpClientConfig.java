package air.kanna.transmiter.config;

/**
 * 客户端配置项
 * @author kan-na
 *
 */
public class HttpClientConfig extends BaseConfig {

    /*
     * ipAddress：服务端的IP地址
     * port：服务端与客户端的连接端口（注意，不是监听端口）
     * maxAccept：最大连接数
     */
    private String directIpAddress = DEFAULT_IP;//客户端转发到达的IP地址
    private int directPort = DEFAULT_PORT;//客户端转发到达的端口
    
    public String getDirectIpAddress() {
        return directIpAddress;
    }
    public void setDirectIpAddress(String directIpAddress) {
        this.directIpAddress = directIpAddress;
    }
    public int getDirectPort() {
        return directPort;
    }
    public void setDirectPort(int directPort) {
        this.directPort = directPort;
    }
    
}
