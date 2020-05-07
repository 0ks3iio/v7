<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<div class="box box-default">
	<#if loginInfo.unitClass != 2>
	<div class="box-header">
		<h3 class="box-caption">所有学生</h3>
	</div>
	</#if>
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">年级：</span>
				<div class="filter-content">
					<select name="gradeId" id="gradeId" class="form-control" onChange="toCls();">
					     <#list grades as grade>
					     <option value="${grade.id!}" <#if gradeId?default('')==grade.id!>selected</#if>>${grade.gradeName!}</option>
					     </#list>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">班级：</span>
				<div class="filter-content">
					<select name="clsId" id="clsId" class="form-control" onChange="toStuList();">
					     <#if clsList?exists && clsList?size gt 0>
					     <#list clsList as cls>
					     <option value="${cls.id!}" <#if clsId?default('') == cls.id!>selected</#if>>${cls.classNameDynamic!}</option>
					     </#list>
					     </#if>
					</select>
				</div>
			</div>
			<div class="filter-item filter-item-right">
				<a  href="javascript:;" class="btn btn-blue" onclick="toInPage();"><#if deploy?default('')=='hangwai'>学籍增加<#else>转入</#if></a>
			</div>
		</div>
		<div id="stuListDiv"></div>
	</div>
</div>
<script>
$(function(){
	
});
toStuList();

function toCls(){
	var gradeId=$('#gradeId').val();
	var url = '${request.contextPath}/newstusys/abnormal/sch/index/page?schoolId=${schoolId!}&gradeId='+gradeId;
	<#if loginInfo.unitClass==1>
	$('#stuDataDiv').load(url);
	<#else>
	$('.model-div').load(url);
	</#if>
}

function toStuList(){
	var clsId=$('#clsId').val();
	if(clsId == ''){
		showMsgError('请选择个班级');
		return;
	}
	var gradeId=$('#gradeId').val();
	var url = '${request.contextPath}/newstusys/abnormal/stuList/page?schoolId=${schoolId!}&gradeId='+gradeId+'&clsId='+clsId;
	$('#stuListDiv').load(url);
}

function toInPage(){
	var gradeId=$('#gradeId').val();
	var clsId=$('#clsId').val();
	var url = '${request.contextPath}/newstusys/abnormal/in/page?schoolId=${schoolId!}&gradeId='+gradeId+'&clsId='+clsId;
	<#if loginInfo.unitClass==1>
	$('#stuDataDiv').load(url);
	<#else>
	$('.model-div').load(url);
	</#if>
}
</script>