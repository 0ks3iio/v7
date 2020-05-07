package net.zdsoft.basedata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.OrderDao;
import net.zdsoft.basedata.entity.Order;
import net.zdsoft.basedata.service.OrderService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service("orderService")
public class OrderServiceImpl extends BaseServiceImpl<Order, String> implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Override
    protected BaseJpaRepositoryDao<Order, String> getJpaDao() {
        return orderDao;
    }

    @Override
    protected Class<Order> getEntityClass() {
        return Order.class;
    }

    @Override
    public List<Order> findByServer(Integer serverId, String[] customerIds) {
        return orderDao.findByServer(serverId, customerIds);
    }

    @Override
    public List<Order> findByCustomerId(String customerId, Integer state, String formattedTime, String formattedTime2) {
        return orderDao.findByCustomerId(customerId, state, formattedTime, formattedTime2);
    }

}
