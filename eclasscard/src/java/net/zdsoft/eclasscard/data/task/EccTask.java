package net.zdsoft.eclasscard.data.task;

public abstract class EccTask implements Runnable {
	protected String bizId;
	protected boolean isEnd;

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

}
