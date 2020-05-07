package net.zdsoft.scoremanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.dao.HwReinstateDao;
import net.zdsoft.scoremanage.data.entity.HwReinstate;
import net.zdsoft.scoremanage.data.entity.HwStatis;
import net.zdsoft.scoremanage.data.entity.HwStatisEx;
import net.zdsoft.scoremanage.data.service.HwReinstateService;
import net.zdsoft.scoremanage.data.service.HwStatisExService;
import net.zdsoft.scoremanage.data.service.HwStatisService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author niuchao
 * @date 2019/11/5 11:32
 */
@Service("hwReinstateService")
public class HwReinstateServiceImpl extends BaseServiceImpl<HwReinstate, String> implements HwReinstateService {

    @Autowired
    private HwReinstateDao HwReinstateDao;
    @Autowired
    private HwStatisService hwStatisService;
    @Autowired
    private HwStatisExService hwStatisExService;

    @Override
    protected BaseJpaRepositoryDao<HwReinstate, String> getJpaDao() {
        return HwReinstateDao;
    }

    @Override
    protected Class<HwReinstate> getEntityClass() {
        return HwReinstate.class;
    }

    @Override
    public List<HwReinstate> findListByStudentId(String unitId, String acadyear, String semester, String studentId) {
        return HwReinstateDao.findByUnitIdAndAcadyearAndSemesterAndStudentId(unitId, acadyear, semester, studentId);
    }

    @Override
    public HwReinstate findOneByStudentId(String unitId, String acadyear, String semester, String studentId, String examId) {
        return HwReinstateDao.findByUnitIdAndAcadyearAndSemesterAndStudentIdAndExamId(unitId, acadyear, semester, studentId, examId);
    }

    @Override
    public void saveReinstate(HwReinstate hwReinstate, HwStatis oldStatis, HwStatis statis) {
        if(hwReinstate!=null){
            save(hwReinstate);
        }
        if(oldStatis!=null){
            if(CollectionUtils.isNotEmpty(oldStatis.getExList1())){
                hwStatisExService.deleteAll(oldStatis.getExList1().toArray(new HwStatisEx[oldStatis.getExList1().size()]));
            }
            hwStatisService.delete(oldStatis);
        }
        if(statis!=null){
           hwStatisService.save(statis);
           if(CollectionUtils.isNotEmpty(statis.getExList1())){
               hwStatisExService.saveAll(statis.getExList1().toArray(new HwStatisEx[statis.getExList1().size()]));
           }
        }
    }

    @Override
    public void deleteReinstate(HwReinstate hwReinstate, HwStatis statis, List<HwStatisEx> statisExList) {
        if(hwReinstate!=null){
            delete(hwReinstate);
        }
        if(statis!=null){
            hwStatisService.delete(statis);
        }
        if(CollectionUtils.isNotEmpty(statisExList)){
            hwStatisExService.deleteAll(statisExList.toArray(new HwStatisEx[statisExList.size()]));
        }
    }

    @Override
    public List<HwReinstate> findListByGradeIdAndExamId(String unitId, String acadyear, String semester, String gradeId, String examId) {
        return HwReinstateDao.findByUnitIdAndAcadyearAndSemesterAndGradeIdAndExamId(unitId, acadyear, semester, gradeId, examId);
    }
}
