package net.zdsoft.familydear.service.impl;

import java.util.*;

import com.google.common.collect.Lists;
import net.zdsoft.activity.service.FamilyDearRegisterService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.familydear.common.FamDearConstant;
import net.zdsoft.familydear.dao.FamDearActivityDao;
import net.zdsoft.familydear.dto.FamActivityDto;
import net.zdsoft.familydear.entity.FamDearActivity;
import net.zdsoft.familydear.entity.FamDearArrange;
import net.zdsoft.familydear.entity.FamDearPlan;
import net.zdsoft.familydear.service.FamDearActivityService;
import net.zdsoft.familydear.service.FamDearArrangeService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
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
 * @ClassName: FamDearActivityServiceImpl
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/7 9:09
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/7 9:09
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service("famDearActivityService")
public class FamDearActivityServiceImpl extends BaseServiceImpl<FamDearActivity, String> implements FamDearActivityService {
    @Autowired
    private FamDearActivityDao famDearActivityDao;
    @Autowired
    private FamDearArrangeService famDearArrangeService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    FamilyDearRegisterService familyDearRegisterService;
    @Override
    protected BaseJpaRepositoryDao<FamDearActivity, String> getJpaDao() {
        return famDearActivityDao;
    }

    @Override
	public List<FamDearActivity> findByPlanIds(String[] planIds) {
		return famDearActivityDao.findByPlanIds(planIds);
	}


    @Override
	public List<FamDearActivity> findByNameAndPlanIds(String activityName,
			String[] planIds) {
		return famDearActivityDao.findByNameAndPlanIds(activityName, planIds);
	}

    @Override
    public List<FamDearActivity> findListByYearAndTitleByPage(String[] ids,String state,String year, Pagination pagination) {
        List<FamDearActivity> list=new ArrayList<FamDearActivity>();
        Specification<FamDearActivity> specification = new Specification<FamDearActivity>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<FamDearActivity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {


                List<Predicate> ps = new ArrayList<Predicate>();
//                if (honorIds.size()> 0) {
//                    String[] honorIdArr = honorIds.toArray(new String[honorIds.size()]);
//                    queryIn("id", honorIdArr, root, ps, null);
//                }
                if(StringUtils.isNotBlank(state)){
                    ps.add(criteriaBuilder.like(root.get("state").as(String.class),state));
                }
                if(StringUtils.isNotBlank(year)){
                    ps.add(criteriaBuilder.like(root.get("year").as(String.class),year));
                }
                if (ids!=null&&ids.length>0) {
                    CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("id").as(String.class));
                    for (int i = 0; i < ids.length; i++) {
                        in.value(ids[i]);
                    }
                    ps.add(in);
                }
                queryIn("id", ids, root, ps, null);
                List<Order> orderList = new ArrayList<>();
                orderList.add(criteriaBuilder.desc(root.get("createTime").as(Date.class)));
                criteriaQuery.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return criteriaQuery.getRestriction();
            }
        };
        if (pagination != null) {
            Pageable pageable = Pagination.toPageable(pagination);
            Page<FamDearActivity> findAll = famDearActivityDao.findAll(specification, pageable);
            pagination.setMaxRowCount((int) findAll.getTotalElements());
            list = findAll.getContent();
        }
        else {
            list=famDearActivityDao.findAll(specification);
        }
        return list;
    }

    @Override
    public List<FamDearActivity> findListByYearAndState(String[] ids, String state, String year) {
        List<FamDearActivity> list=new ArrayList<FamDearActivity>();
        Specification<FamDearActivity> specification = new Specification<FamDearActivity>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<FamDearActivity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {


                List<Predicate> ps = new ArrayList<Predicate>();
//                if (honorIds.size()> 0) {
//                    String[] honorIdArr = honorIds.toArray(new String[honorIds.size()]);
//                    queryIn("id", honorIdArr, root, ps, null);
//                }
                if(StringUtils.isNotBlank(state)){
                    ps.add(criteriaBuilder.like(root.get("state").as(String.class),state));
                }
                if(StringUtils.isNotBlank(year)){
                    ps.add(criteriaBuilder.equal(root.get("year").as(String.class),year));
                }
                if (ids!=null&&ids.length>0) {
                    CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("id").as(String.class));
                    for (int i = 0; i < ids.length; i++) {
                        in.value(ids[i]);
                    }
                    ps.add(in);
                    queryIn("id", ids, root, ps, null);
                }
                List<Order> orderList = new ArrayList<>();
                orderList.add(criteriaBuilder.asc(root.get("createTime").as(String.class)));
                criteriaQuery.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return criteriaQuery.getRestriction();
            }
        };
        list=famDearActivityDao.findAll(specification);
        return list;
    }

    @Override
    public void saveFamDearActivityDto(FamActivityDto famActivityDto,String arrangeIds) {
        FamDearActivity famDearActivity = new FamDearActivity();
        String id = UUID.randomUUID().toString().replaceAll("-","");
        if(net.zdsoft.framework.utils.StringUtils.isNotBlank(famActivityDto.getId())) {
            id=famActivityDto.getId();
        }
        famDearActivity.setId(id);
        famDearActivity.setTitle(famActivityDto.getTitle());
        famDearActivity.setYear(famActivityDto.getYear());
        famDearActivity.setPlanId(famActivityDto.getPlanId());
        famDearActivity.setFileContent(famActivityDto.getFileContent().trim());
//        famDearActivity.setRequire(famActivityDto.getRequire());
        famDearActivity.setCreateTime(new Date());
        famDearActivity.setCreateUserId(famActivityDto.getCreateUserId());
        famDearActivity.setUnitId(famActivityDto.getUnitId());
        String[] mcodes = {"DM-XJJQC"};
        List<McodeDetail> mds = SUtils.dt(mcodeRemoteService.findByMcodeIds(mcodes), new TR<List<McodeDetail>>() {} );
        Map<String,String> stringMap = new HashMap<>();
        for(McodeDetail mcodeDetail:mds){
            stringMap.put(mcodeDetail.getThisId(),mcodeDetail.getMcodeContent());
        }
        if(net.zdsoft.framework.utils.StringUtils.isBlank(famActivityDto.getState())) {
            famDearActivity.setState(FamDearConstant.PLAN_UNPUBLISH);
        }else {
            famDearActivity.setState(famActivityDto.getState());
        }
        List<FamDearArrange> list = famActivityDto.getFamDearArrangeList();
        if(CollectionUtils.isNotEmpty(list)) {
            //移除表单提交的空批次
            for(int i=0;i<list.size();i++) {
                FamDearArrange famDearArrange = list.get(i);
                if(StringUtils.isBlank(famDearArrange.getBatchType())){
                    list.remove(famDearArrange);
                }
            }
            for(FamDearArrange famDearArrange:list){
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                if (net.zdsoft.framework.utils.StringUtils.isNotBlank(famDearArrange.getId())) {
                    uuid = famDearArrange.getId();
                    //ids.add(famDearArrange.getId());
                }
                famDearArrange.setId(uuid);
                famDearArrange.setUnitId(famDearActivity.getUnitId());
                famDearArrange.setActivityId(famDearActivity.getId());
                if (net.zdsoft.framework.utils.StringUtils.isNotBlank(famDearArrange.getRuralValue())) {
                    String[] ruralValue = famDearArrange.getRuralValue().split(",");

                    if(ruralValue.length>0) {
                        String[] rurals =new String[ruralValue.length];
                        for(int i = 0;i<ruralValue.length;i++){
                            rurals[i] = stringMap.get(ruralValue[i]);
                        }
                        famDearArrange.setRural(StringUtils.join(rurals, ","));
                    }
                }
            }
            famDearArrangeService.saveAll(list.toArray(new FamDearArrange[]{}));
        }
        if(net.zdsoft.framework.utils.StringUtils.isNotBlank(arrangeIds)) {
            String[] ids = arrangeIds.split(",");
            List<FamDearArrange> famDearArranges = famDearArrangeService.findListByIds(ids);
            famDearArrangeService.deleteAll(famDearArranges.toArray(new FamDearArrange[0]));
            familyDearRegisterService.deleteByIds(ids);
        }
        save(famDearActivity);
    }

    @Override
    protected Class<FamDearActivity> getEntityClass() {
        return FamDearActivity.class;
    }
}
