package net.zdsoft.eclasscard.data.service;

import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccUserFace;
import net.zdsoft.framework.entity.Pagination;

public interface EccUserFaceService extends BaseService<EccUserFace, String>{

	/**
	 * 分页查询单位下数据
	 * @param unitId
	 * @param fillName 填充学生姓名
	 * @param page
	 * @return
	 */
	public List<EccUserFace> findByUnitIdPage(String unitId,boolean fillName, Pagination page);
	
	/**
	 * 根据用户ownerId查询人脸
	 * @param ownerIds
	 * @return
	 */
	public List<EccUserFace> findByOwnerIds(String[] ownerIds);

	/**
	 * 批量保存按学号保存的人脸图片
	 * @param array
	 * @param unitId
	 * @return 返回未找到，验证失败，验证成功数
	 */
	public String saveBacthUpload(String array, String unitId);
	
	Integer countByUnitId(String unitId);

	public void deleteByOwnerIds(Set<String> failIds,String unitId);
	
	/**
	 * 查找上传了人脸的单位
	 * @return
	 */
	public Set<String> findUseFaceUnitIds();
	
	/**
	 * 清除非在校生数据
	 */
	public void deleteStuNotInSchool();

}
