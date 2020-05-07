package net.zdsoft.bigdata.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.Tag;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;

import java.util.List;

/**
 * Created by wangdongdong on 2018/5/15 9:35.
 */
public interface TagService extends BaseService<Tag, String> {

    /**
     * 根据单位id和标签类型查询
     * @param unitId
     * @param tagType
     * @return
     */
    List<Tag> findTagsByUnitIdAndTagType(String unitId, Short tagType);

    /** 根据单位Id和标签类型获取指定名称的标签 */
    List<Tag> findTagsByUnitIdAndTagTypeAndTagNames(String unitId, Short tagType, String[] tagNames);

    /**
     * 保存标签并建立联系
     * @param tag
     * @param businessId (图表或报表id)
     */
    void saveTag(Tag tag, String businessId);

    /**
     * 根据标签id删除
     *
     * @param tagId
     */
    void deleteByTagId(String tagId) throws BigDataBusinessException;

    /**
     * 标签是否被使用
     * @param tagId 标签Id
     * @return
     */
    boolean isUsed(String tagId);

    /**
     * 标签是否被使用
     * @param tagId 标签Id
     * @param businessId chartId reportId 或者 cockpitId 不在计算之内
     * @return
     */
    boolean isUsed(String tagId, String businessId);

    void updateTagRelationByBusinessId(String[] tags, String businessId);
}
