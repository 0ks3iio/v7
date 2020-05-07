package net.zdsoft.szxy.operation.macro;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.zdsoft.szxy.base.api.RegionRemoteService;
import net.zdsoft.szxy.base.entity.Region;
import net.zdsoft.szxy.base.enu.RegionTypeCode;
import net.zdsoft.szxy.operation.inner.permission.entity.Group;
import net.zdsoft.szxy.operation.security.UserDataRegionHolder;
import net.zdsoft.szxy.plugin.mvc.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 行政区划宏组件
 *
 * @author shenke
 * @since 2019/2/27 下午5:46
 */
@RestController
@RequestMapping("/operation/{dataType}/macro/region")
public class RegionController {

    @Resource
    private RegionRemoteService regionRemoteService;

    @GetMapping("/provinceList")
    public Response doGetProvinceList(@RequestParam(value = "regionCode", required = false) String regionCode) {

        //已经确定了行政区划
        Set<String> regionCodes = UserDataRegionHolder.getRegions();
        Set<String> provinceCodes = new HashSet<>(regionCodes.size());
        if (!regionCodes.contains(Group.ALL_REGION)) {
            for (String region : regionCodes) {
                provinceCodes.add(region.substring(0, 2));
            }
        }

        List<Region> regions = regionRemoteService.getProvinceRegionByType(RegionTypeCode.TYPE_1)
                .stream().filter(e -> {
                    if (regionCodes.isEmpty() || provinceCodes.isEmpty()) {
                        return true;
                    }
                    return provinceCodes.contains(e.getRegionCode());
                }).collect(Collectors.toList());
        //将行政区划划分 A-G H-K L-S T-Z
        List<Region> aGList = new ArrayList<>();
        List<Region> hKList = new ArrayList<>();
        List<Region> lSList = new ArrayList<>();
        List<Region> tZList = new ArrayList<>();
        for (Region region : regions) {
            char first = getRegionNamePinyinFirstLetter(region.getRegionName());

            if ('a' <= first && first <= 'g') {
                aGList.add(region);
            } else if ('h' <= first && first <= 'k') {
                hKList.add(region);
            } else if ('l' <= first && first <= 's') {
                lSList.add(region);
            } else {
                tZList.add(region);
            }
        }
        Comparator<Region> comparator = Comparator.comparingInt(o -> getRegionNamePinyinFirstLetter(o.getRegionName()));
        aGList.sort(comparator);
        hKList.sort(comparator);
        lSList.sort(comparator);
        tZList.sort(comparator);
        StringBuilder assembly = new StringBuilder();
        assemblyHtml(aGList, "A-G", assembly, regionCode);
        assemblyHtml(hKList, "H-K", assembly, regionCode);
        assemblyHtml(lSList, "L-S", assembly, regionCode);
        assemblyHtml(tZList, "T-Z", assembly, regionCode);
        return Response.ok().data("html", assembly.toString()).build();
    }

    private StringBuilder assemblyHtml(List<Region> regions, String title, StringBuilder assembly, String regionCode) {
        if (regions.isEmpty()) {
            return assembly;
        }
        assembly.append("<dl class=\"clearfix\"><dt>").append(title).append("</dt><dd class=\"clearfix\">");
        String provinceCode = StringUtils.isNotBlank(regionCode) ? regionCode.substring(0, 2) : regionCode;
        for (Region region : regions) {
            assembly.append("<span data-region-code=\"").append(region.getRegionCode()).append("\" data-full-code=\"")
                    .append(region.getFullCode()).append("\"")
                    .append(isCurrent(provinceCode, region) ? " class=\"current\"" : "")
                    .append(">").append(region.getRegionName()).append("</span>");
        }
        return assembly.append("</dd></dl>");
    }

    private boolean isCurrent(String regionCode, Region region) {
        if (StringUtils.isBlank(regionCode)) {
            return false;
        }
        return region.getRegionCode().equals(regionCode);
    }

    private char getRegionNamePinyinFirstLetter(String regionName) {
        return PinyinHelper.toHanyuPinyinStringArray(regionName.charAt(0))[0].toLowerCase().charAt(0);
    }

    @GetMapping(value = "subRegion")
    public Response doGetChildren(String fullRegionCode) {
        List<Region> subRegionList = regionRemoteService.getUnderlingRegionByFullCode(fullRegionCode, null);

        subRegionList = subRegionList.stream().filter(e -> !fullRegionCode.equals(e.getFullCode()))
                .collect(Collectors.toList());
        Set<String> regionCodes;
        boolean isCity = fullRegionCode.endsWith("0000");
        if (!(regionCodes = UserDataRegionHolder.getRegions()).isEmpty() && !regionCodes.contains(Group.ALL_REGION)) {
            subRegionList = subRegionList.stream().filter(e -> {
                for (String regionCode : regionCodes) {
                    if (regionCode.length() == 2 && e.getRegionCode().startsWith(regionCode)) {
                        return true;
                    }
                    if (isCity) {
                        if (regionCode.length() == 4 && e.getRegionCode().startsWith(regionCode)) {
                            return true;
                        } else if (regionCode.length() == 6 && e.getRegionCode().startsWith(regionCode.substring(0, 4))) {
                            return true;
                        }
                    } else {
                        if (regionCode.length() == 6 && e.getRegionCode().startsWith(regionCode)) {
                            return true;
                        } else if (regionCode.length() == 4 && e.getRegionCode().startsWith(regionCode)) {
                            return true;
                        }
                    }
                }
                return false;
            }).collect(Collectors.toList());
        }
        return Response.ok().data("msg", subRegionList).build();
    }
}
