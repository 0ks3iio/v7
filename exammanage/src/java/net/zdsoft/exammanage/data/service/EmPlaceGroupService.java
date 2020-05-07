package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmPlaceGroup;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EmPlaceGroupService extends BaseService<EmPlaceGroup, String> {

    public List<EmPlaceGroup> findByGroupIdAndSchoolId(String groupId, String schoolId);

    public Map<String, Set<String>> findGroupMap(String unitId, String[] groupIds);

    public String autoArrangePlace(String examId, String unitId);

    public List<EmPlaceGroup> findByExamIdAndSchoolId(String examId, String unitId);

    public String saveAndDel(String examId, String unitId, String groupId, List<EmPlaceGroup> saveAll);


}
