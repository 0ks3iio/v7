package net.zdsoft.familydear.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.familydear.dao.FamdearActualReportDao;
import net.zdsoft.familydear.dao.FamdearMonthDao;
import net.zdsoft.familydear.entity.FamDearActivity;
import net.zdsoft.familydear.entity.FamdearActualReport;
import net.zdsoft.familydear.service.FamdearActualReportService;
import net.zdsoft.familydear.service.FamdearMonthService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;

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
import java.util.List;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.familydear.service.impl
 * @ClassName: FamdearActualReportServiceImpl
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/24 9:37
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/24 9:37
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service("famdearActualReportService")
public class FamdearActualReportServiceImpl extends BaseServiceImpl<FamdearActualReport, String> implements FamdearActualReportService {
    @Autowired
    private FamdearActualReportDao famdearActualReportDao;
    @Override
    protected BaseJpaRepositoryDao<FamdearActualReport, String> getJpaDao() {
        return famdearActualReportDao;
    }

    @Override
    protected Class<FamdearActualReport> getEntityClass() {
        return FamdearActualReport.class;
    }

    @Override
    public List<FamdearActualReport> getListByArrangeIds(String[] ids,String createUserId,String state, Pagination pagination) {
        List<FamdearActualReport> list=new ArrayList<FamdearActualReport>();
        Specification<FamdearActualReport> specification = new Specification<FamdearActualReport>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<FamdearActualReport> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {


                List<Predicate> ps = new ArrayList<Predicate>();
//                if (honorIds.size()> 0) {
//                    String[] honorIdArr = honorIds.toArray(new String[honorIds.size()]);
//                    queryIn("id", honorIdArr, root, ps, null);
//                }
                if(StringUtils.isNotBlank(createUserId)){
                    ps.add(criteriaBuilder.like(root.get("createUserId").as(String.class),createUserId));
                }
                if(StringUtils.isNotBlank(state)){
                    ps.add(criteriaBuilder.like(root.get("state").as(String.class),state));
                }
                if (ids!=null&&ids.length>0) {
                    CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("arrangeId").as(String.class));
                    for (int i = 0; i < ids.length; i++) {
                        in.value(ids[i]);
                    }
                    ps.add(in);
                    queryIn("arrangeId", ids, root, ps, null);

                }
                List<Order> orderList = new ArrayList<>();
                orderList.add(criteriaBuilder.desc(root.get("createTime").as(Date.class)));
                criteriaQuery.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return criteriaQuery.getRestriction();

            }
        };
        if (pagination != null) {
            Pageable pageable = Pagination.toPageable(pagination);
            Page<FamdearActualReport> findAll = famdearActualReportDao.findAll(specification, pageable);
            pagination.setMaxRowCount((int) findAll.getTotalElements());
            list = findAll.getContent();
        }
        else {
            list=famdearActualReportDao.findAll(specification);
        }
        return list;
    }

	@Override
	public List<FamdearActualReport> getListByTime(String unitId,
			Date startTime, Date endTime) {
		return famdearActualReportDao.getListByTime(unitId, startTime, endTime);
	}

	@Override
	public List<FamdearActualReport> getListByUnitIdAndOthers(String unitId,
			String year,Date startDate,Date endDate, String[] arrangeIds,String state) {



        List<FamdearActualReport> list=new ArrayList<FamdearActualReport>();
        Specification<FamdearActualReport> specification = new Specification<FamdearActualReport>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<FamdearActualReport> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {


                List<Predicate> ps = new ArrayList<Predicate>();
//                if (honorIds.size()> 0) {
//                    String[] honorIdArr = honorIds.toArray(new String[honorIds.size()]);
//                    queryIn("id", honorIdArr, root, ps, null);
//                }
                if(StringUtils.isNotBlank(unitId)){
                    ps.add(criteriaBuilder.equal(root.get("unitId").as(String.class),unitId));
                }
                if(StringUtils.isNotBlank(year)){
                    ps.add(criteriaBuilder.equal(root.get("year").as(String.class),year));
                }
                if(startDate!=null){
                    ps.add(criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("arriveTime"), startDate));
                }
                if(endDate!=null){
                    ps.add(criteriaBuilder.lessThanOrEqualTo(root.<Date> get("backTime"), endDate));
                }
                if(StringUtils.isNotBlank(state)){
                    ps.add(criteriaBuilder.equal(root.<Date> get("state"), state));
                }
                if (arrangeIds!=null&&arrangeIds.length>0) {
                    CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("arrangeId").as(String.class));
                    for (int i = 0; i < arrangeIds.length; i++) {
                        in.value(arrangeIds[i]);
                    }
                    ps.add(in);
                    queryIn("arrangeId", arrangeIds, root, ps, null);

                }
                List<Order> orderList = new ArrayList<>();
                orderList.add(criteriaBuilder.desc(root.get("createTime").as(Date.class)));
                criteriaQuery.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return criteriaQuery.getRestriction();

            }
        };
        list=famdearActualReportDao.findAll(specification);
        return list;
//		return famdearActualReportDao.getListByUnitIdAndOthers(unitId, year,startDate,endDate, arrangeIds);
	}

    @Override
    public List<FamdearActualReport> getListByIdsPage(String[] ids, Pagination pagination) {
        List<FamdearActualReport> list=new ArrayList<FamdearActualReport>();
        Specification<FamdearActualReport> specification = new Specification<FamdearActualReport>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<FamdearActualReport> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {


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
            Page<FamdearActualReport> findAll = famdearActualReportDao.findAll(specification, pageable);
            pagination.setMaxRowCount((int) findAll.getTotalElements());
            list = findAll.getContent();
        }
        else {
            list=famdearActualReportDao.findAll(specification);
        }
        return list;
    }


}
