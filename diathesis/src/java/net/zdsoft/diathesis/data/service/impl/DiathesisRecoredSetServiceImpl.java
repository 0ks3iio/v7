package net.zdsoft.diathesis.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.diathesis.data.dao.DiathesisRecordSetDao;
import net.zdsoft.diathesis.data.dto.DiathesisProjectDto;
import net.zdsoft.diathesis.data.entity.DiathesisOption;
import net.zdsoft.diathesis.data.entity.DiathesisRecordSet;
import net.zdsoft.diathesis.data.service.DiathesisCustomAuthorService;
import net.zdsoft.diathesis.data.service.DiathesisOptionService;
import net.zdsoft.diathesis.data.service.DiathesisRecordSetService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: panlf
 * @Date: 2019/8/8 17:50
 */
@Service("diathesisRecordSetService")
public class DiathesisRecoredSetServiceImpl extends BaseServiceImpl<DiathesisRecordSet, String> implements DiathesisRecordSetService {

    @Autowired
    private DiathesisOptionService diathesisOptionService;
    @Autowired
    private DiathesisRecordSetDao diathesisRecordSetDao;
    @Autowired
    private DiathesisCustomAuthorService diathesisCustomAuthorService;



    @Override
    public void saveRecordSet(List<DiathesisProjectDto> projectlist, String unitId, String realName) {

        List<String> projectIds = EntityUtils.getList(projectlist, x -> x.getId());
        List<DiathesisRecordSet> oldSet = diathesisRecordSetDao.findByIdIn(projectIds);
        Map<String, DiathesisRecordSet> oldSetMap = EntityUtils.getMap(oldSet, x -> x.getId(), x -> x);
        List<String> struIds = EntityUtils.getList(projectlist, x -> x.getScoreStructureId());
        Map<String,DiathesisOption> optionMap = diathesisOptionService.findListByStructureIdIn(struIds.toArray(new String[0])).stream().collect(Collectors.toMap(x->x.getId(),x->x));

        List<DiathesisRecordSet> saveList=new ArrayList<>();
        List<DiathesisOption> saveOpList=new ArrayList<>();
        for (DiathesisProjectDto dto : projectlist) {
            DiathesisRecordSet old = oldSetMap.get(dto.getId());

            if(old==null){
                old=new DiathesisRecordSet();
                old.setOperator(realName);
                old.setUnitId(unitId);
                old.setId(dto.getId());
            }
            old.setModifyTime(new Date());
            old.setAllMaxScore(dto.getAllMax());
            old.setScore(dto.getScore());
            old.setSemesterMaxScore(dto.getSemesterMax());
            old.setScoreType(dto.getScoreType());
            old.setScoreStructureId(dto.getScoreStructureId());

            String structureId = dto.getScoreStructureId();
            if(StringUtils.isNotBlank(structureId)){
                List<DiathesisOption> optionList = dto.getOptionList();
                if(CollectionUtils.isNotEmpty(optionList)){
                    for (DiathesisOption option : optionList) {
                        DiathesisOption oldOption = optionMap.get(option.getId());
                        oldOption.setScore(option.getScore());
                        saveOpList.add(oldOption);
                    }
                }
            }
            saveList.add(old);
        }
        if(CollectionUtils.isNotEmpty(saveOpList))
            diathesisOptionService.saveAll(saveOpList.toArray(new DiathesisOption[0]));
        if(CollectionUtils.isNotEmpty(saveList))
            diathesisRecordSetDao.saveAll(saveList);
    }


    @Override
    public void saveOne(DiathesisRecordSet recordSet) {
        diathesisRecordSetDao.save(recordSet);
    }

    @Override
    public List<DiathesisRecordSet> findByUnitId(String unitId) {

        return diathesisRecordSetDao.findByUnitId(unitId);
    }

    @Override
    public void saveAllEntity(List<DiathesisRecordSet> recordSetList) {
        if(CollectionUtils.isNotEmpty(recordSetList))
            diathesisRecordSetDao.saveAll(recordSetList);
    }

    @Override
    public void deleteByIds(String[] projectIds) {
        diathesisRecordSetDao.deleteByIds(projectIds);
    }

	@Override
	protected BaseJpaRepositoryDao<DiathesisRecordSet, String> getJpaDao() {
		return diathesisRecordSetDao;
	}

	@Override
	protected Class<DiathesisRecordSet> getEntityClass() {
		return DiathesisRecordSet.class;
	}
}
