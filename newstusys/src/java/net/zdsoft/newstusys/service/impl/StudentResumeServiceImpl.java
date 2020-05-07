package net.zdsoft.newstusys.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.newstusys.dao.StudentResumeJdbcDao;
import net.zdsoft.newstusys.entity.StudentResume;
import net.zdsoft.newstusys.service.StudentResumeService;

/**
 * Created by Administrator on 2018/3/1.
 */
@Service("studentResumeService")
public class StudentResumeServiceImpl implements StudentResumeService {
    @Autowired
    private StudentResumeJdbcDao studentResumeJdbcDao;
    
	public void save(StudentResume t) {
		if(StringUtils.isBlank(t.getId())) {
			studentResumeJdbcDao.save(t);
		} else {
			studentResumeJdbcDao.update(t);
		}
	}

	@Override
	public void saveAll(StudentResume[] ts) {
		if(ArrayUtils.isEmpty(ts)) {
			return;
		}
		List<StudentResume> ins = new ArrayList<StudentResume>();
		List<StudentResume> ups = new ArrayList<StudentResume>();
		for(StudentResume t : ts) {
			if(StringUtils.isBlank(t.getId())) {
				ins.add(t);
			} else {
				ups.add(t);
			}
		}
		if(ins.size() > 0) {
			studentResumeJdbcDao.batchSave(ins);
		}
		if(ups.size() > 0) {
			studentResumeJdbcDao.batchUpdate(ups);
		}
	}

	@Override
    public void deleteResumeByStuId(String... stuIds) {
    	studentResumeJdbcDao.deleteByStuIds(stuIds);
    }
	
	public void delete(String... ids) {
		studentResumeJdbcDao.deleteByIds(ids);
	}

    @Override
    public List<StudentResume> findByStuid(String stuId) {
        return studentResumeJdbcDao.findByStuid(stuId);
    }

	@Override
	public List<StudentResume> findByStuids(String... stuIds) {
		return studentResumeJdbcDao.findByStuids(stuIds);
	}
}
