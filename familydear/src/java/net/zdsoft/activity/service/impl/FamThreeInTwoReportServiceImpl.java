package net.zdsoft.activity.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
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

import net.zdsoft.activity.dao.FamThreeInTwoReportDao;
import net.zdsoft.activity.entity.FamDearThreeInTwoReport;
import net.zdsoft.activity.entity.FamDearThreeInTwoStu;
import net.zdsoft.activity.service.FamThreeInTwoReportService;
import net.zdsoft.activity.service.FamThreeInTwoStuService;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

@Service("FamThreeInTwoReportService")
public class FamThreeInTwoReportServiceImpl extends BaseServiceImpl<FamDearThreeInTwoReport,String> implements FamThreeInTwoReportService{
	
	@Autowired
	private FamThreeInTwoReportDao famThreeInTwoReportDao;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private FamThreeInTwoStuService famThreeInTwoStuService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private DeptRemoteService deptRemoteService;

	@Override
	public List<FamDearThreeInTwoReport> getFamDearThreeInTwoReportByUnitIdAndOthers(
			String unitId, String userId, Date startTime, Date endTime,
			String title, String[] stuId,Pagination pagination) {
		List<FamDearThreeInTwoReport> famDearThreeInTwoReports=new ArrayList<>();
		Specification<FamDearThreeInTwoReport> specification = new Specification<FamDearThreeInTwoReport>() {

			@Override
			public Predicate toPredicate(Root<FamDearThreeInTwoReport> root,
					CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> ps = new ArrayList<Predicate>();
				if(startTime!=null){
                    ps.add(criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("startTime"), startTime));
                }
                if(endTime!=null){
                    ps.add(criteriaBuilder.lessThanOrEqualTo(root.<Date> get("endTime"), endTime));
                }
                ps.add(criteriaBuilder.equal(root.get("unitId").as(String.class), unitId));
                ps.add(criteriaBuilder.equal(root.get("isDelete").as(String.class), "0"));
                if(StringUtils.isNotBlank(userId)){
                    ps.add(criteriaBuilder.equal(root.get("createUserId").as(String.class),userId));
                }
                if(StringUtils.isNotBlank(title)){
                	ps.add(criteriaBuilder.equal(root.get("title").as(String.class),title));
                }
				if (stuId!=null&&stuId.length>0) {
					ps.add(criteriaBuilder.or(Arrays.stream(stuId).map(e->criteriaBuilder.like(root.<String>get("objStu"), "%"+e+"%")).toArray(Predicate[]::new)));
				}
				List<Order> orderList = new ArrayList<>();
				orderList.add(criteriaBuilder.desc(root.get("createTime").as(Date.class)));
				orderList.add(criteriaBuilder.asc(root.get("id").as(String.class)));
				criteriaQuery.where(ps.toArray(new Predicate[0])).orderBy(orderList);
				return criteriaQuery.getRestriction();
			}
			
		};
		if (pagination != null) {
			Pageable pageable = Pagination.toPageable(pagination);
			Page<FamDearThreeInTwoReport> findAll = famThreeInTwoReportDao.findAll(specification, pageable);
			pagination.setMaxRowCount((int) findAll.getTotalElements());
			famDearThreeInTwoReports = findAll.getContent();
		}
		else {
			famDearThreeInTwoReports=famThreeInTwoReportDao.findAll(specification);
		}
		SetThings(famDearThreeInTwoReports);
		return famDearThreeInTwoReports;
	}
	
	@Override
	public List<FamDearThreeInTwoReport> getFamDearThreeInTwoReportsByUnitIdAndUserIds(
			String unitId, String[] userId, Date startTime, Date endTime,
			String title, Pagination pagination) {
		List<FamDearThreeInTwoReport> famDearThreeInTwoReports=new ArrayList<>();
		Specification<FamDearThreeInTwoReport> specification = new Specification<FamDearThreeInTwoReport>() {

			@Override
			public Predicate toPredicate(Root<FamDearThreeInTwoReport> root,
					CriteriaQuery<?> criteriaquery,
					CriteriaBuilder criteriabuilder) {
				List<Predicate> ps = new ArrayList<Predicate>();
				if(startTime!=null){
                    ps.add(criteriabuilder.greaterThanOrEqualTo(root.<Date> get("startTime"), startTime));
                }
                if(endTime!=null){
                    ps.add(criteriabuilder.lessThanOrEqualTo(root.<Date> get("endTime"), endTime));
                }
                ps.add(criteriabuilder.equal(root.get("unitId").as(String.class), unitId));
                ps.add(criteriabuilder.equal(root.get("isDelete").as(String.class), "0"));
                if(StringUtils.isNotBlank(title)){
                	ps.add(criteriabuilder.equal(root.get("title").as(String.class), title));
                }
                if(ArrayUtils.isNotEmpty(userId)){
                	ps.add(root.<String>get("createUserId").in(userId));
                }
                List<Order> orderList = new ArrayList<>();
				orderList.add(criteriabuilder.desc(root.get("createTime").as(Date.class)));
				orderList.add(criteriabuilder.asc(root.get("id").as(String.class)));
				criteriaquery.where(ps.toArray(new Predicate[0])).orderBy(orderList);
				return criteriaquery.getRestriction();
			}
			
		};
		if (pagination != null) {
			Pageable pageable = Pagination.toPageable(pagination);
			Page<FamDearThreeInTwoReport> findAll = famThreeInTwoReportDao.findAll(specification, pageable);
			pagination.setMaxRowCount((int) findAll.getTotalElements());
			famDearThreeInTwoReports = findAll.getContent();
		}
		else {
			famDearThreeInTwoReports=famThreeInTwoReportDao.findAll(specification);
		}
		SetThings(famDearThreeInTwoReports);
		return famDearThreeInTwoReports;
	}

	private void SetThings(List<FamDearThreeInTwoReport> famDearThreeInTwoReports){
		if(CollectionUtils.isNotEmpty(famDearThreeInTwoReports)){
			Set<String> stuSet=new HashSet<>();
			Set<String> userSet=new HashSet<>();
			for (FamDearThreeInTwoReport famDearThreeInTwoReport : famDearThreeInTwoReports) {
				userSet.add(famDearThreeInTwoReport.getCreateUserId());
				String objStu=famDearThreeInTwoReport.getObjStu();
				if(StringUtils.isNotBlank(objStu)){
					String[] stu=objStu.split(",");
					for (String string : stu) {
						stuSet.add(string);
					}
				}
			}
			List<User> users=userRemoteService.findListObjectByIds(userSet.toArray(new String[0]));
			Map<String,User> userMap=EntityUtils.getMap(users, e->e.getId());
			Set<String> teaSet=EntityUtils.getSet(users, e->e.getOwnerId());
			List<Teacher> teachers=teacherRemoteService.findListObjectByIds(teaSet.toArray(new String[0]));
			Map<String,Teacher> teaMap=EntityUtils.getMap(teachers, e->e.getId());
			Set<String> deptSet=EntityUtils.getSet(teachers, e->e.getDeptId());
			List<Dept> depts=SUtils.dt(deptRemoteService.findListByIds(deptSet.toArray(new String[0])),Dept.class);
			Map<String,Dept> deptMap=EntityUtils.getMap(depts, e->e.getId());
			List<FamDearThreeInTwoStu> famDearThreeInTwoStus=famThreeInTwoStuService.findListByIdIn(stuSet.toArray(new String[0]));
			Map<String,FamDearThreeInTwoStu> stuMap=new HashMap<>();
			stuMap=EntityUtils.getMap(famDearThreeInTwoStus, e->e.getId());
			Map<String, Map<String, McodeDetail>> mcodeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeIds(new String[]{"DM-XJHDZT","DM-XJHDLX"}),
	                new TypeReference<Map<String, Map<String, McodeDetail>>>() {
	                });
	        Map<String, McodeDetail> ztMap = mcodeMap.get("DM-XJHDZT");
	        Map<String, McodeDetail> lxMap = mcodeMap.get("DM-XJHDLX");
	        for (FamDearThreeInTwoReport famDearThreeInTwoReport : famDearThreeInTwoReports) {
	        	if(MapUtils.isNotEmpty(userMap)){
	        		User user=userMap.get(famDearThreeInTwoReport.getCreateUserId());
	        		if(user!=null){
	        			famDearThreeInTwoReport.setTeaName(user.getRealName());
	        			if(MapUtils.isNotEmpty(teaMap)&&teaMap.containsKey(user.getOwnerId())){
	        				Teacher teacher=teaMap.get(user.getOwnerId());
	        				if(teacher!=null){
	        					if(MapUtils.isNotEmpty(deptMap)&&deptMap.containsKey(teacher.getDeptId())){
	        						Dept dept=deptMap.get(teacher.getDeptId());
	        						if(dept!=null){
	        							famDearThreeInTwoReport.setDeptName(dept.getDeptName());
	        						}
	        					}
	        				}
	        			}
	        		}
	        	}
	        	String type = famDearThreeInTwoReport.getType();
	        	String stus=famDearThreeInTwoReport.getObjStu();
                StringBuilder builder = new StringBuilder();
                StringBuilder builderStu = new StringBuilder();

                if (StringUtils.isNotEmpty(type)) {
                    String[] types = type.split(",");
                    for (String str : types) {
                        McodeDetail detail = lxMap.get(str);
                        if (detail != null) {
                            builder.append(detail.getMcodeContent() + ",");
                        }
                    }
                    if (builder.length() >= 1) {
                    	famDearThreeInTwoReport.setTypeStr(builder.substring(0,builder.length()-1));
                    }

                }
                
                if(StringUtils.isNotBlank(stus)){
                	String[] stuIds=stus.split(",");
                	for (String str : stuIds) {
						FamDearThreeInTwoStu famDearThreeInTwoStu=stuMap.get(str);
						if(famDearThreeInTwoStu!=null){
							builderStu.append(famDearThreeInTwoStu.getStuname()+",");
						}
					}
                	if(builderStu.length()>=1){
                		famDearThreeInTwoReport.setStuNames(builderStu.substring(0, builderStu.length()-1));
                	}
                }
                
                McodeDetail deaDetail=ztMap.get(famDearThreeInTwoReport.getTitle());
                famDearThreeInTwoReport.setTitleStr(deaDetail.getMcodeContent());
			}
		}
	}

	@Override
	protected BaseJpaRepositoryDao<FamDearThreeInTwoReport, String> getJpaDao() {
		return famThreeInTwoReportDao;
	}

	@Override
	protected Class<FamDearThreeInTwoReport> getEntityClass() {
		return FamDearThreeInTwoReport.class;
	}

}
