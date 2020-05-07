package net.zdsoft.diathesis.data.service.impl;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.dao.DiathesisScoreInfoDao;
import net.zdsoft.diathesis.data.entity.DiathesisCustomAuthor;
import net.zdsoft.diathesis.data.entity.DiathesisScoreInfo;
import net.zdsoft.diathesis.data.service.DiathesisCustomAuthorService;
import net.zdsoft.diathesis.data.service.DiathesisProjectService;
import net.zdsoft.diathesis.data.service.DiathesisScoreInfoService;
import net.zdsoft.diathesis.data.service.DiathesisUnitService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("diathesisScoreInfoService")
public class DiathesisScoreInfoServiceImpl
        extends BaseServiceImpl<DiathesisScoreInfo, String>
        implements DiathesisScoreInfoService {
    @Autowired
    private DiathesisUnitService diathesisUnitService;
    @Autowired
    private DiathesisScoreInfoDao diathesisScoreInfoDao;
    @Autowired
    private DiathesisProjectService diathesisProjectService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private DiathesisCustomAuthorService diathesisCustomAuthorService;

    @Override
    public List<DiathesisScoreInfo> findByTypeAndScoreTypeId(String type, String scoreTypeId) {
        return diathesisScoreInfoDao.findByTypeAndScoreTypeId(type, scoreTypeId);
    }

    @Override
    public List<DiathesisScoreInfo> findByStudentIdAndScoreTypeIdIn(String studentId, String[] scoreTypeIds) {
        return diathesisScoreInfoDao.findByStuIdAndScoreTypeIdIn(studentId, scoreTypeIds);
    }

    @Override
    public void deleteByScoreTypeId(String scoreTypeId) {
        diathesisScoreInfoDao.deleteByScoreTypeId(scoreTypeId);
    }

    @Override
    protected BaseJpaRepositoryDao<DiathesisScoreInfo, String> getJpaDao() {
        return diathesisScoreInfoDao;
    }

    @Override
    protected Class<DiathesisScoreInfo> getEntityClass() {
        return DiathesisScoreInfo.class;
    }

	@Override
	public List<DiathesisScoreInfo> findByUnitIdAndStudentId(String unitId, String studentId) {
		return diathesisScoreInfoDao.findByUnitIdAndStudentId(unitId,studentId);
	}

	@Override
	public List<DiathesisScoreInfo> findByScoreTypeIdIn(String[] scoreTypeIds) {
		 return diathesisScoreInfoDao.findByScoreTypeIdIn(scoreTypeIds);
	}

	@Override
	public void deleteByScoreTypeIdIn(String[] scoreTypeIds) {
		diathesisScoreInfoDao.deleteByScoreTypeIdIn(scoreTypeIds);
	}

    @Override
    public boolean isUsingByUnitId(String unitId) {
        Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
        Integer count;
        if(Unit.UNIT_CLASS_SCHOOL==unit.getUnitClass()){
            count=diathesisScoreInfoDao.countByUnitIdAndType(unitId, DiathesisConstant.INPUT_TYPE_3);
        }else{
            //如果是教育局要判断所有调用这个学校的有没有设置过
            List<Unit> unitList = diathesisUnitService.findAllChildUnit(unitId);
            List<String> unitIds = EntityUtils.getList(unitList, x -> x.getId());
            List<DiathesisCustomAuthor> authorList = diathesisCustomAuthorService.findAuthorListByUnitIdInAndTypeIn(unitIds, DiathesisConstant.AUTHOR_TREE_MAP.get(DiathesisConstant.AUTHOR_GOBAL_SET).toArray(new Integer[0]));
            unitIds.removeAll(EntityUtils.getList(authorList, x -> x.getUnitId()));
            count=diathesisScoreInfoDao.countByUnitIdInAndType(unitIds, DiathesisConstant.INPUT_TYPE_3);
        }
        return count==null || count==0 ?false:true;
    }

    @Override
    public List<DiathesisScoreInfo> findByUnitIdAndProjectIdIn(String unitId, List<String> projectIdList) {
        return diathesisScoreInfoDao.findByUnitIdAndProjectIdIn(unitId,projectIdList);
    }

    @Override
    public List<DiathesisScoreInfo> findByScoreTypeIdAndStuId(String scoreTypeId, String stuId) {
        return diathesisScoreInfoDao.findByScoreTypeIdAndStuId(scoreTypeId,stuId);
    }

    @Override
    public List<DiathesisScoreInfo> findByUnitIdAndProjectIdInAndScoreTypeIdIn(String unitId, List<String> projectIds, List<String> typeList) {
        return diathesisScoreInfoDao.findByUnitIdAndProjectIdInAndScoreTypeIdIn(unitId,projectIds,typeList);
    }

    @Override
    public List<DiathesisScoreInfo> findByUnitIdAndProjectIdInAndScoreTypeIdInAndStudentId(String unitId, List<String> projectIds, List<String> typeList, String studentId) {
        return diathesisScoreInfoDao.findByUnitIdAndProjectIdInAndScoreTypeIdInAndStudentId(unitId,projectIds,typeList,studentId);
    }

    @Override
    public List<DiathesisScoreInfo> findByScoreTypeIdAndStuIdAndProjectIdIn(String id, String studentId, List<String> topIds) {
        return diathesisScoreInfoDao.findByScoreTypeIdAndStuIdAndProjectIdIn(id,studentId,topIds);
    }

    @Override
    public List<DiathesisScoreInfo> findByUnitIdAndProjectIdInAndScoreTypeIdInAndEvaluateId(String unitId, List<String> projectIds, List<String> typeList, String evaluateStuId) {
        return diathesisScoreInfoDao.findByUnitIdAndProjectIdInAndScoreTypeIdInAndEvaluateId(unitId,projectIds,typeList,evaluateStuId);
    }


}
