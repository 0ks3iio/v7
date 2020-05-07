<div class="filter filter-f16">
    <div class="filter-item">
        <span class="filter-name">科目：</span>
        <div class="filter-content">
        	<select name="subjectId" id="subjectId" class="form-control" onChange="findByCondition()">
				<#if (emSubjectList?exists && emSubjectList?size>0)>
					<#assign ii=0>
					<#list emSubjectList as item>
						<#if item.isLock?default('0')=='0'>
							<#assign ii=ii+1>
			     			<option  value = "${item.subjectId!}" >${item.courseName!}</option>
			     		</#if>
			     	</#list>
			     	<#if (ii == 0)>
			     		<option  value = "" >--请选择--</option>
			     	</#if>
			     <#else>
			     	<option  value = "" >--请选择--</option>
			     </#if>
			</select>
        </div>
    </div>
    <div class="filter-item">
        <span class="filter-name">导入类型：</span>
        <div class="filter-content">
        	<select name="exportType" id="exportType" class="form-control" onChange="findByCondition()">
				<option  value = "1" >按班级</option>
			    <option  value = "2" >按考号</option>
			</select>
        </div>
    </div>
     <div class="filter-item">
    <span class="tip tip-grey">
    <#if (emSubjectList?exists && emSubjectList?size>0)>
		<#assign iii=0>
		<#list emSubjectList as item>
			<#if item.isLock?default('0')=='1'>
				<#if iii==0>
					${item.courseName!}
				<#else>
					、${item.courseName!}
				</#if>
				<#assign iii=iii+1>
     		</#if>
     	</#list>
     	<#if (iii>0)>
     		上述科目已被锁定，不能导入成绩。
     	</#if>
     </#if>
     </span>
     </div>
     <div class="filter-item filter-item-right">
	   <a href="javascript:" class="btn btn-blue"  onclick="toScoreBack();">返回</a>
	</div>
</div>
<div id="importScoreDiv">
</div>
<script>
$(function(){
	findByCondition();
})
function toScoreBack(){
	var url =  '${request.contextPath}/exammanage/scoreInput/index/page?examId=${examId!}';
	$("#showTabDiv").load(url);
}
function findByCondition(){
	var subjectId=$("#subjectId").val();
    if(subjectId==""){
    	layer.tips("科目不能为空", $("#subjectId"), {
				tipsMore: true,
				tips:3		
			});
		return;
    }
    var exportType=$("#exportType").val();
    if(exportType==""){
    	layer.tips("导入类型不能为空", $("#exportType"), {
				tipsMore: true,
				tips:3		
			});
		return;
    }
    
    var url =  '${request.contextPath}/exammanage/scoreInfo/main?examId=${examId!}&subjectId='+subjectId+'&exportType='+exportType;
	$("#importScoreDiv").load(url);
}
</script>