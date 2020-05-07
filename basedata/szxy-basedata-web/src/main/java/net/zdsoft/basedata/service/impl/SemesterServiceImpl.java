package net.zdsoft.basedata.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.dao.SchoolSemesterDao;
import net.zdsoft.basedata.entity.SchoolSemester;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.SemesterDao;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service("semesterService")
public class SemesterServiceImpl extends BaseServiceImpl<Semester, String> implements SemesterService {

    @Autowired
    private SemesterDao semesterDao;

    @Autowired
    private SchoolSemesterDao schoolSemesterDao;

    @Override
    public Semester getCurrentSemester(int type) {
//        Specification<Semester> spec = new Specification<Semester>() {
//            @Override
//            public Predicate toPredicate(Root<Semester> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
//                List<Predicate> ps = new ArrayList<Predicate>();
//                Expression<Date> currentDate = cb.literal(new Date());
//                ps.add(cb
//                        .between(currentDate, root.get("workBegin").as(Date.class), root.get("workEnd").as(Date.class)));
//                ps.add(cb.equal(root.<Integer>get("isDeleted"),0));
//                cq.where(cb.and(ps.toArray(new Predicate[0])));
//                return cq.getRestriction();
//            }
//        };
    	//上面范围不包括开始结束时间
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Semester semester = semesterDao.findByCurrentDay(sdf.format(new Date()));
        if (semester != null) {
            return semester;
        }
        else {
            if (type == 0) {
                return semester;
            }
            else if (type == 1) {
                Specification<Semester> specPre = new Specification<Semester>() {
                    @Override
                    public Predicate toPredicate(Root<Semester> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                        List<Predicate> ps = new ArrayList<Predicate>();
                        Expression<Date> currentDate = cb.literal(new Date());
                        ps.add(cb.lessThanOrEqualTo(root.get("workEnd").as(Date.class), currentDate));
                        ps.add(cb.equal(root.<Integer>get("isDeleted"),0));
                        cq.where(cb.and(ps.toArray(new Predicate[0])));
                        cq.orderBy(cb.desc(root.get("workEnd").as(Date.class)));
                        return cq.getRestriction();
                    }
                };
                List<Semester> semesters = semesterDao.findAll(specPre);
                return CollectionUtils.isEmpty(semesters) ? null : semesters.get(0);

            }
            else if (type == 2) {
                Specification<Semester> specAfter = new Specification<Semester>() {
                    @Override
                    public Predicate toPredicate(Root<Semester> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                        List<Predicate> ps = new ArrayList<Predicate>();
                        Expression<Date> currentDate = cb.literal(new Date());
                        ps.add(cb.greaterThanOrEqualTo(root.get("workBegin").as(Date.class), currentDate));
                        ps.add(cb.equal(root.<Integer>get("isDeleted"),0));
                        cq.where(cb.and(ps.toArray(new Predicate[0])));
                        cq.orderBy(cb.asc(root.get("workBegin").as(Date.class)));
                        return cq.getRestriction();
                    }
                };
                // return semesterDao.findOne(specAfter);
                List<Semester> semesters = semesterDao.findAll(specAfter);
                return CollectionUtils.isEmpty(semesters) ? null : semesters.get(0);
            }
            else {
                return semester;
            }
        }

    }

    @Override
    protected BaseJpaRepositoryDao<Semester, String> getJpaDao() {
        return this.semesterDao;
    }

    @Override
    protected Class<Semester> getEntityClass() {
        return Semester.class;
    }

    @Override
    public List<String> findAcadeyearList() {
        return semesterDao.getAcadyearList();
    }

    @Override
    public Semester findCurrentSemester(int type, final String schoolId) {
        if (StringUtils.isBlank(schoolId)) {
            return getCurrentSemester(type);
        }
//        Specification<SchoolSemester> spec = new Specification<SchoolSemester>() {
//            @Override
//            public Predicate toPredicate(Root<SchoolSemester> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
//                List<Predicate> ps = new ArrayList<Predicate>();
//                Expression<Date> currentDate = cb.literal(new Date());
//                ps.add(cb.equal(root.get("schoolId").as(String.class),schoolId));
//                ps.add(cb.between(currentDate, root.get("workBegin").as(Date.class), root.get("workEnd").as(Date.class)));
//                ps.add(cb.equal(root.<Integer>get("isDeleted"),0));
//                cq.where(cb.and(ps.toArray(new Predicate[0])));
//                return cq.getRestriction();
//            }
//        };
      //上面范围不包括开始结束时间
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SchoolSemester sch = schoolSemesterDao.findByCurrentDay(schoolId, sdf.format(new Date()));
        if (sch != null) {
            return sch.toSemester();
        }

        if (sch == null ) {
        	//当前时间教育局是否设置
        	Semester edu = getCurrentSemester(type);
        	if(edu!=null){
        		return edu;
        	}
        	if(type== 0){  //如果0 返回null
        		return null;
        	}
        	if (type == 1 ) {//当前不存在取之前的，学校不存在则取教育的
                List<SchoolSemester> semesters = schoolSemesterDao.findAll(new Specification<SchoolSemester>() {
                    @Override
                    public Predicate toPredicate(Root<SchoolSemester> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                        return criteriaQuery.where(criteriaBuilder.lessThanOrEqualTo(root.get("workEnd").as(Date.class), new Date()), criteriaBuilder.equal(root.<Integer>get("isDeleted"),0))
                                .orderBy(criteriaBuilder.desc(root.get("workEnd").as(Date.class))).getRestriction();
                    }
                });
                sch = semesters != null ? semesters.get(0) : null ;
                if(sch!=null){
                	return sch.toSemester();
                }
            }
            if ( type == 2 ) {//当前不存在取之后的 学校不存在则取教育的
                List<SchoolSemester> semesters = schoolSemesterDao.findAll(new Specification<SchoolSemester>() {
                    @Override
                    public Predicate toPredicate(Root<SchoolSemester> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                        return criteriaQuery.where(criteriaBuilder.greaterThanOrEqualTo(root.get("workBegin").as(Date.class), new Date()), criteriaBuilder.equal(root.<Integer>get("isDeleted"),0))
                                .orderBy(criteriaBuilder.asc(root.get("workBegin").as(Date.class))).getRestriction();
                    }
                });
                sch = semesters != null ? semesters.get(0) : null ;
                if(sch!=null){
                	return sch.toSemester();
                }
            }
            
        }
        //当前时间学校和教育都存在的时候才执行下面方法获取教育局的对应1或者2数据信息
        return getCurrentSemester(type);
    }

	@Override
	public Semester findByAcadyearAndSemester(String acadyear, int semester,
			String unitId) {
		SchoolSemester schoolSemester = null;
		if(StringUtils.isNotBlank(unitId)){
			schoolSemester=schoolSemesterDao.findByAcadyearAndSemester(acadyear, semester, unitId);
		}
		if(schoolSemester==null){
			return semesterDao.findByAcadYearAndSemester(acadyear, semester);
		}else{
			return schoolSemester.toSemester();
		}
	}

	@Override
	public List<Semester> findListByDate(Date date) {
		return semesterDao.findListByDate(date);
	}
}
