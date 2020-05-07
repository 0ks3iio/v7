<#assign ysy="YSY"/>
<#assign total="TOTAL"/>
<link rel="stylesheet" href="${request.contextPath}/static/components/datatables/media/css/jquery.dataTables.min.css">
<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<div class="box box-default">
    <div class="box-body" id="settleContent">
        <div class="filter">
            <div class="filter-item">
                <div class="filter-content">
                    <button class="btn btn-blue student-switch hidden" type="button" onclick="saveTeachClassStu()">保存
                    </button>
                    <button class="btn btn-blue" type="button" onclick="backFixed()">返回</button>
                </div>
            </div>
        </div>
        <div class="table-switch-container no-margin openTeachClass">
            <div class="table-switch-box">
                <div class="table-switch-filter">
                    <div class="filter">
                        <div class="filter-item">
                            <span class="filter-name"><span>科目：</span><span>${shortNames!}</span></span>
                        </div>
                        <div class="filter-item">
                            <div class="filter-content">
                                <input type="text" class="form-control float-left" placeholder="请输入"
                                       style="width: 70px;"
                                       id="searchSomeRows">
                                <button type="button" class="btn btn-blue float-left ml5" onclick="searchRow();">选行
                                </button>
                            </div>
                        </div>
                        <div class="filter-item">
                            <div class="filter-content">
                                <input type="text" class="form-control float-left" placeholder="请输入"
                                       style="width: 70px;"
                                       id="openNum">
                                <button class="btn btn-blue float-left ml5" type="button" onclick="openSomeClass();">
                                    开班
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="table-switch-data default">
                    <span>总数：<em id="leftTotalCount">0</em></span>
                    <span>男：<em id="leftManCount">0</em></span>
                    <span>女：<em id="leftWomanCount">0</em></span>
                    <#list courseList as course>
                        <span>${course.subjectName!}：<em id="left_${course_index}_Avg">0.0</em></span>
                    </#list>
                    <span>语数英：<em id="leftThreeAvg">0.0</em></span>
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
                        <#list courseList as course>
                            <th>${course.subjectName!}</th>
                        </#list>
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
                                        <input type="checkbox" class="wp" name="studentIdName"
                                               value="${student.studentId!}">
                                        <span class="lbl"></span>
                                    </label>
                                </td>
                                <td>${student.studentName!}</td>
                                <td class="sex">${student.sex!}</td>
                                <td class="class_name">${student.className!}</td>
                                <td class="chosen_subject">${student.chooseSubjects!}</td>
                                <#list courseList as course>
                                    <td class="score_${course_index}">${student.subjectScore[course.id]?default("0.0")}</td>
                                </#list>
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
                        <li class="active" id="independentId"><a href="#cc" data-toggle="tab">独立开班</a></li>
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
                                <button class="btn btn-blue js-popover-create" type="button"
                                        onclick="addNewClass(null)">创建班级
                                </button>
                                <button class="btn btn-default" type="button" onclick="deleteClass(null, this)">删除
                                </button>
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
                            <#list courseList as course>
                                <span>${course.subjectName!}：<em id="right_${course_index}_Avg">0.0</em></span>
                            </#list>
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
                                <#list courseList as course>
                                    <th>${course.subjectName!}</th>
                                </#list>
                                <th>语数英</th>
                                <th>总分</th>
                            </tr>
                            </thead>
                            <tbody>

                            </tbody>
                        </table>
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
    </div>
</div>

<script>

    // stuDtoMap:页面学生信息
    var initFlag = false;
    var stuIdStr = "";
    var deleteTeachClassId = "";
    var deleteTeachClassObj;
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
        '   <td class="score_0">${student.subjectScore[courseList[0].id]?default("0")}</td>\n' +
        '   <td class="score_1">${student.subjectScore[courseList[1].id]?default("0")}</td>\n' +
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
    });

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
        var url = '${request.contextPath}/newgkelective/${divideId!}/divideClass/singleRecomb/schedulingEdit/page?subjectType=A&subjectIds=${subjectIds!}';
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
            initFlag = true
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
        var scoreTwoTotal = 0;
        var scoreThreeTotal = 0;

        tableObj.find(".score_0:visible").each(function () {
            scoreOneTotal += parseInt($(this).text());
        });
        tableObj.find(".score_1:visible").each(function () {
            scoreTwoTotal += parseInt($(this).text());
        });
        tableObj.find(".score_three:visible").each(function () {
            scoreThreeTotal += parseInt($(this).text());
        });

        $("#" + id + "_0_Avg").text((scoreOneTotal / totalCount).toFixed(2));
        $("#" + id + "_1_Avg").text((scoreTwoTotal / totalCount).toFixed(2));
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

    // 此处调用智能分班接口，通用
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

                    refeshTeachClassStudentSet();
                } else {
                    layerTipMsg(data.success, "失败", "原因：" + data.msg);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }

    // 此处调用智能分班接口，通用
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
                        layer.msg("教学班已删除！", {
                            offset: 't',
                            time: 2000
                        });
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
                url: '${request.contextPath}/newgkelective/${divideId!}/divideClass/singleRecomb/autoOpenClass',
                data: {
                    "subjectIds": "${subjectIds!}",
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
                    isOpenSome = false;
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    isOpenSome = false;
                }
            });
        }, function () {
            isOpenSome = false;
        });
    }

    function backFixed() {
        $("#showList").load("${request.contextPath}/newgkelective/${divide.id!}/divideClass/singleRecomb/openTeachClass?subjectType=A")
    }

    function refeshTeachClassStudentSet() {
        $("#showList").load("${request.contextPath}/newgkelective/${divide.id!}/divideClass/singleRecomb/teachClassStudentSet?subjectType=A&subjectIds=${subjectIds!}")
    }
</script>