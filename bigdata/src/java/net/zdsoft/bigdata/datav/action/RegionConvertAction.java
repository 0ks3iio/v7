package net.zdsoft.bigdata.datav.action;

import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.echarts.convert.api.GeoCoordHandler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.regex.Pattern;

/**
 * 将地图上
 * @author shenke
 * @since 2018/10/26 下午4:43
 */
@Controller
@RequestMapping("/bigdata/datav/region/convert")
public class RegionConvertAction extends BigdataBaseAction{

    private List<GeoCoordHandler> geoCoordHandlerList;
    private Pattern city = Pattern.compile("^[a-zA-Z0-9]{4}00$");
    private Pattern province = Pattern.compile("^[a-zA-Z0-9]{2}0000$");
    private Pattern county = Pattern.compile("^[a-zA-Z0-9]{6}");

    @PostConstruct
    public void initGeoHandler() {
        geoCoordHandlerList = new ArrayList<>();
        ServiceLoader.load(GeoCoordHandler.class).forEach(geoCoordHandlerList::add);
    }

    @ResponseBody
    @PostMapping("region")
    public Response execute(@RequestParam(value = "name", required = false) String name) {
        String regionCode = "";
        for (GeoCoordHandler geoCoordHandler : geoCoordHandlerList) {
            regionCode = geoCoordHandler.getRegionCode(name);
            if (!"00".equalsIgnoreCase(regionCode)) {
                break;
            }
        }
        if (StringUtils.isBlank(regionCode)) {
            return Response.ok().data("").build();
        }
        //fullCode -> regionCode
        if (city.matcher(regionCode).matches()) {
            regionCode = regionCode.substring(0, 4);
            if (regionCode.endsWith("00")) {
                regionCode = regionCode.substring(0, 2);
            }
        }
        else if (province.matcher(regionCode).matches()) {
            regionCode = regionCode.substring(0, 2);
        }
        else if (county.matcher(regionCode).matches()) {

        }

        return Response.ok().data(regionCode).build();
    }
}
