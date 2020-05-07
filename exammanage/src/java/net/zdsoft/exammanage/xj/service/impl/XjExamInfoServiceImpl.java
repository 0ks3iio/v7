package net.zdsoft.exammanage.xj.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.xj.dao.XjExamInfoDao;
import net.zdsoft.exammanage.xj.entity.XjexamInfo;
import net.zdsoft.exammanage.xj.service.XjExamInfoService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yangsj  2017年10月12日下午4:49:52
 */
@Service
public class XjExamInfoServiceImpl extends BaseServiceImpl<XjexamInfo, String> implements XjExamInfoService {

    @Autowired
    private XjExamInfoDao xjExamInfoDao;

    @Override
    protected BaseJpaRepositoryDao<XjexamInfo, String> getJpaDao() {
        // TODO Auto-generated method stub
        return xjExamInfoDao;
    }

    @Override
    protected Class<XjexamInfo> getEntityClass() {
        // TODO Auto-generated method stub
        return XjexamInfo.class;
    }

    @Override
    public XjexamInfo findStuSeatInfo(String studentName, String admission, String type) {
        // TODO Auto-generated method stub
        return xjExamInfoDao.findStuSeatInfo(studentName, admission, type);

    }

    @Override
    public XjexamInfo findStuInfo(String studentName, String admission) {
        // TODO Auto-generated method stub
        return xjExamInfoDao.findStuInfo(studentName, admission);
    }
}
