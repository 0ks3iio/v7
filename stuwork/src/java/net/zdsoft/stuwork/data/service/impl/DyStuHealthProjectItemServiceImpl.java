package net.zdsoft.stuwork.data.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.dao.DyStuHealthProjectItemDao;
import net.zdsoft.stuwork.data.entity.DyStuHealthItem;
import net.zdsoft.stuwork.data.entity.DyStuHealthProjectItem;
import net.zdsoft.stuwork.data.entity.DyStuHealthResult;
import net.zdsoft.stuwork.data.service.DyStuHealthItemService;
import net.zdsoft.stuwork.data.service.DyStuHealthProjectItemService;
import net.zdsoft.stuwork.data.service.DyStuHealthResultService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dyStuHealthProjectItemService")
public class DyStuHealthProjectItemServiceImpl extends BaseServiceImpl<DyStuHealthProjectItem, String> implements DyStuHealthProjectItemService{
	
	@Autowired
	private DyStuHealthProjectItemDao dyStuHealthProjectItemDao;
	@Autowired
	private DyStuHealthResultService dyStuHealthResultService;
	@Autowired
	private DyStuHealthItemService dyStuHealthItemService;
	
	@Override
	public String saveProject(String unitId,String[] itemIds,String acadyear,String semester){
		List<DyStuHealthResult> resultList=dyStuHealthResultService.findByUidAndSe(unitId, acadyear, semester);
		Set<String> itemIdset=new HashSet<String>();
		CollectionUtils.addAll(itemIdset, itemIds);
		if(CollectionUtils.isNotEmpty(resultList)){
			for(DyStuHealthResult result:resultList){
				if(!itemIdset.contains(result.getItemId())){
					return result.getItemName()+"指标已存在对应的体检结果，无法去除";
				}
			}
		}
		//List<DyStuHealthProjectItem> projectList=DyStuHealthProjectItemDao.findBySemester(unitId, acadyear, semester);
		List<DyStuHealthProjectItem> insertList=new ArrayList<DyStuHealthProjectItem>();
		List<DyStuHealthItem> itemList=dyStuHealthItemService.findListByIds(itemIds);
		Map<String,DyStuHealthItem> itemMap=EntityUtils.getMap(itemList, "id");
		dyStuHealthProjectItemDao.deleteBySemester(unitId, acadyear, semester);//先删除该学年学期的数据
		//添加所有已选的数据
		if(CollectionUtils.isNotEmpty(itemIdset)){
			for(String itemId:itemIdset){
				DyStuHealthProjectItem project=new DyStuHealthProjectItem();
				project.setId(UuidUtils.generateUuid());
				project.setUnitId(unitId);
				project.setAcadyear(acadyear);
				project.setSemester(semester);
				project.setItemId(itemId);
				DyStuHealthItem item=itemMap.get(itemId);
				if(item!=null){
					project.setOrderId(item.getOrderId());
					project.setItemName(item.getItemName());
					project.setItemUnit(item.getItemUnit());
				}
				insertList.add(project);
			}
			saveAll(insertList.toArray(new DyStuHealthProjectItem[0]));
		}
		return "success";
	}
	@Override
	public List<DyStuHealthProjectItem> findByItemId(String unitId,String itemId){
		return dyStuHealthProjectItemDao.findByItemId(unitId, itemId);
	}
	@Override
	public List<DyStuHealthProjectItem> findBySemester(String unitId,String acadyear,String semester){
		return dyStuHealthProjectItemDao.findBySemester(unitId, acadyear,semester);
	}
	
	@Override
	protected BaseJpaRepositoryDao<DyStuHealthProjectItem, String> getJpaDao() {
		return dyStuHealthProjectItemDao;
	}

	@Override
	protected Class<DyStuHealthProjectItem> getEntityClass() {
		return DyStuHealthProjectItem.class;
	}

	@Override
	public List<DyStuHealthProjectItem> findByUIdAndSemester(String unitId, String acadyear, String semester) {
		// TODO Auto-generated method stub
		return dyStuHealthProjectItemDao.findByUIdAndSemester(unitId,acadyear,semester);
	}
	
	

}
