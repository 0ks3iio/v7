package net.zdsoft.diathesis.data.service.impl;

import net.zdsoft.basedata.remote.service.FilePathRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.diathesis.data.dao.DiathesisRecordInfoDao;
import net.zdsoft.diathesis.data.entity.DiathesisRecordInfo;
import net.zdsoft.diathesis.data.service.DiathesisRecordInfoService;
import net.zdsoft.diathesis.data.service.DiathesisStructureService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 
 * @author niuchao
 * @since  2019年4月1日
 */
@Service("diathesisRecordInfoService")
public class DiathesisRecordInfoServiceImpl extends BaseServiceImpl<DiathesisRecordInfo, String> implements DiathesisRecordInfoService {
	
    @Autowired
    private DiathesisRecordInfoDao diathesisRecordInfoDao;
    @Autowired
    private DiathesisStructureService diathesisStructureService;
    @Autowired
    private FilePathRemoteService filePathRemoteService;

	@Override
	protected BaseJpaRepositoryDao<DiathesisRecordInfo, String> getJpaDao() {
		return diathesisRecordInfoDao;
	}

	@Override
	protected Class<DiathesisRecordInfo> getEntityClass() {
		return DiathesisRecordInfo.class;
	}

	@Override
	public List<DiathesisRecordInfo> findListByUnitIdAndRecordIds(String unitId, String[] recordIds) {
		return diathesisRecordInfoDao.findByUnitIdAndRecordIdIn(unitId, recordIds);
	}

	@Override
	public void deleteByRecordIds(String unitId, String[] recordIds) {
//		List<DiathesisRecordInfo> recordInfoList = findListByUnitIdAndRecordIds(unitId, recordIds);
//		List<DiathesisStructure> structureList = diathesisStructureService.findListByIdIn(EntityUtils.getSet(recordInfoList, DiathesisRecordInfo::getScoreStructureId).toArray(new String[0]));
//		structureList = structureList.stream().filter(e->DiathesisConstant.DATA_TYPE_4.equals(e.getDataType())).collect(Collectors.toList());
//		if(CollectionUtils.isNotEmpty(structureList)){
//			List<String>  structureIds= EntityUtils.getList(structureList, DiathesisStructure::getId);
//			String fileSystemPath = filePathRemoteService.getFilePath();// 文件系统地址
//			for (DiathesisRecordInfo recordInfo : recordInfoList) {
//				if(structureIds.contains(recordInfo.getScoreStructureId())){
//					File oldPic = new File(fileSystemPath + File.separator + recordInfo.getContentTxt());
//					FileUtils.deleteQuietly(oldPic);// 删除附件
//				}
//			}
//		}
		diathesisRecordInfoDao.deleteByUnitIdAndRecordIdIn(unitId, recordIds);
	}

	@Override
	public void deleteAllByRecordIds(List<String> recordIds) {
		if(CollectionUtils.isEmpty(recordIds))return;
		diathesisRecordInfoDao.deleteByRecordIds(recordIds);
	}

}
