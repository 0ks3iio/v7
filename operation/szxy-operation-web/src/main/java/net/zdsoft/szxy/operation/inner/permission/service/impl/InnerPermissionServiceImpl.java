package net.zdsoft.szxy.operation.inner.permission.service.impl;

import net.zdsoft.szxy.operation.inner.dao.OpUserDao;
import net.zdsoft.szxy.operation.inner.entity.OpUser;
import net.zdsoft.szxy.operation.inner.permission.dao.GroupDao;
import net.zdsoft.szxy.operation.inner.permission.dao.GroupModuleRelationDao;
import net.zdsoft.szxy.operation.inner.permission.dao.ModuleDao;
import net.zdsoft.szxy.operation.inner.permission.dao.ModuleOperateDao;
import net.zdsoft.szxy.operation.inner.permission.dao.UserGroupRelationDao;
import net.zdsoft.szxy.operation.inner.permission.dao.UserModuleRelationDao;
import net.zdsoft.szxy.operation.inner.permission.dto.OperateAuth;
import net.zdsoft.szxy.operation.inner.permission.entity.Group;
import net.zdsoft.szxy.operation.inner.permission.entity.GroupModuleRelation;
import net.zdsoft.szxy.operation.inner.permission.entity.Module;
import net.zdsoft.szxy.operation.inner.permission.entity.ModuleOperate;
import net.zdsoft.szxy.operation.inner.permission.entity.UserModuleRelation;
import net.zdsoft.szxy.operation.inner.permission.service.InnerPermissionService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2019/3/30 下午4:26
 */
@Service
public class InnerPermissionServiceImpl implements InnerPermissionService {

    @Resource
    private GroupDao groupDao;
    @Resource
    private GroupModuleRelationDao groupModuleRelationDao;
    @Resource
    private UserModuleRelationDao userModuleRelationDao;
    @Resource
    private UserGroupRelationDao userGroupRelationDao;
    @Resource
    private ModuleDao moduleDao;
    @Resource
    private ModuleOperateDao moduleOperateDao;
    @Resource
    private OpUserDao opUserDao;

    @Override
    public List<Module> getModuleByUserId(String userId) {

        if (isAdmin(userId)) {
            return moduleDao.findAll();
        }

        //获取用户单独授权的系统
        Set<String> singleAuthModuleIds = userModuleRelationDao.getModuleIdByUserId(userId);
        Set<String> authModuleIds = new HashSet<>(singleAuthModuleIds);

        //获取用户所在分组
        Set<String> groupIds = userGroupRelationDao.getUserGroupIdsByUserId(userId);
        if (!groupIds.isEmpty()) {
            Set<String> groupAuthModuleIds = groupModuleRelationDao.getModuleIdByGroupIds(groupIds.toArray(new String[0]));
            authModuleIds.addAll(groupAuthModuleIds);
        }

        if (!authModuleIds.isEmpty()) {
            return moduleDao.getModulesByIds(authModuleIds.toArray(new String[0]));
        }
        return Collections.emptyList();
    }

    private boolean isAdmin(String userId) {
        Optional<OpUser> user = opUserDao.findById(userId);
        return "admin".equals(user.get().getUsername());
    }

    @Override
    public Set<OperateAuth> getOperateByUserId(String userId) {

        if (isAdmin(userId)) {
            List<Module> modules = moduleDao.findAll();
            Map<String, Module> moduleMap = modules.stream().collect(Collectors.toMap(Module::getId, Function.identity()));

            String[] moduleIds = modules.stream().map(Module::getId).toArray(String[]::new);
            return moduleOperateDao.getModuleOperatesByModuleIds(moduleIds).stream().map(e -> {
                OperateAuth auth = new OperateAuth();
                auth.setOperateCode(e.getOperateCode());
                auth.setUrlPrefix(moduleMap.get(e.getModuleId()).getUrlPrefix());
                return auth;
            }).collect(Collectors.toSet());
        }

        Map<String, ModuleOperateMapper> mappers = new HashMap<>();
        Set<String> operateIdSet = new HashSet<>();
        Set<String> moduleIdSet = new HashSet<>();

        //获取用户单独授权的系统
        List<UserModuleRelation> userModuleRelations = userModuleRelationDao.getUserModuleRelationsByUserId(userId);
        for (UserModuleRelation userModuleRelation : userModuleRelations) {
            ModuleOperateMapper mapper = new ModuleOperateMapper();
            mapper.setModuleId(userModuleRelation.getModuleId());
            mapper.setOperateId(userModuleRelation.getOperateId());
            //存在相同的功能点，合并数据范围
            if (mappers.containsKey(mapper.createKey())) {
            } else {
                mappers.put(mapper.createKey(), mapper);
                operateIdSet.add(mapper.getOperateId());
                moduleIdSet.add(mapper.getModuleId());
            }
        }


        //获取分组数据
        Set<String> groupIds = userGroupRelationDao.getUserGroupIdsByUserId(userId);
        if (!groupIds.isEmpty()) {
            String[] groupIdArray = groupIds.toArray(new String[0]);
            List<GroupModuleRelation> groupModuleRelations = groupModuleRelationDao.getGroupModuleRelationsByGroupIds(groupIdArray);

            for (GroupModuleRelation groupModuleRelation : groupModuleRelations) {
                ModuleOperateMapper mapper = new ModuleOperateMapper();
                mapper.setModuleId(groupModuleRelation.getModuleId());
                mapper.setOperateId(groupModuleRelation.getOperateId());
                mappers.put(mapper.createKey(), mapper);
                operateIdSet.add(mapper.getOperateId());
                moduleIdSet.add(mapper.getModuleId());
            }
        }

        if (operateIdSet.isEmpty()) {
            return Collections.emptySet();
        }

        Map<String, String> operateCodeMap =

                moduleOperateDao.getModuleOperatesByIds(operateIdSet.toArray(new String[0])).stream()
                        .collect(Collectors.toMap(ModuleOperate::getId, ModuleOperate::getOperateCode));

        Map<String, String> moduleUrlMap =
                moduleDao.getModulesByIds(moduleIdSet.toArray(new String[0])).stream()
                        .collect(Collectors.toMap(Module::getId, Module::getUrlPrefix));

        return mappers.values().stream().map(e -> {
            OperateAuth auth = new OperateAuth();
            auth.setOperateCode(operateCodeMap.get(e.getOperateId()));
            auth.setUrlPrefix(Optional.ofNullable(moduleUrlMap.get(e.getModuleId())).orElseThrow(()->new RuntimeException("用户权限数据存在异常")));
            return auth;
        }).filter(e->StringUtils.isNotBlank(e.getOperateCode())).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getAuthRegionCodes(String userId) {
        //用户单独的授权范围
        Optional<OpUser> optional = opUserDao.findById(userId);
        OpUser user = optional.orElseThrow(()->new RuntimeException("No Current User"));
        Set<String> regionCodes = new HashSet<>();
        if (user.getAuthRegionCode() != null && !Group.ALL_REGION.equals(user.getAuthRegionCode())) {
            regionCodes.add(user.getAuthRegionCode());
        }
        //获取分组的地区范围
        String[] groupIds =  userGroupRelationDao.getUserGroupIdsByUserId(userId).toArray(new String[0]);
        if (ArrayUtils.isNotEmpty(groupIds)) {
            Set<String> groupRegions = groupDao.getGroupsByIds(groupIds).stream().map(Group::getRegionCode)
                    .filter(Objects::nonNull)
                    .filter(e->!Group.ALL_REGION.equals(e))
                    .collect(Collectors.toSet());
            regionCodes.addAll(groupRegions);
        }
        return regionCodes;
    }
}
