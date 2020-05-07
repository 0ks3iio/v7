package net.zdsoft.framework.pagedata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import net.zdsoft.framework.pagedata.dao.FwDataDetailJpaDao;
import net.zdsoft.framework.pagedata.entity.FwDataDetail;
import net.zdsoft.framework.pagedata.service.FwDataDetailService;

@Service("fwDataDetailService")
public class FwDataDetailServiceImpl implements FwDataDetailService {

    @Autowired
    private FwDataDetailJpaDao fwDataDetailJpaDao;

    @Override
    public List<FwDataDetail> findByGroupCodeOrderBy(String groupCode) {
        return fwDataDetailJpaDao.findByGroupCodeOrderBy(groupCode);
    }

    @Override
    public List<FwDataDetail> findByGroupCodeUnHidenOrderBy(String groupCode) {
        return fwDataDetailJpaDao.findByGroupCodeUnHidenOrderBy(groupCode);
    }

    @Override
    public FwDataDetail findOne(String id) {
       return fwDataDetailJpaDao.getOne(id);
    }

    @Override
    public List<FwDataDetail> save(List<FwDataDetail> details) {
        return fwDataDetailJpaDao.saveAll(details);     
    }

    @Override
    public FwDataDetail save(FwDataDetail detail) {
        return fwDataDetailJpaDao.save(detail);     
    }

}
