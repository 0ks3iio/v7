package net.zdsoft.stuwork.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dao.DyStudentRewardSettingDao;
import net.zdsoft.stuwork.data.dto.DyStudentRewardSettingDto;
import net.zdsoft.stuwork.data.entity.DyStudentRewardProject;
import net.zdsoft.stuwork.data.entity.DyStudentRewardSetting;
import net.zdsoft.stuwork.data.service.DyStudentRewardPointService;
import net.zdsoft.stuwork.data.service.DyStudentRewardProjectService;
import net.zdsoft.stuwork.data.service.DyStudentRewardSettingService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service("dyStudentRewardSettingService")
public class DyStudentRewardSettingServiceImpl extends BaseServiceImpl<DyStudentRewardSetting, String> implements DyStudentRewardSettingService{
	
	@Autowired
	private DyStudentRewardSettingDao dyStudentRewardSettingDao;
	
	@Autowired
	private DyStudentRewardProjectService dyStudentRewardProjectService;
	
	@Autowired
	private DyStudentRewardPointService dyStudentRewardPointService;
	
	@Override
	protected BaseJpaRepositoryDao<DyStudentRewardSetting, String> getJpaDao() {
		return dyStudentRewardSettingDao;
	}

	@Override
	protected Class<DyStudentRewardSetting> getEntityClass() {
		return DyStudentRewardSetting.class;
	}

	@Override
	public List<DyStudentRewardSetting> findListByProjectIdsOrderByRewardGradeAndRewardLevel(
			String[] projectIds, String unitId) {
		return dyStudentRewardSettingDao.findListByProjectIdsOrderByRewardGradeAndRewardLevelOrder(unitId,projectIds);
	}

	@Override
	public List<DyStudentRewardSetting> findListByProjectIdsOrderByRewardPeriodAndRewardLevel(
			String[] projectIds, String unitId) {
		return dyStudentRewardSettingDao.findListByProjectIdsOrderByRewardPeriodAndRewardLevelOrder(unitId,projectIds);
	}
	@Override
	public void saveSettingsOne(List<DyStudentRewardSetting> dyStudentRewardSettings,String unitId, String classesType) {
		Set<String> projectIds =dyStudentRewardSettings.stream().map(DyStudentRewardSetting::getProjectId).collect(Collectors.toSet());
		List<DyStudentRewardProject> oldProjectList=dyStudentRewardProjectService.findListByIds(projectIds.toArray(new String[0]));
		Set<String> deleteIds =new HashSet<>();
		Map<String,List<DyStudentRewardSetting>> proIdListMap=new HashMap<>();
		for (DyStudentRewardSetting setting : dyStudentRewardSettings) {
			if(StringUtils.isBlank(setting.getProjectId())){
				continue;
			}
			if(!proIdListMap.containsKey(setting.getProjectId())){
				proIdListMap.put(setting.getProjectId(), new ArrayList<>());
			}
			proIdListMap.get(setting.getProjectId()).add(setting);
		}
		Map<String,List<DyStudentRewardSetting>> lastListMap=new HashMap<>();
		if(CollectionUtils.isNotEmpty(oldProjectList)){
			List<DyStudentRewardProject> projectInsertList=new ArrayList<>();
			DyStudentRewardProject project = null;
			for (DyStudentRewardProject oldProject : oldProjectList) {
				if(StuworkConstants.STUDENT_REWARD_ID.equals(oldProject.getUnitId())){//默认的project 添加成本单位的
					project=new DyStudentRewardProject();
					project.setId(UuidUtils.generateUuid());
					project.setRewardClasses(oldProject.getRewardClasses());
					project.setClassesType(oldProject.getClassesType());
					project.setProjectRemark(oldProject.getProjectRemark());
					project.setRewardPeriod(oldProject.getRewardPeriod());
					project.setProjectName(oldProject.getProjectName());
					project.setProjectType(oldProject.getProjectType());
					project.setRewardPointType(oldProject.getRewardPointType());
					project.setRewardCountPointType(oldProject.getRewardCountPointType());
					project.setUnitId(unitId);
					projectInsertList.add(project);
					if(proIdListMap.containsKey(oldProject.getId())){
						List<DyStudentRewardSetting> insList=proIdListMap.get(oldProject.getId());
						for(DyStudentRewardSetting setting:insList){
							setting.setProjectId(project.getId());
						}
						lastListMap.put(project.getId(), insList);
					}
				}else{
					lastListMap.put(oldProject.getId(), proIdListMap.get(oldProject.getId()));
					deleteIds.add(oldProject.getId());
				}
			}//402880556b54d305016b54e9f2ae003e
			if(CollectionUtils.isNotEmpty(projectInsertList)){
				dyStudentRewardProjectService.saveAll(projectInsertList.toArray(new DyStudentRewardProject[0]));
			}
		}
		List<DyStudentRewardSetting> settingInsertList=new ArrayList<>();
		for(Entry<String, List<DyStudentRewardSetting>> entry:lastListMap.entrySet()){
			List<DyStudentRewardSetting> inSettingList=entry.getValue();
			if(CollectionUtils.isNotEmpty(inSettingList)){
				int i=1;
				for (DyStudentRewardSetting inSetting : inSettingList) {
					inSetting.setId(UuidUtils.generateUuid());
					inSetting.setUnitId(unitId);
					inSetting.setRewardLevelOrder(i);
					inSetting.setRewardPeriod(0);
					i++;
					settingInsertList.add(inSetting);
				}
			}
		}
		if(CollectionUtils.isNotEmpty(deleteIds)){
			dyStudentRewardSettingDao.deleteByProIds(unitId, deleteIds.toArray(new String[0]));
		}
		if(CollectionUtils.isNotEmpty(settingInsertList)){
			saveAll(settingInsertList.toArray(new DyStudentRewardSetting[0]));
		}
	}
	@Override
	public void saveSettings(
			List<DyStudentRewardSetting> dyStudentRewardSettings,String unitId, String classesType) {
		List<DyStudentRewardSetting> insertDto=new ArrayList<DyStudentRewardSetting>();
		List<DyStudentRewardProject> insertProject = new ArrayList<DyStudentRewardProject>();
		List<DyStudentRewardSetting> updateDto=new ArrayList<DyStudentRewardSetting>();
		Set<String> projectIds = new HashSet<String>();
		for (DyStudentRewardSetting dyStudentRewardSetting : dyStudentRewardSettings) {
			String projectId= dyStudentRewardSetting.getProjectId();
			projectIds.add(projectId);
		}
		Map<String,DyStudentRewardProject> projectMap=dyStudentRewardProjectService.findMapByIdIn(projectIds.toArray(new String[0]));
		Map<String,DyStudentRewardProject> addProjectMap = new HashMap<String, DyStudentRewardProject>();
		for (DyStudentRewardSetting dyStudentRewardSetting : dyStudentRewardSettings) {
			if(StuworkConstants.STUDENT_REWARD_ID.equals(dyStudentRewardSetting.getUnitId())){
				String projectId= dyStudentRewardSetting.getProjectId();
				DyStudentRewardProject oldProject = projectMap.get(projectId);
				DyStudentRewardProject project = new DyStudentRewardProject();
				if(oldProject!=null && StuworkConstants.STUDENT_REWARD_ID.equals(oldProject.getUnitId())){
					project.setId(UuidUtils.generateUuid());
					project.setRewardClasses(oldProject.getRewardClasses());
					project.setClassesType(oldProject.getClassesType());
					project.setProjectRemark(oldProject.getProjectRemark());
					project.setRewardPeriod(oldProject.getRewardPeriod());
					project.setProjectName(oldProject.getProjectName());
					project.setProjectType(oldProject.getProjectType());
					project.setRewardPointType(oldProject.getRewardPointType());
					project.setRewardCountPointType(oldProject.getRewardCountPointType());
					project.setUnitId(unitId);
					insertProject.add(project);
					projectMap.remove(projectId);
					addProjectMap.put(projectId, project);
				}
				dyStudentRewardSetting.setId(UuidUtils.generateUuid());
				DyStudentRewardProject addProject= addProjectMap.get(projectId);
				if(addProject!=null){
					dyStudentRewardSetting.setProjectId(addProject.getId());
				}else{
					throw new RuntimeException("保存失败，没用对应的项目");
				}
				
				dyStudentRewardSetting.setUnitId(unitId);
				if(StringUtils.isNotBlank(dyStudentRewardSetting.getRewardPoint())){
					dyStudentRewardSetting.setRewardPoint(Float.parseFloat(dyStudentRewardSetting.getRewardPoint())+"");
				}
				insertDto.add(dyStudentRewardSetting);
			}else{
				//delIds.add(dyStudentRewardSetting.getId());
				if(StringUtils.isNotBlank(dyStudentRewardSetting.getRewardPoint())){
					dyStudentRewardSetting.setRewardPoint(Float.parseFloat(dyStudentRewardSetting.getRewardPoint())+"");
				}
				updateDto.add(dyStudentRewardSetting);
			}
		}
		
		if(CollectionUtils.isNotEmpty(insertDto)){
			saveAll(insertDto.toArray(new DyStudentRewardSetting[0]));
		}
		if(CollectionUtils.isNotEmpty(insertProject)){
			dyStudentRewardProjectService.saveAll(insertProject.toArray(new DyStudentRewardProject[0]));
		}
		if(CollectionUtils.isNotEmpty(updateDto)){
			List<DyStudentRewardSetting> newUpdateDto=new ArrayList<DyStudentRewardSetting>();
			saveAll(updateDto.toArray(new DyStudentRewardSetting[0]));
			//TODO 更新其他艺术节
			if(StuworkConstants.STUDENT_REWARD_FESTIVAL.equals(classesType)){
				Map<String,String> updatePointMap = new HashMap<String, String>();
				Map<String,String> updateRemarkMap = new HashMap<String, String>();
				
				for (DyStudentRewardSetting setting : updateDto) {
					DyStudentRewardProject project = projectMap.get(setting.getProjectId());
					if(project!=null){
						updatePointMap.put(project.getRewardClasses()+"-"+project.getProjectName()+"-"+setting.getRewardLevel(), setting.getRewardPoint());
						updateRemarkMap.put(project.getRewardClasses()+"-"+project.getProjectName()+"-"+setting.getRewardLevel(), setting.getRemark());
					}
				}
				List<DyStudentRewardSettingDto> studentRewardSettingDtoList = dyStudentRewardProjectService.findListByClassType(classesType, unitId, false, null);
				for (DyStudentRewardSettingDto dyStudentRewardSettingDto : studentRewardSettingDtoList) {
					DyStudentRewardProject project = dyStudentRewardSettingDto.getDyStudentRewardProject();
					List<DyStudentRewardSetting> dyStudentRewardSettingList = dyStudentRewardSettingDto.getDyStudentRewardSettings();
					for (DyStudentRewardSetting setting : dyStudentRewardSettingList) {
						if(setting.getRewardPeriod()!=0){
							String point = updatePointMap.get(project.getRewardClasses()+"-"+project.getProjectName()+"-"+setting.getRewardLevel());
							String remark = updateRemarkMap.get(project.getRewardClasses()+"-"+project.getProjectName()+"-"+setting.getRewardLevel());
							if(point==null){
								setting.setRewardPoint("");						
							}else{
								setting.setRewardPoint(point);						
							}
							if(remark==null){
								setting.setRemark("");
							}else{
								setting.setRemark(remark);
							}

						}
						newUpdateDto.add(setting);
					}
				}
				
				if(CollectionUtils.isNotEmpty(newUpdateDto)){
					saveAll(newUpdateDto.toArray(new DyStudentRewardSetting[0]));
				}
			}
		}
	}
	@Override
	public void updateFestival(String[] projectIds,String rewardPeriod, String rewardClasses,
			String acadyear, String semester, String unitId){
		List<DyStudentRewardProject> projectList = dyStudentRewardProjectService.findListByRewardPeriodAndRewardClasses(rewardPeriod,rewardClasses,unitId);
		if(CollectionUtils.isNotEmpty(projectList)){
			List<String> projectIdset=Arrays.asList(projectIds);
			for(DyStudentRewardProject project:projectList){
				if(!projectIdset.contains(project.getId())){//有一个不是自己的 说明有相同的
					throw new RuntimeException("已经有相同年份的"+rewardClasses);
				}
			}
		}
		List<DyStudentRewardProject> defaultProjectList = dyStudentRewardProjectService.findListByRewardPeriodAndRewardClasses(StuworkConstants.STUDENT_REWARD_PERIOD_DEFAULT,rewardClasses,unitId);
		if(CollectionUtils.isEmpty(defaultProjectList)){
			throw new RuntimeException("必须先设置奖项和积分!");
		}
		List<DyStudentRewardProject> oldProjectList =dyStudentRewardProjectService.findListByIds(projectIds);
		List<DyStudentRewardProject> updateProjectList=new ArrayList<>();
		for (DyStudentRewardProject oldProject : oldProjectList) {//对project
			oldProject.setAcadyear(acadyear);
			oldProject.setSemester(semester);
			oldProject.setRewardPeriod(Integer.valueOf(rewardPeriod));
			updateProjectList.add(oldProject);
		}
		List<DyStudentRewardSetting> oldSettings =dyStudentRewardSettingDao.findListByProjectIdsOrderByRewardPeriodAndRewardLevelOrder(unitId, projectIds);
		for (DyStudentRewardSetting oldSetting : oldSettings) {
			oldSetting.setRewardPeriod(Integer.valueOf(rewardPeriod));
		}
		if(CollectionUtils.isNotEmpty(updateProjectList)){
			dyStudentRewardProjectService.saveAll(updateProjectList.toArray(new DyStudentRewardProject[0]));
		}
		if(CollectionUtils.isNotEmpty(oldSettings)){
			saveAll(oldSettings.toArray(new DyStudentRewardSetting[0]));
		}
	}
	@Override
	public void addFestival(String rewardPeriod, String rewardClasses,String acadyear, String semester,
			String unitId) {
		List<DyStudentRewardProject> projectList = dyStudentRewardProjectService.findListByRewardPeriodAndRewardClasses(rewardPeriod,rewardClasses,unitId);
		if(CollectionUtils.isNotEmpty(projectList)){
			throw new RuntimeException("已经有相同年份的"+rewardClasses);
		}
		List<DyStudentRewardProject> defaultProjectList = dyStudentRewardProjectService.findListByRewardPeriodAndRewardClasses(StuworkConstants.STUDENT_REWARD_PERIOD_DEFAULT,rewardClasses,unitId);
		if(CollectionUtils.isEmpty(defaultProjectList)){
			throw new RuntimeException("必须先设置奖项和积分!");
		}
		List<DyStudentRewardProject> addProjectList = new ArrayList<DyStudentRewardProject>();
		List<DyStudentRewardSetting> addSettingList = new ArrayList<DyStudentRewardSetting>();
		Set<String> projectIds = new HashSet<String>();
		Map<String,String> idMap = new HashMap<String, String>();
		for (DyStudentRewardProject oldProject : defaultProjectList) {
			projectIds.add(oldProject.getId());
			DyStudentRewardProject project = new DyStudentRewardProject();
			project.setId(UuidUtils.generateUuid());
			project.setRewardClasses(oldProject.getRewardClasses());
			project.setClassesType(oldProject.getClassesType());
			project.setProjectRemark(oldProject.getProjectRemark());
			project.setProjectName(oldProject.getProjectName());
			project.setRewardPeriod(Integer.parseInt(rewardPeriod));
			project.setProjectType(oldProject.getProjectType());
			project.setRewardPointType(oldProject.getRewardPointType());
			project.setRewardCountPointType(oldProject.getRewardCountPointType());
			project.setUnitId(unitId);
			project.setAcadyear(acadyear);
			project.setSemester(semester);
			idMap.put(oldProject.getId(), project.getId());
			addProjectList.add(project);
		}
		
		List<DyStudentRewardSetting> defaultSettings = dyStudentRewardSettingDao.findListByProjectIdsOrderByRewardPeriodAndRewardLevelOrder(unitId, projectIds.toArray(new String[0]));
		
		
		for (DyStudentRewardSetting defaultSetting : defaultSettings) {
			DyStudentRewardSetting setting = new DyStudentRewardSetting();
			setting.setId(UuidUtils.generateUuid());
			String newProjectId = idMap.get(defaultSetting.getProjectId());
			setting.setProjectId(newProjectId);             
			setting.setRewardGrade(defaultSetting.getRewardGrade());            
			setting.setRewardLevel(defaultSetting.getRewardLevel());           
			setting.setRewardLevelOrder(defaultSetting.getRewardLevelOrder());
			setting.setRewardPeriod(Integer.parseInt(rewardPeriod));          
			setting.setRewardPoint(defaultSetting.getRewardPoint());  
			setting.setRemark(defaultSetting.getRemark()); 
			setting.setUnitId(unitId);  
			addSettingList.add(setting);
		}
		
		if(CollectionUtils.isNotEmpty(addProjectList)){
			dyStudentRewardProjectService.saveAll(addProjectList.toArray(new DyStudentRewardProject[0]));
		}
		
		if(CollectionUtils.isNotEmpty(addSettingList)){
			saveAll(addSettingList.toArray(new DyStudentRewardSetting[0]));
		}
	}

	@Override
	public void rewardGameSave(String rewardClasses, String projectName,
			String rewardGrade, String rewardLevel,String unitId,String projectId, String settingId) {
		DyStudentRewardProject oldProject = null;
		DyStudentRewardSetting oldSetting =null;
		if(StringUtils.isNotBlank(projectId)&&StringUtils.isNotBlank(settingId)){
			oldProject = dyStudentRewardProjectService.findOne(projectId);
			oldSetting = findOne(settingId);
		}
		DyStudentRewardProject project = dyStudentRewardProjectService.findByClassesAndName(rewardClasses,projectName,unitId);
		if(oldProject!=null){
			if(project!=null){
				if(!oldProject.getId().equals(project.getId())){
					throw new RuntimeException("已存在相同名称的项目,无法修改");
				}
				List<DyStudentRewardSetting> settings = findListByProjectIdsOrderByRewardGradeAndRewardLevel(new String[]{project.getId()}, unitId);
				for (DyStudentRewardSetting dyStudentRewardSetting : settings) {
					if(dyStudentRewardSetting.getRewardGrade().equals(rewardGrade)&&dyStudentRewardSetting.getRewardLevel().equals(rewardLevel)&&(!dyStudentRewardSetting.getId().equals(oldSetting.getId()))) {
						throw new RuntimeException("相同项目下已存在同名的级别和奖励，无法修改");
					}
				}
				oldProject.setRewardClasses(rewardClasses);
				oldProject.setProjectName(projectName);
				oldSetting.setRewardGrade(rewardGrade);
				oldSetting.setRewardLevel(rewardLevel);
				dyStudentRewardProjectService.save(oldProject);
				save(oldSetting);
			}else{
				dyStudentRewardProjectService.save(oldProject);
				List<DyStudentRewardSetting> settings = findListByProjectIdsOrderByRewardGradeAndRewardLevel(new String[]{oldProject.getId()}, unitId);
				for (DyStudentRewardSetting dyStudentRewardSetting : settings) {
					if(dyStudentRewardSetting.getRewardGrade().equals(rewardGrade)&&dyStudentRewardSetting.getRewardLevel().equals(rewardLevel)&&(!dyStudentRewardSetting.getId().equals(oldSetting.getId()))) {
						throw new RuntimeException("相同项目下已存在同名的级别和奖励，无法修改");
					}
				}
				oldProject.setRewardClasses(rewardClasses);
				oldProject.setProjectName(projectName);
				oldSetting.setRewardGrade(rewardGrade);
				oldSetting.setRewardLevel(rewardLevel);
				dyStudentRewardProjectService.save(oldProject);
				save(oldSetting);
			}
			
		}else{
			if(project!=null){
				
				List<DyStudentRewardSetting> settings = findListByProjectIdsOrderByRewardGradeAndRewardLevel(new String[]{project.getId()}, unitId);
				for (DyStudentRewardSetting dyStudentRewardSetting : settings) {
					if(dyStudentRewardSetting.getRewardGrade().equals(rewardGrade)&&dyStudentRewardSetting.getRewardLevel().equals(rewardLevel)) {
						throw new RuntimeException("相同项目下已存在同名的级别和奖励，无法新增");
					}
				}
				DyStudentRewardSetting setting = new DyStudentRewardSetting();
				setting.setId(UuidUtils.generateUuid());
				setting.setProjectId(project.getId());             
				setting.setRewardGrade(rewardGrade);            
				setting.setRewardLevel(rewardLevel);           
				setting.setRewardLevelOrder(settings.size()+1);
				setting.setRewardPeriod(0);          
				setting.setRewardPoint("");  
				setting.setRemark(""); 
				setting.setUnitId(unitId); 
				save(setting);
			}else{
				//List<DyStudentRewardProject> projects = dyStudentRewardProjectService.findByClasses(rewardClasses,unitId);
				
				project = new DyStudentRewardProject();
				project.setId(UuidUtils.generateUuid());
				project.setRewardClasses(rewardClasses);
				project.setClassesType(StuworkConstants.STUDENT_REWARD_GAME);
//			project.setProjectRemark(null);
				project.setProjectName(projectName);
				project.setRewardPeriod(0);
				project.setProjectType("0001");
				project.setRewardPointType("2");
				project.setRewardCountPointType("1");
				project.setUnitId(unitId);
//			project.setAcadyear(null);
//			project.setSemester(null);
				dyStudentRewardProjectService.save(project);
				
				DyStudentRewardSetting setting = new DyStudentRewardSetting();
				setting.setId(UuidUtils.generateUuid());
				setting.setProjectId(project.getId());             
				setting.setRewardGrade(rewardGrade);            
				setting.setRewardLevel(rewardLevel);           
				setting.setRewardLevelOrder(1);
				setting.setRewardPeriod(0);          
				setting.setRewardPoint("");  
				setting.setRemark(""); 
				setting.setUnitId(unitId); 
				save(setting);
			}
			
		}
		
	}

	@Override
	public List<DyStudentRewardSetting> findByUnitId(String unitId) {
		return dyStudentRewardSettingDao.findByUnitId(unitId) ;
	}
	@Override
	public void deleteByProIds(String unitId, String[] projectIds){
		dyStudentRewardSettingDao.deleteByProIds(unitId, projectIds);;
	}

	@Override
	public void deleteSetting(String settingId, String projectId) {
		List<DyStudentRewardSetting> settings = dyStudentRewardSettingDao.findByProjectId(projectId);
		if(settings.size()==1){
			dyStudentRewardPointService.deleteByProjectId(projectId);
			dyStudentRewardProjectService.delete(projectId);
			delete(settingId);
		}else{
			dyStudentRewardPointService.deleteBySettingId(settingId);
			delete(settingId);
		}
	}

	@Override
	public List<DyStudentRewardSetting> findInProjectId(String[] projectIds,
			Pagination page) {
		if(page!=null){
			Pageable pageable = Pagination.toPageable(page);
			return dyStudentRewardSettingDao.findInProjectId(projectIds,
					pageable);
		}else{
			return dyStudentRewardSettingDao.findInProjectId(projectIds,
					null);
		}
		
	}

	@Override
	public String doImport(List<String[]> datas,String unitId) {
		
		Json importResultJson=new Json();
		List<String[]> errorDataList=new ArrayList<String[]>();
		int successCount  =0;
		String[] errorData=null;
		
		
		List<DyStudentRewardSettingDto>  dtoList = dyStudentRewardProjectService.findListByClassType(StuworkConstants.STUDENT_REWARD_GAME, unitId, false, "0");
		
		Map<String,Map<String,DyStudentRewardSetting>> dtoMap = new HashMap<String,Map<String,DyStudentRewardSetting>>();
		Map<String,DyStudentRewardSettingDto> dtoMap2 = new HashMap<String,DyStudentRewardSettingDto>();
		if(CollectionUtils.isNotEmpty(dtoList)) {
			for (DyStudentRewardSettingDto dto : dtoList) {
				DyStudentRewardProject project = dto.getDyStudentRewardProject();
				String key = project.getRewardClasses()+"-"+project.getProjectName();
				dtoMap2.put(key, dto);
				Map<String,DyStudentRewardSetting> settingMap = dtoMap.get(key);
				if(settingMap==null || settingMap.isEmpty()) {
					settingMap = new HashMap<String,DyStudentRewardSetting>();
				}
				List<DyStudentRewardSetting> settings = dto.getDyStudentRewardSettings();
				for (DyStudentRewardSetting setting : settings) {
					settingMap.put(setting.getRewardGrade()+"-"+setting.getRewardLevel(), setting);
				}
				dtoMap.put(key, settingMap);
			}
		}
		
		
		List<DyStudentRewardProject> projectList = new ArrayList<DyStudentRewardProject>();
		List<DyStudentRewardSetting> addSetting = new ArrayList<DyStudentRewardSetting>();
		List<DyStudentRewardSetting> updateSetting = new ArrayList<DyStudentRewardSetting>();
		
		
		for (String[] data : datas) {
			
			
			if(StringUtils.isBlank(data[0])) {
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="类型";
				errorData[2]="";
				errorData[3]="类型不能为空";
				errorDataList.add(errorData);
				continue;
			}else {
				if(data[0].length()>25){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="类型";
					errorData[2]=data[0];
					errorData[3]="类型长度不能超过25";
					errorDataList.add(errorData);
					continue;
				}
			}
			if(StringUtils.isBlank(data[1])) {
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="项目名称";
				errorData[2]="";
				errorData[3]="项目名称不能为空";
				errorDataList.add(errorData);
				continue;
			}else {
				if(data[1].length()>25){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="项目名称";
					errorData[2]=data[1];
					errorData[3]="项目名称长度不能超过25";
					errorDataList.add(errorData);
					continue;
				}
			}
			if(StringUtils.isBlank(data[2])) {
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="级别";
				errorData[2]="";
				errorData[3]="级别不能为空";
				errorDataList.add(errorData);
				continue;
			}else {
				if(data[2].length()>50){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="级别";
					errorData[2]=data[2];
					errorData[3]="级别长度不能超过50";
					errorDataList.add(errorData);
					continue;
				}
			}
			if(StringUtils.isBlank(data[3])) {
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="奖级";
				errorData[2]="";
				errorData[3]="奖级不能为空";
				errorDataList.add(errorData);
				continue;
			}else {
				if(data[3].length()>50){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="奖级";
					errorData[2]=data[3];
					errorData[3]="奖级长度不能超过50";
					errorDataList.add(errorData);
					continue;
				}
			}
			if(StringUtils.isNotBlank(data[4])) {
				Pattern pattern = Pattern.compile("^[+]?([0-9]+(.)?+([0-9]{1})?+([0-9]{1})?)$");
				boolean wsFlag=pattern.matcher(data[4]).matches();
				if(wsFlag) {
					if(data[4].length()>6) {
						errorData = new String[4];
						errorData[0]=errorDataList.size()+1+"";
						errorData[1]="分值";
						errorData[2]=data[4];
						errorData[3]="分值不能超过999";
						errorDataList.add(errorData);
						continue;

					}
				}else {
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="分值";
					errorData[2]=data[4];
					errorData[3]="分值最多是2位小数";
					errorDataList.add(errorData);
					continue;
				}
				
			}
			if(StringUtils.isNotBlank(data[5]) && data[5].length()>100 ) {
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="备注";
				errorData[2]=data[5];
				errorData[3]="备注长度不能超过100";
				errorDataList.add(errorData);
				continue;
			}
			
			
			
			String key = data[0]+"-"+data[1];
			DyStudentRewardSettingDto dto = dtoMap2.get(key);
			if(dto!=null) {
				DyStudentRewardProject project =  dto.getDyStudentRewardProject();
				Map<String,DyStudentRewardSetting> settingMap = dtoMap.get(key);
				if(settingMap!=null) {
					DyStudentRewardSetting oldSetting = settingMap.get(data[2]+"-"+data[3]);
					if(oldSetting!=null) {
						oldSetting.setRewardPoint(data[4]);  
						oldSetting.setRemark(data[5]); 
						updateSetting.add(oldSetting);
					}else {
						DyStudentRewardSetting setting = new DyStudentRewardSetting();
						setting.setId(UuidUtils.generateUuid());
						setting.setProjectId(project.getId());             
						setting.setRewardGrade(data[2]);            
						setting.setRewardLevel(data[3]);           
						setting.setRewardLevelOrder(1);
						setting.setRewardPeriod(0);          
						setting.setRewardPoint(data[4]);  
						setting.setRemark(data[5]); 
						setting.setUnitId(unitId); 
						
						addSetting.add(setting);
					}
				}else {
					DyStudentRewardSetting setting = new DyStudentRewardSetting();
					setting.setId(UuidUtils.generateUuid());
					setting.setProjectId(project.getId());             
					setting.setRewardGrade(data[2]);            
					setting.setRewardLevel(data[3]);           
					setting.setRewardLevelOrder(1);
					setting.setRewardPeriod(0);          
					setting.setRewardPoint(data[4]);  
					setting.setRemark(data[5]); 
					setting.setUnitId(unitId); 
					
					addSetting.add(setting);
				}
				
				
			}else {
				DyStudentRewardProject project = new DyStudentRewardProject();
				project.setId(UuidUtils.generateUuid());
				project.setRewardClasses(data[0]);
				project.setClassesType(StuworkConstants.STUDENT_REWARD_GAME);
				project.setProjectName(data[1]);
				project.setRewardPeriod(0);
				project.setProjectType("0001");
				project.setRewardPointType("2");
				project.setRewardCountPointType("1");
				project.setUnitId(unitId);
				projectList.add(project);
				
				DyStudentRewardSetting setting = new DyStudentRewardSetting();
				setting.setId(UuidUtils.generateUuid());
				setting.setProjectId(project.getId());             
				setting.setRewardGrade(data[2]);            
				setting.setRewardLevel(data[3]);           
				setting.setRewardLevelOrder(1);
				setting.setRewardPeriod(0);          
				setting.setRewardPoint(data[4]);  
				setting.setRemark(data[5]); 
				setting.setUnitId(unitId); 
				
				addSetting.add(setting);
				
			}
			successCount++;
		}
		
		if(CollectionUtils.isNotEmpty(projectList)) {
			dyStudentRewardProjectService.saveAll(projectList.toArray(new DyStudentRewardProject[0]));
		}
		if(CollectionUtils.isNotEmpty(addSetting)) {
			saveAll(addSetting.toArray(new DyStudentRewardSetting[0]));
		}
		if(CollectionUtils.isNotEmpty(updateSetting)) {
			saveAll(updateSetting.toArray(new DyStudentRewardSetting[0]));
		}
		
		importResultJson.put("totalCount", datas.size());
		importResultJson.put("successCount", successCount);
		importResultJson.put("errorCount", errorDataList.size());
		importResultJson.put("errorData", errorDataList);
		return importResultJson.toJSONString();
	}


}
