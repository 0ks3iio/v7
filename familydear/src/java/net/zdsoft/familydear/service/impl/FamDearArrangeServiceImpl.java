package net.zdsoft.familydear.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.familydear.common.FamDearConstant;
import net.zdsoft.familydear.dao.FamDearArrangeDao;
import net.zdsoft.familydear.entity.FamDearArrange;
import net.zdsoft.familydear.entity.FamDearPlan;
import net.zdsoft.familydear.service.FamDearArrangeService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.familydear.service.impl
 * @ClassName: FamDearArrangeServiceImpl
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/7 9:04
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/7 9:04
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service("famDearArrangeService")
public class FamDearArrangeServiceImpl extends BaseServiceImpl<FamDearArrange, String> implements FamDearArrangeService{
    @Autowired
    private FamDearArrangeDao famDearArrangeDao;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private UserRemoteService userRemoteService;
    @Override
    protected BaseJpaRepositoryDao<FamDearArrange, String> getJpaDao() {
        return famDearArrangeDao;
    }

    @Override
	public List<FamDearArrange> getFamilyDearArrangeList(String[] activitys) {
        List<FamDearArrange> list = new ArrayList<>();
        if(activitys!=null&&activitys.length>0) {
           list = famDearArrangeDao.getFamilyDearArrangeList(activitys);
            for (FamDearArrange famDearArrange : list) {
                String endTimeStr = DateUtils.date2String(famDearArrange.getEndTime(), "yyyy.MM.dd");
                String endApplyTimeStr = DateUtils.date2String(famDearArrange.getApplyEndTime(), "yyyy.MM.dd");
                famDearArrange.setActivityTimeStr(DateUtils.date2String(famDearArrange.getStartTime(), "yyyy.MM.dd") + "-" + endTimeStr);
                famDearArrange.setApplyTimeStr(DateUtils.date2String(famDearArrange.getApplyTime(), "yyyy.MM.dd") + "-" + endApplyTimeStr);
            }
        }
        return list;
	}

	public List<FamDearArrange> getFamilyDearArrangeListByUnitId(String[] activitys,String unitId){
        List<FamDearArrange> list = famDearArrangeDao.getFamilyDearArrangeList(activitys);
        for(FamDearArrange famDearArrange :list){
            String endTimeStr = DateUtils.date2String(famDearArrange.getEndTime(),"yyyy.MM.dd");
            String endApplyTimeStr = DateUtils.date2String(famDearArrange.getApplyEndTime(),"yyyy.MM.dd");
            famDearArrange.setActivityTimeStr(DateUtils.date2String(famDearArrange.getStartTime(),"yyyy.MM.dd")+"-"+endTimeStr);
            famDearArrange.setApplyTimeStr(DateUtils.date2String(famDearArrange.getApplyTime(),"yyyy.MM.dd")+"-"+endApplyTimeStr);
        }
        List<Teacher> teachers = SUtils.dt(teacherRemoteService.findByUnitId(unitId), Teacher.class);
        Map<String,String> teacherNameMap = teachers.stream().collect(Collectors.toMap(Teacher::getId,Teacher::getTeacherName));
        for (FamDearArrange famDearArrange : list) {
            if(net.zdsoft.framework.utils.StringUtils.isNotBlank(famDearArrange.getLeaderUserId())){
                String[] ids = famDearArrange.getLeaderUserId().split(",");
                List<User>  userList = userRemoteService.findListObjectByIds(ids);
                Map<String,String> userTeaIdMap = new HashMap<>();
                for (User user : userList) {
                    userTeaIdMap.put(user.getId() , user.getOwnerId());

                }
                StringBuffer buffer = new StringBuffer();
                for (String id1 : ids) {
                    String teacherName = teacherNameMap.get(userTeaIdMap.get(id1));
                    buffer.append(teacherName + ",");
                }
                famDearArrange.setLeaderUserNames(buffer.substring(0,buffer.length()-1));
            }

        }
        return list;
    }

    @Override
    public List<FamDearArrange> getFamilyDearArrangeListByContryNameAndActivityId(String contryName, String activityId) {
        List<FamDearArrange> list=new ArrayList<FamDearArrange>();
        Specification<FamDearArrange> specification = new Specification<FamDearArrange>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<FamDearArrange> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> ps = Lists.newArrayList();
//                ps.add(criteriaBuilder.equal(root.get("year").as(String.class),year));
                if(activityId!=null){
                    ps.add(criteriaBuilder.equal(root.<Date> get("activityId"), activityId));
                }
                if(StringUtils.isNotBlank(contryName)){
                    ps.add(criteriaBuilder.like(root.get("rural").as(String.class),"%"+contryName+"%"));
                }
                List<Order> orderList = new ArrayList<>();
//                orderList.add(criteriaBuilder.asc(root.get("createTime").as(String.class)));
//                criteriaQuery.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                criteriaQuery.where(ps.toArray(new Predicate[0]));
                return criteriaQuery.getRestriction();
            }
        };
        list=famDearArrangeDao.findAll(specification);
        return list;
    }

    @Override
	public List<FamDearArrange> getFamilyDearArrangeListByContryName(
			String contryName) {
		contryName = "%" + contryName + "%";
		return famDearArrangeDao.getFamilyDearArrangeListByContryName(contryName);
	}

    @Override
    public List<FamDearArrange> getFamilyDearArrangeListByParams(Date startTime, Date endTime, String contryName) {
        List<FamDearArrange> list=new ArrayList<FamDearArrange>();
        Specification<FamDearArrange> specification = new Specification<FamDearArrange>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<FamDearArrange> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> ps = Lists.newArrayList();
//                ps.add(criteriaBuilder.equal(root.get("year").as(String.class),year));
                if(startTime!=null){
                    ps.add(criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("applyTime"), startTime));
                }
                if(endTime!=null){
                    ps.add(criteriaBuilder.lessThanOrEqualTo(root.<Date> get("applyEndTime"), endTime));
                }
                if(StringUtils.isNotBlank(contryName)){
                    ps.add(criteriaBuilder.like(root.get("rural").as(String.class),"%"+contryName+"%"));
                }
                List<Order> orderList = new ArrayList<>();
//                orderList.add(criteriaBuilder.asc(root.get("createTime").as(String.class)));
//                criteriaQuery.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                criteriaQuery.where(ps.toArray(new Predicate[0]));
                return criteriaQuery.getRestriction();
            }
        };
        list=famDearArrangeDao.findAll(specification);
        return list;
    }

    @Override
    protected Class<FamDearArrange> getEntityClass() {
        return FamDearArrange.class;
    }
}
