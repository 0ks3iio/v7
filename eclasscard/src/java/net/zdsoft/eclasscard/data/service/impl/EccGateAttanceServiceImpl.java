package net.zdsoft.eclasscard.data.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.dao.EccGateAttanceDao;
import net.zdsoft.eclasscard.data.dto.AttanceSearchDto;
import net.zdsoft.eclasscard.data.entity.EccGateAttance;
import net.zdsoft.eclasscard.data.service.EccGateAttanceService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service("eccGateAttanceService")
public class EccGateAttanceServiceImpl extends BaseServiceImpl<EccGateAttance, String> implements EccGateAttanceService {
	@Autowired
	private EccGateAttanceDao eccGateAttanceDao;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	
	@Override
	protected BaseJpaRepositoryDao<EccGateAttance, String> getJpaDao() {
		return eccGateAttanceDao;
	}

	@Override
	protected Class<EccGateAttance> getEntityClass() {
		return EccGateAttance.class;
	}
	@Override
	public List<EccGateAttance> getMyClassListByCon(String unitId,AttanceSearchDto attDto){
		
		List<EccGateAttance> gateList=null;
		if(StringUtils.isNotBlank(attDto.getClassId())){
			List<Student> stuList=SUtils.dt(studentRemoteService.findByClassIds(new String[]{attDto.getClassId()}),new TR<List<Student>>(){});
			if(CollectionUtils.isNotEmpty(stuList)){
				HashSet<String> stuIds=new HashSet<String>();
				for(Student stu:stuList){
					stuIds.add(stu.getId());
				}
				if(attDto.getAttStatus()!=0){
					gateList=eccGateAttanceDao.getListByCon(attDto.getAttStatus(),stuIds.toArray(new String[0]));
				}else{
					gateList=eccGateAttanceDao.getListByCon(stuIds.toArray(new String[0]));
				}
				
				Map<String,Student> stuMap=EntityUtils.getMap(stuList, "id");
				if(CollectionUtils.isNotEmpty(gateList)){
					for(EccGateAttance gate:gateList){
						Student stu=stuMap.get(gate.getStudentId());
						if(stu!=null){
							gate.setStudentName(stu.getStudentName());
							gate.setStudentCode(stu.getStudentCode());
						}
					}
				}
			}
			
		}
		return gateList;
	}

	@Override
	public EccGateAttance findByStuId(String studentId) {
		return eccGateAttanceDao.findByStuId(studentId);
	}

	@Override
	public void updateStatus(String[] ids, int status) {
		eccGateAttanceDao.updateStatus(status,ids);
	}

	@Override
	public List<EccGateAttance> getSchoolListByCon(final String unitId,List<Clazz> clazzList,
			final AttanceSearchDto attDto,Pagination page) {
		List<EccGateAttance> gateList=null;
		final Set<String> studentIds=new HashSet<String>();
		List<Student> studentList=null;
		Set<String> classIds=null;
		boolean flag=false;
		if(StringUtils.isNotBlank(attDto.getClassId())){
			classIds=new HashSet<String>();
			studentList=SUtils.dt(studentRemoteService.findByClassIds(new String[]{attDto.getClassId()}),new TR<List<Student>>(){});
			classIds.add(attDto.getClassId());
		}else{
			classIds=EntityUtils.getSet(clazzList, "id");
			if(CollectionUtils.isNotEmpty(classIds)){
				studentList=SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[0])),new TR<List<Student>>(){});
			}
			if(StringUtils.isNotBlank(attDto.getGradeId())){
			}else{
				flag=true;//代表全校的学生 直接用unitid来查 为了不超过1000个id in有问题
			}
		}
		if(CollectionUtils.isNotEmpty(studentList)){//
			for(Student stu:studentList){
				studentIds.add(stu.getId());
			}
		}else if(!flag){
			return null;
		}
		Specification<EccGateAttance> specification = new Specification<EccGateAttance>() {
            @Override
            public Predicate toPredicate(Root<EccGateAttance> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                if (studentIds.size()> 0) {
                	String[] stuIds = studentIds.toArray(new String[studentIds.size()]);
                	queryIn("studentId", stuIds, root, ps, null);
//                		In<String> in = cb.in(root.get("studentId").as(String.class));
//                		for (String stuId:studentIds) {
//                			in.value(stuId);
//                		}
//                		ps.add(in);
                }
            	ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
                if(attDto.getAttStatus()!=0){
                	ps.add(cb.equal(root.get("status").as(Integer.class), attDto.getAttStatus()));
                }
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.asc(root.get("status").as(Integer.class)));

                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
        if (page != null) {
            Pageable pageable = Pagination.toPageable(page);
            Page<EccGateAttance> findAll = eccGateAttanceDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());
            gateList = findAll.getContent();
        }
        else {
        	gateList = eccGateAttanceDao.findAll(specification);
        }
        //全校的情况 需要再获取学生信息
        if(CollectionUtils.isEmpty(studentList)){
        	studentList=SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[0])),new TR<List<Student>>(){});
        }
        Map<String,Student> studentMap=EntityUtils.getMap(studentList,"id");
        Map<String,Clazz> clazzMap=EntityUtils.getMap(clazzList,"id");
        Map<String,String> teacherNameMap=EntityUtils.getMap(SUtils.dt(teacherRemoteService.findByUnitId(unitId),new TR<List<Teacher>>(){}),"id","teacherName");
        Map<String,Grade> gradeMap=EntityUtils.getMap(SUtils.dt(gradeRemoteService.findBySchoolId(unitId),new TR<List<Grade>>(){}),"id");
        List<EccGateAttance> gateListLast=new ArrayList<EccGateAttance>();
        //赋值页面数据信息
        if(CollectionUtils.isNotEmpty(gateList)){
        	for(EccGateAttance gate:gateList){
    			Student stu=studentMap.get(gate.getStudentId());
    			if(stu!=null){
    				gate.setStudentName(stu.getStudentName());
    				gate.setStudentCode(stu.getStudentCode());
    				Clazz clazz=clazzMap.get(stu.getClassId());
    				if(clazz!=null){
    					gate.setClassName(clazz.getClassName());
    					gate.setTeacherName(teacherNameMap.get(clazz.getTeacherId()));
    					Grade grade=gradeMap.get(clazz.getGradeId());
    					if(grade!=null){
    						gate.setGradeName(grade.getGradeName());
    						gate.setGradeCode(grade.getGradeCode());
    					}
    				}
    			}
    			gateListLast.add(gate);
        	}
        	//按年级 班级排序
        	if(CollectionUtils.isNotEmpty(gateListLast)){
        		Collections.sort(gateListLast, new Comparator<EccGateAttance>(){
					@Override
					public int compare(EccGateAttance o1, EccGateAttance o2) {
						if(o1.getGradeCode()!=null && o2.getGradeCode()!=null){
							if(o1.getGradeCode().compareTo(o2.getGradeCode())==0){
								return o1.getClassName().compareTo(o2.getClassName());
							}else{
								return o1.getGradeCode().compareTo(o2.getGradeCode());
							}
						}
						return 0;
					}
        			
        		});
        	}
        }
		return gateListLast;
	}
}
