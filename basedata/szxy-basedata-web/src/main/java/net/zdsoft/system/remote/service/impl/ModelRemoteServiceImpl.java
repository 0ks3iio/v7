package net.zdsoft.system.remote.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.server.Model;
import net.zdsoft.system.enums.user.UserTypeEnum;
import net.zdsoft.system.remote.service.ModelRemoteService;
import net.zdsoft.system.service.server.ModelService;
import net.zdsoft.system.service.server.SysPlatformModelService;
import net.zdsoft.system.service.user.RolePermService;
import net.zdsoft.system.service.user.UserPermService;

@Service("modelRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class ModelRemoteServiceImpl extends BaseRemoteServiceImpl<Model, Integer> implements ModelRemoteService {

    @Autowired
    private ModelService modelService;
    @Autowired
    private SysPlatformModelService sysPlatformModelService;
    @Autowired
    private UserPermService userPermService;
    @Autowired
    private RolePermService rolePermService;

    @Override
    protected BaseService<Model, Integer> getBaseService() {
        return modelService;
    }

    @Override
    public String findByUserId(String userId) {
        return SUtils.s(modelService.findByUserId(userId));
    }

    @Override
    public String findByUserId(String userId, Integer ownerType, String unitId, Integer unitClass) {
        if (ownerType == UserTypeEnum.STUDENT.getValue() || ownerType == UserTypeEnum.PARENT.getValue()) {// 学生或家长
            Integer platform = Integer.valueOf(ownerType.intValue() * (-1));
            return SUtils.s(sysPlatformModelService.findModelsByPlatform(platform));
        }
        else {// 教师、超管
            return SUtils.s(modelService.findAllEnableModelByUser(userId, unitId, unitClass));
        }
    }

    @Override
    public int countAuthUserBySubId(Integer subId) {
        List<Model> models = modelService.findBySubSystemId(subId);
        if (CollectionUtils.isEmpty(models)) {
            return 0;
        }
        List<Integer> modelIds = new ArrayList<Integer>();
        for (Model model : models) {
            modelIds.add(model.getId());
        }
        Set<String> userIds = new HashSet<String>();
        // 用户授权的userId
        userIds.addAll(userPermService.getUserIdsByModelIds(modelIds.toArray(new Integer[modelIds.size()])));
        // 用户组授权的userId
        userIds.addAll(rolePermService.getUserIdsByModelIds(modelIds.toArray(new Integer[modelIds.size()])));
        return userIds.size();
    }

    @Override
    public String findListBySubId(Integer subId) {
        return SUtils.s(modelService.findBySubSystemId(subId));
    }
}
