package net.zdsoft.bigdata.base.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.base.entity.PropertyTopic;

import java.util.List;

/**
 * @author yangkj
 * @since 2019/5/20 10:56
 */
public interface BgPropertyTopicService extends BaseService<PropertyTopic,String> {

    PropertyTopic findByName(String name);

    /**
     * 根据排序号返回list集合
     */
    List<PropertyTopic> findAllByOrderId();

    /**
     * 编辑时根据orderId重新排序
     */
    void editPropertyTopicByOrderId(PropertyTopic propertyTopic);

    /**
     * 新增时根据orderId重新排序
     */
    void savePropertyTopicByOrderId(PropertyTopic propertyTopic);

    /**
     * 删除后根据orderId重新排序
     */
    void deletePropertyTopicByOrderId(String id);

    /**
     * 获取当前数据库中资产主题的最大排序号
     */
    Integer findLargestOrderId();
}
