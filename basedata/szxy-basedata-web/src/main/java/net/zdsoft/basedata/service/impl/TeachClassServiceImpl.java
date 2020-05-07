package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dao.TeachClassDao;
import net.zdsoft.basedata.dao.TeachClassJdbcDao;
import net.zdsoft.basedata.dao.TeachClassStuDao;
import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.dto.TeachClassSearchDto;
import net.zdsoft.basedata.entity.ClassHour;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.CourseType;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassEx;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.service.ClassHourService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.CourseTypeService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.basedata.service.TeachClassExService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.basedata.service.TeachClassStuService;
import net.zdsoft.basedata.service.TeachPlaceService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.ArrayUtil;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;

@Service("teachClassService")
public class TeachClassServiceImpl extends BaseServiceImpl<TeachClass, String> implements TeachClassService {
    @Autowired
    private TeachClassDao teachClassDao;
    @Autowired
    private TeachClassStuDao teachClassStuDao;
    @Autowired
    private TeachClassStuService teachClassStuService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeachClassExService teachClassExService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private CourseTypeService courseTypeService;
    @Autowired
    private TeachClassJdbcDao teachClassJdbcDao;
    @Autowired
    private CourseScheduleService courseScheduleService;
    @Autowired
    private TeachPlaceService teachPlaceService;
    @Autowired
    private ClassHourService classHourService;
    @Override
    public List<TeachClass> findByAcadyearAndSemesterAndUnitId(String acadyear, String semester, String unitId) {
        return teachClassDao.findByAcadyearSemesterUnitId(acadyear, semester, unitId);
    }

    @Override
    protected BaseJpaRepositoryDao<TeachClass, String> getJpaDao() {
        return teachClassDao;
    }

    @Override
    protected Class<TeachClass> getEntityClass() {
        return TeachClass.class;
    }

    @Override
    public List<TeachClass> findTeachClassList(String unitId, String acadyear, String semester, String courseId) {
        return teachClassDao.findTeachClassList(unitId, acadyear, semester, courseId);
    }

    @Override
    public boolean findExistsClassName(String id, String schoolId,String acadyear, String semester, String className) {
        List<TeachClass> tc = null;
        if (StringUtils.isNotBlank(className)) {
            className = className.trim();
        }
        tc = teachClassDao.findByAcadyearAndSemesterAndName(schoolId,acadyear, semester, className);
        if(CollectionUtils.isEmpty(tc)){
        	return false;
        }
        if(StringUtils.isBlank(id)){
        	return true;
        }else{
        	Set<String> ids = EntityUtils.getSet(tc,"id");
        	ids.remove(id);
        	return CollectionUtils.isNotEmpty(ids);
        }
//        if (StringUtils.isBlank(id)) {
//            tc = teachClassDao.findByAcadyearAndSemesterAndName(schoolId,acadyear, semester, className);
//        }
//        else {
//            tc = teachClassDao.findByIdAndAcadyearAndSemesterAndName(id, schoolId,acadyear, semester, className);
//        }
//        return CollectionUtils.isNotEmpty(tc);
    }

    @Override
    public void saveCopyTeachClaStu(TeachClass ent) {
        List<TeachClassStu> tcslist = teachClassStuDao.findByClassIds(new String[] { ent.getId() });
        ent.setId(UuidUtils.generateUuid());
        ent.setCreationTime(new Date());
        List<TeachClassStu> addtcslist = null;
        if (CollectionUtils.isNotEmpty(tcslist)) {
            addtcslist = new ArrayList<TeachClassStu>();
            for (TeachClassStu stuent : tcslist) {
                TeachClassStu copytc = new TeachClassStu();
                EntityUtils.copyProperties(stuent, copytc);
                copytc.setId(UuidUtils.generateUuid());
                copytc.setClassId(ent.getId());
                copytc.setCreationTime(new Date());
                addtcslist.add(copytc);
            }
        }
        if (addtcslist != null) {
            teachClassStuDao.saveAll(addtcslist);
        }
        teachClassDao.save(ent);
    }

    @Override
    public List<TeachClass> findByAcadyearSemesterUnitId(String acadyear, String semester, String unitId,
            Pagination page) {
        List<TeachClass> list = findByAcadyearAndSemesterAndUnitId(acadyear, semester, unitId);
        if (page == null) {
            return list;
        }
        else {
            List<TeachClass> returnlist = teachClassDao.findByAcadyearSemesterUnitId(acadyear, semester, unitId,
                    Pagination.toPageable(page));
            if (CollectionUtils.isNotEmpty(list)) {
                page.setMaxRowCount(list.size());
            }
            else {
                page.setMaxRowCount(0);
            }
            return returnlist;
        }

    }

//    public List<TeachClass> findByAcadyearSemesterUnitId(String acadyear, String semester, String unitId) {
//        return teachClassDao.findByAcadyearSemesterUnitId(acadyear, semester, unitId);
//    }

    @Override
    public List<TeachClass> findTeachClassList(final String unitId, final String acadyear, final String semester,
            final String subjectId, final boolean isUsing, String[] gradeIds,boolean isFiltration) {
        TeachClassSearchDto dto=new TeachClassSearchDto();
        if(gradeIds!=null && gradeIds.length != 0){
        	dto.setGradeIds(ArrayUtil.print(gradeIds));
        }
		dto.setUnitId(unitId);
		dto.setAcadyearSearch(acadyear);
		dto.setSemesterSearch(semester);
		if(isUsing){
			dto.setIsUsing(BaseConstants.ONE_STR);
		}
		dto.setSubjectId(subjectId);
		List<TeachClass>tclist =findListByDto(dto);
		List<TeachClass>tclistall=new ArrayList<TeachClass>();
		if(isFiltration){
			if(CollectionUtils.isNotEmpty(tclist)){
				Set<String> parentClassIdSet = new HashSet<String>();
				for(TeachClass ent :tclist){
					if(BaseConstants.ONE_STR.equals(ent.getIsUsingMerge())){//为了获取合班信息
						if(StringUtils.isNotBlank(ent.getParentId()))
								parentClassIdSet.add(ent.getParentId());
					}else {
						if(!BaseConstants.ONE_STR.equals(ent.getIsMerge()))//为了过滤合班
							tclistall.add(ent);
					}
				}
				
				if(CollectionUtils.isNotEmpty(parentClassIdSet)){
					tclistall.addAll(this.findListByIds(parentClassIdSet.toArray(new String[0])));
				}
			}
		}else{
			if(CollectionUtils.isNotEmpty(tclist)){
				tclistall.addAll(tclist);
			}
		}
		return tclistall;
				
				
    }

    @Override
    public List<TeachClass> findTeachClassListByIds(String[] ids) {
        List<TeachClass> list = new ArrayList<TeachClass>();
        if (ids != null && ids.length > 0) {
            list = teachClassDao.findTeachClassListByIds(ids);
        }
        return list;
    }
    
    @Override
    public List<TeachClass> findTeachClassContainNotUseByIds(String[] ids) {
    	List<TeachClass> list = new ArrayList<TeachClass>();
    	if (ids != null && ids.length > 0) {
    		list = teachClassDao.findTeachClassContainNotUseByIds(ids);
    	}
    	return list;
    }

    @Override
    public List<TeachClass> findByCourseIdAndInIds(String courseId, String[] ids) {
        return teachClassDao.findByCourseIdAndInIds(courseId, ids);
    }

    @Override
    public void deleteByIds(String... ids) {
        if (ids != null && ids.length > 0) {
            teachClassStuDao.deleteByClassIds(ids);
            teachClassDao.deleteByIds(ids);
        }
    }
    
    @Override
    public void notUsing(String[] ids) {
    	teachClassDao.notUsing(ids);
    }
    @Override
    public void yesUsing(String[] ids) {
    	teachClassDao.yesUsing(ids);
    }
    @Override
    public List<TeachClass> saveAllEntitys(TeachClass... teachClass) {
        return teachClassDao.saveAll(checkSave(teachClass));
    }

    @Override
    public List<TeachClass> findListByTeacherId(String teacherId, String acadyear, String semester) {
        return teachClassDao.findByTeaIdAndAcadyearAndSemester(teacherId, acadyear, semester);
    }

	@Override
	public List<TeachClass> findTeachClassList(final String unitId, final String acadyear, final String semester, final boolean isGk, String studentId) {
		List<TeachClassStu> findByStuIds = teachClassStuService.findByStuIds(new String[]{studentId});
		final Set<String> teachClassIds = EntityUtils.getSet(findByStuIds, "classId");
		List<TeachClass> returnList = new ArrayList<TeachClass>();
		if(CollectionUtils.isEmpty(findByStuIds)){
			return returnList;
		}
		Specification<TeachClass> s = new Specification<TeachClass>() {
            @Override
            public Predicate toPredicate(Root<TeachClass> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();

                queryIn("id", teachClassIds.toArray(new String[0]), root, ps, null);
                ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
                ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                if(StringUtils.isNotBlank(acadyear)){
                	ps.add(cb.equal(root.get("acadyear").as(String.class), acadyear));
                }
                if(StringUtils.isNotBlank(semester)){
                	ps.add(cb.equal(root.get("semester").as(String.class), semester));
                }
                if(isGk){
                	ps.add(cb.notEqual(root.get("gradeId").as(String.class), Constant.GUID_ZERO));
                }else{
                	ps.add(cb.equal(root.get("gradeId").as(String.class), Constant.GUID_ZERO));
                }
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.desc(root.get("creationTime").as(Date.class)));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
        returnList = teachClassDao.findAll(s);
		return returnList;
	}

	@Override
	public Map<String,Integer[]> countNumByIds(String[] ids) {
		Map<String,Integer[]> map=new HashMap<String, Integer[]>();
		if(ArrayUtils.isNotEmpty(ids)){
			List<TeachClassStu> stulist =teachClassStuDao.findByClassIds(ids);
			if(CollectionUtils.isNotEmpty(stulist)){
				Set<String> stuids =EntityUtils.getSet(stulist, "studentId");
				Map<String, Student> mapstu =studentService.findMapByIdIn(stuids.toArray(new String[0]));
				for(TeachClassStu ent:stulist){
					Integer[] strs = map.get(ent.getClassId());
					if(strs==null){
						strs =new Integer[3];
						map.put(ent.getClassId(), strs);
					}
					Student stuent = mapstu.get(ent.getStudentId());
					if(stuent!=null){
						if(strs[0]==null){
							strs[0]=0;
						}
						strs[0]=strs[0]+1;
						if(BaseConstants.MALE == (stuent.getSex()==null?0:stuent.getSex())){
							if(strs[1]==null){
								strs[1]=0;
							}
							strs[1]=strs[1]+1;
						}else if(BaseConstants.FEMALE == (stuent.getSex()==null?0:stuent.getSex())){
							if(strs[2]==null){
								strs[2]=0;
							}
							strs[2]=strs[2]+1;
						}
					}
				}
			}
		}
		
		return map;
	}

	@Override
	public List<TeachClass> findByStuIdAndAcadyearAndSemester(String studentId,
			String acadyear, String semester) {
		return teachClassDao.findByStuIdAndAcadyearAndSemester(studentId, acadyear, semester);
	}
	
	@Override
	public List<TeachClass> findByStuIdAndAcadyearAndSemester(String studentId,String schoolId,
			String acadyear, String semester) {
		return teachClassDao.findByStuIdAndAcadyearAndSemester(studentId, schoolId,acadyear, semester);
	}

	@Override
	public List<TeachClass> findByGradeId(String gradeId) {
		//TeachClass GradeId多选
		return teachClassDao.findByLikeGradeId("%"+gradeId+"%");
	}

	@Override
	public List<TeachClass> findByNames(final String unitId, final String[] names) {
		List<TeachClass> returnList = new ArrayList<TeachClass>();
		Specification<TeachClass> s = new Specification<TeachClass>() {
            @Override
            public Predicate toPredicate(Root<TeachClass> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();

                queryIn("name", names, root, ps, null);
                ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
                ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));  
                ps.add(cb.equal(root.get("isUsing").as(String.class), "1"));
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.desc(root.get("creationTime").as(Date.class)));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
        returnList = teachClassDao.findAll(s);
		return returnList;
	}

	@Override
	public List<TeachClass> findListByDto(TeachClassSearchDto dto) {
		return teachClassJdbcDao.findListByDto(dto);
	}
	@Override
	public List<TeachClass> findListByDtoWithMaster(TeachClassSearchDto dto) {
		return teachClassJdbcDao.findListByDto(dto);
	}

	@Override
	public void makeTimePlace(List<TeachClass> teachClassList,boolean isMakeNum) {
		if(CollectionUtils.isNotEmpty(teachClassList)){
			//暂时只考虑主教师
			Set<String> ids=new HashSet<String>();
			Set<String> subjectIds=new HashSet<String>();
			Set<String> teacherIds=new HashSet<String>();
			
			//合班
			Map<String,List<String>> parentChildMap=null;
			Set<String> parentIdSet = new HashSet<String>();
			
			//关联科目id
			Set<String> relaHourids=new HashSet<>();
			Set<String> relapids=new HashSet<>();
			
			for(TeachClass t:teachClassList){
				ids.add(t.getId());
				subjectIds.add(t.getCourseId());
				teacherIds.add(t.getTeacherId());
				
				if(Constant.IS_TRUE_Str.equals(t.getIsMerge())){
					parentIdSet.add(t.getId());
				}
				
				if(StringUtils.isNotBlank(t.getRelaCourseId())) {
					relaHourids.add(t.getRelaCourseId());
				}
				if(StringUtils.isNotBlank(t.getPlaceId())) {
					relapids.add(t.getPlaceId());
				}
			}
			
			Map<String, TeachPlace> pMap=new HashMap<String, TeachPlace>();
			if(CollectionUtils.isNotEmpty(relapids)){
				pMap = teachPlaceService.findMapByIdIn(relapids.toArray(new String[]{}));
			}
			Map<String,ClassHour> classHourMap=new HashMap<>();
			if(CollectionUtils.isNotEmpty(relaHourids)) {
				List<ClassHour> classHourList = classHourService.findListByIds(relaHourids.toArray(new String[0]));
				classHourList=classHourService.makeClassNames(classHourList);
				if(CollectionUtils.isNotEmpty(classHourList)) {
					classHourMap=EntityUtils.getMap(classHourList, e->e.getId());
					subjectIds.addAll(EntityUtils.getSet(classHourList, e->e.getSubjectId()));
				}
				
			}
			
			if(CollectionUtils.isNotEmpty(parentIdSet)){
				List<TeachClass> childTeachClassList = teachClassDao.findByParentIdIn(parentIdSet.toArray(new String[0]));
				Set<String> childIds = EntityUtils.getSet(childTeachClassList, e->e.getId());
				ids.addAll(childIds);
				parentChildMap = new HashMap<String, List<String>>();
				for (TeachClass teachClass : childTeachClassList) {
					if(!parentChildMap.containsKey(teachClass.getParentId())){
						parentChildMap.put(teachClass.getParentId(), new ArrayList<String>());
					}
					parentChildMap.get(teachClass.getParentId()).add(teachClass.getId());
				}
			}
			
			
			Map<String,Course> courseMap=new HashMap<String,Course>();
			Map<String, CourseType> courseTypeMap =new HashMap<String, CourseType>();
			List<Course> courseList = courseService.findListByIdIn(subjectIds.toArray(new String[]{}));
			
			if(CollectionUtils.isNotEmpty(courseList)){
				Set<String> courseTypeId=new HashSet<String>();
				for(Course c:courseList){
					courseMap.put(c.getId(), c);
					if(StringUtils.isNotBlank(c.getCourseTypeId())){
						courseTypeId.add(c.getCourseTypeId());
					}
				}
				if(courseTypeId.size()>0){
					courseTypeMap = courseTypeService.findMapByIdIn(courseTypeId.toArray(new String[0]));
				}
				
			}
			Map<String, Integer> stuNumByTeachClassId=new HashMap<String, Integer>();
			if(isMakeNum){
				stuNumByTeachClassId=teachClassStuService.countByClassIds(ids.toArray(new String[]{}));
			}
			
			Map<String, Teacher> teacherMap = teacherService.findMapByIdIn(teacherIds.toArray(new String[]{}));
			
			List<TeachClassEx> list = teachClassExService.findByTeachClassIdIn(ids.toArray(new String[]{}), true);
			Map<String,List<TeachClassEx>> exMap=new HashMap<String,List<TeachClassEx>>();
			if(CollectionUtils.isNotEmpty(list)){
				for(TeachClassEx ex:list){
					if(!exMap.containsKey(ex.getTeachClassId())){
						exMap.put(ex.getTeachClassId(), new ArrayList<TeachClassEx>());
					}
					exMap.get(ex.getTeachClassId()).add(ex);
				}
			}
			Course c=null;
			for(TeachClass t:teachClassList){
				if(StringUtils.isNotBlank(t.getRelaCourseId())) {
					//关联走班课程
					TeachClassEx ee=new TeachClassEx();
					//时间不显示
					if(classHourMap.containsKey(t.getRelaCourseId())) {
						ClassHour hh = classHourMap.get(t.getRelaCourseId());
						ee.setTimeStr(courseMap.get(hh.getSubjectId()).getSubjectName()+"("+hh.getClassNames()+")");
					}
					
					if(pMap.containsKey(t.getPlaceId())){
						ee.setPlaceName(pMap.get(t.getPlaceId()).getPlaceName());
					}
					t.setExList(new ArrayList<>());
					t.getExList().add(ee);
				}else {
					if(exMap.containsKey(t.getId())){
						t.setExList(exMap.get(t.getId()));
					}
				}
				c = courseMap.get(t.getCourseId());
				if(c!=null){
					t.setCourseName(c.getSubjectName());
					if(StringUtils.isNotBlank(c.getCourseTypeId())){
						if(courseTypeMap.containsKey(c.getCourseTypeId())){
							t.setFromCourseType(courseTypeMap.get(c.getCourseTypeId()).getName());
						}
					}
				}
				if(teacherMap.containsKey(t.getTeacherId())){
					t.setTeacherNames(teacherMap.get(t.getTeacherId()).getTeacherName());
				}
				if(stuNumByTeachClassId.containsKey(t.getId())){
					t.setStudentNum(stuNumByTeachClassId.get(t.getId()));
				}else if(MapUtils.isNotEmpty(parentChildMap) && parentChildMap.containsKey(t.getId())){
					List<String> inList = parentChildMap.get(t.getId());
					for (String inId : inList) {
						if(stuNumByTeachClassId.containsKey(inId)){
							t.setStudentNum(t.getStudentNum()+stuNumByTeachClassId.get(inId));
						}
					}
				}
			}
		}
	}

	@Override
	public List<TeachClass> findBySearch(String unitId, String acadyear,
			String semester, String classType,String gradeId,String subjectId) {
		TeachClassSearchDto dto=new TeachClassSearchDto();
		dto.setGradeIds(gradeId);
		dto.setUnitId(unitId);
		dto.setAcadyearSearch(acadyear);
		dto.setSemesterSearch(semester);
		dto.setClassType(classType);
		dto.setSubjectId(subjectId);
		return findListByDto(dto);
	}

	@Override
	public void saveAllTeachClassAndCourseSchedule(TeachClass teachClass,List<TeachClassEx> teachExList,
			List<CourseSchedule> insertList, CourseScheduleDto deleteDto, String oldTeachClassId) {
		saveAllEntitys(teachClass);
		teachClassExService.deleteByTeachClass(new String[]{teachClass.getId()});
		if(deleteDto!=null){
			courseScheduleService.deleteByClassId(deleteDto);
		}
		if(CollectionUtils.isNotEmpty(teachExList)){
			teachClassExService.saveAll(teachExList.toArray(new TeachClassEx[]{}));
		}
		if(CollectionUtils.isNotEmpty(insertList)){
			courseScheduleService.saveAllEntitys(insertList.toArray(new CourseSchedule[]{}));
		}
		if(teachClass.getIsMerge().equals("1")){
			if(!StringUtils.isNotEmpty(oldTeachClassId)){
				//新增
				String teachClassId = teachClass.getTeachClassIds();
				String[] teachClassIdArr = teachClassId.split(",");
				teachClassDao.updateParentId(teachClass.getId(), teachClassIdArr);
			}else {
				//编辑
				teachClassDao.updateParentIdById(null, oldTeachClassId);
				String teachClassId = teachClass.getTeachClassIds();
				String[] teachClassIdArr = teachClassId.split(",");
				teachClassDao.updateParentId(teachClass.getId(), teachClassIdArr);
			}
		}
		// 更新 课表 考勤状态
		if(teachClass!=null && StringUtils.isBlank(oldTeachClassId)){
			Map<String, Integer> punchMap = new HashMap<>();
			punchMap.put(teachClass.getCourseId(),teachClass.getPunchCard());
			courseScheduleService.updatePunchCard(teachClass.getUnitId(),teachClass.getAcadyear(),Integer.parseInt(teachClass.getSemester()),
					teachClass.getId(),punchMap);
		}
	}

	@Override
	public TeachClass findById(String id) {
		return teachClassDao.findById(id).orElse(null);
	}

	@Override
	public void deleteClassAndScheduleById(String id, CourseScheduleDto dto) {
		deleteByIds(id);
		teachClassExService.deleteByTeachClass(new String[]{id});
		if(dto!=null){
			courseScheduleService.deleteByClassId(dto);
		}
	}

	@Override
	public void updateIsUsing(TeachClass oldTeachClass, CourseScheduleDto delDto) {
		save(oldTeachClass);
		if(delDto!=null){
			courseScheduleService.deleteByClassId(delDto);
		}
	}

	@Override
	public List<TeachClass> findByStudentIds(String acadyear, String semester,
			String schoolId, String[] studentId) {
		if(studentId==null || studentId.length==0){
			return new ArrayList<TeachClass>();
		}
		List<TeachClass> returnTeachList=new ArrayList<TeachClass>();
		if(studentId.length<=1000){
			returnTeachList=teachClassDao.findByStudentIds(acadyear,semester,schoolId,studentId);
		}else{
			Set<String> ids=new HashSet<String>();
			int cyc = studentId.length / 1000 + (studentId.length % 1000 == 0 ? 0 : 1);
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > studentId.length)
					max = studentId.length;
				List<TeachClass> returnTeachList1 = teachClassDao.findByStudentIds(acadyear,semester,schoolId,ArrayUtils.subarray(studentId, i * 1000, max));
				if(CollectionUtils.isNotEmpty(returnTeachList1)){
					for(TeachClass t:returnTeachList1){
						if(ids.contains(t.getId())){
							returnTeachList.add(t);
							ids.add(t.getId());
						}
					}
				}
			}
		}
		
		return returnTeachList;
	}

	@Override
	public void insertTeachClassAndCourseSchedule(
			List<TeachClass> teachClassList, List<TeachClassEx> exList,
			List<CourseSchedule> scheduleList,CourseScheduleDto deleteDto) {
		if(deleteDto!=null){
			String[] classIds=deleteDto.getClassIds();
			if(ArrayUtils.isNotEmpty(classIds)){
				teachClassExService.deleteByTeachClass(classIds);
				if(StringUtils.isNotBlank(deleteDto.getAcadyear())){
					//根据acadyear判断删不删课表数据
					courseScheduleService.deleteByClassId(deleteDto);
				}
			}
		}
		
		if(CollectionUtils.isNotEmpty(teachClassList)){
			for(TeachClass class1 : teachClassList){
				class1.setIsUsing(BaseConstants.ONE_STR);
				class1.setIsMerge(BaseConstants.ZERO_STR);
				class1.setIsUsingMerge(BaseConstants.ZERO_STR);
			}
			saveAll(teachClassList.toArray(new TeachClass[]{}));
		}
		if(CollectionUtils.isNotEmpty(exList)){
			teachClassExService.saveAll(exList.toArray(new TeachClassEx[]{}));
		}
		if(CollectionUtils.isNotEmpty(scheduleList)){
			courseScheduleService.saveAllEntitys(scheduleList.toArray(new CourseSchedule[]{}));
		}
		
	}

	@Override
	public void deleteStusByClaIds(String[] ids) {
		if(ArrayUtils.isNotEmpty(ids)){
			teachClassStuDao.deleteByClassIds(ids);
		}
	}

	@Override
	public List<TeachClass> findByParentIds(String[] parentIds) {
		if(ArrayUtils.isEmpty(parentIds)){
			return new ArrayList<TeachClass>();
		}
		return teachClassDao.findByParentIdIn(parentIds);
	}

	@Override
	public List<TeachClass> findByStuIds(String acadyear, String semester,
			String isBigClass, String[] stuIds) {
		List<TeachClass> teachClassList = teachClassStuService.findByStudentIds(stuIds, acadyear, semester);
		if("1".equals(isBigClass)){
			Set<String> classIdSet = new HashSet<String>();
			for(int i = 0; i < teachClassList.size(); i++) {
				String isUsingMerge = teachClassList.get(i).getIsUsingMerge();
				if("1".equals(isUsingMerge)){
					String parentId = teachClassList.get(i).getParentId();
					if(StringUtils.isNotEmpty(parentId)){
						classIdSet.add(parentId);
						teachClassList.remove(i);
						i--;
					}
				}
			}
			List<TeachClass> bigClass = findListByIdIn(classIdSet.toArray(new String[classIdSet.size()]));
			for (TeachClass teachClass : bigClass) {
				teachClassList.add(teachClass);
			}
		}
		return teachClassList;
	}

	@Override
	public List<TeachClass> findBySearch(String unitId, String acadyear, String semester, String... gradeId) {
		TeachClassSearchDto dto=new TeachClassSearchDto();
		dto.setGradeIds(StringUtils.join(gradeId, ","));
		dto.setUnitId(unitId);
		dto.setAcadyearSearch(acadyear);
		dto.setSemesterSearch(semester);
		return findListByDto(dto);
	}

	@Override
	public void deleteByGradeIds(String... gradeIds) {
		if(ArrayUtils.isNotEmpty(gradeIds)){
			courseScheduleService.deleteTeachClassScheduleByGradeIds(gradeIds);
			teachClassStuService.deleteByGradeIds(gradeIds);
			teachClassExService.deleteByGradeIds(gradeIds);
			teachClassDao.deleteByGradeIdIn(gradeIds);
		}
	}

	@Override
	public void deleteBySubjectIds(String... subjectIds) {
		if(ArrayUtils.isNotEmpty(subjectIds)){
			teachClassStuService.deleteBySubjectIds(subjectIds);
			teachClassExService.deleteBySubjectIds(subjectIds);
			teachClassDao.deleteByCourseIdIn(subjectIds);
		}
	}

	@Override
	public List<TeachClass> findbyUnitIdIn(String... unitIds) {
		if(ArrayUtils.isEmpty(unitIds)){
			return new ArrayList<TeachClass>();
		}
		return teachClassDao.findbyUnitIdIn(unitIds);
	}

	@Override
	public Map<String, List<String>> findClassIdsByVirtualIds(String unitId, String acadyear, String semester, String gradeId, String[] virtualIds) {
		if(ArrayUtils.isEmpty(virtualIds)){
			return new HashMap<String, List<String>>();
		}
		Map<String, List<String>> map =  teachClassJdbcDao.findMapByVirtualIds(unitId, acadyear, semester, gradeId, virtualIds);
		return map;
	}

	@Override
	public List<TeachClass> findByRelaCourseIds(String unitId, String acadyear, String semester, String gradeId, String[] virtualIds) {
		if(ArrayUtils.isEmpty(virtualIds)){
			return new ArrayList<TeachClass>();
		}
		return teachClassDao.findByRelaCourseIds(unitId, acadyear, semester, "%"+gradeId+"%", virtualIds);
	}
	
	@Override
	public List<TeachClass> findClassHasEx(String unitId, String acadyear, String semester, String gradeId, String[] classTypes){
		if(ArrayUtils.isEmpty(classTypes)){
			return new ArrayList<TeachClass>();
		}
		return teachClassJdbcDao.findClassHasEx(unitId, acadyear, semester, gradeId, classTypes);
	}

	@Override
	public List<TeachClass> findByUnitIdAndAcadyearAndSemesterAndTeaIds(String unitId, String acadyear, String semester, String[] teacherIds) {
		if(ArrayUtils.isEmpty(teacherIds)){
			return new ArrayList<TeachClass>();
		}
		return teachClassDao.findByUnitIdAndAcadyearAndSemesterAndTeaIds(unitId,acadyear,semester,teacherIds);
	}

	@Override
	public List<TeachClass> findByUnitIdAndTeaIds(String unitId, String[] teacherIds) {
		return teachClassDao.findByUnitIdAndIsDeletedAndTeacherIdIn(unitId,0,teacherIds);
	}
}
