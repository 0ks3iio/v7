package net.zdsoft.diathesis.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.diathesis.data.dto.DiathesisGroupList;
import net.zdsoft.diathesis.data.dto.DiathesisIdAndNameDto;
import net.zdsoft.diathesis.data.dto.DiathesisMutualGroupDto;
import net.zdsoft.diathesis.data.entity.DiathesisMutualGroup;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/6/3 17:21
 */
public interface DiathesisMutualGroupService extends BaseService<DiathesisMutualGroup,String> {

    void deleteById(String groupId);

    List<DiathesisMutualGroupDto> findGroupByInfo(String classId ,Integer semester,String acadyear);

    List<DiathesisIdAndNameDto> findMutualStudentList(String studentId,String unitId,String acadyear,Integer semeter);

    List<DiathesisMutualGroup> findByLeadIdAndSemester(String studentId, String acadyear, Integer semester);

    DiathesisMutualGroup findOneById(String groupId);

    void saveGroupList(DiathesisGroupList groupList);
}
