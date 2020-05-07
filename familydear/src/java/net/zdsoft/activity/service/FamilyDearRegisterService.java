package net.zdsoft.activity.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import net.zdsoft.activity.dto.FamilyActureDto;
import net.zdsoft.activity.dto.FamilyDearAuditDto;
import net.zdsoft.activity.dto.FamilyStaDto;
import net.zdsoft.activity.entity.FamilyDearRegister;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.familydear.entity.FamdearActualReport;
import net.zdsoft.framework.entity.Pagination;

public interface FamilyDearRegisterService extends BaseService<FamilyDearRegister,String>{

	public List<FamilyDearRegister> getFamilyDearRegisterList(String unitId ,String teacherId, String[] arrangeIds);

	public List<FamilyDearRegister> getFamilyDearRegisterListByArrangeId( String[] arrangeIds);
	
	public List<FamilyDearRegister> getFamilyDearReByArrangeIdAndActivityIdAndUnitId(String unitId,String activityId,String arrangeId);
	
	public List<FamilyDearRegister> getFamilyDearReByActivityIdsAndStatusAndUnitId(String unitId,int status,String activityId,String[] arrangeIds,String contryName);
	
	public List<FamilyDearRegister> findBy(String unitId,int status,String activityId,String[] arrangeIds,String[] teaIds,String teacherName,String contryName,Pagination page);

	public List<FamilyDearAuditDto> findByParams(String unitId, int status, String activityId, String[] arrangeIds, String[] teaIds, String teacherName, String contryName, Pagination page);
	
	public List<FamilyDearRegister> getFamilyDearRegisterList(String unitId ,String teacherId);
	
	public List<FamilyDearRegister> getListByUnitIdAndTeacherIds(String unitId, String deptId, String year, Date startTime, Date endTime, String[] activityIds, Pagination page);

	public void deleteByIds(String[] arrangeIds);
	
	public FamilyStaDto getListByUnitAndDeptId(FamilyStaDto familyStaDto,List<FamdearActualReport> famdearActualReports,String[] userSets);
	
	public FamilyActureDto getListByUnitAndDeptId(FamilyActureDto familyActureDto,String unitId,String deptId,String[] activityIds);
}
