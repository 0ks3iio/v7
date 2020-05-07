package net.zdsoft.familydear.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.familydear.entity.FamDearArrange;

public interface FamDearArrangeService extends BaseService<FamDearArrange, String>  {
	
	public List<FamDearArrange> getFamilyDearArrangeList(String[] activitys);
	
	public List<FamDearArrange> getFamilyDearArrangeListByContryName(String contryName);

	public List<FamDearArrange> getFamilyDearArrangeListByParams(Date startTime, Date endTime, String contryName);

	public List<FamDearArrange> getFamilyDearArrangeListByUnitId(String[] activitys,String unitId);

	public List<FamDearArrange> getFamilyDearArrangeListByContryNameAndActivityId(String contryName,String activityId);
 }
