package net.zdsoft.career.data.service;

import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.career.data.entity.CarTypeResult;


public interface CarTypeResultService extends BaseService<CarTypeResult,String>{

	public CarTypeResult findByResultType(String resultType);

	public Set<String> findAllTypes();

}
