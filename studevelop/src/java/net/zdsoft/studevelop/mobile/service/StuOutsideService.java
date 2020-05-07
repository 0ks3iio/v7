package net.zdsoft.studevelop.mobile.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.mobile.entity.StuOutside;

public interface StuOutsideService extends BaseService<StuOutside,String>{
	
	/**
	 * 新增
	 */
	void save(StuOutside item);
	
	/**
	 * 新增（包含上传附件）
	 * @param item
	 * @param files
	 */
	void save(StuOutside item, List<MultipartFile> files);
	
	/**
	 * 更新
	 * @param item
	 */
	void update(StuOutside item);
	
	/**
	 * 更新
	 * @param item
	 */
	void update(StuOutside item, List<MultipartFile> files);
	
	/**
	 * 查询StuOutside
	 * @param id
	 * @return
	 */
	StuOutside findObj(String id);

	/**
	 * 查询学生的校外表现list
	 * @param acadyear
	 * @param semester
	 * @param studentId
	 * @param type 1:校外表现， 2：假期
	 * @return
	 */
	List<StuOutside> findList(String acadyear, String semester, String studentId, int type);

	List<StuOutside> findStuOutSideList(String acadyear, String semester, String[] studentIds);
	
	
}
