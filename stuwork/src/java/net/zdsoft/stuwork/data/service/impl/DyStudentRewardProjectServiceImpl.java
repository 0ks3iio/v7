package net.zdsoft.stuwork.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dao.DyStudentRewardProjectDao;
import net.zdsoft.stuwork.data.dto.DyStudentRewardSettingDto;
import net.zdsoft.stuwork.data.entity.DyStudentRewardProject;
import net.zdsoft.stuwork.data.entity.DyStudentRewardSetting;
import net.zdsoft.stuwork.data.service.DyStudentRewardProjectService;
import net.zdsoft.stuwork.data.service.DyStudentRewardSettingService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("dyStudentRewardProjectService")
public class DyStudentRewardProjectServiceImpl extends BaseServiceImpl<DyStudentRewardProject, String> implements DyStudentRewardProjectService{
	
	
	@Autowired
	private DyStudentRewardProjectDao dyStudentRewardProjectDao;
	
	@Autowired
	private DyStudentRewardSettingService dyStudentRewardSettingService;
	
	
	@Override
	protected BaseJpaRepositoryDao<DyStudentRewardProject, String> getJpaDao() {
		return dyStudentRewardProjectDao;
	}

	@Override
	protected Class<DyStudentRewardProject> getEntityClass() {
		return DyStudentRewardProject.class;
	}

	@Override
	public List<DyStudentRewardSettingDto> findListByClassType(
			String classesType, String unitId,boolean withDefault,String studentRewardPeriod) {
		List<DyStudentRewardSettingDto> dyStudentRewardSettingDtos = new ArrayList<DyStudentRewardSettingDto>();
		List<String> projectIds = new ArrayList<String>();
		projectIds.add("1");
		Map<String,List<DyStudentRewardSetting>> settingMap = new HashMap<String,List<DyStudentRewardSetting>>();
		List<DyStudentRewardProject> studentRewardProjects =new ArrayList<DyStudentRewardProject>();
		List<DyStudentRewardSetting> studentRewardSettings =new ArrayList<DyStudentRewardSetting>();
		if(StuworkConstants.STUDENT_REWARD_GAME.equals(classesType)){
			studentRewardProjects= dyStudentRewardProjectDao.findListByClassesType(classesType,unitId);
			for (DyStudentRewardProject dyStudentRewardProject : studentRewardProjects) {
				projectIds.add(dyStudentRewardProject.getId());
			}
			studentRewardSettings = dyStudentRewardSettingService.findListByProjectIdsOrderByRewardGradeAndRewardLevel(projectIds.toArray(new String[0]),unitId);
		}else if(StuworkConstants.STUDENT_REWARD_SCHOOL.equals(classesType)){
			studentRewardProjects = dyStudentRewardProjectDao.findListByClassesTypeOrderByProjectType(classesType, unitId);
			if(withDefault){
				if(CollectionUtils.isEmpty(studentRewardProjects)){
					studentRewardProjects = dyStudentRewardProjectDao.findListByClassesTypeOrderByProjectType(classesType, StuworkConstants.STUDENT_REWARD_ID);
					unitId = StuworkConstants.STUDENT_REWARD_ID;
				}
			}
			for (DyStudentRewardProject dyStudentRewardProject : studentRewardProjects) {
				projectIds.add(dyStudentRewardProject.getId());
			}
			studentRewardSettings = dyStudentRewardSettingService.findListByProjectIdsOrderByRewardGradeAndRewardLevel(projectIds.toArray(new String[0]),unitId);
		}else if(StuworkConstants.STUDENT_REWARD_FESTIVAL.equals(classesType)){
			if(StringUtils.isNotBlank(studentRewardPeriod)){
				studentRewardProjects = dyStudentRewardProjectDao.findListByClassesTypeOrderByProjectType(classesType,unitId,Integer.parseInt(studentRewardPeriod));
			}else{
				studentRewardProjects = dyStudentRewardProjectDao.findListByClassesTypeOrderByProjectType(classesType,unitId);
			}
			
			if(withDefault){
				if(CollectionUtils.isEmpty(studentRewardProjects)){
					studentRewardProjects = dyStudentRewardProjectDao.findListByClassesTypeOrderByProjectType(classesType, StuworkConstants.STUDENT_REWARD_ID);
					unitId = StuworkConstants.STUDENT_REWARD_ID;
				}
			}
			for (DyStudentRewardProject dyStudentRewardProject : studentRewardProjects) {
				projectIds.add(dyStudentRewardProject.getId());
			}
			studentRewardSettings = dyStudentRewardSettingService.findListByProjectIdsOrderByRewardPeriodAndRewardLevel(projectIds.toArray(new String[0]),unitId);
		}
		
		for (DyStudentRewardSetting setting : studentRewardSettings) {
			List<DyStudentRewardSetting> settingList = settingMap.get(setting.getProjectId());
			if(CollectionUtils.isEmpty(settingList)){
				settingList = new ArrayList<DyStudentRewardSetting>();
			}
			settingList.add(setting);
			settingMap.put(setting.getProjectId(), settingList);
		}
		for (DyStudentRewardProject dyStudentRewardProject : studentRewardProjects) {
			DyStudentRewardSettingDto dto = new DyStudentRewardSettingDto();
			dto.setDyStudentRewardProject(dyStudentRewardProject);
			List<DyStudentRewardSetting> settingList = settingMap.get(dyStudentRewardProject.getId());
			if(CollectionUtils.isEmpty(settingList)){
				settingList=new ArrayList<DyStudentRewardSetting>();
			}
			dto.setDyStudentRewardSettings(settingList);
			dyStudentRewardSettingDtos.add(dto);
		}
		
		return dyStudentRewardSettingDtos;
	}

	@Override
	public List<DyStudentRewardProject> findListByRewardPeriodAndRewardClasses(
			String rewardPeriod, String rewardClasses, String unitId) {
		int period = 0;
		if(StringUtils.isNotBlank(rewardPeriod)){
			period =Integer.parseInt(rewardPeriod);
		}
		return dyStudentRewardProjectDao.findListByRewardPeriodAndRewardClasses(
				period,rewardClasses,unitId);
	}

	@Override
	public DyStudentRewardProject findByClassesAndName(String rewardClasses,
			String projectName, String unitId) {
		return dyStudentRewardProjectDao.findByClassesAndName(rewardClasses,
				projectName,unitId);
	}

	@Override
	public List<DyStudentRewardProject> findByClasses(String rewardClasses, String classType, String unitId) {
		return dyStudentRewardProjectDao.findByTypeClassesAndName(rewardClasses, classType, unitId);
	}

	@Override
	public List<DyStudentRewardProject> findByUnitId(String unitId) {
		return dyStudentRewardProjectDao.findByUnitId(unitId) ;
	}

	@Override
	public List<DyStudentRewardSettingDto> findListByFestival(
			final String classesType,final String unitId,Pagination page) {
		List<DyStudentRewardProject> projects =findListByFestivalWithOutZero(classesType,unitId);
		final Map<String,DyStudentRewardProject> projectMap = new HashMap<String, DyStudentRewardProject>();
		for (DyStudentRewardProject project : projects) {
			projectMap.put(project.getId(), project);
		}
		List<DyStudentRewardSetting> settings=new ArrayList<DyStudentRewardSetting>();
		if(projectMap.keySet().size()>0){
			settings = dyStudentRewardSettingService.findInProjectId(projectMap.keySet().toArray(new String[0]),page);
			List<DyStudentRewardSetting> setting2 = dyStudentRewardSettingService.findInProjectId(projectMap.keySet().toArray(new String[0]),null);
			page.setMaxRowCount(setting2.size());
		}
		List<DyStudentRewardSettingDto> dtoList = new ArrayList<DyStudentRewardSettingDto>();
		for (DyStudentRewardSetting setting : settings) {
			DyStudentRewardSettingDto dto = new DyStudentRewardSettingDto();
			dto.setDyStudentRewardProject(projectMap.get(setting.getProjectId()));
			List<DyStudentRewardSetting> newSetting=new ArrayList<DyStudentRewardSetting>();
			newSetting.add(setting);
			dto.setDyStudentRewardSettings(newSetting);
			dtoList.add(dto);
		}
		return dtoList;
	}

	public List<DyStudentRewardProject> findListByFestivalWithOutZero(
			String classesType, String unitId) {
		return dyStudentRewardProjectDao.findListByFestivalWithOutZero(
				classesType, unitId) ;
	}
	@Override
	public void deletByIds(String[] ids){
		dyStudentRewardProjectDao.deletByIds(ids);
	}
	@Override
	public List<DyStudentRewardProject> findByClassesType(String classesType, String unitId) {
		return dyStudentRewardProjectDao.findListByClassesType(classesType, unitId);
	}
	

}
