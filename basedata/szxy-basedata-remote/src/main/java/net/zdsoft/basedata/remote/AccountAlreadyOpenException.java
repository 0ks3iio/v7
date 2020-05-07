package net.zdsoft.basedata.remote;

/**
 * @author shenke
 * @since 2019/2/20 上午9:43
 */
public class AccountAlreadyOpenException extends Exception {

    private String id;

    public AccountAlreadyOpenException(String message, String id) {
        super(message);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
