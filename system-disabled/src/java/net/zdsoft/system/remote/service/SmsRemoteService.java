package net.zdsoft.system.remote.service;

public interface SmsRemoteService {
	/**
	 * 发送短信
	 * @param phones 手机号码
	 * @param msg 发送的内容
	 * @param sendTime 定时发送，为null，表示即时发送，格式yyyyMMddhhmmss
	 * @return
	 */
	public String sendSms(String[] phones, String msg, String sendTime);

}
