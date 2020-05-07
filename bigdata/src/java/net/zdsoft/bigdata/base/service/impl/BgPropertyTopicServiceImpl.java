package net.zdsoft.bigdata.base.service.impl;

import com.alibaba.fastjson.JSON;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.base.dao.BgPropertyTopicDao;
import net.zdsoft.bigdata.base.entity.PropertyTopic;
import net.zdsoft.bigdata.base.enu.PropertyTopicOrderCode;
import net.zdsoft.bigdata.base.service.BgPropertyTopicService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yangkj
 * @since 2019/5/20 10:56
 */
@Service("bgPropertyTopicService")
public class BgPropertyTopicServiceImpl extends BaseServiceImpl<PropertyTopic,String>
                                        implements BgPropertyTopicService {

    @Autowired
    private BgPropertyTopicService bgPropertyTopicService;

    @Autowired
    private BgPropertyTopicDao bgPropertyTopicDao;

    @Override
    protected BaseJpaRepositoryDao<PropertyTopic, String> getJpaDao() {
        return bgPropertyTopicDao;
    }

    @Override
    protected Class<PropertyTopic> getEntityClass() {
        return PropertyTopic.class;
    }

    @Override
    public PropertyTopic findByName(String name) {
        return bgPropertyTopicDao.findByName(name);
    }

    @Override
    public List<PropertyTopic> findAllByOrderId() {
        List<PropertyTopic> propertyTopicList = bgPropertyTopicDao.findAll();
        int orderLength = 2;
        if (propertyTopicList.size()>=orderLength){
            //根据orderId排序
            propertyTopicList.sort((x,y)->x.getOrderId()-y.getOrderId());
        }
        return propertyTopicList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editPropertyTopicByOrderId(PropertyTopic propertyTopic){
        Integer oldOrderId = findOne(propertyTopic.getId()).getOrderId();
        Integer orderId = propertyTopic.getOrderId();
        Integer largestOrderId = findLargestOrderId();
        if (PropertyTopicOrderCode.LARGEST.equals(largestOrderId) &&
                PropertyTopicOrderCode.LARGEST.equals(orderId)){
            bgPropertyTopicDao.save(propertyTopic);
        }else {
            PropertyTopic orderPropertyTopic = findOneBy("orderId", orderId);
            if (orderPropertyTopic!=null&&!oldOrderId.equals(orderId)){
                if (oldOrderId>orderId){
                    orderPropertyTopic.setOrderId(orderPropertyTopic.getOrderId()+1);
                }else {
                    orderPropertyTopic.setOrderId(orderPropertyTopic.getOrderId()-1);
                }
                bgPropertyTopicDao.save(orderPropertyTopic);
            }
            bgPropertyTopicDao.save(propertyTopic);
            List<PropertyTopic> allByOrderId = getPropertyTopics();
            bgPropertyTopicDao.saveAll(allByOrderId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void savePropertyTopicByOrderId(PropertyTopic propertyTopic) {
        Integer orderId = propertyTopic.getOrderId();
        Integer largestOrderId = findLargestOrderId();
        if (PropertyTopicOrderCode.LARGEST.equals(largestOrderId) &&
                PropertyTopicOrderCode.LARGEST.equals(orderId)){
            bgPropertyTopicDao.save(propertyTopic);
        }else {
            PropertyTopic orderPropertyTopic = findOneBy("orderId", orderId);
            if (orderPropertyTopic!=null){
                orderPropertyTopic.setOrderId(orderPropertyTopic.getOrderId()+1);
                bgPropertyTopicDao.save(orderPropertyTopic);
            }
            bgPropertyTopicDao.save(propertyTopic);
            List<PropertyTopic> allByOrderId = getPropertyTopics();
            bgPropertyTopicDao.saveAll(allByOrderId);
        }
    }

    private List<PropertyTopic> getPropertyTopics() {
        List<PropertyTopic> allByOrderId = findAllByOrderId();
        List<PropertyTopic> collect = allByOrderId.stream().filter(x ->
                x.getOrderId() >= PropertyTopicOrderCode.LEAST && x.getOrderId() < PropertyTopicOrderCode.LARGEST)
                .collect(Collectors.toList());
        for (int i = 0; i < collect.size(); i++) {
            collect.get(i).setOrderId(i + 100);
        }
        return collect;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletePropertyTopicByOrderId(String id) {
        bgPropertyTopicDao.deleteById(id);
        List<PropertyTopic> propertyTopics = getPropertyTopics();
        bgPropertyTopicDao.saveAll(propertyTopics);
    }

    @Override
    public Integer findLargestOrderId() {
        PropertyTopic propertyTopic = bgPropertyTopicService.findOneBy("orderId",PropertyTopicOrderCode.LEAST);
        if (propertyTopic==null){
            return PropertyTopicOrderCode.LEAST;
        }
        List<PropertyTopic> propertyTopics = bgPropertyTopicService.findAllByOrderId();
        return propertyTopics.get(propertyTopics.size()-1).getOrderId();
    }
}
