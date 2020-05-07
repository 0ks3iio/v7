package net.zdsoft.base.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import net.zdsoft.base.dao.DeveloperDao;
import net.zdsoft.base.dto.DeveloperDto;
import net.zdsoft.base.entity.eis.Developer;
import net.zdsoft.base.entity.eis.OpenApiApply;
import net.zdsoft.base.entity.eis.OpenApiInterfaceType;
import net.zdsoft.base.enums.ApplyStatusEnum;
import net.zdsoft.base.enums.YesNoEnum;
import net.zdsoft.base.service.DeveloperService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Validators;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("developerService")
public class DeveloperServiceImpl extends BaseServiceImpl<Developer, String> implements DeveloperService {

    @Autowired
    private DeveloperDao developerDao;

    @Override
    protected BaseJpaRepositoryDao<Developer, String> getJpaDao() {
        return developerDao;
    }

    @Override
    protected Class<Developer> getEntityClass() {
        return Developer.class;
    }

    @Override
    public Developer findByTicketKey(String ticketKey) {
        return developerDao.findByTicketKey(ticketKey);
    }

    @Override
    public Developer findByUsername(String username) {
        return developerDao.findByUsername(username);
    }

    @Override
    public void save(Developer user) {
        developerDao.save(user);
    }

    @Override
    public int updatePwd(String newPwd, String id) {
        return developerDao.updatePwd(newPwd, id);
    }

    @Override
    public int updateDeveloper(Developer developer) {
        return developerDao.updateDeveloper(
                nullToString(developer.getDescription()), nullToString(developer.getRealName()),
                nullToString(developer.getMobilePhone()), nullToString(developer.getEmail()),
                nullToString(developer.getAddress()),nullToString(developer.getIps()), developer.getId());
    }

    private String nullToString(String str) {
        if (Validators.isEmpty(str)) {
            return "";
        }
        return str;
    }

    @Override
    public List<Developer> getAllOdereByCreationTime() {
        return developerDao.findAllOdereByCreationTime();
    }

    @Override
    public int updateUnitName(String id, String name) {
        return developerDao.updateUnitName(name, id);
    }

    @Override
    public int updateIps(String id, String ips) {
        return developerDao.updateIps(ips, id);
    }

	@Override
	public List<Developer> findByApkeyAndCreationTimeDesc(String apKey) {
		return developerDao.findByApkeyAndCreationTimeDesc(apKey);
	}

	@Override
	public void delInterface(String[] type, String ticketKey) {
		// TODO 删除申请接口和通过的字段
		
	}
}
