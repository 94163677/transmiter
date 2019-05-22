package air.kanna.transmiter.tools;

import java.util.List;

import air.kanna.transmiter.config.BaseConfig;
import air.kanna.transmiter.config.HttpClientConfig;
import air.kanna.transmiter.config.HttpServerConfig;

/**
 * 实用工具合集
 * @author kan-na
 *
 */
public class Tools {
    
    //字符串是否为空
    public static final boolean isEmpty(String str) {
        return str == null || str.length() <= 0;
    }
    
  //字符串是否非空
    public static final boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    //列表是否为空
    public static final boolean isEmpty(List list) {
        return list == null || list.size() <= 0;
    }
    
  //列表是否非空
    public static final boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }
    
    //基础配置项检查
    public static final void checkConfig(BaseConfig config) {
        if(!BaseConfig.checkIP(config.getIpAddress())) {
            throw new IllegalArgumentException("Connect Ip check error");
        }
        if(!BaseConfig.checkPort(config.getPort())) {
            throw new IllegalArgumentException("Connect port check error");
        }
        if(config.getMaxAccept() <= 0) {
            throw new IllegalArgumentException("MaxAccept must larger then 0");
        }
    }
    
    //客户端配置检查
    public static final void checkConfig(HttpClientConfig config) {
        checkConfig((BaseConfig)config);
        if(!BaseConfig.checkIP(config.getDirectIpAddress())) {
            throw new IllegalArgumentException("Direct Ip check error");
        }
        if(!BaseConfig.checkPort(config.getDirectPort())) {
            throw new IllegalArgumentException("Direct port check error");
        }
        if(config.getPort() == config.getDirectPort()) {
            throw new IllegalArgumentException("Connect Server port cannot equal Direct port");
        }
    }
    
    //服务端配置检查
    public static final void checkConfig(HttpServerConfig config) {
        checkConfig((BaseConfig)config);
        if(!BaseConfig.checkPort(config.getClientPort())) {
            throw new IllegalArgumentException("Client port check error");
        }
        if(config.getPort() == config.getClientPort()) {
            throw new IllegalArgumentException("Connect port cannot equal Client port");
        }
    }

}
