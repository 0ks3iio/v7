package net.zdsoft.stuwork.data.service.impl;

import com.google.common.collect.Lists;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dao.DyStudentRewardPointDao;
import net.zdsoft.stuwork.data.dto.DyStudentRewardPointDto;
import net.zdsoft.stuwork.data.dto.DyStuworkDataCountDto;
import net.zdsoft.stuwork.data.entity.DyStudentRewardPoint;
import net.zdsoft.stuwork.data.entity.DyStudentRewardProject;
import net.zdsoft.stuwork.data.entity.DyStudentRewardSetting;
import net.zdsoft.stuwork.data.service.DyStudentRewardPointService;
import net.zdsoft.stuwork.data.service.DyStudentRewardProjectService;
import net.zdsoft.stuwork.data.service.DyStudentRewardSettingService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaBuilder.In;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service("dyStudentRewardPointService")
public class DyStudentRewardPointServiceImpl extends BaseServiceImpl<DyStudentRewardPoint, String> implements DyStudentRewardPointService{
	
	@Autowired
	private DyStudentRewardPointDao dyStudentRewardPointDao;
	
	@Autowired
	private StudentRemoteService studentRemoteService;
	
	@Autowired
	private ClassRemoteService classRemoteService; 
	
	@Autowired
	private DyStudentRewardProjectService dyStudentRewardProjectService;
	@Autowired
	private DyStudentRewardSettingService dyStudentRewardSettingService;
	
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	
	
	@Override
	protected BaseJpaRepositoryDao<DyStudentRewardPoint, String> getJpaDao() {
		return dyStudentRewardPointDao;
	}

	@Override
	protected Class<DyStudentRewardPoint> getEntityClass() {
		return DyStudentRewardPoint.class;
	}

	@Override
	public List<DyStudentRewardPoint> getStudentRewardPointByStudentId(
			final String schoolId,final String[] studentIds, final String acadyear, final String semester,Pagination page,boolean flag) {
		List<DyStudentRewardPoint> studentRewardPointList =  new ArrayList<DyStudentRewardPoint>();

			Specification<DyStudentRewardPoint> specification = new Specification<DyStudentRewardPoint>() {
				@Override
				public Predicate toPredicate(Root<DyStudentRewardPoint> root,
						CriteriaQuery<?> cq, CriteriaBuilder cb) {
					List<Predicate> ps = Lists.newArrayList();
                    List<Predicate> listOr = new ArrayList<Predicate>();
					ps.add(cb.equal(root.get("unitId").as(String.class), schoolId));
	                if(StringUtils.isNotBlank(acadyear)){
	                	ps.add(cb.equal(root.get("acadyear").as(String.class), acadyear));
	                }
	                if(StringUtils.isNotBlank(semester)){
	                	ps.add(cb.equal(root.get("semester").as(String.class), semester));
	                }
	                ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                    Predicate [] predicateOr = null;
	                if(null!=studentIds && studentIds.length>0){
//						if (studentIds.length <= 800) {
                            In<String> in = cb.in(root.get("studentId").as(String.class));
                            for (int i = 0; i < studentIds.length; i++) {
								in.value(studentIds[i]);
							}
							ps.add(in);
//						} else {
//							int per = 800;
//							int rou = studentIds.length % per==0?(studentIds.length/per):(studentIds.length/per+1);
//							for (int i = 0;i<rou;i++) {
//                                In<String> in = cb.in(root.get("studentId").as(String.class));
//							    int st = i*per;
//							    int et = (i+1)*per;
//							    if(et > studentIds.length){
//							        et = studentIds.length;
//                                }
//							    for(int j=st;j<et;j++){
//                                    in.value(studentIds[j]);
//                                }
//                                listOr.add(in);
//							}
//						}
					}
	                List<Order> orderList = new ArrayList<Order>();
	                orderList.add(cb.desc(root.<Date>get("creationTime")));
                    if (listOr.size() == 0) {
                        cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                    } else {
                        cq.where(cb.and(ps.toArray(new Predicate[0])), cb.or(listOr.toArray(new Predicate[0]))).orderBy(orderList);
                    }
                    return cq.getRestriction();
	            }			
			};
			if (page != null) {
	            Pageable pageable = Pagination.toPageable(page);
	            Page<DyStudentRewardPoint> findAll = dyStudentRewardPointDao.findAll(specification, pageable);
	            page.setMaxRowCount((int) findAll.getTotalElements());
	            studentRewardPointList = findAll.getContent();
	        }else{
	        	studentRewardPointList = dyStudentRewardPointDao.findAll(specification);
	        }
			
			if(flag && studentRewardPointList.size()>0){
				studentRewardPointList = dealPointList(studentRewardPointList, null);
			}
		return studentRewardPointList;
	}

	@Override
	public List<DyStudentRewardPoint> getStudentRewardPointByStucode(
			String schoolId,String studentCode, String acadyear, String semester,Pagination page) {
		List<DyStudentRewardPoint> studentRewardPointList =  new ArrayList<DyStudentRewardPoint>();
		Student student = SUtils.dc(studentRemoteService.findBySchIdStudentCode(schoolId, studentCode), Student.class);
		if(student!=null){
			studentRewardPointList = getStudentRewardPointByStudentId(schoolId, new String[]{student.getId()}, acadyear, semester, page,true);
		}else{
			if(page!=null){
				page.setMaxRowCount(0);
			}
		}
		return studentRewardPointList;
	}

	public List<DyStudentRewardPoint> findBySettingIdStuSearch(String settingId, int semester, String acadyear, String fieldType, String search){
		List<DyStudentRewardPoint> pointList =dyStudentRewardPointDao.findBySettingId(settingId,semester+"",acadyear);
		pointList = dealPointList(pointList, fieldType+"="+search);
		return pointList;
	}

	@Override
	public List<DyStudentRewardPoint> findBySettingId(String settingId, int semester, String acadyear) {
		List<DyStudentRewardPoint> pointList =dyStudentRewardPointDao.findBySettingId(settingId,semester+"",acadyear); 
		pointList = dealPointList(pointList, null);
		return pointList;
	}

	private List<DyStudentRewardPoint> dealPointList(List<DyStudentRewardPoint> pointList, String stuSearch){
		Set<String> stuIdSet= new HashSet<String>();
		Set<String> projectIds = new HashSet<String>();
		Set<String> settingIds = new HashSet<String>();
		for(DyStudentRewardPoint item : pointList){
			stuIdSet.add(item.getStudentId());
			projectIds.add(item.getProjectId());
			settingIds.add(item.getSettingId());
		}
		Map<String, String> stuNameMap = new HashMap<String, String>();
		Map<String, String> stuCodeMap = new HashMap<String, String>();
		Map<String, String> stuClsIdMap = new HashMap<>();
		Set<String> clsIds = new HashSet<>();
		List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(stuIdSet.toArray(new String[0])), new TR<List<Student>>() {});
		String stuCode = "";
		String stuName = "";
		if (StringUtils.isNotEmpty(stuSearch)) {
			String[] strs = stuSearch.split("=");
			if(strs.length == 2){
				if("1".equals(strs[0])){
					stuName = strs[1];
				} else if("2".equals(strs[0])){
					stuCode = strs[1];
				}
			}
		}
		boolean hasCode = StringUtils.isNotEmpty(stuCode);
		boolean hasName = StringUtils.isNotEmpty(stuName);
		Map<String, Student> stuMap = new HashMap<>();
		for (Student stu : studentList) {
			if(hasName && stu.getStudentName().indexOf(stuName) == -1){
				continue;
			}
			if(hasCode && StringUtils.trimToEmpty(stu.getStudentCode()).indexOf(stuCode) == -1){
				continue;
			}
			stuMap.put(stu.getId(), stu);
			stuNameMap.put(stu.getId(), stu.getStudentName());
			stuCodeMap.put(stu.getId(), stu.getStudentCode());
			stuClsIdMap.put(stu.getId(), stu.getClassId());
			clsIds.add(stu.getClassId());
		}
		Map<String, Clazz> clsMap = EntityUtils.getMap(SUtils.dt(classRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR<List<Clazz>>() {}), Clazz::getId);
		Map<String,DyStudentRewardProject> projectMap = dyStudentRewardProjectService.findMapByIdIn(projectIds.toArray(new String[0]));
		Map<String,DyStudentRewardSetting> settingMap = dyStudentRewardSettingService.findMapByIdIn(settingIds.toArray(new String[0]));
		Iterator<DyStudentRewardPoint> pi = pointList.iterator();
		while (pi.hasNext()) {
			DyStudentRewardPoint point=pi.next();
			if(!stuMap.containsKey(point.getStudentId())){
				pi.remove();
			}
			point.setStudentName(stuNameMap.get(point.getStudentId()));
			point.setStucode(stuCodeMap.get(point.getStudentId()));
			if(stuClsIdMap.containsKey(point.getStudentId())
					&& clsMap.containsKey(stuClsIdMap.get(point.getStudentId()))) {
				//TODO by nizq
				point.setClassName(clsMap.get(stuClsIdMap.get(point.getStudentId())).getClassNameDynamic());
			}
			DyStudentRewardProject project = projectMap.get(point.getProjectId());
			if(project!=null){
				point.setProjectName(project.getProjectName());
				point.setRewardClasses(project.getRewardClasses());
				point.setProjectRemark(project.getProjectRemark());
			}
			DyStudentRewardSetting setting = settingMap.get(point.getSettingId());
			if(setting!=null){
				point.setRewardGrade(setting.getRewardGrade());
				point.setRewardLevel(setting.getRewardLevel());
				point.setRewardPeriod(setting.getRewardPeriod());
			}
		}
		
		return pointList;
		
	}

	@Override
	public void saveDto(DyStudentRewardPointDto pointDto,String acadyear,String semester) {
		Set<String> pointIds = new HashSet<String>();
		Map<String,DyStudentRewardPoint> pointMap = new HashMap<String,DyStudentRewardPoint>();
		DyStudentRewardProject project = dyStudentRewardProjectService.findOne(pointDto.getDyStudentRewardProject().getId());
		DyStudentRewardSetting setting = dyStudentRewardSettingService.findOne(pointDto.getDyStudentRewardSetting().getId());
		List<DyStudentRewardPoint> points = pointDto.getDyStudentRewardPoints();
		List<DyStudentRewardPoint> savePoints = new ArrayList<DyStudentRewardPoint>();
		for(DyStudentRewardPoint point : points) {
			if(point!=null&&StringUtils.isNotBlank(point.getStudentId())){
				if(StringUtils.isNotBlank(point.getId())){
					pointIds.add(point.getId());
					pointMap.put(point.getId(), point);
				}else{
					point.setId(UuidUtils.generateUuid());
					point.setProjectId(project.getId());
					point.setSettingId(setting.getId());
					point.setUnitId(project.getUnitId());
					point.setAcadyear(acadyear);
					point.setSemester(semester);
					point.setCreationTime(new Date());
					point.setModifyTime(point.getCreationTime());
					if(StuworkConstants.STUDENT_REWARD_GAME.equals(project.getClassesType())){
						point.setRewardCountPoint("all");
					}else if(StuworkConstants.STUDENT_REWARD_SCHOOL.equals(project.getClassesType())){
						point.setRewardCountPoint(acadyear);
					}else if(StuworkConstants.STUDENT_REWARD_FESTIVAL.equals(project.getClassesType())){
						point.setRewardCountPoint(project.getRewardPeriod()+"");
					}
					savePoints.add(point);
				}
			}
		}
		
		if(CollectionUtils.isNotEmpty(pointIds)){
			List<DyStudentRewardPoint>  updatePoints = findListByIds(pointIds.toArray(new String[0]));
			if(CollectionUtils.isNotEmpty(updatePoints)){
				for (DyStudentRewardPoint dyStudentRewardPoint : updatePoints) {
					DyStudentRewardPoint oldPoint = pointMap.get(dyStudentRewardPoint.getId());
					dyStudentRewardPoint.setRewardPoint(oldPoint.getRewardPoint());
					dyStudentRewardPoint.setRemark(oldPoint.getRemark());
					dyStudentRewardPoint.setModifyTime(new Date());
					savePoints.add(dyStudentRewardPoint);
				}
			}
		}
		if(CollectionUtils.isNotEmpty(pointIds)){
			deletePoints(setting.getId(),acadyear,semester,pointIds.toArray(new String[0]));
			
		}else{
			deletePoints(setting.getId(),acadyear,semester);
			
		}
		
		saveAll(savePoints.toArray(new DyStudentRewardPoint[0]));
		
	}

	private void deletePoints(String settingId, String acadyear, String semester,
			String[] pointIds) {
		dyStudentRewardPointDao.deletePoints(settingId,acadyear,semester,pointIds);
	}

	public void deletePoints(String settingId, String acadyear, String semester) {
		dyStudentRewardPointDao.deletePoints(settingId,acadyear,semester);
	}

	@Override
	public DyStuworkDataCountDto findStuworkCountByStudentId(
			Map<String, Integer> maxValueMap, String studentId,
			Integer maxValuePer, boolean isShow) {
		// 
		DyStuworkDataCountDto dto = new DyStuworkDataCountDto();
		// float countNamber = 0;
		String[] acadyears = new String[4];
		Student student = SUtils.dc(studentRemoteService.findOneById(studentId),
				Student.class);
		String classId = student.getClassId();
		Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId),
				Clazz.class);
		String unitId = student.getSchoolId();
		if (clazz != null) {
			// 判断是否是高中
			if (clazz.getSection() == 3 ||clazz.getSection() == 9) {
				acadyears = createAcadyear(clazz.getAcadyear(), isShow);
				List<DyStudentRewardPoint> points = dyStudentRewardPointDao
						.findByAcadyear(unitId, studentId, acadyears);
				acadyears = createAcadyear(clazz.getAcadyear(), true);
				makeDto(maxValueMap, maxValuePer, dto, acadyears, points);

			}
		}
		return dto;
	}

	private void makeDto(Map<String, Integer> maxValueMap, Integer maxValuePer, DyStuworkDataCountDto dto, String[] acadyears, List<DyStudentRewardPoint> points) {
		Set<String> projectIds = new HashSet<String>();
		Set<String> settingIds = new HashSet<String>();
		String firstAcadyear = acadyears[0];
		String lastAcadyear = acadyears[3];
		Map<String, List<DyStudentRewardPoint>> acadyearPointMap = new HashMap<String, List<DyStudentRewardPoint>>();
		if(CollectionUtils.isNotEmpty(points)){
			for (DyStudentRewardPoint point : points) {
				
				projectIds.add(point.getProjectId());
				settingIds.add(point.getSettingId());
				String point_key = point.getAcadyear();
				List<DyStudentRewardPoint> tempAcadyearPoints = acadyearPointMap
						.get(point_key);
				if (CollectionUtils.isEmpty(tempAcadyearPoints)) {
					tempAcadyearPoints = new ArrayList<DyStudentRewardPoint>();
				}
				if (point_key.equals(firstAcadyear)) {
					if (point.getSemester().equals("2")) {
						tempAcadyearPoints.add(point);
						acadyearPointMap.put(point_key, tempAcadyearPoints);
					}
				} else if (point_key.equals(lastAcadyear)) {
					if (point.getSemester().equals("1")) {
						tempAcadyearPoints.add(point);
						acadyearPointMap.put(point_key, tempAcadyearPoints);
					}
				} else {
					tempAcadyearPoints.add(point);
					acadyearPointMap.put(point_key, tempAcadyearPoints);
				}
				
			}
		}
		Map<String, DyStudentRewardSetting> settingMap = dyStudentRewardSettingService
				.findMapByIdIn(settingIds.toArray(new String[0]));
		Map<String, DyStudentRewardProject> projectMap = dyStudentRewardProjectService
				.findMapByIdIn(projectIds.toArray(new String[0]));
		//最终生成的 numbercount和infocount 的keyset
		Map<String, Set<String>> mapkey = new HashMap<String, Set<String>>();
		Map<String, Float> numberCount = new HashMap<String, Float>();
		Map<String, String> infoCount = new HashMap<String, String>();

		Map<String, Float> gameFloatCount = new HashMap<String, Float>();
		Map<String, Float> festivalFloatCount = new HashMap<String, Float>();
		Map<String, Float> schoolFloatCount = new HashMap<String, Float>();
		for (String key : acadyearPointMap.keySet()) {
			List<DyStudentRewardPoint> acadyearPoints = acadyearPointMap
					.get(key);
			for (DyStudentRewardPoint dyStudentRewardPoint : acadyearPoints) {
				DyStudentRewardProject project = projectMap
						.get(dyStudentRewardPoint.getProjectId());
				DyStudentRewardSetting setting = settingMap
						.get(dyStudentRewardPoint.getSettingId());
				String tempkey = "";
				String info = "";
				String lastKey = "";
				if(project!=null) {
					if (project.getClassesType().equals(StuworkConstants.STUDENT_REWARD_GAME)) {
						tempkey = key + "-" + project.getRewardClasses()
						+ "-" + project.getProjectName();
						info = project.getRewardClasses() + "-"
								+ project.getProjectName() + "-"
								+ setting.getRewardGrade() + "-"
								+ setting.getRewardLevel();
						//学科竞赛备注
						if(StringUtils.isNotBlank(dyStudentRewardPoint.getRemark())) {
							info=info+"(备注:"+dyStudentRewardPoint.getRemark()+")";
						}
						lastKey = key;
						
						// 特殊处理
						Float gamenumber = gameFloatCount.get(project
								.getRewardClasses()
								+ "-"
								+ project.getProjectName());
						if (gamenumber != null
								&& gamenumber > dyStudentRewardPoint
								.getRewardPoint()) {
							
						} else {
							gameFloatCount.put(project.getRewardClasses()
									+ "-" + project.getProjectName(),
									dyStudentRewardPoint.getRewardPoint());
						}
						
					} else if (project.getClassesType().equals(StuworkConstants.STUDENT_REWARD_SCHOOL) ){
						if (StringUtils.isBlank(project.getProjectRemark())) {
							project.setProjectRemark("");
						}
						if("全校性活动".equals(project.getRewardClasses())){//后期额外改动全校性活动 成个人和团体两种 但分类未变，所以此时以下操作
							if("0501".equals(project.getProjectType())){//个人
								project.setProjectRemark("个人");
							}else{
								project.setProjectRemark("团体");
							}
						}
						tempkey = key + "-" + project.getRewardClasses()
						+ "-" + project.getProjectName() + "-"
						+ project.getProjectRemark();
						info = project.getProjectName();
						//全校性活动备注
						if(StringUtils.isNotBlank(dyStudentRewardPoint.getRemark())) {
							info=info+"(备注:"+dyStudentRewardPoint.getRemark()+")";
						}
						
						lastKey = key + "-" + project.getRewardClasses()
						+ "-" + project.getProjectRemark();
						
					} else if (project.getClassesType().equals(StuworkConstants.STUDENT_REWARD_FESTIVAL)) {
						tempkey = key + "-" + project.getRewardClasses()
						+ "-" + project.getProjectName() + "-"
						+ project.getRewardPeriod();
						info = setting.getRewardLevel();
						//学生干部、社团骨干、评优先进、突出贡献、体育节、外语节、艺术节、科技节、文化节备注
						if(StringUtils.isNotBlank(dyStudentRewardPoint.getRemark())) {
							info=info+"(备注:"+dyStudentRewardPoint.getRemark()+")";
						}
						lastKey = key + "-" + project.getRewardClasses()
						+ "-" + project.getProjectName();
						
						Float festivalnumber = festivalFloatCount
								.get(project.getRewardClasses()+"-"+project.getRewardPeriod() + "");
						
						if (festivalnumber != null ) {
							festivalnumber = festivalnumber+dyStudentRewardPoint.getRewardPoint();
							if(maxValuePer>0&&festivalnumber > maxValuePer){
								festivalFloatCount.put(
										project.getRewardClasses()+"-"+project.getRewardPeriod() + "",
										(float)maxValuePer);
							}else{
								
								festivalFloatCount.put(
										project.getRewardClasses()+"-"+project.getRewardPeriod() + "",
										festivalnumber);
							}
						} else {
							if(maxValuePer>0&&dyStudentRewardPoint.getRewardPoint()> maxValuePer){
								festivalFloatCount.put(
										project.getRewardClasses()+"-"+project.getRewardPeriod() + "",
										(float)maxValuePer);
							}else{
								
								festivalFloatCount.put(
										project.getRewardClasses()+"-"+project.getRewardPeriod() + "",
										dyStudentRewardPoint.getRewardPoint());
							}
							
						}
					}
					Float tempNumberCount = numberCount.get(tempkey);
					float tempNumberCountFloat = 0;
					if (tempNumberCount != null) {
						tempNumberCountFloat = tempNumberCount;
					}
					if (project.getClassesType().equals("1")) {
						if (tempNumberCountFloat < dyStudentRewardPoint
								.getRewardPoint()) {
							tempNumberCountFloat = dyStudentRewardPoint
									.getRewardPoint();
						}
					} else {
						tempNumberCountFloat += dyStudentRewardPoint
								.getRewardPoint();
						
					}
					String tempInfoCount = infoCount.get(tempkey);
					if (StringUtils.isBlank(tempInfoCount)) {
						tempInfoCount = "";
					}
					tempInfoCount = tempInfoCount + info + ",";
					numberCount.put(tempkey, tempNumberCountFloat);
					infoCount.put(tempkey, tempInfoCount);
					Set<String> keyList = mapkey.get(lastKey);
					if (CollectionUtils.isEmpty(keyList)) {
						keyList = new HashSet<String>();
					}
					keyList.add(tempkey);
					mapkey.put(lastKey, keyList);
					
//					 处理校内奖励
					if("社团骨干".equals(project.getRewardClasses())){
						Float schoolnumber = schoolFloatCount.get(key + "-"
								+ project.getRewardClasses() + "-"
								+ project.getProjectRemark());
//						Float nowNumberCount = numberCount.get(tempkey);
						Float nowNumberCount = dyStudentRewardPoint.getRewardPoint();
						schoolnumber=schoolnumber==null?0.0f:schoolnumber;
						schoolFloatCount.put(
								key + "-" + project.getRewardClasses()
								+ "-"+ project.getProjectRemark(),
								nowNumberCount+schoolnumber);
					}else 
					if (project.getClassesType().equals(StuworkConstants.STUDENT_REWARD_SCHOOL)) {
						Float schoolnumber = schoolFloatCount.get(key + "-"+ project.getRewardClasses() + "-"+ project.getProjectRemark());
						Float nowNumberCount = numberCount.get(tempkey);
						if (schoolnumber != null
								&& schoolnumber > nowNumberCount) {
						} else {
							schoolFloatCount.put(key + "-" + project.getRewardClasses()+ "-"+ project.getProjectRemark(),nowNumberCount);
						}
					}
				}
				}

		}
		//TODO 此处处理评优先进-先进
		for (String acadyear : acadyears) {
			if(numberCount.containsKey(acadyear+"-评优先进-三星级寝室成员-先进")
					&& numberCount.containsKey(acadyear+"-评优先进-优秀寝室长-先进")) {
				float score = numberCount.get(acadyear+"-评优先进-三星级寝室成员-先进")+numberCount.get(acadyear+"-评优先进-优秀寝室长-先进");
				if(schoolFloatCount.containsKey(acadyear+"-评优先进-先进")) {
					if(score>schoolFloatCount.get(acadyear+"-评优先进-先进")) {
						schoolFloatCount.put(acadyear+"-评优先进-先进", score);
					}
				}else {
					schoolFloatCount.put(acadyear+"-评优先进-先进", score);
				}
				
			}
		}
		List<String[]> stuworks = new ArrayList<String[]>();
		List<String[]> festivals = new ArrayList<String[]>();
		List<String[]> games = new ArrayList<String[]>();

		Float[] countnumbers = dto.getCountNumbers();
		Map<String, Float> floatMap = new HashMap<String, Float>();
		for (String acadyear : acadyears) {
			String[] stuwrok = new String[20];
			String[] festival = new String[24];
			String[] game = new String[2];

			Map<String, String> countkeyMap = new HashMap<String, String>();

			countkeyMap.put(acadyear + "-" + "学生干部" + "-", "0_2");
			countkeyMap.put(acadyear + "-" + "社团骨干" + "-", "0_4");
			countkeyMap.put(acadyear + "-" + "突出贡献" + "-", "0_6");
			countkeyMap.put(acadyear + "-" + "评优先进" + "-" + "评优",
					"0_16");
			countkeyMap.put(acadyear + "-" + "评优先进" + "-" + "先进",
					"0_18");
			countkeyMap.put(acadyear + "-" + "体育节" + "-" + "个人", "1_0");
			countkeyMap.put(acadyear + "-" + "体育节" + "-" + "团体", "1_2");
			countkeyMap.put(acadyear + "-" + "外语节" + "-" + "个人", "1_4");
			countkeyMap.put(acadyear + "-" + "外语节" + "-" + "团体", "1_6");
			countkeyMap.put(acadyear + "-" + "艺术节" + "-" + "个人", "1_8");
			countkeyMap
					.put(acadyear + "-" + "艺术节" + "-" + "团体", "1_10");
			countkeyMap
					.put(acadyear + "-" + "科技节" + "-" + "个人", "1_12");
			countkeyMap
					.put(acadyear + "-" + "科技节" + "-" + "团体", "1_14");
			countkeyMap
					.put(acadyear + "-" + "文化节" + "-" + "个人", "1_16");
			countkeyMap
					.put(acadyear + "-" + "文化节" + "-" + "团体", "1_18");
			countkeyMap.put(acadyear + "-" + "全校性活动" + "-"+"个人", "1_20");
			countkeyMap.put(acadyear + "-" + "全校性活动" + "-"+"团体", "1_22");
			countkeyMap.put(acadyear, "2_0");

			for (String countkey : countkeyMap.keySet()) {
				if(countkey.equals(acadyear + "-" + "全校性活动" + "-个人")||countkey.equals(acadyear + "-" + "全校性活动" + "-团体")){
					System.out.println("");
				}
				String index = countkeyMap.get(countkey);
				String[] arrayIndex = index.split("_");
				Set<String> keyList = mapkey.get(countkey);
				if (keyList == null) {
					keyList = new HashSet<String>();
				}
				StringBuffer sb = new StringBuffer("");
				float countFloat = 0;
				for (String key : keyList) {
					if (numberCount.get(key) != null) {
						countFloat = countFloat + numberCount.get(key);
					}
					if (StringUtils.isNotBlank(infoCount.get(key))) {
						sb.append(infoCount.get(key));
					}
				}
				if (arrayIndex[0].equals("0")) {
					String sbstr = sb.toString();
					if(sbstr.length()>1){
						sbstr =sbstr.substring(0, sbstr.length()-1);
					}
					stuwrok[Integer.parseInt(arrayIndex[1])] = sbstr;
					Float float3 = schoolFloatCount.get(countkey);
					if (float3 == null) {
						float3 = (float) 0;
					}
					stuwrok[Integer.parseInt(arrayIndex[1]) + 1] = float3
							+ "";

					String floatMapkey = "";
					if (Integer.parseInt(arrayIndex[1]) == 2) {
						floatMapkey = DyStuworkDataCountDto.XSGB_MAX_NUMBER;
					} else if (Integer.parseInt(arrayIndex[1]) == 4) {
						floatMapkey = DyStuworkDataCountDto.STGG_MAX_NUMBER;
					} else if (Integer.parseInt(arrayIndex[1]) == 6) {
						floatMapkey = DyStuworkDataCountDto.TCGX_MAX_NUMBER;
					} else if (Integer.parseInt(arrayIndex[1]) == 16) {
						floatMapkey = DyStuworkDataCountDto.PYXJ_MAX_NUMBER
								+ "1";
					} else if (Integer.parseInt(arrayIndex[1]) == 18) {
						floatMapkey = DyStuworkDataCountDto.PYXJ_MAX_NUMBER
								+ "2";
					}
					Float float2 = floatMap.get(floatMapkey);
					if (float2 == null) {
						float2 = (float) 0;
					}
					float2 = float2 + float3;
					floatMap.put(floatMapkey, float2);
					if(countnumbers[0]==null){
						countnumbers[0] = (float) 0;
					}
					countnumbers[0] = countnumbers[0] + float3;
				} else if (arrayIndex[0].equals("1")) {
					String sbstr = sb.toString();
					if(sbstr.length()>1){
						sbstr =sbstr.substring(0, sbstr.length()-1);
					}
					festival[Integer.parseInt(arrayIndex[1])] = sbstr;
					
					if (Integer.parseInt(arrayIndex[1]) == 20 || Integer.parseInt(arrayIndex[1]) == 22) {
						Float float3 = schoolFloatCount.get(countkey);
						if (float3 == null) {
							float3 = (float) 0;
						}
						festival[Integer.parseInt(arrayIndex[1]) + 1] = float3
								+ "";
					} else {
						festival[Integer.parseInt(arrayIndex[1]) + 1] = countFloat
								+ "";
					}
					if (countnumbers[1] == null) {
						countnumbers[1] = (float) 0;
					}
					if (Integer.parseInt(arrayIndex[1]) == 20|| Integer.parseInt(arrayIndex[1]) == 22) {
						countnumbers[1] = countnumbers[1] + countFloat;
					}
				} else if (arrayIndex[0].equals("2")) {
					String sbstr = sb.toString();
					if(sbstr.length()>1){
						sbstr =sbstr.substring(0, sbstr.length()-1);
					}
					game[Integer.parseInt(arrayIndex[1])] = sbstr;
					
					game[Integer.parseInt(arrayIndex[1]) + 1] = countFloat
							+ "";
				}

			}

			stuworks.add(stuwrok);
			festivals.add(festival);
			games.add(game);
			// sb = new StringBuffer("");
			// List<String> keyList17 = mapkey.get(key17);
		}

		Map<String, List<String[]>> infoMap = new HashMap<String, List<String[]>>();
		infoMap.put(DyStuworkDataCountDto.STUWORK_LIST, stuworks);
		infoMap.put(DyStuworkDataCountDto.FESTIVAL_LIST, festivals);
		infoMap.put(DyStuworkDataCountDto.GAME_LIST, games);
		dto.setInfoMap(infoMap);
		dto.setAcadyears(acadyears);
		// countNumbers
		// 特殊处理学科竞赛总分
		float gamecount = 0;
		for (String gamefloatkety : gameFloatCount.keySet()) {
			gamecount += gameFloatCount.get(gamefloatkety);
		}
		int gameMax = maxValueMap.get(DyStuworkDataCountDto.XKJS_MAX_NUMBER);
		if(gameMax!=0){
			
			if(gamecount>gameMax){
				countnumbers[2] = (float) gameMax;
			}else{
				countnumbers[2] = gamecount;
				
			}
		}else{
			countnumbers[2] = gamecount;
		}

		dto.setCountNumbers(countnumbers);

		// 特殊处理节假日总分
		float festivalcount = 0;
		for (String festivalfloatkety : festivalFloatCount.keySet()) {
			festivalcount += festivalFloatCount.get(festivalfloatkety);
		}
		if(festivalcount!=0) {
			int max = maxValueMap
					.get(DyStuworkDataCountDto.FESTIVAL_MAX_NUMBER);
			if(max!=0){
				if (festivalcount > max) {
					festivalcount=max;
				} else {
				
				}
			}
		}
		
		if (countnumbers[1] != null) {
			float float1 = countnumbers[1];
			int max = maxValueMap
					.get(DyStuworkDataCountDto.QXXHD_MAX_NUMBER);
			if(max!=0){
				if (float1 > max) {
					countnumbers[1] = max + festivalcount;
				} else {
					countnumbers[1] = float1 + festivalcount;
				}
			}else{
				countnumbers[1] = float1 + festivalcount;
			}

		} else {
			countnumbers[1] = festivalcount;
		}
		
		// 处理德育总分
		countnumbers[0] = (float) 0;
		//处理德育评优先进
		String key1 = DyStuworkDataCountDto.PYXJ_MAX_NUMBER
				+ "1";
		String key2 = DyStuworkDataCountDto.PYXJ_MAX_NUMBER
				+ "2";
		Float f1 =floatMap.get(key1);
		Float f2 =floatMap.get(key2);
		floatMap.remove(key1);
		floatMap.remove(key2);
		if(f1==null){
			f1=(float) 0;
		}
		if(f2==null){
			f2=(float) 0;
		}
		floatMap.put( DyStuworkDataCountDto.PYXJ_MAX_NUMBER, f1+f2);
		for (String key : floatMap.keySet()) {
			Float float3 = floatMap.get(key);
			if (float3 == null) {
				float3 = (float) 0;
			} else {
				if(maxValueMap.get(key)!=0){
					if (float3 > maxValueMap.get(key)) {
						countnumbers[0] = countnumbers[0]
								+ maxValueMap.get(key);
					} else {
						countnumbers[0] = countnumbers[0] + float3;
					}
				}else{
					countnumbers[0] = countnumbers[0] + float3;
				}
			}
		}

		dto.setCountNumbers(countnumbers);
	}
	
	public float countList(List<String> keyList,Map<String, Float> numberCount,Map<String, String> infoCount,StringBuffer sb) {
		float countFloat = 0;
		for (String key : keyList) {
			countFloat =countFloat + numberCount.get(key);
			sb.append(infoCount.get(key));
		}
		return countFloat;
	}
	
	
	
	private String[] createAcadyear(String acadyear, boolean isShow) {
		List<String> acadyearList = new ArrayList<String>();
		if(StringUtils.isNotBlank(acadyear)) {
			//取初3
			int openAcadyearBefore = Integer.parseInt(acadyear.substring(0,4));
			int openAcadyearAfter = Integer.parseInt(acadyear.substring(5,9));
			
			String acadyear1 = (openAcadyearBefore-1)+"-"+(openAcadyearAfter-1);
//			String acadyear1 = (openAcadyearBefore-1)+"-"+(openAcadyearAfter-1);
			String acadyear3 = (openAcadyearBefore+1)+"-"+(openAcadyearAfter+1);
			String acadyear4 = (openAcadyearBefore+2)+"-"+(openAcadyearAfter+2);
			if(isShow){
				acadyearList.add(acadyear1);
			}
			acadyearList.add(acadyear);
			acadyearList.add(acadyear3);
			acadyearList.add(acadyear4);
			
		}
		
		return acadyearList.toArray(new String[0]);
	}
	
	private String createAcadyears(String acadyear) {
		StringBuffer acadyearList = new StringBuffer();
		if(StringUtils.isNotBlank(acadyear)) {
			//取初3
			int openAcadyearBefore = Integer.parseInt(acadyear.substring(0,4));
			int openAcadyearAfter = Integer.parseInt(acadyear.substring(5,9));
			
			String acadyear1 = (openAcadyearBefore-1)+"-"+(openAcadyearAfter-1);
//			String acadyear1 = (openAcadyearBefore-1)+"-"+(openAcadyearAfter-1);
			String acadyear3 = (openAcadyearBefore+1)+"-"+(openAcadyearAfter+1);
			String acadyear4 = (openAcadyearBefore+2)+"-"+(openAcadyearAfter+2);
			acadyearList.append(acadyear1+",");
			acadyearList.append(acadyear+",");
			acadyearList.append(acadyear3+",");
			acadyearList.append(acadyear4);
			
		}
		
		return acadyearList.toString();
	}
	
	@Override
	public Map<String,Float> findStuworkCountByUnitId(Map<String, Integer> maxValueMap, String unitId,
			Integer maxValuePer, Map<String, Boolean> showMap) {
		Map<String,Float> retunMap = new HashMap<String, Float>();
		List<Clazz> classList=SUtils.dt(classRemoteService.findBySchoolId(unitId), new TR<List<Clazz>>(){});
		List<Clazz> classList1 = new ArrayList<Clazz>();
		Map<String,Clazz> classMap = new HashMap<String, Clazz>();
		for (Clazz clazz : classList) {
			if (clazz.getSection() == 3||clazz.getSection() == 9) {
				classList1.add(clazz);
				classMap.put(clazz.getId(), clazz);
			}
		}
		
		Map<String,String> studentAcadyearMap = new HashMap<String,String>();
		List<Student> students = SUtils.dt(studentRemoteService.findByClassIds(classMap.keySet().toArray(new String[0])),new TR<List<Student>>() {});
//		students=SUtils.dt(studentRemoteService.findListByIds("06C16FA33762433C8C442752F9105AC2"),new TR<List<Student>>() {});
		Map<String,Student> sMap = new HashMap<String, Student>();
		for (Student student: students) {
			sMap.put(student.getId(), student);
			String acadyearstr =studentAcadyearMap.get(student.getId());
			Clazz clazz = classMap.get(student.getClassId());
			if(StringUtils.isBlank(acadyearstr)){
				studentAcadyearMap.put(student.getId(),createAcadyears(clazz.getAcadyear()));
			}
		}
		
		List<DyStudentRewardPoint> points = dyStudentRewardPointDao.findByUnitId(unitId);
		List<DyStudentRewardSetting> settings = dyStudentRewardSettingService.findByUnitId(unitId);
		List< DyStudentRewardProject> projects = dyStudentRewardProjectService.findByUnitId(unitId);
		Map<String,DyStudentRewardSetting> settingMap =new HashMap<String, DyStudentRewardSetting>();
		Map<String, DyStudentRewardProject> projectMap = new HashMap<String, DyStudentRewardProject>();
		for (DyStudentRewardSetting dyStudentRewardSetting : settings) {
			settingMap.put(dyStudentRewardSetting.getId(), dyStudentRewardSetting);
		}
		for (DyStudentRewardProject dyStudentRewardProject : projects) {
			projectMap.put(dyStudentRewardProject.getId(), dyStudentRewardProject);
		}
		
		
		Map<String, List<DyStudentRewardPoint>> acadyearPointMap = new HashMap<String, List<DyStudentRewardPoint>>();
		for (DyStudentRewardPoint point : points) {
			
			Student student = sMap.get(point.getStudentId());
			if(student!=null){
				Clazz clazz1 = classMap.get(student.getClassId());
				String acadyearstr =studentAcadyearMap.get(student.getId());
				if(StringUtils.isBlank(acadyearstr)) {
					acadyearstr = createAcadyears(clazz1.getAcadyear());
				}
				String[] acadyears = acadyearstr.split(",");
				String firstAcadyear = acadyears[0];
				String lastAcadyear = acadyears[3];

				if(lastAcadyear.equals(point.getAcadyear())&&"2".equals(point.getSemester())){
					continue;//高三下不计入
				}
				boolean isShow = showMap.containsKey(clazz1.getGradeId())&&showMap.get(clazz1.getGradeId());
				if(point.getAcadyear().equals(firstAcadyear) && !isShow){
					continue;
				}
				String point_key = point.getAcadyear()+"-"+point.getStudentId();
				List<DyStudentRewardPoint> tempAcadyearPoints = acadyearPointMap.get(point_key);
				if (CollectionUtils.isEmpty(tempAcadyearPoints)) {
					tempAcadyearPoints = new ArrayList<DyStudentRewardPoint>();
				}
				if (point.getAcadyear().equals(firstAcadyear)) {
					if (point.getSemester().equals("2")) {
						tempAcadyearPoints.add(point);
						acadyearPointMap.put(point_key, tempAcadyearPoints);
					}
				} else if (point.getAcadyear().equals(lastAcadyear)) {
					if (point.getSemester().equals("1")) {
						tempAcadyearPoints.add(point);
						acadyearPointMap.put(point_key, tempAcadyearPoints);
					}
				} else {
					tempAcadyearPoints.add(point);
					acadyearPointMap.put(point_key, tempAcadyearPoints);
				}
			}
			
		}
		
		for (Student student : students) {
			//key classes+projectname
			Map<String, Float> gameFloatCount = new HashMap<String, Float>();
			//key+period
			Map<String, Float> festivalFloatCount = new HashMap<String, Float>();
			//key acadyear+classes+remark
			Map<String, Float> schoolFloatCount = new HashMap<String, Float>();
			//key  acadyear+classes+projectname+remark
			Map<String, Float> schoolAllFloatCount = new HashMap<String, Float>();
			//取最高分用
			
//			Clazz clazz1 = classMap.get(student.getClassId());
			String acadyearstr =studentAcadyearMap.get(student.getId());
			String[] acadyears = acadyearstr.split(",");
			for (String key : acadyears) {
				List<DyStudentRewardPoint> acadyearPoints = acadyearPointMap.get(key+"-"+student.getId());
				if(CollectionUtils.isEmpty(acadyearPoints)){
					acadyearPoints= new ArrayList<DyStudentRewardPoint>();
				}
				for (DyStudentRewardPoint point : acadyearPoints) {
					DyStudentRewardProject project = projectMap.get(point.getProjectId());
					DyStudentRewardSetting setting = settingMap.get(point.getSettingId());
					if(project!=null) {
						if (project.getClassesType().equals(StuworkConstants.STUDENT_REWARD_GAME)) {
							Float gamenumber = gameFloatCount.get(project.getRewardClasses() + "-" + project.getProjectName());
							if(gamenumber!=null && gamenumber>point.getRewardPoint()) {
							}else {
								gameFloatCount.put(project.getRewardClasses() + "-" + project.getProjectName(), point.getRewardPoint());
							}
						}else if (project.getClassesType().equals(StuworkConstants.STUDENT_REWARD_SCHOOL)) {
							if("0501".equals(project.getProjectType())){//全校性 个人 
								project.setProjectRemark("个人");
							}else if("0502".equals(project.getProjectType())){
								project.setProjectRemark("团体");
							}
							if(StringUtils.isBlank(project.getProjectRemark())){
								project.setProjectRemark("");
							}
							Float schoolallnumber =  schoolAllFloatCount.get(key+"-"+project.getRewardClasses()+"-"+project.getProjectName()+"-"+project.getProjectRemark());
							if(schoolallnumber!=null){
								schoolallnumber = schoolallnumber+point.getRewardPoint();
							}else{
								schoolallnumber = point.getRewardPoint();
							}
							schoolAllFloatCount.put(key+"-"+project.getRewardClasses()+"-"+project.getProjectName()+"-"+project.getProjectRemark(),schoolallnumber);
							Float schoolnumber =  schoolFloatCount.get(key+"-"+project.getRewardClasses()+"-"+project.getProjectRemark());
							if(schoolnumber==null){
								schoolnumber=schoolallnumber;
							}else{
								if("社团骨干".equals(project.getRewardClasses())||"全校性活动".equals(project.getRewardClasses())||"突出贡献".equals(project.getRewardClasses())){
									schoolnumber+=point.getRewardPoint();
								}else{
									if(schoolnumber<schoolallnumber){
										schoolnumber = schoolallnumber;
									}
								}
							}
							schoolFloatCount.put(key+"-"+project.getRewardClasses()+"-"+project.getProjectRemark(),schoolnumber);
							
							
						} else if (project.getClassesType().equals( StuworkConstants.STUDENT_REWARD_FESTIVAL)) {
							Float festivalfloat = festivalFloatCount.get(key+"-"+project.getRewardClasses()+"-"+project.getRewardPeriod());
							if(festivalfloat!=null){
								festivalfloat =festivalfloat+point.getRewardPoint();
							}else{
								festivalfloat =point.getRewardPoint();	
							}
							festivalFloatCount.put(key+"-"+project.getRewardClasses()+"-"+project.getRewardPeriod(),festivalfloat);
						}
					}
				}
				//TODO 此处处理评优先进-先进
				if(schoolAllFloatCount.containsKey(key+"-评优先进-三星级寝室成员-先进")
						&& schoolAllFloatCount.containsKey(key+"-评优先进-优秀寝室长-先进")) {
					float score = schoolAllFloatCount.get(key+"-评优先进-三星级寝室成员-先进")+schoolAllFloatCount.get(key+"-评优先进-优秀寝室长-先进");
					if(schoolFloatCount.containsKey(key+"-评优先进-先进")) {
						if(score>schoolFloatCount.get(key+"-评优先进-先进")) {
							schoolFloatCount.put(key+"-评优先进-先进", score);
						}
					}else {
						schoolFloatCount.put(key+"-评优先进-先进", score);
					}
				}
			}
			
			//处理学生数据
			float gamecountFloat= 0;
			//学科竞赛分数
			for (String key : gameFloatCount.keySet()) {
				gamecountFloat +=gameFloatCount.get(key);
			}
			if(maxValueMap.get(DyStuworkDataCountDto.XKJS_MAX_NUMBER)!=0){
				if(gamecountFloat>maxValueMap.get(DyStuworkDataCountDto.XKJS_MAX_NUMBER)){
					gamecountFloat = maxValueMap.get(DyStuworkDataCountDto.XKJS_MAX_NUMBER);
				}
			}
			
//			public static final String CXDD_MAX_NUMBER="CXDD.MAX.NUMBER";//操行等第
//			public static final String ZZBX_MAX_NUMBER="ZZBX.MAX.NUMBER";//值周表现
//			public static final String JX_MAX_NUMBER="JX.MAX.NUMBER";//军训
//
//			public static final String XSGB_MAX_NUMBER="XSGB.MAX.NUMBER";//学生干部
//			public static final String STGG_MAX_NUMBER="STGG.MAX.NUMBER";//社团骨干
//			public static final String PYXJ_MAX_NUMBER="PYXJ.MAX.NUMBER";//评优先进
//			public static final String TCGX_MAX_NUMBER="TCGX.MAX.NUMBER";//突出贡献
//
//			public static final String XKJS_MAX_NUMBER="XKJS.MAX.NUMBER";//学科竞赛
//
//			public static final String QXXHD_MAX_NUMBER="QXXHD.MAX.NUMBER";//全校性活动
//			public static final String FESTIVAL_MAX_NUMBER="5FESTIVAL.MAX.NUMBER";//5大节日
//			
			float xsgbCountFloat = 0;
			float stggCountFloat = 0;
			float pyxjpyCountFloat = 0;
			float pyxjxjCountFloat = 0;
			float tcgxCountFloat = 0;
			float qxxhdCountFloat = 0;
			for (String key : schoolFloatCount.keySet()) {
				float schoolfloat = schoolFloatCount.get(key);
				if(key.contains("学生干部")){
					xsgbCountFloat =xsgbCountFloat+schoolfloat;
				}else if(key.contains("社团骨干")){
					stggCountFloat =stggCountFloat+schoolfloat;
				}else if(key.contains("评优先进")){
					if(key.endsWith("-评优")){
						pyxjpyCountFloat =pyxjpyCountFloat+schoolfloat;
					}else if(key.endsWith("-先进")){
						pyxjxjCountFloat =pyxjxjCountFloat+schoolfloat;
					}
				}else if(key.contains("突出贡献")){
					tcgxCountFloat =tcgxCountFloat+schoolfloat;
				}else if(key.contains("全校性活动")){
					qxxhdCountFloat =qxxhdCountFloat+schoolfloat;
				}
			}
			if(maxValueMap.get(DyStuworkDataCountDto.XSGB_MAX_NUMBER)!=0){
				if(xsgbCountFloat>maxValueMap.get(DyStuworkDataCountDto.XSGB_MAX_NUMBER)){
					xsgbCountFloat = maxValueMap.get(DyStuworkDataCountDto.XSGB_MAX_NUMBER);
				}
			}
			if(maxValueMap.get(DyStuworkDataCountDto.STGG_MAX_NUMBER)!=0){
				if(stggCountFloat>maxValueMap.get(DyStuworkDataCountDto.STGG_MAX_NUMBER)){
					stggCountFloat = maxValueMap.get(DyStuworkDataCountDto.STGG_MAX_NUMBER);
				}
			}
			pyxjpyCountFloat =pyxjpyCountFloat+pyxjxjCountFloat;
			if(maxValueMap.get(DyStuworkDataCountDto.PYXJ_MAX_NUMBER)!=0){
				if(pyxjpyCountFloat>maxValueMap.get(DyStuworkDataCountDto.PYXJ_MAX_NUMBER)){
					pyxjpyCountFloat = maxValueMap.get(DyStuworkDataCountDto.PYXJ_MAX_NUMBER);
				}
//				if(>maxValueMap.get(DyStuworkDataCountDto.PYXJ_MAX_NUMBER)){
//					pyxjxjCountFloat = maxValueMap.get(DyStuworkDataCountDto.PYXJ_MAX_NUMBER);
//				}
			}
//			if(maxValueMap.get(DyStuworkDataCountDto.PYXJ_MAX_NUMBER)!=0){
//			}
			if(maxValueMap.get(DyStuworkDataCountDto.TCGX_MAX_NUMBER)!=0){
				if(tcgxCountFloat>maxValueMap.get(DyStuworkDataCountDto.TCGX_MAX_NUMBER)){
					tcgxCountFloat = maxValueMap.get(DyStuworkDataCountDto.TCGX_MAX_NUMBER);
				}
			}
			if(maxValueMap.get(DyStuworkDataCountDto.QXXHD_MAX_NUMBER)!=0){
				if(qxxhdCountFloat>maxValueMap.get(DyStuworkDataCountDto.QXXHD_MAX_NUMBER)){
					qxxhdCountFloat = maxValueMap.get(DyStuworkDataCountDto.QXXHD_MAX_NUMBER);
				}
			}
			float festivalFloat = 0;
			for (String key : festivalFloatCount.keySet()) {
				float periodFestivalFloat = festivalFloatCount.get(key);
				if(maxValuePer!=0){
					if(periodFestivalFloat>maxValuePer){
						periodFestivalFloat = maxValuePer;
					}
				}
				festivalFloat = festivalFloat+periodFestivalFloat;
			}
			if(maxValueMap.get(DyStuworkDataCountDto.FESTIVAL_MAX_NUMBER)!=0){
				if(festivalFloat>maxValueMap.get(DyStuworkDataCountDto.FESTIVAL_MAX_NUMBER)){
					festivalFloat = maxValueMap.get(DyStuworkDataCountDto.FESTIVAL_MAX_NUMBER);
				}
			}
			float allcount = gamecountFloat+xsgbCountFloat+stggCountFloat+pyxjpyCountFloat+
							tcgxCountFloat+qxxhdCountFloat+festivalFloat;
			
			if(allcount!=0){
				
				DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
				String p=decimalFormat.format(allcount);//format 返回的是字符串
				
				retunMap.put(student.getId(), Float.valueOf(p));
			}else{
				retunMap.put(student.getId(), allcount);
			}
		}
		
		return retunMap;
	}

	@Override
	public void setRecoverStuScore(String[] studentIds,Map<String,Integer> stuYearMap,String acadyear){
		//学生处罚
		List<DyStudentRewardPoint> points = dyStudentRewardPointDao.findByStudentIdIn(studentIds);
		if(CollectionUtils.isNotEmpty(points)){
			int nowYear= Integer.parseInt(acadyear.split("-")[0]);
			//直接获取统计后的数据
			Map<String,List<DyStudentRewardPoint>> pointListMap=points.stream().collect(Collectors.groupingBy(DyStudentRewardPoint::getStudentId));
			List<DyStudentRewardPoint> lastList=new ArrayList<>();
			for(Map.Entry<String, List<DyStudentRewardPoint>> entry:pointListMap.entrySet()){
				int year=stuYearMap.get(entry.getKey());
				List<DyStudentRewardPoint> inList=pointListMap.get(entry.getKey());
				for(DyStudentRewardPoint in:inList){
					//处理该学生休学前的老数据
					if(StringUtils.isNotBlank(in.getAcadyear()) && Integer.parseInt(in.getAcadyear().split("-")[0])+year <= nowYear) {
						in.setAcadyear(getAcadyear(in.getAcadyear(),year));
						lastList.add(in);
					}
				}
			}
			if(CollectionUtils.isNotEmpty(lastList)){
				this.saveAll(lastList.toArray(new DyStudentRewardPoint[0]));
			}
		}
	}
	public String getAcadyear(String acadyear,int l){
		return (Integer.parseInt(acadyear.split("-")[0])+l)+"-"+(Integer.parseInt(acadyear.split("-")[1])+l);
	}

	@Override
	public void deleteBySettingId(String settingId) {
		dyStudentRewardPointDao.deleteBySettingId(settingId);
	}
	
	
	@Override
	public void deleteByProjectId(String projectId) {
		dyStudentRewardPointDao.deleteByProjectId(projectId);
	}
	@Override
	public void updateIsDeteletd(String[] projectIds){
		dyStudentRewardPointDao.updateIsDeteletd(projectIds);
	}
	@Override
	public String saveImport(List<String[]> datas, String unitId, String classesType, String acadyear, String semester, String coverType) {
		
		Json importResultJson=new Json();
		List<String[]> errorDataList=new ArrayList<String[]>();
		int successCount  =0;
		String[] errorData=null;

		Set<String> delPointProIds = new HashSet<String>();
		List<DyStudentRewardPoint> savePoints = new ArrayList<DyStudentRewardPoint>();
		if(StuworkConstants.STUDENT_REWARD_OTHER.equals(classesType)) {
			delPointProIds.add(StuworkConstants.STUDENT_OTHER_ID);
			Map<String,String> stucodeMap = new HashMap<String,String>();
			for (String[] data : datas) {
				stucodeMap.put(data[1], data[0]);
			}
			List<Student> stuList = SUtils.dt(studentRemoteService.findBySchIdStudentCodes(unitId,  stucodeMap.keySet().toArray(new String[0])), new TR<List<Student>>() {});
			Map<String,Student> studentMap = new HashMap<String,Student>();
			for (Student student : stuList) {
				studentMap.put(student.getStudentCode(), student);
			}
			
			for (String[] data : datas) {
				if(StringUtils.isBlank(data[0])) {
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="学号";
					errorData[2]="";
					errorData[3]="学号不能为空";
					errorDataList.add(errorData);
					continue;
				}
				if(StringUtils.isBlank(data[1])) {
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="学号";
					errorData[2]="";
					errorData[3]="学号不能为空";
					errorDataList.add(errorData);
					continue;
				}
				
				if(StringUtils.isBlank(data[2])) {
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="项目名称";
					errorData[2]="";
					errorData[3]="项目名称能为空";
					errorDataList.add(errorData);
					continue;
				}else {
					if(data[2].length()>25){
						errorData = new String[4];
						errorData[0]=errorDataList.size()+1+"";
						errorData[1]="项目名称";
						errorData[2]=data[2];
						errorData[3]="项目名称长度不能超过25";
						errorDataList.add(errorData);
						continue;
					}
				}
				
				Student student = studentMap.get(data[1]);
				if(student!=null) {
					if(student.getStudentName().equals(data[0])) {
						
						DyStudentRewardPoint point = new DyStudentRewardPoint();
						point.setId(UuidUtils.generateUuid());
						point.setProjectId(StuworkConstants.STUDENT_OTHER_ID);
						point.setSettingId(StuworkConstants.STUDENT_OTHER_ID);
						point.setStudentId(student.getId());
						point.setUnitId(unitId);
						point.setRewardPoint(0);
						point.setCreationTime(new Date());
						point.setModifyTime(point.getCreationTime());
						point.setDeleted(false);
						point.setAcadyear(acadyear);
						point.setSemester(semester);
						point.setRemark(data[2]);
						point.setRewardCountPoint("0000");
						savePoints.add(point);
						successCount++;
					}else {
						errorData = new String[4];
						errorData[0]=errorDataList.size()+1+"";
						errorData[1]="学生姓名";
						errorData[2]=data[0];
						errorData[3]="学生姓名与学号不对应";
						errorDataList.add(errorData);
						continue;
					}
				}else {
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="学号";
					errorData[2]=data[1];
					errorData[3]="学号没有找到对应的学生";
					errorDataList.add(errorData);
					continue;
				}
			}
		}else {
			
			List< DyStudentRewardProject> projects = dyStudentRewardProjectService.findByClassesType(classesType, unitId);
			Map<String, DyStudentRewardProject> projectMap = new HashMap<String, DyStudentRewardProject>();
			for (DyStudentRewardProject dyStudentRewardProject : projects) {
				projectMap.put(dyStudentRewardProject.getId(), dyStudentRewardProject);
				if (!StuworkConstants.STUDENT_REWARD_FESTIVAL.equals(classesType)) {
					delPointProIds.add(dyStudentRewardProject.getId());// 覆盖导入中，非节日奖励类型的按学期全部清空
				}
			}

			List<DyStudentRewardSetting> settings = dyStudentRewardSettingService.findListByProjectIdsOrderByRewardGradeAndRewardLevel(projectMap.keySet().toArray(new String[0]), unitId);
			Map<String,DyStudentRewardSetting> settingMap =new HashMap<String, DyStudentRewardSetting>();
			Map<String,String> projectIdsMap = new HashMap<String,String>();
			// <rewardClass-rewardperiod, List<projectId>>
			Map<String, List<String>> delProIdMap = new HashMap<>();
			for (DyStudentRewardSetting setting : settings) {
				DyStudentRewardProject project = projectMap.get(setting.getProjectId());
				if(project!=null) {
					if(StuworkConstants.STUDENT_REWARD_GAME.equals(classesType)) {
						projectIdsMap.put(project.getRewardClasses()+"-"+project.getProjectName()+"-"+setting.getRewardGrade()+"-"+setting.getRewardLevel(), project.getId()+"-"+setting.getId());
					}else if(StuworkConstants.STUDENT_REWARD_SCHOOL.equals(classesType)) {
						projectIdsMap.put(project.getRewardClasses()+"-"+project.getProjectName(), project.getId()+"-"+setting.getId());
					}else if(StuworkConstants.STUDENT_REWARD_FESTIVAL.equals(classesType)) {
						projectIdsMap.put(project.getRewardClasses()+"-"+project.getProjectName()+"-"+setting.getRewardLevel()+"-"+project.getRewardPeriod(), project.getId()+"-"+setting.getId());
						List<String> pids = delProIdMap.get(project.getRewardClasses()+"-"+project.getRewardPeriod());
						if(pids == null){
							pids = new ArrayList<>();
						}
						pids.add(project.getId());
						delProIdMap.put(project.getRewardClasses()+"-"+project.getRewardPeriod(), pids);
					}


					settingMap.put(setting.getId(), setting);
				}
			}

			
			
			
			Map<String,String> stucodeMap = new HashMap<String,String>();
			for (String[] data : datas) {
				stucodeMap.put(data[1], data[0]);
				
			}
			List<Student> stuList = SUtils.dt(studentRemoteService.findBySchIdStudentCodes(unitId,  stucodeMap.keySet().toArray(new String[0])), new TR<List<Student>>() {});
			Map<String,Student> studentMap = new HashMap<String,Student>();
			for (Student student : stuList) {
				studentMap.put(student.getStudentCode(), student);
			}
			
			for (String[] data : datas) {
				if(StringUtils.isBlank(data[0])) {
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="学号";
					errorData[2]="";
					errorData[3]="学号不能为空";
					errorDataList.add(errorData);
					continue;
				}
				if(StringUtils.isBlank(data[1])) {
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="学号";
					errorData[2]="";
					errorData[3]="学号不能为空";
					errorDataList.add(errorData);
					continue;
				}
				
//				if(StuworkConstants.STUDENT_REWARD_GAME.equals(classesType)) {
//					
//				}else if(StuworkConstants.STUDENT_REWARD_SCHOOL.equals(classesType)) {
//					
//					
//				}else if(StuworkConstants.STUDENT_REWARD_FESTIVAL.equals(classesType)) {
//					
//				}
				
				
				if(StuworkConstants.STUDENT_REWARD_GAME.equals(classesType)) {
					
					if(StringUtils.isBlank(data[2])) {
						errorData = new String[4];
						errorData[0]=errorDataList.size()+1+"";
						errorData[1]="类型";
						errorData[2]="";
						errorData[3]="类型不能为空";
						errorDataList.add(errorData);
						continue;
					}
					if(StringUtils.isBlank(data[3])) {
						errorData = new String[4];
						errorData[0]=errorDataList.size()+1+"";
						errorData[1]="项目名称";
						errorData[2]="";
						errorData[3]="项目名称不能为空";
						errorDataList.add(errorData);
						continue;
					}
					if(StringUtils.isBlank(data[4])) {
						errorData = new String[4];
						errorData[0]=errorDataList.size()+1+"";
						errorData[1]="级别";
						errorData[2]="";
						errorData[3]="级别不能为空";
						errorDataList.add(errorData);
						continue;
					}
					if(StringUtils.isBlank(data[5])) {
						errorData = new String[4];
						errorData[0]=errorDataList.size()+1+"";
						errorData[1]="奖级";
						errorData[2]="";
						errorData[3]="奖级不能为空";
						errorDataList.add(errorData);
						continue;
					}
					if(StringUtils.isBlank(data[6])) {
//						errorData = new String[4];
//						errorData[0]=errorDataList.size()+1+"";
//						errorData[1]="分值";
//						errorData[2]="";
//						errorData[3]="分值不能为空";
//						errorDataList.add(errorData);
//						continue;
					}else {
						Pattern pattern = Pattern.compile("^[+]?([0-9]+(.)?+([0-9]{1})?+([0-9]{1})?)$");
						boolean wsFlag=pattern.matcher(data[6]).matches();
						if(wsFlag) {
							if(data[6].length()>6) {
								errorData = new String[4];
								errorData[0]=errorDataList.size()+1+"";
								errorData[1]="分值";
								errorData[2]=data[6];
								errorData[3]="分值不能超过999";
								errorDataList.add(errorData);
								continue;

							}
						}else {
							errorData = new String[4];
							errorData[0]=errorDataList.size()+1+"";
							errorData[1]="分值";
							errorData[2]=data[6];
							errorData[3]="分值最多是2位小数";
							errorDataList.add(errorData);
							continue;
						}
					}
				
				}else if(StuworkConstants.STUDENT_REWARD_SCHOOL.equals(classesType)) {
				
					if(StringUtils.isBlank(data[2])) {
						errorData = new String[4];
						errorData[0]=errorDataList.size()+1+"";
						errorData[1]="类型";
						errorData[2]="";
						errorData[3]="类型不能为空";
						errorDataList.add(errorData);
						continue;
					}
					if(StringUtils.isBlank(data[3])) {
						errorData = new String[4];
						errorData[0]=errorDataList.size()+1+"";
						errorData[1]="项目名称";
						errorData[2]="";
						errorData[3]="项目名称不能为空";
						errorDataList.add(errorData);
						continue;
					}
					
					if(StringUtils.isBlank(data[4])) {
//						errorData = new String[4];
//						errorData[0]=errorDataList.size()+1+"";
//						errorData[1]="分值";
//						errorData[2]="";
//						errorData[3]="分值不能为空";
//						errorDataList.add(errorData);
//						continue;
					}else {
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
				}else if(StuworkConstants.STUDENT_REWARD_FESTIVAL.equals(classesType)) {
					if(StringUtils.isBlank(data[2])) {
						errorData = new String[4];
						errorData[0]=errorDataList.size()+1+"";
						errorData[1]="类型";
						errorData[2]="";
						errorData[3]="类型不能为空";
						errorDataList.add(errorData);
						continue;
					}
					if(StringUtils.isBlank(data[3])) {
						errorData = new String[4];
						errorData[0]=errorDataList.size()+1+"";
						errorData[1]="项目名称";
						errorData[2]="";
						errorData[3]="项目名称不能为空";
						errorDataList.add(errorData);
						continue;
					}
			
					if(StringUtils.isBlank(data[4])) {
						errorData = new String[4];
						errorData[0]=errorDataList.size()+1+"";
						errorData[1]="奖级";
						errorData[2]="";
						errorData[3]="奖级不能为空";
						errorDataList.add(errorData);
						continue;
					}
					if(StringUtils.isBlank(data[6])) {
//						errorData = new String[4];
//						errorData[0]=errorDataList.size()+1+"";
//						errorData[1]="分值";
//						errorData[2]="";
//						errorData[3]="分值不能为空";
//						errorDataList.add(errorData);
//						continue;
					}else {
						Pattern pattern = Pattern.compile("^[+]?([0-9]+(.)?+([0-9]{1})?+([0-9]{1})?)$");
						boolean wsFlag=pattern.matcher(data[6]).matches();
						if(wsFlag) {
							if(data[6].length()>6) {
								errorData = new String[4];
								errorData[0]=errorDataList.size()+1+"";
								errorData[1]="分值";
								errorData[2]=data[6];
								errorData[3]="分值不能超过999";
								errorDataList.add(errorData);
								continue;

							}
						}else {
							errorData = new String[4];
							errorData[0]=errorDataList.size()+1+"";
							errorData[1]="分值";
							errorData[2]=data[6];
							errorData[3]="分值最多是2位小数";
							errorDataList.add(errorData);
							continue;
						}
					}
					if(StringUtils.isBlank(data[5])) {
						errorData = new String[4];
						errorData[0]=errorDataList.size()+1+"";
						errorData[1]="年份";
						errorData[2]="";
						errorData[3]="年份不能为空";
						errorDataList.add(errorData);
						continue;
					}else {
//						Pattern pattern = Pattern.compile("^[+]?([0-9])$");
//						Pattern pattern = Pattern.compile("^[+]?([0-9]+)$");
						Pattern pattern = Pattern.compile("^[0-9]\\d{0,3}$");
						boolean wsFlag=pattern.matcher(data[5]).matches();
						if(wsFlag) {
							int a = Integer.parseInt(data[5]);
							if(a<=0) {
								errorData = new String[4];
								errorData[0]=errorDataList.size()+1+"";
								errorData[1]="年份";
								errorData[2]=data[5];
								errorData[3]="年份必须大于0";
								errorDataList.add(errorData);
								continue;
							}
						}else {
							errorData = new String[4];
							errorData[0]=errorDataList.size()+1+"";
							errorData[1]="年份";
							errorData[2]=data[5];
							errorData[3]="年份必须是整数且不大于4位数字";
							errorDataList.add(errorData);
							continue;
						}
					}
				}
				
				Student student = studentMap.get(data[1]);
				if(student!=null) {
					if(student.getStudentName().equals(data[0])) {
						
						String key="";
						// +"-"+
						if(StuworkConstants.STUDENT_REWARD_GAME.equals(classesType)) {
							key= data[2].trim()+"-"+data[3].trim()+"-"+data[4].trim()+"-"+data[5].trim();
						}else if(StuworkConstants.STUDENT_REWARD_SCHOOL.equals(classesType)) {
							key=data[2].trim()+"-"+data[3].trim();
						}else if(StuworkConstants.STUDENT_REWARD_FESTIVAL.equals(classesType)) {
							key =  data[2].trim()+"-"+data[3].trim()+"-"+data[4].trim()+"-"+data[5].trim();
						}

						
						String projectIdStr = projectIdsMap.get(key);
						if(StringUtils.isNotBlank(projectIdStr)) {
							if(StuworkConstants.STUDENT_REWARD_FESTIVAL.equals(classesType)
							&& delProIdMap.containsKey(data[2].trim()+"-"+data[5].trim())) {// 覆盖导入：节日奖励中，按节日+年份来清空数据
								delPointProIds.addAll(delProIdMap.get(data[2].trim()+"-"+data[5].trim()));
								delProIdMap.remove(data[2].trim()+"-"+data[5].trim());
							}
							String[] ids = projectIdStr.split("-");
							DyStudentRewardProject project = projectMap.get(ids[0]);
							DyStudentRewardSetting setting = settingMap.get(ids[1]);
							DyStudentRewardPoint point = new DyStudentRewardPoint();
							point.setId(UuidUtils.generateUuid());
							point.setProjectId(project.getId());
							point.setSettingId(setting.getId());
							point.setStudentId(student.getId());
							point.setUnitId(unitId);
							point.setCreationTime(new Date());
							point.setModifyTime(point.getCreationTime());
							point.setDeleted(false);
							
							if(StuworkConstants.STUDENT_REWARD_GAME.equals(classesType)) {
								if(StringUtils.isNotBlank(setting.getRewardPoint())){
									point.setRewardPoint(Float.parseFloat(setting.getRewardPoint()));
								}else {
									if(StringUtils.isBlank(data[6])) {
										errorData = new String[4];
										errorData[0]=errorDataList.size()+1+"";
										errorData[1]="分值";
										errorData[2]="";
										errorData[3]="分值不能为空";
										errorDataList.add(errorData);
										continue;
									}
									point.setRewardPoint(Float.parseFloat(data[6]));
								}
								
								point.setAcadyear(acadyear);
								point.setSemester(semester);
								point.setRewardCountPoint("all");
								point.setRemark(data[7]);
								savePoints.add(point);
								successCount++;
							}else if(StuworkConstants.STUDENT_REWARD_SCHOOL.equals(classesType)) {
								if(StringUtils.isNotBlank(setting.getRewardPoint())){
									point.setRewardPoint(Float.parseFloat(setting.getRewardPoint()));
								}else {
									if(StringUtils.isBlank(data[4])) {
										errorData = new String[4];
										errorData[0]=errorDataList.size()+1+"";
										errorData[1]="分值";
										errorData[2]="";
										errorData[3]="分值不能为空";
										errorDataList.add(errorData);
										continue;
									}
									point.setRewardPoint(Float.parseFloat(data[4]));
								}
								point.setAcadyear(acadyear);
								point.setSemester(semester);
								point.setRewardCountPoint(data[2]);
								point.setRemark(data[5]);
								savePoints.add(point);
								successCount++;
							}else if(StuworkConstants.STUDENT_REWARD_FESTIVAL.equals(classesType)) {
								if(StringUtils.isNotBlank(setting.getRewardPoint())){
									point.setRewardPoint(Float.parseFloat(setting.getRewardPoint()));
								}else {
									if(StringUtils.isBlank(data[6])) {
										errorData = new String[4];
										errorData[0]=errorDataList.size()+1+"";
										errorData[1]="分值";
										errorData[2]="";
										errorData[3]="分值不能为空";
										errorDataList.add(errorData);
										continue;
									}
									point.setRewardPoint(Float.parseFloat(data[6]));
								}
								point.setAcadyear(project.getAcadyear());
								point.setSemester(project.getSemester()+"");
								point.setRewardCountPoint(project.getRewardPeriod()+"");
								point.setRemark(data[7]);
								savePoints.add(point);
								successCount++;
							}
							
							
						}else {
							errorData = new String[4];
							errorData[0]=errorDataList.size()+1+"";
							errorData[1]="项目名称";
							errorData[2]="";
							errorData[3]="找不到对应的项目";
							errorDataList.add(errorData);
							continue;
						}
						
						
					}else {
						errorData = new String[4];
						errorData[0]=errorDataList.size()+1+"";
						errorData[1]="学生姓名";
						errorData[2]=data[0];
						errorData[3]="学生姓名与学号不对应";
						errorDataList.add(errorData);
						continue;
					}
				}else {
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="学号";
					errorData[2]=data[1];
					errorData[3]="学号没有找到对应的学生";
					errorDataList.add(errorData);
					continue;
				}
			}
		}

		if ("1".equals(coverType) && !delPointProIds.isEmpty()) {// 覆盖导入清空数据
			if (StuworkConstants.STUDENT_REWARD_FESTIVAL.equals(classesType)) {
				dyStudentRewardPointDao.deleteByProjectIds(delPointProIds.toArray(new String[0]));
			} else {
				dyStudentRewardPointDao.deleteByAcadyearSemesterProjectIds(acadyear, semester, delPointProIds.toArray(new String[0]));
			}
		}

		if(CollectionUtils.isNotEmpty(savePoints)) {
			saveAll(savePoints.toArray(new DyStudentRewardPoint[0]));
		}
		
		importResultJson.put("totalCount", datas.size());
		importResultJson.put("successCount", successCount);
		importResultJson.put("errorCount", errorDataList.size());
		importResultJson.put("errorData", errorDataList);
		return importResultJson.toJSONString();
	}
	@Override
	public void addOtherPoint(String remark, String stuId, String acadyear, String semester, String unitId) {
		String[] stuIds = stuId.split(",");
		List<DyStudentRewardPoint> savePoints = new ArrayList<DyStudentRewardPoint>();
		for (String stu : stuIds) {
			DyStudentRewardPoint point = new DyStudentRewardPoint();
			point.setId(UuidUtils.generateUuid());
			point.setProjectId(StuworkConstants.STUDENT_OTHER_ID);
			point.setSettingId(StuworkConstants.STUDENT_OTHER_ID);
			point.setStudentId(stu);
			point.setUnitId(unitId);
			point.setRewardPoint(0);
			point.setCreationTime(new Date());
			point.setModifyTime(point.getCreationTime());
			point.setDeleted(false);
			point.setAcadyear(acadyear);
			point.setSemester(semester);
			point.setRemark(remark);
			point.setRewardCountPoint("0000");
			savePoints.add(point);
		}
		
		saveAll(savePoints.toArray(new DyStudentRewardPoint[0]));
		
	}

	@Override
	public Map<String, DyStuworkDataCountDto> findStuworkCountByStudentIds(Map<String, Integer> maxValueMap, String classId,
																		   String[] studentIds, Integer maxValuePer, boolean isShow) {
		Map<String, DyStuworkDataCountDto> dtoMap = new HashMap<String, DyStuworkDataCountDto>();
		Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId),
				Clazz.class);
		String unitId = clazz.getSchoolId();
		// float countNamber = 0;
		if (clazz != null) {
			// 判断是否是高中
			if (clazz.getSection() == 3 ||clazz.getSection() == 9) {
				String[] acadyears = new String[4];
				acadyears = createAcadyear(clazz.getAcadyear(), isShow);
				List<DyStudentRewardPoint> pointsList = dyStudentRewardPointDao
						.findByAcadyear(unitId, studentIds, acadyears);
				acadyears = createAcadyear(clazz.getAcadyear(), true);
				Map<String, List<DyStudentRewardPoint>> listMap = EntityUtils.getListMap(pointsList, DyStudentRewardPoint::getStudentId, Function.identity());
				for (String studentId : studentIds) {
					DyStuworkDataCountDto dto = new DyStuworkDataCountDto();
					List<DyStudentRewardPoint> points = listMap.get(studentId);
					makeDto(maxValueMap, maxValuePer, dto, acadyears, points);
					dtoMap.put(studentId, dto);
				}
			}
		}
		return dtoMap;
	}

	@Override
	public Map<String, String> findXkjsByStudentIds(String unitId, String acadyear, String semester, String[] studentIds) {
		List<DyStudentRewardProject> projectList = dyStudentRewardProjectService.findByClassesType(StuworkConstants.STUDENT_REWARD_GAME, unitId);
		Map<String, DyStudentRewardProject> projectMap = EntityUtils.getMap(projectList, DyStudentRewardProject::getId);
		String[] projectIds = EntityUtils.getList(projectList, DyStudentRewardProject::getId).toArray(new String[projectList.size()]);
		List<DyStudentRewardPoint> pointList = dyStudentRewardPointDao.findByStudentIds(unitId, acadyear, semester, studentIds,projectIds);
		Map<String, List<DyStudentRewardPoint>> pointMap = EntityUtils.getListMap(pointList, DyStudentRewardPoint::getStudentId, Function.identity());
		Map<String, String> returnMap = new HashMap<String, String>();
		for (String studentId : studentIds) {
			List<DyStudentRewardPoint> tempList = pointMap.get(studentId);
			if(CollectionUtils.isNotEmpty(tempList)){
				Float score = 0f;
				Map<String, List<DyStudentRewardPoint>> tempMap = EntityUtils.getListMap(tempList, DyStudentRewardPoint::getProjectId, Function.identity());
				Map<String, Float> gameFloatCount = new HashMap<String, Float>();
				for (Entry<String, List<DyStudentRewardPoint>> entry : tempMap.entrySet()) {
					DyStudentRewardProject project = projectMap.get(entry.getKey());
					String key = project.getRewardClasses()+ "-" + project.getProjectName();
					for (DyStudentRewardPoint point : entry.getValue()) {
						Float gamenumber = gameFloatCount.get(key);
						// 特殊处理
						if (gamenumber != null && gamenumber > point.getRewardPoint()) {
							
						} else {
							gameFloatCount.put(key,point.getRewardPoint());
						}
					}
				}
				score = gameFloatCount.values().stream().reduce(0f,Float::sum);
				returnMap.put(studentId, score+"");
			}else{
				returnMap.put(studentId, "");
			}
		}
		return returnMap;
	}
	
}
