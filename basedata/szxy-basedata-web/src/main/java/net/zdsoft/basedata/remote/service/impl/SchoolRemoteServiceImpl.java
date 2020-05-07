package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.SchoolDao;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.SchoolService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;

@Service("schoolRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class SchoolRemoteServiceImpl extends BaseRemoteServiceImpl<School,String> implements SchoolRemoteService {

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private SchoolDao schoolDao;

    @Override
    public String findByRegionCodes(String... regionCodes) {
        return SUtils.s(schoolService.findListByIn("regionCode", regionCodes));
    }

    @Override
    protected BaseService<School, String> getBaseService() {
        return schoolService;
    }

    @Override
    public String findByCode(String code) {
        // TODO Auto-generated method stub
        return SUtils.s(schoolDao.findByCode(code));
    }

    @Override
    public String findByDistrictId(String districtId) {
        // TODO Auto-generated method stub schoolDistrictId
        return SUtils.s(schoolDao.findByDistrictId(districtId));
    }

    @Override
    public String findByTypeSectionName(String runschtype, String section, String schoolName) {
        // TODO Auto-generated method stub
        return SUtils.s(schoolDao.findByTypeSectionName(runschtype, section, schoolName));
    }

    @Override
    public String findByRegiontypeSection(String regiontype, String section) {
        // TODO Auto-generated method stub
        return SUtils.s(schoolDao.findByRegiontypeSection(regiontype, section));
    }

    @Override
    public String findBySectionName(String section, String schoolName) {
        // TODO Auto-generated method stub
        return SUtils.s(schoolDao.findBySectionName(section, schoolName));
    }

    @Override
    public String saveAllEntitys(String entitys) {
        School[] dt = SUtils.dt(entitys, new TR<School[]>() {
        });
        return SUtils.s(schoolService.saveAllEntitys(dt));
    }

    @Override
    public String findSectionsById(String id) {
        return schoolService.findSectionsById(id);
    }

	@Override
	public String findByCodeIn(String[] schoolCodes) {
		return SUtils.s(schoolDao.findByCodeIn(schoolCodes));
	}

}
