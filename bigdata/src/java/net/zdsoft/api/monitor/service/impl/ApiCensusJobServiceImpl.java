package net.zdsoft.api.monitor.service.impl;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import net.zdsoft.api.base.entity.eis.ApiCensusCount;
import net.zdsoft.api.base.entity.eis.ApiInterface;
import net.zdsoft.api.base.entity.eis.ApiInterfaceCount;
import net.zdsoft.api.base.entity.eis.ApiInterfaceType;
import net.zdsoft.api.base.service.ApiCensusCountService;
import net.zdsoft.api.base.service.ApiInterfaceCountService;
import net.zdsoft.api.base.service.ApiInterfaceService;
import net.zdsoft.api.base.service.ApiInterfaceTypeService;
import net.zdsoft.api.monitor.service.ApiCensusJobService;
import net.zdsoft.api.monitor.vo.Census;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;

@Service("apiCensusJobService")
public class ApiCensusJobServiceImpl implements ApiCensusJobService {
	
	@Autowired
	private ApiInterfaceTypeService apiInterfaceTypeService;
	@Autowired
	private ApiInterfaceService apiInterfaceService;
	@Autowired
	private ApiInterfaceCountService apiInterfaceCountService;
	@Autowired
	private ApiCensusCountService apiCensusCountService;
	
	@Override
	public void doCensus() throws JSONException {
		Date mouthFirst, mouthEnd, weekFirst, weekEnd, dayFirst, dayEnd ;
		dayFirst    = DateUtils.getStartDate(new Date());
        dayEnd      = DateUtils.currentEndDate();
        weekFirst   = getWeekMap(dayFirst).get("weekFirst");
        weekEnd     = getWeekMap(dayFirst).get("weekEnd");
        mouthFirst  = getMouthFirst(dayFirst);
        mouthEnd    = getMouthEnd(dayFirst);
        Integer[] types = {ApiInterfaceType.RESULT_TYPE,ApiInterfaceType.PUBLIC_TYPE};
        List<ApiInterfaceType> allResultTypes      = apiInterfaceTypeService.findByClassifyIn(types);
    	List<ApiInterface>     allInterfaces       = apiInterfaceService.findAll();
    	
    	List<ApiCensusCount> saveApiCensusCounts = new ArrayList<ApiCensusCount>();
    	//----------根据类型来统计
    	Map<String, ApiCensusCount> typeMap = new HashMap<String, ApiCensusCount>();
		getCensusByDate(mouthFirst, mouthEnd, allResultTypes, "mouth",  typeMap);
		getCensusByDate(weekFirst , weekEnd , allResultTypes, "week" ,  typeMap);
		getCensusByDate(dayFirst  , dayEnd  , allResultTypes, "newday", typeMap);
		
		
		//----------根据开发者来统计
		Map<String, ApiCensusCount> developerMap = new HashMap<String, ApiCensusCount>();
		List<String> developerList = apiInterfaceCountService.getDistinctTicketKey();
		getDeveloperCensusByDate(developerList, "all", developerMap);
		
		//----------统计总数
		
		ApiCensusCount allACC = new ApiCensusCount();
		allACC.setKey("all");
		allACC.setValue("");
		allACC.setTypeCount(CollectionUtils.isNotEmpty(allResultTypes) ? allResultTypes.size() : 0);
		allACC.setApiCount (CollectionUtils.isNotEmpty(allInterfaces)  ? allInterfaces.size()  : 0);
		
		int saveCount = 0, saveNum = 0,findCount = 0, findNum = 0;
		for (Map.Entry<String,ApiCensusCount> entry : developerMap.entrySet()) {
			ApiCensusCount census = entry.getValue();
			saveCount += census.getSaveCount();
			saveNum   += census.getSaveNum();
			findCount += census.getFindCount();
			findNum   += census.getFindNum();
			saveApiCensusCounts.add(census);
		}
		allACC.setSaveCount(saveCount);
		allACC.setSaveNum(saveNum);
		allACC.setFindCount(findCount);
		allACC.setFindNum(findNum);
		saveApiCensusCounts.add(allACC);
		
		for (Map.Entry<String,ApiCensusCount> entry : typeMap.entrySet()) {
			ApiCensusCount census = entry.getValue();
			saveApiCensusCounts.add(census);
		}
		
		if(CollectionUtils.isNotEmpty(saveApiCensusCounts)){
			doSaveApiCensusCount(saveApiCensusCounts);
		}
	}
	//-------------------------------------------私有方法区 -------------------------------------------------------------------------
	private void doSaveApiCensusCount(List<ApiCensusCount> saveApiCensusCounts) {
		Date   dayFirst     = DateUtils.getStartDate(new Date());
		Date   dayEnd       = DateUtils.currentEndDate();
		String key = DateUtils.date2String(new Date(), "yyyy-MM-dd");
		if(RedisUtils.hasLocked(key)){
			 try{
				 List<ApiCensusCount> todayCounts = apiCensusCountService.findByDate(dayFirst, dayEnd);
				 if(CollectionUtils.isEmpty(todayCounts)){
					 saveApiCensusCounts.forEach(c->{
						c.setCreationTime(new Date());
						c.setId(UuidUtils.generateUuid());
					 });
					apiCensusCountService.saveAll(saveApiCensusCounts.toArray(new ApiCensusCount[saveApiCensusCounts.size()]));
				 }
			  }catch(Exception e){
				  e.printStackTrace();
			  }finally{
				  RedisUtils.unLock(key);
			  }
		}
	}
	/**
	 * 根据时间来得到统计数据
	 * @param  mouthFirst
	 * @param  mouthEnd
	 * @param  allResultTypes
	 * @param  type
	 * @param  censusCount
	 * @throws JSONException
	 */
	private void getCensusByDate(Date dateStart, Date dataEnd,List<ApiInterfaceType> allResultTypes, String type,  Map<String, ApiCensusCount> typeMap) throws JSONException {
        for (ApiInterfaceType c : allResultTypes) {
        	int saveCount = 0,findCount = 0,saveNum = 0,findNum = 0;
        	JSONArray inteArray = new JSONArray();
        	JSONArray typeArray = new JSONArray();
        	//按照类型的对象
            ApiCensusCount typeACC;
		    if(typeMap.get(c.getType()) != null){
			   typeACC = typeMap.get(c.getType());
		    }else{
			   typeACC = new ApiCensusCount();
			   typeACC.setKey("type");
			   typeACC.setValue(c.getType());
		    }
        	List<ApiInterfaceCount> allApiInterfaceCounts =
        			apiInterfaceCountService.findByResultTypeAndCreationTime(c.getType(), dateStart, dataEnd);
        	int tsave = 0;
			int tfind = 0;
			int tsaveNum = 0;
			int tfindNum = 0;
			if(CollectionUtils.isNotEmpty(allApiInterfaceCounts)){
				Census typeCensus = new Census();
				Map<String,List<ApiInterfaceCount>> interfaceIdMap = EntityUtils.getListMap(allApiInterfaceCounts, 
						ApiInterfaceCount::getInterfaceId, Function.identity());
				for (Map.Entry<String,List<ApiInterfaceCount>> entry : interfaceIdMap.entrySet()) {
					   Census inCensus = new Census();
					   List<ApiInterfaceCount> interfaceCounts = entry.getValue();
					   int isave = 0;
					   int ifind = 0;
					   int isaveNum = 0;
					   int ifindNum = 0;
					   for (ApiInterfaceCount t : interfaceCounts) {
						    int count = t.getCount();
							if(t.getDataType() == ApiInterface.BASE_DATE_TYPE ){
								tfind += count;
								findNum++;
								ifindNum++;
								tfindNum++;
								ifind += count;
							}else if (t.getDataType() == ApiInterface.PUSH_DATE_TYPE){
								tsave += count;
								saveNum++;
								isaveNum++;
								tsaveNum++;
								isave += count;
							}
						}
					   inCensus.setInterfaceId(entry.getKey());
					   inCensus.setResultType(c.getType());
					   inCensus.setiSaveCount(isave);
					   inCensus.setiFindCount(ifind);
					   inCensus.setiFindNum(ifindNum);
					   inCensus.setiSaveNum(isaveNum);
					   inteArray.add(inCensus);
			    }
				saveCount += tsave; 
				findCount += tfind;
				typeCensus.setResultType(c.getType());
				typeCensus.settSaveCount(tsave);
				typeCensus.settFindCount(tfind);
				typeCensus.settSaveNum(tsaveNum);
				typeCensus.settFindNum(tfindNum);
				typeArray.add(typeCensus);
			}
			
			String inteString = StringUtils.EMPTY;
	        String typeString = StringUtils.EMPTY;
	        if(inteArray != null){
	        	inteString = inteArray.toJSONString();
	        }
	        if(typeArray != null ){
	        	typeString = typeArray.toJSONString();
	        }
			if("mouth".equals(type)){
				typeACC.setMouthCensus(typeString);
				typeACC.setMouthApiCensus(inteString);
	        }
	        if("week".equals(type)){
	        	typeACC.setWeekCensus(typeString);
	        	typeACC.setWeekApiCensus(inteString);
	        }
	        if("newday".equals(type)){
	        	typeACC.setDayCensus(typeString);
	        	typeACC.setDayApiCensus(inteString);
	        }
	        if(typeMap.get(c.getType()) != null){
	        	findCount = findCount + typeACC.getFindCount();
	        	findNum   = findNum   + typeACC.getFindNum();
	        	saveCount = saveCount + typeACC.getSaveCount();
	        	saveNum   = saveNum   + typeACC.getSaveNum();
	        }
	        typeACC.setFindCount(findCount);
	        typeACC.setFindNum(findNum);
	        typeACC.setSaveCount(saveCount);
	        typeACC.setSaveNum(saveNum);
	        typeMap.put(c.getType(), typeACC);
		}
	}
	
	
	/**
	 * 根据开发者来进行统计
	 * @param mouthFirst
	 * @param mouthEnd
	 * @param developerList
	 * @param type
	 * @param developerMap
	 */
    private void getDeveloperCensusByDate(List<String> developerList, String type,Map<String, ApiCensusCount> developerMap) {
    	for (String c : developerList) {
    		int saveCount = 0,findCount = 0,saveNum = 0,findNum = 0;
        	JSONArray typeArray = new JSONArray();
        	//按照类型的对象
            ApiCensusCount developerACC;
		    if(developerMap.get(c) != null){
			   developerACC = developerMap.get(c);
		    }else{
			   developerACC = new ApiCensusCount();
			   developerACC.setKey("developer");
			   developerACC.setValue(c);
		    }
        	List<ApiInterfaceCount> allApiInterfaceCounts = apiInterfaceCountService.findByTicketKey(c);
			if(CollectionUtils.isNotEmpty(allApiInterfaceCounts)){
				Map<String,List<ApiInterfaceCount>> typeMap = EntityUtils.getListMap(allApiInterfaceCounts, 
						ApiInterfaceCount::getResultType, Function.identity());
				for (Map.Entry<String,List<ApiInterfaceCount>> entry : typeMap.entrySet()) {
					   Census typeCensus = new Census();
					   int tsave = 0;
					   int tfind = 0;
					   int tsaveNum = 0;
					   int tfindNum = 0;
					   List<ApiInterfaceCount> interfaceCounts = entry.getValue();
					   for (ApiInterfaceCount t : interfaceCounts) {
						    int count = t.getCount();
							if(t.getDataType() == ApiInterface.BASE_DATE_TYPE ){
								tfind += count;
								tfindNum++;
							}else if (t.getDataType() == ApiInterface.PUSH_DATE_TYPE){
								tsave += count;
								tsaveNum++;
							}
						}
					typeCensus.setResultType(entry.getKey());
					typeCensus.settSaveCount(tsave);
					typeCensus.settFindCount(tfind);
					typeCensus.settSaveNum(tsaveNum);
					typeCensus.settFindNum(tfindNum);
					typeArray.add(typeCensus);
					saveCount += tsave; 
					saveNum   += tsaveNum;
					findCount += tfind;
					findNum   += tfindNum;
			    }
			}
	        String typeString = StringUtils.EMPTY;
	        if(typeArray != null ){
	        	typeString = typeArray.toJSONString();
	        }
	        if("all".equals(type)){
	        	developerACC.setAllCensus(typeString);
	        	developerACC.setSaveCount(saveCount);
	        	developerACC.setSaveNum(saveNum);
	        	developerACC.setFindCount(findCount);
	        	developerACC.setFindNum(findNum);
	        }
	        developerMap.put(c, developerACC);
		}
	}
	

	private Date getMouthEnd(Date startDate) {
		Calendar cale = Calendar.getInstance();  
        cale.add(Calendar.MONTH, 1);  
        cale.set(Calendar.DAY_OF_MONTH, 0);  
		return cale.getTime();
	}

	private Date getMouthFirst(Date startDate) {
		Calendar cale = Calendar.getInstance();
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);  
        cale.set(Calendar.DAY_OF_MONTH, 1);  
		return cale.getTime();
	}
	
    private Map<String, Date> getWeekMap(Date date) {  
	     Map<String, Date> weekMap = new HashMap<>();
	     Calendar cal = Calendar.getInstance();  
	     cal.setTime(date);  
	     // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
	     int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天  
	     if (1 == dayWeek) {  
	        cal.add(Calendar.DAY_OF_MONTH, -1);  
	     }  
	     cal.setFirstDayOfWeek(Calendar.MONDAY);  
	     int day = cal.get(Calendar.DAY_OF_WEEK);  
	     cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);  
	     weekMap.put("weekFirst", cal.getTime());
	     cal.add(Calendar.DATE, 6);  
	     weekMap.put("weekEnd", cal.getTime());
	     return weekMap;
	}  
}
