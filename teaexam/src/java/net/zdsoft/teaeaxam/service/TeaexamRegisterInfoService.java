package net.zdsoft.teaeaxam.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.teaeaxam.entity.TeaexamRegisterInfo;
import net.zdsoft.teaeaxam.entity.TeaexamSiteSetting;

public interface TeaexamRegisterInfoService extends BaseService<TeaexamRegisterInfo, String>{

	public List<TeaexamRegisterInfo> findByTeacherIdAndExamIdIn(String teacherId, String[] examIds);
	public List<TeaexamRegisterInfo> findByExamIdIn(String[] examIds);
	public List<TeaexamRegisterInfo> findByStatusAndExamIdIn(int status, String[] examIds);
	public List<TeaexamRegisterInfo> findBy(String examId, String subId, String schId, String status, String state, String[] teacherIds, Pagination page);
	
	public List<TeaexamRegisterInfo> findByStatusAndRoomNo(String status, String roomNo, String subjectId, String examId);
	
	/**
	 * 单科目自动编排
	 * @param examId
	 * @param subInfoId
	 */
	public void saveAutoArrange(String examId, String subInfoId);
	
	/**
	 * 生成考号
	 * @param examId
	 * @param type 值为1时全部重新生成，否则只对没有考号的考生进行生成
	 */
	public void saveAutoCardNo(String examId, String type);
	
	/**
	 * 保存更换考场信息
	 * @param info
	 */
	public void saveChangeRm(TeaexamRegisterInfo info);
	
	/**
	 * 获取考场和人数
	 * @param examId
	 * @param subInfoId
	 * @return
	 */
	public List<TeaexamSiteSetting> findRoomByExamIdSubId(String examId, String subInfoId);
	
	/**
	 * 已安排老师数量
	 * @param examId
	 * @return
	 */
	public int findCountByArrange(String examId);
	
	public int findCountByStatus(String examId, int status);
	
	public String doImport(List<String[]> datas, String subjectId, String examId);
	
	public List<TeaexamRegisterInfo> findByTeacherId(String teacherId);
}
