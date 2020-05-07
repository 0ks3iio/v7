package net.zdsoft.eclasscard.data.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.constant.EccUsedFor;
import net.zdsoft.eclasscard.data.dto.EccUsedForDto;
import net.zdsoft.eclasscard.data.websocket.WebsocketMsg;
import net.zdsoft.eclasscard.data.websocket.service.EClassCardWebSocketService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.system.entity.config.UnitIni;
import net.zdsoft.system.remote.service.SystemIniRemoteService;
import net.zdsoft.system.remote.service.UnitIniRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class EccNeedServiceUtils {
	
	public static final String POST_MSG_TYPE_BE_0001 = "0001";
	public static final String POST_MSG_TYPE_BE_0002 = "0002";
	public static final String POST_MSG_TYPE_BE_0003 = "0003";
	public static final String POST_MSG_TYPE_BE_0004 = "0004";
	public static final String POST_MSG_TYPE_BE_0005 = "0005";
	public static final String POST_MSG_TYPE_BE_0006 = "0006";
	public static final String POST_MSG_TYPE_BE_0007 = "0007";
	public static final String POST_MSG_TYPE_BE_0008 = "0008";
	public static final String POST_MSG_TYPE_BE_0009 = "0009";
	public static final String POST_MSG_TYPE_BE_0010 = "0010";//推定时开关机时间
	public static final String POST_MSG_TYPE_BE_0011 = "0011";//推下发人脸
	
	private static final Logger log = LoggerFactory.getLogger(EccNeedServiceUtils.class);
	private static SystemIniRemoteService getSystemIniRemoteService() {
		return Evn.getBean("systemIniRemoteService");
	}
	
	private static UnitIniRemoteService getUnitIniRemoteService() {
		return Evn.getBean("unitIniRemoteService");
	}
	
	private static EClassCardWebSocketService getEClassCardWebSocketService() {
		return Evn.getBean("eClassCardWebSocketService");
	}
	
	/**
	 * 获取班牌使用版本
	 * @param unitId
	 * @return
	 */
	public static String getEClassCardVerison(String unitId) {
		UnitIni unitIni = SUtils.dc(getUnitIniRemoteService().getUnitIni(unitId, UnitIni.ECC_USE_VERSION),UnitIni.class);
		if(unitIni!=null&&StringUtils.isNotBlank(unitIni.getNowvalue())){
			return unitIni.getNowvalue();
		}
		String eccSysVersion = getSystemIniRemoteService().findValue(EccConstants.ECC_USE_VERSION);
		return eccSysVersion;
		
	}
	/**
	 * 获取班牌首页是否定制layout
	 * @param unitId
	 * @return
	 */
	public static boolean getIsLayout() {
		String layout = getSystemIniRemoteService().findValue(EccConstants.ECC_INDEX_LAYOUT);
		if(Objects.equals("1", layout)){
			return true;
		}
		return false;
		
	}
	
	/**
	 * 获取是否开启定时开关机
	 * @param unitId
	 * @return
	 */
	public static String getEClassCardOpenClose(String unitId) {
		UnitIni unitIni = SUtils.dc(getUnitIniRemoteService().getUnitIni(unitId, UnitIni.ECC_USE_OPEN_CLOSE),UnitIni.class);
		if(unitIni!=null&&StringUtils.isNotBlank(unitIni.getNowvalue())){
			return unitIni.getNowvalue();
		}
		return "0";
	}
	
	/**
	 * 获取班牌厂家
	 * @param unitId
	 * @return
	 */
	public static String getEClassCardFactory(String unitId) {
		UnitIni unitIni = SUtils.dc(getUnitIniRemoteService().getUnitIni(unitId, UnitIni.ECC_USE_FACTORY_TYPE),UnitIni.class);
		if(unitIni!=null&&StringUtils.isNotBlank(unitIni.getNowvalue())){
			return unitIni.getNowvalue();
		}
		return "";
	}
	/**
	 * 获取是否显示综合评价
	 * @param unitId
	 * @return
	 */
	public static String getEClassCardShowZHPJ(String unitId) {
		UnitIni unitIni = SUtils.dc(getUnitIniRemoteService().getUnitIni(unitId, UnitIni.ECC_USE_SHOW_ZHPJ),UnitIni.class);
		if(unitIni!=null&&StringUtils.isNotBlank(unitIni.getNowvalue())){
			return unitIni.getNowvalue();
		}
		return "0";
	}
	
	/**
	 * 获取单位下使用的班牌用途列表
	 * @param notContansType
	 * @param unitId
	 * @return
	 */
	public static List<EccUsedForDto> getEccUsedForList(
			Set<String> notContansType,String unitId) {
		if(notContansType == null)notContansType = Sets.newHashSet();
		if(UnitIni.ECC_USE_VERSION_HW.equals(getEClassCardVerison(unitId))){
			notContansType.add(EccConstants.ECC_MCODE_BPYT_7);
		}
		List<EccUsedForDto> list = new ArrayList<>();
		for (EccUsedFor ent : EccUsedFor.values()) {
			if (CollectionUtils.isNotEmpty(notContansType)
					&& notContansType.contains(ent.getThisId())) {
				continue;
			}
			EccUsedForDto usedFor = new EccUsedForDto();
			usedFor.setThisId(ent.getThisId());
			usedFor.setContent(ent.getContent());
			list.add(usedFor);
		}
		return list;
	}
	
	/**
	 * 推送通知公告到页面
	 * @param type
	 * @param sids
	 * @param unitId
	 */
	public static void postBulletin(Integer type, Set<String> sids,String unitId,String bulletinId) {
		List<String> cardList = Lists.newArrayList(sids);
		WebsocketMsg msg = new WebsocketMsg();
		if(type==EccConstants.ECC_BULLETIN_TYPE_3){
			msg.setType("function");
			msg.setFunName("showFullScreenBulletin('"+bulletinId+"')");
			postBeMsg(cardList, POST_MSG_TYPE_BE_0008, bulletinId);
		}else if(type==EccConstants.ECC_BULLETIN_TYPE_2){
			msg.setId("topbulletinDiv");
			msg.setType("div");
			msg.setUrl("/eccShow/eclasscard/standard/showindex/topbulletin");
			postBeMsg(cardList, POST_MSG_TYPE_BE_0007, "");
		}else{
			msg.setId("bulletinDiv");
			msg.setType("div");
			if(UnitIni.ECC_USE_VERSION_STANDARD.equals(getEClassCardVerison(unitId))){
				msg.setUrl("/eccShow/eclasscard/standard/showindex/bulletin");
				postBeMsg(cardList, POST_MSG_TYPE_BE_0006, "");
			}else{
				msg.setUrl("/eccShow/eclasscard/showIndex/bulletin");
			}
		}
		
		String jsonMessage = JSON.toJSONString(msg);
		getEClassCardWebSocketService().sendToCard(cardList, jsonMessage);
	}
	
	/**
	 * 推送上课考勤，下课到首页
	 * @param sids
	 * @param type 1.刷到首页，2刷到考勤页
	 */
	public static void postClassClock(Set<String> sids,int type) {
		WebsocketMsg msg = new WebsocketMsg();
		List<String> cardList = Lists.newArrayList(sids);
		if(type==1){
			msg.setType("fresh");
			msg.setUrl("/eccShow/eclasscard/showIndex");
			postBeMsg(cardList, POST_MSG_TYPE_BE_0002, "");
		}else if(type==3){
			msg.setType("function");
			msg.setFunName("showInOutAttence()");
		}else{
			msg.setType("function");
			msg.setFunName("showClassClock()");
			postBeMsg(cardList, POST_MSG_TYPE_BE_0001, "");
		}
		String jsonMessage = JSON.toJSONString(msg);
		getEClassCardWebSocketService().sendToCard(cardList, jsonMessage);
	}
	
	/**
	 * 推送荣誉到页面
	 * @param sids
	 */
	public static void postHonor(Set<String> sids) {
		WebsocketMsg msg = new WebsocketMsg();
		msg.setType("div");
		msg.setId("honorDiv");
		msg.setUrl("/eccShow/eclasscard/standard/showindex/honorlist");
		String jsonMessage = JSON.toJSONString(msg);
		List<String> cardList = Lists.newArrayList(sids);
		getEClassCardWebSocketService().sendToCard(cardList, jsonMessage);
		postBeMsg(cardList, POST_MSG_TYPE_BE_0003, "");
		postBeMsg(cardList, POST_MSG_TYPE_BE_0004, "");
	}
	
	/**
	 * 推送相册
	 * @param sids
	 */
	public static void postPhoto(Set<String> sids,String unitId) {
		List<String> cardList = Lists.newArrayList(sids);
		WebsocketMsg msg = new WebsocketMsg();
		msg.setType("div");
		msg.setId("albumDiv");
		if(UnitIni.ECC_USE_VERSION_STANDARD.equals(getEClassCardVerison(unitId))){
			msg.setUrl("/eccShow/eclasscard/standard/showindex/album");
			postBeMsg(cardList, POST_MSG_TYPE_BE_0005, "");
		}else{
			msg.setUrl("/eccShow/eclasscard/showIndex/album");
		}
		String jsonMessage = JSON.toJSONString(msg);
		getEClassCardWebSocketService().sendToCard(cardList, jsonMessage);
		PushPotoUtils.removePushCards(sids);
	}
	
	/**
	 * 推送简介
	 * @param sids
	 */
	public static void postDesc(Set<String> sids) {
		WebsocketMsg msg = new WebsocketMsg();
		msg.setType("div");
		msg.setId("classDescDiv");
		msg.setUrl("/eccShow/eclasscard/showIndex/description");
		String jsonMessage = JSON.toJSONString(msg);
		List<String> cardList = Lists.newArrayList(sids);
		getEClassCardWebSocketService().sendToCard(cardList, jsonMessage);
	}
	
	/**
	 * 推送寝室考勤页面年级信息
	 * @param sids
	 */
	public static void postDormClock(Set<String> sids) {
		WebsocketMsg msg = new WebsocketMsg();
		msg.setType("div");
		msg.setUrl("/eccShow/eclasscard/showIndex/clockGrade");
		msg.setId("dormClockGradeDiv");
		String jsonMessage = JSON.toJSONString(msg);
		List<String> cardList = Lists.newArrayList(sids);
		getEClassCardWebSocketService().sendToCard(cardList, jsonMessage);
	}
	
	/**
	 * 同步同一场地多块班牌学生打卡信息
	 * @param cardList
	 * @param arr
	 */
	public static void postRingClassClock(List<String> cardList,JSONArray arr) {
		WebsocketMsg msg = new WebsocketMsg();
		msg.setType("function");
		msg.setFunName("showStudentRingAttance("+arr.toJSONString()+")");
		String jsonMessage = JSON.toJSONString(msg);
		getEClassCardWebSocketService().sendToCard(cardList, jsonMessage);
	}
	
	/**
	 * 同步上下学非本班级班牌打卡的学生打卡信息
	 * @param cardList
	 * @param arr
	 */
	public static void postInoutClock(List<String> cardList,JSONArray arr) {
		WebsocketMsg msg = new WebsocketMsg();
		msg.setType("function");
		msg.setFunName("showOtherCardInoutAttance("+arr.toJSONString()+")");
		String jsonMessage = JSON.toJSONString(msg);
		getEClassCardWebSocketService().sendToCard(cardList, jsonMessage);
	}
	
	/**
	 * 推送进出校信息到微课
	 * @param array
	 */
	public static void sendInOutDataToWeiKe(JSONArray array){
		Map<String,String> parmMap = new HashMap<>();
		parmMap.put("param", array.toString());
		try {
			String wkHeathUrl = getSystemIniRemoteService().findValue("WK.HEALTH.URL");
			if(StringUtils.isNotBlank(wkHeathUrl)){
				EccUtils.sendHttpPost(wkHeathUrl, parmMap);
			}else{
				log.error("未配置微课健康数据推送url");
			}
		} catch (Exception e) {
			log.error("推送微课失败："+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 推到考场门贴展示页面
	 * @param sids
	 * @param id
	 */
	public static void postExamDoorSticker(Set<String> sids, String id) {
		if(StringUtils.isNotBlank(id)){
			WebsocketMsg msg = new WebsocketMsg();
			msg.setType("function");
			msg.setFunName("showExamDoorSticker('"+id+"')");
			String jsonMessage = JSON.toJSONString(msg);
			List<String> cardList = Lists.newArrayList(sids);
			getEClassCardWebSocketService().sendToCard(cardList, jsonMessage);
			postBeMsg(cardList, POST_MSG_TYPE_BE_0009, id);
		}
	}
	
	/**
	 * 直接关闭全屏
	 * @param sids
	 */
	public static void postCloseFullScreen(Set<String> sids) {
		WebsocketMsg msg = new WebsocketMsg();
		msg.setType("function");
		msg.setFunName("closeFullScreen()");
		String jsonMessage = JSON.toJSONString(msg);
		List<String> cardList = Lists.newArrayList(sids);
		getEClassCardWebSocketService().sendToCard(cardList, jsonMessage);
	}
	
	/**
	 * 检查是否有全屏展示的内容
	 * @param sids
	 */
	public static void postCheckFullScreen(Set<String> sids) {
		WebsocketMsg msg = new WebsocketMsg();
		msg.setType("function");
		msg.setFunName("checkFullScreenObj()");
		String jsonMessage = JSON.toJSONString(msg);
		List<String> cardList = Lists.newArrayList(sids);
		getEClassCardWebSocketService().sendToCard(cardList, jsonMessage);
	}
	
	public static void fullScreenLockCheck(Set<String> sids) {
		WebsocketMsg msg = new WebsocketMsg();
		msg.setType("function");
		msg.setFunName("fullScreenLockCheck()");
		String jsonMessage = JSON.toJSONString(msg);
		List<String> cardList = Lists.newArrayList(sids);
		getEClassCardWebSocketService().sendToCard(cardList, jsonMessage);
	}
	
	/**
	 * 前端分离后推送
	 * @param cardList
	 * @param id
	 * @param param
	 */
	public static void postBeMsg(List<String> cardList,String id,String param) {
		if(param==null)param = "";
		WebsocketMsg msg = new WebsocketMsg();
		msg.setType("text");
		msg.setId(id);
		msg.setText(param);
		String jsonMessage = JSON.toJSONString(msg);
		getEClassCardWebSocketService().sendToCard(cardList, jsonMessage);
	}
	/**
	 * 推送留言到页面
	 * @param sids
	 */
	public static void postLeaveWord(Set<String> sids,String stuId,String familyId,String message,String time) {
		WebsocketMsg msg = new WebsocketMsg();
		msg.setType("function");
		msg.setFunName("showIndexLeaveWord('"+stuId+"','"+familyId+"','"+message+"','"+time+"')");
		String jsonMessage = JSON.toJSONString(msg);
		List<String> cardList = Lists.newArrayList(sids);
		getEClassCardWebSocketService().sendToCard(cardList, jsonMessage);
	}
}
