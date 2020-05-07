package net.zdsoft.framework.config;

public class CacheEntity {

	private Object value; // 值

	private long timestamp; // 保存的时间戳

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	private int expire; // 有效时间

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
		this.timestamp = System.nanoTime();
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

}
