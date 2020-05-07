package net.zdsoft.studevelop.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.dao.StuDevelopPerformItemCodeDao;
import net.zdsoft.studevelop.data.entity.StuDevelopPerformItemCode;
import net.zdsoft.studevelop.data.service.StuDevelopPerformItemCodeService;
import net.zdsoft.studevelop.data.service.StudevelopMonthPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("StuDevelopPerfomItemCodeService")
public class StudevelopPerformItemCodeServiceImpl extends BaseServiceImpl<StuDevelopPerformItemCode ,String> implements StuDevelopPerformItemCodeService {

    @Autowired
    private StuDevelopPerformItemCodeDao stuDevelopPerformItemCodeDao;
    @Autowired
    private StudevelopMonthPerformanceService studevelopMonthPerformanceService;
    @Override
    protected BaseJpaRepositoryDao<StuDevelopPerformItemCode, String> getJpaDao() {
        return stuDevelopPerformItemCodeDao;
    }

    @Override
    protected Class<StuDevelopPerformItemCode> getEntityClass() {
        return StuDevelopPerformItemCode.class;
    }

    @Override
    public void deleteByItemId(String itemId) {
        stuDevelopPerformItemCodeDao.deleteByItemId(itemId);
    }

    @Override
    public List<StuDevelopPerformItemCode> getAllByItemId(String itemId) {
        return stuDevelopPerformItemCodeDao.getAllByItemId(itemId);
    }

    @Override
    public List<StuDevelopPerformItemCode> getPerformItemsByItemIds(String[] itemIds) {
        return stuDevelopPerformItemCodeDao.getPerformItemsByItemIds(itemIds);
    }

    @Override
    public void deleteItemCodebyId(String unitId ,String id) {
        delete(id);
        studevelopMonthPerformanceService.deleteByItemCodeId(unitId,id);
    }
}
