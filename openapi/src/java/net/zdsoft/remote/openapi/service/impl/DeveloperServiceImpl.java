package net.zdsoft.remote.openapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.remote.openapi.dao.DeveloperDao;
import net.zdsoft.remote.openapi.entity.Developer;
import net.zdsoft.remote.openapi.service.DeveloperService;

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
        return developerDao.updateDeveloper(nullToString(developer.getUnitName()),
                nullToString(developer.getDescription()), nullToString(developer.getRealName()),
                nullToString(developer.getMobilePhone()), nullToString(developer.getEmail()),
                nullToString(developer.getHomepage()), nullToString(developer.getAddress()),
                nullToString(developer.getIps()), developer.getId());
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

}
