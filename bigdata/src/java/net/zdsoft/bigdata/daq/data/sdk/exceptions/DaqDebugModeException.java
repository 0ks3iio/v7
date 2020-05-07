package net.zdsoft.bigdata.daq.data.sdk.exceptions;

/**
 * Debug模式下的错误
 */
public class DaqDebugModeException extends RuntimeException {

	private static final long serialVersionUID = -629285445506126964L;

	public DaqDebugModeException(String message) {
		super(message);
	}

	public DaqDebugModeException(Throwable error) {
		super(error);
	}

	public DaqDebugModeException(String message, Throwable error) {
		super(message, error);
	}

}
