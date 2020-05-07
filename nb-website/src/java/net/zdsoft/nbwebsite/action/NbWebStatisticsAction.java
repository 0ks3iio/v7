package net.zdsoft.nbwebsite.action;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.CustomRoleUserRemoteService;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.SortUtils;
import net.zdsoft.nbwebsite.constant.WebsiteConstants;
import net.zdsoft.nbwebsite.constant.WebsiteConstants.Type;
import net.zdsoft.nbwebsite.dto.WebArticleDto;
import net.zdsoft.nbwebsite.entity.WebArticle;
import net.zdsoft.nbwebsite.remote.service.WebArticleRemoteService;
import net.zdsoft.nbwebsite.service.CommonService;
import net.zdsoft.nbwebsite.service.WebArticleService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @description: 统计发布的数量
 * @author: yangsj
 * 
 * @date: 2017-2-10
 */
@Controller
@RequestMapping("/sitedata")
public class NbWebStatisticsAction extends BaseAction {

	@Autowired
	private RegionRemoteService regionRemoteService;
	@Autowired
	private CustomRoleUserRemoteService customRoleUserService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private WebArticleRemoteService webArticleRemoteService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private CustomRoleUserRemoteService customRoleUserRemoteService;

	@Autowired
	private WebArticleService webArticleService;

	@Autowired
	private McodeRemoteService mcodeRemoteService;

	@RequestMapping("/statistics/index/page")
	public String index(ModelMap map,
			@RequestParam(required = false) String container) {
		List<Unit> uList = null;
		String commitUnitId = getLoginInfo().getUnitId();
		Unit unit = SUtils.dc(unitRemoteService.findById(commitUnitId),
				Unit.class);
		String regionCode = unit.getRegionCode();
		Region region = SUtils.dc(
				regionRemoteService.findRegionsByFullCode(regionCode),
				Region.class);
		String regionCodeString = region.getRegionCode();
		uList = SUtils.dt(
				unitRemoteService.findByRegionCode(regionCodeString + "%"),
				new TR<List<Unit>>() {
				});

		map.put("uList", uList);
		map.put("type", "1");
		map.put("manager", isManager());
		map.put("container", container);
		return "/nbsitedata/webbackground/statistics/index1.ftl";
	}

	// User details
	@RequestMapping("/statistics/userdetail/page")
	public String userdetail(ModelMap map,
			@RequestParam(required = false) String container) {
		// 获得文章类型
		List<McodeDetail> models = getMcodeDetails();
		String unitIds = getLoginInfo().getUnitId();
		List<User> users = SUtils.dt(userRemoteService.findByUnitIds(unitIds),
				new TR<List<User>>() {
				});
		List<User> user1 = new ArrayList<User>();
		for (User user : users) {
			if (customRoleUserService.containRole(user.getId(),
					WebsiteConstants.MANAGER_ROLE_CODE)) {
				user1.add(user);
			}
		}
		map.put("userManager", user1);
		map.put("models", models);
		map.put("type", "1");
		map.put("manager", isManager());
		map.put("container", container);
		return "/nbsitedata/webbackground/statistics/userdetail.ftl";
	}

	@ResponseBody
	@RequestMapping("/statistics/unitLists/page")
	public String unitLists(ModelMap map,
			@RequestParam(required = false) String container, String unitName) {
		List<Unit> uList2 = null;
		String commitUnitId = getLoginInfo().getUnitId();
		Unit unit = SUtils.dc(unitRemoteService.findById(commitUnitId),
				Unit.class);
		String regionCode = unit.getRegionCode();
		Region region = SUtils.dc(
				regionRemoteService.findRegionsByFullCode(regionCode),
				Region.class);
		String regionCodeString = region.getRegionCode();
		if (unitName == null || StringUtils.isBlank(unitName)) {
			uList2 = SUtils.dt(
					unitRemoteService.findByRegionCode(regionCodeString + "%"),
					new TR<List<Unit>>() {
					});
		} else {
			uList2 = SUtils.dt(
					unitRemoteService.findByRegionCodeUnitName("%" + unitName
							+ "%", regionCodeString + "%"),
					new TR<List<Unit>>() {
					});
		}

		if (CollectionUtils.size(uList2) > 0) {

			return JSON.toJSONString(uList2);
		} else {
			return error("没有该单位");
		}
	}

	@RequestMapping("/statistics/list/page")
	public String doLoadList(@RequestParam(required = false) String state,
			@RequestParam String container,
			@RequestParam(required = false) String title,
			@RequestParam(required = false) Date startDate,
			@RequestParam(required = false) Date endDate,
			@RequestParam(required = false) String commitUnitId,
			@RequestParam(required = false) String unitName, ModelMap map) {
		if( StringUtils.isNotBlank(commitUnitId)){
			
			unitName=StringUtils.EMPTY;
		}
		
		
		
		if (commitUnitId == null || StringUtils.isBlank(commitUnitId)
				|| commitUnitId.equals("1")
				|| commitUnitId.equalsIgnoreCase("undefined")) {
			commitUnitId = getLoginInfo().getUnitId();
		}
		if (endDate != null) {
			endDate = DateUtils.addDays(endDate, 1);
		}
		if (endDate == null && startDate != null) {
			endDate = new Date();
		}
		if (startDate == null && endDate != null) {
			startDate = DateUtils.addYears(endDate, -100);

		}

		List<WebArticleDto> dtos1 = new ArrayList<WebArticleDto>();
		List<McodeDetail> models = null;
		Pagination page = createPagination();
		List<WebArticle> list = new ArrayList<WebArticle>();
		List<Map<String, String>> listWebArticles = null;
		boolean personalDetails = false;
		boolean unitDetails = false;
		boolean schoolDetails = false;
		boolean userPosting = false;
		// 单位列表级联管理
		// if(commitUnitId==null){
		// commitUnitId=getLoginInfo().getUnitId();
		// }
		// Unit unit=SUtils.dc(unitRemoteService.findById(commitUnitId),
		// Unit.class);
		// Integer unitClassInteger = null;
		// if(new Integer(Unit.UNIT_REGION_CITY).equals(unit.getRegionLevel())){
		// unitClassInteger = new Integer(Unit.UNIT_CLASS_EDU);
		// }
		// else if (new
		// Integer(Unit.UNIT_REGION_COUNTY).equals(unit.getRegionLevel())) {
		// unitClassInteger = new Integer(Unit.UNIT_CLASS_SCHOOL);
		// }
		// List<Unit>
		// units1=SUtils.dt(unitRemoteService.findDirectUnits(commitUnitId,
		// unitClassInteger),new TR<List<Unit>>(){});

		// 下属单位发布总数
		if (StringUtils.equals("unit", state)) {
			WebArticle webArticle = findUnitRelease(commitUnitId, startDate,
					endDate);
			list.add(webArticle);
			unitDetails = true;
		}
		// 下属单位明细
		if (StringUtils.equals("unitDetails", state)) {
			Unit unit = SUtils.dc(unitRemoteService.findById(commitUnitId),
					Unit.class);
			if (new Integer(Unit.UNIT_CLASS_SCHOOL).equals(unit.getUnitClass())) {
				models = getMcodeDetails();
				schoolDetails = true;
			} else {
				unitDetails = true;
			}
			list = findPersonRelease(commitUnitId, page, startDate, endDate,
					unitName);

		}
		// 个人明细
		if (StringUtils.equals("personalDetails", state)) {
			models = getMcodeDetails();
			if (isManager()) {
				listWebArticles = findPersonReleaseDetails(commitUnitId,
						startDate, endDate, page);
			} else {
				listWebArticles = findPersonTotal(getLoginInfo().getUserId(),
						startDate, endDate);

			}
			personalDetails = true;

		}
		map.put("mapList", listWebArticles);
		map.put("listWebArticle", list);
		map.put("schoolDetails", schoolDetails);
		map.put("unitDetails", unitDetails);
		map.put("userPosting", userPosting);
		map.put("personalDetails", personalDetails);
		map.put("models", models);
		map.put("dtos", dtos1);
		map.put("commitState", WebsiteConstants.STATE_UNCOMMIT);
		map.put("manager", isManager());
		map.put("pagination", page);
		map.put("type", "1");
		map.put("container", container);
		return "/nbsitedata/webbackground/statistics/list.ftl";
	}

	/**
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	// isManager=false 查出的个人发布总量
	private List<Map<String, String>> findPersonTotal(String userId,
			Date startDate, Date endDate) {
		List<Map<String, String>> listWebArticles = new ArrayList<Map<String, String>>();
		List<McodeDetail> mocodeDetails = getMcodeDetails();
		User user = SUtils.dc(userRemoteService.findById(userId), User.class);
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("userName", user.getRealName());
		int n = 0;
		for (McodeDetail mcodeDetail : mocodeDetails) {
			// totalRelease =
			// webArticleRemoteService.findPersonReleaseByType(userId,'0'+String.valueOf(i));
			String totalRelease = null;
			if (startDate != null) {
				totalRelease = webArticleRemoteService
						.findPersonReleaseByTypeDate(user.getId(),
								mcodeDetail.getThisId(), startDate,
								DateUtils.addDays(endDate, 1));
			} else {
				totalRelease = webArticleRemoteService.findPersonReleaseByType(
						user.getId(), mcodeDetail.getThisId());
			}
			map.put(mcodeDetail.getThisId(), totalRelease);
			n += (Integer.valueOf(totalRelease));
		}

		map.put("count", String.valueOf(n));
		listWebArticles.add(map);
		return listWebArticles;

	}

	@RequestMapping("/statistics/userPosting/list/page")
	public String doUserPostingList(
			@RequestParam(required = false) String state,
			@RequestParam(required = false) String type,
			@RequestParam(required = false) String auditUserId,
			@RequestParam(required = false) String commitState,
			@RequestParam(required = false) String title,
			@RequestParam(required = false) Date startDate,
			@RequestParam(required = false) Date endDate,
			@RequestParam(required = false) String commitUnitId, ModelMap map) {
		if (commitUnitId == null || StringUtils.isBlank(commitUnitId)) {
			commitUnitId = getLoginInfo().getUnitId();
		}
		List<WebArticleDto> dtos1 = new ArrayList<WebArticleDto>();
		List<McodeDetail> models = null;
		Pagination page = createPagination();
		List<WebArticle> list = new ArrayList<WebArticle>();
		List<Map<String, String>> listWebArticles = null;
		boolean personalDetails = false;
		boolean unitDetails = false;
		boolean schoolDetails = false;
		boolean userPosting = true;
		list = webArticleRemoteService.findArticles(getLoginInfo().getUserId(),
				title, type, auditUserId, commitState, startDate, endDate,
				false, page);
		map.put("mapList", listWebArticles);
		map.put("listWebArticle", list);
		map.put("schoolDetails", schoolDetails);
		map.put("unitDetails", unitDetails);
		map.put("userPosting", userPosting);
		map.put("personalDetails", personalDetails);
		map.put("models", models);
		map.put("dtos", dtos1);
		map.put("commitState", WebsiteConstants.STATE_UNCOMMIT);
		map.put("manager", isManager());
		map.put("pagination", page);
		map.put("type", "1");
		map.put("container", "a");
		return "/nbsitedata/webbackground/statistics/list.ftl";
	}

	/**
	 * @return
	 */
	// 查找出个人明细
	private List<Map<String, String>> findPersonReleaseDetails(String unitIds,
			Date startDate, Date endDate, Pagination page) {
		// TODO Auto-generated method stub

		List<Map<String, String>> listWebArticles = new ArrayList<Map<String, String>>();
		// 进行分页显示
		// userRemoteService.findByUnitIds(unitIds,page)

		JSONObject jsonObject = JSON.parseObject(userRemoteService.findByUnitId(unitIds, JSON.toJSONString(page)));
		List<User> users = SUtils.dt(jsonObject.getString("data"),new TypeReference<List<User>>() {});
		Long maxRowCount = SUtils.dc(jsonObject.getString("count"),Long.class);
		
		page.setMaxRowCount(maxRowCount.intValue());
		page.initialize();

		//
		// List<User> users =
		// SUtils.dt(userRemoteService.findByUnitIds(unitIds),
		// new TR<List<User>>() {
		// });

		List<User> user1 = new ArrayList<User>();
		List<User> user2 = new ArrayList<User>();
		for (User user : users) {
			if (customRoleUserService.containRole(user.getId(),
					WebsiteConstants.MANAGER_ROLE_CODE)) {
				user1.add(user);
			} else {
				user2.add(user);
			}
		}

		user1.addAll(user2);

		// List<User> user3= user1.addAll(0, user2);
		// List<User> user3 = ((List<User>) user1).Concat(user2).ToList();

		List<McodeDetail> mocodeDetails = getMcodeDetails();
		List<UserCount> userCounts = Lists.newArrayList();
		for (User user : user1) {
			Map<String, String> map = new LinkedHashMap<String, String>();
			int n = 0;
			UserCount userCount = new UserCount();
			userCount.setUser(user);
			for (McodeDetail mcodeDetail : mocodeDetails) {
				// totalRelease =
				// webArticleRemoteService.findPersonReleaseByType(userId,'0'+String.valueOf(i));
				String totalRelease = null;
				if (startDate != null) {
					totalRelease = webArticleRemoteService
							.findPersonReleaseByTypeDate(user.getId(),
									mcodeDetail.getThisId(), startDate, endDate);
				} else {
					totalRelease = webArticleRemoteService
							.findPersonReleaseByType(user.getId(),
									mcodeDetail.getThisId());
				}

				map.put(mcodeDetail.getThisId(), totalRelease);
				n += (Integer.valueOf(totalRelease));
			}
			userCount.setCount(new Integer(n));
			userCounts.add(userCount);
			userCount.setDetail(map);
			// listWebArticles.add(map);
		}
		SortUtils.DESC(userCounts, "count");
		// Map.put
		for (UserCount userCount : userCounts) {
			Map<String, String> maps = Maps.newLinkedHashMap();
			maps.put("userName", userCount.getUser().getRealName());
			for (Entry<String, String> set : userCount.getDetail().entrySet()) {
				maps.put(set.getKey(), set.getValue());
			}
			maps.put("count", String.valueOf(userCount.getCount()));
			listWebArticles.add(maps);
		}

		return listWebArticles;
	}

	public class UserCount {
		public User user;
		public Integer count;
		public Map<String, String> detail = new LinkedHashMap<String, String>();

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public Integer getCount() {
			return count;
		}

		public void setCount(Integer count) {
			this.count = count;
		}

		public Map<String, String> getDetail() {
			return detail;
		}

		public void setDetail(Map<String, String> detail) {
			this.detail = detail;
		}

	}
	
	/**
	 * 是否是审核员
	 * 
	 * @return
	 */
	protected boolean isManager() {
		boolean isManager = customRoleUserRemoteService.containRole(
				getLoginInfo().getUserId(), WebsiteConstants.MANAGER_ROLE_CODE);
		return isManager;
	}

	// 查找单个单位数量（发布数量，通过数量，点击数量）
	private WebArticle findUnitRelease(String commitUnitId, Date startDate,
			Date endDate) {
		// TODO Auto-generated method stub

		if (StringUtils.isNotBlank(commitUnitId)) {
			Unit unit = SUtils.dc(unitRemoteService.findById(commitUnitId),
					Unit.class);
			String regionCode = unit.getRegionCode();
			Region region = SUtils.dt(
					regionRemoteService.findRegionsByFullCode(regionCode),
					new TR<Region>() {
					});
			String regionCodeString = region.getRegionCode();
			List<Unit> uList = SUtils.dt(
					unitRemoteService.findByRegionCode(regionCodeString + "%"),
					new TR<List<Unit>>() {
					});
			List<String> unitIdsList = new ArrayList<String>();
			for (Unit unit2 : uList) {
				unitIdsList.add(unit2.getId());
			}
			WebArticle webArticle = new WebArticle();
			String adoptRelease = null;
			String totalRelease = null;
			String clicksRelease = null;
			if (startDate != null) {
				int sum = unitIdsList.size();
				int n = sum % 1000 == 0 ? 0 : (int) Math.ceil(sum / 1000) + 1;
				if (n == 0) {
					adoptRelease = webArticleService.findAllAdoptReleaseByDate(
							unitIdsList, startDate, endDate);
					totalRelease = webArticleService.findAllReleaseByDate(
							unitIdsList, startDate, endDate);
					clicksRelease = webArticleService
							.findAllClickReleaseByDate(unitIdsList, startDate,
									endDate);
				} else {
					int num1 = 0;
					int num2 = 0;
					int num3 = 0;
					for (int j = 0; j < n; j++) {
						List<String> list1 = unitIdsList.subList(j * 1000,
								(j + 1) * 1000 > sum ? sum : (j + 1) * 1000);
						num1 += Integer
								.valueOf(webArticleService
										.findAllReleaseByDate(list1, startDate,
												endDate));
						num2 += Integer.valueOf(webArticleService
								.findAllAdoptReleaseByDate(list1, startDate,
										endDate));
						num3 += Integer.valueOf(webArticleService
								.findAllClickReleaseByDate(list1, startDate,
										endDate));
					}
					// List<String> list1 =unitIdsList.subList(n*1000,
					// unitIdsList.size()+1);
					// int n1= num1 +
					// (Integer.valueOf(webArticleService.findAllReleaseByDate(list1,startDate,endDate)));
					// int n2= num2 +
					// (Integer.valueOf(webArticleService.findAllAdoptReleaseByDate(list1,startDate,endDate)));
					// int n3= num3 +
					// (Integer.valueOf(webArticleService.findAllClickReleaseByDate(list1,startDate,endDate)));
					totalRelease = String.valueOf(num1);
					adoptRelease = String.valueOf(num2);
					clicksRelease = String.valueOf(num3);
				}
			} else {
				int sum = unitIdsList.size();

				int n = sum % 1000 == 0 ? 0 : (int) Math.ceil(sum / 1000) + 1;

				if (n == 0) {
					totalRelease = webArticleService
							.findAllRelease(unitIdsList);
					adoptRelease = webArticleService
							.findAllAdoptRelease(unitIdsList);
					clicksRelease = webArticleService
							.findAllClickRelease(unitIdsList);
				} else {
					int num1 = 0;
					int num2 = 0;
					int num3 = 0;
					for (int j = 0; j < n; j++) {
						List<String> list1 = unitIdsList.subList(j * 1000,
								(j + 1) * 1000 > sum ? sum : (j + 1) * 1000);
						num1 += Integer.valueOf(webArticleService
								.findAllRelease(list1));
						num2 += Integer.valueOf(webArticleService
								.findAllAdoptRelease(list1));
						num3 += Integer.valueOf(webArticleService
								.findAllClickRelease(list1));
					}
					// List<String> list1 =unitIdsList.subList(n*1000,
					// unitIdsList.size()+1);
					// int n1= num1 +
					// (Integer.valueOf(webArticleService.findAllRelease(list1)));
					// int n2= num2 +
					// (Integer.valueOf(webArticleService.findAllAdoptRelease(list1)));
					// int n3= num3 +
					// (Integer.valueOf(webArticleService.findAllClickRelease(list1)));
					totalRelease = String.valueOf(num1);
					adoptRelease = String.valueOf(num2);
					clicksRelease = String.valueOf(num3);
				}
			}
			// if(startDate!=null){
			// adoptRelease=webArticleRemoteService.findAdoptReleaseByUnitIdDate(commitUnitId,startDate,endDate);
			// totalRelease =
			// webArticleRemoteService.findReleaseByUnitIdDate(commitUnitId,startDate,endDate);
			// clicksRelease =
			// webArticleRemoteService.findClickReleaseByUnitIdDate(commitUnitId,startDate,endDate);
			// }else{
			// adoptRelease =
			// webArticleRemoteService.findAdoptReleaseByUnitId(commitUnitId);
			// totalRelease =
			// webArticleRemoteService.findAllReleaseByUnitId(commitUnitId);
			// clicksRelease =
			// webArticleRemoteService.findClickReleaseByUnitId(commitUnitId);
			// }
			webArticle.setClicksRelease(clicksRelease);
			webArticle.setAdoptRelease(adoptRelease);
			webArticle.setTotalRelease(totalRelease);
			Unit u1 = SUtils.dc(unitRemoteService.findById(commitUnitId),
					Unit.class);
			webArticle.setUnitName(u1.getUnitName());

			return webArticle;
		} else {
			return null;
		}
	}

	// 查找个人数量（发布数量，通过数量，点击数量）
	private List<WebArticle> findPersonRelease(String commitUnitId,
			Pagination page, Date startDate, Date endDate, String unitName) {
		// TODO Auto-generated method stub
		List<WebArticle> listWebArticles = new ArrayList<WebArticle>();
		List<Unit> units = new ArrayList<Unit>();
		if (StringUtils.isNotBlank(unitName)) {
			units = SUtils.dt(unitLists(null, "", unitName),
					new TR<List<Unit>>() {
					});
		}
		if (units.size() > 1) {
			for (Unit unit : units) {
				WebArticle webArticle = new WebArticle();
				String adoptRelease = null;
				String totalRelease = null;
				String clicksRelease = null;
				if (endDate != null) {
					adoptRelease = webArticleRemoteService
							.findAdoptReleaseByUnitIdDate(unit.getId(), startDate,
									endDate);
					totalRelease = webArticleRemoteService
							.findReleaseByUnitIdDate(unit.getId(), startDate, endDate);
					clicksRelease = webArticleRemoteService
							.findClickReleaseByUnitIdDate(unit.getId(), startDate,
									endDate);
				} else {
					adoptRelease = webArticleRemoteService
							.findAdoptReleaseByUnitId(unit.getId());
					totalRelease = webArticleRemoteService
							.findAllReleaseByUnitId(unit.getId());
					clicksRelease = webArticleRemoteService
							.findClickReleaseByUnitId(unit.getId());
				}
		        webArticle.setClicksRelease(clicksRelease);
				webArticle.setAdoptRelease(adoptRelease);
				webArticle.setTotalRelease(totalRelease);				
				webArticle.setUnitName(unit.getUnitName());
				listWebArticles.add(webArticle);
			}
			return listWebArticles;

		} else {
			Unit unit = SUtils.dc(unitRemoteService.findById(commitUnitId),
					Unit.class);
			if (new Integer(Unit.UNIT_CLASS_SCHOOL).equals(unit.getUnitClass())) {
				List<McodeDetail> mocodeDetails = getMcodeDetails();
				int n = 0;
				for (McodeDetail mcodeDetail : mocodeDetails) {
					WebArticle webArticle = new WebArticle();
					String totalRelease = null;
					if (startDate != null) {
						totalRelease = webArticleRemoteService
								.findReleaseByTypeDate(commitUnitId,
										mcodeDetail.getThisId(), startDate,
										endDate);
					} else {
						totalRelease = webArticleRemoteService
								.findAllReleaseByUnitIdType(commitUnitId,
										mcodeDetail.getThisId());
					}
					webArticle.setTotalRelease(totalRelease);
					Unit u1 = SUtils.dc(
							unitRemoteService.findById(commitUnitId),
							Unit.class);
					webArticle.setUnitName(u1.getUnitName());
					n += (Integer.valueOf(totalRelease));
					listWebArticles.add(webArticle);
				}
				listWebArticles.get(0).setAllRelease(String.valueOf(n));
				return listWebArticles;
			} else {
				String regionCode = unit.getRegionCode();
				Region region = SUtils.dt(
						regionRemoteService.findRegionsByFullCode(regionCode),
						new TR<Region>() {
						});
				String regionCodeString = region.getRegionCode();
				List<Unit> uList = SUtils.dt(unitRemoteService
						.findByUnitClassAndRegion(0, regionCodeString + "%",
								page), new TR<List<Unit>>() {
				});
				List<String> unitIdsList = new ArrayList<String>();
				for (Unit unit2 : uList) {
					unitIdsList.add(unit2.getId());
				}
				for (String id : unitIdsList) {
					WebArticle webArticle = new WebArticle();
					String adoptRelease = null;
					String totalRelease = null;
					String clicksRelease = null;
					if (endDate != null) {
						adoptRelease = webArticleRemoteService
								.findAdoptReleaseByUnitIdDate(id, startDate,
										endDate);
						totalRelease = webArticleRemoteService
								.findReleaseByUnitIdDate(id, startDate, endDate);
						clicksRelease = webArticleRemoteService
								.findClickReleaseByUnitIdDate(id, startDate,
										endDate);
					} else {
						adoptRelease = webArticleRemoteService
								.findAdoptReleaseByUnitId(id);
						totalRelease = webArticleRemoteService
								.findAllReleaseByUnitId(id);
						clicksRelease = webArticleRemoteService
								.findClickReleaseByUnitId(id);
					}
					// adoptRelease =
					// webArticleRemoteService.findAdoptReleaseByUnitId(id);
					// totalRelease =
					// webArticleRemoteService.findAllReleaseByUnitId(id);
					// clicksRelease =
					// webArticleRemoteService.findClickReleaseByUnitId(id);

					webArticle.setClicksRelease(clicksRelease);
					webArticle.setAdoptRelease(adoptRelease);
					webArticle.setTotalRelease(totalRelease);
					Unit u1 = SUtils.dc(unitRemoteService.findById(id),
							Unit.class);
					webArticle.setUnitName(u1.getUnitName());
					listWebArticles.add(webArticle);
				}
			}
			return listWebArticles;
		}
	}

	// List<User> commitUsers =
	// SUtils.dt(userRemoteService.findByIds(commitUserIds.toArray(new
	// String[0])), new TR<List<User>>(){});
	// String commitUnitId=getLoginInfo().getUnitId();

	// Unit unit=SUtils.dc(unitRemoteService.findById(commitUnitId),
	// Unit.class);
	// String regionCode=unit.getRegionCode();
	// Region
	// region=SUtils.dt(regionRemoteService.findRegionsByFullCode(regionCode),
	// new TR<List<Region>>(){}).get(0);
	// String regionCodeString=region.getRegionCode();
	// // Unit
	// unit2=SUtils.dc(unitRemoteService.findById(commitUnitId),Unit.class);
	// if(new Integer(Unit.UNIT_REGION_COUNTY).equals(unit.getRegionLevel())){
	// page.setPageIndex(1);
	// }
	// List<Unit> units1=SUtils.dt(unitRemoteService.findByUnitClassAndRegion(1,
	// regionCodeString+"%",page), new TR<List<Unit>>(){});

	//
	// Integer unitClassInteger = null;
	// if(new Integer(Unit.UNIT_REGION_CITY).equals(unit.getRegionLevel())){
	// unitClassInteger = new Integer(Unit.UNIT_CLASS_EDU);
	// }
	// else if (new
	// Integer(Unit.UNIT_REGION_COUNTY).equals(unit.getRegionLevel())) {
	// unitClassInteger = new Integer(Unit.UNIT_CLASS_SCHOOL);
	// }else if(new
	// Integer(Unit.UNIT_REGION_SCHOOL).equals(unit.getRegionLevel())) {
	// List<McodeDetail> mocodeDetails = getMcodeDetails();
	// int n=0;
	// for (McodeDetail mcodeDetail : mocodeDetails) {
	// WebArticle webArticle=new WebArticle();
	// String totalRelease=null;
	// if(startDate!=null){
	// totalRelease=webArticleRemoteService.findReleaseByTypeDate(commitUnitId,
	// mcodeDetail.getThisId(), startDate, endDate);
	// }else{
	// totalRelease =
	// webArticleRemoteService.findAllReleaseByUnitIdType(commitUnitId,mcodeDetail.getThisId());
	// }
	// webArticle.setTotalRelease(totalRelease);
	// Unit u1=SUtils.dc(unitRemoteService.findById(commitUnitId), Unit.class);
	// webArticle.setUnitName(u1.getUnitName());
	// n+=(Integer.valueOf(totalRelease));
	// listWebArticles.add(webArticle);
	// }
	// listWebArticles.get(0).setAllRelease(String.valueOf(n));
	//
	// return listWebArticles;
	// }
	// List<Unit>
	// units1=SUtils.dt(unitRemoteService.findDirectUnits(commitUnitId,
	// unitClassInteger,page),new TR<List<Unit>>(){});
	// for (Unit unit2 : units1) {
	// WebArticle webArticle=findUnitRelease(unit2.getId(),startDate,endDate);
	// listWebArticles.add(webArticle);
	// }
	// return listWebArticles;

	private List<McodeDetail> getMcodeDetails() {
		List<McodeDetail> models = SUtils.dt(mcodeRemoteService
				.findByMcodeIds(WebsiteConstants.TYPE_MCODE_ID),
				new TR<List<McodeDetail>>() {
				});
		List<McodeDetail> rm = Lists.newArrayList();
		for (McodeDetail model : models) {
			if (ArrayUtils.contains(ArrayUtils.toArray(
					Type.FRENDLY_LINKS.getThisId(),
					Type.PICTURE_FLIP.getThisId(),
					Type.REGIONAL_PICTURE.getThisId()), model.getThisId())) {
				rm.add(model);
			}
		}
		models.removeAll(rm);
		return models;
	}
}
