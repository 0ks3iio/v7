<@dcm.reportData  reportCode=_report.reportCode>
<@dcm.dcRdButton class="btn btn-info" value="新增积分考核" url="/dc/report/detailByDataId/" + _report.id + "/0?_unitId=" + "{unitId}"?url! + "&_userId=" + "{userId}"?url! reportId=_report.id />
<#if editMode?default(false)>
<@dcm.dcEditTemplate value="编辑本模板" reportId=_report.id templateType="list" />
<@dcm.dcRdButton value="返回填报列表" templatePath="index.ftl" url="/dc/report/listReports" />
</#if>
<p></p>
<input type="hidden" id="creation_time@orderBy" isCondition=true value="desc" />
<table class="table  table-bordered table-hover"><tr>
<@dcm.columnOne label="员工" isCondition=true type="select" vsql="select id, teacher_name from base_teacher where dept_id = '" + _deptId + "' and  is_deleted = 0 and unit_id = '" + _unitId + "' order by teacher_name" columnId="teacher_id" value=teacher_id! /> 
<@dcm.columnOne label="状态" isCondition=true columnId="state"  value=state! type="select" cselect=",-- 请选择 --;1,待审核;2,初审通过;3,终审通过;4,审核不通过" /> 
<th class="thForm" >起始日期</th><td>
<div style="display:block;" id="myDiv">
<div >
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
<td><@dcm.dcRdButton isConditionButton=true value="查找" url="/dc/report/listReportData/" + _report.id reportId=_report.id />
<input type="checkbox" id="showAllOperationUserIsMe" onclick="_onOperation2('${request.contextPath}/dc/report/listReportData/19461578629802688406718687649080', '${_report.id}');"><label for="showAllOperationUserIsMe">查看所有待我审批（包含其他部门）</label>
</td>
</tr></table>
<p ></p>
<table class="table  table-bordered table-hover">
<tr>
<th >发生时间</th>
<th width="100">创建时间</th>
<th width="100">员工</th>
<th width="100">部门</th>
<th width="100">积分变化</th>
<th width="100">积分类型</th>
<th width="100">状态</th>
<th width="100">初步审核人</th>
<th width="100">最终审核人</th>

<th>说明</th>
<th width="150">操作</th>
</tr>
<#list _datas as data>
<tr id="_tr_${data.id}">
<td>${data.wo_date!}</td>
<td>${data.creation_time}</td>
<td><@dcm.dcObjVal type="teacher" id=data.teacher_id columnId="teacherName" /></td>
<td><@dcm.dcObjVal type="dept" id=data.dept_id columnId="deptName" /></td>
<td>${data.score!}</td>
<td><@dcm.dcCSelectVal value=data.score_type! cselect="6,加班;27,值班;3,团建;4,分享;5,考勤;10,开拓;11,拜访;12,出差;13,汇报;14,表扬;15,培训;16,建议;17,证书;18,招聘;19,育人;20,会议;21,计划;22,投诉;23,批评;24,违纪;25,协作;26,方案"  /></td>
<td><@dcm.dcCSelectVal value=data.state!1 cselect="1,待审核;2,初审通过;3,终审通过;4,审核不通过" /></td>
<td><@dcm.dcObjVal type="user" id=data.operation_user_id! columnId="realName" /></td>
<td><@dcm.dcObjVal type="user" id=data.final_operation_user_id! columnId="realName" /></td>

<td>${data.description!}</td>
<#if data.state == "3" || data.state == "2" || data.state == "4"  || data.operation_user_id?default("") != _userId>
<td>
<@dcm.dcRdButton value="查看" class="btn btn-green" url="/dc/report/detailByDataId/" + _report.id + "/" + data.id + "?_updatable=false" reportId=_report.id/>
<#else>
<td width="240">
<span id="span1_${data.id}" style="display:none;" >
<@dcm.dcRdButton value="查看" class="btn btn-green" url="/dc/report/detailByDataId/" + _report.id + "/" + data.id + "?_updatable=false" reportId=_report.id/>
</span>
<span id="span2_${data.id}" >
<@dcm.dcRdButton value="审核" class="btn btn-orange" url="/dc/report/detailByDataId/" + _report.id + "/" + data.id + "?_updatable=true" reportId=_report.id/>
<#if data.final_operation_user_id?default("")  != "">
<@dcm.dcRemoteButton class="btn btn-green" value="初审通过" url="/dc/punch/chekPass/" + _report.id + "/" + data.id + "?state=2" dataId=data.id reportId=_report.id checkReturn="checkPass()" doAfter="doAfterPass(\\'" + data.id + "\\')"/>
</#if>
<@dcm.dcRemoveButton class="btn btn-ringblue" value="删除" dataId=data.id reportId=_report.id  doAfter="doAfter2(\\'" + data.id + "\\')"/>
</span>
</#if>
</td>
</tr>
</#list>
</table>
<script>
function _onOperation2(url, reportId){
	layer.load(2);
	$("#deskTopContainer").find(".model-div-show").load(url, function(){
		layer.closeAll('loading');
	});
}


function doAfter2(dataId){
	$("#_tr_" + dataId).remove();
}

function checkPass(){
	return true;
}
function doAfterPass(dataId){
	$("#span2_" + dataId).hide();
	$("#span1_" + dataId).show();
	
	//$("INPUT[dataId='" + dataId + "']").val("已审核");
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
