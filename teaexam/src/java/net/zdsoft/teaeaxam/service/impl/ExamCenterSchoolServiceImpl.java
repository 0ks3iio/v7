package net.zdsoft.teaeaxam.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.teaeaxam.constant.TeaexamConstant;
import net.zdsoft.teaeaxam.dao.ExamCenterSchoolDao;
import net.zdsoft.teaeaxam.dao.ExamCenterSchoolItemDao;
import net.zdsoft.teaeaxam.entity.ExamCenterSchool;
import net.zdsoft.teaeaxam.entity.ExamCenterSchoolItem;
import net.zdsoft.teaeaxam.service.ExamCenterSchoolService;

/**
 * 
 * 
 * @author weixh
 * 2019年7月19日
 */
@Service("examCenterSchoolService")
public class ExamCenterSchoolServiceImpl extends BaseServiceImpl<ExamCenterSchool, String>
		implements ExamCenterSchoolService {
	@Autowired
	private ExamCenterSchoolDao examCenterSchoolDao;
	@Autowired
	private ExamCenterSchoolItemDao examCenterSchoolItemDao;
	
	@Override
	protected BaseJpaRepositoryDao<ExamCenterSchool, String> getJpaDao() {
		return examCenterSchoolDao;
	}
	
	public List<ExamCenterSchool> findCenterSchool(){
		List<ExamCenterSchool> cenSchs = examCenterSchoolDao.findSchoolByTypes(new String[] {TeaexamConstant.TYPE_CENTER_SCHOOL, TeaexamConstant.TYPE_NORMAL_SCHOOL+""});
		List<String> cenSchIds = EntityUtils.getList(cenSchs, ExamCenterSchool::getId);
		List<ExamCenterSchoolItem> items = examCenterSchoolItemDao.findItemByCenterSchIds(cenSchIds.toArray(new String[0]));
		Map<String, List<ExamCenterSchoolItem>> im = EntityUtils.getListMap(items, ExamCenterSchoolItem::getCenterSchoolId, Function.identity());
		for(ExamCenterSchool cs : cenSchs) {
			if(StringUtils.equals(cs.getType(), TeaexamConstant.TYPE_CENTER_SCHOOL)) {
				List<ExamCenterSchoolItem> schs = im.get(cs.getId());
				if(CollectionUtils.isNotEmpty(schs)) {
					cs.setUnitIds(EntityUtils.getSet(schs, ExamCenterSchoolItem::getUnitId));
				}
			}
		}
		return cenSchs;
	}
	
	public ExamCenterSchool findCenterSchoolById(String id){
		ExamCenterSchool cs = this.findOne(id);
		if(cs != null && StringUtils.equals(cs.getType(), TeaexamConstant.TYPE_CENTER_SCHOOL)) {
			List<ExamCenterSchoolItem> items = examCenterSchoolItemDao.findItemByCenterSchIds(new String[] {id});
			if(CollectionUtils.isNotEmpty(items)) {
				cs.setUnitIds(EntityUtils.getSet(items, ExamCenterSchoolItem::getUnitId));
			}
		}
		return cs;
	}

	@Override
	protected Class<ExamCenterSchool> getEntityClass() {
		return ExamCenterSchool.class;
	}

	

}
