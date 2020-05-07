package net.zdsoft.scoremanage.remote.service.impl;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.scoremanage.data.entity.ScoreInfo;
import net.zdsoft.scoremanage.data.service.ScoreInfoService;
import net.zdsoft.scoremanage.remote.service.ScoreInfoRemoteService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service("scoreInfoRemoteService")
public class ScoreInfoRemoteServiceImpl extends BaseRemoteServiceImpl<ScoreInfo, String> implements ScoreInfoRemoteService{

	@Autowired
	private ScoreInfoService scoreInfoService;
	
	@Override
	protected BaseService<ScoreInfo, String> getBaseService() {
		return scoreInfoService;
	}

	@Override
	public String findScoreInfoList(String examId, String[] subjectIds) {
		return SUtils.s(scoreInfoService.findScoreInfoList(examId,subjectIds));
	}
	
	@Override
	public String findJsonScoreInfo(String schoolId, String examId, String gradeCode) {
		return scoreInfoService.findJsonScoreInfo(schoolId,examId, gradeCode, null);
	}

	@Override
	public String findScoreByCondition(String unitId, String acadyear,
			String semester, String studentId, String examId) {
		return SUtils.s(scoreInfoService.findByCondition(unitId, acadyear, semester, studentId, examId));
	}

	@Override
	public String findBystudentIds(String unitId, String acadyear,
			String semester, String[] studentIds, String examId) {
		return SUtils.s(scoreInfoService.findBystudentIds(unitId, acadyear, semester, studentIds, examId));
	}
	
}
