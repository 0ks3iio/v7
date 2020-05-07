package net.zdsoft.appstore.remote;

import net.zdsoft.keel.util.UUIDGenerator;

import org.apache.log4j.Logger;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.SessionConfig;
import org.apache.mina.io.IoHandlerAdapter;
import org.apache.mina.io.IoSession;
import org.apache.mina.io.socket.SocketSessionConfig;

/**
 * 
 * @author zhangxm
 * @version $Revision: 1.0 $, $Date: 2015-7-28 下午9:25:20 $
 */
public abstract class AbstractHandler extends IoHandlerAdapter {

    private static Logger logger = Logger.getLogger(AbstractHandler.class);

    private static final String KEY = "0";

    private SessionManager sessionManager = null;

    private volatile int count = 0;

	private int idleTime = 130;

    private final UUIDGenerator uuid = new UUIDGenerator();

    public void setIdleTime(int idleTime) {
        this.idleTime = idleTime;
    }

    public AbstractHandler(ServerConfig serverConfig) {
        sessionManager = serverConfig.getSessionManager();
    }

    @Override
    public void sessionCreated(IoSession session) {

        if (logger.isDebugEnabled()) {
            logger.debug("Session created:"+session.getRemoteAddress() + " Connected");
        }
        count++;

        sessionManager.addSession(uuid.generateHex(), session);
        
        SessionConfig cfg = session.getConfig();
        if (cfg instanceof SocketSessionConfig) {
            ((SocketSessionConfig) cfg).setSessionReceiveBufferSize(1024 * 4);
        }
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        sessionManager.removeSession(session);
        if (logger.isDebugEnabled()) {
            logger.debug("Session removed");
        }
        session.close();
        count--;
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        sessionManager.removeSession(session);
        String deviceIp = "";
        try {
            if (session != null) {
                deviceIp = session.getRemoteAddress().toString();
            }
        }
        catch (Exception e) {
            logger.warn("getRemoteAddress error");
        }
        logger.error("deviceIP=" + deviceIp + " Caught exception:" + cause.getMessage());
        if (logger.isDebugEnabled()) {
            logger.debug("Caught exception:", cause);
        }
        session.close();
        count--;
    }

    @Override
    public void sessionOpened(IoSession session) {
        session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, idleTime);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
        if (status == IdleStatus.BOTH_IDLE) {
            if (logger.isDebugEnabled()) {
                logger.debug("Close the connection if reader is idle.");
            }
            session.close();
        }
    }

    public abstract AbstractMessage createMessage();

    public abstract void processMessage(IoSession session, AbstractMessage message);

    @Override
    public void dataRead(IoSession session, ByteBuffer readBuffer) {
        AbstractMessage message = null;

        Object object = session.removeAttribute(KEY);
        if (object != null) {
            message = (AbstractMessage) object;
        }

        do {
            // 没有残余消息的时候新建消息
            if (message == null) {
                // 没有起始标志的情况
                if (AbstractMessage.getStartFlag() == 0) {
                    message = createMessage();
                }
                else {
                    byte b = readBuffer.get();
                    // 如果没有发现消息起始标志则进入下个循环
                    if (b != AbstractMessage.getStartFlag()) {
                        continue;
                    }
                    else {
                        // 否则新建消息
                        message = createMessage();
                    }
                }
            }

            int missingHeadLength = message.getMissingHeadLength();

            // 如果头不完整
            if (missingHeadLength > 0) {
                int _readLength;
                boolean hasEnoughBytes = true;

                if (readBuffer.remaining() >= missingHeadLength) {
                    _readLength = missingHeadLength;
                }
                else {
                    _readLength = readBuffer.remaining();
                    hasEnoughBytes = false;
                }

                byte[] _head = new byte[_readLength];
                readBuffer.get(_head, 0, _readLength);
                message.readHead(_head);

                if (!hasEnoughBytes) {
                    // 把head不完整的信息保存到session里
                    session.setAttribute(KEY, message);
                    return;
                }
            }

            if (!message.processHead()) {
                // 不合法的头
                message = null;
                continue;
            }

            int missingBodyLength = message.getMissingBodyLength();
            // 如果体不完整
            if (missingBodyLength > 0) {
                int _readLength;
                boolean hasEnoughBytes = true;

                if (readBuffer.remaining() >= missingBodyLength) {
                    _readLength = missingBodyLength;
                }
                else {
                    _readLength = readBuffer.remaining();
                    hasEnoughBytes = false;
                }

                byte[] _body = new byte[_readLength];
                readBuffer.get(_body, 0, _readLength);
                message.readBody(_body);

                if (!hasEnoughBytes) {
                    // 把body不完整的信息保存到session里
                    session.setAttribute(KEY, message);
                    return;
                }
            }

            // 分别处理消息
            processMessage(session, message);

            // 处理完后清空消息
            message = null;
        }
        while (readBuffer.hasRemaining());
    }

    public int getIdleTime() {
        return idleTime;
    }

    public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
