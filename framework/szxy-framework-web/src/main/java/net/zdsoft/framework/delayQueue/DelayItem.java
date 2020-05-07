package net.zdsoft.framework.delayQueue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author jiang feng 延迟任务线程 实现Delayed接口
 *
 * @param <T>
 */
public class DelayItem<T extends Runnable> implements Delayed {

	/**
	 * 标识
	 */
	private final String key;

	/**
	 * 到期时间
	 */
	private final long time;

	/**
	 * 任务对象
	 */
	private final T task;

	/**
	 * 原子类
	 */
	private static final AtomicLong atomic = new AtomicLong(0);

	private final long n;

	public DelayItem(String key, String triggerTime, T t) throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long startTime = sf.parse(triggerTime).getTime();
		long nowTime = System.currentTimeMillis();
		System.out.println("距离执行还有---------------"
				+ ((startTime - nowTime) / 1000) + "秒");
		// 转换成ns
		long nanoTime = TimeUnit.NANOSECONDS.convert(
				(startTime - nowTime) / 1000, TimeUnit.SECONDS);
		this.key = key;
		this.time = System.nanoTime() + nanoTime;
		this.task = t;
		this.n = atomic.getAndIncrement();
	}

	/**
	 * 返回与此对象相关的剩余延迟时间，以给定的时间单位表示
	 */
	public long getDelay(TimeUnit unit) {

		return unit
				.convert(this.time - System.nanoTime(), TimeUnit.NANOSECONDS);
	}

	public int compareTo(Delayed other) {
		if (other == this)
			return 0;
		if (other instanceof DelayItem) {
			DelayItem<?> x = (DelayItem<?>) other;
			long diff = time - x.time;
			if (diff < 0)
				return -1;
			else if (diff > 0)
				return 1;
			else if (n < x.n)
				return -1;
			else
				return 1;
		}
		long d = (getDelay(TimeUnit.NANOSECONDS) - other
				.getDelay(TimeUnit.NANOSECONDS));
		return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
	}

	public T getTask() {
		return this.task;
	}

	public String getKey() {
		return this.key;
	}

	@Override
	public int hashCode() {

		return task.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof DelayItem) {
			return ((DelayItem<?>) object).getKey() == getKey() ? true : false;
		}
		return false;
	}
}
