package net.zdsoft.basedata.remote;

/**
 * @author shenke
 * @since 2019/2/20 上午9:43
 */
public class UsernameAlreadyExistsException extends Exception {

	private static final long serialVersionUID = -2050487592124219301L;
	private String username;

    public UsernameAlreadyExistsException(String message, String username) {
        super(message);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
