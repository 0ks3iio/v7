<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>新增调课</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/font-awesome.css" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/mui.css"/>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/mui.picker.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/pages.css"/>
    <script src="${request.contextPath}/static/mui/js/mui.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${request.contextPath}/static/mui/js/mui.picker.js" type="text/javascript" charset="utf-8"></script>
    <script src="${request.contextPath}/static/mui/js/jquery-1.9.1.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${request.contextPath}/static/mui/js/common.js"></script>
    <script src="${request.contextPath}/static/mui/js/weike.js"></script>
    <script src="${request.contextPath}/static/mui/js/myWeike.js"></script>
</head>
<body>
<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" id="cancelId" style="display:none"></a>
<div class="layer layer-index">
    <#--<header class="mui-bar mui-bar-nav">
        <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
        <h1 class="mui-title">申请调课</h1>
    </header>-->
    <div class="mui-content add-form" style="padding-bottom: 51px;">
        <div class="mui-card edu-switch-list">
            <ul class="mui-table-view" style="margin: 0;">
                <#if isAdmin>
                    <li class="mui-table-view-cell">
                        <a class="mui-navigate-right js-teacher" onclick="teacherLayer(this)">
                            <span class="f-left">需调教师：</span>
                            <span class="f-right mr-30 teachercell"></span>
                        </a>
                    </li>
                </#if>
                <li class="mui-table-view-cell">
                    <a class="mui-navigate-right js-course" onclick="courseLayer(this)" classId="">
                        <span class="f-left">需调节次：</span>
                        <span class="f-right mr-30 subjectcell"></span>
                    </a>
                </li>
                <li class="mui-table-view-cell">
                    <a class="mui-navigate-right js-switch" onclick="switchLayer(this)">
                        <span class="f-left">被调节次：</span>
                        <span class="f-right mr-30 subjectcell"></span>
                    </a>
                </li>
                <li class="fn-flex mui-text-center" style="background: #f9f9f9;">
                    <a class="fn-flex-auto f-16 mt-10 mb-10 btn-delete" href="#" onclick="switchDelete(this)">删除</a>
                    <a class="fn-flex-auto f-16 mt-10 mb-10 c-blue" href="#" onclick="copySwitchLayer(this)"
                       style="border-left: 1px solid #d7d7d7;">复制到</a>
                </li>
            </ul>
            <span id="addRow" class="mui-table-view" style="margin: 0;display: none"></span>
        </div>
        <ul class="mui-table-view">
            <li class="mui-table-view-cell mui-text-center" id="addSwitch">
                <span class="f-16 mui-inline"><i class="c-blue fa fa-plus-circle f-24 mr-5 f-left"></i>添加</span>
            </li>
        </ul>
        <ul class="mui-table-view">
            <li class="mui-table-view-cell">
                <span class="f-16">备注</span>
            </li>
            <li class="mui-table-view-cell">
                <textarea id="remark" name="" rows="" cols="" placeholder="请输入" class="f-14"
                          style="border: none;margin: 0;"></textarea>
            </li>
        </ul>
    </div>
    <nav class="mui-bar mui-bar-tab">
        <a class="mui-tab-item f-16 classSwitchCommit" href="#" style="background: #2f7bff;color: #fff;">确定</a>
    </nav>
</div>

<!-- 需调课程 -->
<div class="layer layer-course" style="display: none">
    <#--<header class="mui-bar mui-bar-nav">
        <a class="mui-icon mui-icon-left-nav mui-pull-left" onclick="goApplyIndex(this)"></a>
        <h1 class="mui-title">课程表</h1>
    </header>-->
    <div class="tabMenu01 tab_menu mui-bg-white" style="position:fixed;z-index:10;left:0px;right:0px;">
        <ul class="fn-flex" id="weekSelect" week="${nowWeek.week}">
            <#list weekInfoList as item>
                <li class="mainlevel fn-flex-auto <#if item_index == 0>active</#if>" week="${item.week}"><a class="mainlevel_a" href="#">第${item.week}周</a></li>
            </#list>
        </ul>
    </div>
    <div class="mui-content mui-bg-white" style="padding-top:45px;padding-bottom: 51px;">
        <div class="tabBox01 tab_box">
            <div>
                <div class="ml-15 mr-15">
                    <div class="mt-10 mb-10 c-666" id="selectTeacherName">任课教师：${teacherName}</div>
                    <!--<div class="mt-10 mb-10 mui-clearfix">
                        <span class="c-666 f-left">高一（1）班</span>
                        <span class="c-blue f-right">数学（张思民）</span>
                    </div>-->
                    <table class="course-table">
                        <thead>
                        <tr>
                            <th width="5%"></th>
                            <th width="5%"></th>
                            <th width="18%" id="week_0">周一</th>
                            <th width="18%" id="week_1">周二</th>
                            <th width="18%" id="week_2">周三</th>
                            <th width="18%" id="week_3">周四</th>
                            <th width="18%" id="week_4">周五</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#if morn != 0>
                            <#list 1..morn as mornLesson>
                                <tr>
                                    <#if mornLesson == 1>
                                        <td rowspan="${morn}">早自习</td>
                                    </#if>
                                    <td>${mornLesson}</td>
                                    <td id="0_1_${mornLesson}" class="edit timetabledetile"></td>
                                    <td id="1_1_${mornLesson}" class="edit timetabledetile"></td>
                                    <td id="2_1_${mornLesson}" class="edit timetabledetile"></td>
                                    <td id="3_1_${mornLesson}" class="edit timetabledetile"></td>
                                    <td id="4_1_${mornLesson}" class="edit timetabledetile"></td>
                                </tr>
                            </#list>
                        </#if>
                        <#list 1..am as amLesson>
                            <tr>
                                <#if amLesson == 1>
                                    <td rowspan="${am}">上午</td>
                                </#if>
                                <td>${amLesson}</td>
                                <td id="0_2_${amLesson}" class="edit timetabledetile"></td>
                                <td id="1_2_${amLesson}" class="edit timetabledetile"></td>
                                <td id="2_2_${amLesson}" class="edit timetabledetile"></td>
                                <td id="3_2_${amLesson}" class="edit timetabledetile"></td>
                                <td id="4_2_${amLesson}" class="edit timetabledetile"></td>
                            </tr>
                        </#list>
                        <#list 1..pm as pmLesson>
                            <tr>
                                <#if pmLesson == 1>
                                    <td rowspan="${pm}">下午</td>
                                </#if>
                                <td>${pmLesson}</td>
                                <td id="0_3_${pmLesson}" class="edit timetabledetile"></td>
                                <td id="1_3_${pmLesson}" class="edit timetabledetile"></td>
                                <td id="2_3_${pmLesson}" class="edit timetabledetile"></td>
                                <td id="3_3_${pmLesson}" class="edit timetabledetile"></td>
                                <td id="4_3_${pmLesson}" class="edit timetabledetile"></td>
                            </tr>
                        </#list>
                        <#if night != 0>
                            <#list 1..night as nightLesson>
                                <tr>
                                    <#if nightLesson == 1>
                                        <td rowspan="${night}">晚自习</td>
                                    </#if>
                                    <td>${nightLesson}</td>
                                    <td id="0_4_${nightLesson}" class="edit timetabledetile"></td>
                                    <td id="1_4_${nightLesson}" class="edit timetabledetile"></td>
                                    <td id="2_4_${nightLesson}" class="edit timetabledetile"></td>
                                    <td id="3_4_${nightLesson}" class="edit timetabledetile"></td>
                                    <td id="4_4_${nightLesson}" class="edit timetabledetile"></td>
                                </tr>
                            </#list>
                        </#if>
                        </tbody>
                        <#--<tr>
                            <td>2</td>
                            <td class="active">1班</td>
                            <td class="replace">1班<span>代</span></td>
                            <td class="warn">1班</td>
                            <td class="edit">1班</td>
                            <td class="edit">1班</td>
                        </tr>-->
                    </table>
                </div>
            </div>
        </div>
    </div>
    <#--<nav class="mui-bar mui-bar-tab fn-flex" style="line-height: 50px;">
    	<span class="fn-flex-auto mui-checkbox mui-left f-16">
		    <label>全选</label>
		    <input name="checkbox" type="checkbox" style="top: 10px;">
    	</span>
        <a class="mui-tab-item f-16 fn-flex-auto" href="#" style="background: #2f7bff;color: #fff;">确定(2)</a>
    </nav>-->
</div>

<!-- 被调课程 -->
<input type="hidden" id="maxWeek" value="${maxWeek?default(1)}">
<div class="layer layer-switch" style="display: none">
    <#--<header class="mui-bar mui-bar-nav">
        <a class="mui-icon mui-icon-left-nav mui-pull-left" onclick="goApplyIndex(this)"></a>
        <h1 class="mui-title">课程表</h1>
    </header>-->
    <div class="tabMenu01 tab_menu mui-bg-white" style="position:fixed;z-index:10;left:0px;right:0px;">
        <ul class="fn-flex ul-switch" week="">
            <li class="mainlevel fn-flex-auto active" id="thisWeek"><a class="mainlevel_a" href="#"></a></li>
            <li class="mainlevel fn-flex-auto" id="nextWeek"><a class="mainlevel_a" href="#"></a></li>
        </ul>
    </div>
    <div class="mui-content mui-bg-white" style="padding-top:45px;padding-bottom: 51px;">
        <div class="tabBox01 tab_box">
            <div>
                <div class="ml-15 mr-15">
                    <div class="mt-10 mb-10 c-666" id="className">所选班级：</div>
                    <!--<div class="mt-10 mb-10 mui-clearfix">
                        <span class="c-666 f-left">高一（1）班</span>
                        <span class="c-blue f-right">数学（张思民）</span>
                    </div>-->
                    <table class="course-table">
                        <thead>
                        <tr>
                            <th width="5%"></th>
                            <th width="5%"></th>
                            <th width="18%">周一</th>
                            <th width="18%">周二</th>
                            <th width="18%">周三</th>
                            <th width="18%">周四</th>
                            <th width="18%">周五</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#if morn != 0>
                            <#list 1..morn as mornLesson>
                                <tr>
                                    <#if mornLesson == 1>
                                        <td rowspan="${morn}">早自习</td>
                                    </#if>
                                    <td>${mornLesson}</td>
                                    <td id="_0_1_${mornLesson}" class="edit timetabledetile"></td>
                                    <td id="_1_1_${mornLesson}" class="edit timetabledetile"></td>
                                    <td id="_2_1_${mornLesson}" class="edit timetabledetile"></td>
                                    <td id="_3_1_${mornLesson}" class="edit timetabledetile"></td>
                                    <td id="_4_1_${mornLesson}" class="edit timetabledetile"></td>
                                </tr>
                            </#list>
                        </#if>
                        <#list 1..am as amLesson>
                        <tr>
                            <#if amLesson == 1>
                                <td rowspan="${am}">上午</td>
                            </#if>
                            <td>${amLesson}</td>
                            <td id="_0_2_${amLesson}" class="edit timetabledetile"></td>
                            <td id="_1_2_${amLesson}" class="edit timetabledetile"></td>
                            <td id="_2_2_${amLesson}" class="edit timetabledetile"></td>
                            <td id="_3_2_${amLesson}" class="edit timetabledetile"></td>
                            <td id="_4_2_${amLesson}" class="edit timetabledetile"></td>
                        </tr>
                        </#list>
                        <#list 1..pm as pmLesson>
                        <tr>
                            <#if pmLesson == 1>
                                <td rowspan="${pm}">下午</td>
                            </#if>
                            <td>${pmLesson}</td>
                            <td id="_0_3_${pmLesson}" class="edit timetabledetile"></td>
                            <td id="_1_3_${pmLesson}" class="edit timetabledetile"></td>
                            <td id="_2_3_${pmLesson}" class="edit timetabledetile"></td>
                            <td id="_3_3_${pmLesson}" class="edit timetabledetile"></td>
                            <td id="_4_3_${pmLesson}" class="edit timetabledetile"></td>
                        </tr>
                        </#list>
                        <#if night != 0>
                            <#list 1..night as nightLesson>
                                <tr>
                                    <#if nightLesson == 1>
                                        <td rowspan="${night}">晚自习</td>
                                    </#if>
                                    <td>${nightLesson}</td>
                                    <td id="_0_4_${nightLesson}" class="edit timetabledetile"></td>
                                    <td id="_1_4_${nightLesson}" class="edit timetabledetile"></td>
                                    <td id="_2_4_${nightLesson}" class="edit timetabledetile"></td>
                                    <td id="_3_4_${nightLesson}" class="edit timetabledetile"></td>
                                    <td id="_4_4_${nightLesson}" class="edit timetabledetile"></td>
                                </tr>
                            </#list>
                        </#if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <#--<nav class="mui-bar mui-bar-tab fn-flex" style="line-height: 50px;">
    	<span class="fn-flex-auto mui-checkbox mui-left f-16">
		    <label>全选</label>
		    <input name="checkbox" type="checkbox" style="top: 10px;">
    	</span>
        <a class="mui-tab-item f-16 fn-flex-auto" href="#" style="background: #2f7bff;color: #fff;">确定(2)</a>
    </nav>-->
</div>

<!-- 复制 -->
<div class="layer layer-copy" style="display: none">
    <div class="mui-content add-form" style="padding-bottom: 51px;">
        <p class="c-333 ml-20 mr-20 mt-10">复制时将默认保持调课的周次跨距。比如跨周调课，复制时也将跨周。</p>
        <p class="ml-20">需调课程周次复制到</p>
        <ul class="mui-table-view" style="margin: 0;">
            <#if weekInfoList?exists && weekInfoList?size gt 0>
                <#list weekInfoList as item>
                <li class="mui-table-view-cell mui-checkbox mui-left">
                    <span class="f-16">第${item.week}周</span>
                    <input class="copyweeks" week="${item.week}" name="checkbox" type="checkbox">
                </li>
                </#list>
            </#if>
        </ul>
    </div>
    <nav class="mui-bar mui-bar-tab">
        <a id="copyCommit" class="mui-tab-item f-16" href="#" style="background: #2f7bff;color: #fff;">确定</a>
    </nav>
</div>

<#if isAdmin>
    <!-- 教师列表 -->
    <div class="layer layer-teacher" style="display: none">
        <div class="search-container teacherDiv">
            <input type="hidden" id="opopId" value="">
            <div class="mui-content-padded">
                <div class="mui-input-row mui-search">
                    <input type="search" id="searchTeacherName" class="mui-input-clear" placeholder="请输入教师名称">
                </div>
            </div>
        </div>
        <div class="mui-content teacherContent" style="padding-top: 40px;">

        </div>
    </div>
</#if>
<script>

    var adjustObj;
    var switchObj;
    var copyObj;
    var teacherObj;
    var isSubmit = false;

    $(function () {

        $('#cancelId').off('click').on('click',function(){
            if($(".layer-index").is(":hidden")){
                $(".layer-copy").hide();
                $(".layer-teacher").hide();
                $(".layer-course").hide();
                $(".layer-switch").hide();
                $(".layer-index").show();
            }else{
                var url = '${request.contextPath}/mobile/open/adjusttipsay/showList/page?unitId=${unitId!}&userId=${userId!}&teacherId=${teacherId!}&adminType=${adminType!}&type=3';
                loadByHref(url);
            }
        });

        var $div_li1=$(".tab_menu > ul > li");
        $div_li1.click(function(){
            $(this).addClass("active").siblings().removeClass("active");
            $("#weekSelect").attr("week", $(this).attr("week"));
            timeTableDetile();
        });

        $(".classSwitchCommit").on("tap", function () {
            console.log(isSubmit);
            if (isSubmit) {
                return;
            }
            isSubmit = true;
            var adjustIds = $(".edu-switch-list ul:eq(0) .js-course").attr("courseScheduleId");
            var beenAdjustIds = $(".edu-switch-list ul:eq(0) .js-switch").attr("courseScheduleId");
            var courseScheduleArr = [];
            var error = false;
            courseScheduleArr.push(adjustIds);
            courseScheduleArr.push(beenAdjustIds);
            if (!adjustIds || !beenAdjustIds) {
                toastMsg("未选择需调节次或被调节次", 2000);
                isSubmit = false;
                return;
            }
            $(".edu-switch-list ul:gt(0) .js-course").each(function () {
                var adjustIdTmp = $(this).attr("courseScheduleId");
                var beenAdjustIdTmp = $(this).parents("ul").find(".js-switch").attr("courseScheduleId");
                if (!adjustIdTmp || !beenAdjustIdTmp) {
                    toastMsg("未选择需调节次或被调节次", 2000);
                    error = true;
                    return;
                }
                if (courseScheduleArr.indexOf(adjustIdTmp) != -1) {
                    $(this).children(".subjectcell").text("该课程重复");
                    error = true;
                }
                courseScheduleArr.push(adjustIdTmp);
                if (courseScheduleArr.indexOf(beenAdjustIdTmp) != -1) {
                    $(this).parents("ul").find(".js-switch").children(".subjectcell").text("该课程重复");
                    error = true;
                }
                courseScheduleArr.push(beenAdjustIdTmp);
                adjustIds += "," + adjustIdTmp;
                beenAdjustIds += "," + beenAdjustIdTmp;
            });
            if (error) {
                isSubmit = false;
                return;
            }
            var remark = $("#remark").val();
            if (remark.length > 50) {
                toastMsg("备注请勿超过50个字符", 2000);
                isSubmit = false;
                return;
            }
            console.log("adjustIds : " + adjustIds);
            console.log("beenAdjustIds : " + beenAdjustIds);
            $.ajax({
                <#if isAdmin>
                url:"${request.contextPath}/mobile/open/adjusttipsay/manage/arrange",
                <#else>
                url:"${request.contextPath}/mobile/open/adjusttipsay/apply/commit",
                </#if>
                data:{"unitId":"${unitId!}",
                    "userId":"${userId!}",
                    "teacherId":"${teacherId!}",
                    "adjustIds":adjustIds,
                    "beenAdjustIds":beenAdjustIds,
                    "remark":remark,
                    "adminType":"${adminType}"},
                type:"post",
                success:function (result) {
                    var jsonResult = JSON.parse(result);
                    if (jsonResult.success) {
                        <#if isAdmin>
                            loadByHref("${request.contextPath}/mobile/open/adjusttipsay/showList/page?unitId=${unitId!}&userId=${userId!}&teacherId=${teacherId!}&adminType=${adminType!}&type=3&from=02");
                        <#else>
                            loadByHref("${request.contextPath}/mobile/open/adjusttipsay/showList/page?unitId=${unitId!}&userId=${userId!}&teacherId=${teacherId!}&adminType=${adminType!}&type=3");
                        </#if>
                        toastMsg("提交成功，请等待审核", 2000);
                    } else {
                        toastMsg(jsonResult.msg, 2000);
                    }
                    isSubmit = false;
                },
                error:function(XMLHttpRequest, textStatus, errorThrown){
                    isSubmit = false;
                }
            });
        });

        $("#thisWeek").on("tap", function () {
            if (!$(this).hasClass("active")) {
                $(this).addClass("active");
                $(this).next().removeClass("active");
                $(".ul-switch").attr("week",$(this).attr("week"));
                switchTableDetile();
            }
        });

        $("#nextWeek").on("tap", function () {
            if (!$(this).hasClass("active")) {
                $(this).addClass("active");
                $(this).prev().removeClass("active");
                $(".ul-switch").attr("week",$(this).attr("week"));
                switchTableDetile();
            }
        });

        $("#weekSelect").children("li").on("tap", function () {
            if ($(this).hasClass("active")) {
                return;
            }
            $(this).siblings().removeClass("active");
            $(this).addClass("active");
            $("#weekSelect").attr("week", $(this).attr("week"));
        });

        $("#addSwitch").on("tap", function () {
            <#if isAdmin>
            $("#addRow").before("<ul class=\"mui-table-view\" style=\"margin: 0;\">\n" +
                    "                <li class=\"mui-table-view-cell\">\n" +
                    "                    <a class=\"mui-navigate-right js-teacher\" onclick=\"teacherLayer(this)\">\n" +
                    "                        <span class=\"f-left\">需调教师：</span>\n" +
                    "                        <span class=\"f-right mr-30 teachercell\"></span>\n" +
                    "                    </a>\n" +
                    "                </li>" +
                    "                <li class=\"mui-table-view-cell\">\n" +
                    "                    <a class=\"mui-navigate-right js-course\" onclick=\"courseLayer(this)\">\n" +
                    "                        <span class=\"f-left\">需调节次：</span>\n" +
                    "                        <span class=\"f-right mr-30 subjectcell\"></span>\n" +
                    "                    </a>\n" +
                    "                </li>\n" +
                    "                <li class=\"mui-table-view-cell\">\n" +
                    "                    <a class=\"mui-navigate-right js-switch\"  onclick=\"switchLayer(this)\">\n" +
                    "                        <span class=\"f-left\">被调节次：</span>\n" +
                    "                        <span class=\"f-right mr-30 subjectcell\"></span>\n" +
                    "                    </a>\n" +
                    "                </li>\n" +
                    "                <li class=\"fn-flex mui-text-center\" style=\"background: #f9f9f9;\">\n" +
                    "                    <a class=\"fn-flex-auto f-16 mt-10 mb-10\" href=\"#\" onclick=\"switchDelete(this)\">删除</a>\n" +
                    "                    <a class=\"fn-flex-auto f-16 mt-10 mb-10 c-blue\" href=\"#\"\n" +
                    "                       style=\"border-left: 1px solid #d7d7d7;\" onclick=\"copySwitchLayer(this)\">复制到</a>\n" +
                    "                </li>\n" +
                    "            </ul>");
            <#else>
            $("#addRow").before("<ul class=\"mui-table-view\" style=\"margin: 0;\">\n" +
                    "                <li class=\"mui-table-view-cell\">\n" +
                    "                    <a class=\"mui-navigate-right js-course\" onclick=\"courseLayer(this)\">\n" +
                    "                        <span class=\"f-left\">需调节次：</span>\n" +
                    "                        <span class=\"f-right mr-30 subjectcell\"></span>\n" +
                    "                    </a>\n" +
                    "                </li>\n" +
                    "                <li class=\"mui-table-view-cell\">\n" +
                    "                    <a class=\"mui-navigate-right js-switch\"  onclick=\"switchLayer(this)\">\n" +
                    "                        <span class=\"f-left\">被调节次：</span>\n" +
                    "                        <span class=\"f-right mr-30 subjectcell\"></span>\n" +
                    "                    </a>\n" +
                    "                </li>\n" +
                    "                <li class=\"fn-flex mui-text-center\" style=\"background: #f9f9f9;\">\n" +
                    "                    <a class=\"fn-flex-auto f-16 mt-10 mb-10\" href=\"#\" onclick=\"switchDelete(this)\">删除</a>\n" +
                    "                    <a class=\"fn-flex-auto f-16 mt-10 mb-10 c-blue\" href=\"#\"\n" +
                    "                       style=\"border-left: 1px solid #d7d7d7;\" onclick=\"copySwitchLayer(this)\">复制到</a>\n" +
                    "                </li>\n" +
                    "            </ul>");
            </#if>
        });

        $("#copyCommit").on("tap", function () {
            console.log("copyShowCourse");
            var adjustId = $(copyObj).parents("ul").find(".js-course").attr("courseScheduleId");
            var beenAdjustId = $(copyObj).parents("ul").find(".js-switch").attr("courseScheduleId");
            <#if isAdmin>
            var teacherId = $(copyObj).parents("ul").find(".js-teacher").attr("teacherId");
            </#if>
            var copyWeeks = "";
            $(".copyweeks").each(function () {
                if ($(this).prop("checked")) {
                    copyWeeks += $(this).attr("week") + ",";
                }
            });
            console.log(copyWeeks);
            if (copyWeeks == "") {
                $(".layer-copy").hide();
                $(".layer-index").show();
                return;
            }
            $.ajax({
                url:"${request.contextPath}/mobile/open/adjusttipsay/apply/copyswitch",
                data:{"adjustId":adjustId,
                    "beenAdjustId":beenAdjustId,
                    "copyWeeks":copyWeeks,
                    "teacherId":<#if isAdmin>teacherId<#else>"${teacherId!}"</#if>,
                    "nowAcadyear":"${acadyear}",
                    "nowSemester":"${semester}"},
                success:function (result) {
                    var jsonResult = JSON.parse(result);
                    if (jsonResult.success) {
                        var jsonValue = JSON.parse(jsonResult.businessValue);
                        for (var i = 0; i < jsonValue.length; i += 2) {
                            var subjectAdjust = "第" + jsonValue[i].weekOfWorktime + "周" + $("#week_" + jsonValue[i].dayOfWeek).text();
                            if ("1" == jsonValue[i].periodInterval) {
                                subjectAdjust += "早自习第" + jsonValue[i].period + "节 - " + jsonValue[i].className;
                            } else if ("2" == jsonValue[i].periodInterval) {
                                subjectAdjust += "上午第" + jsonValue[i].period + "节 - " + jsonValue[i].className;
                            } else if ("3" == jsonValue[i].periodInterval) {
                                subjectAdjust += "下午第" + jsonValue[i].period + "节 - " + jsonValue[i].className;
                            } else if ("4" == jsonValue[i].periodInterval) {
                                subjectAdjust += "晚自习第" + jsonValue[i].period + "节 - " + jsonValue[i].className;
                            }
                            var subjectBeenAdjust = "第" + jsonValue[i + 1].weekOfWorktime + "周" + $("#week_" + jsonValue[i + 1].dayOfWeek).text();
                            if ("1" == jsonValue[i + 1].periodInterval) {
                                subjectBeenAdjust += "早自习第" + jsonValue[i + 1].period + "节 - " + jsonValue[i + 1].subjectName;
                            } else if ("2" == jsonValue[i + 1].periodInterval) {
                                subjectBeenAdjust += "上午第" + jsonValue[i + 1].period + "节 - " + jsonValue[i + 1].subjectName;
                            } else if ("3" == jsonValue[i + 1].periodInterval) {
                                subjectBeenAdjust += "下午第" + jsonValue[i + 1].period + "节 - " + jsonValue[i + 1].subjectName;
                            } else if ("4" == jsonValue[i + 1].periodInterval) {
                                subjectBeenAdjust += "晚自习第" + jsonValue[i + 1].period + "节 - " + jsonValue[i + 1].subjectName;
                            }
                            if (jsonValue[i + 1].teacherName) {
                                subjectBeenAdjust += "(" + jsonValue[i + 1].teacherName + ")";
                            }
                        <#if isAdmin>
                            $(copyObj).parents("ul").after("<ul class=\"mui-table-view cannotdelete\" style=\"margin: 0;\">\n" +
                                    "                    <li class=\"mui-table-view-cell\">\n" +
                                    "                        <a class=\"mui-navigate-right js-teacher\">\n" +
                                    "                            <span class=\"f-left\">需调教师：</span>\n" +
                                    "                            <span class=\"f-right mr-30 teachercell\">" + jsonValue[i].teacherName + "</span>\n" +
                                    "                        </a>\n" +
                                    "                    </li>\n" +
                                    "                    <li class=\"mui-table-view-cell\">\n" +
                                    "                        <a class=\"mui-navigate-right js-course\" courseScheduleId=\"" + jsonValue[i].id + "\">\n" +
                                    "                            <span class=\"f-left\">需调节次：</span>\n" +
                                    "                            <span class=\"f-right mr-30 subjectcell\">" + subjectAdjust + "</span>\n" +
                                    "                        </a>\n" +
                                    "                    </li>\n" +
                                    "                    <li class=\"mui-table-view-cell\">\n" +
                                    "                        <a class=\"mui-navigate-right js-switch\" courseScheduleId=\"" + jsonValue[i + 1].id + "\">\n" +
                                    "                            <span class=\"f-left\">被调节次：</span>\n" +
                                    "                            <span class=\"f-right mr-30 subjectcell\">" + subjectBeenAdjust + "</span>\n" +
                                    "                        </a>\n" +
                                    "                    </li>\n" +
                                    "                    <li class=\"fn-flex mui-text-center\" style=\"background: #f9f9f9;\">\n" +
                                    "                        <a class=\"fn-flex-auto f-16 mt-10 mb-10 btn-delete\" href=\"#\" onclick=\"switchDelete(this)\">删除</a>\n" +
                                    "                        <a class=\"fn-flex-auto f-16 mt-10 mb-10 color-grey\" href=\"#\"\n" +
                                    "                           style=\"border-left: 1px solid #d7d7d7;\">复制到</a>\n" +
                                    "                    </li>\n" +
                                    "                </ul>");
                        <#else>
                            $(copyObj).parents("ul").after("<ul class=\"mui-table-view cannotdelete\" style=\"margin: 0;\">\n" +
                                    "                    <li class=\"mui-table-view-cell\">\n" +
                                    "                       <a class=\"mui-navigate-right js-course\" courseScheduleId=\"" + jsonValue[i].id + "\">\n" +
                                    "                           <span class=\"f-left\">需调节次：</span>\n" +
                                    "                           <span class=\"f-right mr-30 subjectcell\">" + subjectAdjust + "</span>\n" +
                                    "                       </a>\n" +
                                    "                    </li>\n" +
                                    "                    <li class=\"mui-table-view-cell\">\n" +
                                    "                       <a class=\"mui-navigate-right js-switch\" courseScheduleId=\"" + jsonValue[i + 1].id + "\">\n" +
                                    "                           <span class=\"f-left\">被调节次：</span>\n" +
                                    "                           <span class=\"f-right mr-30 subjectcell\">" + subjectBeenAdjust + "</span>\n" +
                                    "                       </a>\n" +
                                    "                    </li>\n" +
                                    "                    <li class=\"fn-flex mui-text-center\" style=\"background: #f9f9f9;\">\n" +
                                    "                       <a class=\"fn-flex-auto f-16 mt-10 mb-10 btn-delete\" href=\"#\" onclick=\"switchDelete(this)\">删除</a>\n" +
                                    "                       <a class=\"fn-flex-auto f-16 mt-10 mb-10 color-grey\" href=\"#\"\n" +
                                    "                       style=\"border-left: 1px solid #d7d7d7;\">复制到</a>\n" +
                                    "                    </li>\n" +
                                    "                </ul>");
                        </#if>
                            $(".layer-copy").hide();
                            $(".layer-index").show();
                        }
                    } else {
                        toastMsg(jsonResult.msg, 2000);
                    }
                },
                error:function(XMLHttpRequest, textStatus, errorThrown){

                }
            });
        });

        <#if isAdmin?default(false)>
            $('#searchTeacherName').bind('search',function(){
                searchTeacherList();
            });
        </#if>
    });

    function switchDelete(obj) {
        if ($(obj).parents(".edu-switch-list").children("ul").length <= 1) {
            $(obj).parents("ul").find(".js-teacher").removeAttr("teacherId");
            $(obj).parents("ul").find(".teachercell").text("");
            $(obj).parents("ul").find(".js-course").removeAttr("courseScheduleId");
            $(obj).parents("ul").find(".js-switch").removeAttr("courseScheduleId");
            $(obj).parents("ul").find(".subjectcell").text("");
        } else {
            $(obj).parents("ul").remove();
        }
    }

    // 复制到
    function copySwitchLayer(obj){
        copyObj = obj;
        var adjustId = $(obj).parents("ul").find(".js-course").attr("courseScheduleId");
        var beenAdjustId = $(obj).parents("ul").find(".js-switch").attr("courseScheduleId");
        <#if isAdmin>
            var teacherId = $(obj).parents("ul").find(".js-teacher").attr("teacherId");
            if (!teacherId) {
                toastMsg("未选择教师", 2000);
                return;
            }
        </#if>
        if (!adjustId) {
            toastMsg("未选择需调课程", 2000);
            return;
        }
        if (!beenAdjustId) {
            toastMsg("未选择被调课程", 2000);
            return;
        }
        copyObj = obj;
        $(".layer-copy").show();
        $(".layer-index").hide();
    }

    function courseLayer(obj) {
        adjustObj = obj;
        <#if isAdmin>
            var teacherId = $(adjustObj).parents("ul").find(".js-teacher").attr("teacherId");
            if (!teacherId) {
                toastMsg("请先选择教师", 2000);
                return;
            }
            var teacherName = $(adjustObj).parents("ul").find(".teachercell").text();
            $("#selectTeacherName").text("任课教师：" + teacherName);
        </#if>
        timeTableDetile();
        $(".layer-course").show();
        $(".layer-index").hide();
    }

    function switchLayer(obj) {
        $("#nextWeek").show();
        var classId = $(obj).parents("ul").find(".js-course").attr("classId");
        var weekTmp = $(obj).parents("ul").find(".js-course").attr("week");
        if (!classId) {
            toastMsg("请先选择需调课程", 2000);
            return;
        }
        if (!weekTmp) {
            toastMsg("需调课程周次信息缺失，请重新选择", 2000);
            return;
        }
        var className = $(obj).parents("ul").find(".js-course").attr("className");
        $("#className").text("所选班级：" + className);
        var selectWeek = parseInt(weekTmp);
        $(".ul-switch").attr("week", selectWeek);
        $("#thisWeek").children().text("第" + selectWeek + "周");
        $("#thisWeek").attr("week", selectWeek);
        $("#thisWeek").addClass("active");
        $("#nextWeek").removeClass("active");
        selectWeek += 1;
        $("#nextWeek").children().text("第" + selectWeek + "周");
        $("#nextWeek").attr("week", selectWeek);
        if (selectWeek - 1 == parseInt($("#maxWeek").val())) {
            $("#nextWeek").hide();
        }
        switchObj = obj;
        switchTableDetile();
        $(".layer-switch").show();
        $(".layer-index").hide();
    }
    
    <#if isAdmin?default(false)>
    function teacherLayer(obj) {
        teacherObj = obj;
        searchTeacherList();
        $(".layer-teacher").show();
        $(".layer-index").hide();
    }
    
    function teacherSelect(id, name) {
        $(teacherObj).children(".teachercell").text(name);
        $(teacherObj).attr("teacherId", id);
        $(".layer-teacher").hide();
        $(".layer-index").show();
    }
    </#if>

    function timeTableDetile() {
        clear();
        $.ajax({
            url:"${request.contextPath}/mobile/open/adjusttipsay/switch/timetableinfo",
            data:{"unitId":"${unitId!}",
                "week":$("#weekSelect").attr("week"),
                <#if isAdmin>"objectId":$(adjustObj).parents("ul").find(".js-teacher").attr("teacherId"),<#else>"objectId":"${teacherId}",</#if>
                "nowAcadyear":"${acadyear}",
                "nowSemester":"${semester}"},
            success:function (result) {
                var jsonResult = JSON.parse(result);
                if (jsonResult.success) {
                    var jsonValue = JSON.parse(jsonResult.businessValue);
                    for (var i = 0; i < jsonValue.length; i++) {
                        var locate = "#" + jsonValue[i].dayOfWeek + "_" + jsonValue[i].periodInterval + "_" + jsonValue[i].period;
                        var courseScheduleId = jsonValue[i].id;
                        var classId = jsonValue[i].classId;
                        var subjectType = jsonValue[i].subjectType;
                        var isDeleted = jsonValue[i].isDeleted;
                        $(locate).text(jsonValue[i].subjectName);
                        var subject = "第" + jsonValue[i].weekOfWorktime + "周" + $("#week_" + jsonValue[i].dayOfWeek).text();
                        if ("1" == jsonValue[i].periodInterval) {
                            subject += "早自习第" + jsonValue[i].period + "节 - " + jsonValue[i].className;
                        } else if ("2" == jsonValue[i].periodInterval) {
                            subject += "上午第" + jsonValue[i].period + "节 - " + jsonValue[i].className;
                        } else if ("3" == jsonValue[i].periodInterval) {
                            subject += "下午第" + jsonValue[i].period + "节 - " + jsonValue[i].className;
                        } else if ("4" == jsonValue[i].periodInterval) {
                            subject += "晚自习第" + jsonValue[i].period + "节 - " + jsonValue[i].className;
                        }
                        $(locate).attr({"courseScheduleId":courseScheduleId,
                            "classId":classId,
                            "className":jsonValue[i].className,
                            "subject":subject});
                        if ($(adjustObj).attr("courseScheduleId") == courseScheduleId) {
                            $(locate).removeClass("edit");
                            $(locate).addClass("active");
                        }
                        if (subjectType == "3") {
                            $(locate).removeClass("edit");
                            $(locate).addClass("replace");
                            $(locate).append("<span>虚</span>");
                        }
                        if (isDeleted == 1) {
                            $(locate).removeClass("edit");
                            $(locate).addClass("replace");
                            $(locate).append("<span>过</span>");
                        }
                        if (isDeleted == 3) {
                            $(locate).removeClass("edit");
                            $(locate).addClass("replace");
                            $(locate).append("<span>合</span>");
                        }
                        if (subjectType != "3" && isDeleted == 0) {
                            $(locate).on("click", function () {
                                $(adjustObj).parent().next().find(".subjectcell").empty();
                                $(adjustObj).children(".subjectcell").text($(this).attr("subject"));
                                $(adjustObj).attr({
                                    "courseScheduleId": $(this).attr("courseScheduleId"),
                                    "classId": $(this).attr("classId"),
                                    "className": $(this).attr("className"),
                                    "week": $("#weekSelect").attr("week")
                                });
                                $(".layer-course").hide();
                                $(".layer-index").show();
                            });
                        }
                    }
                } else {
                    toastMsg(jsonResult.msg, 2000);
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){

            }
        });
    }

    function switchTableDetile() {
        clear();
        $.ajax({
            url:"${request.contextPath}/mobile/open/adjusttipsay/switch//timetableinfo",
            data:{"unitId":"${unitId!}",
                "week":$(".ul-switch").attr("week"),
                "objectId":$(".js-course").attr("classId"),
                <#if isAdmin>"teacherId":$(switchObj).parents("ul").find(".js-teacher").attr("teacherId"),<#else>"teacherId":"${teacherId}",</#if>
                "nowAcadyear":"${acadyear}",
                "nowSemester":"${semester}"},
            success:function (result) {
                var jsonResult = JSON.parse(result);
                if (jsonResult.success) {
                    var jsonValue = JSON.parse(jsonResult.businessValue);
                    for (var i = 0; i < jsonValue.length; i++) {
                        var locate = "#_" + jsonValue[i].dayOfWeek + "_" + jsonValue[i].periodInterval + "_" + jsonValue[i].period;
                        var courseScheduleId = jsonValue[i].id;
                        var classId = jsonValue[i].classId;
                        var subjectType = jsonValue[i].subjectType;
                        var isDeleted = jsonValue[i].isDeleted;
                        $(locate).text(jsonValue[i].subjectName);
                        var subject = "第" + jsonValue[i].weekOfWorktime + "周" + $("#week_" + jsonValue[i].dayOfWeek).text();
                        if ("1" == jsonValue[i].periodInterval) {
                            subject += "早自习第" + jsonValue[i].period + "节 - " + jsonValue[i].subjectName;
                        } else if ("2" == jsonValue[i].periodInterval) {
                            subject += "上午第" + jsonValue[i].period + "节 - " + jsonValue[i].subjectName;
                        } else if ("3" == jsonValue[i].periodInterval) {
                            subject += "下午第" + jsonValue[i].period + "节 - " + jsonValue[i].subjectName;
                        } else if ("4" == jsonValue[i].periodInterval) {
                            subject += "晚自习第" + jsonValue[i].period + "节 - " + jsonValue[i].subjectName;
                        }
                        if (jsonValue[i].teacherName) {
                            subject  += "(" + jsonValue[i].teacherName + ")";
                        }
                        $(locate).attr({"courseScheduleId":courseScheduleId,
                            "classId":classId,
                            "subject":subject});
                        if ($(switchObj).attr("courseScheduleId") == courseScheduleId) {
                            $(locate).removeClass("edit");
                            $(locate).addClass("active");
                        }
                        if ($(switchObj).parents("ul").find(".js-course").attr("courseScheduleId") == courseScheduleId) {
                            $(locate).removeClass("edit");
                            $(locate).addClass("replace");
                            $(locate).append("<span>换</span>");
                        }
                        if (subjectType == "3") {
                            $(locate).removeClass("edit");
                            $(locate).addClass("replace");
                            $(locate).append("<span>虚</span>");
                        }
                        if (isDeleted == 1) {
                            $(locate).removeClass("edit");
                            $(locate).addClass("replace");
                            $(locate).append("<span>过</span>");
                        }
                        if (isDeleted == 2) {
                            $(locate).removeClass("edit");
                            $(locate).addClass("replace");
                            $(locate).append("<span>冲</span>");
                        }
                        if ($(switchObj).parents("ul").find(".js-course").attr("courseScheduleId") != courseScheduleId && subjectType != "3" && isDeleted == 0) {
                            $(locate).on("click", function () {
                                $(switchObj).children(".subjectcell").text($(this).attr("subject"));
                                $(switchObj).attr({
                                    "courseScheduleId": $(this).attr("courseScheduleId"),
                                    "classId": $(this).attr("classId")
                                });
                                $(".layer-switch").hide();
                                $(".layer-index").show();
                            });
                        }
                    }
                } else {
                    toastMsg(jsonResult.msg, 2000);
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){

            }
        });
    }

    function searchTeacherList(){
        var searchTeacherName=$("#searchTeacherName").val();
        $.ajax({
            url:'${request.contextPath}/mobile/open/adjusttipsay/findTeacherList',
            data:{'unitId':'${unitId!}','acadyear':'${acadyear!}','semester':'${semester!}','searchTeacherName':searchTeacherName},
            type:'post',
            dataType:'json',
            success:function(data){
                $(".teacherContent").html("");
                if(data.success){
                    var groupDtoList=data.groupDtoList;
                    addTeacherGroup(groupDtoList);
                }else{
                    $(".teacherContent").html('<div class="mui-page-noData"><i></i><p class="f-16">未搜到结果</p></div>');
                }

            }
        });
    }

    function addTeacherGroup(groupDtoList){
        var text='<ul class="mui-table-view choose-teacher">';
        for(var mm in groupDtoList){
            var groupDto=groupDtoList[mm];
            text=text+' <li class="mui-table-view-cell mui-collapse">'
                    +'<a class="mui-navigate-right" href="#">'+groupDto.teachGroupName+'</a><ul class="mui-table-view">';
            for(var kk in groupDto.showList){
                var dto=groupDto.showList[kk];
                if(dto[2]=='1'){
                    text=text+'<li class="mui-table-view-cell teacher-cell c-red" onclick="teacherSelect(\''+dto[0]+'\', \''+dto[1]+'\')">'+dto[1]+'<span class="f-right">存在冲突</span>';
                }else{
                    text=text+'<li class="mui-table-view-cell teacher-cell " onclick="teacherSelect(\''+dto[0]+'\', \''+dto[1]+'\')">'+dto[1];
                }
                text=text+'</li>';
            }
            text=text+'</ul></li>';
        }
        text=text+'</ul>';
        $(".teacherContent").html(text);
    }

    function clear() {
        $(".timetabledetile").each(function () {
            $(this).text("");
            $(this).removeAttr("courseScheduleId");
            $(this).removeAttr("classId");
            $(this).removeAttr("subject");
            $(this).removeAttr("class");
            $(this).addClass("edit");
            $(this).addClass("timetabledetile");
            $(this).off("click");
        });
    }
</script>
</body>
</html>