package net.zdsoft.background.common;

import java.util.concurrent.RejectedExecutionException;

import net.zdsoft.background.util.ServiceUtils;
import net.zdsoft.keel.util.concurrent.AbstractRunnableTask;
import net.zdsoft.keel.util.concurrent.ScheduledTaskExecutor;
import net.zdsoft.message.client.MessageClient;
import net.zdsoft.message.client.MessageHandler;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public abstract class AbstractService implements Runnable, MessageHandler{
	private final Logger logger = Logger.getLogger(getClass());
	private int interval = 1000;
	private String name = null;
	private boolean isRunning = false;
	private boolean isBusy = false;
	private long lastErrorTime = 0L;
	private final ScheduledTaskExecutor scheduledTaskExecutor = new ScheduledTaskExecutor(100);
	protected boolean isAsync = false;
	protected MessageClient messageClient = null;
	protected ServiceConfig serviceConfig = null;

	public abstract void init();
	
	public void start() {
		init();
		this.isRunning = true;
	}
	
	public abstract void destroy();
	
	public void stop() {
		this.isRunning = false;
		destroy();
		while (this.isBusy) {
			try {
				Thread.sleep(1L);
			} catch (InterruptedException e) {
				this.logger.error("Service Interrupted", e);
			}
		}
	}

	public void run() {
		for (;;) {
			try {
				Thread.sleep(this.interval);
			} catch (InterruptedException e) {
				this.logger.error("Service Interrupted", e);
			}
			if (this.isRunning) {
				try {
					this.isBusy = true;
					if (execute() > 50) {
						try {
							Thread.sleep(1000L);
						} catch (InterruptedException e) {
							this.logger.error("Service Interrupted", e);
						}
						continue;
					}
					this.isBusy = false;
				} catch (Exception e) {
					this.logger.error("Service Run Error", e);
					this.isBusy = false;
					if (System.currentTimeMillis() - this.lastErrorTime > 3600000L) {
						ServiceUtils.sendManagerMessage(this.messageClient,"ServiceError:"+ StringUtils.right(e.toString(), 95));
						this.lastErrorTime = System.currentTimeMillis();
					}
				}
			}
		}
	}

	public abstract int execute();

	public void messageReceived(byte[] messageId, String userId,
			String messageContent) {
		try {
			this.isBusy = true;
			if ((this.isAsync)
					&& (this.scheduledTaskExecutor.getQueue().size() < 5000)) {
				final String _userId = userId;
				final String _messageContent = messageContent;
				try {
					this.scheduledTaskExecutor
							.execute(new AbstractRunnableTask(
									"messageReceived()") {
								public void processTask() throws Exception {
									AbstractService.this.messageReceived(
											_userId, _messageContent);
								}
							});
				} catch (RejectedExecutionException e) {
					this.logger.error("messageReceived task was rejected", e);
				}
			} else {
				if ((this.logger.isDebugEnabled())
						&& (this.scheduledTaskExecutor.getQueue().size() >= 5000)) {
					this.logger.debug("---"
							+ this.scheduledTaskExecutor.getQueue().size());
				}
				messageReceived(userId, messageContent);
			}
			this.isBusy = false;
		} catch (Exception e) {
			this.logger.error("msgReceive Run Error", e);
			if (System.currentTimeMillis() - this.lastErrorTime > 3600000L) {
				ServiceUtils.sendManagerMessage(
						this.messageClient,
						"msgReceiveError:"
								+ StringUtils.right(e.toString(), 90));
				this.lastErrorTime = System.currentTimeMillis();
			}
		}
	}

	public abstract void messageReceived(String paramString1,
			String paramString2);

	public void messageSubmitted(byte[] messageId) {
	}

	public boolean isBusy() {
		return this.isBusy;
	}

	public void setServiceConfig(ServiceConfig serviceConfig) {
		this.serviceConfig = serviceConfig;

		this.name = serviceConfig.getName();
		this.interval = serviceConfig.getServiceInterval();
	}

	public String getName() {
		return this.name;
	}

	public String managerCommand(String command) {
		return null;
	}

	public void kickedOutByServer() {
		this.logger.error("Kicked out by server, reconnecting after 1 sec");
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException localInterruptedException) {
		}
	}

	public int getInterval() {
		return this.interval;
	}

	public boolean isRunning() {
		return this.isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public MessageClient getMessageClient() {
		return this.messageClient;
	}
}
