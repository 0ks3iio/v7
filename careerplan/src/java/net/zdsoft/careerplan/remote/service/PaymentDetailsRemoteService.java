package net.zdsoft.careerplan.remote.service;

public interface PaymentDetailsRemoteService {
	
	public boolean checkByOrderTypeAndUserIdWithMaster(String orderType, String userId);

}

