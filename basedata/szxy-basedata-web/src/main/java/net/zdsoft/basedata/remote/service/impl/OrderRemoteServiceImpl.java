package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.Order;
import net.zdsoft.basedata.remote.service.OrderRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.OrderService;
import net.zdsoft.framework.utils.SUtils;

@Service("orderRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class OrderRemoteServiceImpl extends BaseRemoteServiceImpl<Order,String> implements OrderRemoteService {

    @Autowired
    private OrderService orderService;

    @Override
    protected BaseService<Order, String> getBaseService() {
        return orderService;
    }

    @Override
    public String findByServer(Integer serverId, String[] customerIds) {
        return SUtils.s(orderService.findByServer(serverId, customerIds));
    }

    @Override
    public String findByCustomerId(String customerId, Integer customerType, String formattedTime, String formattedTime2) {
        return SUtils.s(orderService.findByCustomerId(customerId, customerType, formattedTime, formattedTime2));
    }

}
