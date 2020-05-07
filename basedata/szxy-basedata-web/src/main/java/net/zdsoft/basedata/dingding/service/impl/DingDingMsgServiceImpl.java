package net.zdsoft.basedata.dingding.service.impl;

import java.util.List;

import net.zdsoft.basedata.dingding.entity.DdSyncdataLog;
import net.zdsoft.basedata.dingding.entity.DdUnitOpen;
import net.zdsoft.basedata.dingding.entity.DingDingMsg;
import net.zdsoft.basedata.dingding.service.DingDingMsgService;
import net.zdsoft.basedata.dingding.service.DingDingSyncdataLogService;
import net.zdsoft.basedata.dingding.service.DingDingUnitOpenService;
import net.zdsoft.basedata.dingding.utils.DingDingUtils;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service("dingDingMsgService")
public class DingDingMsgServiceImpl extends
		BaseServiceImpl<DingDingMsg, String> implements DingDingMsgService {
	private static Logger log = Logger.getLogger(DingDingMsgServiceImpl.class);

	public static final String DINGDING_MESSAGE_URL = "https://oapi.dingtalk.com/message/send?access_token=";// 钉钉ＵＲＬ

	public static final String DINGDING_AGENTID = "agentid";

	@Autowired
	DingDingUnitOpenService dingDingUnitOpenService;
	@Autowired
	DingDingSyncdataLogService dingDingSyncdataLogService;

	public String addMessages(String unitId, List<JSONObject> contents) {
		String returnMsg = "";
		String accessToken;
		List<DdUnitOpen> unitList = dingDingUnitOpenService
				.findByUnitIdAndState(unitId, 1);
		if (CollectionUtils.isEmpty(unitList)) {
			DdSyncdataLog dataLog = new DdSyncdataLog();
			dataLog.setUnitId(unitId);
			dataLog.setDataType("msg");
			dataLog.setObjectName("钉钉系统消息");
			dataLog.setHandlerType("推送");
			dataLog.setResult(0);
			dataLog.setRemark("推送失败,该单位没有开通钉钉推送服务");
			dingDingSyncdataLogService.insertLogs(dataLog);
			log.error("推送失败,该单位没有开通钉钉推送服务");
			return "推送失败,该单位没有开通钉钉推送服务";
		}
		DdUnitOpen unit = unitList.get(0);
		accessToken = DingDingUtils.getAccessToken(unit.getCorpId(),
				unit.getCorpSecret());
		if (StringUtils.isBlank(accessToken)) {
			DdSyncdataLog dataLog = new DdSyncdataLog();
			dataLog.setUnitId(unitId);
			dataLog.setDataType("msg");
			dataLog.setObjectName("钉钉系统消息");
			dataLog.setHandlerType("推送");
			dataLog.setResult(0);
			dataLog.setRemark("推送失败,accessToken(调用接口凭证)为空");
			dingDingSyncdataLogService.insertLogs(dataLog);
			log.error("推送失败,accessToken(调用接口凭证)为空");
			return "推送失败,accessToken(调用接口凭证)为空";
		}
		for (JSONObject content : contents) {
			String dingdingUrl = DINGDING_MESSAGE_URL;
			DdSyncdataLog dataLog = new DdSyncdataLog();
			dataLog.setUnitId(unitId);
			dataLog.setDataType("msg");
			dataLog.setObjectName("钉钉系统消息");
			dataLog.setHandlerType("推送");
			content.put(DINGDING_AGENTID, unit.getAgentId());
			dingdingUrl += accessToken;
			log.info("推送钉钉消息URL:" + dingdingUrl);
			String result = DingDingUtils.push(dingdingUrl,
					content.toJSONString());
			if (StringUtils.isNotBlank(result) && !"error".equals(result)) {
				JSONObject jsonOjbect = JSONObject.parseObject(result);
				if ("0".equals(jsonOjbect.getString("errcode"))) {
					dataLog.setResult(1);
					if (content.toJSONString().length() > 1000){
						dataLog.setRemark(content.toJSONString().substring(0,
								1000));
					}else{
						dataLog.setRemark(content.toJSONString());
					}
				} else {
					log.error(jsonOjbect.getString("errmsg"));
					dataLog.setResult(0);
					dataLog.setRemark(jsonOjbect.getString("errmsg"));
					returnMsg = jsonOjbect.getString("errmsg");
				}

			} else {
				returnMsg = "钉钉发送消息出错啦！";
				dataLog.setResult(0);
				dataLog.setRemark(returnMsg);
				log.error("钉钉发送消息出错啦！");
			}

			dingDingSyncdataLogService.insertLogs(dataLog);
		}
		return returnMsg;
	}

	@Override
	protected BaseJpaRepositoryDao<DingDingMsg, String> getJpaDao() {
		return null;
	}

	@Override
	protected Class<DingDingMsg> getEntityClass() {
		return DingDingMsg.class;
	}

}
