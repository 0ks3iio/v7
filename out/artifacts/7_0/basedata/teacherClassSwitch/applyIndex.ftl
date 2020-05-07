<div class="form-horizontal" role="form">
    <div class="form-group">
        <label class="col-sm-2 control-label no-padding-right">调课课程：</label>
        <div class="col-sm-8">
            <div class="promptContainer">
                <table width="100%">
                    <tbody>
                    <tr>
                        <td width="100px"><span class="ml20 mb20 inline-block">需调课程:</span></td>
                        <td><input type="text" class="form-control mb20 js-course" onclick="courseLayer(this)" placeholder="请选择"></td>
                        <td width="100px"><span class="ml20 mb20 inline-block">被调课程:</span></td>
                        <td><input type="text" class="form-control mb20 js-switch" onclick="switchLayer(this)" placeholder="请选择"></td>
                        <td>
                            <a class="ml10 mr10 mb20 inline-block js-copy disabled" href="#"><i
                                    class="fa fa-copy color-grey"></i></a>
                        </td>
                    </tr>
                    <tr id="addRow">
                        <td colspan="5">
                            <a id="addSwitch" class="btn btn-white btn-sm ml20" href="#">+ 添加</a>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label no-padding-right">备注：</label>
        <div class="col-sm-8">
            <input id="remark" type="text" name="" class="form-control" placeholder="" maxlength="50">
        </div>
    </div>
    <form class="hidden">

    </form>
    <div class="form-group">
        <label class="col-sm-2 control-label no-padding-right"></label>
        <div class="col-sm-6">
            <button class="btn btn-blue classSwitchCommit">确认</button>
            <button class="btn btn-white classSwitchClear">取消</button>
        </div>
    </div>
</div>

<!-- 需调课程 -->
<div class="layer layer-course">
    <div class="layer-content">
        <div class="clearfix mb10">
            <div class="pull-left">
                <input id="teacherId" class="hidden" value="${teacherId!}">
                <span class="mt7 pull-left">
                    教师：${teacherName!}
                    <span class="ml10 mr10 color-ccc">|</span>
                    周次：
                </span>
                <select name="" id="weekSelect" class="form-control pull-left number" onchange="timeTableDetile()">
                    <#list weekInfoList as item>
                        <option <#if item.week == nowWeek.week>id="now" selected="selected"</#if> value="${item.week}">
                            第${item.week}周
                        </option>
                    </#list>
                </select>
            </div>
            <input class="hidden" value="${nowWeek.week}" id="nowWeek">
            <a class="btn btn-white pull-right btn-sm mt3" href="#" id="backToNow">返回当前周</a>
        </div>
        <table class="table table-bordered table-striped text-center table-schedule table-schedule-sm no-margin">
            <thead>
            <tr>
                <th class="text-center" width="30"></th>
                <th class="text-center" width="30"></th>
                <th class="text-center" id="week_0">周一</th>
                <th class="text-center" id="week_1">周二</th>
                <th class="text-center" id="week_2">周三</th>
                <th class="text-center" id="week_3">周四</th>
                <th class="text-center" id="week_4">周五</th>
            </tr>
            </thead>
            <tbody>
            <#if mm gt 0>
	            <#list 1..mm as mmLesson>
	            <tr>
	                <#if mmLesson == 1>
	                <td rowspan="${mm}">早自习</td>
	                </#if>
	                <td>${mmLesson}</td>
	                <td id="0_1_${mmLesson}" class="edited timetabledetile"></td>
	                <td id="1_1_${mmLesson}" class="edited timetabledetile"></td>
	                <td id="2_1_${mmLesson}" class="edited timetabledetile"></td>
	                <td id="3_1_${mmLesson}" class="edited timetabledetile"></td>
	                <td id="4_1_${mmLesson}" class="edited timetabledetile"></td>
	            </tr>
	            </#list>
            </#if>
            <#list 1..am as amLesson>
            <tr>
                <#if amLesson == 1>
                <td rowspan="${am}">上午</td>
                </#if>
                <td>${amLesson}</td>
                <td id="0_2_${amLesson}" class="edited timetabledetile"></td>
                <td id="1_2_${amLesson}" class="edited timetabledetile"></td>
                <td id="2_2_${amLesson}" class="edited timetabledetile"></td>
                <td id="3_2_${amLesson}" class="edited timetabledetile"></td>
                <td id="4_2_${amLesson}" class="edited timetabledetile"></td>
            </tr>
            </#list>
            <#list 1..pm as pmLesson>
            <tr>
                <#if pmLesson == 1>
                <td rowspan="${pm}">下午</td>
                </#if>
                <td>${pmLesson}</td>
                <td id="0_3_${pmLesson}" class="edited timetabledetile"></td>
                <td id="1_3_${pmLesson}" class="edited timetabledetile"></td>
                <td id="2_3_${pmLesson}" class="edited timetabledetile"></td>
                <td id="3_3_${pmLesson}" class="edited timetabledetile"></td>
                <td id="4_3_${pmLesson}" class="edited timetabledetile"></td>
            </tr>
            </#list>
            <#if night gt 0>
	            <#list 1..night as nmLesson>
	            <tr>
	                <#if nmLesson == 1>
	                <td rowspan="${night}">晚自习</td>
	                </#if>
	                <td>${nmLesson}</td>
	                <td id="0_4_${nmLesson}" class="edited timetabledetile"></td>
	                <td id="1_4_${nmLesson}" class="edited timetabledetile"></td>
	                <td id="2_4_${nmLesson}" class="edited timetabledetile"></td>
	                <td id="3_4_${nmLesson}" class="edited timetabledetile"></td>
	                <td id="4_4_${nmLesson}" class="edited timetabledetile"></td>
	            </tr>
	            </#list>
            </#if>
            </tbody>
        </table>
    </div>
</div>

<!-- 被调课程 -->
<input type="hidden" id="maxWeek" value="${maxWeek?default(1)}">
<div class="layer layer-switch">
    <div class="layer-content">
        <div class="clearfix mb10">
            <div class="pull-left">
                <input id="teacherId" class="hidden" value="${teacherId!}">
                <span class="mt7 pull-left">
                    教师：${teacherName!}
                    <span class="ml10 mr10 color-ccc">|</span>
                    周次：
                </span>
                <div class="btn-group btn-group-sm btn-switch" role="group" week="${nowWeek.week}">
                    <a id="thisWeek" type="button" class="btn btn-blue" week="${nowWeek.week}">
                        第${nowWeek.week}周
                    </a>
                    <a id="nextWeek" type="button" class="btn btn-white" week="${nowWeek.week+1}">
                        第${nowWeek.week + 1}周
                    </a>
                </div>
            </div>
        </div>
        <table class="table table-bordered table-striped text-center table-schedule table-schedule-sm no-margin">
            <thead>
            <tr>
                <th class="text-center" width="30"></th>
                <th class="text-center" width="30"></th>
                <th class="text-center">周一</th>
                <th class="text-center">周二</th>
                <th class="text-center">周三</th>
                <th class="text-center">周四</th>
                <th class="text-center">周五</th>
            </tr>
            </thead>
            <tbody>
            <#--<td class="active">1班</td>
            <td class="edited">1班</td>
            <td class="replace">语文<span>调</span></td>
            <td class="warn">语文</td>
            <td class="edited"></td>-->
            <#list 1..am as amLesson>
            <tr>
                <#if amLesson == 1>
                <td rowspan="${am}">上午</td>
                </#if>
                <td>${amLesson}</td>
                <td id="_0_2_${amLesson}" class="edited timetabledetile"></td>
                <td id="_1_2_${amLesson}" class="edited timetabledetile"></td>
                <td id="_2_2_${amLesson}" class="edited timetabledetile"></td>
                <td id="_3_2_${amLesson}" class="edited timetabledetile"></td>
                <td id="_4_2_${amLesson}" class="edited timetabledetile"></td>
            </tr>
            </#list>
            <#list 1..pm as pmLesson>
            <tr>
                <#if pmLesson == 1>
                <td rowspan="${pm}">下午</td>
                </#if>
                <td>${pmLesson}</td>
                <td id="_0_3_${pmLesson}" class="edited timetabledetile"></td>
                <td id="_1_3_${pmLesson}" class="edited timetabledetile"></td>
                <td id="_2_3_${pmLesson}" class="edited timetabledetile"></td>
                <td id="_3_3_${pmLesson}" class="edited timetabledetile"></td>
                <td id="_4_3_${pmLesson}" class="edited timetabledetile"></td>
            </tr>
            </#list>
            </tbody>
        </table>
    </div>
</div>

<!-- 复制到 -->
<div class="layer layer-copy">
    <div class="layer-content">
        <div class="explain mb10">复制时将默认保持调课的周次跨距。比如跨周调课，复制时也将跨周。</div>
        <p class="font-16">需调课程周次复制到：</p>
        <div class="publish-course">
            <#list weekInfoList as item>
                <#if item_index != 0>
                <span week="${item.week}">
                    第${item.week}周
                </span>
                </#if>
            </#list>
        </div>
    </div>
</div>

<script>
    var adjustObj;
    var switchObj;
    var copyObj;
    var isSubmit = false;

    $(function(){

        $(".classSwitchCommit").on("click", function () {
            if (isSubmit) {
                return;
            }
            isSubmit = true;
            var adjustIds = $(".js-course").attr("courseScheduleId");
            var beenAdjustIds = $(".js-switch").attr("courseScheduleId");
            var courseScheduleArr = [];
            var error = false;
            courseScheduleArr.push($(".js-course").attr("courseScheduleId"));
            courseScheduleArr.push($(".js-switch").attr("courseScheduleId"));
            if (!adjustIds || !beenAdjustIds) {
                layer.msg("未选择需调课程或被调课程", {offset: 't',time: 2000});
                isSubmit = false;
                return;
            }
            $(".js-course-add").each(function () {
                var adjustIdTmp = $(this).attr("courseScheduleId");
                var beenAdjustIdTmp = $(this).parents("tr").find(".js-switch").attr("courseScheduleId");
                if (!$(this).attr("courseScheduleId") || !$(this).parents("tr").find(".js-switch").attr("courseScheduleId")) {
                    layer.msg("未选择需调课程或被调课程", {offset: 't',time: 2000});
                    error = true;
                }
                if (courseScheduleArr.indexOf(adjustIdTmp) != -1) {
                    layer.tips('该课程重复', $(this), {
                        tipsMore: true,
                        tips:3
                    });
                    error = true;
                }
                courseScheduleArr.push(adjustIdTmp);
                if (courseScheduleArr.indexOf(beenAdjustIdTmp) != -1) {
                    layer.tips('该课程重复', $(this).parents("tr").find(".js-switch"), {
                        tipsMore: true,
                        tips:3
                    });
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
                layer.msg("备注请勿超过50个字符", {offset: 't',time: 2000});
                isSubmit = false;
                return;
            }
            $.ajax({
                url:"${request.contextPath}/basedata/classswitch/apply/commit",
                data:{"adjustIds":adjustIds,
                      "beenAdjustIds":beenAdjustIds,
                      "remark":remark},
                success:function (result) {
                    var jsonResult = JSON.parse(result);
                    if (jsonResult.success) {
                        $("#gradeTableList").load("${request.contextPath}/basedata/classswitch/switchhead/page?schoolId=${schoolId}&teacherId=${teacherId}&from=01");
                        layer.msg("提交成功，请等待审核", {offset: 't',time: 2000});
                    } else {
                        layer.msg(jsonResult.msg, {offset: 't',time: 2000});
                    }
                    isSubmit = false;
                },
                error:function(XMLHttpRequest, textStatus, errorThrown){
                    isSubmit = false;
                }
            });
        });

        $('.publish-course span').on('click', function(e){
            e.preventDefault();
            if($(this).hasClass('disabled')) return;

            if($(this).hasClass('active')){
                $(this).removeClass('active');
            }else{
                $(this).addClass('active');
            }
        });

        $('#addSwitch').on("click", function () {
            $("#addRow").before("<tr>\n" +
                    "                 <td><span class=\"ml20 mb20 inline-block\">需调课程:</span></td>\n" +
                    "                 <td><input type=\"text\" class=\"form-control mb20 js-course js-course-add\" onclick=\"courseLayer(this)\" value=\"\" placeholder=\"请选择\"></td>\n" +
                    "                 <td><span class=\"ml20 mb20 inline-block\">被调课程:</span></td>\n" +
                    "                 <td><input type=\"text\" class=\"form-control mb20 js-switch\"  onclick=\"switchLayer(this)\" placeholder=\"请选择\"></td>\n" +
                    "                 <td>\n" +
                    "                     <a class=\"ml10 mr10 mb20 inline-block disabled\" href=\"#\"><i class=\"fa fa-copy color-grey\"></i></a>\n" +
                    "                     <a class=\"mr10 mb20 inline-block font-16\" href=\"#\" onclick=\"switchDel(this)\"><i class=\"fa fa-trash-o color-blue\"></i></a>\n" +
                    "                 </td>\n" +
                    "             </tr>");
        });

        $("#thisWeek").on("click", function () {
            if ($(this).hasClass("btn-white")) {
                $(this).removeClass("btn-white");
                $(this).addClass("btn-blue");
                $(this).next().removeClass("btn-blue");
                $(this).next().addClass("btn-white");
                $(".btn-switch").attr("week",$(this).attr("week"));
                switchTableDetile();
            }
        });

        $("#nextWeek").on("click", function () {
            if ($(this).hasClass("btn-white")) {
                $(this).removeClass("btn-white");
                $(this).addClass("btn-blue");
                $(this).prev().removeClass("btn-blue");
                $(this).prev().addClass("btn-white");
                $(".btn-switch").attr("week",$(this).attr("week"));
                switchTableDetile();
            }
        });

        $("#backToNow").on("click", function () {
            $("#weekSelect").children("#now").selected();
            timeTableDetile();
        });

        $(".classSwitchClear").on("click", function () {
            $("#gradeTableList").load("${request.contextPath}/basedata/classswitch/switchhead/page?schoolId=${schoolId}&teacherId=${teacherId}&from=01");
        });

    });

    function switchDel(obj) {
        $(obj).parents("tr").remove();
    }

    // 复制到
    function copySwitchLayer(obj){
        copyObj = obj;
        layer.open({
            type: 1,
            shadow: 0.5,
            title: '复制到',
            area: '630px',
            btn: ['确定', '取消'],
            content: $('.layer-copy'),
            yes:function(index,layero){
            	var copyWeeks = "";
		        $(".layer-copy span").each(function () {
		            if ($(this).hasClass("active")) {
		                copyWeeks += $(this).attr("week") + ",";
		                $(this).removeClass("active");
		            }
		        });
		        if (copyWeeks == "") {
		            layer.msg("未选择复制周次", {offset: 't',time: 2000});
		            return;
		        }else{
            		copyShowCourse(index,copyWeeks);
		        }
			}
        });
    }

    function courseLayer(obj) {
        adjustObj = obj;
        timeTableDetile();
        layer.open({
            type: 1,
            shadow: 0.5,
            title: false,
            area: '620px',
            content: $('.layer-course')
        });
    }

    function timeTableDetile() {
        clear();
        $.ajax({
            url:"${request.contextPath}/basedata/classswitch/apply/timetableinfo",
            data:{"week":$("#weekSelect option:selected").val(),
                "objectId":$("#teacherId").val(),
                "nowAcadyear":"${nowAcadyear}",
                "nowSemester":"${nowSemester}"},
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
                        var subject = "第" + $("#weekSelect option:selected").val() + "周" + $("#week_" + jsonValue[i].dayOfWeek).text();
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
                                        "subject":subject});
                        if ($(adjustObj).attr("courseScheduleId") == courseScheduleId) {
                            $(locate).removeClass("edited");
                            $(locate).addClass("active");
                        }
                        $(locate).on("click", function () {
                            $(adjustObj).val($(this).attr("subject"));
                            $(adjustObj).attr({"courseScheduleId": $(this).attr("courseScheduleId"), "classId": $(this).attr("classId"), "week": $("#weekSelect option:selected").val()});
                            $(adjustObj).parents("tr").find(".js-switch").val("");
                            $(adjustObj).parents("tr").find(".js-switch").removeAttr("courseScheduleId");
                            $(".layui-layer-close").click();
                            clear();
                        });
                        if (subjectType == "3") {
                            $(locate).addClass("replace");
                            $(locate).append("<span>虚</span>");
                            $(locate).off("click");
                            $(locate).attr("title", "虚拟课程无法参与调课！")
                        }
                        if (isDeleted == 1) {
                            $(locate).addClass("replace");
                            $(locate).append("<span>过</span>");
                            $(locate).off("click");
                            $(locate).attr("title", "已发生课程无法参与调课！")
                        }
                        if (isDeleted == 3) {
                            $(locate).addClass("replace");
                            $(locate).append("<span>合</span>");
                            $(locate).off("click");
                            $(locate).attr("title", "合班课程无法参与调课！")
                        }
                    }
                } else {
                    layer.msg(jsonResult.msg, {offset: 't',time: 2000});
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){

            }
        });
    }

    function switchLayer(obj) {
        $("#nextWeek").show();
        var classId = $(obj).parents("tr").find(".js-course").attr("classId");
        var weekTmp = $(obj).parents("tr").find(".js-course").attr("week");
        if (!classId) {
            layer.msg("请先选择需调课程", {offset: 't',time: 2000});
            return;
        }
        if (!weekTmp) {
            layer.msg("需调课程周次信息缺失，请重新选择", {offset: 't',time: 2000});
            return;
        }
        var selectWeek = parseInt(weekTmp);
        $(".btn-switch").attr("week", selectWeek);
        $("#thisWeek").text("第" + selectWeek + "周");
        $("#thisWeek").attr("week", selectWeek);
        selectWeek += 1;
        $("#nextWeek").text("第" + selectWeek + "周");
        $("#nextWeek").attr("week", selectWeek);
        $("#thisWeek").click();
        if (selectWeek - 1 == parseInt($("#maxWeek").val())) {
            $("#nextWeek").hide();
        }
        switchObj = obj;
        switchTableDetile();
        layer.open({
            type: 1,
            shadow: 0.5,
            title: false,
            area: '620px',
            content: $('.layer-switch')
        });
    }

    function switchTableDetile() {
        clear();
        $.ajax({
            url:"${request.contextPath}/basedata/classswitch/apply/timetableinfo",
            data:{"week":$(".btn-switch").attr("week"),
                "objectId":$(".js-course").attr("classId"),
                "teacherId":$("#teacherId").val(),
                "nowAcadyear":"${nowAcadyear}",
                "nowSemester":"${nowSemester}"},
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
                        var subject = "第" + $(".btn-switch").attr("week") + "周" + $("#week_" + jsonValue[i].dayOfWeek).text();
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
                            $(locate).removeClass("edited");
                            $(locate).addClass("active");
                        }
                        $(locate).on("click", function () {
                            $(switchObj).val($(this).attr("subject"));
                            $(switchObj).attr({"courseScheduleId":$(this).attr("courseScheduleId"),"classId":$(this).attr("classId")});
                            $(".layui-layer-close").click();
                        	if($(switchObj).parent("td").siblings().find("a").hasClass("disabled")){
	                        	$(switchObj).parent("td").siblings().find("a").removeClass("disabled");
	                        	$(switchObj).parent("td").siblings().find("a").on("click",function(){
	                        		copySwitchLayer(this);
	                        	})
	                        	$(switchObj).parent("td").siblings().find("i").removeClass("color-grey").addClass("color-blue");
	                        }
                            clear();
                        });
                        if ($(adjustObj).attr("courseScheduleId") == courseScheduleId) {
                            $(locate).removeClass("edited");
                            $(locate).addClass("replace");
                            $(locate).append("<span>换</span>");
                            $(locate).off("click");
                        }
                        if (subjectType == "3") {
                            $(locate).removeClass("edited");
                            $(locate).addClass("replace");
                            $(locate).append("<span>虚</span>");
                            $(locate).off("click");
                            $(locate).attr("title", "虚拟课程无法参与调课！")
                        }
                        if (isDeleted == 1) {
                            $(locate).addClass("replace");
                            $(locate).append("<span>过</span>");
                            $(locate).off("click");
                            $(locate).attr("title", "已发生课程无法参与调课！")
                        }
                        if (isDeleted == 2) {
                            $(locate).addClass("replace");
                            $(locate).append("<span>冲</span>");
                            $(locate).off("click");
                            $(locate).attr("title", "冲突课程无法参与调课！")
                        }
                    }
                } else {
                    layer.msg(jsonResult.msg, {offset: 't',time: 2000});
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){

            }
        });
    }

    function clear() {
        $(".timetabledetile").each(function () {
            $(this).text("");
            $(this).removeAttr("courseScheduleId");
            $(this).removeAttr("classId");
            $(this).removeAttr("subject");
            $(this).removeAttr("class");
            $(this).removeAttr("title");
            $(this).addClass("edited");
            $(this).addClass("timetabledetile");
            $(this).off("click");
        });
    }

    function copyShowCourse(index,copyWeeks) {
        var adjustId = $(copyObj).parents("tr").find(".js-course").attr("courseScheduleId");
        var beenAdjustId = $(copyObj).parents("tr").find(".js-switch").attr("courseScheduleId");
        if (!adjustId) {
            layer.msg("未选择需调课程", {offset: 't',time: 2000});
            $(".layer-copy span").each(function () {
                $(this).removeClass("active");
            });
            return;
        }
        if (!beenAdjustId) {
            layer.msg("未选择被调课程", {offset: 't',time: 2000});
            $(".layer-copy span").each(function () {
                $(this).removeClass("active");
            });
            return;
        }
        $.ajax({
            url:"${request.contextPath}/basedata/classswitch/apply/copyswitch",
            data:{"adjustId":adjustId,
                "beenAdjustId":beenAdjustId,
                "copyWeeks":copyWeeks,
                "teacherId":$("#teacherId").val(),
                "nowAcadyear":"${nowAcadyear}",
                "nowSemester":"${nowSemester}"},
            success:function (result) {
                var jsonResult = JSON.parse(result);
                if (jsonResult.success) {
                	layer.close(index);
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
                        $(copyObj).parent().parent().after("<tr>\n" +
                                "                            <td><span class=\"ml20 mb20 inline-block\">需调课程:</span></td>\n" +
                                "                            <td><input type=\"text\" disabled=\"disabled\" class=\"form-control mb20 js-course js-course-add\" value=\"" + subjectAdjust + "\" courseScheduleId=\"" + jsonValue[i].id + "\"></td>\n" +
                                "                            <td><span class=\"ml20 mb20 inline-block\">被调课程:</span></td>\n" +
                                "                            <td><input type=\"text\" disabled=\"disabled\" class=\"form-control mb20 js-switch\" value=\"" + subjectBeenAdjust + "\" courseScheduleId=\"" + jsonValue[i + 1].id + "\"></td>\n" +
                                "                            <td>\n" +
                                "                                <a class=\"ml10 mr10 mb20 inline-block font-16\" href=\"#\" onclick=\"switchDel(this)\"><i class=\"fa fa-trash-o color-blue\"></i></a>\n" +
                                "                            </td>\n" +
                                "                        </tr>")
                    }
                } else {
                    layer.msg(jsonResult.msg, {offset: 't',time: 2000});
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){

            }
        });
    }
</script>