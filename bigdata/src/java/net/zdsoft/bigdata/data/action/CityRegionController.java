package net.zdsoft.bigdata.data.action;

import java.util.List;
import java.util.stream.Collectors;

import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;

/**
 * @author shenke
 * @since 2018/8/23 10:21
 */
@Controller
@RequestMapping("/bigdata/city")
public class CityRegionController extends BigdataBaseAction {

    @Autowired
    private RegionRemoteService regionRemoteService;

    @GetMapping(
            value = "{regionCode}"
    )
    public String city(@PathVariable("regionCode") String regionCode,
                       @RequestParam("selectedRegionCode") String selectedRegionCode,
                       ModelMap model) {
        String regionsJson = regionRemoteService.findUnderlineRegions(StringUtils.rightPad(regionCode, 6, "0"));
        List<Region> regions = JSONObject.parseArray(regionsJson, Region.class);
        regions = regions.stream().filter(region -> region.getRegionCode().length()!=2).collect(Collectors.toList());
        model.addAttribute("regions", regions);
        model.addAttribute("selectedRegionCode", selectedRegionCode);
        return "/bigdata/chart/cityRegion.ftl";
    }
}
