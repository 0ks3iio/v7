package net.zdsoft.basedata.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.SchtypeSectionDao;
import net.zdsoft.basedata.entity.SchtypeSection;
import net.zdsoft.basedata.service.SchtypeSectionService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service("schtypeSectionService")
public class SchtypeSectionServiceImpl extends BaseServiceImpl<SchtypeSection, String> implements SchtypeSectionService {

    @Autowired
    private SchtypeSectionDao schtypeSectionDao;

    @Override
    protected BaseJpaRepositoryDao<SchtypeSection, String> getJpaDao() {
        return schtypeSectionDao;
    }

    @Override
    protected Class<SchtypeSection> getEntityClass() {
        return SchtypeSection.class;
    }

    @Override
    public SchtypeSection findBySchoolType(String schoolType) {
        return schtypeSectionDao.findBySchoolType(schoolType);
    }

    @Override
    public Map<String, String> findAllMap() {
        List<SchtypeSection> findAll = this.findAll();
        Map<String, String> map = new HashMap<String, String>();
        for (SchtypeSection item : findAll) {
            map.put(item.getSchoolType(), item.getSection());
        }
        return map;
    }

}
