package net.zdsoft.exammanage.xj.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.xj.dao.XjExamContrastDao;
import net.zdsoft.exammanage.xj.entity.XjexamContrast;
import net.zdsoft.exammanage.xj.service.XjExamContrastService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yangsj  2017年10月12日下午4:47:23
 */
@Service
public class XjExamContrastServiceImpl extends BaseServiceImpl<XjexamContrast, String> implements XjExamContrastService {

    @Autowired
    private XjExamContrastDao xjExamContrastDao;

    @Override
    protected BaseJpaRepositoryDao<XjexamContrast, String> getJpaDao() {
        // TODO Auto-generated method stub
        return xjExamContrastDao;
    }

    @Override
    protected Class<XjexamContrast> getEntityClass() {
        // TODO Auto-generated method stub
        return XjexamContrast.class;
    }


}
