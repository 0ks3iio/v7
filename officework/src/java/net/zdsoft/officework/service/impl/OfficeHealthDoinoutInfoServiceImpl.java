package net.zdsoft.officework.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.officework.dto.OfficeHealthInfoDto;
import net.zdsoft.officework.entity.OfficeHealthDoinoutInfo;
import net.zdsoft.officework.service.OfficeHealthDoinoutInfoService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
/**
 * office_health_doinout_info
 * @author 
 * 
 */
@Service("officeHealthDoinoutInfoService")
public class OfficeHealthDoinoutInfoServiceImpl implements
		OfficeHealthDoinoutInfoService {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void saveBatch(List<OfficeHealthDoinoutInfo> list) {
		if (CollectionUtils.isNotEmpty(list)) {
			
			
			for (int i = 0; i < list.size(); i++) {
				OfficeHealthDoinoutInfo ent  = list.get(i);
				if(StringUtils.isBlank(ent.getId())){
					ent.setId(UuidUtils.generateUuid());
				}
				if(ent.getCreationTime()==null){
					ent.setCreationTime(new Date());
				}
				entityManager.persist(ent);
				if (i % 30 == 0) {
					entityManager.flush();
					entityManager.clear();
				}
			}
			entityManager.flush();
			entityManager.clear();
		}
	}

}