package net.zdsoft.studevelop.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StudevelopActivity;
/**
 * studevelop_activity
 * @author 
 * 
 */
public interface StudevelopActivityService extends BaseService<StudevelopActivity, String>{

	/**
	 * 获取活动信息，包含封面图片
	 * @param acadyear
	 * @param semester
	 * @param actType
	 * @param rangeId
	 * @param rangeType
	 * @return
	 */
	public List<StudevelopActivity> findActBySemeRangeId(String acadyear, int semester, String actType, 
											String rangeId, String rangeType);

	public List<StudevelopActivity> findActBySemeRangeType(
			String acadyear, int semester, String actType, String rangeType);
	
	/**
	 * 根据ids数组删除studevelop_activity数据
	 * @param ids
	 * @return
	 */
	public Integer delete(String[] ids);
	
	/**
	 * 包含删除附件、处理临时目录的图片
	 * @param info
	 */
	public void saveInfo(StudevelopActivity info);

}