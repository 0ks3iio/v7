package net.zdsoft.system.service.server.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.enums.UnitClassEnum;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Specifications;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.system.dao.server.ModelDao;
import net.zdsoft.system.entity.SysPlatformModel;
import net.zdsoft.system.entity.server.Model;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.entity.user.RolePermission;
import net.zdsoft.system.entity.user.UserRole;
import net.zdsoft.system.enums.YesNoEnum;
import net.zdsoft.system.enums.server.AppStatusEnum;
import net.zdsoft.system.enums.server.OrderTypeEnum;
import net.zdsoft.system.enums.user.UserTypeEnum;
import net.zdsoft.system.service.server.ModelService;
import net.zdsoft.system.service.server.ServerAuthorizeService;
import net.zdsoft.system.service.server.ServerService;
import net.zdsoft.system.service.server.SysPlatformModelService;
import net.zdsoft.system.service.user.RolePermService;
import net.zdsoft.system.service.user.RolePermissionService;
import net.zdsoft.system.service.user.UserPermService;
import net.zdsoft.system.service.user.UserRoleService;

@Service("modelService")
public class ModelServiceImpl extends BaseServiceImpl<Model, Integer> implements ModelService {

    @Autowired
    private ModelDao modelDao;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private UserRemoteService userRemoteService;
    @Autowired
    private SysPlatformModelService sysPlatformModelService;
    @Autowired
    private ServerService serverService;
    @Autowired
    private ServerAuthorizeService serverAuthorizeService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private UserPermService userPermService;
    @Autowired
    private RolePermService rolePermService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    
    @Override
    public List<Model> findBySubSystemId(Integer subSystem) {
        return modelDao.findBySubSystem(subSystem);
    }

    @Override
    public List<Model> findByUnitClass(final Integer unitClass) {
        Specifications<Model> spec = new Specifications<Model>();
        spec.addEq("unitClass", unitClass);
        return modelDao.findAll(spec.getSpecification());
    }
    
    @Override
    public List<Model> findByUserId(String userId) {
        User user = User.dc(userRemoteService.findOneById(userId, true));
        if(user == null)
    		return new ArrayList<>();
         //取授权模块
        List<UserRole> urs = userRoleService.findByUserId(userId);
        List<Model> ms = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(urs)) {
            List<String> roleIds = EntityUtils.getList(urs, UserRole::getRoleId);
            List<RolePermission> rps = rolePermissionService.findListByIn("roleId", roleIds.toArray(new String[0]));
            Set<Integer> moIds = new HashSet<>();
            for (RolePermission item : rps) {
                moIds.add(Integer.valueOf(item.getModuleId()));
            }
            if (CollectionUtils.isNotEmpty(moIds)) {
                if ( moIds.size() > 1000 ) {
                    List<Integer> modelIds = Lists.newArrayList(moIds);
                    int loopNumber = modelIds.size()/1000;
                    for (int i=0; i<loopNumber; i++ ) {
                        List<Model> models = modelDao.findExceptMobileModelByIds(modelIds.subList(i * 1000, (i+1)*1000).toArray(new Integer[0]));
                        if ( models != null ) {
                            ms.addAll(models);
                        }
                        //
                        if ( i+1 == loopNumber &&  modelIds.size() -(1000 * loopNumber) > 0) {
                            List<Model> modelList = modelDao.findExceptMobileModelByIds(modelIds.subList((i+1) * 1000, modelIds.size()).toArray(new Integer[0]));
                            if ( models != null ) {
                                ms.addAll(modelList);
                            }
                        }
                    }
                } else {
                    ms = modelDao.findExceptMobileModelByIds(moIds.toArray(new Integer[moIds.size()]));
                }
            }
        }
        Unit unit = Unit.dc(unitRemoteService.findOneById(user.getUnitId()));

        /**
         * 管理员账号登录的时候，默认有系统管理权限， add by linqz, 2017-3-21
         */
        boolean isAdmin = (user.getUserType() != null)
                && (user.getUserType() == User.USER_TYPE_TOP_ADMIN
                || (user.getUserType() == User.USER_TYPE_UNIT_ADMIN));
        if ( isAdmin ) {
            List<Model> models = null;
            if (unit.getUnitClass() == null) {
                models = modelDao.findBySubSystemIn(new Integer[] { 99, new Integer(700) });
            }
            else {
                models = modelDao.findBySubSystems(unit.getUnitClass(), new Integer[] { 99, 700 });
            }
            ms.addAll(models);
        }

        //如果是文轩学生和家长登录的，现在先定死只取  智能分班排课  7601  学生选课 7602 
        String userName = user.getUsername();
		if(StringUtils.startsWithIgnoreCase(userName, "scwx_")) {
			if(!(user.getOwnerType().equals(User.OWNER_TYPE_TEACHER))){
				List<SysPlatformModel> modelList = new ArrayList<>();
				modelList.addAll(sysPlatformModelService.findListByIds(new Integer[] {7602,7601}));
				if (CollectionUtils.isNotEmpty(modelList)) {
					ms = ms == null ? Lists.<Model> newArrayList() : ms;
					ms.addAll(SysPlatformModel.turn2Models(modelList));
				}
			}
		}else {
			// 取平台模块
			if (user != null) {
				Integer platform = user.getOwnerType() * (-1);
				List<SysPlatformModel> modelList = sysPlatformModelService.findByPlatform(platform);
				if (CollectionUtils.isNotEmpty(modelList)) {
					ms = ms == null ? Lists.<Model> newArrayList() : ms;
					ms.addAll(SysPlatformModel.turn2Models(modelList));
				}
			}
		}
        
        
        return ms == null ? new ArrayList<Model>() : ms;
    }

    @Override
    public Model findByIntId(Integer id) {
        Specifications<Model> spec = new Specifications<Model>();
        spec.addEq("id", id);
        return modelDao.findOne(spec.getSpecification()).orElse(null);
    }

    @Override
    protected BaseJpaRepositoryDao<Model, Integer> getJpaDao() {
        return modelDao;
    }

    @Override
    protected Class<Model> getEntityClass() {
        return Model.class;
    }

    @Override
    public List<Model> findBySubSystemIdsAndParentIdsAndUnitClass(Integer[] serverIds, Integer[] parentIds,
            Integer unitClass) {
        return modelDao.findBySubSystemIdsAndParentIdsAndUnitClass(serverIds, parentIds, unitClass);
    }

    @Override
    public List<Model> findAllEnableModelByUser(String userId, String unitId, Integer unitClass) {
        List<Model> modelList = new ArrayList<Model>();

        String sections = "";
        if (null != unitClass && unitClass == UnitClassEnum.SCHOOL.getValue()) {
            sections = schoolRemoteService.findSectionsById(unitId);
        }

        Set<Integer> serverIdSet = new HashSet<Integer>();

        // 系统订阅类型
        List<Server> systemServerList = serverService.findServerList(new Integer[] { OrderTypeEnum.SYSTEM.getValue() },
                unitClass, AppStatusEnum.ONLINE.getValue(), sections, UserTypeEnum.TEACHER.getValue(), null, null,
                "isNotNull");

        if (CollectionUtils.isNotEmpty(systemServerList)) {
            Set<Integer> systemServerId = EntityUtils.getSet(systemServerList, "subId");
            serverIdSet.addAll(systemServerId);
        }

        // 单位订阅无需授权应用
        List<Server> noAuthorServerList = serverAuthorizeService.listServers(unitId, unitClass,
                UserTypeEnum.TEACHER.getValue(), new Integer[] { OrderTypeEnum.UNIT_PERSONAL_NO_AUTH.getValue() },
                sections, null, "isNotNull");
        if (CollectionUtils.isNotEmpty(noAuthorServerList)) {
            Set<Integer> noAuthorServerId = EntityUtils.getSet(noAuthorServerList, "subId");
            serverIdSet.addAll(noAuthorServerId);
        }

        // 系统订阅应用和单位订阅无需授权应用下模块
        if (serverIdSet.size() > 0) {
            modelList.addAll(modelDao.findExceptMobileModelBySubsystemAndUnitClass(
                    serverIdSet.toArray(new Integer[serverIdSet.size()]), unitClass));
        }

        // 授权模块
        modelList.addAll(findAuthorModel(userId, unitId, unitClass, sections));

        // 系统用户组模块
        modelList.addAll(findSystemRoleModel(userId));

        return modelList;
    }

    /**
     * 查找用户所在系统组模块
     * 
     * @author cuimq
     * @param userId
     * @return
     */
    private List<Model> findSystemRoleModel(String userId) {
        List<Model> modelList = new ArrayList<Model>();

        List<Integer> modelIdList = rolePermService.findModelIdsByUserIdAndIsSystem(userId, YesNoEnum.YES.getValue());

        if (CollectionUtils.isNotEmpty(modelIdList)) {
            modelList.addAll(modelDao.findExceptMobileModelByIds(modelIdList.toArray(new Integer[modelIdList.size()])));
            modelList.addAll(findParModels(modelList));
        }
        return modelList;
    }

    /**
     * 获取授权模块
     * 
     * @author cuimq
     * @param userId
     * @param unitId
     * @param unitClass
     * @param sections
     * @return
     */
    private List<Model> findAuthorModel(String userId, String unitId, Integer unitClass, String sections) {
        List<Model> modelList = new ArrayList<Model>();

        List<Server> authorServerList = new ArrayList<Server>();

        // 单位订阅需要授权
        authorServerList.addAll(serverAuthorizeService.listServers(unitId, unitClass, UserTypeEnum.TEACHER.getValue(),
                new Integer[] { OrderTypeEnum.UNIT_PERSONAL_AUTH.getValue() }, sections, null, "isNotNull"));
        // 系统订阅需授权
        authorServerList.addAll(serverService.findServerList(new Integer[] { OrderTypeEnum.SYSTEM.getValue() },
                unitClass, AppStatusEnum.ONLINE.getValue(), sections, UserTypeEnum.TEACHER.getValue(),
                new Integer[] {}, Integer.valueOf(YesNoEnum.YES.getValue()), "isNotNull"));

        if (CollectionUtils.isNotEmpty(authorServerList)) {
        	Set<String> authorServerIdSet = authorServerList.stream().map(Server::getSubId).map(String::valueOf).collect(Collectors.toSet());
            List<Model> authorModelList = modelDao.findExceptMobileModelBySubsystemAndUnitClass(
                    authorServerIdSet.toArray(new Integer[authorServerIdSet.size()]), unitClass);
            List<Integer> authorModelIds = EntityUtils.getList(authorModelList, Model::getId);

            if (CollectionUtils.isNotEmpty(authorModelIds)) {
                List<Integer> modelIds = new ArrayList<>();
                // 用户已授权模块
                modelIds.addAll(userPermService.findModelIdsByUserId(userId));
                // 用户所在用户组已授权模块
                modelIds.addAll(rolePermService.findModelIdsByUserIdAndIsSystem(userId, YesNoEnum.NO.getValue()));

                // 过滤已不在订阅应用模块下的授权模块
                if (CollectionUtils.isNotEmpty(modelIds)) {
                    List<Integer> newModelIds = new ArrayList<Integer>();
                    for (Integer modelId : modelIds) {
                        if (authorModelIds.contains(modelId)) {
                            newModelIds.add(modelId);
                        }
                    }

                    if (CollectionUtils.isNotEmpty(newModelIds)) {
                        modelList.addAll(modelDao.findExceptMobileModelByIds(newModelIds
                                .toArray(new Integer[newModelIds.size()])));
                        modelList.addAll(findParModels(modelList));
                    }
                }
            }
        }
        return modelList;
    }

    /**
     * 获取父模块
     * 
     * @author cuimq
     * @param modelList
     * @return
     */
    private List<Model> findParModels(List<Model> modelList) {
        List<Model> parModelList = new ArrayList<Model>();
        if (CollectionUtils.isNotEmpty(modelList)) {
            List<Integer> parModelIdList = new ArrayList<Integer>();
            for (Model model : modelList) {
                if (model.getParentId() != -1) {
                    parModelIdList.add(model.getParentId());
                }
            }
            parModelList.addAll(modelDao.findExceptMobileModelByIds(parModelIdList.toArray(new Integer[parModelIdList
                    .size()])));
        }
        return parModelList;
    }
}
