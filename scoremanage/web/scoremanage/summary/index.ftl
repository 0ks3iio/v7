<div class="box box-default">
    <div class="box-body">
        <div class="filter">
            <div class="filter-item">
                <span class="filter-name">学年：</span>
                <div class="filter-content">
                    <select name="acadyear" id="acadyear" class="form-control" onchange="examListSwitch()">
                        <#if acadyearList?exists && (acadyearList?size>0)>
                            <#list acadyearList as item>
                                <option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
                            </#list>
                        <#else>
                            <option value="">未设置</option>
                        </#if>
                    </select>
                </div>
            </div>
            <div class="filter-item">
                <span class="filter-name">学期：</span>
                <div class="filter-content">
                    <select name="semester" id="semester" class="form-control" onchange="examListSwitch()">
                        ${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
                    </select>
                </div>
            </div>
            <div class="filter-item">
                <span class="filter-name">考试：</span>
                <div class="filter-content">
                    <select name="examId" id="examId" class="form-control" onchange="examSwitch()" style="width: 300px">
                        <#--<#if gradeList?exists && (gradeList?size>0)>
                            <#list gradeList as item>
                                <option value="${item.id!}">${item.gradeName!}</option>
                            </#list>
                        <#else>
                            <option value="">暂无年级</option>
                        </#if>-->
                    </select>
                </div>
            </div>
            <div class="filter-item">
                <span class="filter-name">年级：</span>
                <div class="filter-content">
                    <select name="gradeId" id="gradeId" class="form-control" onchange="examDetail()">

                    </select>
                </div>
            </div>

            <div class="filter-item filter-item-right">
                <button class="btn btn-blue" onclick="examExport()">导出</button>
            </div>
        </div>

        <div style="display: none" id="gradeCodeList">
            <#list gradeList as grade>
                <div gradeCode="${grade.fieldValue!}" class="gradeCode">
                    <option value="${grade.fieldValue!}">${grade.mcodeContent!}</option>
                </div>
            </#list>
        </div>

        <div id="examInfoDetail">

        </div>
    </div>
</div>

<script>
    var examGradeInfo = {};

    $(function () {
        examListSwitch();
    });

    function examListSwitch() {
        var acadyear = $("#acadyear").val();
        var semester = $("#semester").val();
        $.ajax({
            url: "${request.contextPath}/scoremanage/required/exam/list?acadyear=" + acadyear + "&semester=" + semester,
            success: function (dataStr) {
                data = JSON.parse(dataStr);
                examGradeInfo = {};
                var examHtml = "";
                if (data.length > 0) {
                    for (var i = 0; i < data.length; i++) {
                        examHtml += "<option value='" + data[i].id + "'>" + data[i].examName + "</option>";
                        examGradeInfo[data[i].id] = data[i].ranges.split(",");
                    }
                } else {
                    examHtml += "<option value=''>暂无考试</option>";
                }
                $("#examId").html(examHtml);
                examGradeList();
                examDetail();
            }
        });
    }

    function examDetail() {
        var examId = $("#examId").val();
        var gradeCode = $("#gradeId").val();
        var acadyear = $("#acadyear").val();
        var semester = $("#semester").val();
        if (examId && gradeCode) {
            $("#examInfoDetail").load("${request.contextPath}/scoremanage/required/detail?examId=" + examId + "&gradeCode=" + gradeCode + "&acadyear=" + acadyear + "&semester=" + semester)
        } else {
            $("#examInfoDetail").html("<div class=\"no-data\">\n" +
                "                <span class=\"no-data-img\">\n" +
                "                <img src=\"${request.contextPath}/static/images/public/nodata6.png\" alt=\"\">\n" +
                "                </span>\n" +
                "                <div class=\"no-data-body\">\n" +
                "                <p class=\"no-data-txt\">无任何考试信息</p>\n" +
                "                </div>\n" +
                "                </div>");
        }
    }

    function examExport() {
        var examId = $("#examId").val();
        var gradeCode = $("#gradeId").val();
        var acadyear = $("#acadyear").val();
        var semester = $("#semester").val();
        if (examId && gradeCode) {
            document.location.href="${request.contextPath}/scoremanage/required/export?examId=" + examId + "&gradeCode=" + gradeCode + "&acadyear=" + acadyear + "&semester=" + semester;
        } else {
            layer.msg('未选择年级以及考试！', {
                icon: 2,
                time: 1500,
                shade: 0.2
            });
        }
    }

    function examSwitch() {
        examGradeList();
        examDetail();
    }

    function examGradeList() {
        var examId = $("#examId").val();
        var arr = examGradeInfo[examId];
        $("#gradeId").empty();
        if (arr && arr.length > 0) {
            $("#gradeCodeList .gradeCode").each(function () {
                if (arr.indexOf($(this).attr("gradeCode")) > -1) {
                    $("#gradeId").append($(this).html());
                }
            });
        } else {
            $("#gradeId").append("<option value=\"\">暂无年级</option>");
        }
    }

</script>