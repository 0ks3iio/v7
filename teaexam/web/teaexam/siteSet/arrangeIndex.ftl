<#include "arrangeCommon.ftl">
<div class="filter site-filter">
	<div class="filter-item">
		<a class="btn btn-blue" href="javascript:;" onclick="autoArrange();">自动分配</a>
		<#--<a class="btn btn-blue" id="examBtn" href="javascript:;" onclick="autoArrange();">全部重新分配</a>-->
		<span class="color-blue" id="ingArrange" <#if status?default('0') != '1'>style="display:none;"</#if>><i class="fa fa-spinner fa-spin"></i> 正在自动分配中...</span>
		<span class="color-red" id="reArrange" <#if status?default('0') != '-1'>style="display:none;"</#if>><i class="fa fa-exclamation-circle"></i> 自动分配失败，请重试</span>
	</div>
	<div class="filter-item filter-item-right">
		<span class="filter-name">考试时间：</span>
		<div class="filter-content" id="subTime">${(subList[0].startTime?string('yyyy-MM-dd HH:mm'))?if_exists} ~ ${(subList[0].endTime?string('HH:mm'))?if_exists}</div>
	</div>
	<div class="filter-item filter-item-right">
		<span class="filter-name">科目：</span>
		<div class="filter-content">
			<select name="subInfoId" id="subInfoId" class="form-control" onchange="arrangeList();">
				<#if subList?exists && subList?size gt 0>
				<#list subList as sub>
				<option value="${sub.id!}" <#if subInfoId?default('')==sub.id>selected</#if> id="${sub.id!}_option" time="${(sub.startTime?string('yyyy-MM-dd HH:mm'))?if_exists} ~ ${(sub.endTime?string('HH:mm'))?if_exists}">${sub.subjectName!}(${mcodeSetting.getMcode("DM-XD",sub.section?string)})</option>
				</#list>
				</#if>
			</select>
		</div>
	</div>
</div>
<div id="arrangeList"></div>
<script>
arrangeList();

function arrangeList(){
	var sid = $('#subInfoId').val();
	$('#subTime').text($('#'+sid+'_option').attr('time'));
	var url = '${request.contextPath}/teaexam/siteSet/setIndex/${examId!}/siteList?subInfoId='+sid;
	$('#arrangeList').load(url);
}

function teaList(rmNo){
	$('.site-filter').hide();
	var sid = $('#subInfoId').val();
	var url = '${request.contextPath}/teaexam/siteSet/setIndex/${examId!}/teaList?subInfoId='+sid+'&roomNo='+rmNo;
	$('#arrangeList').load(url);
}
</script>