package net.zdsoft.basedata.remote.service.impl;

import net.zdsoft.basedata.dao.TeacherDao;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static net.zdsoft.framework.utils.JpaUtils.processIn;

@Service("teacherRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class TeacherRemoteServiceImpl extends
		BaseRemoteServiceImpl<Teacher, String> implements TeacherRemoteService {

	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TeacherDao teacherDao;

	@Override
	protected BaseService<Teacher, String> getBaseService() {
		return teacherService;
	}

	@Override
	public String findByUnitId(String unitId) {
		return SUtils.s(teacherService.findByUnitId(unitId));
	}

	@Override
	public String findByDeptId(String deptId) {
		return SUtils.s(teacherService.findByDeptId(deptId));
	}

	@Override
	public String findByNameCodeLike(final String name, final String code,
			final String... unitIds) {
		return SUtils.s(teacherDao.findAll(new Specification<Teacher>() {
			@Override
			public Predicate toPredicate(Root<Teacher> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				return cq.where(
						root.get("unitId").as(String.class).in(unitIds),
						cb.equal(root.get("isDeleted"), 0),
						cb.like(root.get("teacherName").as(String.class),
								name == null ? "%" : name),
						cb.like(root.get("teacherCode").as(String.class),
								code == null ? "%" : code)).getRestriction();
			}
		}));
	}

	@Override
	public String findByIdentityCardNo(final String... cardNos) {
		return SUtils.s(teacherDao.findAll(new Specification<Teacher>() {
			@Override
			public Predicate toPredicate(Root<Teacher> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = new ArrayList<Predicate>();
				ps.add(cb.equal(root.get("isDeleted"), 0));
				if(cardNos != null && cardNos.length>0){
            		In<String> in = cb.in(root.get("identityCard").as(String.class));
 					for (int i = 0; i < cardNos.length; i++) {
 						in.value(cardNos[i]);
 					}
 					ps.add(in);
            	 }
				cq.where(ps.toArray(new Predicate[0]));
				return cq.getRestriction();
			}
		}));
	}

	@Override
	public String findByDeptIds(String... deptIds) {
		return SUtils.s(teacherDao.findByDeptIds(deptIds));
	}

	@Override
	public String findByInstituteId(String instituteId) {
		return SUtils.s(teacherDao.findByInstituteId(instituteId));
	}

	@Override
	public String findByUnitId(String unitId, String pagination) {
		List<Teacher> ts = teacherDao.findByUnitId(unitId,
				Pagination.toPageable(pagination));
		return SUtils.s(ts, teacherDao.countByUnitId(unitId));
	}

	@Override
	public String findByDeptId(String deptId, String pagination) {
		List<Teacher> ts = teacherDao.findByDeptId(deptId,
				Pagination.toPageable(pagination));
		return SUtils.s(ts, teacherDao.countByDeptId(deptId));
	}

	@Override
	public String findByUnitIdUserState(String unitId, Integer userState) {
		return SUtils.s(teacherDao.findByUnitIdUserState(unitId, userState));
	}

	@Override
	public String findByDeptIdUserState(String deptId, Integer userState) {
		return SUtils.s(teacherDao.findByDeptIdUserState(deptId, userState));
	}

	@Override
	public String findByWeaveUnitId(String weaveUnitId) {
		return findOneBy(ArrayUtils.toArray("weaveUnitId", "isDeleted"),
				ArrayUtils.toArray(weaveUnitId, "0"));
	}

	@Override
	public String saveAllEntitys(String entitys) {
		Teacher[] dt = SUtils.dt(entitys, new TR<Teacher[]>() {
		});
		return SUtils.s(teacherService.saveAllEntitys(dt));
	}

	@Override
	public String findMapByDeptIdIn(String[] deptIds) {
		return SUtils.s(teacherService.findMapByDeptIdIn(deptIds));
	}

	@Override
	public String findByTeacherNameAndIdentityCard(String realName,
			String identityCard) {
		return SUtils.s(teacherService.findByTeacherNameAndIdentityCard(
				realName, identityCard));
	}

	@Override
	public String findByTeacherNameAndIdentityCardWithNoUser(String realName,
			String identityCard) {
		return SUtils.s(teacherService
				.findByTeacherNameAndIdentityCardWithNoUser(realName,
						identityCard));// TODO Auto-generated method stub
	}

	@Override
	public String findByTeacherNameAndMobilePhone(String realName,
			String mobilePhone) {
		return SUtils.s(teacherService.findByTeacherNameAndMobilePhone(
				realName, mobilePhone));
	}

	@Override
	public String findByNameLikeIdNotIn(final String name, final String unitId,
			final String pagination, final String... ids) {
		Specification<Teacher> specification = new Specification<Teacher>() {
			@Override
			public Predicate toPredicate(Root<Teacher> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = new ArrayList<Predicate>();
				ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
				ps.add(cb.equal(root.get("isDeleted"), 0));
				if (StringUtils.isNotBlank(name)) {
					ps.add(cb.like(root.get("teacherName").as(String.class),
							"%" + name + "%"));
				}
				if (ids != null && ids.length > 0) {
					ps.add(cb.not(root.get("id").as(String.class).in(ids)));
				}
				cq.where(ps.toArray(new Predicate[0]));
				return cq.getRestriction();
			}
		};
		Pagination page = SUtils.dc(pagination, Pagination.class);
		if (page != null) {
			Pageable pageable = Pagination.toPageable(pagination);
			Page<Teacher> findAll = teacherDao.findAll(specification, pageable);
			page.setMaxRowCount((int) findAll.getTotalElements());
			return SUtils.s(findAll.getContent(),
					page != null ? (long) page.getMaxRowCount() : 0);
		} else {
			return SUtils.s(teacherDao.findAll(specification));
		}
	}

	@Override
	public String findByCardNumber(String unitId, String cardNumber) {
		return SUtils.s(teacherService.findByCardNumber(unitId, cardNumber));
	}

	@Override
	public int[] updateCardNumber(List<String[]> techerCardList) {
		return teacherDao.updateCardNumber(techerCardList);
	}

	@Override
	public String findByUnitIdIn(String[] unitIds) {
		return SUtils.s(teacherDao.findByUnitIdIn(unitIds));
	}

	@Override
	public String findByTeacherNameLike(String teacherName) {
		return SUtils.s(teacherService.findListByTeacherName(null,teacherName));
	}

	@Override
	public String findPartByTeacher(String[] ids) {
		return SUtils.s(teacherService.findPartByTeacher(ids));
	}

	@Override
	public String findByUnitIdIn(String[] unitIds, String pagination) {
		if(StringUtils.isBlank(pagination)) {
			return findByUnitIdIn(unitIds);
		}
		return SUtils.s(teacherDao.findByUnitIdIn(unitIds,Pagination.toPageable(pagination)));
	}

	@Override
	public long countByUnitIds(String[] unitIds) {
		if (ArrayUtils.isEmpty(unitIds)) {
			return 0L;
		}
		return teacherDao.count((Specification<Teacher>) (root, criteriaQuery, criteriaBuilder) -> {
			return criteriaQuery.where(
					criteriaBuilder.equal(root.get("incumbencySign"), "11"),
					processIn(unitIds, criteriaBuilder, root.get("unitId")),
					criteriaBuilder.equal(root.get("isDeleted"), 0)).getRestriction();
		});
	}

}
