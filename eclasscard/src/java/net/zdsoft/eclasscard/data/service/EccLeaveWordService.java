package net.zdsoft.eclasscard.data.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccLeaveWord;
import net.zdsoft.framework.entity.Pagination;

public interface EccLeaveWordService extends BaseService<EccLeaveWord, String>{
	/**
	 * 
	 * @param leaveWord
	 * @param type  1.学生发送    2.家长发送
	 */
	public void saveLeaveWord(EccLeaveWord leaveWord,String type,String basePath);
	
	public void updateStatus(String receiverId,String senderId,Date time);

	public List<EccLeaveWord> findBySenderAndReceiverId(String receiverId,
			String senderId ,Date lastTime,Pagination page);
	
	public String findLastWordByCache(String key);

	public List<EccLeaveWord> findByReceiverIdsNotRead(String[] stuIds);
	

}
