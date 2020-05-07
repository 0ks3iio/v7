package net.zdsoft.appstore.remote.server;

import net.zdsoft.appstore.remote.AbstractMessage;

import org.apache.log4j.Logger;

/**
 * 
 * @author zhangxm
 * @version $Revision: 1.0 $, $Date: 2015-7-28 下午9:34:11 $
 */
public class SignInMessage extends AbstractMessage {

    private static Logger logger = Logger.getLogger(SignInMessage.class);
    private final Logger msgLog = Logger.getLogger("msg." + this.getClass().getName());

    protected static final String PHONEINFO = "PHONEINFO";

    static {
        setHeadMaxLength(10);
    }

    private String funcNo;
    private int sequence;

    @Override
    public boolean processHead() {

        int bodyLength = Integer.parseInt(new String(new byte[] { head[0], head[1], head[2], head[3] }));

        if (bodyLength > 9999) {
            return false;
        }

        setBodyMaxLength(bodyLength - getHeadMaxLength());

        funcNo = new String(new byte[] { head[4], head[5] });
        
        sequence = Integer.parseInt(new String(new byte[] { head[6], head[7], head[8], head[9]}));
        
        if (!"05".equals(funcNo)) {
        	logger.info("bodyLength[" + bodyLength + "],funcNo[" + funcNo + "],sequence["+ sequence + "]");
        }

        return true;
    }

    protected void copyMessage(SignInMessage message) {
        this.head = message.getHead();
        this.body = message.getBody();
        setBodyMaxLength(message.getBodyMaxLength());
        this.funcNo = message.getFuncNo();
        this.sequence = message.getSequence();
        // 统一记录设备请求报文
        printMsgLog();
    }
    
    /**
     * 记录设备请求报文
     */
    private void printMsgLog() {
        if (!msgLog.isDebugEnabled()) {
            return;
        }
        String msg = "";
        if (this.head != null && this.head.length > 0) {
            byte[] head = new byte[this.head.length];
            System.arraycopy(this.head, 0, head, 0, this.head.length);
            msg = new String(head);
        }
        if (this.body != null && this.body.length > 0) {
            byte[] body = new byte[this.body.length];
            System.arraycopy(this.body, 0, body, 0, this.body.length);
            msg = msg + new String(body);
        }
        msgLog.info(getClass().getSimpleName() + ":\t\t" + msg);
    }
    
    public SignInMessage() {
        super();
    }

    public String getFuncNo() {
        return funcNo;
    }

    public int getSequence() {
        return sequence;
    }
    
    /**
     * @param sequence
     *            要设置的 sequence。
     */
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

}
