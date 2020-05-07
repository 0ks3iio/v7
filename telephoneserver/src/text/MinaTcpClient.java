package text;

import java.net.InetSocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
/** 
 * @说明 Mina TCP客户端 
 * @author 崔素强 
 * @version 1.0 
 * @since 
 */  
public class MinaTcpClient extends IoHandlerAdapter {  
    private IoConnector connector;  
    private static IoSession session;  
    public MinaTcpClient() {  
        connector = new NioSocketConnector();  
        connector.setHandler(this);  
        ConnectFuture connFuture = connector.connect(new InetSocketAddress("192.168.0.155", 8899));  
        connFuture.awaitUninterruptibly();  
        session = connFuture.getSession();  
        System.out.println("TCP 客户端启动");  
    }  
    public static void main(String[] args) throws Exception {  
        MinaTcpClient client = new MinaTcpClient();  
        for(int j=0;j<550;j++){ // 发送两遍  
//            byte[] bts = new byte[20];  
//            for (int i = 0; i < 20; i++) {  
//                bts[i] = (byte) i;  
//            }  
            IoBuffer buffer = IoBuffer.allocate(20);  
            // 自动扩容  
            buffer.setAutoExpand(true);  
            // 自动收缩  
            buffer.setAutoShrink(true);  
            buffer.put("0079080001123456            ".getBytes());
            buffer.put(("3322508"+(100+j)).getBytes());
            buffer.put("                          201806051528301".getBytes());
//            buffer.put(StringUtils.enoughZero(String.valueOf("123456"), 18).getBytes());  
//            buffer.put(StringUtils.enoughZero(String.valueOf("3322508992"), 18).getBytes());  
//            buffer.put("201806051528301".getBytes());  
            buffer.flip();  
            session.write(buffer);  
//            Thread.sleep(2000);  
        }  
        // 关闭会话，待所有线程处理结束后  
        client.connector.dispose(true);  
    }  
    @Override  
    public void messageReceived(IoSession iosession, Object message)  
            throws Exception {  
        IoBuffer bbuf = (IoBuffer) message;  
        byte[] byten = new byte[bbuf.limit()];  
        bbuf.get(byten, bbuf.position(), bbuf.limit());  
        System.out.println("客户端收到消息" + byten);  
    }  
    @Override  
    public void exceptionCaught(IoSession session, Throwable cause)  
            throws Exception {  
        System.out.println("客户端异常");  
        super.exceptionCaught(session, cause);  
    }  
    @Override  
    public void messageSent(IoSession iosession, Object obj) throws Exception {  
        System.out.println("客户端消息发送");  
        super.messageSent(iosession, obj);  
    }  
    @Override  
    public void sessionClosed(IoSession iosession) throws Exception {  
        System.out.println("客户端会话关闭");  
        super.sessionClosed(iosession);  
    }  
    @Override  
    public void sessionCreated(IoSession iosession) throws Exception {  
        System.out.println("客户端会话创建");  
        super.sessionCreated(iosession);  
    }  
    @Override  
    public void sessionIdle(IoSession iosession, IdleStatus idlestatus)  
            throws Exception {  
        System.out.println("客户端会话休眠");  
        super.sessionIdle(iosession, idlestatus);  
    }  
    @Override  
    public void sessionOpened(IoSession iosession) throws Exception {  
        System.out.println("客户端会话打开");  
        super.sessionOpened(iosession);  
    }  
} 