package net.zdsoft.openapi.remote.openapi.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.zdsoft.base.dto.InterfaceCountDto;
import net.zdsoft.base.entity.eis.Developer;
import net.zdsoft.base.entity.eis.OpenApiInterface;
import net.zdsoft.base.entity.eis.OpenApiInterfaceCount;
import net.zdsoft.base.enums.ApplyStatusEnum;
import net.zdsoft.base.service.DeveloperService;
import net.zdsoft.base.service.OpenApiApplyService;
import net.zdsoft.base.service.OpenApiInterfaceCountService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.openapi.remote.openapi.constant.OpenApiConstants;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author yangsj  2017年12月28日上午9:51:34
 * 统计接口的数量
 */
@Controller
@RequestMapping("/developer/interface/count/")
public class OpenapiInterfaceCount extends OpenApiBaseAction {

	@Resource
    private OpenApiApplyService applyService;
	@Autowired
	private OpenApiInterfaceCountService openApiInterfaceCountService;
	@Autowired
	private DeveloperService developerService;
	
	@RequestMapping("/index")
    public String interfaceManageIndex(ModelMap map) {
        return "/openapi/system/interfaceCount/inteCountTab.ftl";
    }
	
	@ControllerInfo(value="报表展示")
	@RequestMapping("/interfaceCall/page")
	public String interfaceCallList(Param param, ModelMap map) throws Exception {
		Date start = DateUtils.parseDate(param.getStart(), "yyyy-MM-dd");
        Date end   = DateUtils.parseDate(param.getEnd(), "yyyy-MM-dd");	
        //查找
  		Developer developer = null;
  		if(StringUtils.isNotBlank(param.getDeveloperId())){
  			developer = developerService.findOne(param.getDeveloperId());
  		}
  		end = net.zdsoft.framework.utils.DateUtils.getNextDay(end);
  		String ticketKey = developer == null ? null : developer.getTicketKey();
  		Pagination page = createPagination();
  		List<OpenApiInterfaceCount> operationLogs = openApiInterfaceCountService.
  				findDoInterfaceNumAndPage(ticketKey,param.getInterfaceType(),start,end,page);
  		List<InterfaceCountDto> dtos = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(operationLogs)){
			Set<String> urlSet = EntityUtils.getSet(operationLogs, OpenApiInterfaceCount::getInterfaceId);
			List<OpenApiInterface> openApiInterfaceList = openApiInterfaceService.findByUriIn(urlSet.toArray(new String[urlSet.size()]));
			Map<String, OpenApiInterface> uriMap = EntityUtils.getMap(openApiInterfaceList, OpenApiInterface::getUri);
			Map<String, String> developerNameMap = Maps.newHashMap();
			if(StringUtils.isBlank(ticketKey)){
				List<Developer> allDevelopers = developerService.findAll();
//				developerNameMap = EntityUtils.getMap(allDevelopers, Developer::getTicketKey, Developer::getUnitName);
			}else{
//				developerNameMap.put(developer.getTicketKey(), developer.getUnitName());
			}
			for (OpenApiInterfaceCount openApiInterfaceCount : operationLogs) {
				InterfaceCountDto dto = new InterfaceCountDto();
				String ticketKey1 = openApiInterfaceCount.getTicketKey();
				String uri = null;
				if(MapUtils.isNotEmpty(developerNameMap) && StringUtils.isNotBlank(developerNameMap.get(ticketKey1))){
					dto.setDeveloperName(developerNameMap.get(ticketKey1));
					if(MapUtils.isNotEmpty(uriMap) && uriMap.get(uri) != null){
						dto.setMethod(uriMap.get(uri).getMethodType());
						dto.setInterfaceName(uriMap.get(uri).getDescription());
					}
					dto.setType(openApiInterfaceCount.getType());
					dto.setCreationTime(net.zdsoft.framework.utils.DateUtils.date2StringBySecond(openApiInterfaceCount.getCreationTime()));
					dto.setId(openApiInterfaceCount.getId());
					dtos.add(dto);
				}
			}
		}
  		map.put("pagination", page);
  		map.put("interfaceCountDtos", dtos);
  		return "/openapi/system/interfaceCount/list/interfaceCallList.ftl";
	}
	
	
	@RequestMapping("/showOpenapiCount/page")
	public String showOpenapiCount(ModelMap map, String isBackstage, String type) {
		//默认取前一周的接口次数
		String endTime= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(
				net.zdsoft.framework.utils.DateUtils.addDay(new Date(), -7));
		Developer developer = getDeveloper();
		List<String> types = null;
        if (null != developer) {
            types = applyService.getTypes(ApplyStatusEnum.PASS_VERIFY.getValue(), developer.getId());
            map.put("developerId", developer.getId());
            map.put("ticketKey", developer.getTicketKey());
        }
		map.put("openApiApplys", getTypes(types));
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("isBackstage", "true".equals(isBackstage));
		if(StringUtils.isNotBlank(type) && "2".equals(type)){
			return "/openapi/system/interfaceCount/list/interfaceCallIndex.ftl";
		}else{
			return "/openapi/interfaceCount/showInterface.ftl";
		}
	}
	
	@ResponseBody
    @RequestMapping("/showDeveloperList")
    public String showSortDeveloperList(ModelMap map,String type) {
		List<Developer> allDevelopers = getSortDeveloperByCountNum(type);
		JSONArray jsonArray = new JSONArray();
		for(Developer developer:allDevelopers){
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id",developer.getId());
			jsonArray.add(jsonObject);
		}
		return jsonArray.toJSONString();
    }
	
	@ControllerInfo(value="显示openapi调用接口次数")
	@RequestMapping("/changeOpenapiCount/page")
	public String changeShowOpenapiCount(Param param, ModelMap map) throws Exception {
		Integer[][] loadingDataInt = null;
		String showType = param.getShowType();
		Date start = DateUtils.parseDate(param.getStart(), "yyyy-MM-dd");
        Date end   = DateUtils.parseDate(param.getEnd(), "yyyy-MM-dd");	
		if(StringUtils.isBlank(showType)) {
			showType = OpenApiConstants.SHOW_INTERCE_DAY;
		}
		String showFormat = getShowFormat(showType);
		String[] xAxisDatas = getXaxisDatas(start, end, showType, showFormat);
		loadingDataInt = new Integer[1][xAxisDatas.length];
		//查找
		Developer developer = null;
		if(StringUtils.isNotBlank(param.getDeveloperId())){
			developer = developerService.findOne(param.getDeveloperId());
		}else if("false".equals(param.getIsBackstage())){
		    developer = getDeveloper();
		}
		end = net.zdsoft.framework.utils.DateUtils.getNextDay(end);
		String ticketKey = developer == null ? null : developer.getTicketKey();
	    List<OpenApiInterfaceCount> operationLogs = openApiInterfaceCountService.findDoInterfaceNum(ticketKey,param.getInterfaceType(),start,end);
		//查找出当前接口和调用的日志信息
		if(CollectionUtils.isNotEmpty(operationLogs)) {
			for (int j = 0; j < xAxisDatas.length; j++) {
				List<OpenApiInterfaceCount> operationLogs1 = getUseOpenapiCount(xAxisDatas, operationLogs, j,start,end,showFormat);
				int num = operationLogs1.size();
				loadingDataInt[0][j] = num;
			}
		}else {
			for (int j = 0; j < xAxisDatas.length; j++) {
				int num = 0;
				loadingDataInt[0][j] = num;
			}
		}
		return setShowParam(map, loadingDataInt, xAxisDatas);
	}

	
	public class Param{
		private String developerId;
		private String start;
		private String end;
		private String showType;
		private String interfaceType;
		private String isBackstage;  //是否是后台查询
		public String getDeveloperId() {
			return developerId;
		}
		public void setDeveloperId(String developerId) {
			this.developerId = developerId;
		}
		public String getStart() {
			return start;
		}
		public void setStart(String start) {
			this.start = start;
		}
		public String getEnd() {
			return end;
		}
		public void setEnd(String end) {
			this.end = end;
		}
		public String getShowType() {
			return showType;
		}
		public void setShowType(String showType) {
			this.showType = showType;
		}
		public String getInterfaceType() {
			return interfaceType;
		}
		public void setInterfaceType(String interfaceType) {
			this.interfaceType = interfaceType;
		}
		public String getIsBackstage() {
			return isBackstage;
		}
		public void setIsBackstage(String isBackstage) {
			this.isBackstage = isBackstage;
		}
	}
	
	//TODO ----------------------------------------私有方法区 -------------------------------------------
	/**
	 * 降序排序 
	 * @param type
	 * @return
	 */
	private List<Developer> getSortDeveloperByCountNum(String type) {
		List<Developer> allDevelopers = developerService.findAll();
		Map<String,Integer>  developerCountMap = openApiInterfaceCountService.findCountByType(type);
		allDevelopers.forEach(d->{
			long countNum = 0;
			if(developerCountMap != null){
				countNum = developerCountMap.get(d.getTicketKey()) == null? 0 : developerCountMap.get(d.getTicketKey());
			}
			d.setCountNum(countNum);
		});
		allDevelopers.sort(Comparator.comparingLong(Developer::getCountNum).reversed());
		return allDevelopers;
	}
	
	/**
	 * @param showType
	 * @return
	 */
	private String getShowFormat(String showType) {
		String showFormat = "yyyy-MM-dd";
		if(showType.equals(OpenApiConstants.SHOW_INTERCE_MONTH)) {
        	showFormat = "yyyy-MM";
        }else if(showType.equals(OpenApiConstants.SHOW_INTERCE_WEEK)){
        	showFormat = "yyyy-MM-dd -- yyyy-MM-dd";
        }
		return showFormat;
	}

	/**
	 * @param start
	 * @param end
	 * @param showType
	 * @param showFormat
	 * @return
	 */
	private String[] getXaxisDatas(Date start, Date end, String showType, String showFormat) {
		String[] xAxisDatas = null;
	        try {
	            Calendar c = Calendar.getInstance();
	            //判断两个日期相差多少，超过35 天的用周表示，超过98月的用月份表示，超过365天的用年表示
	            Integer showNum;
	            Integer showDateForm;
	            int days = (int) ((end.getTime() - start.getTime()) / (1000*3600*24));
	            if(showType.equals(OpenApiConstants.SHOW_INTERCE_WEEK)){
	            	if(days<=7) {
	            		xAxisDatas = new String[1];
	            		xAxisDatas[0] = new SimpleDateFormat("yyyy-MM-dd").format(start) + "--" + new SimpleDateFormat("yyyy-MM-dd").format(end);
	            	}else {
	            		int n = days/7 ;
	            		if(days % 7 == 0) {
	            			xAxisDatas = new String[n];
	            		}else{
	            			xAxisDatas = new String[n+1];
	            		}
	            		Date startDate = start;
	            		Date endDate = null;
	            		Date mindDate = null;
	            		for(int i=0;i<n;i++) {
	            			if(i == 0) {
	            				mindDate = startDate;
	            			}else {
	            				mindDate = endDate;
	            			}
	            			c.setTime(mindDate);
            				c.add(Calendar.DAY_OF_MONTH, +7);
            				endDate = c.getTime();
            				String s1 = new SimpleDateFormat("yyyy-MM-dd").format(mindDate);
            				String e1 = new SimpleDateFormat("yyyy-MM-dd").format(endDate);
            				xAxisDatas[i] = s1 + "--" + e1;
	            		}
	            		if(days % 7 > 0) {
	            			xAxisDatas[n] = new SimpleDateFormat("yyyy-MM-dd").format(endDate) + "--" + new SimpleDateFormat("yyyy-MM-dd").format(end);
	            		}
	               }
	            }else {
	            	//把相差的天数12等分
	            	c.setTime(start);
	            	int year1 = c.get(Calendar.YEAR);
	            	c.setTime(end);
	            	int year2 = c.get(Calendar.YEAR);
	            	if(showType.equals(OpenApiConstants.SHOW_INTERCE_MONTH)) {
	            		c.setTime(start);
	            		int month1 = c.get(Calendar.MONTH);
	            		c.setTime(end);
	            		int month2 = c.get(Calendar.MONTH);
	            		showNum = year1 == year2 ? month2 - month1: month2 - month1+12;
	            		showDateForm = Calendar.MONTH;
	            	}else{
	            		showNum = days;
	            		showDateForm = Calendar.DATE;
	            	}
	            	xAxisDatas = new String[showNum+1];
	            	xAxisDatas[0] = new SimpleDateFormat(showFormat).format(start);
	            	for (int i = 1; i < xAxisDatas.length-1; i++) {
	            		c.setTime(new SimpleDateFormat(showFormat).parse(xAxisDatas[i-1]));
	            		int num1=c.get(showDateForm); 
	            		c.set(showDateForm,num1+1); 
	            		String dayAfter=new SimpleDateFormat(showFormat).format(c.getTime());
	            		xAxisDatas[i] = dayAfter;
	            	}
	            	xAxisDatas[xAxisDatas.length-1] = new SimpleDateFormat(showFormat).format(end);
	            }
	        }catch (Exception e) {
			}
		return xAxisDatas;
	}

	/**
	 * @param map
	 * @param jsonObject
	 * @param legendDatas
	 * @param loadingDataInt
	 * @param xAxisDatas
	 * @return
	 */
	private String setShowParam(ModelMap map, Integer[][] loadingDataInt,
			String[] xAxisDatas) {
		String[] legendDatas = new String[] { "接口调用次数" };
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("xAxisData", xAxisDatas);
		jsonObject.put("legendData", legendDatas);
		// loadingData数据顺序和xAxisData相对
		jsonObject.put("loadingData", loadingDataInt);
		JSONObject map1 = new JSONObject();
		map1.put("jsonData", jsonObject);
		map1.put("col", 12);
		map1.put("title", "接口统计图");
		JSONObject json = JSONObject.parseObject( map1.toJSONString());
		if (json.containsKey("jsonData")) {
			String o = JSONUtils.toJSONString(json.get("jsonData"));
			json.put("jsonData", o);
		}
		map.put("data", json);
		return "/openapi/interfaceCount/showCount.ftl";
	}
	
	/**
	 * @param xAxisDatas
	 * @param operationLogs
	 * @param j
	 * @return
	 */
	private List<OpenApiInterfaceCount> getUseOpenapiCount(String[] xAxisDatas, List<OpenApiInterfaceCount> operationLogs, int j,Date start,Date end, String showFormat) {
		operationLogs = EntityUtils.filter2(operationLogs, t->{
			try {
				Date o1Date;
				Date o2Date;
				if(j == 0) {
					o1Date = start;
				}else {
					if(showFormat.equals("yyyy-MM-dd -- yyyy-MM-dd")) {
						String date = xAxisDatas[j].substring(0, 10);
						o1Date = DateUtils.parseDate(
								date, "yyyy-MM-dd");
					}else {
						o1Date = DateUtils.parseDate(
								xAxisDatas[j], showFormat);
					}
				}
				if(xAxisDatas == null || j == (xAxisDatas.length-1)) {
					o2Date = net.zdsoft.framework.utils.DateUtils.getNextDay(end);
				}else {
					if(showFormat.equals("yyyy-MM-dd -- yyyy-MM-dd")) {
						String date = xAxisDatas[j].substring(12);
						o2Date = DateUtils.parseDate(
								date, "yyyy-MM-dd");
					}else {
						o2Date = DateUtils.parseDate(
								xAxisDatas[j+1], showFormat);
					}
				}
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateString = df.format(t.getCreationTime());
				Date logTime = df.parse(dateString);
				return  (o1Date.before(logTime) && o2Date.after(logTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return true;
	    });	
		return operationLogs;
	}
}
