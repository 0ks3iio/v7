package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmArrangeDao;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.exammanage.data.utils.Coloring;
import net.zdsoft.exammanage.data.utils.Group;
import net.zdsoft.exammanage.data.utils.Seat;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("emArrangeService")
public class EmArrangeServiceImpl extends BaseServiceImpl<EmArrange, String> implements EmArrangeService {
    @Autowired
    private EmArrangeDao emArrangeDao;
    @Autowired
    private EmPlaceService emPlaceService;
    @Autowired
    private EmPlaceSettingService emPlaceSettingService;
    @Autowired
    private EmOptionService emOptionService;
    @Autowired
    private EmOptionSchoolService emOptionSchoolService;
    @Autowired
    private EmEnrollStudentService emArrangeService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private EmEnrollStudentService emEnrollStudentService;
    @Autowired
    private EmEnrollStuCountService emEnrollStuCountService;
    @Autowired
    private EmJoinexamschInfoService emJoinexamschInfoService;
    @Autowired
    private EmExamRegionService emExamRegionService;
    @Autowired
    private EmPlaceStudentService emPlaceStudentService;

    private static void editPlaceStu(EmPlaceStudent placeStu, String stuId, EmExamInfo examInfo, EmRegion emRegion, EmOption option, EmPlace place,
                                     int seatNo) {
        placeStu.setStudentId(stuId);
        placeStu.setExamId(examInfo.getId());
        placeStu.setExamPlaceId(place.getId());
        if (seatNo > 9) {
            placeStu.setSeatNum(seatNo + "");
        } else {
            placeStu.setSeatNum("0" + seatNo);
        }
        placeStu.setExamNumber(getExamNumber(examInfo, emRegion, option, place, placeStu.getSeatNum()));
    }

    /**
     * 年份（取末尾两位数）+考试编号（取末尾两位数）+考区（设置多少位取多少位）+
     * 考点（设置多少位取多少位）+考场（有多少位取多少位，系统生成至少需要有两位数）+
     * 座位号（有多少位取多少位，系统生成至少需要有两位数）
     *
     * @return 获得考号
     */
    private static String getExamNumber(EmExamInfo examInfo, EmRegion emRegion, EmOption option, EmPlace place, String seatNo) {
        String examNumber = "";
        String year = DateUtils.date2String(new Date(), "yyyy");
        examNumber = year.substring(2, 4);
        String examCode = examInfo.getExamCode();
        if (examCode.length() > 1) {
            examNumber = examNumber + examCode.substring(examCode.length() - 2, examCode.length());
        } else {
            examNumber = examNumber + "0" + examCode;
        }
        examNumber = examNumber + emRegion.getExamRegionCode() + option.getOptionCode() +
                place.getExamPlaceCode() + seatNo;
        return examNumber.replace(" ", "");
    }

    public static String randomStuId(Set<String> stuIdSet) {
        String[] stuIds = stuIdSet.toArray(new String[0]);
        Random r = new Random();
        int rr = r.nextInt(stuIds.length);
        return stuIds[rr];
    }

    /**
     * @param posX
     * @param posY
     * @param seatSettings
     * @return -1表示无此座位号
     */
    private static int countSeatNo(int posX, int posY, List<Integer> seatSettings) {
        if (posX > seatSettings.size() - 1) {
            return -1;
        }
        int seatNo = 0;
        for (int i = 0; i < seatSettings.size(); i++) {
            if (posX > i) {
                seatNo = seatNo + seatSettings.get(i);
            } else {
                int maxI = seatSettings.get(i);
                if (posY > maxI - 1) {
                    seatNo = -1;
                } else {
                    if (i % 2 == 0) {
                        seatNo = seatNo + maxI - posY;
                    } else {
                        seatNo = seatNo + 1 + posY;
                    }
                    break;
                }
            }
        }
        return seatNo;
    }

    public static void main(String[] args) {
        List<Integer> seats = Arrays.asList(7, 8, 8, 7); // 座位信息
        System.out.println(countSeatNo(3, 6, seats));
    }

    private static void countOptGroupMap(Map<String, Integer> map, String key, int num) {
        int value = map.get(key) - num;
        if (value <= 0) {
            map.remove(key);
        } else {
            map.put(key, value);
        }
    }

    private static Map<String, Integer> removeNullMap(Map<String, Integer> map) {
        Map<String, Integer> mm = new HashMap<>();
        for (String key : map.keySet()) {
            if (map.get(key) > 0) {
                mm.put(key, map.get(key));
            }
        }
        return mm;
    }

    private static Map<String, Float> averSchMap(Map<String, Integer> map, int placeNum) {
        Map<String, Float> mm = new HashMap<>();
        for (String key : map.keySet()) {
            int value = map.get(key);
            float aver = (float) value / placeNum;
            mm.put(key, aver);
        }
        return mm;
    }

    //找到Map中value最大的key
    private static String countMaxOptionSch(Map<String, Integer> optSchMap) {
        String maxKey = "";
        int maxValue = 0;
        for (String key : optSchMap.keySet()) {
            if (optSchMap.get(key) > maxValue) {
                maxKey = key;
                maxValue = optSchMap.get(key);
            }
        }
        return maxKey;
    }

    @Override
    protected BaseJpaRepositoryDao<EmArrange, String> getJpaDao() {
        return emArrangeDao;
    }

    @Override
    protected Class<EmArrange> getEntityClass() {
        return EmArrange.class;
    }

    @Override
    public EmArrange findByExamId(String examId) {
        return emArrangeDao.findByExamId(examId);
    }

    @Override
    public List<EmArrange> findByExamIdIn(String[] examIds) {
        return emArrangeDao.findByExamIdIn(examIds);
    }

    @Override
    public void saveArrange(String examId, String type, String[] seatNums) {
        //1.保存对应的编排设置
        List<EmPlaceSetting> settings = emPlaceSettingService.findByExamId(examId);
        if (CollectionUtils.isNotEmpty(settings))
            emPlaceSettingService.deleteAll(settings.toArray(new EmPlaceSetting[0]));
        settings = new ArrayList<>();
        EmPlaceSetting e;
        int col = 1;
        int sumSeatNum = 0;
        for (String seatNumStr : seatNums) {
            e = new EmPlaceSetting();
            e.setId(UuidUtils.generateUuid());
            e.setColumnNo(col);
            e.setSeatNum(NumberUtils.toInt(seatNumStr));
            e.setExamId(examId);
            settings.add(e);
            sumSeatNum += NumberUtils.toInt(seatNumStr);
            col++;
        }
        emPlaceSettingService.saveAll(settings.toArray(new EmPlaceSetting[0]));
        EmArrange arrange = emArrangeDao.findByExamId(examId);
        if (arrange != null)
            emArrangeDao.delete(arrange);
        arrange = new EmArrange();
        arrange.setId(UuidUtils.generateUuid());
        arrange.setExamId(examId);
        arrange.setType(type);
        arrange.setSumSeatNum(sumSeatNum);
        save(arrange);

    }

    @Override
    public void arrangeResult(String examId) {
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        EmArrange arrange = emArrangeDao.findByExamId(examId);
        //每个考场的座位数
        int seatNum = arrange.getSumSeatNum();
        //2.计算各个考点需要多少考场，并且修改exammanage_option表中的考场数
        List<EmOption> options1 = emOptionService.findByExamIdWithMaster(examId);
        //3.生成考场数据
        emPlaceService.deleteByExamId(examId);
        List<EmPlace> places1 = new ArrayList<>();
        EmPlace place1;
        for (EmOption option : options1) {
            int placeNum = option.getOptionStudentCount() / seatNum;
            if (option.getOptionStudentCount() % seatNum > 0) {
                placeNum++;
            }
            option.setOptionPlaceCount(placeNum);
            for (int i = 0; i < placeNum; i++) {
                place1 = new EmPlace();
                place1.setExamId(examId);
                place1.setOptionId(option.getId());
                place1.setSchoolId(option.getOptionSchoolId());
                place1.setCount(seatNum);
                if (i < 99) {
                    place1.setExamPlaceCode(String.format("%02d", i + 1));
                } else {
                    place1.setExamPlaceCode(i + 1 + "");
                }
                places1.add(place1);
            }
        }

        emPlaceService.saveAllEntitys(places1.toArray(new EmPlace[0]));
        emOptionService.saveAll(options1.toArray(new EmOption[0]));
        List<EmPlaceSetting> settinglist = emPlaceSettingService.findByExamId(examId);
        if (arrange == null || StringUtils.isEmpty(arrange.getType()) || CollectionUtils.isEmpty(settinglist)) {
            return;
        }

        //每个考场一半数
        float averSeatNum = (float) seatNum / 2;
        //座位排序
        List<Integer> seatSettings = new ArrayList<>();
        for (EmPlaceSetting set : settinglist) {
            seatSettings.add(set.getSeatNum());
        }
        List<EmEnrollStudent> emStulist = emEnrollStudentService.findByExamIdAndHasPass(examId, "1");
        //数据整理
        Map<String, Set<String>> schStuMap = new HashMap<>();
        Map<String, Set<String>> clsStuMap = new HashMap<>();
        Map<String, Set<String>> schClsMap = new HashMap<>();
        Map<String, String> clsSchMap = new HashMap<>();
        Map<String, Integer> clsStuNumMap = new HashMap<>();
        Map<String, Set<String>> groupStuMap = new HashMap<>();
        pushMapData(emStulist, schStuMap, clsStuMap, schClsMap, clsSchMap, clsStuNumMap, groupStuMap, arrange.getType());
        //取到所有的考点
        List<EmOption> options = emOptionService.findByExamIdWithMaster(examId);
        Map<String, EmRegion> emRegionMap = EntityUtils.getMap(emExamRegionService.findByExamIdAndUnitId(examId, examInfo.getUnitId()), "id");
        int notArrangeOptionNum = options.size();
        emPlaceStudentService.deleteByExamId(examId);

        Map<String, Integer> clsGroupMap = new HashMap<>();
        if (StringUtils.equals(arrange.getType(), "2")) {
            //按照班级不相邻进行排序，先进行group数量的计算
            countGroupType2(examId, options, clsStuNumMap, schClsMap, schStuMap, clsGroupMap);
        }


        //开始编排，一个考点一个考场的编排
        for (EmOption option : options) {
            String schId = option.getOptionSchoolId();
            EmRegion emRegion = emRegionMap.get(option.getExamRegionId());
            List<EmOptionSchool> optSchlist = emOptionSchoolService.findByOptionIdWithMaster(examId, option.getId());
            //获得考点下各个学校参与的人数分配
            Map<String, Integer> optAllSchMap = EntityUtils.getMap(optSchlist, "joinSchoolId", "joinStudentCount");
            //获得考场list
            List<EmPlace> places = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, schId, false);
            //每一组（班级/学校）在这个考点有多少人
            Map<String, Integer> groupNumMap = new HashMap<>();

            if (!StringUtils.equals(arrange.getType(), "1") && !StringUtils.equals(arrange.getType(), "2")) {
                //随机排序
                groupNumMap = removeNullMap(optAllSchMap);
                List<EmPlaceStudent> placeStulist = new ArrayList<>();
                for (EmPlace place : places) {
                    //每个考点随机取到学校取学生
                    if (groupNumMap.size() == 0) {
                        break;
                    }
                    EmPlaceStudent placeStu;
                    for (int no = 1; no <= seatNum; no++) {
                        if (!groupNumMap.isEmpty()) {
                            String joinSchId = randomStuId(groupNumMap.keySet());
                            groupNumMap.put(joinSchId, groupNumMap.get(joinSchId) - 1);
                            if (groupNumMap.get(joinSchId) < 1) {
                                groupNumMap.remove(joinSchId);
                            }
                            Set<String> stuIds = schStuMap.get(joinSchId);
                            if (!stuIds.isEmpty()) {
                                String stu = randomStuId(stuIds);
                                placeStu = new EmPlaceStudent();
                                editPlaceStu(placeStu, stu, examInfo, emRegion, option, place, no);
                                placeStulist.add(placeStu);
                                schStuMap.get(joinSchId).remove(stu);
                            }
                        }
                    }
                    //保存数据
                    if (CollectionUtils.isNotEmpty(placeStulist)) {
                        emPlaceStudentService.saveAllEntitys(placeStulist.toArray(new EmPlaceStudent[0]));
                    }
                    if (groupNumMap.isEmpty()) {
                        //人数排完则直接去排下一个考点
                        break;
                    }
                }
                continue;
            } else if (StringUtils.equals(arrange.getType(), "1")) {
                //同校不相邻 groupId为schId
                groupNumMap = removeNullMap(optAllSchMap);
            } else {
                if (notArrangeOptionNum <= 0) {
                    break;
                }
                //同班不相邻 groupId 为 clsId
                Map<String, Integer> optSchMap = removeNullMap(optAllSchMap);
                if (optSchMap.isEmpty()) {
                    continue;
                }
                //这个考点有哪些班级参与
                Set<String> clsAllIds = new HashSet<>();
                for (String joinSchId : optSchMap.keySet()) {
                    Set<String> clsIds = schClsMap.get(joinSchId);
                    for (String clsId : clsIds) {
                        clsAllIds.add(clsId);
                    }
                }

                for (String clsId : clsAllIds) {
                    if (!clsGroupMap.containsKey(option.getId() + "," + clsId)) {
                        continue;
                    }
                    int num = clsGroupMap.get(option.getId() + "," + clsId);
                    groupNumMap.put(clsId, num);
                }
            }
            //未编排的考场数
            int notArrangePlaceNum = places.size();
            for (EmPlace place : places) {
                if (notArrangePlaceNum <= 0) {
                    break;
                }
                //改考场未安排的座位数
                int notArrangeSeatNum = arrange.getSumSeatNum();
                //分配一个考场各个颗粒的人数
                Map<String, Integer> groupMap = new HashMap<>();
                List<Group> studentGroups = new ArrayList<>();
                //计算各考场人数分配
                allotPlaceNum(groupNumMap, notArrangePlaceNum, notArrangeSeatNum, averSeatNum, groupMap);
                for (String groupId : groupMap.keySet()) {
                    Group g = new Group(groupId, groupMap.get(groupId));
                    studentGroups.add(g);
                }
                List<Seat> placeSeats = Coloring.createSeats(seatSettings, studentGroups);
                List<EmPlaceStudent> placeStulist = new ArrayList<>();
                List<Integer> seatNos = new ArrayList<>();
                EmPlaceStudent placeStu;
                for (Seat e : placeSeats) {
                    if (e.getState() == -2) {
                        continue;
                    }
                    int seatNo = countSeatNo(e.getPosX(), e.getPosY(), seatSettings);
                    if (e.getState() == 1) {
                        placeStu = new EmPlaceStudent();
                        Set<String> stuIdSet = groupStuMap.get(e.getGroupId());
                        if (CollectionUtils.isEmpty(stuIdSet)) {
                            seatNos.add(seatNo);
                            groupStuMap.remove(e.getGroupId());
                            continue;
                        }
                        String stuId = randomStuId(stuIdSet);
                        stuIdSet.remove(stuId);
                        if (!stuIdSet.isEmpty()) {
                            groupStuMap.put(e.getGroupId(), stuIdSet);
                        } else {
                            groupStuMap.remove(e.getGroupId());
                        }
                        editPlaceStu(placeStu, stuId, examInfo, emRegion, option, place, seatNo);
                        placeStulist.add(placeStu);
                        countOptGroupMap(groupMap, e.getGroupId(), 1);
                    } else {
                        //有冲突  没有排完整，冲突最小，取最有解
                        seatNos.add(seatNo);
                    }
                }
                for (int seatNo : seatNos) {
                    if (groupMap.size() == 0) {
                        break;
                    }
                    String groupId = randomStuId(groupMap.keySet());
                    Set<String> stuIdSet = groupStuMap.get(groupId);
                    if (stuIdSet == null) {
                        groupMap.remove(groupId);
                        continue;
                    }
                    String stuId = randomStuId(stuIdSet);
                    stuIdSet.remove(stuId);
                    if (!stuIdSet.isEmpty()) {
                        groupStuMap.put(groupId, stuIdSet);
                    } else {
                        groupStuMap.remove(groupId);
                    }
                    countOptGroupMap(groupMap, groupId, 1);
                    placeStu = new EmPlaceStudent();
                    editPlaceStu(placeStu, stuId, examInfo, emRegion, option, place, seatNo);
                    placeStulist.add(placeStu);
                }
//				System.out.println(option.getOptionName() + "考点,需要安排"+option.getOptionStudentCount()+"考场数量"+option.getOptionPlaceCount()+">>>>"+place.getExamPlaceCode()+"考场"+placeStulist.size());
                if (CollectionUtils.isNotEmpty(placeStulist)) {
                    emPlaceStudentService.saveAllEntitys(placeStulist.toArray(new EmPlaceStudent[0]));
                }
                notArrangePlaceNum--;
            }
        }
    }

    private void countGroupType2(String examId, List<EmOption> options, Map<String, Integer> clsStuNumMap,
                                 Map<String, Set<String>> schClsMap, Map<String, Set<String>> schStuMap, Map<String, Integer> clsGroupMap) {
        List<EmOptionSchool> optSchs = emOptionSchoolService.findByExamId(examId);
        Map<String, List<EmOptionSchool>> schOptsMap = new HashMap<>();
        for (EmOptionSchool opt : optSchs) {
            if (!schOptsMap.containsKey(opt.getJoinSchoolId())) {
                schOptsMap.put(opt.getJoinSchoolId(), new ArrayList<>());
            }
            schOptsMap.get(opt.getJoinSchoolId()).add(opt);
        }
        for (String schId : schOptsMap.keySet()) {
            Set<String> clsIds = schClsMap.get(schId);//学校参加考试班级
            if (clsIds == null) {
                continue;
            }
            int schNum = schStuMap.get(schId).size();//学校报名总人数
            Map<String, Integer> surplusClsMap = new HashMap<>();
            for (String clsId : clsIds) {
                surplusClsMap.put(clsId, clsStuNumMap.get(clsId));
            }
            List<EmOptionSchool> optSchlist = schOptsMap.get(schId);
            Map<String, Integer> noonOptNumMap = new HashMap<>();
            for (EmOptionSchool opt : optSchlist) {
                int optStuNum = opt.getJoinStudentCount();//考点安排学生数
                int noonOptStuNum = opt.getJoinStudentCount();//考点没有安排的学生数量
                for (String clsId : clsIds) {
                    if (surplusClsMap.get(clsId) <= 0) {
                        continue;
                    }
                    int clsNum = clsStuNumMap.get(clsId);
                    int abNum = clsNum * optStuNum / schNum;
                    if (abNum > 0) {
                        clsGroupMap.put(opt.getOptionId() + "," + clsId, abNum);
                    }
                    noonOptStuNum = noonOptStuNum - abNum;
                    surplusClsMap.put(clsId, surplusClsMap.get(clsId) - abNum);
                    removeNullMap(surplusClsMap);
                }
                if (noonOptStuNum > 0) {
                    noonOptNumMap.put(opt.getOptionId(), noonOptStuNum);
                }
            }
            for (String optId : noonOptNumMap.keySet()) {
                int noonOptStuNum = noonOptNumMap.get(optId);
                for (; noonOptStuNum > 0; noonOptStuNum--) {
                    String clsId = countMaxOptionSch(surplusClsMap);
                    int surplusNum = surplusClsMap.get(clsId);
                    if (surplusNum <= 0) {
//						System.out.println("clsId班级安排人数时，出现人数不够排现象");
                        break;
                    }
                    if (clsGroupMap.containsKey(optId + "," + clsId)) {
                        clsGroupMap.put(optId + "," + clsId, clsGroupMap.get(optId + "," + clsId) + 1);
                    } else {
                        clsGroupMap.put(optId + "," + clsId, 1);
                    }
                    surplusClsMap.put(clsId, surplusClsMap.get(clsId) - 1);
                    removeNullMap(surplusClsMap);
                }
            }
        }
    }

    //计算一个考场的人数分配
    private void allotPlaceNum(Map<String, Integer> groupNumMap, int notArrangePlaceNum, int notArrangeSeatNum, float averSeatNum, Map<String, Integer> groupMap) {
        String maxGroupId = "";
        Set<String> groupIds = new HashSet<>();
        Map<String, Float> averSchMap = averSchMap(groupNumMap, notArrangePlaceNum);
        Set<String> keySet = new HashSet<>();
        for (String a : groupNumMap.keySet()) {
            keySet.add(a);
        }
        for (String gId : keySet) {
            if (!groupNumMap.containsKey(gId)) {
                continue;
            }
            if (notArrangeSeatNum <= 0) {
                break;
            }
            int num = 0;//安排进这个考场的人数
            float aver = averSchMap.get(gId);
            if (aver > averSeatNum) {
                maxGroupId = gId;
                num = (int) Math.floor(averSeatNum);
            } else {
                num = (int) Math.floor(aver);
            }
            if (num == 0) {
                num = 1;
            }
            if (notArrangeSeatNum >= num) {
            } else {
                num = notArrangeSeatNum;
            }
            notArrangeSeatNum = notArrangeSeatNum - num;
            groupMap.put(gId, num);
            groupIds.add(gId);
            //更新该考点未编排的学生数
            countOptGroupMap(groupNumMap, gId, num);
        }
        //这个考点有未安排的考生，并且这个考场没有排满的情况下继续安排
        if (notArrangeSeatNum > 0 && groupNumMap.size() > 0) {
            boolean end = false;
            String[] groupIdArrs = groupNumMap.keySet().toArray(new String[0]);
            int j = 0;
            int x = 0;
            for (; !end; ) {
                x++;
                if (x == 500) {
                    //防止死循环
                    break;
                }
                if (j >= groupIdArrs.length) {
                    j = 0;
                }
                if (!groupNumMap.containsKey(groupIdArrs[j])) {
                    j++;
                    continue;
                }
                if (!StringUtils.equals(groupIdArrs[j], maxGroupId)) {
                    groupMap.put(groupIdArrs[j], groupMap.get(groupIdArrs[j]) + 1);
                    notArrangeSeatNum--;
                    //更新该考点未编排的学生数
                    countOptGroupMap(groupNumMap, groupIdArrs[j], 1);
                    groupIdArrs = groupNumMap.keySet().toArray(new String[0]);
                } else {
                    if (groupIdArrs.length == 1) {
                        groupMap.put(groupIdArrs[j], groupMap.get(groupIdArrs[j]) + 1);
                        notArrangeSeatNum--;
                        //更新该考点未编排的学生数
                        countOptGroupMap(groupNumMap, groupIdArrs[j], 1);
                        groupIdArrs = groupNumMap.keySet().toArray(new String[0]);
                    }
                }
                j++;
                if (notArrangeSeatNum == 0 || groupNumMap.size() == 0) {
                    end = true;
                }
            }
        }
    }

    private void pushMapData(List<EmEnrollStudent> emStulist, Map<String, Set<String>> schStuMap,
                             Map<String, Set<String>> clsStuMap, Map<String, Set<String>> schClsMap, Map<String, String> clsSchMap,
                             Map<String, Integer> clsStuNumMap, Map<String, Set<String>> groupStuMap, String type) {
        for (EmEnrollStudent emStu : emStulist) {
            clsSchMap.put(emStu.getClassId(), emStu.getSchoolId());
            clsStuNumMap.put(emStu.getClassId(), clsStuNumMap.containsKey(emStu.getClassId()) ? clsStuNumMap.get(emStu.getClassId()) + 1 : 1);

            if (!schClsMap.containsKey(emStu.getSchoolId())) {
                schClsMap.put(emStu.getSchoolId(), new HashSet<>());
            }
            schClsMap.get(emStu.getSchoolId()).add(emStu.getClassId());

            if (!schStuMap.containsKey(emStu.getSchoolId())) {
                schStuMap.put(emStu.getSchoolId(), new HashSet<>());
            }
            schStuMap.get(emStu.getSchoolId()).add(emStu.getStudentId());

            if (!clsStuMap.containsKey(emStu.getClassId())) {
                clsStuMap.put(emStu.getClassId(), new HashSet<>());
            }
            clsStuMap.get(emStu.getClassId()).add(emStu.getStudentId());
            String groupId = "";
            if (StringUtils.equals(type, "1")) {
                groupId = emStu.getSchoolId();
            } else if (StringUtils.equals(type, "2")) {
                groupId = emStu.getClassId();
            } else {
                continue;
            }
            if (!groupStuMap.containsKey(groupId)) {
                groupStuMap.put(groupId, new HashSet<>());
            }
            groupStuMap.get(groupId).add(emStu.getStudentId());
        }
    }

}



