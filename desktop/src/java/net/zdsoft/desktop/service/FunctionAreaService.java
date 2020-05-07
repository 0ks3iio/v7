package net.zdsoft.desktop.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.desktop.entity.FunctionArea;

public interface FunctionAreaService extends BaseService<FunctionArea, String>{

	void save(FunctionArea fa);

	/**
	 * 
	 */
	List<FunctionArea> findByTypes(List<String> typeIds);

	/**
	 * @param type
	 */
	List<FunctionArea> findByType(String type);

	/**
	 * @param unitClass
	 * @param ownerType
	 */
	List<FunctionArea> findByUnitClassAndType(Integer unitClass, Integer ownerType);
}
