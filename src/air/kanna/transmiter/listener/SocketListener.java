package air.kanna.transmiter.listener;

import java.net.Socket;

public interface SocketListener {
    void onSocketError(Exception expection, Socket socket);
    void onSocketEnd(Socket socket);
}
