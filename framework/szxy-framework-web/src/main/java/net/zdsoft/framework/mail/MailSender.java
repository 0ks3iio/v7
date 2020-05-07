package net.zdsoft.framework.mail;

import java.util.List;

public class MailSender implements Runnable {

	/**
	 * 发件人别名
	 */
	private String fromAliasName;

	/**
	 * 收件人
	 */
	private String to;

	/**
	 * 主题
	 */
	private String subject;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 附件列表
	 */
	private List<String> attachFileList;

	/**
	 * 构造器
	 * 
	 * @param fromAliasName 发件人别名
	 * @param to            收件人
	 * @param subject       主题
	 * @param content       内容
	 */
	public MailSender(String fromAliasName, String to, String subject, String content) {
		this.fromAliasName = fromAliasName;
		this.to = to;
		this.subject = subject;
		this.content = content;
	}

	/**
	 * 构造器
	 * 
	 * @param fromAliasName  发件人别名
	 * @param to             收件人
	 * @param subject        主题
	 * @param content        内容
	 * @param attachFileList 附件列表
	 */
	public MailSender(String fromAliasName, String to, String subject, String content, List<String> attachFileList) {
		this(fromAliasName, to, subject, content);
		this.attachFileList = attachFileList;
	}

	@Override
	public void run() {
		new MailSenderUtils(fromAliasName, to, subject, content, attachFileList).send();
	}
}
