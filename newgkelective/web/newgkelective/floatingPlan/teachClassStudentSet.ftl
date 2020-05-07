<#if planType == "A">
    <#assign batchNameMap={"1":"选考一", "2":"选考二", "3":"选考三"}/>
<#else>
    <#assign batchNameMap={"1":"学考一", "2":"学考二", "3":"学考三", "4":"学考四"}/>
</#if>
<#assign ysy="YSY"/>
<#assign total="TOTAL"/>
<link rel="stylesheet" href="${request.contextPath}/static/components/datatables/media/css/jquery.dataTables.min.css">
<div class="filter">
    <div class="filter-item">
        <div class="filter-content">
            <button class="btn btn-blue student-switch hidden" type="button" onclick="saveTeachClassStu()">保存</button>
            <button class="btn btn-default" type="button" onclick="backToPerArrange()">返回</button>
        </div>
    </div>
</div>
<div class="table-switch-container no-margin openTeachClass">
    <div class="table-switch-box">
        <div class="table-switch-filter">
            <div class="filter">
                <div class="filter-item">
                    <span class="filter-name"><span>科目：</span><span>${course.subjectName!}</span></span>
                </div>
                <div class="filter-item">
                    <span class="filter-name"><span>时间点：</span><span>${batchNameMap[batch]}</span></span>
                </div>
                <div class="filter-item">
                    <div class="filter-content">
                        <input type="text" class="form-control float-left" placeholder="请输入" style="width: 70px;"
                               id="searchSomeRows">
                        <button type="button" class="btn btn-blue float-left ml5" onclick="searchRow();">选行</button>
                    </div>
                </div>
                <div class="filter-item">
                    <div class="filter-content">
                        <input type="text" class="form-control float-left" placeholder="请输入" style="width: 70px;"
                               id="openNum">
                        <button class="btn btn-blue float-left ml5" type="button" onclick="openSomeClass();">开班</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="table-switch-data default">
            <span>总数：<em id="leftTotalCount">0</em></span>
            <span>男：<em id="leftManCount">${maleCount!}</em></span>
            <span>女：<em id="leftWomanCount">${femaleCount!}</em></span>
            <span>${course.subjectName!}：<em id="leftOneAvg">${courseAvg!}</em></span>
            <span>语数英：<em id="leftThreeAvg">${courseAvg!}</em></span>
        </div>
        <table class="table table-bordered table-striped js-sort-table-left" id="leftTableId"
               style="margin-bottom: 0px;">
            <thead>
            <tr>
                <th>
                    <label class="pos-rel">
                        <input type="checkbox" class="wp leftSelectAllStudent" name="">
                        <span class="lbl"></span>
                    </label>
                </th>
                <th>姓名</th>
                <th>性别</th>
                <th>新行政班 <a class="float-right color-grey js-popover-filter js-popover-filter-class" href="#"><i
                                class="fa fa-filter"></i></a></th>
                <th>选课 <a class="float-right color-grey js-popover-filter js-popover-filter-subject" href="#"><i
                                class="fa fa-filter"></i></a></th>
                <th>${course.subjectName!}</th>
                <th>语数英</th>
                <th>总分</th>
            </tr>
            </thead>
            <tbody>
            <#if unSolveStudentList?exists && unSolveStudentList?size gt 0>
                <#list unSolveStudentList as student>
                    <tr>
                        <td>
                            <label class="pos-rel">
                                <input type="checkbox" class="wp" name="studentIdName" value="${student.studentId!}">
                                <span class="lbl"></span>
                            </label>
                        </td>
                        <td>${student.studentName!}</td>
                        <td class="sex">${student.sex!}</td>
                        <td class="class_name">${student.className!}</td>
                        <td class="chosen_subject">${student.chooseSubjects!}</td>
                        <td class="score_one">${student.subjectScore[course.id]?default("0")}</td>
                        <td class="score_three">${student.subjectScore[ysy]!}</td>
                        <td>${student.subjectScore[total]!}</td>
                    </tr>
                </#list>
            </#if>
            </tbody>
        </table>
    </div>
    <div class="table-switch-control">
        <button class="btn btn-sm student-switch" type="button" onclick="leftToRight()"><i
                    class="wpfont icon-arrow-right"></i></button>
        <button class="btn btn-sm student-switch" type="button" onclick="rightToLeft()"><i
                    class="wpfont icon-arrow-left"></i></button>
    </div>
    <div class="table-switch-box">
        <div class="table-switch-filter clearfix">
            <ul class="nav nav-tabs nav-tabs-1 float-left" style="border-bottom:none;margin: -5px 0 0 -10px;">
                <li <#if !isCombine>class="active"</#if> id="independentId"><a href="#cc" data-toggle="tab">独立开班</a>
                </li>
                <li <#if isCombine>class="active"</#if> id="combinationId"><a href="#dd" data-toggle="tab">合班</a></li>
            </ul>
            <div class="filter float-right">
                <div class="filter-item student-switch">
                    <span class="filter-name">班级：</span>
                    <div class="filter-content">
                        <select class="form-control" name="" id="teachClassId">
                            <#if independentTeachClassList?exists && independentTeachClassList?size gt 0>
                                <#list independentTeachClassList as item>
                                    <option value="${item.id}"
                                            <#if teachClassId?default("") == item.id>selected</#if>>${item.className}
                                        (<#if item.studentList?exists>${item.studentList?size}<#else>0</#if>)
                                    </option>
                                </#list>
                            <#else>
                                <option value="emptyTeachClass">无班级</option>
                            </#if>
                        </select>
                    </div>
                </div>
                <div class="filter-item filter-item-right">
                    <div class="filter-content student-switch">
                        <button class="btn btn-blue js-popover-create" type="button" onclick="addNewClass(null)">创建班级
                        </button>
                        <button class="btn btn-default" type="button" onclick="deleteClass(null, this)">删除</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="tab-content no-padding">
            <div id="cc" class="tab-pane active">
                <div class="table-switch-data default">
                    <span>总数：<em id="rightTotalCount">0</em></span>
                    <span>男：<em id="rightManCount">0</em></span>
                    <span>女：<em id="rightWomanCount">0</em></span>
                    <span>${course.subjectName!}：<em id="rightOneAvg">0</em></span>
                    <span>语数英：<em id="rightThreeAvg">0</em></span>
                </div>
                <table class="table table-bordered table-striped js-sort-table-right" id="rightTableId"
                       style="margin-bottom: 0px;">
                    <thead>
                    <tr>
                        <th>
                            <label class="pos-rel">
                                <input type="checkbox" class="wp rightSelectAllStudent" name="">
                                <span class="lbl"></span>
                            </label>
                        </th>
                        <th>姓名</th>
                        <th>性别</th>
                        <th>新行政班</th>
                        <th>选课</th>
                        <th>${course.subjectName!}</th>
                        <th>语数英</th>
                        <th>总分</th>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
            <div id="dd" class="tab-pane <#if isCombine>active</#if>">
                <div class="table-switch-data no-padding">
                    <div class="color-yellow padding-10" style="background: #E5F3FF;"><i
                                class="fa fa-exclamation-circle"></i> 已在左侧选择&nbsp<em id="selectStudentCount">0</em>&nbsp名学生
                    </div>
                    <!--<div class="color-yellow padding-10" style="background: #FFF1EB;"><i class="fa fa-exclamation-circle"></i> 合班前，请先在左侧选择学生</div>-->
                </div>
                <#--<table class="table table-bordered table-striped js-sort-table-combination no-margin">
                    <thead>
                    <tr>
                        <th width="100">新行政班</th>
                        <th width="170">在行政班上课的科目</th>
                        <th width="70">总人数</th>
                        <th width="70">男</th>
                        <th width="70">女</th>
                        <th>合班时间点(科目-人数)</th>
                        <th width="100">操作</th>
                        <th width="19"></th>
                    </tr>
                    </thead>
                </table>-->
                <div style="height: 372px;overflow-y: scroll;border: 1px solid #ddd;border-top: none;">
                    <table class="table table-bordered table-striped no-margin">
                        <thead>
                        <tr>
                            <th width="100">新行政班</th>
                            <th width="150">在行政班上课的科目</th>
                            <th width="70">总人数</th>
                            <th width="70">男</th>
                            <th width="70">女</th>
                            <th>合班时间点(科目-人数)</th>
                            <th width="100">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#if combinationClassList?exists && combinationClassList?size gt 0>
                            <#list combinationClassList as combinationClass>
                                <#assign row=1/>
                                <#if combinationToTeachClassMap[combinationClass.id]?exists && combinationToTeachClassMap[combinationClass.id]?size gt 1>
                                    <#assign row = combinationToTeachClassMap[combinationClass.id]?size/>
                                </#if>
                                <tr>
                                    <td rowspan="${row}" width="100">${combinationClass.className!}</td>
                                    <td rowspan="${row}" width="170">${combinationClass.subNames!}</td>
                                    <td rowspan="${row}" width="70">${combinationClass.studentCount!}</td>
                                    <td rowspan="${row}" width="70">${combinationClass.boyCount!}</td>
                                    <td rowspan="${row}" width="70">${combinationClass.girlCount!}</td>
                                    <#if combinationToTeachClassMap[combinationClass.id]?exists && combinationToTeachClassMap[combinationClass.id]?size gt 0>
                                        <td>${batchNameMap[combinationToTeachClassMap[combinationClass.id][0].batch]!}
                                            (${combinationToTeachClassMap[combinationClass.id][0].relateName!}-<em
                                                    class="stu-count">${combinationToTeachClassMap[combinationClass.id][0].studentCount!}</em>)
                                            <#if combinationClass.relateId?exists && combinationClass.relateId == combinationToTeachClassMap[combinationClass.id][0].id>
                                                <a class="table-btn color-blue float-right" href="javascript:void(0)"
                                                   teachClassId="${combinationToTeachClassMap[combinationClass.id][0].id!}"
                                                   onclick='deleteClass("${combinationToTeachClassMap[combinationClass.id][0].id!}", this)'>解散</a>
                                            </#if>
                                        </td>
                                    <#else>
                                        <td class="empty"></td>
                                    </#if>
                                    <td rowspan="${row}" width="100">
                                        <#if combinationClass.classType?default("0") != "1">
                                            <a class="table-btn color-blue" href="javascript:void(0)"
                                               onclick="addNewRelateClass(this)"
                                                    <#if combinationClass.relateId?exists>
                                                        relateClassId="${combinationClass.relateId}"
                                                    </#if> combinationClassId="${combinationClass.id!}">
                                                合班
                                            </a>
                                        </#if>
                                    </td>
                                </tr>
                                <#if combinationToTeachClassMap[combinationClass.id]?exists && combinationToTeachClassMap[combinationClass.id]?size gt 1>
                                    <#list 1..row as index>
                                        <#if index == row>
                                            <#break>
                                        </#if>
                                        <tr>
                                            <td>${batchNameMap[combinationToTeachClassMap[combinationClass.id][index].batch]!}
                                                (${combinationToTeachClassMap[combinationClass.id][index].relateName!}
                                                -${combinationToTeachClassMap[combinationClass.id][index].studentCount!}
                                                )
                                                <#if combinationClass.relaterId?exists && combinationClass.relaterId == combinationToTeachClassMap[combinationClass.id][index].id>
                                                    <a class="table-btn color-blue float-right"
                                                       href="javascript:void(0)"
                                                       teachClassId="${combinationToTeachClassMap[combinationClass.id][index].id!}">解散</a>
                                                </#if>
                                            </td>
                                        </tr>
                                    </#list>
                                </#if>
                            </#list>
                        </#if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="filterClassId" style="display:none;">
    <div id="filterClassIdInner">

    </div>
</div>
<div id="filterSubjectId" style="display:none;">
    <div id="filterSubjectIdInner">

    </div>
</div>

<script>

    // stuDtoMap:页面学生信息
    var stuIdStr = "";
    var deleteTeachClassId = "";
    var deleteTeachClassObj;
    var combinationObj;
    var studentIdToTrMap = new Map();
    var classIdToStudentIdsArr = new Map();
    var classNameCountMap = new Map();
    var subjectChosenCountMap = new Map();
    var leftTableObj;
    var rightTableObj;
    var combinationTableObj;

    <#if solveStudentList?exists && solveStudentList?size gt 0>
    <#list solveStudentList as student>
    studentIdToTrMap['${student.studentId}'] =
        '<tr>\n' +
        '   <td>\n' +
        '       <label class="pos-rel">\n' +
        '           <input type="checkbox" class="wp" name="studentIdName" value="${student.studentId!}">\n' +
        '           <span class="lbl"></span>\n' +
        '       </label>\n' +
        '   </td>\n' +
        '   <td>${student.studentName!}</td>\n' +
        '   <td class="sex">${student.sex!}</td>\n' +
        '   <td>${student.className!}</td>\n' +
        '   <td>${student.chooseSubjects!}</td>\n' +
        '   <td class="score_one">${student.subjectScore[course.id]?default("0")}</td>\n' +
        '   <td class="score_three">${student.subjectScore[ysy]!}</td>\n' +
        '   <td class="score_total">${student.subjectScore[total]!}</td>\n' +
        '</tr>';
    </#list>
    </#if>

    <#if independentTeachClassList?exists && independentTeachClassList?size gt 0>
    <#list independentTeachClassList as item>
    classIdToStudentIdsArr['${item.id}'] = [${item.stuIdStr!}]
    </#list>
    </#if>

    <#if combinationToTeachClassMap?exists && combinationToTeachClassMap?size gt 0>
    <#list combinationToTeachClassMap?keys as key>
    <#if combinationToTeachClassMap[key]?exists && combinationToTeachClassMap[key]?size gt 0>
    <#list combinationToTeachClassMap[key] as item>
    classIdToStudentIdsArr['${item.id}'] = [${item.stuIdStr!}]
    </#list>
    </#if>
    </#list>
    </#if>

    $(function () {
        // 通过js添加table水平垂直滚动条
        leftTableObj = $('.js-sort-table-left').DataTable({
            // 设置垂直方向高度
            scrollY: 372,
            // 禁用搜索
            searching: false,
            // 禁止表格分页
            paging: false,
            // 禁止宽度自动
            autoWidth: false,
            info: false,
            order: [],
            // 禁用指定列排序
            columnDefs: [
                {orderable: false, targets: 0},
                {orderable: false, targets: 3},
                {orderable: false, targets: 4}
            ],
            language: {
                emptyTable: "暂无数据"
            }
        });

        $("#filterClassIdInner").html(filterClassInfo());
        $('.js-popover-filter-class').popover({
            content: $("#filterClassIdInner"),
            html: true,
            placement: 'bottom',
            trigger: 'manual',
            container: '.openTeachClass'
        });
        $('.js-popover-filter-class').click(function () {
            $('.js-popover-filter-subject').popover('hide');
            $(this).popover('show');
        });

        $("#filterSubjectIdInner").html(filterSubjectInfo());
        $('.js-popover-filter-subject').popover({
            content: $("#filterSubjectIdInner"),
            html: true,
            placement: 'bottom',
            trigger: 'manual',
            container: '.openTeachClass'
        });
        $('.js-popover-filter-subject').click(function () {
            $('.js-popover-filter-class').popover('hide');
            $(this).popover('show');
        });

        $("#independentId").on("click", function () {
            $(".student-switch").show();
        });

        $("#combinationId").on("click", function () {
            $(".student-switch").hide();
            clearNew();
            // 计算左边
            refHeadMsg("left");
            // 计算右边
            refHeadMsg("right");
        });

        $("#leftTableId").on("click", "input:checkbox", function () {
            selectStudentCount();
        });

        $(".openTeachClass").on("change", ".leftSelectAllStudent", function () {
            if (!$(this).prop("checked")) {
                $("#leftTableId").find('input:checkbox[name=studentIdName]').prop("checked", false);
            } else {
                $("#leftTableId").find('input:checkbox[name=studentIdName]').each(function () {
                    if ($(this).parent().is(":visible")) {
                        $(this).prop("checked", true);
                    }
                });
            }
            selectStudentCount();
        });

        $(".openTeachClass").on("change", ".rightSelectAllStudent", function () {
            if (!$(this).prop("checked")) {
                $("#rightTableId").find('input:checkbox[name=studentIdName]').prop("checked", false);
            } else {
                $("#rightTableId").find('input:checkbox[name=studentIdName]').each(function () {
                    if ($(this).parent().is(":visible")) {
                        $(this).prop("checked", true);
                    }
                });
            }
        });

        $("#teachClassId").on("change", function () {
            independentTeachClassSwitch();
            clearNew();
            // 计算左边
            refHeadMsg("left");
            // 计算右边
            refHeadMsg("right");
        });

        independentTeachClassSwitch();

        // 计算左边
        refHeadMsg("left");
        // 计算右边
        refHeadMsg("right");

        $(".leftSelectAllStudent").prop("checked", true);
        $("#leftTableId").find('input:checkbox[name=studentIdName]').prop("checked", true);
        selectStudentCount();

        <#if isCombine>
        $(".student-switch").hide();
        $("#cc").removeClass("active");
        $("#dd").addClass("active");
        </#if>
    });

    function selectStudentCount() {
        var count = $("#leftTableId").find('input:checkbox[name=studentIdName]:checked').size();
        $("#selectStudentCount").html(count);
    }

    // 合班，若该时间点该科目已存在合班班级，直接将学生纳入该班级
    var relateObj;

    function addNewRelateClass(obj) {
        if (!$("#leftTableId").find('input:checkbox[name=studentIdName]:checked').size() > 0) {
            layer.msg("请先选择合班学生！", {
                offset: 't',
                time: 2000
            });
            return;
        }
        relateObj = obj;
        if (typeof ($(obj).attr("relateClassId")) == "undefined") {
            addNewClass(obj);
        } else {
            var studentIds = "";
            if ($("#leftTableId").find('input:checkbox[name=studentIdName]:checked').size() >= 0) {
                $("#leftTableId").find('input:checkbox[name=studentIdName]:checked').each(function () {
                    var studentId = $(this).val();
                    if (!studentIdToTrMap.get(studentId)) {
                        studentIdToTrMap.set(studentId, '<tr>' + $(this).parents("tr").html() + '</tr>');
                    }
                    studentIds = studentIds + "," + studentId;
                });
                studentIds = studentIds.substring(1);
            }
            $.ajax({
                url: '${request.contextPath}/newgkelective/${divideId!}/floatingPlan/teachClassSaveStu?append=1',
                data: {"studentIds": studentIds, "teachClassId": $(obj).attr("relateClassId")},
                dataType: 'json',
                type: 'post',
                success: function (data) {
                    if (data.success) {
                        layer.msg("保存成功", {
                            offset: 't',
                            time: 2000
                        });
                        /*var tmp = $("#leftTableId").find('input:checkbox[name=studentIdName]:checked').size() + parseInt($(relateObj).parents("tr").find(".stu-count").text());
                        $(relateObj).parents("tr").find(".stu-count").text(tmp);
                        $("#leftTableId").find('input:checkbox[name=studentIdName]:checked').parents("tr").remove();*/
                        refeshTeachClassStudentSet();
                    } else {
                        layerTipMsg(data.success, "失败", "原因：" + data.msg);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
        }
    }

    // 返回
    var isBack = false;

    function backToPerArrange() {
        if (isBack) {
            return;
        }
        isBack = true;
        var url = '${request.contextPath}/newgkelective/${divideId!}/floatingPlan/teachClassSet?planType=${planType!}';
        $("#aa").load(url);
    }

    // 单独开班切换班级
    function independentTeachClassSwitch() {

        if (rightTableObj) {
            rightTableObj.destroy();
        }

        $("#leftTableId").find("tbody").find(".unSave").show().removeClass("unSave");
        var tmp = $("#teachClassId").val();
        if (tmp != "emptyTeachClass") {
            var htmlTmp = "";
            var studentIdArr = classIdToStudentIdsArr.get(tmp);
            if (!studentIdArr) {
                studentIdArr = classIdToStudentIdsArr[tmp];
            }
            if (studentIdArr) {
                for (var i = 0; i < studentIdArr.length; i++) {
                    var trTmp = studentIdToTrMap.get(studentIdArr[i]);
                    if (!trTmp) {
                        trTmp = studentIdToTrMap[studentIdArr[i]];
                    }
                    htmlTmp += trTmp;
                }
            }
            $("#rightTableId").find("tbody").html(htmlTmp);
        }

        rightTableObj = $('.js-sort-table-right').DataTable({
            // 设置垂直方向高度
            scrollY: 372,
            // 禁用搜索
            searching: false,
            // 禁止表格分页
            paging: false,
            // 禁止宽度自动
            autoWidth: false,
            destroy: true,
            info: false,
            order: [],
            // 禁用指定列排序
            columnDefs: [
                {orderable: false, targets: 0},
                {orderable: false, targets: 3},
                {orderable: false, targets: 4}
            ],
            language: {
                emptyTable: "暂无数据"
            }
        });
    }

    // 新增班级
    function addNewClass(obj) {
        if (obj) {
            combinationObj = obj;
        }
        var url = '${request.contextPath}/newgkelective/${divideId!}/floatingPlan/schedulingEdit/page?subjectIds=${course.id!}&subjectType=${planType!}&batch=${batch!}';
        layerDivUrl(url, {title: "新建班级", width: 350, height: 250});
    }

    function leftToRight() {

        if ($("#leftTableId").find('input:checkbox[name=studentIdName]:checked').size() == 0) {
            layer.msg("未选择学生！", {
                offset: 't',
                time: 2000
            });
            return;
        }

        if ($("#teachClassId").val() == "emptyTeachClass") {
            addNewClass(null);
            return;
        }

        if (rightTableObj) {
            rightTableObj.destroy();
        }

        var allTr = $("#rightTableId").find("tbody").find("input:checkbox[name=studentIdName]");
        if (allTr.size() == 0) {
            $("#rightTableId").find("tbody").html("");
        }

        var htmlText = "";

        $("#leftTableId").find('input:checkbox[name=studentIdName]:checked').each(function () {
            var studentId = $(this).val();
            if (!studentIdToTrMap.has(studentId)) {
                studentIdToTrMap.set(studentId, '<tr>' + $(this).parents("tr").html() + '</tr>');
            }
            $(this).prop('checked', false);
            if ($("#rightTableId").find("input:checkbox[value=" + studentId + "]").size() == 0) {
                // console.log("notFind");
                htmlText = htmlText + '<tr class="new">' + $(this).parents("tr").html() + '</tr>';
                $(this).parents("tr").addClass("unSave").hide();
            } else {
                // console.log("find");
                $("#rightTableId").find("input:checkbox[value=" + studentId + "]").parents("tr").removeClass("unSave").show();
                $(this).parents("tr").remove();
            }
        });
        $("#rightTableId").find("tbody").append(htmlText);
        $("#leftTableId").find(".checkboxAllClass").prop('checked', false);

        rightTableObj = $('.js-sort-table-right').DataTable({
            // 设置垂直方向高度
            scrollY: 372,
            // 禁用搜索
            searching: false,
            // 禁止表格分页
            paging: false,
            // 禁止宽度自动
            autoWidth: false,
            destroy: true,
            info: false,
            // 禁用指定列排序
            columnDefs: [
                {orderable: false, targets: 0}
            ],
            language: {
                emptyTable: "暂无数据"
            }
        });

        updateSort();

        if ($("#teachClassId").val() && $("#teachClassId").val() != "emptyTeachClass") {
            saveTeachClassStu();
        }

    }

    function rightToLeft() {
        var allTr = $("#leftTableId").find("tbody").find("input:checkbox[name=studentIdName]");

        if ($("#rightTableId").find('input:checkbox[name=studentIdName]:checked').size() == 0) {
            layer.msg("未选择学生！", {
                offset: 't',
                time: 2000
            });
            return;
        }

        if (allTr.size() == 0) {
            $("#leftTableId").find("tbody").html("");
        }

        if (rightTableObj) {
            rightTableObj.destroy();
        }

        var studentId = "";
        var htmlText = "";

        $("#rightTableId").find('input:checkbox[name=studentIdName]:checked').each(function () {
            studentId = $(this).val();
            if (!studentIdToTrMap.get(studentId)) {
                studentIdToTrMap.set(studentId, '<tr>' + $(this).parents("tr").html() + '</tr>');
            }
            $(this).prop('checked', false);
            if ($("#leftTableId").find("input:checkbox[value=" + studentId + "]").size() == 0) {
                // console.log("notFind");
                htmlText = htmlText + '<tr class="new">' + $(this).parents("tr").html() + '</tr>';
                $(this).parents("tr").addClass("unSave").hide();
            } else {
                // console.log("find");
                $("#leftTableId").find("input:checkbox[value=" + studentId + "]").parents("tr").removeClass("unSave").show();
                $(this).parents("tr").remove();
            }
        });
        $("#leftTableId").find("tbody").append(htmlText);
        $("#rightTableId").find(".checkboxAllClass").prop('checked', false);

        rightTableObj = $('.js-sort-table-right').DataTable({
            // 设置垂直方向高度
            scrollY: 372,
            // 禁用搜索
            searching: false,
            // 禁止表格分页
            paging: false,
            // 禁止宽度自动
            autoWidth: false,
            destroy: true,
            info: false,
            order: [],
            // 禁用指定列排序
            columnDefs: [
                {orderable: false, targets: 0},
                {orderable: false, targets: 3},
                {orderable: false, targets: 4}
            ],
            language: {
                emptyTable: "暂无数据"
            }
        });

        updateSort();

        saveTeachClassStu();

    }

    function refFilterMsg() {
        // $('.js-popover-filter-class').data('bs.popover').options.content = filterClassInfo();
        // $('.js-popover-filter-subject').data('bs.popover').options.content = filterSubjectInfo();

        $('.js-popover-filter-class').popover('destroy');
        $("#filterClassId").html('<div id="filterClassIdInner">' + filterClassInfo() + '</div>');
        $('.js-popover-filter-class').popover({
            content: $("#filterClassIdInner"),
            html: true,
            placement: 'bottom',
            trigger: 'manual',
            container: '.openTeachClass'
        });

        $('.js-popover-filter-subject').popover('destroy');
        $("#filterSubjectId").html('<div id="filterSubjectIdInner">' + filterSubjectInfo() + '</div>');
        $('.js-popover-filter-subject').popover({
            content: $("#filterSubjectIdInner"),
            html: true,
            placement: 'bottom',
            trigger: 'manual',
            container: '.openTeachClass'
        });

        // 计算左边
        refHeadMsg("left");
        // 计算右边
        refHeadMsg("right");
    }

    // 刷新表头人数以及平均分
    function refHeadMsg(id) {
        var tableObj = $("#" + id + "TableId");

        var boyCount = 0;
        var girlCount = 0;
        var totalCount = 0;
        tableObj.find("tbody").find("input:checkbox[name=studentIdName]").each(function () {
            if ($(this).parent().is(":visible")) {
                totalCount++;
            }
        });

        tableObj.find(".sex:visible").each(function () {
            if ($(this).text() == "男") {
                boyCount++;
            } else if ($(this).text() == "女") {
                girlCount++;
            }
        });

        $("#" + id + "TotalCount").text(totalCount);
        $("#" + id + "ManCount").text(boyCount);
        $("#" + id + "WomanCount").text(girlCount);

        if (totalCount == 0) {
            totalCount = 1;
        }

        var scoreOneTotal = 0;
        var scoreThreeTotal = 0;

        tableObj.find(".score_one:visible").each(function () {
            scoreOneTotal += parseInt($(this).text());
        });
        tableObj.find(".score_three:visible").each(function () {
            scoreThreeTotal += parseInt($(this).text());
        });

        $("#" + id + "OneAvg").text((scoreOneTotal / totalCount).toFixed(2));
        $("#" + id + "ThreeAvg").text((scoreThreeTotal / totalCount).toFixed(2));
    }

    // commit
    function saveNew() {
        $("#leftTableId").find(".new").removeClass("new");
        $("#leftTableId").find(".unSave").remove();
        $("#rightTableId").find(".new").removeClass("new");
        $("#rightTableId").find(".unSave").remove();
    }

    // revert
    function clearNew() {
        $("#leftTableId").find(".new").removeClass("new").remove();
        $("#leftTableId").find(".unSave").show().removeClass("unSave");
        $("#rightTableId").find(".new").removeClass("new").remove();
        $("#rightTableId").find(".unSave").show().removeClass("unSave");
    }

    function filterClassInfo() {
        classNameCountMap.clear();
        var total = 0;
        if ($("#leftTableId tbody").find(".class_name").size() > 0) {
            $("#leftTableId tbody").find("tr").each(function () {
                if ($(this).children(".class_name").size() == 0) {
                    return;
                }
                var tmp = $(this).children(".class_name").text();
                if (!classNameCountMap.has(tmp)) {
                    classNameCountMap.set(tmp, 0);
                }
                classNameCountMap.set(tmp, classNameCountMap.get(tmp) + 1);
                total++;
            });
        }
        var htmlTmp = '<div class="all">\n' +
            '            <label class="pos-rel">\n' +
            '                <input type="checkbox" class="wp" name="classFilter" checked value=""\n' +
            '                       onChange="filterTouch(this, 1)">\n' +
            '                <span class="lbl"> 全部(<em class="count">' + total + '</em>)</span>\n' +
            '            </label>\n' +
            '        </div>';
        classNameCountMap.forEach(function (value, key) {
            htmlTmp += '<div>\n' +
                '       <label class="pos-rel">\n' +
                '           <input type="checkbox" class="wp" name="classFilter" checked value="' + key + '"\n' +
                '               onChange="filterTouch(this, 1)">\n' +
                '           <span class="lbl"> ' + key + '(<em class="count">' + value + '</em>)</span>\n' +
                '       </label>\n' +
                '    </div>'
        })
        return htmlTmp;
    }

    function filterSubjectInfo() {
        subjectChosenCountMap.clear();
        var total = 0;
        $("#leftTableId tbody").find("tr").each(function () {
            if ($(this).children(".chosen_subject").size() == 0) {
                return;
            }
            var tmp = $(this).children(".chosen_subject").text();
            if (!subjectChosenCountMap.has(tmp)) {
                subjectChosenCountMap.set(tmp, 0);
            }
            subjectChosenCountMap.set(tmp, subjectChosenCountMap.get(tmp) + 1);
            total++;
        });
        var htmlTmp = '<div class="all">\n' +
            '            <label class="pos-rel">\n' +
            '                <input type="checkbox" class="wp" name="subjectFilter" checked value=""\n' +
            '                       onChange="filterTouch(this, 2)">\n' +
            '                <span class="lbl"> 全部(<em class="count">' + total + '</em>)</span>\n' +
            '            </label>\n' +
            '        </div>';
        subjectChosenCountMap.forEach(function (value, key) {
            htmlTmp += '<div>\n' +
                '       <label class="pos-rel">\n' +
                '           <input type="checkbox" class="wp" name="subjectFilter" checked value="' + key + '"\n' +
                '               onChange="filterTouch(this, 2)">\n' +
                '           <span class="lbl"> ' + key + '(<em class="count">' + value + '</em>)</span>\n' +
                '       </label>\n' +
                '    </div>'
        })
        return htmlTmp;
    }

    function filterTouch(obj, index) {
        var thisObj = $(obj).parent().parent();
        var otherObj = thisObj.siblings();

        // 行政班筛选
        if (index == 1) {
            // 全部按钮
            if (thisObj.hasClass("all")) {
                if ($(obj).prop("checked")) {
                    otherObj.find("input").prop("checked", true);
                    $("#leftTableId tbody").find("tr").show();
                } else {
                    otherObj.find("input").prop("checked", false);
                    $("#leftTableId tbody").find("tr").hide();
                }
            } else {
                $("#leftTableId tbody").find("tr").hide();
                if ($(obj).prop("checked")) {
                    var tmp = $(obj).val();
                    $("#leftTableId tbody").find("tr").each(function () {
                        if ($(this).find(".class_name").text().indexOf(tmp) > -1) {
                            $(this).show();
                        }
                    });
                }
                otherObj.find("input").each(function () {
                    if ($(this).parent().parent().hasClass("all")) {
                        return;
                    }
                    if ($(this).prop("checked")) {
                        var tmp = $(this).val();
                        $("#leftTableId tbody").find("tr").each(function () {
                            if ($(this).find(".class_name").text().indexOf(tmp) > -1) {
                                $(this).show();
                            }
                        });
                    }
                });
            }
        } else {
            // 全部按钮
            if (thisObj.hasClass("all")) {
                if ($(obj).prop("checked")) {
                    otherObj.find("input").prop("checked", true);
                    $("#leftTableId tbody").find("tr").show();
                } else {
                    otherObj.find("input").prop("checked", false);
                    $("#leftTableId tbody").find("tr").hide();
                }
            } else {
                $("#leftTableId tbody").find("tr").hide();
                if ($(obj).prop("checked")) {
                    var tmp = $(obj).val();
                    $("#leftTableId tbody").find("tr").each(function () {
                        if ($(this).find(".chosen_subject").text().indexOf(tmp) > -1) {
                            $(this).show();
                        }
                    });
                }
                otherObj.find("input").each(function () {
                    if ($(this).parent().parent().hasClass("all")) {
                        return;
                    }
                    if ($(this).prop("checked")) {
                        var tmp = $(this).val();
                        $("#leftTableId tbody").find("tr").each(function () {
                            if ($(this).find(".chosen_subject").text().indexOf(tmp) > -1) {
                                $(this).show();
                            }
                        });
                    }
                });
            }
        }

        $("#leftTableId tbody").find("tr").each(function () {
            if ($(this).is(":hidden")) {
                $(this).find("input").prop("checked", false);
            } else {
                $(this).find("input").prop("checked", true);
            }
        });

        if ($("#leftTableId tbody tr:visible").size() > 0) {
            $(".leftSelectAllStudent").prop("checked", true);
        } else {
            $(".leftSelectAllStudent").prop("checked", false);
        }
    }

    function updateSort() {
        /*$("#leftTableId").trigger("update");
        $("#rightTableId").trigger("update");*/
    }

    function saveTeachClassStu() {
        var studentIds = "";
        if ($("#rightTableId").find('input:checkbox[name=studentIdName]').size() >= 0) {
            $("#rightTableId").find('input:checkbox[name=studentIdName]').each(function () {
                if ($(this).parent().is(":visible")) {
                    var studentId = $(this).val();
                    studentIds = studentIds + "," + studentId;
                }
            });
            studentIds = studentIds.substring(1);
        }
        $.ajax({
            url: '${request.contextPath}/newgkelective/${divideId!}/floatingPlan/teachClassSaveStu',
            data: {"studentIds": studentIds, "teachClassId": $("#teachClassId").val()},
            dataType: 'json',
            type: 'post',
            success: function (data) {
                if (data.success) {
                    layer.msg("保存成功", {
                        offset: 't',
                        time: 2000
                    });

                    saveNew();
                    refFilterMsg();

                    var totalCount = 0;
                    classIdToStudentIdsArr[$("#teachClassId").val()] = [];
                    if ($("#rightTableId").find('input:checkbox[name=studentIdName]').size() >= 0) {
                        $("#rightTableId").find('input:checkbox[name=studentIdName]').each(function (i) {
                            totalCount++;
                            var studentId = $(this).val();
                            classIdToStudentIdsArr[$("#teachClassId").val()][i] = studentId;
                        });
                    }

                    $(".leftSelectAllStudent").prop("checked", false);
                    $(".rightSelectAllStudent").prop("checked", false);

                    refeshTeachClassStudentSet();
                } else {
                    layerTipMsg(data.success, "失败", "原因：" + data.msg);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }

    function deleteClass(teachClassId, obj) {
        if (teachClassId == null || teachClassId == "") {
            teachClassId = $("#teachClassId").val();
        }
        if (teachClassId == "emptyTeachClass") {
            layer.msg("没有班级需要删除！", {
                offset: 't',
                time: 2000
            });
            return;
        }
        deleteTeachClassId = teachClassId;
        deleteTeachClassObj = obj;
        var options = {
            btn: ['确定', '取消'],
            title: '确认信息',
            icon: 1,
            closeBtn: 0
        };
        showConfirm("是否确定解散该班级", options, function () {
            $.ajax({
                url: "${request.contextPath}/newgkelective/${divideId!}/floatingPlan/deleteTeachClass",
                data: {"classIds": teachClassId},
                dataType: 'json',
                type: 'post',
                success: function (data) {
                    if (data.success) {
                        /*if (leftTableObj) {
                            leftTableObj.destroy();
                        }*/
                        if ($("#independentId").hasClass("active")) {
                            layer.msg("教学班已删除！", {
                                offset: 't',
                                time: 2000
                            });
                            $("#teachClassId").find('option[value="' + deleteTeachClassId + '"]').remove();
                            if ($("#teachClassId").find('option').size() == 0) {
                                $("#teachClassId").html('<option value="emptyTeachClass">无班级</option>');
                            }
                            // refeshTeachClassStudentSet();
                        } else {
                            layer.msg("合班教学班已解散！", {
                                offset: 't',
                                time: 2000
                            });
                            /*var thisTdObj = $(deleteTeachClassObj).parent();
                            var nextTdObj = thisTdObj.next();
                            var row = parseInt(nextTdObj.attr("rowspan"));
                            if (row > 1) {
                                row--;
                                thisTdObj.siblings().attr("rowspan", row);
                                thisTdObj.remove();
                                nextTdObj.before(nextTdObj.parent().next().html());
                                nextTdObj.parent().next().remove();
                            } else {
                                thisTdObj.addClass("empty").empty();
                            }
                            nextTdObj.find("a").removeAttr("relateClassId");
                            var htmlTmp = "";
                            var studentIdArrTmp = classIdToStudentIdsArr.get(deleteTeachClassId);
                            if (!studentIdArrTmp) {
                                studentIdArrTmp = classIdToStudentIdsArr[deleteTeachClassId];
                            }
                            for (var i = 0; i < studentIdArrTmp.length; i++) {
                                var contentTmp = studentIdToTrMap.get(studentIdArrTmp[i]);
                                if (!contentTmp) {
                                    contentTmp = studentIdToTrMap[studentIdArrTmp[i]];
                                }
                                htmlTmp += contentTmp;
                            }
                            $("#leftTableId").find("tbody").append(htmlTmp);
                            if (!classIdToStudentIdsArr.delete(deleteTeachClassId)) {
                                classIdToStudentIdsArr.set(deleteTeachClassId, []);
                            }
                            leftTableObj = $('.js-sort-table-left').DataTable({
                                // 设置垂直方向高度
                                scrollY: 372,
                                // 禁用搜索
                                searching: false,
                                // 禁止表格分页
                                paging: false,
                                // 禁止宽度自动
                                autoWidth: false,
                                info: false,
                                // 禁用指定列排序
                                columnDefs: [
                                    {orderable: false, targets: 0},
                                    {orderable: false, targets: 3},
                                    {orderable: false, targets: 4}
                                ],
                                language: {
                                    emptyTable: "暂无数据"
                                }
                            });
                            refFilterMsg();*/
                        }

                        refeshTeachClassStudentSet();
                    } else {
                        layer.closeAll();
                        layerTipMsg(data.success, "失败", "原因：" + data.msg);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
        }, function () {
        });
    }

    $('#perArrangeDiv').on('change', '.checkboxAllClass', function () {
        if ($(this).is(':checked')) {
            $(this).parents(".tableDivClass").find('input:checkbox[name=studentIdName]').each(function (i) {
                $(this).prop('checked', true);
            });
        } else {
            $(this).parents(".tableDivClass").find('input:checkbox[name=studentIdName]').each(function (i) {
                $(this).prop('checked', false);
            });
        }
    });

    $('#perArrangeDiv').on('change', '.checkBoxItemClass', function () {
        if ($(this).parents("table").find('input:checkbox[name=studentIdName]:checked').size() > 0) {
            $(this).parents(".tableSwitchBox").find(".checkboxAllClass").prop('checked', true);
        } else {
            $(this).parents(".tableSwitchBox").find(".checkboxAllClass").prop('checked', false);
        }
    });

    function searchRow() {
        var num = $("#searchSomeRows").val().trim();
        var pattern = /[^0-9]/;
        if (pattern.test(num) || num.slice(0, 1) == "0") {
            layer.msg("只能输入非零的整数！", {
                offset: 't',
                time: 2000
            });
            $("#searchSomeRows").val('');
            $("#searchSomeRows").focus();
            return false;
        }
        var rows = parseInt(num);
        if (num <= 0) {
            return false;
        }
        var index = 0;
        if ($("#leftTableId").find('input:checkbox[name=studentIdName]').size() >= 0) {
            $("#leftTableId").find('input:checkbox[name=studentIdName]').each(function () {
                if ($(this).parents("tr").is(":visible")) {
                    if (index++ < rows) {
                        $(this).prop('checked', true);
                    } else {
                        $(this).prop('checked', false);
                    }
                }
            });
        }
        $("#leftTableId").find(".checkboxAllClass").prop('checked', true);
        selectStudentCount();
    }

    var isOpenSome = false;

    function openSomeClass() {
        if (isOpenSome) {
            return;
        }
        isOpenSome = true;
        var num = $("#openNum").val().trim();
        if (num == "") {
            layer.msg("不能为空！", {
                offset: 't',
                time: 2000
            });
            $("#openNum").val("");
            $("#openNum").focus();
            isOpenSome = false;
            return false;
        }
        var pattern = /[^0-9]/;
        if (pattern.test(num) || num.slice(0, 1) == "0") {
            layer.msg("只能输入非零的整数！", {
                offset: 't',
                time: 2000
            });
            $("#openNum").val("");
            $("#openNum").focus();
            isOpenSome = false;
            return false;
        }
        var rows = parseInt(num);
        if (rows <= 0) {
            layer.msg("只能输入非零的整数！", {
                offset: 't',
                time: 2000
            });
            $("#openNum").val("");
            $("#openNum").focus();
            isOpenSome = false;
            return false;
        }
        stuIdStr = "";
        if ($("#leftTableId").find('input:checkbox[name=studentIdName]:checked').size() > 0) {
            $("#leftTableId").find('input:checkbox[name=studentIdName]:checked').each(function () {
                stuIdStr = stuIdStr + "," + $(this).val();
            });
            stuIdStr = stuIdStr.substring(1);
        } else {
            layer.msg("未选择学生！", {
                offset: 't',
                time: 2000
            });
            isOpenSome = false;
            return;
        }

        var options = {
            btn: ['确定', '取消'],
            title: '确认信息',
            icon: 1,
            closeBtn: 0
        };
        showConfirm("是否确定根据系统规则进行开班", options, function () {
            var ii = layer.load();
            $.ajax({
                url: '${request.contextPath}/newgkelective/${divideId!}/floatingPlan/autoOpenClass',
                data: {
                    "batch": "${batch}",
                    "subjectType": "${planType}",
                    "subjectId": "${subjectId!}",
                    "openNum": rows,
                    "stuIds": stuIdStr
                },
                dataType: 'json',
                type: 'post',
                success: function (data) {
                    layer.closeAll();
                    if (data.success) {
                        refeshTeachClassStudentSet();
                    } else {
                        layerTipMsg(data.success, "失败", "原因：" + data.msg);
                    }

                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
        }, function () {
            isOpenSome = false;
        });
    }

    function refeshTeachClassStudentSet() {
        var url = "${request.contextPath}/newgkelective/${divide.id!}/floatingPlan/teachClassStudentSet?subjectId=${subjectId!}&batch=${batch!}&planType=${planType!}";
        if ($("#independentId").hasClass("active")) {
            url += "&isCombine=false&teachClassId=" + $("#teachClassId").val();
        } else {
            url += "&isCombine=true";
        }
        $("#aa").load(url);
    }
</script>