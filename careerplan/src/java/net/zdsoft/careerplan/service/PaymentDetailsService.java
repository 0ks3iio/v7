package net.zdsoft.careerplan.service;

import net.zdsoft.careerplan.entity.PaymentDetails;
import net.zdsoft.basedata.service.BaseService;

public interface PaymentDetailsService extends BaseService<PaymentDetails, String>{
	/**
	 * 验证业务是否已支付成功
	 * @param orderType
	 * @param userId
	 * @return
	 */
	public boolean checkByOrderTypeAndUserIdWithMaster(String orderType,String userId);
	
	public PaymentDetails saveOrFindOne(String orderType, String userId,boolean creatEnt);

	public PaymentDetails findByOrderTypeAndOrderIdAndUserIdWithMaster(String orderType, String orderId, String userId);
	
	public PaymentDetails findByIdWithMaster(String id);
}
