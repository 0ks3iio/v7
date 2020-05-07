package net.zdsoft.teaeaxam.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.teaeaxam.entity.TeaexamSiteSetting;

/**
 * 
 * @author weixh
 * 2018年10月26日	
 */
public interface TeaexamSiteSettingService extends BaseService<TeaexamSiteSetting, String> {
	public List<TeaexamSiteSetting> findSettingByExamId(String examId);
	
	public List<TeaexamSiteSetting> findSettingBySubInfoId(String subInfoId);
	
	public ResultDto saveSet(List<TeaexamSiteSetting> sets, String subInfoId);

	/**
	 * 获取科目下考生可以更换的考场
	 * @param examId TODO
	 * @param subInfoId
	 * @param teaSize
	 * @param nowRmNo TODO
	 * @return
	 */
	public List<TeaexamSiteSetting> findChangeSettingBySubInfoId(String examId, String subInfoId, int teaSize, String nowRmNo);
}
