<div class="tab-pane active">
    <div class="filter">
        <div class="filter-item">
            <span class="filter-name">学年：</span>
            <div class="filter-content">
                <select class="form-control" id="acadyear" name="acadyear" onChange="changeTime()">
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
                <select class="form-control" id="semester" name="semester" onChange="changeTime()">
                ${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
                </select>
            </div>
        </div>
        <#--<div class="filter-item">-->
            <#--<span class="filter-name">年级：</span>-->
            <#--<div class="filter-content">-->
                <#--<select class="form-control" id="gradeCode" name="gradeCode" onChange="changeTime()">-->
                    <#--<#if gradeList?? && (gradeList?size>0)>-->
                        <#--<#list gradeList as item>-->
                            <#--<option value="${item.gradeCode!}" >${item.gradeName!}</option>-->
                        <#--</#list>-->
                    <#--</#if>-->
                <#--</select>-->
            <#--</div>-->
        <#--</div>-->
    </div>
    <div class="padding-10" id="subjectTabList">
    </div>
</div>

<script type="text/javascript">
    $(function(){
        changeTime();
        $('[data-toggle="tooltip"]').tooltip({
            container: 'body',
            trigger: 'hover'
        });

    })
    function changeTime(){
        var acadyear=$("#acadyear").val();
        var semester=$("#semester").val();
        // var gradeCode=$("#gradeCode").val();
        var parmUrl="?year="+acadyear+"&semester="+semester;
        var url="${request.contextPath}/exammanage/stu/stuSignList/page"+parmUrl;
        $("#subjectTabList").load(url);

    }
    <#--function changeExamId(){-->
        <#--var examId=$("#examId").val();-->
        <#--$.ajax({-->
            <#--url:"${request.contextPath}/exammanage/common/subjectList",-->
            <#--data:{unitId:'${unitId!}',examId:examId},-->
            <#--dataType: "json",-->
            <#--success: function(data){-->
                <#--makeSubjectTab(data);-->
            <#--}-->
        <#--});-->

    <#--}-->

</script>