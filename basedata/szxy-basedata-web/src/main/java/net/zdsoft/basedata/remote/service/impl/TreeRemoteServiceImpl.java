package net.zdsoft.basedata.remote.service.impl;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.remote.service.TreeRemoteService;
import net.zdsoft.basedata.service.TreeService;

@Service("treeRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class TreeRemoteServiceImpl implements TreeRemoteService {

    @Autowired
    private TreeService treeService;

    @Override
    public String gradeClassForSchoolInsetZTree(String... unitIds) {
        return treeService.gradeClassForSchoolInsetZTree(unitIds).toJSONString();
    }

	@Override
	public String gradeForSchoolInsetZTree(String... unitIds) {
		return treeService.gradeForSchoolInsetZTree(unitIds).toJSONString();
	}

	@Override
	public String deptForUnitInsetZTree(String... unitIds) {
		return treeService.deptForUnitInsetZTree(unitIds).toJSONString();
	}

	@Override
	public JSONArray deptTeacherForUnitInsetTree(String unitId) {
		return treeService.deptTeacherForUnitInsetZTree(new String[]{unitId});
	}
}
