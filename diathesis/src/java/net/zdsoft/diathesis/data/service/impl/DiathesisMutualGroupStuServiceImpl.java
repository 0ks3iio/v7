package net.zdsoft.diathesis.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.diathesis.data.dao.DiathesisMutualGroupStuDao;
import net.zdsoft.diathesis.data.entity.DiathesisMutualGroupStu;
import net.zdsoft.diathesis.data.service.DiathesisMutualGroupStuService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/6/3 17:15
 */
@Service("diathesisMutualGroupStu")
public class DiathesisMutualGroupStuServiceImpl extends BaseServiceImpl<DiathesisMutualGroupStu,String>
        implements DiathesisMutualGroupStuService {
    @Autowired
    private DiathesisMutualGroupStuDao diathesisMutualGroupStuDao;

    @Override
    protected BaseJpaRepositoryDao<DiathesisMutualGroupStu, String> getJpaDao() {
        return diathesisMutualGroupStuDao;
    }

    @Override
    protected Class<DiathesisMutualGroupStu> getEntityClass() {
        return DiathesisMutualGroupStu.class;
    }


    @Override
    public String findMutualGroupIdByStudentId(String studentId, String acady, Integer semester) {
        return diathesisMutualGroupStuDao.findMutualGroupIdByStudentIdAndSemester(studentId,acady,semester);
    }

    @Override
    public List<DiathesisMutualGroupStu> findListByMutualGroupId(String groupId) {
        return diathesisMutualGroupStuDao.findListByMutualGroupId(groupId);
    }

    @Override
    public List<DiathesisMutualGroupStu> findListByMutualGroupIdIn(List<String> groupId) {
        if(CollectionUtils.isEmpty(groupId))return new ArrayList<>();
        return diathesisMutualGroupStuDao.findListByMutualGroupIdIn(groupId);
    }

    @Override
    public void deleteByMutualGroupIds(List<String> groupIds) {
        if(CollectionUtils.isEmpty(groupIds))
        diathesisMutualGroupStuDao.deleteByMutualGroupIds(groupIds);
    }
}
