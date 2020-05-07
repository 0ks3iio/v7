package net.zdsoft.framework.exception;

public class BusinessErrorException extends Exception {
	private static final long serialVersionUID = -7325319124407093195L;

	public BusinessErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessErrorException(String message) {
		super(message);
	}
}
