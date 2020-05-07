package net.zdsoft.bigdata.frame.data.demo.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;

@Controller
@RequestMapping("/bigdata/frame/common/demo")
public class BaiduMapDemoController extends BaseAction {

	@Autowired
	private RegionRemoteService regionRemoteService;

	@RequestMapping("/baidumap")
	public String query(ModelMap map) {
		return "/bigdata/demo/baiduMap.ftl";
	}
	
	@RequestMapping("/baidumap/regionDetail")
	public String regionDetail(String regionCode,ModelMap map) {
		Region currentRegion = SUtils.dc(
				regionRemoteService.findByFullCode(regionCode), Region.class);
		map.put("currentRegion", currentRegion);
		return "/bigdata/customization/xinjiang/regionDetail.ftl";
	}
	
	@RequestMapping("/baidumap/schoolDetail")
	public String schoolDetail(String schoolId,ModelMap map) {
		return "/bigdata/customization/xinjiang/schoolDetail.ftl";
	}

	@RequestMapping("/baidumap/xz")
	public String xz(String regionCode, String parentRegionCode, ModelMap map) {
		Region currentRegion = SUtils.dc(
				regionRemoteService.findByFullCode(regionCode), Region.class);

		List<Region> subRegionList = SUtils.dt(
				regionRemoteService.findUnderlineRegions(regionCode),
				Region.class);
		List<Region> resultList = new ArrayList<Region>();
		for (Region region : subRegionList) {
			if (!regionCode.equals(region.getFullCode())) {
				resultList.add(region);
			}
		}
		map.put("regionList", resultList);
		map.put("currentRegion", currentRegion);
		map.put("regionCode", regionCode);
		if (StringUtils.isBlank(parentRegionCode))
			parentRegionCode = regionCode;
		map.put("parentRegionCode", parentRegionCode);
		return "/bigdata/demo/baiduMap1.ftl";
	}

	@RequestMapping("/baidumap/school")
	public String school(String regionCode, String parentRegionCode,
			ModelMap map) {
		Region currentRegion = SUtils.dc(
				regionRemoteService.findByFullCode(regionCode), Region.class);
		List<Json> schoolList = readGisInfo();
		map.put("currentRegion", currentRegion);
		map.put("regionCode", regionCode);
		map.put("parentRegionCode", parentRegionCode);
		map.put("schoolList", schoolList);
		return "/bigdata/demo/schoolMap.ftl";
	}

	@SuppressWarnings("deprecation")
	private List<Json> readGisInfo() {
		String filepath = "d:\\test\\乌鲁木齐市学校经纬度.xlsx";
		List<Json> schoolList = new ArrayList<>();
		try (InputStream inputStream = new FileInputStream(filepath)) {
			ExcelReader excelReader = new ExcelReader(inputStream,
					ExcelTypeEnum.XLSX, null,
					new AnalysisEventListener<List<String>>() {
						@Override
						public void invoke(List<String> object,
								AnalysisContext context) {
							if (object != null
									&& !StringUtils.isEmpty(object.get(0))
									&& !StringUtils.isEmpty(object.get(4))) {
								String jwd=object.get(4);
								Json schoolInfo = new Json();
								schoolInfo.put("schoolId", object.get(0));
								schoolInfo.put("schoolName", object.get(1));
								schoolInfo.put("latitude", jwd.split(",")[0]);
								schoolInfo.put("longitude", jwd.split(",")[1]);
								schoolList.add(schoolInfo);
							}
						}

						@Override
						public void doAfterAllAnalysed(AnalysisContext context) {
						}
					});
			excelReader.read(new Sheet(1, 1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return schoolList;
	}
}
