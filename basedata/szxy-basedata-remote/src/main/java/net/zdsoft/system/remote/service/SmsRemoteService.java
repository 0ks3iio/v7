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
	/**
	 * 指定ticket
	 * @param phones
	 * @param msg
	 * @param ticket
	 * @param sendTime
	 * @return
	 */
	public String sendSms(String[] phones, String msg, String ticket, String sendTime);
	/**
	 * 
	 * @param phones
	 * @param msg
	 * @param sendTime
	 * @param unitId 根据单位id获取不同的短信通道
	 * @return
	 */
	public String sendSmsByUnitId(String[] phones, String msg, String sendTime,String unitId);

}
