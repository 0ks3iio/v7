package net.zdsoft.studevelop.mobile.service;

import org.springframework.web.multipart.MultipartFile;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.mobile.entity.StuFamilyWishes;

import java.util.List;

public interface StuFamilyWishesService extends BaseService<StuFamilyWishes,String>{
	
	/**
	 * 新增
	 * @param item
	 * @param file
	 */
	public void save(StuFamilyWishes item, MultipartFile file);
	
	/**
	 * 更新内容
	 * @param item TODO
	 */
	void update(StuFamilyWishes item);
	
	/**
	 * 更新
	 * @param item
	 * @param file
	 */
	void update(StuFamilyWishes item, MultipartFile file);
	
	/**
	 * 查询StuFamilyWishes
	 * @param acadyear
	 * @param semester
	 * @param studentId
	 * @return
	 */
	StuFamilyWishes findObj(String acadyear, String semester, String studentId);

	/**
	 * 查询StuFamilyWishes
	 * @param acadyear
	 * @param semester
	 * @param studentIds
     * @return
     */
	List<StuFamilyWishes>  getStuFamily(String acadyear,String semester , String[] studentIds);

	
}
