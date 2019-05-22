package air.kanna.transmiter.http;

import air.kanna.transmiter.config.HttpClientConfig;

public class TestHttpClient {

    public static void main(String[] args) {
        HttpClientConfig config = new HttpClientConfig();
        
        config.setPort(6001);
        config.setDirectIpAddress("172.16.4.131");
        config.setDirectPort(80);
        config.setMaxAccept(5);
        
        HttpClient client = new HttpClient(config);
        
        try {
            client.start();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
