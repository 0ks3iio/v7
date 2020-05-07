package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.TeacherDao;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.service.DeptService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;

@Service("teacherService")
public class TeacherServiceImpl extends BaseServiceImpl<Teacher, String> implements TeacherService {

    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private UserService userService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private DeptService deptService;


    @Override
    public List<Teacher> findByUnitId(final String unitId) {
        List<Teacher> teachers = teacherDao.findByUnitId(unitId);
        return teachers;
    }

    @Override
    public List<Teacher> findByUnitId(final String unitId, Pagination page) {
        List<Teacher> teachers = teacherDao.findByUnitId(unitId, Pagination.toPageable(page));
        Integer count = countByUnitId(unitId);
        page.setMaxRowCount(count);
        return teachers;
    }

    @Override
    public List<Teacher> findByDeptId(final String deptId) {
        List<Teacher> teachers = teacherDao.findByDeptId(deptId);
        return teachers;
    }

    @Override
    public List<Teacher> findByDeptId(final String deptId, final Pagination page) {
        Integer count = countByDeptId(deptId);
        page.setMaxRowCount(count);
        return teacherDao.findByDeptId(deptId, Pagination.toPageable(page));
    }

    private Integer countByDeptId(String deptId) {
        Long count = teacherDao.countByDeptId(deptId);
        return count == null ? 0 : count.intValue();
    }

    private Integer countByUnitId(String unitId) {
        Long count = teacherDao.countByUnitId(unitId);
        return count == null ? 0 : count.intValue();
    }

    @Override
    protected BaseJpaRepositoryDao<Teacher, String> getJpaDao() {
        return teacherDao;
    }

    @Override
    public Teacher findByUsername(String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return null;
        }
        String ownerId = user.getOwnerId();
        return findOne(ownerId);
    }

    @Override
    public Teacher findByUserId(String userId) {
        User user = userService.findOne(userId);
        if (user == null) {
            return null;
        }
        String ownerId = user.getOwnerId();
        return findOne(ownerId);
    }

    @Override
    public void saveWithUser(Teacher teacher, User user) {
        saveAllEntitys(teacher);
        if (user != null) {
            userService.saveAllEntitys(user);
        }
    }

    @Override
    protected Class<Teacher> getEntityClass() {
        return Teacher.class;
    }

    @Override
    public List<Teacher> saveAllEntitys(Teacher... teacher) {
        return teacherDao.saveAll(checkSave(teacher));
    }

    @Override
    public void deleteAllByIds(String... id) {
        if (id != null && id.length > 0) {
            teacherDao.deleteAllByIds(id);
        }
    }

    @Override
    public Map<String, List<Teacher>> findMapByDeptIdIn(String[] deptIds) {
        List<Teacher> findByDeptIdsIn = findByDeptIdsIn(deptIds);
        Map<String, List<Teacher>> map = new HashMap<String, List<Teacher>>();
        for (Teacher item : findByDeptIdsIn) {
            List<Teacher> list = map.get(item.getDeptId());
            if (list == null) {
                list = new ArrayList<Teacher>();
                map.put(item.getDeptId(), list);
            }
            list.add(item);
        }
        return map;
    }

    private List<Teacher> findByDeptIdsIn(final String[] deptIds) {
        List<Teacher> findByDeptIdIn = new ArrayList<Teacher>();
        if (deptIds != null && deptIds.length > 0) {
            Specification<Teacher> s = new Specification<Teacher>() {
                @Override
                public Predicate toPredicate(Root<Teacher> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                    List<Predicate> ps = new ArrayList<Predicate>();
                    queryIn("deptId", deptIds, root, ps, null);
                    ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                    List<Order> orderList = new ArrayList<Order>();
                    orderList.add(cb.asc(root.get("teacherCode").as(String.class)));
                    cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                    return cq.getRestriction();
                }
            };
            findByDeptIdIn = teacherDao.findAll(s);
        }
        return findByDeptIdIn;
    }

    @Override
    public List<Teacher> findByTeacherNameAndIdentityCard(String teacherName, String identityCard) {
        List<Teacher> teaList = teacherDao.findByTeacherNameAndIdentityCard(teacherName, identityCard);
        makeUnitName(teaList);
        return teaList;
    }

    @Override
    public List<Teacher> findByTeacherNameAndIdentityCardWithNoUser(String teacherName, String identityCard) {
        List<Teacher> teaList = teacherDao.findByTeacherNameAndIdentityCardWithNoUser(teacherName, identityCard);
        makeUnitName(teaList);
        return teaList;
    }

    private void makeUnitName(List<Teacher> sList) {
        if (CollectionUtils.isNotEmpty(sList)) {
            Set<String> deptIds = EntityUtils.getSet(sList, "deptId");
            Set<String> unitIds = EntityUtils.getSet(sList, "unitId");
            Map<String, Dept> deptMap = deptService.findMapByIdIn(deptIds.toArray(new String[]{}));
            Map<String, Unit> unitMap = unitService.findMapByIdIn(unitIds.toArray(new String[]{}));

            if (unitMap != null) {
                for (Teacher tea : sList) {
                    tea.setDeptName((deptMap.get(tea.getDeptId()) == null) ? "" : deptMap.get(tea.getDeptId())
                            .getDeptName());
                    tea.setUnitName((unitMap.get(tea.getUnitId()) == null) ? "" : unitMap.get(tea.getUnitId())
                            .getUnitName());
                }
            }
        }

    }

    @Override
    public List<Teacher> findByTeacherNameAndMobilePhone(String realName,
                                                         String mobilePhone) {
        List<Teacher> teaList = teacherDao.findByTeacherNameAndMobilePhone(realName, mobilePhone);
        makeUnitName(teaList);
        return teaList;
    }

    @Override
    public Teacher findByCardNumber(String unitId, String cardNumber) {
        return teacherDao.findByCardNumber(unitId, cardNumber);
    }

    @Override
    public List<Teacher> findListByTeacherName(String unitId, String teacherName) {
        teacherName = "%" + teacherName + "%";
        if (StringUtils.isBlank(unitId)) {
            return teacherDao.findByTeacherNameLike(teacherName);
        } else {
            return teacherDao.findByTeacherNameLike(unitId, teacherName);
        }
    }

    @Override
    public Map<String, String> findPartByTeacher(String[] ids) {
        List<Object[]> teacherlist = null;
        if (ids != null && ids.length > 0) {
            teacherlist = teacherDao.findPartTeachByIds(ids);
        }
        Map<String, String> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(teacherlist)) {
            for (Object[] strs : teacherlist) {
                map.put((String) strs[0], (String) strs[1]);
            }
        }
        return map;
    }

    @Override
    public void deleteTeachersByUnitId(String unitId) {
        teacherDao.deleteTeahersByUnitId(unitId);
    }
}
