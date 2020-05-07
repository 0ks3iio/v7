package net.zdsoft.careerplan.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.careerplan.remote.service.PaymentDetailsRemoteService;
import net.zdsoft.careerplan.service.PaymentDetailsService;


@Service("paymentDetailsRemoteService")
public class PaymentDetailsRemoteServiceImpl implements PaymentDetailsRemoteService{

	@Autowired
	private PaymentDetailsService paymentDetailsService;
	@Override
	public boolean checkByOrderTypeAndUserIdWithMaster(String orderType, String userId) {
		return paymentDetailsService.checkByOrderTypeAndUserIdWithMaster(orderType, userId);
	}

}
