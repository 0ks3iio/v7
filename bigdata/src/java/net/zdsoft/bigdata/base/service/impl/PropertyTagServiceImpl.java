package net.zdsoft.bigdata.base.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.base.dao.PropertyTagDao;
import net.zdsoft.bigdata.base.entity.PropertyTag;
import net.zdsoft.bigdata.base.service.PropertyTagService;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class PropertyTagServiceImpl extends BaseServiceImpl<PropertyTag,String>
                                        implements PropertyTagService {

    @Resource
    private PropertyTagDao propertyTagDao;

    @Override
    protected BaseJpaRepositoryDao<PropertyTag, String> getJpaDao() {
        return propertyTagDao;
    }

    @Override
    protected Class<PropertyTag> getEntityClass() {
        return PropertyTag.class;
    }

    @Override
    public void savePropertyTag(PropertyTag propertyTag) throws BigDataBusinessException {

        long count = this.countBy("name", propertyTag.getName());
        if (count > 0) {
            throw new BigDataBusinessException("标签不能重复!");
        }

        propertyTag.setModifyTime(new Date());
        if (StringUtils.isBlank(propertyTag.getId())) {
            propertyTag.setId(UuidUtils.generateUuid());
            propertyTag.setCreationTime(new Date());
            propertyTagDao.save(propertyTag);
            return;
        }

        propertyTagDao.update(propertyTag, new String[]{"name", "modifyTime"});
    }
}
