package net.zdsoft.diathesis.data.service;

import net.zdsoft.diathesis.data.entity.DiathesisScoreInfoEx;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/8/14 19:25
 */
public interface DiathesisScoreInfoExService {

    List<DiathesisScoreInfoEx> findListByInfoIdIn(String[] infoIds);

    List<DiathesisScoreInfoEx> findListByScoreTypeId(String scoreTypeId);

    void saveAll(List<DiathesisScoreInfoEx> insertExList);

    List<DiathesisScoreInfoEx> findListByInfoTypeIdIn(List<String> scoreTypeIds);
}
