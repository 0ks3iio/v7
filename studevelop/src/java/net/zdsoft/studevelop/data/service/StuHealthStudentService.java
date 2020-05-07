package net.zdsoft.studevelop.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.studevelop.data.entity.StudevelopHealthStudent;

import java.util.List;

/**
 * Created by Administrator on 2018/4/18.
 */
public interface StuHealthStudentService extends BaseService<StudevelopHealthStudent,String> {

    public void saveHealthStudent( StudevelopHealthStudent studevelopHealthStudent);

    public String saveHealthStudentImport(String unitId ,StudevelopHealthStudent studevelopHealthStudent , String filePath);
}
