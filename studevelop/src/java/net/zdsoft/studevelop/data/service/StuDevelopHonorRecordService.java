package net.zdsoft.studevelop.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StuDevelopHonorRecord;

public interface StuDevelopHonorRecordService extends BaseService<StuDevelopHonorRecord,String>{

	/**
	 * 查询班级学生荣誉情况
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param array
	 * @return
	 */
	public List<StuDevelopHonorRecord> findListByCls(String unitId,String acadyear, String semester,String[] array);

	/**
	 * 根据id查找学生荣誉详情
	 * @param id
	 * @return
	 */
	public StuDevelopHonorRecord findById(String id);

	/**
	 * 获得学生荣誉列表
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param stuId
	 * @return
	 */
	public List<StuDevelopHonorRecord> getHonorList(String unitId,String acadyear,String semester, String stuId);

	/**
	 * 保存荣誉
	 * @param honorRecordXJRW
	 * @param honorRecordQCYGK
	 */
	public void save(StuDevelopHonorRecord honorRecordXJRW,StuDevelopHonorRecord honorRecordQCYGK);

	/**
	 * 获得规定学年学期所有的荣誉情况列表
	 * @param acadyear
	 * @param semester
	 * @param unitId
	 * @return
	 */
	public List<StuDevelopHonorRecord> findAllhonor(String acadyear,String semester, String unitId);

	/**
	 * 通过类型查找所有荣誉情况
	 * @param honortype
	 * @param acadyear
	 * @param semester
	 * @param unitId
	 * @return
	 */
	public List<StuDevelopHonorRecord> findByHonortype(String honortype,
			String acadyear, String semester, String unitId);

	/**
	 * 通过类型和班级Id查找班级所有学生的荣誉情况
	 * @param unitId
	 * @param honortype
	 * @param acadyear
	 * @param semester
	 * @param array
	 * @return
	 */
	public List<StuDevelopHonorRecord> findByfindBytypeAndclass(String unitId,String honortype, String acadyear, String semester, String[] array);

	/**
	 * 通过id删除荣誉信息
	 * @param id
	 */
	public void deleteById(String id);


}
