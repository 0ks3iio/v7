/*
 * @(#)GroupItemsServiceImpl.java    Created on 2017年3月1日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.service.impl;

import net.zdsoft.basedata.dao.GroupItemsDao;
import net.zdsoft.basedata.dao.UnitDao;
import net.zdsoft.basedata.dao.UserDao;
import net.zdsoft.basedata.dto.GroupItemsDto;
import net.zdsoft.basedata.entity.GroupItems;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.enums.GroupTypeEnum;
import net.zdsoft.basedata.service.GroupItemsService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yeqi
 * @version $Revision: 1.0 $, $Date: 2017年3月1日 下午5:02:36 $
 */
@Service("groupItemsService")
public class GroupItemsServiceImpl extends BaseServiceImpl<GroupItems, String> implements GroupItemsService {
    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;
    @Autowired
    private GroupItemsDao groupItemsDao;
    @Autowired
    private UnitDao unitDao;
    @Autowired
    private UserDao userDao;

    @Override
    public List<String> findByGroupId(String groupId) {
        return groupItemsDao.findItemIdsByGroupId(groupId);
    }

    @Override
    public List<GroupItemsDto> findByGroupIdAndType(String groupId, Integer type) {
        List<GroupItemsDto> dtoList = new ArrayList<GroupItemsDto>();
        List<String> itemList = this.findByGroupId(groupId);
        if (type == GroupTypeEnum.ARCHIVESOTHERUNIT.getValue() || type == GroupTypeEnum.ARCHIVESUNIT.getValue()
                || type == GroupTypeEnum.ARRESSSUNIT.getValue()) {
            List<Unit> unitList = unitDao.findAllById(itemList);
            if (unitList != null) {
                for (Unit unit : unitList) {
                    GroupItemsDto dto = new GroupItemsDto();
                    dto.setId(unit.getId());
                    dto.setName(unit.getUnitName());
                    dtoList.add(dto);
                }
            }
        }
        else {
            List<User> userList = userDao.findAllById(itemList);
            String serverName = Evn.getRequest() == null ? null : Evn.getRequest().getServerName();
            String fileUrl = sysOptionRemoteService.getFileUrl(serverName);
            for (User user : userList) {
                GroupItemsDto dto = new GroupItemsDto();
                dto.setId(user.getId());
                dto.setName(user.getRealName());
                String avatarUrl = user.getAvatarUrl();
                if (!Validators.isEmpty(avatarUrl)) {
                    // 微课掌上通这边上传头像，数据库中存完整路径
                    if (avatarUrl.startsWith("http")) {
                        dto.setAvatarUrl(avatarUrl);
                    }
                    // 本地存储，数据库中只是file的后半部分
                    else {
                        dto.setAvatarUrl(fileUrl + avatarUrl);
                    }
                }
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    @Override
    protected BaseJpaRepositoryDao<GroupItems, String> getJpaDao() {
        return groupItemsDao;
    }

    @Override
    protected Class<GroupItems> getEntityClass() {
        return GroupItems.class;
    }

}
