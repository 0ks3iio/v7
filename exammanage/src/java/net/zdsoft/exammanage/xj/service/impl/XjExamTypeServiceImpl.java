package net.zdsoft.exammanage.xj.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.xj.dao.XjExamTypeDao;
import net.zdsoft.exammanage.xj.entity.XjexamType;
import net.zdsoft.exammanage.xj.service.XjExamTypeService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yangsj  2017年10月12日下午4:52:16
 */
@Service
public class XjExamTypeServiceImpl extends BaseServiceImpl<XjexamType, String> implements XjExamTypeService {

    @Autowired
    private XjExamTypeDao xjExamTypeDao;

    @Override
    protected BaseJpaRepositoryDao<XjexamType, String> getJpaDao() {
        // TODO Auto-generated method stub
        return xjExamTypeDao;
    }

    @Override
    protected Class<XjexamType> getEntityClass() {
        // TODO Auto-generated method stub
        return XjexamType.class;
    }

    @Override
    public List<XjexamType> findByTypeKeys(String... typeKeys) {
        // TODO Auto-generated method stub
        return xjExamTypeDao.findByTypeKeys(typeKeys);
    }


}
