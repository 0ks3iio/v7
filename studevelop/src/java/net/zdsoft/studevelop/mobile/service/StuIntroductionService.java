package net.zdsoft.studevelop.mobile.service;

import org.springframework.web.multipart.MultipartFile;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.mobile.entity.StuIntroduction;

import java.util.List;


public interface StuIntroductionService extends BaseService<StuIntroduction,String>{
	
	/**
	 * 新增
	 * @param item 
	 */
	void save(StuIntroduction item);
	
	/**
	 * 新增
	 * @param item
	 * @param file
	 */
	public void save(StuIntroduction item, MultipartFile file);
	
	/**
	 * 更新内容
	 * @param item TODO
	 * @param title
	 */
	void update(StuIntroduction item);
	
	/**
	 * 更新
	 * @param item
	 * @param file
	 */
	void update(StuIntroduction item, MultipartFile file);
	
	/**
	 * 查询StuIntroduction
	 * @param acadyear
	 * @param semester
	 * @param studentId
	 * @return
	 */
	StuIntroduction findObj(String acadyear, String semester, String studentId);

	/**
	 * 通过studentIds 查询 信息
	 * @param acadyear
	 * @param semester
	 * @param studentIds
	 * @return
	 */
	List<StuIntroduction> findStudentsIntro(String acadyear, String semester, String[] studentIds);
	/**
	 * 通过studentId 查询 信息
	 * @param studentId
	 * @param hasRelease
	 * @return
	 */
	List<StuIntroduction> findListByStudentId(String studentId,Integer hasRelease);
	/**
	 * 获取fileUrl
	 * @return
	 */
	String getFileURL();
}
