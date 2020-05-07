package net.zdsoft.career.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.career.data.entity.CarResourceCenter;
import net.zdsoft.framework.entity.Pagination;


public interface CarResourceCenterService extends BaseService<CarResourceCenter,String>{

	public List<CarResourceCenter> findByResourceType(Integer resourceType,Integer type,String title,boolean showPicture,Pagination page);

	public CarResourceCenter findOneById(String resourceId, boolean showPicture);

	public void deleteByIds(String resourceIds);

	public void saveResource(CarResourceCenter carResource,String pictureArray, String videoArray);

}
