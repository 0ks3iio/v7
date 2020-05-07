package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Objects;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.ArrayUtil;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkDivideStusubDao;
import net.zdsoft.newgkelective.data.dao.NewGkDivideStusubJdbcDao;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideStusub;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkChoResultService;
import net.zdsoft.newgkelective.data.service.NewGkDivideStusubService;

@Service("newGkDivideStusubService")
public class NewGkDivideStusubServiceImpl extends BaseServiceImpl<NewGkDivideStusub, String> implements NewGkDivideStusubService {
	@Autowired
	private NewGkDivideStusubDao newGkDivideStusubDao;
	@Autowired
	private NewGkDivideStusubJdbcDao newGkDivideStusubJdbcDao;
	@Autowired
	private NewGkChoResultService newGkChoResultService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	
	@Override
	protected BaseJpaRepositoryDao<NewGkDivideStusub, String> getJpaDao() {
		
		return newGkDivideStusubDao;
	}

	@Override
	protected Class<NewGkDivideStusub> getEntityClass() {
		return NewGkDivideStusub.class;
	}

	@Override
	public String saveChoiceResult(NewGkDivide newGkDivide,boolean isSix,String[] subjectIdArr) {
		String choiceId=newGkDivide.getChoiceId();
		String divideId=newGkDivide.getId();
		String unitid=newGkDivide.getUnitId();
		List<NewGkChoResult> list = newGkChoResultService.findByChoiceIdAndKindType(unitid, NewGkElectiveConstant.KIND_TYPE_01,choiceId);
		if(CollectionUtils.isEmpty(list)) {
			return "选中的选课结果并没有选课数据。";
		}
		//正常学生
		List<Student> stuList = Student.dt(studentRemoteService.findByClaIdsLikeStuCodeNames(unitid,newGkDivide.getGradeId(), null, null));
		if(CollectionUtils.isEmpty(stuList)) {
			return "年级下没有学生，数据有误。";
		}
		List<String> subjectIdList = newGkChoRelationService.findByChoiceIdAndObjectType(unitid,choiceId, NewGkElectiveConstant.CHOICE_TYPE_01);
		if(CollectionUtils.isEmpty(subjectIdList)) {
			return "选中的选课方案，选课科目有问题。";
		}
		if(isSix) {
			if(subjectIdList.size()!=6) {
				return "选课设置中科目范围不是完全的3+1+2模式。";
			}
			if(subjectIdArr==null) {
				List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIdList.toArray(new String[0])),Course.class);
				subjectIdArr = new String[2];
				int i=0;
				for(Course c:courseList) {
					if(NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(c.getSubjectCode())) {
						subjectIdArr[i]=c.getId();
						i++;
					}
					if(NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(c.getSubjectCode())) {
						subjectIdArr[i]=c.getId();
						i++;
					}
				}
				if(subjectIdArr==null || subjectIdArr.length!=2) {
					return "选课设置中科目范围不是完全的3+1+2模式。";
				}
			}
		}
		Map<String,List<String>> subjectIdsByStuId=EntityUtils.getListMap(list, "studentId", "subjectId");
		Map<String, Student> stuMap = EntityUtils.getMap(stuList, e->e.getId());
		List<NewGkDivideStusub> insertList=new ArrayList<NewGkDivideStusub>();
		NewGkDivideStusub sub=null;
		Student stu=null;
		NewGkDivideStusub sub1=null;
		for(Entry<String, List<String>> item:subjectIdsByStuId.entrySet()) {
			if(!stuMap.containsKey(item.getKey())) {
				//学生已经不是正常数据
				continue;
			}
			List<String> choose = item.getValue();
			if(isSix) {
				
				if(choose.contains(subjectIdArr[0]) && choose.contains(subjectIdArr[1])) {
					return "选课方案不是3+1+2模式。";
				}else if(!choose.contains(subjectIdArr[0]) && !choose.contains(subjectIdArr[1])) {
					return "选课方案不是3+1+2模式。";
				}
			}
			stu = stuMap.get(item.getKey());
			sub=new NewGkDivideStusub();
			sub.setChoiceId(choiceId);
			sub.setDivideId(divideId);
			sub.setStudentCode(stu.getStudentCode());
			sub.setStudentName(stu.getStudentName());
			sub.setClassId(stu.getClassId());
			sub.setClassName(stu.getClassName());
			sub.setUnitId(unitid);
			sub.setStudentId(stu.getId());
			if(Objects.equal(stu.getSex(), 2)) {
				sub.setStudentSex("女");
			}else {
				sub.setStudentSex("男");
			}
			sub.setCreationTime(new Date());
			sub.setModifyTime(new Date());
			sub1=new NewGkDivideStusub();
			sub1=EntityUtils.copyProperties(sub, sub1);
			sub.setId(UuidUtils.generateUuid());
			sub1.setId(UuidUtils.generateUuid());
			sub.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_A);
			
			
			String[] chooseArr = choose.toArray(new String[0]);
			Arrays.sort(chooseArr);
			sub.setSubjectIds(ArrayUtil.print(chooseArr));
			insertList.add(sub);
			sub1.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_B);
			Set<String> noChoose=new HashSet<>();
			noChoose.addAll(subjectIdList);
			noChoose.removeAll(choose);
			chooseArr = noChoose.toArray(new String[0]);
			Arrays.sort(chooseArr);
			sub1.setSubjectIds(ArrayUtil.print(chooseArr));
			insertList.add(sub1);
		}
		if(CollectionUtils.isEmpty(insertList)) {
			return "年级下学生选课数据不存在。";
		}
		newGkDivideStusubDao.deleteByChoiceIdAndDivideId(choiceId,newGkDivide.getId());
		saveAll(insertList.toArray(new NewGkDivideStusub[0]));
		
		
		return null;
	}

	@Override
	public void deleteByChoiceIdAndDivideId(String choiceId, String divideId) {
		newGkDivideStusubDao.deleteByChoiceIdAndDivideId(choiceId,divideId);
	}

	@Override
	public List<NewGkDivideStusub> findByDivideIdWithMaster(String divideId, String subjectType,String[] subjectIdArr) {
		return newGkDivideStusubJdbcDao.findByDivideIdAndSubject(divideId,subjectType,subjectIdArr);
	}

	@Override
	public List<NewGkDivideStusub> findListByStudentIdsWithMaster(String divideId,String subjectType, String[] studentIds) {
		return newGkDivideStusubDao.findByDivideIdAndStudentIdIn(divideId,subjectType,studentIds);
	}

	@Override
	public List<NewGkDivideStusub> findNoArrangeXzbStudentWithMaster(String divideId, String[] subjectIdArr) {
		return newGkDivideStusubJdbcDao.findNoArrangeXzbStudent(divideId, subjectIdArr);
	}

	@Override
	public void deleteByChoiceIdAndDivideIdAndStudentId(String choiceId, String divideId, String studentId) {
		newGkDivideStusubDao.deleteByChoiceIdAndDivideIdAndStudentId(choiceId,divideId, studentId);
	}
	
	

}
