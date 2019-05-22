package air.kanna.transmiter.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import air.kanna.transmiter.tools.Tools;

/**
 * 基础配置项
 * @author kan-na
 *
 */
public class BaseConfig {
	public static final String DEFAULT_IP = "localhost";//默认IP地址
	public static final int DEFAULT_PORT = 19080;//默认监听端口号
	public static final int DEFAULT_CLIENT_PORT = 19090;//默认服务端和客户端的连接端口号
    public static final int MAX_ACCEPT = 100;//默认最大连接数
    

    private String ipAddress = DEFAULT_IP;//连接IP地址
    private int port = DEFAULT_PORT;
    private int maxAccept = MAX_ACCEPT;
    
    //IP检查
    public static boolean checkIP(String ip) {
        if(Tools.isEmpty(ip)) {
            return false;
        }
        if(DEFAULT_IP.equalsIgnoreCase(ip)) {
            return true;
        }
        
        Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])"
                + "\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])"
                + "\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])"
                + "\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
        
        Matcher matcher = pattern.matcher(ip);
        return matcher.find();
    }
    
    //端口检查
    public static boolean checkPort(int port) {
        if(port <= 0 || port > 65535) {
            return false;
        }
        return true;
    }
    
    
    public String getIpAddress() {
        return ipAddress;
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public int getMaxAccept() {
        return maxAccept;
    }
    public void setMaxAccept(int maxAccept) {
        this.maxAccept = maxAccept;
    }
}
