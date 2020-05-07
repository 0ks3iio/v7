package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.constant.ChartsConstants;
import net.zdsoft.basedata.dao.ChartsRoleDao;
import net.zdsoft.basedata.entity.ChartsRole;
import net.zdsoft.basedata.entity.ChartsRoleUser;
import net.zdsoft.basedata.service.ChartsRoleService;
import net.zdsoft.basedata.service.ChartsRoleUserService;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;

@Service("chartsRoleService")
public class ChartsRoleServiceImpl implements ChartsRoleService {

    @Autowired
    private ChartsRoleDao chartsRoleDao;
    @Autowired
    private ChartsRoleUserService chartsRoleUserService;

    @Override
    public List<ChartsRole> findByUserId(final String userId) {
        return RedisUtils.getObject(ChartsConstants.KEY_BEFORE_USER + userId + ".role", 0,
                new TypeReference<List<ChartsRole>>() {
                }, new RedisInterface<List<ChartsRole>>() {

                    @Override
                    public List<ChartsRole> queryData() {
                        List<ChartsRoleUser> cruList = chartsRoleUserService.findByUserId(userId);
                        if (CollectionUtils.isNotEmpty(cruList)) {
                            Set<Integer> cruIds = new HashSet<Integer>();
                            for (ChartsRoleUser item : cruList) {
                                cruIds.add(item.getChartsRoleId());
                            }
                            return chartsRoleDao.findByIsUsingAndIdIn(ChartsConstants.IS_USING_1,
                                    cruIds.toArray(new Integer[0]));
                        }
                        else {
                            return new ArrayList<ChartsRole>();
                        }
                    }

                });
    }

    @Override
    public List<ChartsRole> findAll() {
        return chartsRoleDao.findByIsUsing(ChartsConstants.IS_USING_1);
    }

}
