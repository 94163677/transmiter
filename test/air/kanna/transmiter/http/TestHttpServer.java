package air.kanna.transmiter.http;

import air.kanna.transmiter.config.HttpServerConfig;

public class TestHttpServer {

    public static void main(String[] args) {
        HttpServerConfig config = new HttpServerConfig();
        
        config.setMaxAccept(3);
        config.setClientPort(6001);
        config.setPort(9801);
        
        HttpServer server = new HttpServer(config);
        try {
            server.start();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
