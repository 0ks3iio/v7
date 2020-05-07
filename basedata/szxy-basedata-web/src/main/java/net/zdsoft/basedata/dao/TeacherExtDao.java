package net.zdsoft.basedata.dao;

import java.util.List;


public interface TeacherExtDao {
	
	/**
	 * 更新教师一卡通号
	 * @return
	 */
	int[] updateCardNumber(List<String[]> techerCardList);
}
