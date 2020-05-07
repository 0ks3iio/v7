package net.zdsoft.diathesis.data.service.impl;

import net.zdsoft.basedata.entity.CustomRole;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.CustomRoleRemoteService;
import net.zdsoft.basedata.remote.service.CustomRoleUserRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.dao.DiathesisOptionDao;
import net.zdsoft.diathesis.data.dao.DiathesisProjectDao;
import net.zdsoft.diathesis.data.dao.DiathesisSetDao;
import net.zdsoft.diathesis.data.dao.DiathesisStructureDao;
import net.zdsoft.diathesis.data.dto.DiathesisFieldDto;
import net.zdsoft.diathesis.data.dto.DiathesisGlobalSettingDto;
import net.zdsoft.diathesis.data.dto.DiathesisRecordSettingDto;
import net.zdsoft.diathesis.data.dto.DiathesisStructureSettingDto;
import net.zdsoft.diathesis.data.entity.*;
import net.zdsoft.diathesis.data.service.*;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PinyinUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: panlf
 * @Date: 2019/3/29 9:44
 */
@Service("diathesisSetService")
public class DiathesisSetServiceImpl extends BaseServiceImpl<DiathesisSet, String>  implements DiathesisSetService {
	@Autowired
	private DiathesisProjectDao diathesisProjectDao;

	@Autowired
	private DiathesisSubjectFieldService diathesisSubjectFieldService;
	@Autowired
	private DiathesisRecordSetService diathesisRecordSetService;
	@Autowired
	private DiathesisStructureService diathesisStructureService;
	@Autowired
	private DiathesisOptionService diathesisOptionService;

	@Autowired
	private CustomRoleUserRemoteService customRoleUserRemoteService;
	@Autowired
	private DiathesisOptionDao diathesisOptionDao;

	@Autowired
	private CustomRoleRemoteService customRoleRemoteService;

	@Autowired
	private DiathesisStructureDao diathesisStructureDao;

    @Autowired
    private DiathesisSetDao diathesisSetDao;

    @Autowired
	private DiathesisProjectExService diathesisProjectExService;

	@Autowired
	private UnitRemoteService unitRemoteService;

	@Autowired
	private DiathesisCustomAuthorService diathesisCustomAuthorService;

	@Override
	protected BaseJpaRepositoryDao<DiathesisSet, String> getJpaDao() {
		return diathesisSetDao;
	}

	@Override
	protected Class<DiathesisSet> getEntityClass() {
		return DiathesisSet.class;
	}

	@Override
	public DiathesisGlobalSettingDto findGlobalByUnitId(String unitId) {

		DiathesisSet setting = findByUnitId(unitId);
		if(setting==null){
			setting =findByUnitId(DiathesisConstant.TEMPLATE_UNIT_ID).clone();
			setting.setUnitId(unitId);
			setting.setId(UuidUtils.generateUuid());
			setting.setModifyTime(new Date());
			setting.setCreationTime(new Date());
			setting.setOperator("admin");
			diathesisSetDao.save(setting);
		}
		DiathesisGlobalSettingDto dto = transform(setting);
		Map<String, List<DiathesisFieldDto>> fieldMap = diathesisSubjectFieldService.findMapByUnitId(unitId);
		dto.setCompulsoryField(fieldMap.get(DiathesisConstant.SUBJECT_FEILD_BX));
		dto.setElectiveField(fieldMap.get(DiathesisConstant.SUBJECT_FEILD_XX));
		dto.setAcademicField(fieldMap.get(DiathesisConstant.SUBJECT_FEILD_XY));
		return dto;
	}



	@Override
	public DiathesisRecordSettingDto findRecordSettingByProjectId(String projectId, String unitId, String realName) {

		DiathesisProject record = diathesisProjectDao.findByIdAndProjectType(projectId,DiathesisConstant.PROJECT_RECORD);
		DiathesisProjectEx ex=diathesisProjectExService.findByUnitIdAndProjectId(unitId,projectId);
		if(record==null)return null;
		record.setAuditorTypes(ex.getAuditorTypes());
		record.setInputTypes(ex.getInputTypes());
		List<DiathesisStructure> structureList = diathesisStructureDao.findByProjectId(projectId);
		List<DiathesisOption> optionList = diathesisOptionDao.findByProjectId(projectId);
		return generateRecordSettingDto(record,structureList,optionList,unitId);

	}

	@Override
	public void saveRecordSetting(DiathesisRecordSettingDto dto,String realName) {
		String unitId = dto.getUnitId();
		String projectId = dto.getId();

		DiathesisRecordSet recordSet=diathesisRecordSetService.findOne(projectId);

		recordSet.setCountType(dto.getCountType());
		diathesisRecordSetService.saveOne(recordSet);

		DiathesisProjectEx ex = diathesisProjectExService.findByUnitIdAndProjectId(unitId, projectId);
		DiathesisProjectEx saveEx=EntityUtils.copyProperties(ex,new DiathesisProjectEx());

		if(StringUtils.isBlank(saveEx.getId())){
			saveEx.setId(UuidUtils.generateUuid());
		}
		saveEx.setModifyTime(new Date());
		saveEx.setOperator(realName);
		saveEx.setInputTypes(turnToString(dto.getInputTypes()));
		saveEx.setAuditorTypes(turnToString(dto.getAuditorTypes()));


		List<DiathesisStructure> structureList = diathesisStructureService.findListByProjectId(projectId);
		List<DiathesisOption> optionList = diathesisOptionService.findListByUnitIdAndProjectId(unitId, projectId);
		Map<String, DiathesisStructure> structureMap = EntityUtils.getMap(structureList, x -> x.getId(), x -> x);
		Map<String, DiathesisOption> optionMap = EntityUtils.getMap(optionList, x -> x.getId(), x -> x);
		List<String> oldIds = Stream.concat(structureList.stream().map(x -> x.getId()), optionList.stream().map(x -> x.getId())).collect(Collectors.toList());

		List<DiathesisStructure> saveStructureList=new ArrayList<>();
		List<DiathesisOption> saveOptionList=new ArrayList<>();

		List<DiathesisStructureSettingDto> structureSettingDtoList = dto.getStructure();
		if(CollectionUtils.isEmpty(structureSettingDtoList))return;
		int index=0;
		for (DiathesisStructureSettingDto structure : structureSettingDtoList) {
			if((DiathesisConstant.DATA_TYPE_2.equals(structure.getDataType()) || DiathesisConstant.DATA_TYPE_3.equals(structure.getDataType())) && CollectionUtils.isEmpty(structure.getOption())){
				throw new RuntimeException("单选,多选类型的字段,至少设置一项");
			}
			DiathesisStructure diathesisStructure;
			if(StringUtils.isBlank(structure.getId())){
				//新增
				diathesisStructure=new DiathesisStructure();
				diathesisStructure.setId(UuidUtils.generateUuid());
				diathesisStructure.setProjectId(projectId);
				diathesisStructure.setUnitId(unitId);
			}else{
				//编辑
				oldIds.remove(structure.getId());
				diathesisStructure = structureMap.get(structure.getId());
			}
			diathesisStructure.setIsMust(structure.getIsMust());
			diathesisStructure.setIsShow(structure.getIsShow());
			diathesisStructure.setDataType(structure.getDataType());
			diathesisStructure.setTitle(structure.getTitle());
			diathesisStructure.setColNo(index++);
			diathesisStructure.setIsCount(structure.getIsCount());
			saveStructureList.add(diathesisStructure);
			if(CollectionUtils.isEmpty(structure.getOption()))continue;
			int opIndex=0;
			for (DiathesisOption option : structure.getOption()) {
				DiathesisOption diathesisOption ;
				String opId = option.getId();
				if(StringUtils.isBlank(opId)){
					//新增
					diathesisOption=new DiathesisOption();
					diathesisOption.setId(UuidUtils.generateUuid());
					diathesisOption.setUnitId(unitId);
					diathesisOption.setStructureId(diathesisStructure.getId());
					diathesisOption.setProjectId(projectId);
				}else{
					//编辑
					oldIds.remove(opId);
					diathesisOption=optionMap.get(opId);
				}
				diathesisOption.setContentTxt(option.getContentTxt());
				diathesisOption.setColNo(opIndex++);
				saveOptionList.add(diathesisOption);
			}

		}

		diathesisProjectExService.save(saveEx);
		if(CollectionUtils.isNotEmpty(oldIds)){
			diathesisStructureService.deleteByIdIn(oldIds);
			diathesisOptionService.deleteByIn(oldIds);
		}
		if(saveStructureList.size()>0){
			diathesisStructureDao.saveAll(saveStructureList);
		}
		if(saveOptionList.size()>0){
			diathesisOptionDao.saveAll(saveOptionList);
		}

	}

	@Override
	public DiathesisSet findByUnitId(String unitId) {
		return diathesisSetDao.findByUnitId(unitId);
	}

	@Override
	public void deleteRoleByUnitIdAndRoleCode(String unitId, String roleCode) {
		diathesisSetDao.deleteRoleByUnitIdAndRoleCode(unitId,"^"+roleCode+",|(?<=[,])"+roleCode+",|,"+roleCode+"$");
	}

	/*@Override
	public boolean exitByAndUnitIdRoleCode(String unitId, String roleCode) {
		Integer count = diathesisProjectService.countByUnitIdAndLikeRoleCode(unitId, "%" + roleCode + "%");
		return count!=0;
	}*/

	@Override
	public void setChildUnit(DiathesisGlobalSettingDto dto, String realName,String regionCode) {
		//todo
//		List<Unit> list = SUtils.dt(unitRemoteService.findByRegionCode(regionCode.substring(0, 4) + "%"), Unit.class);
//		for (Unit u:list){
//			if(u.getUnitClass()==Unit.UNIT_CLASS_SCHOOL){
//
//			}
//		}
//		Map<String, List<String>> map = dto.getEducationScoreScopesMap();
//		for(Map.Entry<String,List<String>> entry: map.entrySet()){
//			String unitId=entry.getKey();
//			List<String> list1=entry.getValue();
//			DiathesisSet set = diathesisSetDao.findByUnitId(unitId);
//
//		}
	}

	//保存有自主设置权限的学校
	@Override
	public void saveSchoolSet(DiathesisGlobalSettingDto dto) {
		DiathesisSet set = findByUnitId(dto.getUnitId());
		if(!DiathesisConstant.MUTUAL_TYPE_MAP.keySet().contains(dto.getMutualType())){
			throw new RuntimeException("没有这个类型的学生互评方式");
		}
		if(!set.getMutualType().equals(dto.getMutualType())){
			deleteCurrentMutualInfo(dto.getUnitId());
		}
		List<String> evaluation = dto.getEvaluation();
		Collections.sort(evaluation);
		//delEvaluationInProject(dto.getUnitId(),evaluation,toList(set.getEvaluation()));

		save(transformToDiathesisSet(dto));
	}

	private void deleteCurrentMutualInfo(String unitId) {

	}

	//保存没有自主设置权限的学校
	@Override
	public void saveSchoolNoAuthorSet(DiathesisGlobalSettingDto dto) {

		DiathesisSet oldSet = findByUnitId(dto.getUnitId());
		DiathesisSet diathesisSet = EntityUtils.copyProperties(oldSet, new DiathesisSet());
		diathesisSet.setModifyTime(Calendar.getInstance().getTime());
		if(!DiathesisConstant.MUTUAL_TYPE_MAP.keySet().contains(dto.getMutualType())){
			throw new RuntimeException("没有这个类型的学生互评方式");
		}
		diathesisSet.setMutualType(dto.getMutualType());
		diathesisSet.setAuditorTypes(turnToString(dto.getAuditorTypes()));
		diathesisSet.setInputTypes(turnToString(dto.getInputTypes()));

		List<String> evaluation = dto.getEvaluation();
		Collections.sort(evaluation);
		//delEvaluationInProject(dto.getUnitId(),evaluation,toList(oldSet.getEvaluation()));

		diathesisSet.setEvaluation(turnToString(evaluation));

		save(diathesisSet);
	}

//	private void delEvaluationInProject(String unitId, List<String> evaluation,List<String> oldEvaluation) {
//		if(CollectionUtils.isEmpty(evaluation)){
//			throw new RuntimeException("评价人类型不能为空");
//		}
//		//如果没有改动 则不用删除
//		if(evaluation.containsAll(oldEvaluation) && oldEvaluation.containsAll(evaluation))return;
//		//找出old里有, 新的里没有的类型
//		List<String> delList = oldEvaluation.stream().filter(x -> !evaluation.contains(x)).collect(Collectors.toList());
//		if(CollectionUtils.isNotEmpty(delList)){
			//Integer count=diathesisScoreTypeService.countByUnitIdAndTypeAndScoreTypeIn(unitId,DiathesisConstant.INPUT_TYPE_3,delList.toArray(new String[0]));
			//if(count!=null &&  count>0)throw  new RuntimeException("评价已使用,无法删除");
			//List<DiathesisProjectEx> exList=diathesisProjectExService.findChildTypeByUnitId(unitId);
//			for (DiathesisProjectEx ex : exList) {
//				String[] old = ex.getEvaluationTypes().split(",");
//				List<String> newEval=new ArrayList<>();
//				for (String s : old) {
//					if(!delList.contains(s)){
//						newEval.add(s);
//					}
//				}
//				if(CollectionUtils.isEmpty(newEval)){
//					throw new RuntimeException("不能删除项目中唯一设置的评价类型");
//				}
//				ex.setEvaluationTypes(turnToString(newEval));
//			}
//			diathesisProjectExService.saveAll(exList.toArray(new DiathesisProjectEx[0]));
//		}

//		Integer[] propor=new Integer[evaluation.size()];
//		for (int i = 0; i < propor.length; i++) {
//			if(i==propor.length-1){
//				propor[i]=100-(evaluation.size()-1)*(100/evaluation.size());
//			}else{
//				propor[i]=100/evaluation.size();
//			}
//
//		}
//		String proportionsString=StringUtils.join(propor,",");
//		diathesisProjectExService.updateProportionsByUnitId(unitId,proportionsString);
//	}

	//查找单位的设置 如果不存在就初始化
	@Override
	public DiathesisSet findSetIfNullCreate(String unitId,String operator) {
		DiathesisSet set = diathesisSetDao.findByUnitId(unitId);
		if(set==null){
			DiathesisSet diathesisSet = new DiathesisSet();
			DiathesisSet template = diathesisSetDao.findByUnitId(DiathesisConstant.TEMPLATE_UNIT_ID);
			EntityUtils.copyProperties(template,diathesisSet);
			diathesisSet.setOperator(operator);
			diathesisSet.setCreationTime(Calendar.getInstance().getTime());
			diathesisSet.setModifyTime(Calendar.getInstance().getTime());
			diathesisSet.setUnitId(unitId);
			diathesisSetDao.save(diathesisSet);
			return diathesisSet;
		}else{
			return set;
		}
	}

	//有权限的教育局
	@Override
	public void saveEduSet(DiathesisGlobalSettingDto dto) {
		//todo  教育局设置 没设置的都改成默认值
		//1.保存自己的设置信息
		diathesisSetDao.save(transformToDiathesisSet(dto));
		//2.修改下属单位的 分数比例信息

		if(dto.getScoreType().equals(DiathesisConstant.SCORE_PROPORTION_REGULAR)){

			//diathesisSetDao.findByUnitIn()
			Map<String, List<String>> unitMap = dto.getEducationScoreScopesMap();
			if(CollectionUtils.isNotEmpty(unitMap.keySet())){
				DiathesisSet templateSet = findByUnitId(DiathesisConstant.TEMPLATE_UNIT_ID);
				//遍历unitMap 更新比例设置
				for (Map.Entry<String,  List<String>> entry : unitMap.entrySet()) {
					DiathesisSet setting = findByUnitId(entry.getKey());
					if(setting==null){
						setting = templateSet.clone();
						setting.setUnitId(entry.getKey());
						setting.setId(UuidUtils.generateUuid());
						setting.setCreationTime(new Date());
						setting.setOperator(dto.getOperator());
					}
					setting.setModifyTime(new Date());
					setting.setScoreScopes(turnToString(entry.getValue()));
					save(setting);
					//diathesisSetDao.updateScoreScopeByUnitId(entry.getKey(),turnToString(entry.getValue()));
				}
			}
		}
	}

	/**
	 * 查找正在使用的设置信息
	 * @param unitId
	 * @return
	 */
	@Override
	public DiathesisGlobalSettingDto findUsingSetByUnitId(String unitId) {
		while(diathesisCustomAuthorService.hasSetAuthor(unitId)){
			Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
			if(unit.getParentId().equals(Unit.TOP_UNIT_GUID) || unit.getId().equals(unit.getRootUnitId())){
				unitId=DiathesisConstant.TEMPLATE_UNIT_ID;
				break;
			}
			unitId=unit.getParentId();
		}
		return findGlobalByUnitId(unitId);
	}

	@Override
	public void saveUnAuthorRecordSet(DiathesisRecordSettingDto dto, String realName) {
		//保存审核人. 录入人
		DiathesisProjectEx ex = diathesisProjectExService.findOneBy(new String[]{"unitId", "projectId"}, new String[]{dto.getUnitId(), dto.getId()});
		DiathesisProjectEx saveEx;
		if(ex==null){
			saveEx=new DiathesisProjectEx();
			saveEx.setUnitId(dto.getUnitId());
			saveEx.setId(UuidUtils.generateUuid());
			saveEx.setProjectId(dto.getId());
		}else{
			saveEx = EntityUtils.copyProperties(ex, new DiathesisProjectEx());
		}
		saveEx.setModifyTime(new Date());
		saveEx.setOperator(realName);
		saveEx.setInputTypes(turnToString(dto.getInputTypes()));
		saveEx.setAuditorTypes(turnToString(dto.getAuditorTypes()));
		diathesisProjectExService.save(saveEx);
	}

	@Override
	public void deleteRole(String unitId, String roleId, String roleCode) {
		//删除全局设置中的 角色 (录入人,审核人中的)
		deleteRoleByUnitIdAndRoleCode(unitId,roleCode);
		//sys_custom_role 表删除
		customRoleRemoteService.deleteById(roleId);
		//sys_custom_role_user 表删除
		customRoleUserRemoteService.deleteByRoleId(roleId);
	}

	private DiathesisSet transformToEduSet(DiathesisGlobalSettingDto dto) {
		DiathesisSet set = diathesisSetDao.findByUnitId(dto.getUnitId());
		DiathesisSet diathesisSet = EntityUtils.copyProperties(set, new DiathesisSet());
		return null;
	}

	private void addStructure(DiathesisStructureSettingDto structure, String unitId, String projectId,
							  int index,List<DiathesisOption> optionList,List<DiathesisStructure> structureList) {
		DiathesisStructure diathesisStructure = new DiathesisStructure();
		String structureId=UuidUtils.generateUuid();
		diathesisStructure.setId(structureId);
		diathesisStructure.setUnitId(unitId);
		diathesisStructure.setProjectId(projectId);
		diathesisStructure.setColNo(index);
		diathesisStructure.setTitle(structure.getTitle());
		diathesisStructure.setDataType(structure.getDataType());
		diathesisStructure.setIsShow(structure.getIsShow());
		diathesisStructure.setIsMust(structure.getIsMust());
		structureList.add(diathesisStructure);
		if(structure.getOption()==null || structure.getOption().size()==0)return;
		for (int i = 0; i <structure.getOption().size() ; i++) {
			optionList.add(addOption(structure.getOption().get(i),structureId,unitId,projectId,i));
		}
	}


	private DiathesisOption addOption(DiathesisOption option, String structureId,String unitId,String projectId,int index) {
		DiathesisOption newOPtion = new DiathesisOption();
		newOPtion.setId(UuidUtils.generateUuid());
		newOPtion.setUnitId(unitId);
		newOPtion.setStructureId(structureId);
		newOPtion.setColNo(index);
		newOPtion.setProjectId(projectId);
		newOPtion.setContentTxt(option.getContentTxt());
		return newOPtion;
	}


	private DiathesisRecordSettingDto generateRecordSettingDto(DiathesisProject record, List<DiathesisStructure> structureList, List<DiathesisOption> optionList,String unitId) {
		Map<String, List<DiathesisOption>> option = optionList.stream().collect(Collectors.groupingBy(x -> x.getStructureId()));

		DiathesisRecordSet recordSet = diathesisRecordSetService.findOne(record.getId());

		if(recordSet==null){
			recordSet = new DiathesisRecordSet();
			recordSet.setId(record.getId());
			recordSet.setUnitId(unitId);
			recordSet.setOperator(record.getOperator());
			recordSet.setCountType(DiathesisConstant.COUNT_TYPE_0);
			recordSet.setModifyTime(new Date());
			diathesisRecordSetService.saveOne(recordSet);
		}

		DiathesisRecordSettingDto dto = new DiathesisRecordSettingDto();
		dto.setId(record.getId());
		dto.setProjectName(record.getProjectName());

		dto.setCountType(recordSet.getCountType());
		dto.setInputTypes(toList(record.getInputTypes()));
		dto.setAuditorTypes(toList(record.getAuditorTypes()));


		LinkedHashMap<String, String> inputTypesMap = getPeopleHashMap(unitId);
		dto.setPeopleTypesMap(inputTypesMap);

		dto.setStructure(structureList.stream().map(x->{
			DiathesisStructureSettingDto structureSettingDto = new DiathesisStructureSettingDto();
			structureSettingDto.setId(x.getId());
			structureSettingDto.setColNo(x.getColNo());
			structureSettingDto.setTitle(x.getTitle());
			structureSettingDto.setIsShow(x.getIsShow());
			structureSettingDto.setDataType(x.getDataType());
			structureSettingDto.setOption(option.get(x.getId()));
			structureSettingDto.setIsMust(x.getIsMust());
			structureSettingDto.setIsCount(x.getIsCount());
			return structureSettingDto;
		}).collect(Collectors.toList()));
		return dto;
	}

	private LinkedHashMap<String,String> generateMap(List<String> list,Map<String,String> roleMap){
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		for (String s:list){
			map.put(s,roleMap.get(s));
		}
		return map;
	}
	private DiathesisSet transformToDiathesisSet(DiathesisGlobalSettingDto dto) {
		String unitId = dto.getUnitId();

		//保存字段信息
		List<DiathesisSubjectField> fieldList = diathesisSubjectFieldService.findByUnitId(unitId);
		Map<String, String> fieldIdCodeMap = EntityUtils.getMap(fieldList, x -> x.getId(), x -> x.getFieldCode());
		Map<String, DiathesisSubjectField> oldDataMap = EntityUtils.getMap(fieldList, x -> x.getId(), x -> x);
		List<DiathesisSubjectField> saveFieldList=new ArrayList<>();
		List<DiathesisFieldDto> compulsoryFieldList = dto.getCompulsoryField();//必修
		List<DiathesisFieldDto> academicFieldList = dto.getAcademicField();//学业
		List<DiathesisFieldDto> electiveFieldList = dto.getElectiveField();//选修
		List<String> fieldCodes = Stream.concat(compulsoryFieldList.stream(), electiveFieldList.stream()).map(x ->fieldIdCodeMap.get(x.getFieldId())).collect(Collectors.toList());
		if(!fieldCodes.containsAll(DiathesisConstant.COMPULSORY_MAP.keySet()) &&
			fieldCodes.containsAll(DiathesisConstant.ELECTIVE_MAP.keySet())){
			throw new RuntimeException("必修和选修课的内置字段不能删除!");
		}
		setSaveFieldList(unitId, fieldList, oldDataMap, saveFieldList, compulsoryFieldList,DiathesisConstant.SUBJECT_FEILD_BX);
		setSaveFieldList(unitId, fieldList, oldDataMap, saveFieldList, academicFieldList,DiathesisConstant.SUBJECT_FEILD_XY);
		setSaveFieldList(unitId, fieldList, oldDataMap, saveFieldList, electiveFieldList,DiathesisConstant.SUBJECT_FEILD_XX);
		List<String> notDelField=new ArrayList<>();
		notDelField.addAll(EntityUtils.getList(compulsoryFieldList,x->x.getFieldId()));
		notDelField.addAll(EntityUtils.getList(academicFieldList,x->x.getFieldId()));
		notDelField.addAll(EntityUtils.getList(electiveFieldList,x->x.getFieldId()));
		List<String> delFieldList = oldDataMap.keySet().stream().filter(x -> !notDelField.contains(x)).collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(delFieldList)){
			diathesisSubjectFieldService.deleteByIds(delFieldList);
		}
		if(CollectionUtils.isNotEmpty(saveFieldList)){
			diathesisSubjectFieldService.saveAllEntity(saveFieldList);
		}


		DiathesisSet set = diathesisSetDao.findByUnitId(unitId);
		DiathesisSet diathesisSet = new DiathesisSet();
		EntityUtils.copyProperties(set,diathesisSet);
		diathesisSet.setMutualType(dto.getMutualType());
		diathesisSet.setOperator(dto.getOperator());
		diathesisSet.setUnitId(unitId);
		diathesisSet.setModifyTime(Calendar.getInstance().getTime());
		diathesisSet.setAuditorTypes(turnToString(dto.getAuditorTypes()));
		diathesisSet.setInputTypes(turnToString(dto.getInputTypes()));
		diathesisSet.setInputValueType(dto.getInputValueType());
		diathesisSet.setRankItems(turnToString(dto.getRankItems()));
		diathesisSet.setRankValues(turnToString(dto.getRankValues()));
		diathesisSet.setScoreScopes(turnToString(dto.getScoreScopes()));
		diathesisSet.setScoreType(dto.getScoreType());
		diathesisSet.setIsAssignPoint(dto.getIsAssignPoint());
		diathesisSet.setSemesterScoreProp(turnToString(dto.getSemesterScoreProp()));
		if(CollectionUtils.isNotEmpty(dto.getEvaluation())){
			List<String> evaluation = dto.getEvaluation();
			Collections.sort(evaluation);
			diathesisSet.setEvaluation(turnToString(evaluation));
		}
		return diathesisSet;
	}

	private void setSaveFieldList(String unitId, List<DiathesisSubjectField> fieldList,
								  Map<String, DiathesisSubjectField> oldDataMap,
								  List<DiathesisSubjectField> saveFieldList,
								  List<DiathesisFieldDto> newFieldList,
								  String type) {
		Set<String> set = EntityUtils.getSet(newFieldList, x -> x.getFieldName());
		if(set.size()!=newFieldList.size()){
			throw new RuntimeException("科目字段不能重复!");
		}
		if(CollectionUtils.isEmpty(newFieldList)){
			throw new RuntimeException("科目字段不能为空");
		}
		for (int i = 0; i < newFieldList.size(); i++) {
			DiathesisSubjectField saveEntity;
			DiathesisFieldDto temp = newFieldList.get(i);
			if(StringUtils.isBlank(temp.getFieldName())){
				throw new RuntimeException("科目字段名称不能为空");
			}
			if(StringUtils.isBlank(temp.getFieldId())){
				saveEntity=new DiathesisSubjectField();
				saveEntity.setId(UuidUtils.generateUuid());
				saveEntity.setUnitId(unitId);
				saveEntity.setFieldCode(getFieldCode(fieldList,temp.getFieldName(), type));
			}else{
				saveEntity=oldDataMap.get(temp.getFieldId());
			}
			if((saveEntity.getFieldCode().equals(DiathesisConstant.COMPULSORY_SCORE)
					|| saveEntity.getFieldCode().equals(DiathesisConstant.ELECTIVE_SCORE))
					&& !DiathesisConstant.DIATHESIS_YES.equals(temp.getIsUsing())){
				throw new RuntimeException("必修和选修的成绩字段为必选!");
			}
			saveEntity.setModifyTime(new Date());
			saveEntity.setSubjectType(type);
			saveEntity.setSortNum(i);
			saveEntity.setIsUsing(temp.getIsUsing());

			if(!DiathesisConstant.COMPULSORY_MAP.keySet().contains(saveEntity.getFieldCode()) &&
					!DiathesisConstant.ELECTIVE_MAP.keySet().contains(saveEntity.getFieldCode())){
				saveEntity.setFieldName(temp.getFieldName());
			}
			saveFieldList.add(saveEntity);
		}
	}

	private String getFieldCode(List<DiathesisSubjectField> fieldList, String fieldName,String type) {
		List<String> codeList = EntityUtils.getList(fieldList, x -> x.getFieldCode());
		String prefix=DiathesisConstant.FIELD_TYPE_PRE_MAP.get(type);

		String pinyin = PinyinUtils.toHanyuPinyin(fieldName, true);
		if(pinyin.length()>8){
			pinyin=pinyin.substring(0,8);
		}
		String code = prefix + pinyin;
		int i = 0;
		while (true) {
			String t = i == 0 ? "" : (i < 9 ? "0" + i : "" + i);
			if (codeList.contains(code+t)) {
				i++;
			} else {
				code += t;
				break;
			}
		}
		return code;
	}


	private DiathesisGlobalSettingDto transform(DiathesisSet setting) {
		String unitId=setting.getUnitId();
		DiathesisGlobalSettingDto settingDto=new DiathesisGlobalSettingDto();
		settingDto.setId(setting.getId());
		settingDto.setRankItems(toList(setting.getRankItems()));
		settingDto.setRankValues(toList(setting.getRankValues()));
		settingDto.setInputValueType(setting.getInputValueType());
		settingDto.setScoreType(setting.getScoreType());
		settingDto.setInputTypes(toList(setting.getInputTypes()));
		settingDto.setAuditorTypes(toList(setting.getAuditorTypes()));
		settingDto.setMutualType(setting.getMutualType());
		settingDto.setPeopleTypesMap(getPeopleHashMap(unitId));
		settingDto.setScoreScopes(toList(setting.getScoreScopes()));
		settingDto.setScoreType(setting.getScoreType());
		settingDto.setEvaluation(toList(setting.getEvaluation()));
		settingDto.setIsAssignPoint(setting.getIsAssignPoint());
		settingDto.setSemesterScoreProp(toList(setting.getSemesterScoreProp()));
		return settingDto;
	}

	private LinkedHashMap<String, String> getPeopleHashMap(String unitId) {
		List<CustomRole> list = SUtils.dt(customRoleRemoteService.findListBy(new String[]{"unitId", "subsystems"}, new String[]{unitId, "78,"}), CustomRole.class);
		LinkedHashMap<String, String> LinkedMap = new LinkedHashMap<>();
		//排序
		list.sort((x,y)->{
			Integer xOrder=Integer.valueOf(x.getOrderId());
			Integer yOrder=Integer.valueOf(y.getOrderId());
			if(xOrder==null)return 1;
			if(yOrder==null)return -1;
			return xOrder>yOrder?1:-1;
		});
		for(CustomRole c:list){
			LinkedMap.put(c.getRoleCode(),c.getRoleName());
		}
		return LinkedMap;
	}

	private List<String> toList(String str) {
		return StringUtils.isBlank(str)?new ArrayList<String>():Arrays.asList(str.split(","));
	}

	private String  turnToString(List<String> auditorTypes) {
		if(auditorTypes==null || auditorTypes.size()==0)return null;
		return StringUtils.join(auditorTypes.toArray(new String[0]),",");
	}

}
