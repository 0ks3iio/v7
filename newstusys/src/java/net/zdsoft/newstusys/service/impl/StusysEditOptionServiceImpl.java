package net.zdsoft.newstusys.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newstusys.constants.BaseStudentConstants;
import net.zdsoft.newstusys.dao.StusysEditOptionDao;
import net.zdsoft.newstusys.entity.StusysColsDisplay;
import net.zdsoft.newstusys.entity.StusysEditOption;
import net.zdsoft.newstusys.service.StusysColsDisplayService;
import net.zdsoft.newstusys.service.StusysEditOptionService;

/**
 * 
 * @author weixh
 * 2019年9月4日	
 */
@Service("stusysEditOptionService")
public class StusysEditOptionServiceImpl extends BaseServiceImpl<StusysEditOption, String>
		implements StusysEditOptionService {
	@Autowired
	private StusysEditOptionDao stusysEditOptionDao;
	@Autowired
    private StusysColsDisplayService stusysColsDisplayService;
	
	protected BaseJpaRepositoryDao<StusysEditOption, String> getJpaDao() {
		return stusysEditOptionDao;
	}

	protected Class<StusysEditOption> getEntityClass() {
		return StusysEditOption.class;
	}

	@Override
	public StusysEditOption findByUnitId(String unitId) {
		return stusysEditOptionDao.findByUnitId(unitId);
	}

	@Override
	public boolean isEditOpen(String unitId) {
		StusysEditOption op = findByUnitId(unitId);
		if(op != null && op.getIsOpen() == 1) {
			return true;
		}
		return false;
	}
	
	public void saveOption(StusysEditOption option) {
		if(StringUtils.isEmpty(option.getId())) {
			option.setId(UuidUtils.generateUuid());
		}
		option.setModifyTime(new Date());
		this.save(option);
		if(option.getIsOpen() == 1) {
			stusysColsDisplayService.deleteByUnitIdType(option.getUnitId(), BaseStudentConstants.STUSYS_COLS_TYPE_STUDENTEDIT);
			String dis = option.getDisplayCols();
			if(StringUtils.isEmpty(dis)) {
				return;
			}
			String[] cols = (String[]) ArrayUtils.removeElement(dis.replaceAll(" ", "").split(","), "");
			if(ArrayUtils.isEmpty(cols)) {
				return;
			}
			List<StusysColsDisplay> saves = new ArrayList<>();
			int i = 0;
			for(String code : cols) {
				StusysColsDisplay cd = new StusysColsDisplay();
				cd.setColsCode(code);
				cd.setColsConstraint(0);
				cd.setColsName("学生编辑字段");
				cd.setColsOrder(i++);
				cd.setColsType(BaseStudentConstants.STUSYS_COLS_TYPE_STUDENTEDIT);
				cd.setColsUse(1);
				cd.setUnitId(option.getUnitId());
				cd.setId(UuidUtils.generateUuid());
				saves.add(cd);
			}
			stusysColsDisplayService.saveAll(saves.toArray(new StusysColsDisplay[0]));
		}
	}

}
