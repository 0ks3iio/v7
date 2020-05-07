package net.zdsoft.bigdata.data.dao;

import java.util.List;

import net.zdsoft.bigdata.data.entity.Tag;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * Created by wangdongdong on 2018/5/15.
 */
public interface TagDao extends BaseJpaRepositoryDao<Tag, String> {

    /**
     * 根据单位id和标签类型查询
     * @param unitId
     * @param tagType
     * @return
     */
    List<Tag> getTagsByUnitIdAndTagType(String unitId, Short tagType);

    /** 根据单位Id和标签类型获取指定名称的标签 */
    List<Tag> getTagsByUnitIdAndTagTypeAndTagNameIn(String unitId, Short tagType, String[] tagName);

}
