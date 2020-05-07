package net.zdsoft.studevelop.data.service.impl;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.dao.StudevelopMonthPerformanceDao;
import net.zdsoft.studevelop.data.entity.StudevelopMonthPerformance;
import net.zdsoft.studevelop.data.service.StudevelopMonthPerformanceService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * studevelop_month_performance
 * @author 
 * 
 */
@Service("studevelopMonthPerformanceService")
public class StudevelopMonthPerformanceServiceImpl extends BaseServiceImpl<StudevelopMonthPerformance, String> 
		implements StudevelopMonthPerformanceService{
	@Autowired
	private StudevelopMonthPerformanceDao studevelopMonthPerformanceDao;

	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Override
	public Integer delete(String[] ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteByStudentIds(String unitId, String acadyear, int semester, int performanceMouth ,String itemId, String[] studentIds) {
		studevelopMonthPerformanceDao.deleteByStudentIds(unitId,acadyear,semester,performanceMouth , itemId,studentIds);
	}

	@Override
	public List<StudevelopMonthPerformance> getMonthPermanceByStuId(String unitId, String acadyear, int semester, int performanceMouth, String studentId ,String itemId) {
		return studevelopMonthPerformanceDao.getMonthPermanceByStuId(unitId,acadyear,semester,performanceMouth,studentId ,itemId);
	}
	@Override
	public void saveMonthPermance(StudevelopMonthPerformance studevelopMonthPerformance ,String classId,String unitId){
		if(StringUtils.isEmpty(studevelopMonthPerformance.getStudentId())){
//			List<Student> studentList = studentService.findByClassIds(classId);
            List<Student> studentList = Student.dt(studentRemoteService.findByClassIds(classId));
			List<StudevelopMonthPerformance> performanceList = new ArrayList<StudevelopMonthPerformance>();
			List<String> stuIds = new ArrayList<String>();
			for(Student stu : studentList){
				StudevelopMonthPerformance performance = new StudevelopMonthPerformance();
				performance.setStudentId(stu.getId());
				performance.setAcadyear(studevelopMonthPerformance.getAcadyear());
				performance.setSemester(Integer.valueOf(studevelopMonthPerformance.getSemester()));
				performance.setItemId(studevelopMonthPerformance.getItemId());
				performance.setResultId(studevelopMonthPerformance.getResultId());
				performance.setUnitId(unitId);
				performance.setPerformMonth(studevelopMonthPerformance.getPerformMonth());
				performance.setId(UuidUtils.generateUuid());
				performance.setCreationTime(new Date());
				performanceList.add(performance);
				stuIds.add(stu.getId());
			}
			List<StudevelopMonthPerformance> performanceList1 = getMonthPermanceListByStuIds(unitId,studevelopMonthPerformance.getAcadyear(),studevelopMonthPerformance.getSemester(),String.valueOf(studevelopMonthPerformance.getPerformMonth()),studevelopMonthPerformance.getItemId(),stuIds.toArray(new String[0]));
			if(CollectionUtils.isNotEmpty(performanceList1)){
				deleteAll(performanceList1.toArray(new StudevelopMonthPerformance[0]));
			}
			saveAll(performanceList.toArray(new StudevelopMonthPerformance[0]));
		}else {
			List<StudevelopMonthPerformance> performances = getMonthPermanceByStuId(unitId,studevelopMonthPerformance.getAcadyear(),studevelopMonthPerformance.getSemester(),studevelopMonthPerformance.getPerformMonth(),studevelopMonthPerformance.getStudentId(),studevelopMonthPerformance.getItemId());
			StudevelopMonthPerformance performance=null;
			if(CollectionUtils.isNotEmpty(performances)){
				performance = performances.get(0);
			}
			if(performance != null){
				performance.setItemId(studevelopMonthPerformance.getItemId());
				performance.setResultId(studevelopMonthPerformance.getResultId());
				performance.setModifyTime(new Date());
				update(performance,performance.getId(),new String[]{"itemId","resultId"});
			}else{
				studevelopMonthPerformance.setId(UuidUtils.generateUuid());
				studevelopMonthPerformance.setCreationTime(new Date());
				studevelopMonthPerformance.setUnitId(unitId);
				save(studevelopMonthPerformance);
			}

		}
	}

	@Override
	public List<StudevelopMonthPerformance> getMonthPermanceListByStuIds(final String unitId, final String acadyear, final int semester, final String performMonth, final String itemId, final String[] studentIds) {
		Specification<StudevelopMonthPerformance> specification = new Specification<StudevelopMonthPerformance>() {
			@Override
			public Predicate toPredicate(Root<StudevelopMonthPerformance> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("unitId").as(String.class),unitId));
				list.add(cb.equal(root.get("acadyear").as(String.class),acadyear));
				list.add(cb.equal(root.get("semester").as(Integer.class),semester));
				if(StringUtils.isNotEmpty(performMonth)){
					list.add(cb.equal(root.get("performMonth").as(Integer.class),String.valueOf(performMonth)));
				}

				if(StringUtils.isNotEmpty(itemId)){
					list.add(cb.equal(root.get("itemId").as(String.class),itemId));
				}
				if(ArrayUtils.isNotEmpty(studentIds)){
					CriteriaBuilder.In<String>  in = cb.in(root.get("studentId").as(String.class));
					for(String str : studentIds){
						in.value(str);
					}
					list.add(in);
				}
				Predicate[] p = new Predicate[list.size()];
				query.where(list.toArray(p));
				return query.getRestriction();
			}
		};
//		return studevelopMonthPerformanceDao.getMonthPermanceListByStuIds(unitId,acadyear,semester,performanceMouth,itemId,studentIds);
		return studevelopMonthPerformanceDao.findAll(specification);
	}

	@Override
	public  String saveCopyLastMouth(StudevelopMonthPerformance studevelopMonthPerformance, String unitId) {
		String classId = studevelopMonthPerformance.getClassId();
        List<Student> studentList = Student.dt(studentRemoteService.findByClassIds(classId));
		List<String> stuIds = new ArrayList<String>();
		if(CollectionUtils.isEmpty(studentList)){
			return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("该班级下没有学生， 不能复制！"));
		}
		for(Student stu : studentList){
			stuIds.add(stu.getId());
		}
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(),new TR<List<String>>(){});

		String acadyear = studevelopMonthPerformance.getAcadyear();
		int index =acadyearList.indexOf(acadyear);
		int mouth = studevelopMonthPerformance.getPerformMonth();
		if(mouth == 1){
			acadyear = acadyearList.get(index +1);
			mouth =12;
		}else{
			mouth--;
		}

		List<StudevelopMonthPerformance> performanceLastList = getMonthPermanceListByStuIds(unitId,acadyear,studevelopMonthPerformance.getSemester(),String.valueOf(mouth),null,stuIds.toArray(new String[0]));
		List<StudevelopMonthPerformance> performanceCurrentList = getMonthPermanceListByStuIds(unitId,studevelopMonthPerformance.getAcadyear(),studevelopMonthPerformance.getSemester(),String.valueOf(studevelopMonthPerformance.getPerformMonth()),null,stuIds.toArray(new String[0]));
		List<StudevelopMonthPerformance> performanceList = new ArrayList<StudevelopMonthPerformance>();
		if(CollectionUtils.isNotEmpty(performanceLastList)){
			for(StudevelopMonthPerformance performance : performanceLastList){
				StudevelopMonthPerformance p = new StudevelopMonthPerformance();
				p.setId(UuidUtils.generateUuid());
				p.setUnitId(performance.getUnitId());
				p.setCreationTime(new Date());
				p.setResultId(performance.getResultId());
				p.setItemId(performance.getItemId());
				p.setPerformMonth(studevelopMonthPerformance.getPerformMonth());
				p.setAcadyear(studevelopMonthPerformance.getAcadyear());
				p.setSemester(studevelopMonthPerformance.getSemester());
				p.setStudentId(performance.getStudentId());
				performanceList.add(p);
			}
		}else{
			return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("上个月不存在每月在校表现数据， 不能复制！"));
		}
		try{
			if(CollectionUtils.isNotEmpty(performanceCurrentList)){
				deleteAll(performanceCurrentList.toArray(new StudevelopMonthPerformance[0]));
			}

			saveAll(performanceList.toArray(new StudevelopMonthPerformance[0]));
		}catch (Exception e){
			e.printStackTrace();
			return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("操作失败！"));
		}

		return  Json.toJSONString(new ResultDto().setSuccess(true).setCode("00").setMsg("操作成功！"));
	}

	@Override
	public void deleteByItemCodeId(String unitId, String itemcodeId) {
		studevelopMonthPerformanceDao.deleteByItemCodeId(unitId , itemcodeId);
	}

	@Override
	public void deleteByItemId(String unitId, String itemId) {
		studevelopMonthPerformanceDao.deleteByItemId(unitId,itemId);
	}

	@Override
	public List<StudevelopMonthPerformance> getStudevelopMonthPerformanceByItemIds(String unitId, String acadyear, int semester, int performanceMouth, String[] itemIds) {
		return studevelopMonthPerformanceDao.getStudevelopMonthPerformanceByItemIds(unitId,acadyear,semester,performanceMouth,itemIds);
	}

	@Override
	public List<StudevelopMonthPerformance> getMonthPermanceListByStuId(String unitId, String acadyear, int semester, String studentId) {
		return studevelopMonthPerformanceDao.getMonthPermanceListByStuId(unitId,acadyear,semester,studentId);
	}

	@Override
	protected BaseJpaRepositoryDao<StudevelopMonthPerformance, String> getJpaDao() {
		return studevelopMonthPerformanceDao;
	}

	@Override
	protected Class<StudevelopMonthPerformance> getEntityClass() {
		return StudevelopMonthPerformance.class;
	}

	
}
