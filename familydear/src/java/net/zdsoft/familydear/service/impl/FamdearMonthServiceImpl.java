package net.zdsoft.familydear.service.impl;

import net.zdsoft.activity.dto.FamilyMonthDto;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.familydear.dao.FamdearMonthDao;
import net.zdsoft.familydear.entity.FamDearActivity;
import net.zdsoft.familydear.entity.FamDearArrange;
import net.zdsoft.familydear.entity.FamdearActualReport;
import net.zdsoft.familydear.entity.FamdearMonth;
import net.zdsoft.familydear.service.FamdearMonthService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.familydear.service.impl
 * @ClassName: FamdearMonthServiceImpl
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/24 9:34
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/24 9:34
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service("famdearMonthService")
public class FamdearMonthServiceImpl extends BaseServiceImpl<FamdearMonth, String> implements FamdearMonthService {
    @Autowired
    private FamdearMonthDao famdearMonthDao;
    @Autowired
    private UserRemoteService userRemoteService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Override
    protected BaseJpaRepositoryDao<FamdearMonth, String> getJpaDao() {
        return famdearMonthDao;
    }

    @Override
    protected Class<FamdearMonth> getEntityClass() {
        return FamdearMonth.class;
    }

    @Override
    public List<FamdearMonth> findListByActivityIds(String [] activityIds) {
        return famdearMonthDao.findListByActivityIds(activityIds);
    }

    @Override
    public List<FamdearMonth> findListByArrangeIds(String[] arrangeIds) {
        return famdearMonthDao.findListByArrangeIds(arrangeIds);
    }

    @Override
    public List<FamdearMonth> findListByIdsPage(String[] ids,Pagination pagination) {
        List<FamdearMonth> list=new ArrayList<FamdearMonth>();
        Specification<FamdearMonth> specification = new Specification<FamdearMonth>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<FamdearMonth> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {


                List<Predicate> ps = new ArrayList<Predicate>();
//                if (honorIds.size()> 0) {
//                    String[] honorIdArr = honorIds.toArray(new String[honorIds.size()]);
//                    queryIn("id", honorIdArr, root, ps, null);
//                }
                if (ids!=null&&ids.length>0) {
                    CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("id").as(String.class));
                    for (int i = 0; i < ids.length; i++) {
                        in.value(ids[i]);
                    }
                    ps.add(in);
                    queryIn("id", ids, root, ps, null);
                }

                List<Order> orderList = new ArrayList<>();
                orderList.add(criteriaBuilder.desc(root.get("createTime").as(Date.class)));
                criteriaQuery.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return criteriaQuery.getRestriction();
            }
        };
        if (pagination != null) {
            Pageable pageable = Pagination.toPageable(pagination);
            Page<FamdearMonth> findAll = famdearMonthDao.findAll(specification, pageable);
            pagination.setMaxRowCount((int) findAll.getTotalElements());
            list = findAll.getContent();
        }
        else {
            list=famdearMonthDao.findAll(specification);
        }
        return list;
    }

    @Override
	public FamilyMonthDto findListByTimeAndDeptId(FamilyMonthDto familyMonthDto,String unitId, String deptId,
			Date startTime, Date endTime) {
		List<FamdearMonth> famdearMonthAlls=famdearMonthDao.getListByTime(unitId, startTime, endTime);
		List<FamdearMonth> famdearMonthDes=new ArrayList<FamdearMonth>();
		if(CollectionUtils.isNotEmpty(famdearMonthAlls)){
			Set<String> userSet=EntityUtils.getSet(famdearMonthAlls, e->e.getCreateUserId());
			List<User>  userList = userRemoteService.findListObjectByIds(userSet.toArray(new String[0]));
            Map<String,String> userTeaIdMap = new HashMap<>();
            Set<String> teaId=new HashSet<>();
            for (User user : userList) {
                userTeaIdMap.put(user.getId() , user.getOwnerId());
                teaId.add(user.getOwnerId());
            }
            List<Teacher> teachers=SUtils.dt(teacherRemoteService.findListByIds(teaId.toArray(new String[0])), Teacher.class);
            Map<String,Teacher> teaMap=EntityUtils.getMap(teachers, e->e.getId()+"");
            if(StringUtils.isNotBlank(deptId)){
            	for (FamdearMonth famdearMonth : famdearMonthAlls) {
					if(userTeaIdMap.containsKey(famdearMonth.getCreateUserId())){
						String teacherId=userTeaIdMap.get(famdearMonth.getCreateUserId());
						if(teaMap.containsKey(teacherId)){
							Teacher teacher=teaMap.get(teacherId);
							if(StringUtils.equals(deptId, teacher.getDeptId())){
								famdearMonthDes.add(famdearMonth);
							}
						}
					}
				}
            }else{
            	famdearMonthDes=famdearMonthAlls;
            }
        	int reportMeeting=0;//座谈报告会
        	int relateMeeting=0;//联欢会
        	int wjhd=0;//文体活动
        	int doubleLangue=0;//双语
        	int visityStudy=0;//参观学习
        	int dzzLife=0;//党组织生活
        	int titleOrg=0;//主题班会
        	int titleStyle=0;//主题团、队会
        	int otherTime=0;//其他
        	int actureAmount=0;//参加人数
            for (FamdearMonth famdearMonth : famdearMonthDes) {
            	actureAmount+=famdearMonth.getPartnum();
            	if(StringUtils.equals("1", famdearMonth.getActivityForm())){
            		reportMeeting++;
            	}else if(StringUtils.equals("2", famdearMonth.getActivityForm())){
            		relateMeeting++;
            	}else if(StringUtils.equals("3", famdearMonth.getActivityForm())){
            		wjhd++;
            	}else if(StringUtils.equals("4", famdearMonth.getActivityForm())){
            		doubleLangue++;
            	}else if(StringUtils.equals("5", famdearMonth.getActivityForm())){
            		visityStudy++;
            	}else if(StringUtils.equals("6", famdearMonth.getActivityForm())){
            		dzzLife++;
            	}else if(StringUtils.equals("7", famdearMonth.getActivityForm())){
            		titleOrg++;
            	}else if(StringUtils.equals("8", famdearMonth.getActivityForm())||StringUtils.equals("9", famdearMonth.getActivityForm())){
            		titleStyle++;
            	}else if(StringUtils.equals("0", famdearMonth.getActivityForm())){
            		otherTime++;
            	}
			}
            familyMonthDto.setReportMeeting(reportMeeting);
            familyMonthDto.setRelateMeeting(relateMeeting);
            familyMonthDto.setWjhd(wjhd);
            familyMonthDto.setDoubleLangue(doubleLangue);
            familyMonthDto.setVisityStudy(visityStudy);
            familyMonthDto.setDzzLife(dzzLife);
            familyMonthDto.setTitleOrg(titleOrg);
            familyMonthDto.setTitleStyle(titleStyle);
            familyMonthDto.setOtherTime(otherTime);
            familyMonthDto.setTotal(reportMeeting+relateMeeting+wjhd+doubleLangue+visityStudy+dzzLife+titleOrg+titleStyle+otherTime);
            familyMonthDto.setActureAmount(actureAmount);
		}
		return familyMonthDto;
	}

	@Override
    public List<FamdearMonth> findListByTimePage(Date startTime, Date endTime,String createUserId,String state, String type, Pagination pagination) {
        List<FamdearMonth> list=new ArrayList<FamdearMonth>();
        Specification<FamdearMonth> specification = new Specification<FamdearMonth>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<FamdearMonth> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {


                List<Predicate> ps = new ArrayList<Predicate>();
//                if (honorIds.size()> 0) {
//                    String[] honorIdArr = honorIds.toArray(new String[honorIds.size()]);
//                    queryIn("id", honorIdArr, root, ps, null);
//                }
                if(startTime!=null){
                    ps.add(criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("activityTime"), startTime));
                }
                if(endTime!=null){
                    ps.add(criteriaBuilder.lessThanOrEqualTo(root.<Date> get("activityEndTime"), endTime));
                }
                if(StringUtils.isNotBlank(createUserId)){
                    ps.add(criteriaBuilder.equal(root.get("createUserId").as(String.class),createUserId));
                }
                if(StringUtils.isNotBlank(state)){
                    ps.add(criteriaBuilder.equal(root.get("state").as(String.class),state));
                }
                if(StringUtils.isNotBlank(type)){
                    ps.add(criteriaBuilder.equal(root.get("type").as(String.class),type));
                }
                List<Order> orderList = new ArrayList<>();
                orderList.add(criteriaBuilder.desc(root.get("createTime").as(Date.class)));
                criteriaQuery.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return criteriaQuery.getRestriction();
            }
        };
        if (pagination != null) {
            Pageable pageable = Pagination.toPageable(pagination);
            Page<FamdearMonth> findAll = famdearMonthDao.findAll(specification, pageable);
            pagination.setMaxRowCount((int) findAll.getTotalElements());
            list = findAll.getContent();
        }
        else {
            list=famdearMonthDao.findAll(specification);
        }
        return list;
    }
}
