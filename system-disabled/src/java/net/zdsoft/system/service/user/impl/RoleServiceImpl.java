package net.zdsoft.system.service.user.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Specifications;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.dao.user.RoleDao;
import net.zdsoft.system.dao.user.RolePermDao;
import net.zdsoft.system.dao.user.UserRoleDao;
import net.zdsoft.system.entity.user.Role;
import net.zdsoft.system.entity.user.RolePerm;
import net.zdsoft.system.entity.user.UserRole;
import net.zdsoft.system.service.user.RoleService;

@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role, String> implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private RolePermDao rolePermDao;

    @Override
    protected BaseJpaRepositoryDao<Role, String> getJpaDao() {
        return roleDao;
    }

    @Override
    protected Class<Role> getEntityClass() {
        return Role.class;
    }

    @Override
    public List<Role> findByUnitId(String unitId) {
        return roleDao.findAll(new Specifications<Role>().addEq("unitId", unitId).getSpecification());
    }

    @Override
    public List<Role> saveAllEntitys(Role... role) {
        return roleDao.saveAll(checkSave(role));
    }

    @Override
    public List<Role> findListByIds(String[] ids) {
        return roleDao.findByIds(ids);
    }

    @Override
    public Role findByIdAndNameAndUnitId(final String id, final String name, final String unitId) {
        Specification<Role> specification = new Specification<Role>() {

            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                List<Predicate> ps = new ArrayList<Predicate>();
                if (StringUtils.isNotEmpty(id)) {
                    ps.add(cb.notEqual(root.get("id").as(String.class), id));
                }

                ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
                ps.add(cb.equal(root.get("name").as(String.class), name));

                cq.where(ps.toArray(new Predicate[0]));
                return cq.getRestriction();
            }
        };
        return roleDao.findOne(specification).orElse(null);
    }

    @Override
    public void insertRole(Role role, String[] userIds, Integer[] modelIds) {
        roleDao.save(role);

        if (null != userIds && userIds.length > 0) {
            List<UserRole> userRoleList = new ArrayList<UserRole>();
            for (String userId : userIds) {
                UserRole userRole = new UserRole();
                userRole.setId(UuidUtils.generateUuid());
                userRole.setRoleId(role.getId());
                userRole.setUserId(userId);
                userRoleList.add(userRole);
            }
            userRoleDao.saveAll(userRoleList);
        }

        if (null != modelIds && modelIds.length > 0) {
            List<RolePerm> rolePermList = new ArrayList<RolePerm>();
            for (Integer modelId : modelIds) {
                RolePerm rolePerm = new RolePerm();
                rolePerm.setId(UuidUtils.generateUuid());
                rolePerm.setModelId(modelId);
                rolePerm.setRoleId(role.getId());
                rolePerm.setType(3);
                rolePermList.add(rolePerm);
            }
            rolePermDao.saveAll(rolePermList);
        }
    }

    @Override
    public Role findById(String id) {
        return roleDao.findById(id).orElse(null);
    }

    @Override
    public void updateRole(Role role, String[] userIds, Integer[] modelIds, Integer[] allModelIds) {
        roleDao.update(role, role.getId(), new String[] { "name", "description", "modifyTime" });

        userRoleDao.deleteByRoleId(role.getId());
        if (null != userIds && userIds.length > 0) {
            List<UserRole> userRoleList = new ArrayList<UserRole>();
            for (String userId : userIds) {
                UserRole userRole = new UserRole();
                userRole.setId(UuidUtils.generateUuid());
                userRole.setRoleId(role.getId());
                userRole.setUserId(userId);
                userRoleList.add(userRole);
            }
            userRoleDao.saveAll(userRoleList);
        }

        if (null != allModelIds && allModelIds.length > 0) {
            rolePermDao.deleteByRoleIdAndModelIds(role.getId(), allModelIds);
            if (null != modelIds && modelIds.length > 0) {
                List<RolePerm> rolePermList = new ArrayList<RolePerm>();
                for (Integer modelId : modelIds) {
                    RolePerm rolePerm = new RolePerm();
                    rolePerm.setId(UuidUtils.generateUuid());
                    rolePerm.setModelId(modelId);
                    rolePerm.setRoleId(role.getId());
                    rolePerm.setType(3);
                    rolePermList.add(rolePerm);
                }
                rolePermDao.saveAll(rolePermList);
            }
        }
    }

	@Override
	public List<Role> findByUnitIdAndRoleType(String unitId, int roleTypeOper) {
		return roleDao.findByUnitIdAndRoleType(unitId,roleTypeOper);
	}

	@Override
	public Role findByNameAndUnitId(String roleName, String unitId) {
		return roleDao.findByNameAndUnitId(roleName,unitId);
	}
}
