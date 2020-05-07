package net.zdsoft.bigdata.datav.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.datav.dao.ScreenGroupRepository;
import net.zdsoft.bigdata.datav.entity.ScreenGroup;
import net.zdsoft.bigdata.datav.service.ScreenGroupService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shenke
 * @since 2018/11/14 上午9:34
 */
@Service
public class ScreenGroupServiceImpl extends BaseServiceImpl<ScreenGroup, String>
        implements ScreenGroupService {

    @Resource
    private ScreenGroupRepository screenGroupRepository;

    @Override
    protected BaseJpaRepositoryDao<ScreenGroup, String> getJpaDao() {
        return screenGroupRepository;
    }

    @Override
    protected Class<ScreenGroup> getEntityClass() {
        return ScreenGroup.class;
    }

    @Override
    public List<ScreenGroup> getGroupsByUserId(String userId) {
        return screenGroupRepository.getAllByCreateUserId(userId, Sort.by(Sort.Order.asc("name")));
    }
}
