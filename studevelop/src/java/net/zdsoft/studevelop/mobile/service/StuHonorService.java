package net.zdsoft.studevelop.mobile.service;

import org.springframework.web.multipart.MultipartFile;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.mobile.entity.StuHonor;

import java.util.List;

public interface StuHonorService extends BaseService<StuHonor,String>{
	
	/**
	 * 新增
	 * @param item
	 * @param file
	 */
	void save(StuHonor item, MultipartFile file);
	
	/**
	 * 更新
	 * @param item
	 * @param file
	 */
	void update(StuHonor item, MultipartFile file);
	
	/**
	 * 查询StuHonor
	 * @param id
	 * @return
	 */
	StuHonor findObj(String acadyear, String semester, String studentId);

	List<StuHonor> getStuHonorList(String acadyear, String semester, String[] studentIds);
}
