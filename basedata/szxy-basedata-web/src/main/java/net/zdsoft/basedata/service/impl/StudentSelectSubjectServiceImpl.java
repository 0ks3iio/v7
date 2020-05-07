package net.zdsoft.basedata.service.impl;

import java.util.*;
import java.util.Map.Entry;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dao.StudentSelectSubjectDao;
import net.zdsoft.basedata.dao.StudentSelectSubjectJdbcDao;
import net.zdsoft.basedata.dto.StudentSelectSubjectDto;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.StudentSelectSubjectService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.StudentSelectSubject;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.UuidUtils;

@Service("studentSelectSubjectService")
public class StudentSelectSubjectServiceImpl extends BaseServiceImpl<StudentSelectSubject, String> implements StudentSelectSubjectService {

    @Autowired
    private StudentService studentService;
    @Autowired
    private ClassService classService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private StudentSelectSubjectDao studentSelectSubjectDao;
    @Autowired
    private StudentSelectSubjectJdbcDao studentSelectSubjectJdbcDao;
    @Autowired
    private CourseService courseService; 

    @Override
    public List<StudentSelectSubjectDto> findStuSelectByClass(List<StudentSelectSubject> studentSelectSubjectList, String classId) {
        List<StudentSelectSubjectDto> returnList= new ArrayList<>();
        
        Map<String,List<String>> subjectByStudentId = new HashMap<>();
        for (StudentSelectSubject one : studentSelectSubjectList) {
            if (!subjectByStudentId.containsKey(one.getStudentId())) {
                subjectByStudentId.put(one.getStudentId(), new ArrayList<String>());
            }
            subjectByStudentId.get(one.getStudentId()).add(one.getSubjectId());
        }
        Clazz clazz = classService.findOne(classId);
        //按班级取学生
        List<Student> studentList  = studentService.findByClassIds(classId);
        if (CollectionUtils.isEmpty(studentList)) {
            return returnList;
        }
        StudentSelectSubjectDto temp = null;
        for (Student student : studentList) {
            temp = toMakeDto(student);
            temp.setClassName(clazz.getClassName());
            if (subjectByStudentId.containsKey(temp.getStudentId())) {
                temp.setSelectList(subjectByStudentId.get(temp.getStudentId()));
            }
            returnList.add(temp);
        }
        return returnList;
    }

    @Override
    public Map<String, Set<String>> findStuSelectMapByGradeId(String acadyear, Integer semester, String gradeId) {
        Map<String, Set<String>> returnMap = new HashMap<>();
        Grade grade = gradeService.findById(gradeId);
        if (grade == null) {
            return returnMap;
        }
        List<StudentSelectSubject> studentSelectSubjectList = RedisUtils.getObject("STUSELECTALLBYSCHOOL" + acadyear + semester + grade.getSchoolId(), RedisUtils.TIME_ONE_MINUTE, new TypeReference<List<StudentSelectSubject>>() {
        }, new RedisInterface<List<StudentSelectSubject>>() {
            @Override
            public List<StudentSelectSubject> queryData() {
                List<StudentSelectSubject> list = studentSelectSubjectDao.findBySchoolId(acadyear, semester, grade.getSchoolId());
                return list;
            }
        });
        for (StudentSelectSubject one : studentSelectSubjectList) {
            if (gradeId.equals(one.getGradeId())) {
                if (returnMap.containsKey(one.getStudentId())) {
                    returnMap.get(one.getStudentId()).add(one.getSubjectId());
                } else {
                    returnMap.put(one.getStudentId(), new HashSet<>());
                    returnMap.get(one.getStudentId()).add(one.getSubjectId());
                }
            }
        }
        
      //技术拆分成： 技术（通用技术）+信息技术
		List<Course> list= courseService.findBySubjectCodes(BaseConstants.SUBJECT_73_1);
		if(CollectionUtils.isNotEmpty(list)){
			Set<String>stuids=null;
			for(Course ent:list){
				if(returnMap.containsKey(ent.getId())){
					stuids=returnMap.get(ent.getId());
					break;
				}
			}
			for(Course ent:list){
				if(!returnMap.containsKey(ent.getId())){
					returnMap.put(ent.getId(), stuids);
				}
			}
			
		}
		
        return returnMap;
    }

    @Override
    public void updateStudentSelect(String acadyear, Integer semester, String gradeId, String studentId, String subjectIds) {
        studentSelectSubjectDao.deleteByStudentId(acadyear, semester, studentId);
        Student student = studentService.findOne(studentId);
        if (StringUtils.isBlank(subjectIds)) {
            RedisUtils.del("STUSELECTALLBYSCHOOL" + acadyear + semester + student.getSchoolId());
            return;
        }
        String[] subjectIdsArr = subjectIds.split(",");
        List<StudentSelectSubject> resultList = new ArrayList<>();
        for (String one : subjectIdsArr) {
            StudentSelectSubject studentSelect = new StudentSelectSubject();
            studentSelect.setId(UuidUtils.generateUuid());
            studentSelect.setAcadyear(acadyear);
            studentSelect.setSemester(semester);
            studentSelect.setSchoolId(student.getSchoolId());
            studentSelect.setGradeId(gradeId);
            studentSelect.setStudentId(studentId);
            studentSelect.setSubjectId(one);
            studentSelect.setCreationTime(new Date());
            studentSelect.setModifyTime(new Date());
            studentSelect.setIsDeleted(0);
            resultList.add(studentSelect);
        }
        studentSelectSubjectJdbcDao.saveAll(resultList);
        RedisUtils.del("STUSELECTALLBYSCHOOL"  + acadyear + semester + student.getSchoolId());
    }

    @Override
    public void updateStudentSelects(String unitId, String acadyear, Integer semester, String[] studentIds, List<StudentSelectSubject> insertList) {
        studentSelectSubjectJdbcDao.deleteAllByStudentId(acadyear, semester, studentIds);
        if (CollectionUtils.isEmpty(insertList)) {
            RedisUtils.del("STUSELECTALLBYSCHOOL" + acadyear + semester + unitId);
            return;
        }
        studentSelectSubjectJdbcDao.saveAll(insertList);
        RedisUtils.del("STUSELECTALLBYSCHOOL" + acadyear + semester + unitId);

    }

    @Override
    public void updateStudentSelectsByGradeId(String unitId, String acadyear, Integer semester, String gradeId, List<StudentSelectSubject> resultList) {
        studentSelectSubjectJdbcDao.deleteAllByGradeId(acadyear, semester, gradeId);
        if (CollectionUtils.isEmpty(resultList)) {
            RedisUtils.del("STUSELECTALLBYSCHOOL" + acadyear + semester + unitId);
            return;
        }
        studentSelectSubjectJdbcDao.saveAll(resultList);
        RedisUtils.del("STUSELECTALLBYSCHOOL" + acadyear + semester + unitId);
    }

    private StudentSelectSubjectDto toMakeDto(Student student){
        StudentSelectSubjectDto studentDto=new StudentSelectSubjectDto();
        studentDto.setClassId(student.getClassId());
        studentDto.setSex(student.getSex()==null?"":String.valueOf(student.getSex()));
        studentDto.setStudentCode(student.getStudentCode());
        studentDto.setStudentId(student.getId());
        studentDto.setStudentName(student.getStudentName());
        studentDto.setClassName(student.getClassName());
        return studentDto;
    }

    @Override
    protected BaseJpaRepositoryDao<StudentSelectSubject, String> getJpaDao() {
        return studentSelectSubjectDao;
    }

    @Override
    protected Class<StudentSelectSubject> getEntityClass() {
        return StudentSelectSubject.class;
    }

	@Override
	public Map<String, Set<String>> findStuBySubjectMap(String acadyear, Integer semester, String gradeId,boolean isXuankao,String[] subjectIds) {
		Map<String, Set<String>> returnMap = new HashMap<>();
		if(subjectIds==null || subjectIds.length==0) {
			 return returnMap;
		}
		
		List<StudentSelectSubject> studentSelectSubjectList = studentSelectSubjectDao.findByGrade(acadyear, semester, gradeId);
      
        if(isXuankao) {
			if(CollectionUtils.isEmpty(studentSelectSubjectList)) {
				return new HashMap<>();
			}
			List<String> subjectList = Arrays.asList(subjectIds);
			for(StudentSelectSubject r:studentSelectSubjectList) {
				if(!subjectList.contains(r.getSubjectId())) {
					continue;
				}
				if(!returnMap.containsKey(r.getSubjectId())) {
					returnMap.put(r.getSubjectId(), new HashSet<>());
				}
				returnMap.get(r.getSubjectId()).add(r.getStudentId());
			}
			
			//技术拆分成： 技术（通用技术）+信息技术
			List<Course> list= courseService.findBySubjectCodes(BaseConstants.SUBJECT_73_1);
			if(CollectionUtils.isNotEmpty(list)){
				Set<String>stuids=null;
				for(Course ent:list){
					if(returnMap.containsKey(ent.getId())){
						stuids=returnMap.get(ent.getId());
						break;
					}
				}
				for(Course ent:list){
					if(!returnMap.containsKey(ent.getId())){
						returnMap.put(ent.getId(), stuids);
					}
				}
				
			}
		}else {
			Map<String,Set<String>> stuMap=new HashMap<>();
			for(StudentSelectSubject r:studentSelectSubjectList) {
				if(!stuMap.containsKey(r.getStudentId())) {
					stuMap.put(r.getStudentId(), new HashSet<>());
				}
				stuMap.get(r.getStudentId()).add(r.getSubjectId());
			}
			String stuId;
			Set<String> chooseSub;
			for(String sub: subjectIds) {
				returnMap.put(sub, new HashSet<>());
			}
			for(Entry<String, Set<String>> stuSub:stuMap.entrySet()) {
				stuId = stuSub.getKey();
				chooseSub = stuSub.getValue();
				for(String sub: subjectIds) {
					if(!chooseSub.contains(sub)) {
						returnMap.get(sub).add(stuId);
					}
				}
			}
		}
		return returnMap;
        
	}

	@Override
	public void deleteByStudentIds(String... studentIds) {
		studentSelectSubjectDao.deleteByStudentIds(studentIds);
	}

	@Override
	public void deleteBySubjectIds(String... subjectIds) {
		studentSelectSubjectDao.deleteBySubjectIds(subjectIds);
	}

	@Override
	public List<StudentSelectSubject> findListBySchoolIdWithMaster(String acadyear, Integer semester, String schoolId) {
		List<StudentSelectSubject> studentSelectSubjectList = RedisUtils.getObject("STUSELECTALLBYSCHOOL" + acadyear + semester + schoolId, RedisUtils.TIME_ONE_MINUTE, new TypeReference<List<StudentSelectSubject>>() {
        }, new RedisInterface<List<StudentSelectSubject>>() {
            @Override
            public List<StudentSelectSubject> queryData() {
                List<StudentSelectSubject> list = studentSelectSubjectDao.findBySchoolId(acadyear, semester, schoolId);
                return list;
            }
        });
		return studentSelectSubjectList;
	}
}
