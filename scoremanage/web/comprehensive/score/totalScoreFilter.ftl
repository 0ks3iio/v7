<div class="explain explain-no-icon">
    <#if from == '1'>
        <p>说明：各科成绩取自各自所选择的考试的科目成绩</p>
    <#elseif from == '2'>
        <p>说明：英语成绩取自所选考试的笔试成绩（不包含口试）</p>
    <#elseif from == '3'>
        <p>说明：口试成绩取自所选考试的英语口试成绩（不包含笔试）</p>
    <#elseif from == '4'>
        <p>说明：体育成绩取自所选考试的体育成绩</p>
    <#else>
        <p>说明：各科等第取自该学生所参与的历次学考考试的最高等第</p>
    </#if>
</div>
<div class="filter">
    <#--<div class="filter-item">
        <span class="filter-name">学年：</span>
        <div class="filter-content">
            <select name="acadyear" id="acadyear" class="form-control" onchange="change2()">
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
            <select name="semester" id="semester" class="form-control" onchange="change2()">
            ${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
            </select>
        </div>
    </div>-->
    <div class="filter-item">
        <span class="filter-name">年级：</span>
        <div class="filter-content">
            <select name="gradeId" id="gradeId" class="form-control" onchange="change2()">
					<#if gradeList?exists && gradeList?size gt 0>
	                    <#list gradeList as item>
		                     <option value="${item.id!}" gradeCode="${item.gradeCode!}">${item.gradeName!}</option>
                        </#list>
                    <#else>
	                	<option value="">暂无年级</option>
                    </#if>
            </select>
        </div>
    </div>
    <#if from != '5'>
    <div class="filter-item">
        <span class="filter-name">学期：</span>
        <div class="filter-content">
            <select name="gradeCode" id="gradeCode" class="form-control" onchange="change2()">
            <#list gradeCode as item>
                <option value="${item!}">${gradeName[item_index]}</option>
            </#list>
            </select>
        </div>
    </div>
    </#if>
    <#if from == '5'>
    <div class="filter-item filter-item-right">
        <button class="btn btn-blue" onclick="studyTestSync()">同步</button>
    </div>
    <#else>
    <div class="filter-item filter-item-right">
        <button class="btn btn-blue" id="countButton" onclick="subModelCount()">统计</button>
    </div>
    </#if>
</div>

<div id="itemDetailDiv">

</div>

<script>
    var isSubmiting = false;

    $(function () {
        change2();
    });

    function change2() {
        semesterSync();
        <#if from == "5">
        var params = "?unitId=${unitId!}&type=${from}&gradeId=" + $("#gradeId option:selected").val();
        <#else>
        var params = "?unitId=${unitId!}&type=${from}&gradeId=" + $("#gradeId option:selected").val() + "&gradeCode=" + $("#gradeCode option:selected").val();
        </#if>
        var url = "${request.contextPath}/comprehensive/score/model/list" + params;
        $("#itemDetailDiv").load(url);
    }

    function semesterSync() {
        $("#gradeCode option").show();
        let gradeCode = $("#gradeId option:selected").attr("gradeCode");
        let intGradeCode = parseInt(gradeCode) * 10 + 9;
        if (intGradeCode > 900) {
            intGradeCode -= 600;
        }
        $("#gradeCode option").each(function () {
            if (intGradeCode < parseInt($(this).val())) {
                $(this).hide();
            }
        });
    }

    <#if from == '5'>
    function studyTestSync() {
        layer.confirm("开始同步？", {
            title: ['提示','font-size:20px;'],
            btn: ['确认','取消'] //按钮
        }, function(){
            if (isSubmiting) {
                return;
            }
            isSubmiting = true;
            var gradeId = $("#gradeId option:selected").val();
            var params = "?unitId=${unitId!}&type=${from}&gradeId=" + gradeId;
            $.ajax({
                url:"${request.contextPath}/comprehensive/score/model/sync" + params,
                success:function(data) {
                    var dataJson = JSON.parse(data);
                    if(dataJson.success){
                        change2();
                    }
                    layer.closeAll();
                    layer.msg(dataJson.msg, {offset: 't',time: 2000});
                    isSubmiting = false;
                },
                error:function () {
                    isSubmiting = false;
                }
            });
        }, function(){
            layer.closeAll();
        });
    }
    <#else>
    function subModelCount() {
        layer.confirm("开始统计？", {
            title: ['提示','font-size:20px;'],
            btn: ['确认','取消'] //按钮
        }, function(){
            if (isSubmiting) {
                return;
            }
            isSubmiting = true;
            var gradeId = $("#gradeId option:selected").val();
            var gradeCode = $("#gradeCode option:selected").val();
            var params = "unitId=${unitId!}&type=${from}&gradeId=" + gradeId + "&gradeCode=" + gradeCode;
            $.ajax({
                <#if from == "1">
                url:"${request.contextPath}/comprehensive/select/xkcjEdit/count?" + params,
                <#else>
                url:"${request.contextPath}/comprehensive/select/otherEdit/count?" + params,
                </#if>
                success:function(data) {
                    var dataJson = JSON.parse(data);
                    if(dataJson.success){
                        change2();
                    }
                    layer.closeAll();
                    layer.msg(dataJson.msg, {offset: 't',time: 2000});
                    isSubmiting = false;
                },
                error:function () {
                    isSubmiting = false;
                }
            });
        }, function(){
            layer.closeAll();
        });
    }
    </#if>
</script>