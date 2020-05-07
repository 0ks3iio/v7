package net.zdsoft.syncdata.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.syncdata.dao.XFStudentExDao;
import net.zdsoft.syncdata.entity.XFStudentEx;
import net.zdsoft.syncdata.service.XFStudentExService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service("xFStudentExService")
@Lazy(true)
public class XFStudentExServiceImpl extends BaseServiceImpl<XFStudentEx, String> implements
        XFStudentExService {

    @Autowired
    private XFStudentExDao xfStudentExDao;

    @Override
    protected BaseJpaRepositoryDao<XFStudentEx, String> getJpaDao() {
        return xfStudentExDao;
    }

    @Override
    protected Class<XFStudentEx> getEntityClass() {
        return XFStudentEx.class;
    }

	@Override
	public List<XFStudentEx> saveAllEntitys(XFStudentEx... stexs) {
		return xfStudentExDao.saveAll(checkSave(stexs));
	}

}
