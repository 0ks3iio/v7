package net.zdsoft.appstore.remote.server;

import net.zdsoft.appstore.remote.AbstractHandler;
import net.zdsoft.appstore.remote.AbstractMessage;
import net.zdsoft.appstore.remote.ServerConfig;

import org.apache.log4j.Logger;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.io.IoSession;

/**
 * 
 * @author duhc
 * @version $Revision: 1.0 $, $Date: 2018-6-1 $
 */
public class SignInHandler extends AbstractHandler {

    private static Logger logger = Logger.getLogger(SignInHandler.class);

    private static String SEQUENCE = "Sequence";

    public SignInHandler(ServerConfig serverConfig) {
        super(serverConfig);
    }

    @Override
    public void sessionOpened(IoSession session) {
        session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, getIdleTime());
        session.setAttribute(SEQUENCE, new Integer(0));
    }

    @Override
    public AbstractMessage createMessage() {
        return new SignInMessage();
    }

    @Override
    public void processMessage(IoSession session, AbstractMessage message) {
        if (logger.isDebugEnabled()) {
            logger.debug("processMessage");
        }
        processMessageBatch(session, message);
    }

    
    public void processMessageBatch(IoSession session, AbstractMessage message) {

        SignInMessage signInMessage = (SignInMessage) message;
        ResponseMessage responseMessage = new ResponseMessage();
        boolean back = false;
        if ("10".equals(signInMessage.getFuncNo())) {
        	//认证消息包
        	PhoneAuthenMessage phoneAuthenMessage = new PhoneAuthenMessage();
        	phoneAuthenMessage.copyMessage(signInMessage);
        	back = phoneAuthenMessage.processBodyBatch(session);
        	phoneAuthenMessage.processResponse(responseMessage);
        	phoneAuthenMessage = null;
        } else if ("04".equals(signInMessage.getFuncNo())) {
            //学生签到消息包
        	SignRecordMessage signRecordMessage = new SignRecordMessage();
            signRecordMessage.copyMessage(signInMessage);
            signRecordMessage.processBodyBatch(session);
            signRecordMessage.processResponse(responseMessage);
            signRecordMessage = null;
        } else if ("05".equals(signInMessage.getFuncNo())) {
			//心跳消息包
        	ConnectStatusMessage connectStatusMessage = new ConnectStatusMessage();
			connectStatusMessage.copyMessage(signInMessage);
			connectStatusMessage.processBodyBatch(session);
			connectStatusMessage.processResponse(responseMessage);
			connectStatusMessage = null;
        } else if ("08".equals(signInMessage.getFuncNo())) {
            //进出校消息包
        	SignMultiRecordMessage signMultiRecordMessage = new SignMultiRecordMessage();
            signMultiRecordMessage.copyMessage(signInMessage);
            signMultiRecordMessage.processBodyBatch(session);
            signMultiRecordMessage.processResponse(responseMessage);
            signMultiRecordMessage = null;
        } else if ("82".equals(signInMessage.getFuncNo())) {
			//状态检测消息包
        	AbtStatusMessageResp abtStatusMessageResp = new AbtStatusMessageResp();
			abtStatusMessageResp.copyMessage(signInMessage);
			abtStatusMessageResp.processBody(session);
			abtStatusMessageResp = null;
        }
        
        if (responseMessage.responseLength > 0) {
            session.write(responseMessage.writeBuffer, null);
        }

        if ("10".equals(signInMessage.getFuncNo()) && back) {
        	//认证成功主动发送状态查询消息
        	AbtStatusMessageResp abtStatusMessageResp = new AbtStatusMessageResp();
        	responseMessage = new ResponseMessage();
        	abtStatusMessageResp.processResponse(session,responseMessage);
        	session.write(responseMessage.writeBuffer, null);
        }
        responseMessage = null;
    }
}
