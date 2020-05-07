package net.zdsoft.eclasscard.data.service;

import java.util.List;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.dto.AttanceSearchDto;
import net.zdsoft.eclasscard.data.entity.EccGateAttance;
import net.zdsoft.framework.entity.Pagination;

public interface EccGateAttanceService extends BaseService<EccGateAttance, String> {

	public EccGateAttance findByStuId(String studentId);
	
	/**
	 * ͨ
	 * @param unitId
	 * @param attDto
	 * @return
	 */
	public List<EccGateAttance> getMyClassListByCon(String unitId,AttanceSearchDto attDto);
	/**
	 * ͨ
	 * @param unitId
	 * @param attDto
	 * @return
	 */
	public List<EccGateAttance> getSchoolListByCon(String unitId,List<Clazz> clazzList,AttanceSearchDto attDto,Pagination page);
	/**
	 * 
	 * @param id
	 * @param status
	 */
	public void updateStatus(String[] ids,int status);
}
