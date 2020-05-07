package net.zdsoft.studevelop.data.service.impl;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.dao.StudevelopTemplateDao;
import net.zdsoft.studevelop.data.entity.StudevelopTemplate;
import net.zdsoft.studevelop.data.service.StudevelopTemplateService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by luf on 2018/12/17.
 */
@Service("StudevelopTemplateService")
public class StudevelopTemplateServiceImpl extends BaseServiceImpl<StudevelopTemplate ,String> implements StudevelopTemplateService {
    @Autowired
    private StudevelopTemplateDao studevelopTemplateDao;
    @Autowired
    private SystemIniRemoteService systemIniRemoteService;
    @Override
    protected BaseJpaRepositoryDao<StudevelopTemplate, String> getJpaDao() {
        return studevelopTemplateDao;
    }
    @Override
    protected Class<StudevelopTemplate> getEntityClass() {
        return StudevelopTemplate.class;
    }
    @Override
    public List<StudevelopTemplate> getTemplateByCode(String acadyear, String semester, String gradeId, String section, String code,String unitId) {
        String deployRegion = systemIniRemoteService.findValue(BaseConstants.SYS_OPTION_REGION);
//        deployRegion = StuDevelopConstant.DEPLOY_CIXI;
        if (StuDevelopConstant.DEPLOY_CIXI.equals(deployRegion)) {
            unitId = Constant.GUID_ZERO;
            return studevelopTemplateDao.getTemplateBySectionCode(section, code, unitId);
        }else{
            if(StringUtils.isNotEmpty(gradeId)){
                return studevelopTemplateDao.getTemplateByCode(acadyear,semester,gradeId,code ,unitId);
            }else{
                return studevelopTemplateDao.getTemplateBySection(acadyear,semester,section,code,unitId);
            }
        }


    }
}
