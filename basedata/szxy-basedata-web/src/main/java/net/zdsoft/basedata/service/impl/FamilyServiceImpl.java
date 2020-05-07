package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.framework.utils.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.FamilyDao;
import net.zdsoft.basedata.dao.StudentDao;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.service.FamilyService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Service("familyService")
public class FamilyServiceImpl extends BaseServiceImpl<Family, String> implements FamilyService {

    @Autowired
    private FamilyDao familyDao;
    @Autowired
    private StudentService studentService;
    @Autowired
    private UnitService unitService;

    @Override
    protected BaseJpaRepositoryDao<Family, String> getJpaDao() {
        return familyDao;
    }

    @Override
    protected Class<Family> getEntityClass() {
        return Family.class;
    }

    @Override
    public List<Family> saveAllEntitys(Family... family) {
        return familyDao.saveAll(checkSave(family));
    }

    @Override
    public List<Family> findByRealNameAndIdentityCard(String realName, String identityCard) {
    	List<Family>  fList =familyDao.findByRealNameAndIdentityCard(realName, identityCard);
    	makeSchoolName(fList);
		return fList;
    }

	@Override
	public List<Family> findByRealNameAndIdentityCardWithNoUser(
			String realName, String identityCard) {
		List<Family>  fList = familyDao.findByRealNameAndIdentityCardWithNoUser(realName, identityCard);
		makeSchoolName(fList);
		return fList;
	}
	
	private void makeSchoolName(List<Family> fList) {
		if (CollectionUtils.isNotEmpty(fList)) {
			Set<String> stuIds = EntityUtils.getSet(fList, "studentId");
			Set<String> schIds = EntityUtils.getSet(fList, "schoolId");
			Map<String, Student> stuMap = studentService.findMapByIdIn(stuIds
					.toArray(new String[] {}));

			Map<String, Unit> unitMap = unitService.findMapByIdIn(schIds
					.toArray(new String[] {}));

			if (stuMap != null) {
				for (Family fam : fList) {
					fam.setStudentName((stuMap.get(fam.getStudentId()) == null) ? ""
							: stuMap.get(fam.getStudentId())
									.getStudentName());
					fam.setSchoolName((unitMap.get(fam.getSchoolId()) == null) ? ""
							: unitMap.get(fam.getSchoolId()).getUnitName());
				}
			}
		}

	}

	@Override
	public List<Family> findByRealNameAndMobilePhone(String realName,
			String mobilePhone) {
		List<Family>  fList =familyDao.findByRealNameAndMobilePhone(realName, mobilePhone);
    	makeSchoolName(fList);
		return fList;
	}

	@Override
	public List<Family> findListByCondition(Family searchfamily) {
		Specification<Family> specification = new Specification<Family>() {
			@Override
			public Predicate toPredicate(Root<Family> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<>();
				predicateList.add(cb.equal(root.get("relation").as(String.class) , searchfamily.getRelation()));
				predicateList.add(cb.equal(root.get("isDeleted").as(Integer.class),0));
				if(StringUtils.isNotEmpty(searchfamily.getRealName())){
					predicateList.add(cb.like(root.get("realName").as(String.class),searchfamily.getRealName()));
				}
				if(StringUtils.isNotEmpty(searchfamily.getCompany())){
					predicateList.add(cb.like(root.get("company").as(String.class) , searchfamily.getCompany()));
				}
				return criteriaQuery.where(predicateList.toArray(new Predicate[0])).getRestriction();
			}
		};

		return familyDao.findAll(specification);
	}

	@Override
	public void deleteFamiliesBySchoolId(String schoolId) {
		familyDao.deleteFamiliesBySchoolId(schoolId);
	}

	@Override
	public List<Family> findByPhoneNum(String mobile) {
		return familyDao.findByPhoneNum(mobile);
	}

	@Override
	public List<Family> findByStudentIds(String[] stuIds){
		return familyDao.findByStudentIds(stuIds);
	}
}
