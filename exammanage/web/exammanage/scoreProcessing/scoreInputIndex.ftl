<div class="filter filter-f16">
    <div class="filter-item">
        <span class="filter-name">录分类型：</span>
        <div class="filter-content">
        	<label class="pos-rel">
				<input type="radio" class="wp form-radio" name="searchInputType" value="1" checked="checked" onChange="changeType()">
				<span class="lbl"> 按班级</span>
			</label>
			<label class="pos-rel">
				<input type="radio" class="wp form-radio" name="searchInputType" value="2" onChange="changeType()">
				<span class="lbl"> 按考场</span>
			</label>
			<span class="lbl"> &nbsp;&nbsp;&nbsp;&nbsp;</span>
        </div>
    </div>
    <div class="filter-item classSerachDiv">
        <span class="filter-name">班级：</span>
        <div class="filter-content">
        	<select name="searchClassId" id="searchClassId" class="form-control" onChange="findByCondition()">
				<#if (clazzList?exists && clazzList?size>0)>
					<#list clazzList as cla>
			     	<option  value = "${cla.id!}" >${cla.classNameDynamic!}</option>
			     	</#list>
			     <#else>
			     	<option  value = "" >请选择</option>
			     </#if>
			</select>
        </div>
    </div>
    <div class="filter-item placeSerachDiv" style="display:none">
        <span class="filter-name">考场：</span>
        <div class="filter-content">
        	<select name="searchExamPlanId" id="searchExamPlanId" class="form-control" onChange="findByCondition()">
				<#if (examPlanList?exists && examPlanList?size>0)>
					<#list examPlanList as plan>
			     	<option  value = "${plan.id!}" >${plan.examPlaceCode!}</option>
			     	</#list>
			     <#else>
			     	<option  value = "" >请选择</option>
			     </#if>
			</select>
        </div>
    </div>
   <#if (emSubjectList?exists && emSubjectList?size>0)>
    <div class="filter-item filter-item-right">
	    <a href="javascript:" class="btn btn-blue" onclick="exportScore()">导入成绩</a>
	</div>
	</#if>
	
</div>
<div class="tab-header clearfix">
	<ul class="nav nav-tabs nav-tabs-1">
		<#if (emSubjectList?exists && emSubjectList?size>0)>
		<#list emSubjectList as item>
		 	<li <#if item_index==0>class="active"</#if>>
		 		<a data-toggle="tab" href="#aa${item_index}" aria-expanded="false" onclick="showScoreInput('${item.subjectId!}','${item.id!}')">${item.courseName!}</a>
		 	</li>
	 	</#list>
	 	<#else>
			 <span class="tip tip-grey">该考试下还没有安排考试科目，请先维护考试科目</span>
	 	</#if>
	 </ul>
</div>
<div class="tab-content scoreInputDiv">
</div>
<script>
var chooseSubjectId="";
var chooseEmSubjectId="";
$(function(){
	<#if (emSubjectList?exists && emSubjectList?size>0)>
	<#list emSubjectList as item>
	    <#if item_index==0>
	    	showScoreInput('${item.subjectId!}','${item.id!}')
	    </#if>
 	</#list>
</#if>
});
function changeType(){
	var searchInputType=$("input[name='searchInputType']:checked").val();
	if(searchInputType=="1"){
		$(".classSerachDiv").show();
		$(".placeSerachDiv").hide();
	}else{
		$(".classSerachDiv").hide();
		$(".placeSerachDiv").show();
	}
	findByCondition();
}
function makeSearch(){
	var searchInputType=$("input[name='searchInputType']:checked").val();
	var searchClassId=$("#searchClassId").val();
	var searchExamPlanId=$("#searchExamPlanId").val();
	return "searchInputType="+searchInputType+"&searchClassId="+searchClassId+"&searchExamPlanId="+searchExamPlanId;
}


function showScoreInput(subjectId,emSubjectId){
	//条件
	chooseSubjectId=subjectId;
	chooseEmSubjectId=emSubjectId;
	var searchStr=makeSearch();
	var url="${request.contextPath}/exammanage/scoreInput/list/page?examId=${examId!}&subjectId="+subjectId+"&emSubjectId="+emSubjectId+"&"+searchStr;
	$(".scoreInputDiv").load(url);
}

function findByCondition(){

	showScoreInput(chooseSubjectId,chooseEmSubjectId);
}

function exportScore(){
	var url="${request.contextPath}/exammanage/scoreInfo/head?examId=${examId!}";
	$("#showTabDiv").load(url);
}

</script>