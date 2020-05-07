<@dcm.reportData  reportCode=_report.reportCode>
<#if editMode?default(false)>
<@dcm.dcEditTemplate value="编辑本模板" reportId=_report.id templateType="list" />
<@dcm.dcRdButton value="返回填报列表" templatePath="index.ftl" url="/dc/report/listReports" />
</#if>
<p></p>
<input type="hidden" id="creation_time@orderBy" isCondition=true value="desc" />
<table class="table  table-bordered table-hover"><tr>
<th class="thForm" width="100" style="padding:5px">员工</th>
<td width="200" style="padding:5px"> 
<@p.selectOneTeacher clickId="teacher_Name" id="teacher_id" name="teacher_Name" handler="">
<input readonly type="text" class="form-control" id="teacher_Name" value="<@dcm.dcObjVal type="teacher" id=teacher_id! columnId="teacherName" ignoreSpan=true />" >
<input isCondition=true nullable=false type="hidden" id="teacher_id" value="${teacher_id!}"/>
</@p.selectOneTeacher>
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
<td><@dcm.dcRdButton isConditionButton=true value="查找" url="/dc/report/listReportData/16135115440640085031981384514634?teacher_id=" + teacher_id + "&rtnReportId=" + rtnReportId checkReturn="doCookie()"/>
<@dcm.dcRdButton class="btn btn-green" value="返回" url="/dc/report/listReportDataSum/" + rtnReportId /></td>

</tr></table>
<p ></p>
<table class="table  table-bordered table-hover">
<tr>
<th>变更时间</th>
<th width="100">员工</th>
<th width="100">部门</th>
<th>积分变化</th>
<th width="100">积分类型</th>
<th width="100">状态</th>
<th width="100">初步审核人</th>
<th width="100">最终审核人</th>
<th width="300">说明</th>
</tr>
<#list _datas as data>
<tr id="_tr_${data.id}">
<td>${data.creation_time}</td>
<td><@dcm.dcObjVal type="teacher" id=data.teacher_id columnId="teacherName" /></td>
<td><@dcm.dcObjVal type="dept" id=data.dept_id columnId="deptName" /></td>
<td>${data.score!}</td>
<td><@dcm.dcCSelectVal value=data.score_type! cselect="6,加班;27,值班;3,团建;4,分享;5,考勤;10,开拓;11,拜访;12,出差;13,汇报;14,表扬;15,培训;16,建议;17,证书;18,招聘;19,育人;20,会议;21,计划;22,投诉;23,批评;24,违纪;25,协作;26,方案"  /></td>
<td><@dcm.dcCSelectVal value=data.state!1 cselect="1,待审核;2,初审通过;3,终审通过;4,审核不通过" /></td>
<td><@dcm.dcObjVal type="user" id=data.operation_user_id columnId="realName" /></td>
<td><@dcm.dcObjVal type="user" id=data.final_operation_user_id! columnId="realName" /></td>
<td>${data.description!}</td>
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

