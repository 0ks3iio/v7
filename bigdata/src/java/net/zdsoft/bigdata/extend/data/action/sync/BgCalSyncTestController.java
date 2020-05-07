package net.zdsoft.bigdata.extend.data.action.sync;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.bigdata.extend.data.biz.BgCalService;
import net.zdsoft.bigdata.stat.service.BgDataQualityStatService;
import net.zdsoft.bigdata.stat.service.BgMetadataStatService;
import net.zdsoft.bigdata.stat.service.BgSysStatService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/bgcal/common")
public class BgCalSyncTestController extends BigdataBaseAction {

    @Autowired
    private BgCalService bgCalService;

    @Autowired
    private BgSysStatService bgSysStatService;

    @Autowired
    private BgDataQualityStatService bgDataQualityStatService;


    @Autowired
    private BgMetadataStatService bgMetadataStatService;


    @RequestMapping("/student")
    public String student(ModelMap map) {
        bgCalService.dealUserTagCal("student");
        return "/bigdata/v3/common/error.ftl";
    }

    @RequestMapping("/teacher")
    public String teacher(ModelMap map) {
        bgCalService.dealUserTagCal("teacher");
        return "/bigdata/v3/common/error.ftl";
    }

    @RequestMapping("/warning")
    public String warning(ModelMap map) {
        bgCalService.dealWarningCal("402880a46af87c33016af89422fb0036");
        return "/bigdata/v3/common/error.ftl";
    }

    @RequestMapping("/qualityStat")
    public String quality(ModelMap map) {
        try {
            bgDataQualityStatService.dataQualityStat(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/bigdata/v3/common/error.ftl";
    }

    @RequestMapping("/metadataStat/7")
    public String metadataStat7(ModelMap map) {
        ///bgcal/common/metadataStat
        try {
            for (int i = 1; i <= 7; i++) {
                Date today = DateUtils.addDay(new Date(), (0 - i));
                bgMetadataStatService.metadataStatByDaily(today);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/bigdata/v3/common/error.ftl";
    }

    @RequestMapping("/metadataStat")
    public String metadataStat(ModelMap map) {
        ///bgcal/common/metadataStat
        try {
            bgMetadataStatService.metadataStatByDaily(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/bigdata/v3/common/error.ftl";
    }

    @RequestMapping("/bigdata/sysstat")
    public String bigdataSysStat(ModelMap map) {
        bgSysStatService.summaryStat();
        bgSysStatService.usageStat();
        bgSysStatService.moduleStat();
        bgSysStatService.jobStat();
        bgSysStatService.logStat();
        return "/bigdata/v3/common/error.ftl";
    }

    @ResponseBody
    @RequestMapping(value = "/callcackApiTest", method = RequestMethod.POST)
    public String callcackApiTest(@RequestBody String requestBody,
                                  HttpServletRequest request) {
        System.out.println("大数据回调测试返回数据---------------" + requestBody);
        return StringUtils.EMPTY;
    }
}
