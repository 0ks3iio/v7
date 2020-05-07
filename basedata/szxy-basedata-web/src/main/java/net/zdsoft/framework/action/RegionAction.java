/* 
 * @(#)RegionAction.java    Created on 2017年3月10日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.framework.action;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.framework.utils.EntityUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.framework.utils.PinyinUtils;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年3月10日 下午2:39:23 $
 */
@Controller
@RequestMapping("/region")
public class RegionAction extends BaseAction {
    @Autowired
    private RegionRemoteService regionRemoteService;

    @ResponseBody
    @RequestMapping("/proviceList")
    public String proviceList() {
        List<Region> proviceList = Region.dt(regionRemoteService.findProviceRegionByType("1"));

        List<Region> aToGProviceList = new ArrayList<Region>();
        List<Region> hToKProviceList = new ArrayList<Region>();
        List<Region> lToSProviceList = new ArrayList<Region>();
        List<Region> tToZProviceList = new ArrayList<Region>();

        for (Region provice : proviceList) {
            String firstLetter = PinyinUtils.toHanyuPinyin(provice.getRegionName(), false).substring(0, 1);
            if (firstLetter.compareTo("a") >= 0 && firstLetter.compareTo("g") <= 0) {
                aToGProviceList.add(provice);
            }
            else if (firstLetter.compareTo("h") >= 0 && firstLetter.compareTo("k") <= 0) {
                hToKProviceList.add(provice);
            }
            else if (firstLetter.compareTo("l") >= 0 && firstLetter.compareTo("s") <= 0) {
                lToSProviceList.add(provice);
            }
            else if (firstLetter.compareTo("t") >= 0 && firstLetter.compareTo("z") <= 0) {
                tToZProviceList.add(provice);
            }
        }

        String proviceHtml = "";
        proviceHtml += getProviceHtml(aToGProviceList, "A-G");
        proviceHtml += getProviceHtml(hToKProviceList, "H-K");
        proviceHtml += getProviceHtml(lToSProviceList, "L-S");
        proviceHtml += getProviceHtml(tToZProviceList, "T-Z");

        return success(proviceHtml);
    }

    /**
     * 获取省级行政区划Html
     * 
     * @author cuimq
     * @param proviceList
     * @param title
     * @return
     */
    private String getProviceHtml(List<Region> proviceList, String title) {
        String proviceHtml = "";
        if (CollectionUtils.isNotEmpty(proviceList)) {
            proviceHtml += "<dl class=\"clearfix\"><dt>" + title + "</dt><dd class=\"clearfix\">";
            for (Region provice : proviceList) {
                proviceHtml += "<span data-region-code=\"" + provice.getRegionCode() + "\" data-full-code=\""
                        + provice.getFullCode() + "\">" + provice.getRegionName() + "</span>";
            }
            proviceHtml += "</dd></dl>";
        }
        return proviceHtml;
    }

    @ResponseBody
    @RequestMapping("/subRegionList")
    public String subRegionList(final String fullRegionCode) {
        List<Region> subRegionList = Region.dt(regionRemoteService.findUnderlineRegions(fullRegionCode));
        subRegionList = EntityUtils.filter(subRegionList, new EntityUtils.Filter<Region>(){
            @Override
            public boolean doFilter(Region region) {
                return region.getFullCode().equals(fullRegionCode);
            }
        });
        return success(JSONObject.toJSONString(subRegionList));
    }
}
