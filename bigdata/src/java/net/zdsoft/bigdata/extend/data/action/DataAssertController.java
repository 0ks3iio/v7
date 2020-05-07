package net.zdsoft.bigdata.extend.data.action;

import net.zdsoft.bigdata.data.manager.datasource.IHttpComponentsApiDataSource;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.extend.data.entity.Asset;
import net.zdsoft.bigdata.extend.data.service.AssetService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.RedisUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by wangdongdong on 2018/7/9 14:15.
 */
@Controller
@RequestMapping("/bigdata/asset")
public class DataAssertController extends BigdataBaseAction {

    @Resource
    private IHttpComponentsApiDataSource httpComponentsApiDataSource;
    @Resource
    private AssetService assetService;

    @RequestMapping("/index")
    public String index(ModelMap model) {
        List<Asset> assets = assetService.findAll();
        Map<String, Asset> assetMap = assets.stream().collect(Collectors.toMap(Asset::getAssetCode, asset -> asset));
        model.addAttribute("assetMap", assetMap);
        return "/bigdata/extend/asset/index.ftl";
    }

    @RequestMapping("/queryData")
    @ResponseBody
    public Response queryData(String apiUrl, String assertCode) {
        try {
            String result = RedisUtils.get("entity.Asset." + assertCode);
            if (result != null) {
                return Response.ok().data(result).build();
            }
            if (StringUtils.isNotBlank(apiUrl)) {
                result = (String) httpComponentsApiDataSource.executeQuery(apiUrl, 30 * 1000);
                RedisUtils.set("entity.Asset." + assertCode, result, 60 * 60 * 2);
                return Response.ok().data(result).build();
            }
            return Response.error().message("未配置api访问地址").build();
        } catch (Exception e) {
            return Response.error().message("访问【" + apiUrl + "】超时").build();
        }
    }
}
