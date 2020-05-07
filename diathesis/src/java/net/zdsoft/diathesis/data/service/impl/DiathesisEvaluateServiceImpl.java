package net.zdsoft.diathesis.data.service.impl;


import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.diathesis.data.dao.DiathesisEvaluateDao;
import net.zdsoft.diathesis.data.entity.DiathesisEvaluate;
import net.zdsoft.diathesis.data.service.DiathesisEvaluateService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/5/15 10:34
 */
@Service("diathesisStuReadmeService")
public class DiathesisEvaluateServiceImpl extends BaseServiceImpl<DiathesisEvaluate,String>
        implements DiathesisEvaluateService {

    @Autowired
    DiathesisEvaluateDao diathesisEvaluateDao;


    @Override
    protected BaseJpaRepositoryDao<DiathesisEvaluate, String> getJpaDao() {
        return diathesisEvaluateDao;
    }

    @Override
    protected Class<DiathesisEvaluate> getEntityClass() {
        return DiathesisEvaluate.class;
    }


    @Override
    public void saveEvaluate(DiathesisEvaluate evaluate) {
        Assert.hasLength(evaluate.getContentTxt(),"评价内容不能为空");
        Assert.hasLength(evaluate.getStudentId(),"学生id不能为空");
        Assert.hasLength(evaluate.getUnitId(),"单位id不能为空");
        evaluate.setModifyTime(Calendar.getInstance().getTime());
        if(StringUtils.isBlank(evaluate.getId())){
            evaluate.setId(UuidUtils.generateUuid());
            evaluate.setCreationTime(Calendar.getInstance().getTime());
        }else{
            DiathesisEvaluate entity = findOne(evaluate.getId());
            if(entity==null)throw new RuntimeException("找不到这条记录,检查id是否正确");
            evaluate.setCreationTime(entity.getCreationTime());
        }
        diathesisEvaluateDao.save(evaluate);
    }

	@Override
	public List<DiathesisEvaluate> findByUnitIdAndStudentId(String unitId, String studentId) {
		return diathesisEvaluateDao.findByUnitIdAndStudentId(unitId,studentId);
	}
}
