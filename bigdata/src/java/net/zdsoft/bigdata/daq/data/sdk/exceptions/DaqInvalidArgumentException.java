package net.zdsoft.bigdata.daq.data.sdk.exceptions;

/**
 * 非法的DistinctID
 */
public class DaqInvalidArgumentException extends Exception {

	private static final long serialVersionUID = -1294683280631684053L;

public DaqInvalidArgumentException(String message) {
    super(message);
  }

  public DaqInvalidArgumentException(Throwable error) {
    super(error);
  }

}
