package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmStatParmDao;
import net.zdsoft.exammanage.data.entity.EmSpaceItem;
import net.zdsoft.exammanage.data.entity.EmStatObject;
import net.zdsoft.exammanage.data.entity.EmStatParm;
import net.zdsoft.exammanage.data.service.EmSpaceItemService;
import net.zdsoft.exammanage.data.service.EmStatObjectService;
import net.zdsoft.exammanage.data.service.EmStatParmService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("emStatParmService")
public class EmStatParmServiceImpl extends BaseServiceImpl<EmStatParm, String> implements EmStatParmService {

    @Autowired
    private EmStatParmDao emStatParmDao;
    @Autowired
    private EmStatObjectService emStatObjectService;
    @Autowired
    private EmSpaceItemService emSpaceItemService;

    @Override
    protected BaseJpaRepositoryDao<EmStatParm, String> getJpaDao() {
        return emStatParmDao;
    }

    @Override
    protected Class<EmStatParm> getEntityClass() {
        return EmStatParm.class;
    }

    @Override
    public void deleteBySubjectId(String unitId, String examId, String subjectId) {
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        if (emStatObject != null) {
            emStatParmDao.deleteByObjSubjectId(emStatObject.getId(), subjectId);
        }
    }

    @Override
    public EmStatParm findBySubjectId(String unitId, String examId,
                                      String subjectId) {

        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        if (emStatObject == null) {
            return null;
        }
        EmStatParm emStatParm = emStatParmDao.findByObjSubjectId(emStatObject.getId(), subjectId);
        if (emStatParm == null) {
            return null;
        }
        makeSpaceList(emStatParm);
        return emStatParm;
    }

    private void makeSpaceList(EmStatParm emStatParm) {
        Map<String, List<EmSpaceItem>> itemMap = emSpaceItemService.findMapByStatParmId(emStatParm.getId());

        if (itemMap.containsKey(EmSpaceItem.PARM_SPACE)) {
            emStatParm.setEmSpaceItemList1(itemMap.get(EmSpaceItem.PARM_SPACE));
        }
        if (itemMap.containsKey(EmSpaceItem.PARM_PERCENT_F)) {
            emStatParm.setEmSpaceItemList2(itemMap.get(EmSpaceItem.PARM_PERCENT_F));
        }
        if (itemMap.containsKey(EmSpaceItem.PARM_PERCENT_B)) {
            emStatParm.setEmSpaceItemList3(itemMap.get(EmSpaceItem.PARM_PERCENT_B));
        }
        if (itemMap.containsKey(EmSpaceItem.PARM_CLASS_RANK)) {
            emStatParm.setEmSpaceItemList9(itemMap.get(EmSpaceItem.PARM_CLASS_RANK));
        }
    }

    @Override
    public void saveParm(String unitId, EmStatParm emStatParm, boolean isSingle) {
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, emStatParm.getExamId());
        List<EmSpaceItem> insertItem = new ArrayList<EmSpaceItem>();
        EmSpaceItem spaceItem = null;
        //修改
        if (emStatObject != null) {
            //防止改变
            emStatObject.setIsStat("0");//未统计
        } else {
            emStatObject = new EmStatObject();
            emStatObject.setExamId(emStatParm.getExamId());
            emStatObject.setIsStat("0");//未统计
            if (isSingle) {
                emStatObject.setStatType(EmStatObject.STAT_NOTBELONG);
            } else {
                emStatObject.setStatType(EmStatObject.STAT_BELONG);
            }


            emStatObject.setUnitId(unitId);
            emStatObject.setId(UuidUtils.generateUuid());
        }
        emStatParm.setStatObjectId(emStatObject.getId());
        if (StringUtils.isBlank(emStatParm.getId())) {
            //新增
            emStatParm.setId(UuidUtils.generateUuid());
        }
        if (StringUtils.isBlank(emStatParm.getIsCheat())) {
            emStatParm.setIsCheat("0");
        }
        if (StringUtils.isBlank(emStatParm.getIsMiss())) {
            emStatParm.setIsMiss("0");
        }
        if (StringUtils.isBlank(emStatParm.getIsOnlystat())) {
            emStatParm.setIsOnlystat("0");
        }
        if (StringUtils.isBlank(emStatParm.getIsZero())) {
            emStatParm.setIsZero("0");
        }
        if ("1".equals(emStatParm.getNeedRankStat())) {
            if (CollectionUtils.isNotEmpty(emStatParm.getEmSpaceItemList9())) {
                for (EmSpaceItem item : emStatParm.getEmSpaceItemList9()) {
                    if (item == null) {
                        continue;
                    }
                    spaceItem = new EmSpaceItem();
                    spaceItem.setId(UuidUtils.generateUuid());
                    spaceItem.setLowScore(item.getLowScore());
                    spaceItem.setUpScore(item.getUpScore());
                    spaceItem.setParmType(EmSpaceItem.PARM_CLASS_RANK);
                    spaceItem.setStatParmId(emStatParm.getId());
                    spaceItem.setName(item.getUpScore().intValue() + "-" + item.getLowScore().intValue());
                    insertItem.add(spaceItem);
                }
            }
        }
        if (StringUtils.isNotBlank(emStatParm.getStatSpaceType())) {
            if (EmStatParm.SPACE_AUTO.equals(emStatParm.getStatSpaceType())) {
                if (CollectionUtils.isNotEmpty(emStatParm.getEmSpaceItemList1())) {
                    for (EmSpaceItem item : emStatParm.getEmSpaceItemList1()) {
                        if (item == null) {
                            continue;
                        }
                        spaceItem = new EmSpaceItem();
                        spaceItem.setId(UuidUtils.generateUuid());
                        spaceItem.setLowScore(item.getLowScore());
                        spaceItem.setUpScore(item.getUpScore());
                        spaceItem.setParmType(EmSpaceItem.PARM_SPACE);
                        spaceItem.setStatParmId(emStatParm.getId());
                        spaceItem.setName(item.getLowScore() + "-" + item.getUpScore());
                        insertItem.add(spaceItem);
                    }
                }
                emStatParm.setSpaceScore(null);
                emStatParm.setLowScore(null);
                emStatParm.setUpScore(null);
            } else {
                float low = emStatParm.getLowScore();
                float up = emStatParm.getUpScore();
                float spaceScore = emStatParm.getSpaceScore();
                //默认 从小到大
                while (true) {
                    if (up <= low) {
                        break;
                    }
                    spaceItem = new EmSpaceItem();
                    spaceItem.setId(UuidUtils.generateUuid());
                    spaceItem.setLowScore(low);

                    low = low + spaceScore;
                    if (low >= up) {
                        spaceItem.setUpScore(up);
                    } else {
                        spaceItem.setUpScore(low);
                    }
                    spaceItem.setParmType(EmSpaceItem.PARM_SPACE);
                    spaceItem.setStatParmId(emStatParm.getId());
                    spaceItem.setName(spaceItem.getLowScore() + "-" + spaceItem.getUpScore());
                    insertItem.add(spaceItem);
                }
            }

        }
        //默认值--前10% 前20% 后10%
        emStatParm.setRankStatFront("1");
        spaceItem = new EmSpaceItem();
        spaceItem.setId(UuidUtils.generateUuid());
        spaceItem.setLowScore(0f);
        spaceItem.setUpScore(10f);
        spaceItem.setParmType(EmSpaceItem.PARM_PERCENT_F);
        spaceItem.setStatParmId(emStatParm.getId());
        spaceItem.setName("前10%");
        insertItem.add(spaceItem);

        spaceItem = new EmSpaceItem();
        spaceItem.setId(UuidUtils.generateUuid());
        spaceItem.setLowScore(0f);
        spaceItem.setUpScore(20f);
        spaceItem.setParmType(EmSpaceItem.PARM_PERCENT_F);
        spaceItem.setStatParmId(emStatParm.getId());
        spaceItem.setName("前20%");
        insertItem.add(spaceItem);

        emStatParm.setRankStatBack("1");
        spaceItem = new EmSpaceItem();
        spaceItem.setId(UuidUtils.generateUuid());
        spaceItem.setLowScore(0f);
        spaceItem.setUpScore(20f);
        spaceItem.setParmType(EmSpaceItem.PARM_PERCENT_B);
        spaceItem.setStatParmId(emStatParm.getId());
        spaceItem.setName("后20%");
        insertItem.add(spaceItem);
        emStatObjectService.save(emStatObject);
        this.deleteBySubjectId(unitId, emStatParm.getExamId(), emStatParm.getSubjectId());
        this.save(emStatParm);
        emSpaceItemService.deleteByStatParmId(emStatParm.getId());
        if (CollectionUtils.isNotEmpty(insertItem)) {
            emSpaceItemService.saveAllEntity(insertItem);
        }

    }

    @Override
    public Map<String, EmStatParm> findMapByUnitId(String unitId, String examId) {
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        if (emStatObject == null) {
            return new HashMap<String, EmStatParm>();
        }
        List<EmStatParm> emStatParm = emStatParmDao.findByStatObjectId(emStatObject.getId());
        if (CollectionUtils.isEmpty(emStatParm)) {
            return new HashMap<String, EmStatParm>();
        }
        makeAllSpaceList(emStatParm);
        Map<String, EmStatParm> map = makeSubjectMap(emStatParm);
        return map;

    }

    private Map<String, EmStatParm> makeSubjectMap(List<EmStatParm> emStatParm) {
        Map<String, EmStatParm> map = new HashMap<>();
        for (EmStatParm parm : emStatParm) {
            map.put(parm.getSubjectId(), parm);
        }
        return map;
    }

    private void makeAllSpaceList(List<EmStatParm> emStatParms) {
        if (CollectionUtils.isEmpty(emStatParms)) {
            return;
        }
        Set<String> ids = EntityUtils.getSet(emStatParms, EmStatParm::getId);
        List<EmSpaceItem> list = emSpaceItemService.findByStatParmIdIn(ids.toArray(new String[]{}));
        Map<String, List<EmSpaceItem>> itemMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (EmSpaceItem item : list) {
                String key = item.getStatParmId() + "_" + item.getParmType();
                if (!itemMap.containsKey(key)) {
                    itemMap.put(key, new ArrayList<EmSpaceItem>());
                }
                itemMap.get(key).add(item);
            }
            for (EmStatParm parm : emStatParms) {
                if (itemMap.containsKey(parm.getId() + "_" + EmSpaceItem.PARM_SPACE)) {
                    parm.setEmSpaceItemList1(itemMap.get(parm.getId() + "_" + EmSpaceItem.PARM_SPACE));
                }
                if (itemMap.containsKey(parm.getId() + "_" + EmSpaceItem.PARM_PERCENT_F)) {
                    parm.setEmSpaceItemList2(itemMap.get(parm.getId() + "_" + EmSpaceItem.PARM_PERCENT_F));
                }
                if (itemMap.containsKey(parm.getId() + "_" + EmSpaceItem.PARM_PERCENT_B)) {
                    parm.setEmSpaceItemList3(itemMap.get(parm.getId() + "_" + EmSpaceItem.PARM_PERCENT_B));
                }
                if (itemMap.containsKey(parm.getId() + "_" + EmSpaceItem.PARM_CLASS_RANK)) {
                    parm.setEmSpaceItemList9(itemMap.get(parm.getId() + "_" + EmSpaceItem.PARM_CLASS_RANK));
                }
            }
        }
    }

    @Override
    public void updateStat(String[] ids, String isStat) {
        if (ids != null && ids.length > 0) {
            emStatParmDao.updateStat(ids, isStat);
        }

    }

    @Override
    public List<EmStatParm> findByStatObjectIdAndExamId(String statObjectId, String examId) {
        return emStatParmDao.findByStatObjectIdAndExamId(statObjectId, examId);
    }

}
