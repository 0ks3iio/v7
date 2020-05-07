package net.zdsoft.syncdata.service.impl;

import java.util.Date;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.syncdata.constant.JledqConstant;
import net.zdsoft.syncdata.dto.ResultDto;
import net.zdsoft.syncdata.entity.EdqOrg;
import net.zdsoft.syncdata.service.EdqOrgService;
import net.zdsoft.syncdata.util.JledqSyncDataUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 
 * @author weixh
 * @since 2017年11月30日 下午3:43:55
 */
@Service("edqOrgService")
@Lazy(true)
public class EdqOrgServiceImpl implements EdqOrgService {
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private DeptRemoteService deptRemoteService;
	
	public ResultDto saveEdqOrg(EdqOrg eo) {
		if(JledqConstant.OPERATION_DEL == eo.getOperationType()) {
			return new ResultDto(JledqConstant.STATUS_SUC, "暂不处理删除操作的数据！");
		}
		if(JledqConstant.ZZLX_DISTRICT.equals(eo.getZzlx())) {
			return new ResultDto(JledqConstant.STATUS_SUC, "暂不处理学区信息！");
		} else if(JledqConstant.ZZLX_DEPT.equals(eo.getZzlx())) {
			return dealDept(eo);
		} else {
			return dealUnit(eo);
		}
	}
	
	private ResultDto dealDept(EdqOrg eo) {
		ResultDto dto = new ResultDto(JledqConstant.STATUS_SUC, "");
		Dept dp = SUtils.dc(deptRemoteService.findByUnitAndCode(JledqSyncDataUtil.getEdqEduUnitId(), 
				StringUtils.right(eo.getZzdm(), 6)),
				Dept.class);
		if(JledqConstant.OPERATION_ADD == eo.getOperationType()) {
			if(dp != null) {
				return new ResultDto(JledqConstant.STATUS_FAIL, "部门/科室已存在！");
			}
			
		}
		if (dp == null) {
			dp = new Dept();
			dp.setId(UuidUtils.generateUuid());
			dp.setUnitId(JledqSyncDataUtil.getEdqEduUnitId());
			dp.setParentId(BaseConstants.ZERO_GUID);
			dp.setDeptCode(StringUtils.right(eo.getZzdm(), 6));
			dp.setCreationTime(new Date());
			dp.setInstituteId(BaseConstants.ZERO_GUID);
		}
		dp.setDeptShortName(eo.getZzjc());
		dp.setDeptName(eo.getZzmc());
		dp.setModifyTime(new Date());
		dp.setIsDeleted(0);
		dp.setEventSource(0);
		deptRemoteService.saveAllEntitys(SUtils.s(new Dept[] {dp}));
		return dto;
	}
	
	private ResultDto dealUnit(EdqOrg eorg) {
		ResultDto dto = new ResultDto(JledqConstant.STATUS_SUC, "");
		// TODO
		return dto;
	}
}
