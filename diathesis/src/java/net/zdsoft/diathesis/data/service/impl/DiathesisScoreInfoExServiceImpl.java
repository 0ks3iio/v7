package net.zdsoft.diathesis.data.service.impl;


import net.zdsoft.diathesis.data.dao.DiathesisScoreInfoExDao;
import net.zdsoft.diathesis.data.entity.DiathesisScoreInfoEx;
import net.zdsoft.diathesis.data.service.DiathesisScoreInfoExService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/8/14 19:25
 */
@Service("diathesisScoreInfoExService")
public class DiathesisScoreInfoExServiceImpl implements DiathesisScoreInfoExService {
    @Autowired
    private DiathesisScoreInfoExDao diathesisScoreInfoExDao;

    @Override
    public List<DiathesisScoreInfoEx> findListByInfoIdIn(String[] infoIds) {
        if(infoIds==null|| infoIds.length==0)return new ArrayList<>();
        return diathesisScoreInfoExDao.findListByInfoIdIn(infoIds);
    }

    @Override
    public List<DiathesisScoreInfoEx> findListByScoreTypeId(String scoreTypeId) {
        return diathesisScoreInfoExDao.findByScoreTypeId(scoreTypeId);
    }

    @Override
    public void saveAll(List<DiathesisScoreInfoEx> insertExList) {
        diathesisScoreInfoExDao.saveAll(insertExList);
    }

    @Override
    public List<DiathesisScoreInfoEx> findListByInfoTypeIdIn(List<String> scoreTypeIds) {
        if(CollectionUtils.isEmpty(scoreTypeIds))return new ArrayList<>();
        return diathesisScoreInfoExDao.findListByInfoTypeIdIn(scoreTypeIds);
    }
}
