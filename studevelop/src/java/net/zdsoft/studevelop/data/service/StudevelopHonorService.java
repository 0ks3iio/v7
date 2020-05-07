package net.zdsoft.studevelop.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StudevelopHonor;

import java.util.List;

/**
 * studevelop_honor
 * @author 
 * 
 */
public interface StudevelopHonorService extends BaseService<StudevelopHonor, String>{

    public List<StudevelopHonor> getStudevelopHonorByAcadyearAndSemester(String acadyear, int semester , String classId);

    public List<StudevelopHonor> getStudevelopHonorByClassIds(String acadyear, int semester , String[] classIds);
}