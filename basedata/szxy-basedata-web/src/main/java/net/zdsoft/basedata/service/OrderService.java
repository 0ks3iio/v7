package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.Order;

public interface OrderService extends BaseService<Order, String> {

    public List<Order> findByServer(Integer serverId, String[] customerIds);

    public List<Order> findByCustomerId(String customerId, Integer customerType, String formattedTime,
            String formattedTime2);

}
