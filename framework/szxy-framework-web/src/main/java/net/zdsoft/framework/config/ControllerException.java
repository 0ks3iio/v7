package net.zdsoft.framework.config;

import org.apache.log4j.Logger;

public class ControllerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static Logger log = Logger.getLogger(ControllerException.class);

	private String code;
	private String msg;

	public ControllerException(String msg) {
		log.warn(msg);
		this.msg = msg;

	}

	public ControllerException(String code, String msg) {
		log.warn(msg);
		this.code = code;
		this.msg = msg;
	}

	@Override
	public String getMessage() {
		return msg;
	}

	public String getCode() {
		return code;
	}
}
