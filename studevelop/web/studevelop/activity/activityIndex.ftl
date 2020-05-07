<title>活动首页</title>
<#include "activityConstant.ftl" />
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title"><#if actType==SCHOOLACT>学校活动<#elseif actType==CLASSACT>班级活动<#else>主题活动</#if></h4>
	</div>
	<div class="box-body">
		<div >
		<form name="actListForm" id="actListForm">
		<input type="hidden" name="rangeType" id="rangeType" value="${rangeType!}">
		<input type="hidden" name="actType" id="actType" value="${actType!}">
			<div class="filter clearfix">
				<div class="filter-item">
					<span class="filter-name">学年：</span>
					<div class="filter-content">
						<select name="acadyear" id="acadyear" class="form-control" onchange="changeAcadyear();">
							<#list acadyearList as aca>
							<option value="${aca!}" <#if acadyear?default('') ==aca>selected</#if>>${aca!}</option>
							</#list>
						</select>
					</div>
				</div>
				<div class="filter-item">
					<span class="filter-name">学期：</span>
					<div class="filter-content">
						<select name="semester" id="semester" class="form-control" onchange="changeCls();">
							<option value="1" <#if semester?default(0) ==1>selected</#if>>第一学期</option>
							<option value="2" <#if semester?default(0) ==2>selected</#if>>第二学期</option>
						</select>
					</div>
				</div>
				<#if actType?default('') != SCHOOLACT!>
					<div class="filter-item">
						<span class="filter-name">班级：</span>
						<div class="filter-content">
							<select name="rangeId" id="rangeId" class="form-control" onchange="changeCls();">
								<#list clsList as cls>
								<option value="${cls.id!}" <#if rangeId?default('') =='${cls.id!}'>selected</#if>>${cls.classNameDynamic!}</option>
								</#list>
							</select>
						</div>
					</div>
				<#else>
					<input type="hidden" name="rangeId" id="rangeId" value="${rangeId!}">
				</#if>
			</div>
		</form>	
		</div>
		
		<div id="actDiv">
			
		</div>
	</div>
</div>
<script>
$(function(){
<#if actType?default('') != SCHOOLACT! && (!clsList?exists || clsList?size lt 1)>
	layerTipMsg(false,"提示","该学年下没有班级信息!");
<#else>
	changeCls();
</#if>	
});


function changeAcadyear(){
	<#if actType?default('') != SCHOOLACT!>
	var acy = $('#acadyear').val();
	var sem = $('#semester').val();
	var url = "${request.contextPath}/studevelop/activity/${actType!}/index/page?acadyear="+acy
			+"&semester="+sem;
	$('.model-div').load(url);		
	<#else>
	changeCls();
	</#if>
}

function changeCls(){
	var acy = $('#acadyear').val();
	var sem = $('#semester').val();
	var rid = $('#rangeId').val();
	var rtype = $('#rangeType').val();
	if(rid == '' || rid==null){
		layerTipMsg(false,"提示","没有选择班级!");
		return;
	}
	var url = "${request.contextPath}/studevelop/activity/${actType!}/list/page?acadyear="+acy
			+"&semester="+sem+"&rangeId="+rid+"&rangeType="+rtype;
	$('#actDiv').load(url);
}
</script>