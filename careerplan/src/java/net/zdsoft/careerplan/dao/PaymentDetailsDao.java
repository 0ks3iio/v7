package net.zdsoft.careerplan.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.careerplan.entity.PaymentDetails;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface PaymentDetailsDao extends BaseJpaRepositoryDao<PaymentDetails, String>{

	@Query("from PaymentDetails where orderType=?1 and userId=?2 order by creationTime desc")
	public List<PaymentDetails> findByOrderTypeAndUserId(String orderType, String userId);
	@Query("from PaymentDetails where id=?1 ")
	public PaymentDetails findOneById(String id);

}
