package net.zdsoft.savedata.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.constant.BaseSaveConstant;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.SubSchool;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.savedata.entity.RelationBase;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;

/**
 * 基础数据保存数据的接口
 * @author yangsj  2018年7月24日下午5:34:07
 */
@Controller
@RequestMapping(value = { "/remote/openapi/sync", "/openapi/sync" })
public class BaseSyncSaveAction extends BaseSyncAction {

	private Logger logger = LoggerFactory.getLogger(BaseSyncSaveAction.class);
	
	@ResponseBody
	@RequestMapping(value = "eduUnit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addEduUnit(@RequestBody String jsonStr) {
		try {
			List<Unit> saveUnit = new ArrayList<>();
			List<RelationBase> saveRBs = new ArrayList<>();
			if(StringUtils.isNotBlank(jsonStr)) {
				String eduList = doExcuteDate(jsonStr);
				if(StringUtils.isNotBlank(eduList)) {
					List<Unit> eduUnit =  JSONArray.parseArray(eduList, Unit.class);
					if(CollectionUtils.isNotEmpty(eduUnit)) {
						//截取数据
						eduUnit = getLimitData(eduUnit);
						Set<String> idList = EntityUtils.getSet(eduUnit, Unit::getId);
						Map<String, Unit> allUnitMap = getUnitMap(idList);
						Set<String> regionCodeList =  EntityUtils.getSet(eduUnit, Unit::getRegionCode);
						Map<String, Unit> eduMap = new HashMap<>();
						Map<String, Unit> lecelEduMap = new HashMap<>();
						if(CollectionUtils.isNotEmpty(regionCodeList)) {
							Set<String> unionCodeList = new HashSet<>();
							Set<String> endRcList = new HashSet<>();
							regionCodeList.forEach(re->{
								if(re.length() == 9) 
									unionCodeList.add(re);
								if(re.length() == 6) 
									endRcList.add(re);
							});
							if(CollectionUtils.isNotEmpty(endRcList)) {
								List<Unit> edus = SUtils.dt(unitRemoteService.findByUnitClassAndRegion(Unit.UNIT_CLASS_EDU, 
										regionCodeList.toArray(new String[0])), new TR<List<Unit>>() {
								});
								eduMap = EntityUtils.getMap(edus, Unit::getRegionCode);
							}
							if(CollectionUtils.isNotEmpty(unionCodeList)) {
								List<Unit> lecelEdus = SUtils.dt(unitRemoteService.findByUnitClassAndUnionCode(Unit.UNIT_CLASS_EDU, 
										unionCodeList.toArray(new String[0])), new TR<List<Unit>>() {
								});
								lecelEduMap = EntityUtils.getMap(lecelEdus, Unit::getUnionCode);
							}
						}
						StringBuilder detailError = new StringBuilder().append("详细错误:");
						StringBuilder errorIdBuilder = new StringBuilder();
						Map<String, String> baseIdMap = new HashMap<>();
						int errNum = 0;
						int regionCodeErr = 0;
						for (Unit unit : eduUnit) {
							String id =  unit.getId();
							String regionCode = unit.getRegionCode();
							Integer unitType = unit.getUnitType();
							String unionCode = unit.getUnionCode();
													
							if(unitType != null && (unit.getUnitType() != Unit.UNIT_EDU_TOP && unit.getUnitType() != Unit.UNIT_EDU_SUB)) {
								errNum ++;
								errorIdBuilder.append(id + ",");
								continue;
							}
							if(StringUtils.isNotBlank(id) && StringUtils.isNotBlank(regionCode)) {
								if(regionCode.length() == 9) {
									if(lecelEduMap != null && lecelEduMap.get(id)!= null) {
										String oldUnionCode = lecelEduMap.get(id).getUnionCode();
										if(!regionCode.equals(oldUnionCode)) {
											regionCodeErr ++;
											errorIdBuilder.append(id + ",");
											continue;
										}
									}else {
										if(isErrorData(regionCode,lecelEduMap,Boolean.FALSE)) {
											regionCodeErr ++;
											errorIdBuilder.append(id + ",");
											continue;
										}
									}
								}else if(regionCode.length() == 6) {
									if(allUnitMap != null && allUnitMap.get(id)!= null) {
										String oldRegionCode = allUnitMap.get(id).getRegionCode();
										if(!regionCode.equals(oldRegionCode)) {
											regionCodeErr ++;
											errorIdBuilder.append(id + ",");
											continue;
										}
									}else {
										if(isErrorData(regionCode,eduMap,Boolean.FALSE)) {
											regionCodeErr ++;
											errorIdBuilder.append(id + ",");
											continue;
										}
									}
								}else {
									regionCodeErr ++;
									errorIdBuilder.append(id + ",");
									continue;
								}
								
								if(StringUtils.isBlank(unionCode)){
									if(regionCode.length() == 9) {
										unionCode = regionCode;
										regionCode = StringUtils.substring(regionCode, 0, 6);
										unit.setRegionCode(regionCode);
									}else if(StringUtils.endsWith(regionCode, "0000") ) {
										unionCode = StringUtils.substring(regionCode, 0, 2);	
									}else if (StringUtils.endsWith(regionCode, "00")) {
										unionCode = StringUtils.substring(regionCode, 0, 4);
									}else {
										unionCode = regionCode;
									}
								}
								unit.setUnionCode(unionCode);
							}
							unit.setUnitClass(Unit.UNIT_CLASS_EDU);
							if(allUnitMap != null && allUnitMap.get(id) != null) {
								unit.setCreationTime(allUnitMap.get(id).getCreationTime());
								unit.setModifyTime(new Date());
								unit.setId(allUnitMap.get(id).getId());
							}else {
								unit.setId(getBaseId(saveRBs,id,BaseSaveConstant.BASE_UNIT_TYPE));
							}
							if(!getIsRelation())
							   baseIdMap.put(unit.getId(), id);
							saveUnit.add(unit);
						}
						detailError = detailError.append("unitType取的值不符合"+errNum+"条数据;regionCode已经存在或和主键id不一致或长度不符合条件"+regionCodeErr+"条数据;");
						Map<String,String> returnMsg = new HashMap<>();
						if(CollectionUtils.isNotEmpty(saveUnit)) {
							baseSyncSaveService.saveUnit(saveUnit.toArray(new Unit[saveUnit.size()]),returnMsg,Boolean.TRUE);
						}
						return getReturnMsg(eduUnit.size(),errNum+regionCodeErr,detailError,returnMsg,saveRBs,errorIdBuilder,baseIdMap);
					}
				}
			}
			return returnSuccess();
		} catch (Exception e) {
			logger.error("保存教育局数据失败,错误信息是：-------"+e.getMessage());
			return returnMsg(e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "school", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addSchoolAndUnit(@RequestBody String jsonStr) {
		try {
			List<School> saveSchool = new ArrayList<>();
			List<RelationBase> saveRBs = new ArrayList<>();
			if(StringUtils.isNotBlank(jsonStr)) {
				//先进行数据的解析
				String date = doExcuteDate(jsonStr);
				if(StringUtils.isNotBlank(date)) {
					List<School> schools =  JSONArray.parseArray(date, School.class);
					if(CollectionUtils.isNotEmpty(schools)) {
						//截取数据
						schools = getLimitData(schools); 
						//----用到的参数
						StringBuilder detailError = new StringBuilder().append("详细错误:");
						StringBuilder errorIdBuilder = new StringBuilder();					
						Map<String, String> baseIdMap = new HashMap<>();
						int errNum = 0;
						Set<String> schoolIds = EntityUtils.getSet(schools, School::getId);
						Map<String, School> allSchoolMap = getSchoolMap(schoolIds);
						Map<String, List<Grade>> gradeMap = new HashMap<>();
						if(CollectionUtils.isNotEmpty(schoolIds)) {
							Set<String> baseIdSet = getRelationSet(schoolIds,BaseSaveConstant.BASE_SCHOOL_TYPE);
							String[] ids = baseIdSet.toArray(new String[baseIdSet.size()]);
							gradeMap = SUtils.dt(gradeRemoteService.findBySchoolIdMap(ids),
									new TypeReference<Map<String, List<Grade>>>(){});
						}
						for (School school : schools) {
							String id = school.getId();
							Integer isDeleted = school.getIsDeleted();
							if(isDeleted != null && isDeleted == 1 && gradeMap != null && CollectionUtils.isNotEmpty(gradeMap.get(school.getId()))) {
								errorIdBuilder.append(id + ",");
								errNum++;
								continue;
							}
							if(allSchoolMap != null && allSchoolMap.get(id) != null) {
								school.setCreationTime(allSchoolMap.get(id).getCreationTime());
								school.setModifyTime(new Date());
								school.setId(allSchoolMap.get(id).getId());
							}else {
								school.setId(getBaseId(saveRBs,id,BaseSaveConstant.BASE_SCHOOL_TYPE));
							}
							if(!getIsRelation())
							    baseIdMap.put(school.getId(), id);
							saveSchool.add(school);
						}
						detailError = detailError.append("该学校下面还有年级，不能删除"+errNum+"条数据;");
						Map<String,String> returnMsg = new HashMap<>();
						if(CollectionUtils.isNotEmpty(saveSchool)) {
							baseSyncSaveService.saveSchool(saveSchool.toArray(new School[saveSchool.size()]),returnMsg,Boolean.TRUE);
						}
						
//						return getReturnMsg(schools.size(),errNum+errorField,detailError.toString(),saveRBs);
						return getReturnMsg(schools.size(),errNum,detailError,returnMsg,saveRBs,errorIdBuilder,baseIdMap);
					}
				}
			}
			return returnSuccess();
		} catch (Exception e) {
			logger.error("保存学校数据失败,错误信息是：-------"+e.getMessage());
			return returnMsg(e);
		}
	}


	@ResponseBody
	@RequestMapping(value = "subSchool", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addSubSchool(@RequestBody String jsonStr) {
		try {
			List<SubSchool> saveSubSchool = new ArrayList<>();
			List<RelationBase> saveRBs = new ArrayList<>();
			if(StringUtils.isNotBlank(jsonStr)) {
				String dataList = doExcuteDate(jsonStr);
				if(StringUtils.isNotBlank(dataList)) {
					List<SubSchool> subSchool =  JSONArray.parseArray(dataList, SubSchool.class);
					if(CollectionUtils.isNotEmpty(subSchool)) {
						//截取数据
						subSchool = getLimitData(subSchool); 
						Set<String> ids = EntityUtils.getSet(subSchool, SubSchool::getId);
						Set<String> schoolIds =  EntityUtils.getSet(subSchool, SubSchool::getSchoolId);
						//获取验证的map 
						Map<String, School> allSchoolMap = getSchoolMap(schoolIds);
						Map<String, SubSchool> allSubMap = getSubSchoolMap(ids);
						StringBuilder detailError = new StringBuilder().append("详细错误:");
						StringBuilder errorIdBuilder = new StringBuilder();
						Map<String, String> baseIdMap = new HashMap<>();
						
						int errNum = 0;
						for (SubSchool ss : subSchool) {
							String id = ss.getId();
							String schoolId = ss.getSchoolId();
							if(isErrorData(schoolId,allSchoolMap)) {
								errNum++;
								errorIdBuilder.append(id+",");
								continue;
							}
							if(allSubMap != null && allSubMap.get(id) != null) {
								ss.setUpdatestamp(new Date().getTime());
								ss.setId(allSubMap.get(id).getId());
							}else {
								ss.setId(getBaseId(saveRBs,id,BaseSaveConstant.BASE_SUBSCHOOL_TYPE));
							}
							if(StringUtils.isNotBlank(schoolId)) 
							  ss.setSchoolId(allSchoolMap.get(schoolId).getId());
							if(!getIsRelation())
							    baseIdMap.put(ss.getId(), id);
							saveSubSchool.add(ss);
						}
						detailError = detailError.append("学校不存在的"+errNum+"条数据;");
						Map<String,String> returnMsg = new HashMap<>();
						if(CollectionUtils.isNotEmpty(saveSubSchool)) {
							baseSyncSaveService.saveSubSchool(saveSubSchool.toArray(new SubSchool[saveSubSchool.size()]),returnMsg);
						}
						return getReturnMsg(subSchool.size(),errNum,detailError,returnMsg,saveRBs,errorIdBuilder,baseIdMap);
//						return getReturnMsg(subSchool.size(),errNum,detailError,returnMsg,saveRBs);
					}
				}
			}
			return returnSuccess();
		} catch (Exception e) {
			logger.error("保存校区数据失败,错误信息是：-------"+e.getMessage());
			return returnMsg(e);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "semester", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addSemester(@RequestBody String jsonStr) {
		try {
			List<Semester> saveSemester = new ArrayList<>();
			if(StringUtils.isNotBlank(jsonStr)) {
				String dataList = doExcuteDate(jsonStr);
				if(StringUtils.isNotBlank(dataList)) {
					List<Semester> semesters =  JSONArray.parseArray(dataList, Semester.class);
					if(CollectionUtils.isNotEmpty(semesters)) {
						//截取数据
						semesters = getLimitData(semesters); 
						//获取验证的map 已经存在的学年学期不能添加
						List<Semester> allSemester = SUtils.dt(semesterRemoteService.findAll(), new TR<List<Semester>>() {});
						allSemester = allSemester.stream().filter(t -> t.getIsDeleted() == 0).collect(Collectors.toList());
						Map<String, Semester> acadSemeMap = new HashMap<String, Semester>();
						for (Semester semester : allSemester) {
						  String key = semester.getAcadyear()+"-"+String.valueOf(semester.getSemester());
					      acadSemeMap.put(key, semester);
						}
						StringBuilder detailError = new StringBuilder().append("详细错误:");
						StringBuilder errorIdBuilder = new StringBuilder();
						int errNum = 0;
						for (Semester semester : semesters) {
							String  ac = semester.getAcadyear();
							Integer se =semester.getSemester();
							if(StringUtils.isNotBlank(ac) && se != null) {
								String key = ac+"-"+String.valueOf(se);
								if(StringUtils.isNotBlank(key) && acadSemeMap.get(key) != null) {
									errorIdBuilder.append(semester.getId()+",");
									errNum++;
									continue;
								}
							}
							saveSemester.add(semester);
						}
						detailError = detailError.append("学年学期的信息已经存在"+errNum+"条数据;");
						Map<String,String> returnMsg = new HashMap<>();
						if(CollectionUtils.isNotEmpty(saveSemester)) {
							baseSyncSaveService.saveSemester(saveSemester.toArray(new Semester[saveSemester.size()]),returnMsg);
						}
						return getReturnMsg(semesters.size(),errNum,detailError,returnMsg,null,errorIdBuilder);
					}
				}
			}
			return returnSuccess();
		} catch (Exception e) {
			logger.error("保存学年学期数据失败,错误信息是：-------"+e.getMessage());
			return returnMsg(e);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "dept", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addDept(@RequestBody String jsonStr) {
		try {
			List<Dept> saveDept = new ArrayList<>();
			List<RelationBase> saveRBs = new ArrayList<>();
			if(StringUtils.isNotBlank(jsonStr)) {
				String dataList = doExcuteDate(jsonStr);
				if(StringUtils.isNotBlank(dataList)) {
					List<Dept> depts =  JSONArray.parseArray(dataList, Dept.class);
					if(CollectionUtils.isNotEmpty(depts)) {
						//截取数据
						depts = getLimitData(depts); 
						//获取验证的map 已经存在的学年学期不能添加
						Set<String> ids = EntityUtils.getSet(depts, Dept::getId);
						Set<String> unitIds = EntityUtils.getSet(depts, Dept::getUnitId);
						Map<String, Dept> deptMap = getDeptMap(ids);
						Map<String, Unit> allUnitMap = getUnitMap(unitIds);
						StringBuilder detailError = new StringBuilder().append("详细错误:");
						StringBuilder errorIdBuilder = new StringBuilder();
						Map<String, String> baseIdMap = new HashMap<>();
						
						int errNum = 0;
						if(allUnitMap == null) {
							errNum = depts.size();
						}else {
							for (Dept dept : depts) {
								String id = dept.getId();
								String unitId = dept.getUnitId();
								if(isErrorData(unitId,allUnitMap)) {
									errorIdBuilder.append(id+",");
									errNum++;
									continue;
								}
								if(deptMap != null && deptMap.get(id) != null) {
									dept.setCreationTime(deptMap.get(id).getCreationTime());
									dept.setModifyTime(new Date());
									dept.setId(deptMap.get(id).getId());
								}else {
									dept.setId(getBaseId(saveRBs,id,BaseSaveConstant.BASE_DEPT_TYPE));
								}
								if(StringUtils.isNotBlank(unitId)) 
								   dept.setUnitId(allUnitMap.get(unitId).getId());
								if(!getIsRelation())
								    baseIdMap.put(dept.getId(), id);
								saveDept.add(dept);
							}
						}
						detailError = detailError.append("部门中的单位不存在"+errNum+"条数据;");
						Map<String,String> returnMsg = new HashMap<>();
						if(CollectionUtils.isNotEmpty(saveDept)) {
							baseSyncSaveService.saveDept(saveDept.toArray(new Dept[saveDept.size()]),returnMsg);
						}
						return getReturnMsg(depts.size(),errNum,detailError,returnMsg,saveRBs,errorIdBuilder,baseIdMap);
					}
				}
			}
			return returnSuccess();
		} catch (Exception e) {
			logger.error("保存部门数据失败,错误信息是：-------"+e.getMessage());
			return returnMsg(e);
		}
	}
	

	@ResponseBody
	@RequestMapping(value = "teacher", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addTeacher(@RequestBody String jsonStr) {
		try {
			List<Teacher> saveTeacher = new ArrayList<>();
			List<RelationBase> saveRBs = new ArrayList<>();
			if(StringUtils.isNotBlank(jsonStr)) {
				String dataList = doExcuteDate(jsonStr);
				if(StringUtils.isNotBlank(dataList)) {
					List<Teacher> teachers =  JSONArray.parseArray(dataList, Teacher.class);
					if(CollectionUtils.isNotEmpty(teachers)) {
						//截取数据
						teachers = getLimitData(teachers); 
						//获取验证的map 已经存在的学年学期不能添加
						Set<String> ids = EntityUtils.getSet(teachers, Teacher::getId);
						Set<String> unitIds = EntityUtils.getSet(teachers, Teacher::getUnitId);
						Set<String> deptIds = EntityUtils.getSet(teachers, Teacher::getDeptId);
						Set<String> identitys = EntityUtils.getSet(teachers, Teacher::getIdentityCard);
						Map<String, Dept> allDeptMap = getDeptMap(deptIds);
						Map<String, Unit> allUnitMap = getUnitMap(unitIds);
						Map<String, Teacher> allTeacherMap = getTeacherMap(ids);
                        List<Teacher> idCardTeachers = new ArrayList<>();
                        Map<String, Teacher> idCardTeacherMap = new HashMap<>();
                        if(CollectionUtils.isNotEmpty(identitys)) {
                        	idCardTeachers = SUtils.dt(teacherRemoteService.findByIdentityCardNo(identitys.toArray(new String[identitys.size()])), new TR<List<Teacher>>() {}); 
                        	idCardTeacherMap = EntityUtils.getMap(idCardTeachers, Teacher::getIdentityCard);
                        }
						
						StringBuilder detailError = new StringBuilder().append("详细错误:");
						StringBuilder errorIdBuilder = new StringBuilder();
						Map<String, String> baseIdMap = new HashMap<>();
						
						int errorNum = 0;
						int idErrorNum = 0;
						for (Teacher teacher : teachers) {
							String id = teacher.getId();
							String unitId = teacher.getUnitId();
							String deptId = teacher.getDeptId();
							String idCard = teacher.getIdentityCard();
							if(isErrorData(unitId,allUnitMap) || isErrorData(deptId,allDeptMap)) {
								errorIdBuilder.append(id+",");
								errorNum++;
								continue;
							}
							if(StringUtils.isNotBlank(idCard) && StringUtils.isNotBlank(id)) {
								if(isErrorData(idCard,idCardTeacherMap,Boolean.FALSE) && !id.equals(idCardTeacherMap.get(idCard).getId())){
									errorIdBuilder.append(id+",");
									idErrorNum ++;
									continue;
								}
							}
							if(allTeacherMap != null && allTeacherMap.get(id) != null) {
								teacher.setCreationTime(allTeacherMap.get(id).getCreationTime());
								teacher.setModifyTime(new Date());
								teacher.setId(allTeacherMap.get(id).getId());
							}else {
								teacher.setId(getBaseId(saveRBs,id,BaseSaveConstant.BASE_TEACHER_TYPE));
							}
							if(StringUtils.isNotBlank(unitId)) 
								teacher.setUnitId(allUnitMap.get(unitId).getId());
							if(StringUtils.isNotBlank(deptId)) 
								teacher.setDeptId(allDeptMap.get(deptId).getId());
							if(!getIsRelation())
							    baseIdMap.put(teacher.getId(), id);
							saveTeacher.add(teacher);
						}
						detailError = detailError.append("单位不存在"+getErrorNum(unitIds, allUnitMap)+"条数据;部门不存在"+getErrorNum(deptIds, allDeptMap)+"条数据;");
						if(idErrorNum > 0 )
							detailError = detailError.append("身份证号码已经存在"+idErrorNum+"条数据;");
						Map<String,String> returnMsg = new HashMap<>();
						if(CollectionUtils.isNotEmpty(saveTeacher)) {
							baseSyncSaveService.saveTeacher(saveTeacher.toArray(new Teacher[saveTeacher.size()]),returnMsg);
						}
						return getReturnMsg(teachers.size(),errorNum+idErrorNum,detailError,returnMsg,saveRBs,errorIdBuilder,baseIdMap);
					}
				}
			}
			return returnSuccess();
		} catch (Exception e) {
			logger.error("保存教师数据失败,错误信息是：-------"+e.getMessage());
			return returnMsg(e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "grade", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addGrade(@RequestBody String jsonStr) {
		try {
			List<Grade> saveGrade = new ArrayList<>();
			List<RelationBase> saveRBs = new ArrayList<>();
			if(StringUtils.isNotBlank(jsonStr)) {
				String dataList = doExcuteDate(jsonStr);
				if(StringUtils.isNotBlank(dataList)) {
					List<Grade> grades =  JSONArray.parseArray(dataList, Grade.class);
					if(CollectionUtils.isNotEmpty(grades)) {
						//截取数据
						grades = getLimitData(grades); 
						//获取验证的map 已经存在的学年学期不能添加
						Set<String> ids = EntityUtils.getSet(grades, Grade::getId);
						Set<String> unitIds = EntityUtils.getSet(grades, Grade::getSchoolId);
						Set<String> subschoolIds = EntityUtils.getSet(grades, Grade::getSubschoolId);
						Map<String, SubSchool> subSchoolMap = getSubSchoolMap(subschoolIds);
						Map<String, Grade> allGradeMap = getGradeMap(ids);
						Map<String, School> allSchoolMap = getSchoolMap(unitIds);		
						StringBuilder detailError = new StringBuilder().append("详细错误:");
						StringBuilder errorIdBuilder = new StringBuilder();
						Map<String, String> baseIdMap = new HashMap<>();
						
						int errNum = 0;
						for (Grade grade : grades) {
							String id = grade.getId();
							String schoolId = grade.getSchoolId();
//							String gradeCode = grade.getGradeCode();
							String subSchoolId = grade.getSubschoolId();
							if(isErrorData(schoolId,allSchoolMap) || isErrorData(subSchoolId,subSchoolMap)) {
								errorIdBuilder.append(id+",");
								errNum++;
								continue;
							}
							if(allGradeMap != null && allGradeMap.get(id) != null) {
								grade.setCreationTime(allGradeMap.get(id).getCreationTime());
								grade.setModifyTime(new Date());
								grade.setId(allGradeMap.get(id).getId());
							}else {
								grade.setId(getBaseId(saveRBs,id,BaseSaveConstant.BASE_GRADE_TYPE));
							}
							if(StringUtils.isNotBlank(schoolId)) 
							   grade.setSchoolId(allSchoolMap.get(schoolId).getId());
							if(StringUtils.isNotBlank(subSchoolId)) 
							   grade.setSubschoolId(subSchoolMap.get(subSchoolId).getId());
							if(!getIsRelation())
							    baseIdMap.put(grade.getId(), id);
							saveGrade.add(grade);
						}
						detailError = detailError.append("学校不存在"+getErrorNum(unitIds, allSchoolMap)+"条数据;校区不存在的"+getErrorNum(subschoolIds, subSchoolMap)+"条数据;");
						getErrorNum(unitIds,allSchoolMap);
						Map<String,String> returnMsg = new HashMap<>();
						if(CollectionUtils.isNotEmpty(saveGrade)) {
							baseSyncSaveService.saveGrade(saveGrade.toArray(new Grade[saveGrade.size()]),returnMsg);
						}
						return getReturnMsg(grades.size(),errNum,detailError,returnMsg,saveRBs,errorIdBuilder,baseIdMap);
					}
				}
			}
			return returnSuccess();
		} catch (Exception e) {
			logger.error("保存年级数据失败,错误信息是：-------"+e.getMessage());
			return returnMsg(e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "class", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addClazz(@RequestBody String jsonStr) {
		try {
			List<Clazz> saveClazz = new ArrayList<>();
			List<RelationBase> saveRBs = new ArrayList<>();
			if(StringUtils.isNotBlank(jsonStr)) {
				String dataList = doExcuteDate(jsonStr);
				if(StringUtils.isNotBlank(dataList)) {
					List<Clazz> clazzs =  JSONArray.parseArray(dataList, Clazz.class);
					if(CollectionUtils.isNotEmpty(clazzs)) {
						//截取数据
						clazzs = getLimitData(clazzs); 
						//获取验证的map 已经存在的学年学期不能添加
						Set<String> ids = EntityUtils.getSet(clazzs, Clazz::getId);
						Set<String> unitIds = EntityUtils.getSet(clazzs, Clazz::getSchoolId);
						Set<String> subschoolIds = EntityUtils.getSet(clazzs, Clazz::getCampusId);
						Set<String> gradeIds = EntityUtils.getSet(clazzs, Clazz::getGradeId);
						Map<String, Clazz> allClazzMap = getClazzMap(ids);
						Map<String, SubSchool> subSchoolMap = getSubSchoolMap(subschoolIds);
						Map<String, Grade> allGradeMap = getGradeMap(gradeIds);
						Map<String, School> allSchoolMap = getSchoolMap(unitIds);
						StringBuilder detailError = new StringBuilder().append("详细错误:");
						StringBuilder errorIdBuilder = new StringBuilder();
						Map<String, String> baseIdMap = new HashMap<>();
						int errorNum = 0;
						for (Clazz clazz : clazzs) {
							String id = clazz.getId();
							String gradeId = clazz.getGradeId();
							String schoolId = clazz.getSchoolId();
							String subSchoolId = clazz.getCampusId();
							if(isErrorData(schoolId,allSchoolMap) || isErrorData(subSchoolId,subSchoolMap) || isErrorData(gradeId,allGradeMap)) {
								errorIdBuilder.append(id+",");
								errorNum++;
								continue;
							}
							if(allClazzMap != null && allClazzMap.get(id) != null) {
								clazz.setCreationTime(allClazzMap.get(id).getCreationTime());
								clazz.setModifyTime(new Date());
								clazz.setId(allClazzMap.get(id).getId());
							}else {
								clazz.setId(getBaseId(saveRBs,id,BaseSaveConstant.BASE_CLAZZ_TYPE));
							}
							if(StringUtils.isNotBlank(gradeId)) 
							   clazz.setGradeId(allGradeMap.get(gradeId).getId());
							if(StringUtils.isNotBlank(schoolId)) 
							   clazz.setSchoolId(allSchoolMap.get(schoolId).getId());
							if(StringUtils.isNotBlank(subSchoolId)) 
							   clazz.setCampusId(subSchoolMap.get(subSchoolId).getId());
							if(!getIsRelation())
							    baseIdMap.put(clazz.getId(), id);
							saveClazz.add(clazz);
						}
						detailError = detailError.append("单位不存在"+getErrorNum(unitIds, allSchoolMap)+"条数据;校区不存在"+getErrorNum(subschoolIds, subSchoolMap)
						                            +"条数据;年级不存在"+getErrorNum(gradeIds, allGradeMap)+"条数据;");
						Map<String,String> returnMsg = new HashMap<>();
						if(CollectionUtils.isNotEmpty(saveClazz)) {
							baseSyncSaveService.saveClass(saveClazz.toArray(new Clazz[saveClazz.size()]),returnMsg);
						}
						return getReturnMsg(clazzs.size(),errorNum,detailError,returnMsg,saveRBs,errorIdBuilder,baseIdMap);
					}
				}
			}
			return returnSuccess();
		} catch (Exception e) {
			logger.error("保存班级数据失败,错误信息是：-------"+e.getMessage());
			return returnMsg(e);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "student", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addStudent(@RequestBody String jsonStr) {
		try {
			List<Student> saveStudent = new ArrayList<>();
			List<RelationBase> saveRBs = new ArrayList<>();
			if(StringUtils.isNotBlank(jsonStr)) {
				String dataList = doExcuteDate(jsonStr);
				if(StringUtils.isNotBlank(dataList)) {
					List<Student> students =  JSONArray.parseArray(dataList, Student.class);
					if(CollectionUtils.isNotEmpty(students)) {
						//截取数据
						students = getLimitData(students); 
						//获取验证的map 已经存在的学年学期不能添加
						Set<String> ids = EntityUtils.getSet(students, Student::getId);
						Set<String> unitIds = EntityUtils.getSet(students, Student::getSchoolId);
						Set<String> clazzIds = EntityUtils.getSet(students, Student::getClassId);
						Map<String, Student> allStudentMap = getStudentMap(ids);
						Map<String, Clazz> allClazzMap = getClazzMap(clazzIds);
						Map<String, School> allSchoolMap = getSchoolMap(unitIds);
						StringBuilder detailError = new StringBuilder().append("详细错误:");
						StringBuilder errorIdBuilder = new StringBuilder();
						Map<String, String> baseIdMap = new HashMap<>();
						Set<String> identitys = EntityUtils.getSet(students, Student::getIdentityCard);
						List<Student> idCardStudents = new ArrayList<>();
	                    Map<String, Student> idCardStudentMap = new HashMap<>();
                        if(CollectionUtils.isNotEmpty(identitys)) {
                        	idCardStudents = SUtils.dt(studentRemoteService.findByIdentityCards(identitys.toArray(new String[identitys.size()])), new TR<List<Student>>() {}); 
                        	idCardStudentMap = EntityUtils.getMap(idCardStudents, Student::getIdentityCard);
                        }
                        
						int errorNum = 0;
						for (Student student : students) {
							String clazzId = student.getClassId();
							String schoolId = student.getSchoolId();
							String id = student.getId();
							String enrollYear = student.getEnrollYear();
							String idCard = student.getIdentityCard();
							//要加一个判断身份证唯一的条件
							if(isErrorData(schoolId,allSchoolMap) || isErrorData(clazzId,allClazzMap)) {
								errorIdBuilder.append(id+",");
								errorNum++;
								continue;
							}
							if(StringUtils.isNotBlank(idCard) && StringUtils.isNotBlank(id)) {
								if(isErrorData(idCard,idCardStudentMap,Boolean.FALSE) && !id.equals(idCardStudentMap.get(idCard).getId())){
									errorIdBuilder.append(id+",");
									errorNum ++;
									continue;
								}
							}
							if(allStudentMap != null && allStudentMap.get(id) != null) {
								student.setCreationTime(allStudentMap.get(id).getCreationTime());
								student.setModifyTime(new Date());
								student.setId(allStudentMap.get(id).getId());
							}else {
								student.setId(getBaseId(saveRBs,id,BaseSaveConstant.BASE_STUDENT_TYPE));
							}
							if(StringUtils.isNotBlank(clazzId))
							   student.setClassId(allClazzMap.get(clazzId).getId());
							if(StringUtils.isNotBlank(schoolId))
							   student.setSchoolId(allSchoolMap.get(schoolId).getId());
							if(!getIsRelation())
							    baseIdMap.put(student.getId(), id);
							
							if(StringUtils.isBlank(enrollYear)){
								student.setEnrollYear(allClazzMap.get(clazzId).getAcadyear());
							}
							saveStudent.add(student);
						}
						detailError = detailError.append("单位不存在"+getErrorNum(unitIds, allSchoolMap)+"条数据;班级不存在"+getErrorNum(clazzIds, allClazzMap)+"条数据;");
						Map<String,String> returnMsg = new HashMap<>();
						if(CollectionUtils.isNotEmpty(saveStudent)) {
							baseSyncSaveService.saveStudent(saveStudent.toArray(new Student[saveStudent.size()]),returnMsg);
						}
						return getReturnMsg(students.size(),errorNum,detailError,returnMsg,saveRBs,errorIdBuilder,baseIdMap);
					}
				}
			}
			return returnSuccess();
		} catch (Exception e) {
			logger.error("保存学生数据失败,错误信息是：-------"+e.getMessage());
			return returnMsg(e);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "family", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addFamily(@RequestBody String jsonStr) {
		try {
			List<Family> saveFamily = new ArrayList<>();
			List<RelationBase> saveRBs = new ArrayList<>();
			if(StringUtils.isNotBlank(jsonStr)) {
				String dataList = doExcuteDate(jsonStr);
				if(StringUtils.isNotBlank(dataList)) {
					List<Family> familys =  JSONArray.parseArray(dataList, Family.class);
					if(CollectionUtils.isNotEmpty(familys)) {
						//截取数据
						familys = getLimitData(familys); 
						//获取验证的map 已经存在的学年学期不能添加
						Set<String> ids = EntityUtils.getSet(familys, Family::getId);
						Set<String> unitIds = EntityUtils.getSet(familys, Family::getSchoolId);
						Set<String> studentIds = EntityUtils.getSet(familys, Family::getStudentId);
						Map<String, Student> allStudentMap = getStudentMap(studentIds);
						Map<String, School> allSchoolMap = getSchoolMap(unitIds);
						Map<String, Family> allFamilyMap = getFamilyMap(ids);
						StringBuilder detailError = new StringBuilder().append("详细错误:");
						StringBuilder errorIdBuilder = new StringBuilder();
						Map<String, String> baseIdMap = new HashMap<>();
						int errorNum = 0;
						for (Family family : familys) {
							String studentId = family.getStudentId();
							String schoolId = family.getSchoolId();
							String id = family.getId();
							if(isErrorData(schoolId,allSchoolMap) || isErrorData(studentId,allStudentMap)) {
								errorIdBuilder.append(id+",");
								errorNum++;
								continue;
							}
							if(allFamilyMap != null && allFamilyMap.get(id) != null) {
								family.setCreationTime(allFamilyMap.get(id).getCreationTime());
								family.setModifyTime(new Date());
								family.setId(allFamilyMap.get(id).getId());
							}else {
								family.setId(getBaseId(saveRBs,id,BaseSaveConstant.BASE_FAMILY_TYPE));
							}
							if(StringUtils.isNotBlank(studentId))
							   family.setStudentId(allStudentMap.get(studentId).getId());
							if(StringUtils.isNotBlank(schoolId))
							   family.setSchoolId(allSchoolMap.get(schoolId).getId());
							if(!getIsRelation())
							    baseIdMap.put(family.getId(), id);
							saveFamily.add(family);
						}
						detailError = detailError.append("单位不存在"+getErrorNum(unitIds, allSchoolMap)+"条数据;学生不存在"+getErrorNum(studentIds, allStudentMap)+"条数据;");
						Map<String,String> returnMsg = new HashMap<>();
						if(CollectionUtils.isNotEmpty(saveFamily)) {
							baseSyncSaveService.saveFamily(saveFamily.toArray(new Family[saveFamily.size()]),returnMsg);
						}
						return getReturnMsg(familys.size(),errorNum,detailError,returnMsg,saveRBs,errorIdBuilder,baseIdMap);
					}
				}
			}
			return returnSuccess();
		} catch (Exception e) {
			logger.error("保存学生数据失败,错误信息是：-------"+e.getMessage());
			return returnMsg(e);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addUser(@RequestBody String jsonStr) {
		try {
			List<User> saveUser = new ArrayList<>();
			List<RelationBase> saveRBs = new ArrayList<>();
			if(StringUtils.isNotBlank(jsonStr)) {
				String dataList = doExcuteDate(jsonStr);
				if(StringUtils.isNotBlank(dataList)) {
					List<User> users =  JSONArray.parseArray(dataList, User.class);
					if(CollectionUtils.isNotEmpty(users)) {
						//截取数据
						users = getLimitData(users); 
						Set<String> ids = EntityUtils.getSet(users, User::getId);
						Set<String> userNames = EntityUtils.getSet(users, User::getUsername);
						Set<String> unitIds = EntityUtils.getSet(users, User::getUnitId);
						Set<Integer> userTypes = EntityUtils.getSet(users, User::getUserType);
						Set<Integer> ownerTypes = EntityUtils.getSet(users, User::getOwnerType);
						Map<Integer, List<User>> ownerTypeMap = EntityUtils.getListMap(users, User::getOwnerType, Function.identity());
						Map<String, Student> allStudentMap = new HashMap<>();
						Map<String, Family> allFamilyMap = new HashMap<>();
						Map<String, Teacher> allTeacherMap = new HashMap<>();
						for (Map.Entry<Integer, List<User>> entry : ownerTypeMap.entrySet()) {
							if(entry.getKey() == User.OWNER_TYPE_STUDENT)
								allStudentMap = getStudentMap(EntityUtils.getSet(entry.getValue(), User::getOwnerId));
							if(entry.getKey() == User.OWNER_TYPE_TEACHER)
								allTeacherMap = getTeacherMap(EntityUtils.getSet(entry.getValue(), User::getOwnerId));
							if(entry.getKey() == User.OWNER_TYPE_FAMILY)
								allFamilyMap = getFamilyMap(EntityUtils.getSet(entry.getValue(), User::getOwnerId));
						}
						Map<String, User> allUserMap = getUserMap(ids);
						Map<String, User> unitIdMap = new HashMap<>();
						if(CollectionUtils.isNotEmpty(userTypes)) {
							userTypes.remove(User.USER_TYPE_COMMON_USER);
							if(CollectionUtils.isNotEmpty(userTypes)) {
								List<User> adMinUsers = SUtils.dt(userRemoteService.findByUserTypes(userTypes.toArray(new Integer[userTypes.size()])), new TR<List<User>>() {});
								unitIdMap.putAll(EntityUtils.getMap(adMinUsers, User::getUnitId));
							}
						}
						Map<String, User> allNameUserMap = new HashMap<>();
						if(CollectionUtils.isNotEmpty(userNames)) {
							List<User> allNameUser = SUtils.dt(userRemoteService.findByUsernames(userNames.toArray(new String[userNames.size()])), new TR<List<User>>() {});
							allNameUserMap = EntityUtils.getMap(allNameUser, User::getUsername);
						}
						Map<String, Unit> allUnitMap = getUnitMap(unitIds);
						StringBuilder detailError = new StringBuilder().append("详细错误:");
						StringBuilder errorIdBuilder = new StringBuilder();
						Map<String, String> baseIdMap = new HashMap<>();
						int errorNum = 0;
						int adminErrorNum = 0;
						for (User user : users) {
							String id = user.getId();
							String username = user.getUsername();
							String unitId = user.getUnitId();
							Integer userType = user.getUserType();
							Integer ownerType = user.getOwnerType();
							String ownerId = user.getOwnerId();
							if(unitId != null && userType != null && userType != User.USER_TYPE_COMMON_USER && unitIdMap != null) {
								User user2 = unitIdMap.get(unitId);
								if(user2 != null && !user2.getId().equals(id)) {
									errorIdBuilder.append(id+",");
									adminErrorNum ++ ;
									continue;
								}
							}
							if(allUserMap != null && allUserMap.get(id) != null) {
								user.setCreationTime(allUserMap.get(id).getCreationTime());
								user.setModifyTime(new Date());
								user.setId(allUserMap.get(id).getId());
							}else {
								user.setId(getBaseId(saveRBs,id,BaseSaveConstant.BASE_USER_TYPE));
							}
							if(StringUtils.isNotBlank(unitId))
							  user.setUnitId(allUnitMap.get(unitId).getId());
							if(StringUtils.isNotBlank(ownerId) && ownerType != null) {
								if(ownerType == User.OWNER_TYPE_STUDENT ) {
									if(isErrorData(ownerId,allStudentMap)){
										errorIdBuilder.append(id+",");
										errorNum ++ ;
										continue;
									}
									user.setOwnerId(allStudentMap.get(ownerId).getId());
								}
								if(ownerType == User.OWNER_TYPE_TEACHER ) {
									if(isErrorData(ownerId,allTeacherMap)){
										errorIdBuilder.append(id+",");
										errorNum ++ ;
										continue;
									}
									user.setOwnerId(allTeacherMap.get(ownerId).getId());
								}
								if(ownerType == User.OWNER_TYPE_FAMILY ) {
									if(isErrorData(ownerId,allFamilyMap)){
										errorIdBuilder.append(id+",");
										errorNum ++ ;
										continue;
									}
									user.setOwnerId(allFamilyMap.get(ownerId).getId());
								}
							}
							if(!getIsRelation())
							    baseIdMap.put(user.getId(), id);
							
							if(isErrorData(unitId,allUnitMap)) {
								errorIdBuilder.append(id+",");
								errorNum++;
								continue;
							}
							if(isErrorData(username,allNameUserMap,Boolean.FALSE)){
								String bid = user.getId();
								String oldidString  = allNameUserMap.get(username).getId();
								if(StringUtils.isNotBlank(oldidString) && !oldidString.equals(bid)){
									errorIdBuilder.append(bid+",");
									errorNum++;
									continue;
								}
							}
							
							saveUser.add(user);
						}
						detailError = detailError.append("单位不存在"+getErrorNum(unitIds, allUnitMap)+"条数据;用户名已经存在"+getErrorNum(userNames, allNameUserMap,Boolean.FALSE)+"条数据;");
						if(adminErrorNum > 0)
							detailError = detailError.append("同一个单位下不能有多个管理员"+adminErrorNum+"条数据");
						Map<String,String> returnMsg = new HashMap<>();
						if(CollectionUtils.isNotEmpty(saveUser)) {
							baseSyncSaveService.saveUser(saveUser.toArray(new User[saveUser.size()]),returnMsg);
						}
						return getReturnMsg(users.size(),errorNum,detailError,returnMsg,saveRBs,errorIdBuilder,baseIdMap);
					}
				}
			}
			return returnSuccess();
		} catch (Exception e) {
			logger.error("保存用户数据失败,错误信息是：-------"+e.getMessage());
			return returnMsg(e);
		}
	}
	
    
    /**
     * 进行密码加密和解密的接口   1---解密  2---加密
     * @param request
     * @param userName
     * @param pwd (md5 32位小写加密)
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/changeUserPwd", method = RequestMethod.GET)
    public String changeUserPwd(HttpServletRequest request,String pwd,String type) {
    	String endPwd;
        try {
        	if(StringUtils.isNotBlank(pwd) && StringUtils.isNotBlank(type)) {
        		if("1".equals(type)) {
        			endPwd =  PWD.decode(pwd);
        		}else if ("2".equals(type)) {
        			endPwd =  new PWD(pwd).encode();
				}else{
					return returnError("-1", "类型不存在");
				}
        	}else {
        		return returnError("-1", "必填字段为空");
        	}
        }catch (Exception e) {
        	logger.error("解析密码失败,错误信息是：-------"+e);
        	return returnError("-1", "解析密码信息失败！" + e.getMessage());
        }
        return returnSuccess("1",endPwd);
    }
}
