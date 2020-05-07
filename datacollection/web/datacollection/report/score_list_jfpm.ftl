<@dcm.reportData  reportCode=_report.reportCode>
<#if editMode?default(false)>
<@dcm.dcEditTemplate value="编辑本模板" reportId=_report.id templateType="list" />
<@dcm.dcRdButton value="返回填报列表" templatePath="index.ftl" url="/dc/report/listReports" />
</#if>
<p></p>
<input type="hidden" id="creation_time@orderBy" isCondition=true value="desc" />
<table class="table  table-bordered table-hover"><tr>
<th class="thForm" width="100" style="padding:5px">部门</th>
<td width="200" style="padding:5px">
<@p.selectOneDept clickId="dept_name" id="dept_id" name="dept_name" handler="">
<input readonly type="text" class="form-control" id="dept_name" value="<@dcm.dcObjVal type="dept" id=dept_id! columnId="deptName" ignoreSpan=true />" >
<input isCondition=true nullable=false type="hidden" id="dept_id" value="${dept_id!}"/>
<input isCondition=true nullable=false type="hidden" id="teacher_id" value="" operation="NONE"/>
</@p.selectOneDept>
</td>

<th class="thForm" >起始日期</th><td>
<div style="display:block;" id="myDiv">
<div>
	    <div class="input-group">
		<input  isCondition=true  class="form-control date-picker" dataType="date"  alias="startTime" operation=">=" id="creation_time" value="${startTime!}" vtype="data" style="width: 150px" type="text" nullable="false"  placeholder="起始日期">
		<span class="input-group-addon">
			<i class="fa fa-calendar bigger-110"></i>
		</span></div></div>
</div>
</td>

<th class="thForm" >截止日期</th><td>
<div style="display:block;" id="myDiv">
<div>
	    <div class="input-group">
		<input  isCondition=true  class="form-control date-picker" dataType="date"  alias="endTime" operation="<" id="creation_time" value="${endTime!}" vtype="data" style="width: 150px" type="text" nullable="false"  placeholder="截止日期">
		<span class="input-group-addon">
			<i class="fa fa-calendar bigger-110"></i>
		</span></div></div>
</div>
</td>
<td><@dcm.dcRdButton isConditionButton=true value="查找" url="/dc/report/listReportDataSum/" + _report.id reportId=_report.id checkReturn="doCookie()"/>
</td>
</tr></table>
<p ></p>
<table class="table  table-bordered table-hover">
<#if _cacheTime?exists>
<tr>
<td colspan="6">统计时间：${_cacheTime} (根据查询条件，缓存1小时)</td>
</tr>
</#if>
<tr>
<th width="100">排名</th>
<th width="100">员工</th>
<th width="100">部门</th>
<th width="100">积分小计</th>
<th width="100">加分次数</th>
<th>操作</th>
</tr>
<#list _datas as data>
<tr id="_tr_${data.id}">
<td>${data_index + 1}</td>
<td><@dcm.dcObjVal type="teacher" id=data.teacher_id columnId="teacherName" /></td>
<td><@dcm.dcObjVal type="dept" id=data.dept_id columnId="deptName" /></td>
<td>${data.sumScore!}</td>
<td>${data.countScore!}</td>
<td>
<#if data.state == "3" || data.state == "4">
<@dcm.dcRdButton isConditionButton=true value="查看" class="btn btn-green" url="/dc/report/listReportData/16135115440640085031981384514634?teacher_id=" + data.teacher_id + "&rtnReportId=" + _report.id reportId=_report.id/>
<#else>
<@dcm.dcRdButton class="btn btn-orange" value="审核" url="/dc/report/detailByDataId/" + _report.id + "/" + data.id + "?_updatable=true" reportId=_report.id/>
<@dcm.dcRemoveButton class="btn btn-ringblue" value="删除" dataId=data.id reportId=_report.id/>
</#if>
</td>
</td>
</tr>
</#list>
</table>

<script>
function doCookie(){
	return true;
}

$(function(){
		//初始化日期控件
		var viewContent={
			'format' : 'yyyy-mm-dd',
			'minView' : '2'
		};
		initCalendarData("#myDiv",".date-picker",viewContent);
		$('.date-picker').next().on("click", function(){
			$(this).prev().focus();
		});
	});
</script>

</@dcm.reportData>

