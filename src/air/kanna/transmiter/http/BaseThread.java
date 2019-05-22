package air.kanna.transmiter.http;

import java.net.Socket;

import air.kanna.transmiter.listener.SocketListener;
import air.kanna.transmiter.tools.Tools;

/**
 * 有Socket监听器，自定义id和允许中断的基础线程类
 * @author kan-na
 *
 */
public abstract class BaseThread extends Thread {

    //Socket监听器，用于监听Socket的出错和关闭事件
    protected SocketListener listener = new NullSocketListener();
    //线程自定义ID，主要用于输出时识别线程
    protected String id;
    //是否终止线程标志，用于终止线程运行
    protected boolean isBreak = false;
    
    public BaseThread(String id) {
        super();
        if(Tools.isEmpty(id)) {
            throw new NullPointerException("ID is null");
        }
        this.id = id;
    }
    
    public String getThreadId() {
        return id;
    }

    public void setThreadId(String id) {
        this.id = id;
    }

    public SocketListener getListener() {
        return listener;
    }

    
    public void setListener(SocketListener listener) {
        if(listener == null) {
            throw new NullPointerException("SocketListener is null");
        }
        this.listener = listener;
    }

    public boolean isBreak() {
        return isBreak;
    }
    
    public void setIsBreak(boolean isBreak) {
        this.isBreak = isBreak;
    }
    
    /**
     * 空的监听器，什么都不做，用于简化调用监听器的代码
     * @author kan-na
     *
     */
    private class NullSocketListener implements SocketListener{
        public void onSocketError(Exception expection, Socket socket) {}
        public void onSocketEnd(Socket socket) {}
    }
}
