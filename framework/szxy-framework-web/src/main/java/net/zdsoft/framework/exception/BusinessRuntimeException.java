package net.zdsoft.framework.exception;

public class BusinessRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -5066526038454378822L;

	public BusinessRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessRuntimeException(String message) {
		super(message);
	}
}
