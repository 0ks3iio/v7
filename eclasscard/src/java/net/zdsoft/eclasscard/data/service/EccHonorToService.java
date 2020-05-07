package net.zdsoft.eclasscard.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccHonorTo;

public interface EccHonorToService extends BaseService<EccHonorTo,String>{

	public List<EccHonorTo> findByObjectIdIn(String[] objectIds);

	public List<EccHonorTo> findByHonorIdIn(String[] honorIds);

	public void deleteByHonorId(String honorId);

}
