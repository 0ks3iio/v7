package net.zdsoft.newgkelective.data.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkArrayItemDao;
import net.zdsoft.newgkelective.data.dao.NewGkplaceArrangeDao;
import net.zdsoft.newgkelective.data.entity.NewGkArrayItem;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkplaceArrange;
import net.zdsoft.newgkelective.data.service.NewGkPlaceItemService;
import net.zdsoft.newgkelective.data.service.NewGkplaceArrangeService;
@Service("newGkplaceArrangeService")
public class NewGkplaceArrangeServiceImpl extends BaseServiceImpl<NewGkplaceArrange, String> implements NewGkplaceArrangeService{

	@Autowired
	private NewGkplaceArrangeDao newGkplaceArrangeDao;
	@Autowired
	private NewGkArrayItemDao newGkArrayItemDao;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private NewGkPlaceItemService newGkPlaceItemService;
	@Override
	protected BaseJpaRepositoryDao<NewGkplaceArrange, String> getJpaDao() {
		return newGkplaceArrangeDao;
	}

	@Override
	protected Class<NewGkplaceArrange> getEntityClass() {
		return NewGkplaceArrange.class;
	}

	@Override
	public void savePlaceArrang(String arrayItemId,
			Boolean isAddArrayItem, NewGkDivide divide, List<NewGkplaceArrange> newGkplaceArrangeList) {
		//获取学年学期信息
		String semesterJson = semesterRemoteService.getCurrentSemester(2, divide.getUnitId());
		Semester semester = SUtils.dc(semesterJson, Semester.class);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(divide.getGradeId()), Grade.class);	
		String itemNamePrifix = semester.getAcadyear()+"学年"+grade.getGradeName()+"第"+semester.getSemester()+"学期教室方案";
		
		List<NewGkArrayItem> newGkArrayItemList = newGkArrayItemDao.findByDivideIdAndType(divide.getId(), new String[]{NewGkElectiveConstant.ARRANGE_TYPE_01});
		Integer maxTimes = newGkArrayItemList.stream().map(e->e.getTimes()).max(Integer::compare).orElse(0);

		if(isAddArrayItem){
			NewGkArrayItem newGkArrayItem = new NewGkArrayItem();
			newGkArrayItem.setId(arrayItemId);
			newGkArrayItem.setDivideId(divide.getId());
			newGkArrayItem.setDivideType(NewGkElectiveConstant.ARRANGE_TYPE_01);
			newGkArrayItem.setTimes(maxTimes + 1);
			newGkArrayItem.setCreationTime(new Date());
			newGkArrayItem.setModifyTime(new Date());
			newGkArrayItem.setIsDeleted(0);
			String itemName = "";
			for(int i=1;i<50;i++) {
				itemName = itemNamePrifix + (maxTimes + i);
				if(checkItemName(divide.getId(), NewGkElectiveConstant.ARRANGE_TYPE_01, itemName, null)) {
					break;
				}
			}
			newGkArrayItem.setItemName(itemName);
			newGkArrayItemDao.save(newGkArrayItem);
		} else{
			newGkplaceArrangeDao.deleteByArrayItemId(arrayItemId);
		}
		newGkplaceArrangeDao.saveAll(newGkplaceArrangeList);
	}
	
	/**
	 * 检查名称是否重复
	 * @param divideId
	 * @param divideType
	 * @param itemName
	 * @param itemId
	 * @return
	 */
	private boolean checkItemName(String divideId, String divideType, String itemName, String itemId) {
		List<String> aids = newGkArrayItemDao.findIdsByDivideIdName(divideId, divideType, itemName);
		if(CollectionUtils.isNotEmpty(aids) && StringUtils.isNotEmpty(itemId)) {
			aids.remove(itemId);
		}
		if(CollectionUtils.isNotEmpty(aids)) {
			return false;
		}
		return true;
	}

	@Override
	public List<NewGkplaceArrange> findByArrayItemId(String arrayItemId) {
		return newGkplaceArrangeDao.findByArrayItemId(arrayItemId);
	}
	
	@Override
	public List<NewGkplaceArrange> findByArrayItemIdWithMaster(String arrayItemId) {
		return newGkplaceArrangeDao.findByArrayItemId(arrayItemId);
	}

	@Override
	public void deleteByItemIdAndPlaceId(String arrayItemId, String placeId) {
		newGkplaceArrangeDao.deleteByItemIdAndPlaceId(arrayItemId, placeId);
	}

	@Override
	public List<NewGkplaceArrange> findByArrayItemIds(String[] arrayItemIds) {
		return newGkplaceArrangeDao.findByArrayItemIds(arrayItemIds);
	}

	@Override
	public void updateBasicPlaceSet(String gradeId, NewGkplaceArrange[] all) {
		newGkplaceArrangeDao.deleteByArrayItemId(gradeId);
		saveAll(all);
	}

	@Override
	public void deleteByItemId(String arrayItemId) {
		newGkplaceArrangeDao.deleteByArrayItemId(arrayItemId);
	}

	@Override
	public void savePlaceArrangeModify(String arrayItemId, NewGkplaceArrange[] all, String[] placeIdArr) {
		if(StringUtils.isBlank(arrayItemId)) {
			return;
		}

		this.deleteByItemId(arrayItemId);
		if(all != null && all.length>0) {
			this.saveAll(all);
		}
		
		//删除不在场地范围内的 包括行政班 教学班
		newGkPlaceItemService.deleteNotInPlaceIds(arrayItemId,placeIdArr);
	}
}
