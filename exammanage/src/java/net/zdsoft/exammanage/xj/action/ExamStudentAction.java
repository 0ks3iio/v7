package net.zdsoft.exammanage.xj.action;

import com.alibaba.fastjson.TypeReference;
import net.zdsoft.exammanage.xj.constant.XjExamConstants;
import net.zdsoft.exammanage.xj.dto.ExamStuSeatDto;
import net.zdsoft.exammanage.xj.dto.ExamStuSeatInfo;
import net.zdsoft.exammanage.xj.entity.XjexamContrast;
import net.zdsoft.exammanage.xj.entity.XjexamInfo;
import net.zdsoft.exammanage.xj.entity.XjexamType;
import net.zdsoft.exammanage.xj.service.XjExamContrastService;
import net.zdsoft.exammanage.xj.service.XjExamInfoService;
import net.zdsoft.exammanage.xj.service.XjExamTypeService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author yangsj  2017年10月13日下午5:05:41
 */
@Controller
@RequestMapping("/examinfo/stu")
public class ExamStudentAction extends BaseAction {

    @Autowired
    private XjExamInfoService xjExamInfoService;
    @Autowired
    private XjExamContrastService xjExamContrastService;
    @Autowired
    private XjExamTypeService xjExamTypeService;

    @RequestMapping("/showIndex")
    @ControllerInfo("页面的第一次跳转")
    public String showIndex() {

        return "/exammanage/xjExam/student/examShowIndex.ftl";
    }

    @RequestMapping("/index")
    @ControllerInfo("页面的展示")
    public String index(ModelMap map) {
        final List<String> typeKeys = new ArrayList<String>();
        typeKeys.add(XjExamConstants.EXAM_STU_SEAT_TYPE);
        typeKeys.add(XjExamConstants.EXAM_STU_SCORE_TYPE);
        typeKeys.add(XjExamConstants.EXAM_STU_TITLE_TYPE);
        List<XjexamType> typeList = RedisUtils.getObject("XJEXAMCONST" + XjExamConstants.EXAM_STU_SEAT_TYPE + XjExamConstants.EXAM_STU_SCORE_TYPE, RedisUtils.TIME_ONE_HOUR, new TypeReference<List<XjexamType>>() {
        }, new RedisInterface<List<XjexamType>>() {
            @Override
            public List<XjexamType> queryData() {
                return xjExamTypeService.findByTypeKeys(typeKeys.toArray(new String[typeKeys.size()]));
            }
        });

        Map<String, XjexamType> xjtypeMap = EntityUtils.getMap(typeList, "typeKey");
        if (CollectionUtils.isNotEmpty(typeList))
            typeList.remove(xjtypeMap.get(XjExamConstants.EXAM_STU_TITLE_TYPE));
        map.put("typeList", typeList);
        map.put("title", xjtypeMap.get(XjExamConstants.EXAM_STU_TITLE_TYPE).getTypeValue());
        return "/exammanage/xjExam/student/examIndex.ftl";
    }

    @ResponseBody
    @RequestMapping("/findExam")
    @ControllerInfo("查询考生是否存在")
    public String findExam(final String studentName, final String admission, String examType) {
        try {
            XjexamInfo examInfo = RedisUtils.getObject("ZJHCX" + admission + studentName, RedisUtils.TIME_ONE_HOUR, new TypeReference<XjexamInfo>() {
            }, new RedisInterface<XjexamInfo>() {
                @Override
                public XjexamInfo queryData() {
                    return xjExamInfoService.findStuInfo(studentName, admission);
                }
            });

            if (examInfo == null) {
                return error("查无此人，请核对信息");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return error("查询失败");
        }
        return success("查询成功");
    }

    @RequestMapping("/showSeatList")
    @ControllerInfo("查看学生的座位号")
    public String showSeatList(ModelMap map, final String studentName, final String admission, String examType) {

        List<XjexamType> typeList = xjExamTypeService.findByTypeKeys(XjExamConstants.EXAM_STU_TITLE_TYPE);
        List<XjexamContrast> listEC = RedisUtils.getObject("ZJHCXLEC", RedisUtils.TIME_ONE_HOUR, new TypeReference<List<XjexamContrast>>() {
        }, new RedisInterface<List<XjexamContrast>>() {
            @Override
            public List<XjexamContrast> queryData() {
                return xjExamContrastService.findAll();
            }
        });


        Map<String, String> ecMap = EntityUtils.getMap(listEC, "columnKey", "columnValue");
        XjexamInfo examInfo = RedisUtils.getObject("ZJHCX" + admission + studentName, RedisUtils.TIME_ONE_HOUR, new TypeReference<XjexamInfo>() {
        }, new RedisInterface<XjexamInfo>() {
            @Override
            public XjexamInfo queryData() {
                return xjExamInfoService.findStuInfo(studentName, admission);
            }
        });

        map.put("title", typeList.get(0).getTypeValue());
        map.put("ecMap", ecMap);
        map.put("examInfos", examInfo);

        if (XjExamConstants.EXAM_STU_SEAT_TYPE.equals(examType)) {
            String[] strings = getCareful(XjExamConstants.EXAM_STU_EXAM_CAREFUL_TYPE);
            map.put("careful", Arrays.asList(strings));
            map.put("seatsList", toDto(examInfo));
            return "/exammanage/xjExam/student/examShowSeat.ftl";
        } else {
            String[] strings = getCareful(XjExamConstants.EXAM_STU_SCORE_CAREFUL_TYPE);
            Map<String, String> scoreMap = getStuExamScore(examInfo);
            map.put("columnKeys", getColumnKeys(listEC, scoreMap));
            map.put("careful", Arrays.asList(strings));
            map.put("scoreMap", scoreMap);
            return "/exammanage/xjExam/student/examShowScore.ftl";
        }
    }

    //得到学生的考试信息
    private ExamStuSeatDto toDto(XjexamInfo examInfos) {

        ExamStuSeatDto seatsList = new ExamStuSeatDto();
        //XjexamInfo examInfos = xjExamInfoService.findStuInfo(studentName,admission);

        List<ExamStuSeatInfo> listess = new ArrayList<ExamStuSeatInfo>();
        if (examInfos.getColumn_1() != null) {
            ExamStuSeatInfo examStuSeatInfo = new ExamStuSeatInfo();
            examStuSeatInfo.setExamName("语文");
            examStuSeatInfo.setExamPlacd(examInfos.getColumn_4());
            examStuSeatInfo.setExamStuNum(examInfos.getColumn_2());
            examStuSeatInfo.setExamTime(examInfos.getColumn_3());
            listess.add(examStuSeatInfo);
        }
        if (examInfos.getColumn_5() != null) {
            ExamStuSeatInfo examStuSeatInfo = new ExamStuSeatInfo();
            examStuSeatInfo.setExamName("数学");
            examStuSeatInfo.setExamPlacd(examInfos.getColumn_8());
            examStuSeatInfo.setExamStuNum(examInfos.getColumn_6());
            examStuSeatInfo.setExamTime(examInfos.getColumn_7());
            listess.add(examStuSeatInfo);
        }
        if (examInfos.getColumn_9() != null) {
            ExamStuSeatInfo examStuSeatInfo = new ExamStuSeatInfo();
            examStuSeatInfo.setExamName("英语");
            examStuSeatInfo.setExamPlacd(examInfos.getColumn_12());
            examStuSeatInfo.setExamStuNum(examInfos.getColumn_10());
            examStuSeatInfo.setExamTime(examInfos.getColumn_11());
            listess.add(examStuSeatInfo);
        }
        if (examInfos.getColumn_13() != null) {
            ExamStuSeatInfo examStuSeatInfo = new ExamStuSeatInfo();
            examStuSeatInfo.setExamName("物理");
            examStuSeatInfo.setExamPlacd(examInfos.getColumn_16());
            examStuSeatInfo.setExamStuNum(examInfos.getColumn_14());
            examStuSeatInfo.setExamTime(examInfos.getColumn_15());
            listess.add(examStuSeatInfo);
        }
        if (examInfos.getColumn_17() != null) {
            ExamStuSeatInfo examStuSeatInfo = new ExamStuSeatInfo();
            examStuSeatInfo.setExamName("化学");
            examStuSeatInfo.setExamPlacd(examInfos.getColumn_20());
            examStuSeatInfo.setExamStuNum(examInfos.getColumn_18());
            examStuSeatInfo.setExamTime(examInfos.getColumn_19());
            listess.add(examStuSeatInfo);
        }
        if (examInfos.getColumn_21() != null) {
            ExamStuSeatInfo examStuSeatInfo = new ExamStuSeatInfo();
            examStuSeatInfo.setExamName("生物");
            examStuSeatInfo.setExamPlacd(examInfos.getColumn_24());
            examStuSeatInfo.setExamStuNum(examInfos.getColumn_22());
            examStuSeatInfo.setExamTime(examInfos.getColumn_23());
            listess.add(examStuSeatInfo);
        }
        if (examInfos.getColumn_25() != null) {
            ExamStuSeatInfo examStuSeatInfo = new ExamStuSeatInfo();
            examStuSeatInfo.setExamName("政治");
            examStuSeatInfo.setExamPlacd(examInfos.getColumn_28());
            examStuSeatInfo.setExamStuNum(examInfos.getColumn_26());
            examStuSeatInfo.setExamTime(examInfos.getColumn_27());
            listess.add(examStuSeatInfo);
        }
        if (examInfos.getColumn_29() != null) {
            ExamStuSeatInfo examStuSeatInfo = new ExamStuSeatInfo();
            examStuSeatInfo.setExamName("历史");
            examStuSeatInfo.setExamPlacd(examInfos.getColumn_32());
            examStuSeatInfo.setExamStuNum(examInfos.getColumn_30());
            examStuSeatInfo.setExamTime(examInfos.getColumn_31());
            listess.add(examStuSeatInfo);
        }
        if (examInfos.getColumn_33() != null) {
            ExamStuSeatInfo examStuSeatInfo = new ExamStuSeatInfo();
            examStuSeatInfo.setExamName("地理");
            examStuSeatInfo.setExamPlacd(examInfos.getColumn_36());
            examStuSeatInfo.setExamStuNum(examInfos.getColumn_34());
            examStuSeatInfo.setExamTime(examInfos.getColumn_35());
            listess.add(examStuSeatInfo);
        }
        if (examInfos.getColumn_37() != null) {
            ExamStuSeatInfo examStuSeatInfo = new ExamStuSeatInfo();
            examStuSeatInfo.setExamName("技术");
            examStuSeatInfo.setExamPlacd(examInfos.getColumn_40());
            examStuSeatInfo.setExamStuNum(examInfos.getColumn_38());
            examStuSeatInfo.setExamTime(examInfos.getColumn_39());
            listess.add(examStuSeatInfo);
        }
        seatsList.setStuName(examInfos.getStuName());
        seatsList.setAdmission(examInfos.getAdmission());
        seatsList.setClassName(examInfos.getClassName());
        seatsList.setGradeName(examInfos.getGradeName());
        seatsList.setListess(listess);
        seatsList.setCarefulThings(examInfos.getColumn_41());

        return seatsList;
    }

    //得到学生的考试成绩
    private Map<String, String> getStuExamScore(XjexamInfo examInfos) {
        // TODO Auto-generated method stub
        Map<String, String> showMap = new HashMap<String, String>();
        //得到constantMap
        //XjexamInfo examInfos = xjExamInfoService.findStuInfo(studentName,admission);
        Field[] field = examInfos.getClass().getDeclaredFields();
        for (int i = 46; i < field.length; i++) {
            field[i].setAccessible(true);
            try {
                if (field[i].get(examInfos) != null && !field[i].get(examInfos).equals("0")) {
                    showMap.put(field[i].getName(), (String) field[i].get(examInfos));
                }
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return showMap;
    }

    //得到注意事项
    private String[] getCareful(final String type) {
        //List<XjexamType> typeList1 = xjExamTypeService.findByTypeKeys(type);
        List<XjexamType> typeList1 = RedisUtils.getObject("XJEXAMCONST" + type, RedisUtils.TIME_ONE_HOUR, new TypeReference<List<XjexamType>>() {
        }, new RedisInterface<List<XjexamType>>() {
            @Override
            public List<XjexamType> queryData() {
                return xjExamTypeService.findByTypeKeys(type);
            }
        });

        String typeValue = typeList1.get(0).getTypeValue();
        String[] strings = typeValue.split("<br>");
        return strings;
    }

    //得到有值的ColumnKeys
    private List<String> getColumnKeys(List<XjexamContrast> listEC, Map<String, String> seatMap) {
        List<String> columnKeys = new ArrayList<String>();
        for (XjexamContrast xjexamContrast : listEC) {
            if (seatMap.get(xjexamContrast.getColumnKey()) != null)
                columnKeys.add(xjexamContrast.getColumnKey());
        }
        return columnKeys;
    }
}
