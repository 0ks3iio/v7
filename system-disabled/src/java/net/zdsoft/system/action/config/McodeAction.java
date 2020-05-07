/* 
 * @(#)McodeAction.java    Created on 2017-3-1
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.action.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.entity.mcode.McodeList;
import net.zdsoft.system.service.mcode.McodeDetailService;
import net.zdsoft.system.service.mcode.McodeListService;

@Controller
@RequestMapping(value = "/system/mcode")
public class McodeAction extends BaseAction {
    @Autowired
    private McodeListService mcodeListService;
    @Autowired
    private McodeDetailService mcodeDetailService;

    @RequestMapping("/mcodeLists")
    public String getMcodeLists(String mcodeName, ModelMap map) {
        List<McodeList> mcodeLists = new ArrayList<McodeList>();
        Pagination page = createPagination();
        if (!Validators.isEmpty(mcodeName)) {
            mcodeLists = mcodeListService.findByName(mcodeName, page);
        }
        map.put("mcodeLists", mcodeLists);
        map.put("mcodeName", mcodeName);
        map.put("pagination", page);
        return "/system/mcode/mcodeLists.ftl";
    }

    @RequestMapping("/mcodeDetails")
    public String mcodeDetails(String mcodeName, String mcodeId, ModelMap map) {
        List<McodeDetail> mcodeDetails = new ArrayList<McodeDetail>();
        Pagination page = createPagination();
        if (!Validators.isEmpty(mcodeId)) {
            // 查找伪代码列表
            mcodeDetails = mcodeDetailService.findByMcodeId(mcodeId, page);
        }
        map.put("mcodeId", mcodeId);
        map.put("pagination", page);
        map.put("mcodeName", mcodeName);
        map.put("mcodeDetails", mcodeDetails);
        return "/system/mcode/mcodeDetails.ftl";
    }

    @ResponseBody
    @RequestMapping("/modifyIsUsing")
    public String getMcodeLists(Integer isUsing, String id) {
        mcodeDetailService.updateIsUsingById(isUsing, id);
        return returnSuccess();
    }

}
