package net.zdsoft.bigdata.datav.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.datav.dao.ScreenLibraryRepository;
import net.zdsoft.bigdata.datav.entity.ScreenLibrary;
import net.zdsoft.bigdata.datav.service.ScreenLibraryService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author shenke
 * @since 2018/10/24 下午2:07
 */
@Service
public class ScreenLibraryServiceImpl extends BaseServiceImpl<ScreenLibrary, String> implements ScreenLibraryService {

    @Resource
    private ScreenLibraryRepository screenLibraryRepository;

    @Override
    protected BaseJpaRepositoryDao<ScreenLibrary, String> getJpaDao() {
        return screenLibraryRepository;
    }

    @Override
    protected Class<ScreenLibrary> getEntityClass() {
        return ScreenLibrary.class;
    }
}
