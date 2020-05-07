package net.zdsoft.credit.data.action;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.credit.data.entity.CreditDailySet;
import net.zdsoft.credit.data.entity.CreditSet;
import net.zdsoft.credit.data.service.CreditDailySetService;
import net.zdsoft.credit.data.service.CreditSetService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

@Controller
@RequestMapping("exammanage/credit")
public class CreditSetAction extends BaseAction {
    @Autowired
    CreditDailySetService creditDailySetService;
    @Autowired
    CreditSetService creditSetService;
    @Autowired
    SemesterRemoteService semesterRemoteService;

    @RequestMapping("/set")
    public String index(ModelMap modelMap) {
        return "/exammanage/credit/creditSet/creditSetIndex.ftl";
    }

    @RequestMapping("/creditSetInfo")
    public String creditSetInfo(ModelMap modelMap) {
        String unitId = getLoginInfo().getUnitId();
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        CreditSet creditSet = creditSetService.findOneBy(new String[]{"acadyear", "unitId", "semester"}, new String[]{semester.getAcadyear(), unitId, semester.getSemester().toString()});
        if (creditSet != null) {
            List<CreditDailySet> creditDailySetList = creditDailySetService.findBySetId(creditSet.getId());
            if (CollectionUtils.isNotEmpty(creditDailySetList)) {
                creditSet.setDailySetList(creditDailySetList);
            }
            modelMap.put("creditSet", creditSet);
        } else {
            creditSet = creditSetService.findAndInit(getLoginInfo().getUnitId(), semester.getAcadyear(), semester.getSemester() + "");
            if (creditSet == null) {
                creditSet = new CreditSet();
                creditSet.setId(UuidUtils.generateUuid());
            }
            modelMap.put("creditSet", creditSet);
        }
        return "/exammanage/credit/creditSet/creditSetInfo.ftl";
    }

    @RequestMapping("/addCreditDailySet")
    @ResponseBody
    public String addCreditSet(ModelMap modelMap, String setId) {
        JSONObject jsonObject = new JSONObject();
        CreditDailySet creditDailySet = new CreditDailySet();
        creditDailySet.setId(UuidUtils.generateUuid());
        try {
            jsonObject.put("id", creditDailySet.getId());
            jsonObject.put("success", true);
            return jsonObject.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
    }

    @RequestMapping("/delCreditDailySet")
    @ResponseBody
    public String addCreditSet(String id) {
        try {
            creditDailySetService.delete(id);
            List<CreditDailySet> creditDailySets = creditDailySetService.findListBy(new String[]{"parentId"}, new String[]{id});
            creditDailySetService.deleteAll(creditDailySets.toArray(new CreditDailySet[0]));
            return returnSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
    }

    @RequestMapping("/creditDailySetInfo")
    public String creditDailySetInfo(ModelMap modelMap) {
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        CreditSet creditSet = creditSetService.findOneBy(new String[]{"acadyear","unitId", "semester"}, new String[]{semester.getAcadyear(),getLoginInfo().getUnitId(), semester.getSemester().toString()});
        if (creditSet != null) {
            List<CreditDailySet> creditDailySetList = creditDailySetService.findBySetId(creditSet.getId());
            if (CollectionUtils.isNotEmpty(creditDailySetList)) {
                creditSet.setDailySetList(creditDailySetList);
            }
            modelMap.put("creditSet", creditSet);
        } else {
            creditSet = creditSetService.findAndInit(getLoginInfo().getUnitId(), semester.getAcadyear(), semester.getSemester() + "");
            if (creditSet == null) {
                creditSet = new CreditSet();
                creditSet.setId(UuidUtils.generateUuid());
            }
            modelMap.put("creditSet", creditSet);
        }
        return "/exammanage/credit/creditSet/creditDailySetIndex.ftl";
    }

    @RequestMapping("/creditExamSet/save")
    @ResponseBody
    public String savecreditExamSet(CreditSet creditSet) {
        try {
            CreditSet creditSet1 = creditSetService.findOne(creditSet.getId());
            Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
            if (creditSet1 != null) {
                creditSet1.setDailyScore(creditSet.getDailyScore());
                creditSet1.setUsualScore(creditSet.getUsualScore());
                creditSet1.setSumScore(creditSet.getSumScore());
                creditSet1.setPassLine(creditSet.getPassLine());
                creditSet1.setModuleScore(creditSet.getModuleScore());
                creditSetService.save(creditSet1);
            } else {
                creditSet.setAcadyear(semester.getAcadyear());
                creditSet.setSemester(semester.getSemester().toString());
                creditSet.setUnitId(getLoginInfo().getUnitId());
                creditSetService.save(creditSet);
            }
            List<CreditDailySet> creditDailySets = creditSet.getDailySetList();
            if(CollectionUtils.isNotEmpty(creditDailySets)) {
                Set<String> parentIds = creditDailySets.stream().map(CreditDailySet::getParentId).collect(Collectors.toSet());
                List<CreditDailySet> list = creditDailySetService.findListByIds(parentIds.toArray(new String[]{}));
                if(CollectionUtils.isNotEmpty(list)){
                    for (CreditDailySet creditDailySet : list) {
                        if (StringUtils.isBlank(creditDailySet.getParentId())) {
                            float score = 0f;
                            for (CreditDailySet creditDailySet1 : creditDailySets) {
                                if (creditDailySet1.getParentId().equals(creditDailySet.getId())) {
                                    score = score + creditDailySet1.getScore();
                                }
                            }
                            creditDailySet.setScore(score);
                        }
                    }
                    creditDailySets.addAll(list);
                }

                creditDailySetService.saveAll(creditDailySets.toArray(new CreditDailySet[0]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnSuccess();
    }

    @RequestMapping("/creditDailySet/save")
    @ResponseBody
    public String saveCreditDailySet(CreditSet creditSet) {
        List<CreditDailySet> list = creditSet.getDailySetList();
        List<CreditDailySet> creditDailySets = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        //去除提交过来的空对象
        if (CollectionUtils.isNotEmpty(list)) {
            for (CreditDailySet creditDailySet : list) {
                if (StringUtils.isNotBlank(creditDailySet.getIsParent())) {
                    creditDailySet.setSetId(creditSet.getId());
                    if (creditDailySet.getIsParent().equals("0") && StringUtils.isNotBlank(creditDailySet.getName()) && StringUtils.isBlank(creditDailySet.getId())) {
                        creditDailySet.setId(UuidUtils.generateUuid());
                    }
                    if (StringUtils.isNotBlank(creditDailySet.getId())) {
                        if(creditDailySet.getScore()==null){
                            creditDailySet.setScore(0f);
                        }
                        creditDailySets.add(creditDailySet);
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(creditDailySets)) {
            Map<String, CreditDailySet> dailySetMap = creditDailySets.stream().collect(Collectors.toMap(CreditDailySet::getId, Function.identity()));
            //移除name值为空的以及其子对象
            Iterator<CreditDailySet> it = creditDailySets.iterator();
            while (it.hasNext()) {
                CreditDailySet creditDailySet = it.next();
                if ((creditDailySet.getIsParent().equals("1") && StringUtils.isBlank(creditDailySet.getName()))) {
                    ids.add(creditDailySet.getId());
                    it.remove();
                }
                if (StringUtils.isNotBlank(creditDailySet.getParentId())) {
                    if (StringUtils.isBlank(dailySetMap.get(creditDailySet.getParentId()).getName())) {
                        it.remove();
                    }
                }
            }
        }
        try {
            if (CollectionUtils.isNotEmpty(ids)) {
                creditDailySetService.deleteByIds(ids.toArray(new String[0]));
            }
            List<CreditDailySet> list1 = creditDailySetService.findBySetId(creditSet.getId());
            creditDailySetService.deleteAll(list1.toArray(new CreditDailySet[0]));
            creditDailySetService.saveAll(creditDailySets.toArray(new CreditDailySet[0]));
            return returnSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
    }
}
