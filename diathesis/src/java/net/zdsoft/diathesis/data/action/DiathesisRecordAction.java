package net.zdsoft.diathesis.data.action;

import com.alibaba.fastjson.JSONArray;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.dto.*;
import net.zdsoft.diathesis.data.entity.*;
import net.zdsoft.diathesis.data.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.*;
import net.zdsoft.framework.utils.*;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/diathesis")
public class DiathesisRecordAction extends BaseAction {
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private DiathesisProjectService diathesisProjectService;
	@Autowired
	private DiathesisRecordService diathesisRecordService;
	@Autowired
	private DiathesisStructureService diathesisStructureService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private DiathesisRecordInfoService diathesisRecordInfoService;
	@Autowired
	private DiathesisOptionService diathesisOptionService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private DiathesisRoleService diathesisRoleService;

	@Autowired
	private DiathesisCustomAuthorService diathesisCustomAuthorService;



	@Autowired
	private DiathesisProjectExService diathesisProjectExService;
	@Autowired
	private DiathesisUnitService diathesisUnitService;

	@GetMapping("/getProjectList")
	@ControllerInfo("查询类目列表")
	public List<DiathesisProjectDto> getProjectList(String showStatus){
		String unitId = getLoginInfo().getUnitId();
		//todo 需要修改成正在使用的项目即可 tocheck
		List<DiathesisProject> projectList = diathesisProjectService.findListByUnitIdAndProjectTypeIn(unitId,
				new String[]{DiathesisConstant.PROJECT_TOP, DiathesisConstant.PROJECT_RECORD});
		List<DiathesisProjectDto> dtoList = new ArrayList<DiathesisProjectDto>();
		if(CollectionUtils.isNotEmpty(projectList)){
			DiathesisProjectDto dto = null;
			DiathesisProjectDto dto2 = null;
			List<DiathesisProject> topList = projectList.stream().filter(e->DiathesisConstant.PROJECT_TOP.equals(e.getProjectType())).collect(Collectors.toList());
			List<DiathesisProject> recordList = projectList.stream().filter(e->DiathesisConstant.PROJECT_RECORD.equals(e.getProjectType())).collect(Collectors.toList());
			Map<String, List<DiathesisProject>> recordMap = EntityUtils.getListMap(recordList, DiathesisProject::getParentId, Function.identity());
			for (DiathesisProject project : topList) {
				dto = new DiathesisProjectDto();
				dto.setId(project.getId());
				dto.setProjectName(project.getProjectName());
				if(recordMap.containsKey(project.getId())){
					List<DiathesisProject> record2List = recordMap.get(project.getId());
					for (DiathesisProject p : record2List) {
						dto2 = new DiathesisProjectDto();
						dto2.setId(p.getId());
						dto2.setProjectName(p.getProjectName());
						dto.getChildList().add(dto2);
					}
				}
				dtoList.add(dto);
			}

			//审核状态
			if(Constant.IS_TRUE_Str.equals(showStatus)){
				Map<String, Integer> statusMap = diathesisRecordService.findMapByProjectIdIn(unitId, EntityUtils.getList(recordList, DiathesisProject::getId).toArray(new String[0]));
				boolean flag = false;
				for (DiathesisProjectDto item : dtoList) {
					if(CollectionUtils.isNotEmpty(item.getChildList())){
						int count=0;
						for (DiathesisProjectDto item2 : item.getChildList()) {
							if(statusMap.containsKey(item.getId())&&statusMap.get(item.getId())>0){
								item2.setToAuditorNum(statusMap.get(item.getId()));
								count+=statusMap.get(item.getId());
								flag = true;
								item2.setStatus("0");
							}else{
								item2.setToAuditorNum(0);
								item2.setStatus("1");
							}
						}
						item.setToAuditorNum(count);
						if(flag){
							item.setStatus("0");
						}else{
							item.setStatus("1");
						}
					}else{
						item.setToAuditorNum(0);
						item.setStatus("1");
					}
				}
			}
		}
		return dtoList;
	}

	@GetMapping("/getRecord")
	@ControllerInfo("查询单条写实记录")
	public DiathesisRecordDto getRecord(String id, String projectId){
		if(StringUtils.isBlank(id)&&StringUtils.isBlank(projectId)){
			return null;
		}
		LoginInfo loginInfo = getLoginInfo();
		String unitId = loginInfo.getUnitId();
		DiathesisRecordDto recordDto = new DiathesisRecordDto();
		List<DiathesisRecordInfo> recordInfoList;
		Map<String, DiathesisRecordInfo> recordInfoMap = new HashMap<String, DiathesisRecordInfo>();
		if(StringUtils.isNotBlank(id)){
			DiathesisRecord record = diathesisRecordService.findOne(id);
			if(record==null){
				return null;
			}
			recordDto.setId(record.getId());
			recordDto.setStuId(record.getStuId());
			Student student = SUtils.dc(studentRemoteService.findOneById(record.getStuId()), Student.class);
			if(student!=null){
				recordDto.setStuName(student.getStudentName());
			}
			recordDto.setProjectId(record.getProjectId());
			recordDto.setAcadyear(record.getAcadyear());
			recordDto.setSemester(record.getSemester());
			recordInfoList = diathesisRecordInfoService.findListByUnitIdAndRecordIds(unitId, new String[]{id});
			if(CollectionUtils.isNotEmpty(recordInfoList)){
				recordInfoMap = EntityUtils.getMap(recordInfoList, DiathesisRecordInfo::getStructureId);
			}
		}else{
			if(User.OWNER_TYPE_STUDENT==loginInfo.getOwnerType()){
				String studentId = loginInfo.getOwnerId();
				Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
				recordDto.setStuId(studentId);
				recordDto.setStuName(student.getStudentName());
			}
			recordDto.setProjectId(projectId);
		}
		DiathesisProject project = diathesisProjectService.findOne(recordDto.getProjectId());
		if(project!=null){
			recordDto.setProjectName(project.getProjectName());
		}
		//自定义字段内容
		List<DiathesisStructure> structureList = diathesisStructureService.findListByProjectId(projectId);
		if(CollectionUtils.isNotEmpty(structureList)){
			List<DiathesisRecordInfoDto> infoDtoList = new ArrayList<DiathesisRecordInfoDto>();
			DiathesisRecordInfoDto infoDto = null;
			String usingUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_RECORD);
			List<DiathesisOption> optionList =  diathesisOptionService.findListByUnitIdAndProjectId(usingUnitId, projectId);
			Map<String, List<DiathesisOption>> optionMap = EntityUtils.getListMap(optionList, DiathesisOption::getStructureId, Function.identity());
			Map<String, String> keyValueMap = EntityUtils.getMap(optionList, DiathesisOption::getId, DiathesisOption::getContentTxt);

			for (DiathesisStructure structure : structureList) {
				infoDto = new DiathesisRecordInfoDto();
				infoDto.setTitle(structure.getTitle());
				infoDto.setStructureId(structure.getId());
				infoDto.setDataType(structure.getDataType());
				infoDto.setIsMust(structure.getIsMust());
				if(recordInfoMap.containsKey(structure.getId())){
					DiathesisRecordInfo recordInfo = recordInfoMap.get(structure.getId());
					infoDto.setId(recordInfo.getId());
					infoDto.setContentTxt(recordInfo.getContentTxt());
					if(DiathesisConstant.DATA_TYPE_2.equals(structure.getDataType()) || DiathesisConstant.DATA_TYPE_3.equals(structure.getDataType())){
						if(StringUtils.isNotBlank(recordInfo.getContentTxt())){
							String[] split = recordInfo.getContentTxt().split(",");
							String resultTxt = "";
							for (String sp : split) {
								resultTxt += ","+keyValueMap.get(sp);
							}
							infoDto.setResultTxt(resultTxt.substring(1));
						}
					}else if(DiathesisConstant.DATA_TYPE_4.equals(structure.getDataType())){
						String contxt = recordInfo.getContentTxt();
						if(StringUtils.isNotBlank(contxt)){
							ArrayList<FileDto> fileDtos = new ArrayList<>();
							String[] allFile = contxt.split(DiathesisConstant.FILE_SPLIT);
							for (String one : allFile) {
								FileDto dto = new FileDto();
								dto.setFileName(StringUtils.substringBefore(one, ","));
								dto.setFilePath(StringUtils.substringAfter(one, ","));
								fileDtos.add(dto);
							}
							infoDto.setFileList(fileDtos);
						}
					}
				}
				if(DiathesisConstant.DATA_TYPE_2.equals(structure.getDataType()) || DiathesisConstant.DATA_TYPE_3.equals(structure.getDataType())){
					infoDto.setOptionList(optionMap.get(structure.getId()));
				}
				infoDtoList.add(infoDto);
			}
			recordDto.setRecordInfoList(infoDtoList);
		}

		return recordDto;
	}

	@GetMapping("/getRecordList")
	@ControllerInfo("查询写实记录列表")
	public String getRecordList(String projectId, String acadyear, String semester, String gradeId, String classId, String status, String type, HttpServletRequest request){
		Json json = new Json();
		LoginInfo loginInfo = getLoginInfo();
		String unitId = loginInfo.getUnitId();

		//自定义字段
		String usingUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_RECORD);
		List<DiathesisStructure> structureList = diathesisStructureService.findListByProjectId(projectId);

		json.put("structureList", structureList);
		List<DiathesisRecordDto> recordDtoList = new ArrayList<DiathesisRecordDto>();

		List<DiathesisRecord> recordList = new ArrayList<DiathesisRecord>();
		String[] statu = null;
		if(StringUtils.isNotBlank(status)){
			statu = status.split(",");
		}
		if(User.OWNER_TYPE_STUDENT==loginInfo.getOwnerType()){
			//如果是学生
			json.put("userType", User.OWNER_TYPE_STUDENT);
			String studentId = loginInfo.getOwnerId();
			recordList = diathesisRecordService.findListByProjectId(unitId, projectId, acadyear, semester, classId,
					null, studentId, statu, null);
		}else{
			//非学生
			json.put("userType", User.OWNER_TYPE_TEACHER);
			List<String> roleClassList = getRoleClassList(projectId, type);

			//todo 判断导师
			Map<String, List<String>> roleMap = diathesisRoleService.findRoleByUserId(loginInfo.getUnitId(), loginInfo.getUserId());
			List<String> stuIds=new ArrayList<>();
			if(roleMap!=null && roleMap.containsKey(DiathesisConstant.ROLE_4)){
				stuIds = roleMap.get(DiathesisConstant.ROLE_4);
				if(CollectionUtils.isNotEmpty(stuIds)){
					List<Student> stuList = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[0])), Student.class);
					List<Clazz> classList = SUtils.dt(classRemoteService.findListByIds(EntityUtils.getSet(stuList, x -> x.getClassId()).toArray(new String[0])), Clazz.class);
					Map<String, List<String>> gradeMap = classList.stream().collect(Collectors.groupingBy(x -> x.getGradeId(), Collectors.mapping(x -> x.getId(), Collectors.toList())));
					for (Student student : stuList) {
						if(roleClassList.contains(student.getClassId()) ||
								(StringUtils.isNotBlank(gradeId) && (CollectionUtils.isEmpty(gradeMap.get(gradeId)) || !gradeMap.get(gradeId).contains(student.getClassId()))) ||
								(StringUtils.isNotBlank(classId) && !student.getClassId().equals(classId))
						){
							stuIds.remove(student.getId());
						}
					}
				}
			}


			if(CollectionUtils.isEmpty(roleClassList) && CollectionUtils.isEmpty(stuIds)){
				return json.toJSONString();
			}else{
				if(CollectionUtils.isNotEmpty(roleClassList)){
					if(StringUtils.isNotBlank(classId)){
						if(roleClassList.contains(classId)){
							recordList = diathesisRecordService.findListByProjectId(unitId, projectId, acadyear, semester, classId,
									null, null, statu, null);
						}
					}else if(StringUtils.isNotBlank(gradeId)){
						List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, gradeId),Clazz.class);
						if(CollectionUtils.isNotEmpty(clazzList)){
							List<String> clazzIdList = EntityUtils.getList(clazzList, Clazz::getId);
							roleClassList.retainAll(clazzIdList);
							if(CollectionUtils.isNotEmpty(roleClassList)){
								recordList = diathesisRecordService.findListByProjectId(unitId, projectId, acadyear, semester, null,
										roleClassList.toArray(new String[roleClassList.size()]), null, statu, null);
							}
						}
					}else{
						recordList = diathesisRecordService.findListByProjectId(unitId, projectId, acadyear, semester, null,
								roleClassList.toArray(new String[roleClassList.size()]), null, statu, null);
					}
				}
				if(CollectionUtils.isNotEmpty(stuIds)){
					List<DiathesisRecord> stuRecordList = diathesisRecordService.findListByProjectIdAndStuIdIn(unitId, projectId, acadyear, semester,statu, stuIds.toArray(new String[0]));
					if(CollectionUtils.isNotEmpty(stuRecordList)){
						recordList.addAll(stuRecordList);
					}
				}

			}
		}


		if(CollectionUtils.isEmpty(recordList))return json.toJSONString();

		Pagination page = createPagination(request);
		// 分页
		page.setMaxRowCount(recordList.size());
		int end = page.getPageIndex() * page.getPageSize();
		recordList = recordList.subList((page.getPageIndex() - 1) * page.getPageSize(), end > recordList.size() ? recordList.size() : end);
		page.initialize();
		if(CollectionUtils.isNotEmpty(recordList)){
			Set<String> recordIdSet = new HashSet<String>();
			Set<String> stuIdSet = new HashSet<String>();
			recordList.forEach(e->{
				recordIdSet.add(e.getId());
				stuIdSet.add(e.getStuId());
			});

			List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(stuIdSet.toArray(new String[0])),Student.class);
			List<Clazz> classList = SUtils.dt(classRemoteService.findListByIds(EntityUtils.getSet(studentList, Student::getClassId).toArray(new String[0])),Clazz.class);
			Map<String, Student> stuMap = EntityUtils.getMap(studentList, Student::getId);
			Map<String, String> classNameMap = EntityUtils.getMap(classList, Clazz::getId, Clazz::getClassNameDynamic);

			Map<String, String> recordInfoMap = new HashMap<String, String>();
			Map<String, String> optionMap = new HashMap<String, String>();
			if(CollectionUtils.isNotEmpty(structureList)){
				List<DiathesisRecordInfo> infoList = diathesisRecordInfoService.findListByUnitIdAndRecordIds(unitId, recordIdSet.toArray(new String[0]));
				infoList= infoList.stream().filter(e->StringUtils.isNotBlank(e.getContentTxt())).collect(Collectors.toList());
				recordInfoMap = EntityUtils.getMap(infoList, e->e.getRecordId()+e.getStructureId(), e->e.getContentTxt());
				//修改成了正在使用的单位设置
				optionMap = diathesisOptionService.findMapByUnitIdAndProjectId(usingUnitId, projectId);
			}

			DiathesisRecordDto recordDto = null;
			for (DiathesisRecord record : recordList) {
				recordDto = new DiathesisRecordDto();
				recordDto.setId(record.getId());
				recordDto.setStuId(record.getStuId());
				Student stu = stuMap.get(record.getStuId());
				if(stu!=null){
					recordDto.setStuName(stu.getStudentName());
					recordDto.setClassName(classNameMap.get(stu.getClassId())==null?"":classNameMap.get(stu.getClassId()));
				}
				recordDto.setCreationTime(DateUtils.date2StringByMinute(record.getCreationTime()));
				recordDto.setOperator(record.getOperator());
				recordDto.setStatus(record.getStatus());
				if(!DiathesisConstant.AUDIT_STATUS_READY.equals(record.getStatus())){
					recordDto.setAuditOpinion(record.getAuditOpinion());
					recordDto.setAuditor(record.getAuditor());
					recordDto.setAuditTime(DateUtils.date2StringByMinute(record.getAuditTime()));
				}

				if(CollectionUtils.isNotEmpty(structureList)){
					for (DiathesisStructure structure : structureList) {
						String key = record.getId()+structure.getId();
						if(recordInfoMap.containsKey(key)){
							List<Object> list = new ArrayList<>();
							if(DiathesisConstant.DATA_TYPE_2.equals(structure.getDataType()) || DiathesisConstant.DATA_TYPE_3.equals(structure.getDataType())){
								if(StringUtils.isNotBlank(recordInfoMap.get(key))){
									String[] split = recordInfoMap.get(key).split(",");
									String contentTxt = "";
									for (String sp : split) {
										if(optionMap.containsKey(sp)) {
											contentTxt += ","+optionMap.get(sp);
										}
									}
									if(StringUtils.isNotBlank(contentTxt)) {
										contentTxt=contentTxt.substring(1);
									}
									list.add(contentTxt);
									list.add(structure.getDataType());
									recordDto.getRecordInfoMap().put(structure.getId(), list);
								}
							}else if(DiathesisConstant.DATA_TYPE_4.equals(structure.getDataType())){
								String contxt = recordInfoMap.get(key);
								list.add("");
								list.add(structure.getDataType());
								ArrayList<FileDto> fileDtos = new ArrayList<>();
								if(StringUtils.isNotBlank(contxt)){
									String[] allFile = contxt.split(DiathesisConstant.FILE_SPLIT);
									for (String one : allFile) {
										FileDto dto = new FileDto();
										dto.setFileName(StringUtils.substringBefore(one, ","));
										dto.setFilePath(StringUtils.substringAfter(one, ","));
										fileDtos.add(dto);
									}
									list.add(fileDtos);
								}
								recordDto.getRecordInfoMap().put(structure.getId(), list);
							}else{
								list.add(recordInfoMap.get(key));
								list.add(structure.getDataType());
								recordDto.getRecordInfoMap().put(structure.getId(), list);
							}
						}
					}
				}
				recordDtoList.add(recordDto);
			}
		}
		json.put("recordList", recordDtoList);
		json.put("page", page);
		return json.toString();
	}

	/**
	 *  加上权限控制
	 * @param recordDto
	 * @return
	 */
	@PostMapping("/saveRecord")
	@ControllerInfo("保存写实记录")
	public String saveRecord(@RequestBody DiathesisRecordDto recordDto){
		//学生端 权限校验
		//Map<String, List<String>> role = diathesisRoleService.findRoleByUserId(getLoginInfo().getUnitId(), getLoginInfo().getUserId());
		DiathesisProjectEx ex = diathesisProjectExService.findByUnitIdAndProjectId(getLoginInfo().getUnitId(), recordDto.getProjectId());
		String inputTypes = ex.getInputTypes();
		if(StringUtils.isBlank(inputTypes)){
			return error("请先设置这个写实记录的录入人!");
		}
		if(User.OWNER_TYPE_STUDENT==getLoginInfo().getOwnerType() && !inputTypes.contains(DiathesisConstant.ROLE_STUDENT)){
			return error("该写实记录不能由学生录入");
		}

//		Map<String, List<String>> role = diathesisRoleService.findRoleByUserId(getLoginInfo().getUnitId(), getLoginInfo().getUserId());
//		List<String> roleList = Arrays.stream(inputTypes.split(",")).collect(Collectors.toList());


		if(StringUtils.isBlank(recordDto.getStuId())){
			return error("请选择记录对象！");
		}
		DiathesisProject project = diathesisProjectService.findOne(recordDto.getProjectId());
		if(project==null){
			return error("项目不存在");
		}


		List<DiathesisRecordInfoDto> infoDtoList = recordDto.getRecordInfoList();
		if(CollectionUtils.isEmpty(infoDtoList)){
			return error("记录内容不能为空");
		}

		try{
			LoginInfo loginInfo = getLoginInfo();
			String recordId = recordDto.getId();
			//List<DiathesisStructure> structureList = diathesisStructureService.findListByProjectId(loginInfo.getUnitId(), recordDto.getProjectId());
			List<DiathesisStructure> structureList = diathesisStructureService.findListByProjectId(recordDto.getProjectId());
			Map<String, DiathesisStructure> structureMap = EntityUtils.getMap(structureList, DiathesisStructure::getId);

			List<DiathesisRecord> saveRecordList = new ArrayList<DiathesisRecord>();
			List<DiathesisRecordInfo> saveInfoList = new ArrayList<DiathesisRecordInfo>();
			DiathesisRecord record = null;
			DiathesisRecordInfo recordInfo = null;
			if(StringUtils.isBlank(recordId)){//新增
				String[] stuIds = recordDto.getStuId().split(",");
				Map<String, String> stuCodeMap = new HashMap<String, String>();
				List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(stuIds),Student.class);
				Map<String, String> studentCodeMap = EntityUtils.getMap(studentList, Student::getId, Student::getStudentCode);
				List<Clazz> clazzList = SUtils.dt(classRemoteService.findListByIds(EntityUtils.getSet(studentList, Student::getClassId).toArray(new String[0])),Clazz.class);
				Map<String, List<String>> classStuMap = EntityUtils.getListMap(studentList, Student::getClassId, Student::getId);
				List<Grade> gradeList = SUtils.dt(gradeRemoteService.findListByIds(EntityUtils.getSet(clazzList, Clazz::getGradeId).toArray(new String[0])),Grade.class);
				Map<String, List<String>> gradeClaMap = EntityUtils.getListMap(clazzList, Clazz::getGradeId, Clazz::getId);
				for (Grade grade : gradeList) {
					String[] openAcadyear = grade.getOpenAcadyear().split("-");
					String[] inputAcadyear = recordDto.getAcadyear().split("-");
					String gradeCode = grade.getSection().toString();
					gradeCode += (Integer.parseInt(inputAcadyear[0])-Integer.parseInt(openAcadyear[0])+1);
					//gradeCode += recordDto.getSemester();
					for (String claId : gradeClaMap.get(grade.getId())) {
						for (String stuId : classStuMap.get(claId)) {
							stuCodeMap.put(stuId, gradeCode);
						}
					}
				}
				for (String stuId : stuIds) {
					record = new DiathesisRecord();
					record.setId(UuidUtils.generateUuid());
					record.setUnitId(loginInfo.getUnitId());
					record.setProjectId(recordDto.getProjectId());
					record.setStuId(stuId);
					record.setStatus(DiathesisConstant.AUDIT_STATUS_READY);
					record.setCreationTime(new Date());
					record.setOperator(loginInfo.getRealName());
					record.setAcadyear(recordDto.getAcadyear());
					record.setSemester(recordDto.getSemester());
					record.setGradeCode(stuCodeMap.get(stuId));
					for (DiathesisRecordInfoDto infoDto : infoDtoList) {
						if(!structureMap.containsKey(infoDto.getStructureId())){
							DiathesisStructure structure = diathesisStructureService.findOne(infoDto.getStructureId());
							if(structure==null){
								return error("字段不存在");
							}
							return error(project.getProjectName()+"中不包含字段【"+structure.getTitle()+"】");
						}
						DiathesisStructure structure = structureMap.get(infoDto.getStructureId());
						recordInfo = new DiathesisRecordInfo();
						recordInfo.setId(UuidUtils.generateUuid());
						recordInfo.setUnitId(loginInfo.getUnitId());
						recordInfo.setRecordId(record.getId());
						recordInfo.setStructureId(infoDto.getStructureId());
						if((structure.getIsMust()==null || structure.getIsMust()==Constant.IS_TRUE)){
							if(DiathesisConstant.DATA_TYPE_4.equals(structure.getDataType())){
								if(CollectionUtils.isEmpty(infoDto.getFileList())){
									return error(structure.getTitle()+"不能为空");
								}
							}else{
								if (StringUtils.isBlank(infoDto.getContentTxt())){
									return error(structure.getTitle()+"不能为空");
								}
							}
						}
						if(infoDto.getContentTxt().length()>1000){
							return error("写实字段内容不能超过1000字");
						}
						recordInfo.setFileList(infoDto.getFileList());
						recordInfo.setContentTxt(infoDto.getContentTxt());
						recordInfo.setDataType(structure.getDataType());
						recordInfo.setStuCode(studentCodeMap.get(stuId));
						saveInfoList.add(recordInfo);
					}
					saveRecordList.add(record);
				}
			}else{
				//修改
				record = diathesisRecordService.findOne(recordId);
				if(DiathesisConstant.AUDIT_STATUS_PASS.equals(record.getStatus())){
					return error("该记录已经审核通过，不允许修改！");
				}
				Student student = SUtils.dc(studentRemoteService.findOneById(record.getStuId()),Student.class);
				Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()),Clazz.class);
				Grade grade = SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()),Grade.class);
				String[] openAcadyear = grade.getOpenAcadyear().split("-");
				String[] inputAcadyear = recordDto.getAcadyear().split("-");
				String gradeCode = grade.getSection().toString();
				gradeCode += (Integer.parseInt(inputAcadyear[0])-Integer.parseInt(openAcadyear[0])+1);
				//gradeCode += recordDto.getSemester();
				record.setGradeCode(gradeCode);
				record.setAcadyear(recordDto.getAcadyear());
				record.setSemester(recordDto.getSemester());
				record.setOperator(loginInfo.getRealName());
				if(CollectionUtils.isNotEmpty(infoDtoList)){
					for (DiathesisRecordInfoDto infoDto : infoDtoList) {
						if(!structureMap.containsKey(infoDto.getStructureId())){
							DiathesisStructure structure = diathesisStructureService.findOne(infoDto.getStructureId());
							if(structure==null){
								return error("字段不存在");
							}
							return error(project.getProjectName()+"中不包含字段【"+structure.getTitle()+"】");
						}
						DiathesisStructure structure = structureMap.get(infoDto.getStructureId());
						recordInfo = new DiathesisRecordInfo();
						recordInfo.setId(UuidUtils.generateUuid());
						recordInfo.setUnitId(loginInfo.getUnitId());
						recordInfo.setRecordId(record.getId());
						recordInfo.setStructureId(infoDto.getStructureId());
						if((structure.getIsMust()==null || structure.getIsMust()==Constant.IS_TRUE) && StringUtils.isBlank(infoDto.getContentTxt())){
							return error(structure.getTitle()+"不能为空");
						}

						recordInfo.setFileList(infoDto.getFileList());
						recordInfo.setFileList(infoDto.getFileList());
						recordInfo.setContentTxt(infoDto.getContentTxt());
						recordInfo.setResultTxt(infoDto.getResultTxt());
						recordInfo.setDataType(structure.getDataType());
						recordInfo.setStuCode(student.getStudentCode());
						saveInfoList.add(recordInfo);
					}
				}
				saveRecordList.add(record);
			}

			diathesisRecordService.deleteAndSave(loginInfo.getUnitId(), recordId, saveRecordList,saveInfoList);
		} catch (Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}

	@GetMapping("/updateRecordStatus")
	@ControllerInfo("审核写实记录")
	public String updateRecordStatus(String ids, String status, String auditOpinion){
		String auditor = getLoginInfo().getRealName();
		if(StringUtils.isBlank(ids)){
			return error("请选择至少一条记录！");
		}
		try {
			List<DiathesisRecord> recordList = diathesisRecordService.findListByIds(ids.split(","));
			for (DiathesisRecord record : recordList) {
				record.setStatus(status);
				record.setAuditor(auditor);
				record.setAuditTime(new Date());
				if(StringUtils.isNotBlank(auditOpinion)){
					record.setAuditOpinion(auditOpinion);
				}
			}
			diathesisRecordService.saveAll(recordList.toArray(new DiathesisRecord[recordList.size()]));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnSuccess();
	}

	@GetMapping("/deleteRecord")
	@ControllerInfo("删除写实记录")
	public String deleteRecord(String ids){
		if(StringUtils.isBlank(ids)){
			return error("请选择至少一条记录！");
		}
		try {
			diathesisRecordService.deleteRecord(getLoginInfo().getUnitId(),ids.split(","));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnSuccess();
	}

	@GetMapping(value="/getAcadSemList")
	@ControllerInfo("获取学年学期")
	public String getAcadSemList(){
		String unitId = getLoginInfo().getUnitId();
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1, getLoginInfo().getUnitId()), Semester.class);
		String acadyear = semester.getAcadyear();
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), String.class).stream().filter(x->x.compareTo(acadyear)<=0).collect(Collectors.toList());
		List<McodeDetail> mcodeList = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-XQ"), McodeDetail.class);
		List<String> unitIds =new ArrayList<>();

		if(getLoginInfo().getUnitClass().equals(Unit.UNIT_CLASS_EDU)){
			List<Unit> unitList = diathesisUnitService.findAllChildUnit(unitId);
			if(CollectionUtils.isNotEmpty(unitList)){
				unitIds=unitList.stream().filter(x->Unit.UNIT_CLASS_SCHOOL==x.getUnitClass()).map(x->x.getId()).collect(Collectors.toList());
			}
		}else{
			unitIds.add(unitId);
		}
		List<DiathesisGradeDto> gradeList =new ArrayList<>();
		if(CollectionUtils.isNotEmpty(unitIds)){
			Map<String, List<Grade>> map  =new HashMap<>();
			for (int i = 0; i <unitIds.size() ; i+=900) {
				String[] tempUnitIds=unitIds.stream().skip(i).limit(900).toArray(String[]::new);
				map.putAll(SUtils.dt(gradeRemoteService.findBySchoolIdMap(tempUnitIds),new TR<Map<String, List<Grade>>>(){}));
			}
			List<Grade> gradeEntityList = new ArrayList<>();
			for (String key:map.keySet()){
				gradeEntityList.addAll(map.get(key));
			}

			List<Integer> sections = Arrays.asList(BaseConstants.SECTION_PRIMARY, BaseConstants.SECTION_JUNIOR, BaseConstants.SECTION_HIGH_SCHOOL);
			Arrays.asList(BaseConstants.SECTION_PRIMARY, BaseConstants.SECTION_JUNIOR, BaseConstants.SECTION_HIGH_SCHOOL);
			gradeList = gradeEntityList.stream().filter(x->{
//				Integer section = x.getSection();
//				if(sections.contains(section+""))return true;
				String gradeCode = x.getGradeCode();
				if(StringUtils.isNotBlank(gradeCode) && sections.contains(Integer.parseInt(gradeCode.substring(0,1))))return true;
				return false;
			}).map(x -> {
				DiathesisGradeDto dto = new DiathesisGradeDto();
				dto.setGradeCode(x.getGradeCode());
				dto.setGradeName(x.getGradeName());
				return dto;
			}).sorted((x, y) -> x.getGradeCode().compareTo(y.getGradeCode())).distinct().collect(Collectors.toList());
		}

		Json json = new Json();
		json.put("acadyearList", acadyearList);
		json.put("semesterList", mcodeList);
		json.put("gradeList", gradeList);


		if(semester!=null){
			json.put("acadyear", semester.getAcadyear());
			json.put("semester", semester.getSemester());
		}
		return json.toString();
	}

	@GetMapping(value="/getClassList")
	@ControllerInfo("获取班级")
	public String getClassList(String gradeId){
		JsonArray jsonArray = new JsonArray();
		String unitId = getLoginInfo().getUnitId();
		List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, gradeId),Clazz.class);
		if(CollectionUtils.isNotEmpty(clazzList)){
			for (Clazz clazz : clazzList) {
				Json json = new Json();
				json.put("classId", clazz.getId());
				json.put("className", clazz.getClassNameDynamic());
				jsonArray.add(json);
			}
		}
		return jsonArray.toJSONString();
	}

	@GetMapping(value="/getGradeClassList")
	@ControllerInfo("获取有权限的年级班级列表")
	public String getGradeClassList(String projectId, String type){
		JsonArray jsonArray = new JsonArray();
		LoginInfo loginInfo = getLoginInfo();
		String unitId = loginInfo.getUnitId();
		List<String> roles = getProjectExRole(projectId, type, unitId);
		if(CollectionUtils.isEmpty(roles))return jsonArray.toJSONString();
		Map<String, List<String>> roleMap = diathesisRoleService.findRoleByUserId(unitId, loginInfo.getUserId());//用户拥有的权限
		if(MapUtils.isEmpty(roleMap)){
			return jsonArray.toJSONString();
		}
		//所有年级
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId, new Integer[]{BaseConstants.SECTION_PRIMARY,BaseConstants.SECTION_JUNIOR,BaseConstants.SECTION_HIGH_SCHOOL}), Grade.class);
		if(CollectionUtils.isEmpty(gradeList)){
			return jsonArray.toJSONString();
		}
		//所有班级
		List<Clazz> clazzList = SUtils.dt(classRemoteService.findByInGradeIds(
				EntityUtils.getList(gradeList, Grade::getId).toArray(new String[gradeList.size()])), Clazz.class);

		Map<String, List<String>> classMap = EntityUtils.getListMap(clazzList, x -> x.getGradeId(), x -> x.getId());
		Map<String, Clazz> classEntityMap = EntityUtils.getMap(clazzList, x -> x.getId(), x -> x);
		Map<String, List<Clazz>> gradeListMap=new HashMap<>();
		if(roleMap.containsKey(DiathesisConstant.ROLE_1)){
			//管理员 返回全部
			List<String> roleList = roleMap.get(DiathesisConstant.ROLE_1);
			if(CollectionUtils.intersection(roleList, roles).size()>0){
				gradeListMap = EntityUtils.getListMap(clazzList, Clazz::getGradeId, Function.identity());
				for (Grade grade : gradeList) {
					Json json1 = new Json();
					json1.put("gradeId", grade.getId());
					json1.put("gradeName", grade.getGradeName());
					JsonArray jsonArray2 = new JsonArray();
					if(gradeListMap.containsKey(grade.getId())){
						for (Clazz clazz : gradeListMap.get(grade.getId())) {
							Json json2 = new Json();
							json2.put("classId", clazz.getId());
							json2.put("className", clazz.getClassNameDynamic());
							jsonArray2.add(json2);
						}
					}
					json1.put("classList", jsonArray2);
					jsonArray.add(json1);
				}
				return jsonArray.toJSONString();
			}
		}
		Set<String> gradeIdSet=new TreeSet<>();
		Set<String> classIdSet=new TreeSet<>();

		if(roles.contains(DiathesisConstant.ROLE_GRADE)&&roleMap.containsKey(DiathesisConstant.ROLE_2)){
			//年级主任
			gradeIdSet.addAll(roleMap.get(DiathesisConstant.ROLE_2));
		}
		if(roles.contains(DiathesisConstant.ROLE_CLASS)&&roleMap.containsKey(DiathesisConstant.ROLE_3)){
			//班主任
			classIdSet.addAll(roleMap.get(DiathesisConstant.ROLE_3));
		}
		if(roles.contains(DiathesisConstant.ROLE_TUTOR)&&roleMap.containsKey(DiathesisConstant.ROLE_4)){
			//导师
			List<Student> stuList=SUtils.dt(studentRemoteService.findListByIds(roleMap.get(DiathesisConstant.ROLE_4).toArray(new String[0])),Student.class);
			classIdSet.addAll(EntityUtils.getSet(stuList,x->x.getClassId()));
		}
		if(CollectionUtils.isNotEmpty(gradeIdSet)){
			for (String gradeId : gradeIdSet) {
				if (CollectionUtils.isNotEmpty(classMap.get(gradeId))){
					classIdSet.addAll(classMap.get(gradeId));
				}
			}
			gradeIdSet=clazzList.stream().map(x->x.getGradeId()).collect(Collectors.toSet());
		}
		for (String classId : classIdSet) {
			if(classEntityMap.get(classId)!=null){
				gradeIdSet.add(classEntityMap.get(classId).getGradeId());
			}
		}

		for (Grade grade : gradeList) {
			if (!gradeIdSet.contains(grade.getId()))continue;
			Json json1 = new Json();
			json1.put("gradeId", grade.getId());
			json1.put("gradeName", grade.getGradeName());
			JsonArray jsonArray2 = new JsonArray();
			boolean flag=false;
			for (Clazz clazz : clazzList) {
				if (classIdSet.contains(clazz.getId()) && clazz.getGradeId().equals(grade.getId())){
					flag=true;
					Json json2 = new Json();
					json2.put("classId", clazz.getId());
					json2.put("className", clazz.getClassNameDynamic());
					jsonArray2.add(json2);
				}
			}
			if(flag){
				json1.put("classList", jsonArray2);
				jsonArray.add(json1);
			}
		}
		return jsonArray.toJSONString();
	}

	private List<String> getProjectExRole(String projectId, String type, String unitId) {
		//todo v1.2.0 把审核人录入人从 原来的项目表中分离出来
		DiathesisProjectEx projectEx = diathesisProjectExService.findByUnitIdAndProjectId(unitId, projectId);
		//DiathesisProject project = diathesisProjectService.findOne(projectId);
		String[] role=null;
		if("2".equals(type)){//审核权限
			if(StringUtils.isNotBlank(projectEx.getAuditorTypes())){
				role = projectEx.getAuditorTypes().split(",");
			}
		}else{//录入权限
			if(StringUtils.isNotBlank(projectEx.getInputTypes())){
				role = projectEx.getInputTypes().split(",");
			}
		}
		return role==null?new ArrayList<>():Arrays.stream(role).collect(Collectors.toList());
	}

	@GetMapping(value="/getStudentList")
	@ControllerInfo("获取学生列表")
	public String getRoleStudentList(String classId,String projectId){
		JsonArray jsonArray = new JsonArray();
		String unitId = getLoginInfo().getUnitId();
		List<Student> studentList ;
		if(StringUtils.isNotBlank(projectId)){
			Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
			if(clazz==null)return error("不存在这个班级");
			Map<String, List<String>> roleMap = diathesisRoleService.findRoleByUserId(unitId, getLoginInfo().getUserId());
			List<String> roles = getProjectExRole(projectId, "1", unitId);
			//只是导师的话就返回部分学生
			if((roleMap.containsKey(DiathesisConstant.ROLE_1) && CollectionUtils.intersection(roleMap.get(DiathesisConstant.ROLE_1), roles).size()>0 ) ||  //管理员
					(roles.contains(DiathesisConstant.ROLE_GRADE)&&roleMap.containsKey(DiathesisConstant.ROLE_2) && roleMap.get(DiathesisConstant.ROLE_2).contains(clazz.getGradeId()))  ||  //年级主任
					(roles.contains(DiathesisConstant.ROLE_CLASS)&&roleMap.containsKey(DiathesisConstant.ROLE_3) && roleMap.get(DiathesisConstant.ROLE_3).contains(classId))  //班主任
			){
				//管理员, 班主任 ,年级主任 直接取所有学生
				studentList=SUtils.dt(studentRemoteService.findByClassIds(classId),Student.class);
			}else{
				//如果只是导师,就取这个班级下的 所带的学生
				studentList=SUtils.dt(studentRemoteService.findListByIds(roleMap.get(DiathesisConstant.ROLE_4).toArray(new String[0])),Student.class);
				studentList=EntityUtils.filter2(studentList,x->x.getClassId().equals(classId));
			}
		}else{
			studentList=SUtils.dt(studentRemoteService.findByClassIds(classId),Student.class);
		}


		if(CollectionUtils.isNotEmpty(studentList)){
			for (Student student : studentList) {
				Json json = new Json();
				json.put("studentId", student.getId());
				json.put("studentName", student.getStudentName());
				jsonArray.add(json);
			}
		}
		return jsonArray.toJSONString();
	}

	private List<String> getRoleClassList(String projectId, String type){
		List<String> classIds = new ArrayList<String>();
		//DiathesisProject project = diathesisProjectService.findOne(projectId);
		DiathesisProjectEx projectEx = diathesisProjectExService.findByUnitIdAndProjectId(getLoginInfo().getUnitId(), projectId);

		if(projectEx==null){
			return classIds;
		}
		String[] role;
		if("2".equals(type)){//审核权限
			if(StringUtils.isBlank(projectEx.getAuditorTypes())){
				return classIds;
			}
			role = projectEx.getAuditorTypes().split(",");
		}else{//录入权限
			if(StringUtils.isBlank(projectEx.getInputTypes())){
				return classIds;
			}
			role = projectEx.getInputTypes().split(",");
		}
		List<String> roles = Arrays.stream(role).collect(Collectors.toList());//写实记录设置权限
		LoginInfo loginInfo = getLoginInfo();
		String unitId = loginInfo.getUnitId();
		Map<String, List<String>> roleMap = diathesisRoleService.findRoleByUserId(unitId, loginInfo.getUserId());//用户拥有的权限
		if(MapUtils.isEmpty(roleMap)){
			return classIds;
		}
		//所有年级
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId, new Integer[]{BaseConstants.SECTION_PRIMARY,BaseConstants.SECTION_JUNIOR,BaseConstants.SECTION_HIGH_SCHOOL}), Grade.class);//所有年级
		if(CollectionUtils.isEmpty(gradeList)){
			return classIds;
		}
		//所有班级
		List<Clazz> clazzList = SUtils.dt(classRemoteService.findByInGradeIds(
				EntityUtils.getList(gradeList, Grade::getId).toArray(new String[gradeList.size()])), Clazz.class);

		//若是管理员
		if(roleMap.containsKey(DiathesisConstant.ROLE_1)){
			List<String> roleList = roleMap.get(DiathesisConstant.ROLE_1);
			if(CollectionUtils.intersection(roleList, roles).size()>0){
				classIds = EntityUtils.getList(clazzList, Clazz::getId);
				return classIds;
			}
		}

		//如果有自定义权限
//		ArrayList<String> tempRoleList = new ArrayList<>(roles);
//		tempRoleList.removeAll(DiathesisConstant.ROLE_CODE_LIST);
//		if(CollectionUtils.isNotEmpty(tempRoleList)){
//			List<CustomRole> sysList = SUtils.dt(customRoleRemoteService.findListByUnitAndSubsystem(unitId, "78,"),CustomRole.class);
//			Map<String, String> map = sysList.stream().filter(x ->tempRoleList.contains(x.getRoleCode())).collect(Collectors.toMap(x -> x.getId(), x -> x.getRoleCode()));
//			if(CollectionUtils.isNotEmpty(map.keySet())){
//				List<CustomRoleUser> users = SUtils.dt(customRoleUserRemoteService.findUserIdsByCustomRoleIdIn(map.keySet().toArray(new String[0])), CustomRoleUser.class);
//				if(CollectionUtils.isNotEmpty(users)){
//					List<String> list = EntityUtils.getList(users, x -> x.getUserId());
//					if(CollectionUtils.isNotEmpty(list) && list.contains(getLoginInfo().getUserId())){
//						classIds = EntityUtils.getList(clazzList, Clazz::getId);
//						return classIds;
//					}
//				}
//
//			}
//		}

		//若是年级主任
		if(roles.contains(DiathesisConstant.ROLE_GRADE)&&roleMap.containsKey(DiathesisConstant.ROLE_2)){
			List<String> gradeIds = roleMap.get(DiathesisConstant.ROLE_2);
			List<Clazz> otherClazzList = new ArrayList<Clazz>();
			if(roles.contains(DiathesisConstant.ROLE_CLASS)&&roleMap.containsKey(DiathesisConstant.ROLE_3)){
				List<String> clazzIds = roleMap.get(DiathesisConstant.ROLE_3);
				otherClazzList = clazzList.stream().filter(e->clazzIds.contains(e.getId())).collect(Collectors.toList());
			}

			clazzList = clazzList.stream().filter(e->gradeIds.contains(e.getGradeId())).collect(Collectors.toList());
			clazzList.addAll(otherClazzList);
			classIds = EntityUtils.getList(clazzList, Clazz::getId);
			return classIds;

		}
		if(roles.contains(DiathesisConstant.ROLE_CLASS)&&roleMap.containsKey(DiathesisConstant.ROLE_3)){
			//若是班主任
			List<String> clazzIds = roleMap.get(DiathesisConstant.ROLE_3);
			clazzList = clazzList.stream().filter(e->clazzIds.contains(e.getId())).collect(Collectors.toList());
			classIds = EntityUtils.getList(clazzList, Clazz::getId);
			return classIds;
		}
		return classIds.stream().distinct().collect(Collectors.toList());
	}

	/**
	 * 模糊查询
	 * @param projectId
	 * @param type
	 * @param studentName
	 * @return
	 */
	@GetMapping(value="/getStudent")
	@ControllerInfo("获取学生列表")
	public String getRoleStudent(String projectId, String type, String studentName){
		String unitId = getLoginInfo().getUnitId();
		//所有年级
		String gradeKey="diathesis_all_grade"+unitId+"78,";
		String gradeInfo = RedisUtils.get(gradeKey);
		if (StringUtils.isBlank(gradeInfo)){
			gradeInfo=gradeRemoteService.findBySchoolId(unitId, new Integer[]{BaseConstants.SECTION_PRIMARY,BaseConstants.SECTION_JUNIOR,BaseConstants.SECTION_HIGH_SCHOOL});
			RedisUtils.set(gradeKey,gradeInfo,RedisUtils.TIME_HALF_MINUTE);
		}
		List<Grade> gradeList = SUtils.dt(gradeInfo,Grade.class);
		//所有学生
		String studentKey="diathesis_all_student"+unitId+"78,";
		String stuInfo = RedisUtils.get(studentKey);
		if (StringUtils.isBlank(stuInfo)){
			stuInfo=studentRemoteService.findByGradeIds(EntityUtils.getList(gradeList, Grade::getId).toArray(new String[gradeList.size()]));
			RedisUtils.set(studentKey,stuInfo,RedisUtils.TIME_HALF_MINUTE);
		}
		List<Student> studentList = SUtils.dt(stuInfo,Student.class);
		//所有班级
		String classKey="diathesis_all_class"+unitId+"78,";
		String classInfo = RedisUtils.get(classKey);
		if (StringUtils.isBlank(classInfo)){
			classInfo=classRemoteService.findListByIds(EntityUtils.getSet(studentList, Student::getClassId).toArray(new String[0]));
			RedisUtils.set(classKey,classInfo,RedisUtils.TIME_HALF_MINUTE);
		}
		List<Clazz> clazzList = SUtils.dt(classInfo,Clazz.class);

		Map<String, List<String>> classStuMap = EntityUtils.getListMap(studentList, Student::getClassId, Student::getId);
		Map<String, List<String>> gradeClaMap = EntityUtils.getListMap(clazzList, Clazz::getGradeId, Clazz::getId);
		Map<String, String> classNameMap = EntityUtils.getMap(clazzList, x -> x.getId(), x -> x.getClassNameDynamic());
		Map<String, Student> stuMap = EntityUtils.getMap(studentList, x -> x.getId(), x -> x);

		Map<String, List<String>> roleMap = diathesisRoleService.findRoleByUserId(unitId, getLoginInfo().getUserId());
		List<String> roles = getProjectExRole(projectId,type, unitId);
		Set<String> myRoleStuIdSet=new HashSet<>();
		if (roleMap!=null){
			if(roles.contains(DiathesisConstant.ROLE_GRADE)&&roleMap.containsKey(DiathesisConstant.ROLE_2)){
				//年级主任
				List<String> gradeIds = roleMap.get(DiathesisConstant.ROLE_2);
				for (String gradeId : gradeIds) {
					Set<String> stuIds = gradeClaMap.get(gradeId).stream().flatMap(x -> classStuMap.get(x).stream()).collect(Collectors.toSet());
					myRoleStuIdSet.addAll(stuIds);
				}
			}
			if(roles.contains(DiathesisConstant.ROLE_CLASS)&&roleMap.containsKey(DiathesisConstant.ROLE_3)){
				//班主任
				List<String> classIds = roleMap.get(DiathesisConstant.ROLE_3);
				Set<String> stuIds = classIds.stream().flatMap(x -> classStuMap.get(x).stream()).collect(Collectors.toSet());
				myRoleStuIdSet.addAll(stuIds);
			}
			if(roles.contains(DiathesisConstant.ROLE_TUTOR)&&roleMap.containsKey(DiathesisConstant.ROLE_4)){
				//导师
				List<String> stuIds = roleMap.get(DiathesisConstant.ROLE_4);
				myRoleStuIdSet.addAll(stuIds);
			}
		}
		JSONArray jsonArray = new JSONArray();
		if(CollectionUtils.isNotEmpty(myRoleStuIdSet)){
			for (String stuId : myRoleStuIdSet) {
				Json json = new Json();
				Student student = stuMap.get(stuId);
				if(student.getStudentName().contains(studentName)){
					json.put("studentId", student.getId());
					json.put("className", classNameMap.get(student.getClassId()));
					json.put("studentName", student.getStudentName());
					jsonArray.add(json);
				}
			}
		}
		return jsonArray.toJSONString();
	}




}
