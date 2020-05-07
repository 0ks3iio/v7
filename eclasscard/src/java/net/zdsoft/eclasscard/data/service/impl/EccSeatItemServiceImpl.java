package net.zdsoft.eclasscard.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.dao.EccSeatItemDao;
import net.zdsoft.eclasscard.data.entity.EccSeatItem;
import net.zdsoft.eclasscard.data.service.EccSeatItemService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("eccSeatItemService")
public class EccSeatItemServiceImpl extends BaseServiceImpl<EccSeatItem,String> implements EccSeatItemService {
	
	@Autowired
	private EccSeatItemDao eccSeatItemDao;

	@Override
	protected BaseJpaRepositoryDao<EccSeatItem, String> getJpaDao() {
		return eccSeatItemDao;
	}

	@Override
	protected Class<EccSeatItem> getEntityClass() {
		return EccSeatItem.class;
	}

	@Override
	public List<EccSeatItem> findListBySeatId(String unitId, String seatId) {
		return eccSeatItemDao.findListBySeatId(unitId,seatId);
	}

	@Override
	public void deleteByClassId(String classId) {
		eccSeatItemDao.deleteByClassId(classId);
	}
	@Override
	public void saveAndDelete(String unitId, String seatId, Integer[] rowNums, String[] delStuIds, EccSeatItem[] seatItems) {
		if(ArrayUtils.isNotEmpty(rowNums)){
			eccSeatItemDao.deleteByUnitIdAndSeatIdAndRowNumIn(unitId, seatId, rowNums);
		}
		if(ArrayUtils.isNotEmpty(delStuIds)){
			eccSeatItemDao.deleteByUnitIdAndSeatIdAndStudentIdIn(unitId, seatId, delStuIds);
		}
		saveAll(seatItems);
	}


}
