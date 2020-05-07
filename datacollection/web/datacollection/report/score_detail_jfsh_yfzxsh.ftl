<@dcm.reportData  reportCode=_report.reportCode dataId=_dataId>
<p ></p>
<#if editMode?default(false)>
<@dcm.dcEditTemplate value="编辑本模板" reportId=_report.id dataId=_dataId templateType="detail" />
</#if>
<p align="center">
说明：研发中心最终审核，默认初审人和终审人都是当前用户，不可修改。
<br />为了便于输入，19=19:00；19.15=19:15。日期输入格式，2107-1-1，2017.1.1，2017/1/1都支持。<font color='red'>积分会自动计算</font>。
</p>
<input type="hidden" commitData="true"  id="operation_user_id" name="operation_user_id" value="${_data.operation_user_id!_userId}" />
<input type="hidden" commitData="true"  value="${_data.dept_id!_deptId}" id="dept_id"/>
<table align="center" class="table-bordered table-hover">
<tr>
<th class="thForm" width="150" style="padding:5px"><font color='red'>员工名字</font></th>
<td style="padding:5px">
<#if _updatable>
<@p.selectOneTeacher clickId="teacher_Name" id="teacher_id" name="teacher_Name" handler="">
<input readonly type="text" class="form-control" id="teacher_Name" value="<@dcm.dcObjVal type="teacher" id=_data.teacher_id! columnId="teacherName" ignoreSpan=true />" >
<input commitData="true" nullable=false type="hidden" id="teacher_id" value="${_data.teacher_id!}"/>
</@p.selectOneTeacher>
<#else>
<span><@dcm.dcObjVal type="teacher" id=_data.teacher_id! columnId="teacherName"  />1</span>
</#if>
</td>
</tr>

	
        <@dcm.rowOne readonly=true nullable=false label="初步审核人" type="select" value=_data.operation_user_id!_userId columnId="operation_user_id" vsql="select id, real_name from base_user where is_deleted = 0 and  unit_id = '" + _unitId + "' order by real_name" />
<tr><th width="150" style="padding:5px" class="thForm"><font color='red'>最终审核人</font></th>
<td style="padding:5px">
<input commitData="true" notNull=true type="hidden" msgName="最终审核人"  id="final_operation_user_id" value="FF8080813A3EEE1C013A43152ACB09F0"/>
<input readonly type="text" class="form-control" id="final_operation_user_name" value="<@dcm.dcObjVal type="user" id="FF8080813A3EEE1C013A43152ACB09F0" columnId="realName" ignoreSpan=true />" >

<#--
<@p.selectOneTeacherUser clickId="final_operation_user_name" id="final_operation_user_id" name="final_operation_user_name" handler="">

</@p.selectOneTeacherUser>
-->
</td>
</tr>

	<@dcm.rowOne readonly=!_updatable nullable=false label="类型" columnId="score_type" type="select" value=_data.score_type!6 cselect="6,加班;27,值班;3,团建;4,分享;5,考勤;10,开拓;11,拜访;12,出差;13,汇报;14,表扬;15,培训;16,建议;17,证书;18,招聘;19,育人;20,会议;21,计划;22,投诉;23,批评;24,违纪;25,协作;26,方案"   onChange="doChangeType()" />


<tr> 
<@dcm.columnOne readonly=!_updatable onChange="doChangeTime()" nullable=false label="发生日期" dataType="date" columnId="wo_date" value=_data.wo_date! />
</tr>

<tr id="workOver0" style="display:none;"> 
</tr>	
	
<tr id="workOver1" style="display:none;"> 
<@dcm.columnOne readonly=!_updatable onChange="doChangeTime()" nullable=false label="【加班】开始时间" dataType="string" columnId="wo_start_time" value=_data.wo_start_time!"18:00" />
</tr>
<tr id="workOver2" style="display:none;"> 
<@dcm.columnOne readonly=!_updatable onChange="doChangeTime()" nullable=false label="【加班】结束时间" dataType="string" columnId="wo_end_time" value=_data.wo_end_time! />
</tr>
<tr id="workOver3" style="display:none;"> 
<@dcm.columnOne readonly=!_updatable onChange="doChangeTime()" nullable=false label="【加班】成果" dataType="integer"  columnId="wo_effection" type="select" value=_data.wo_effection!"2"  cselect="1,重要成果;2,正常"/>
</tr>

	<@dcm.rowOne readonly=!_updatable nullable=false label="积分" onChange="doScoreChange()" dataType="integer"  columnId="score" value=_data.score! />
	
<tr>
	<@dcm.columnOne ignoreSave="true" readonly=!_updatable nullable=false rowHeight="200" label="内容"  columnId="description2" type="textarea" value=_data.description! />
	<input commitData="true" type="hidden" id="description" name="description" />
</tr>
    <@dcm.rowOne readonly=!_updatable nullable=false label="状态" columnId="state" type="select" value="3" cselect="3,终审通过;1,待审核;2,初审通过;4,审核不通过" />
</table>
<p />
<p align="center">
<#if _updatable >
<@dcm.saveButton preSave="onchangeOpUser()" reportId=_report.id preSave="doPreSave()" />
</#if>
<@dcm.dcRdButton value="返回" reportId=_report.id />
</p>
<script>
scoreIsReadonly = false;

function doPreSave(){
var scoreType = $("#score_type").val();
	if(scoreType == 6){
		$("#description").val($("#preHtml_description2").html() + "【" + $("#description2").val() + "】");
	}
	else{
		$("#description").val($("#description2").val());
	}
	
	var woDate = $("#wo_date").val();
	if(/\d{4}(\/)\d{2}(\/)\d{2}/.test(woDate) == false){
		addFieldError("wo_date", "【发生日期】格式不对，格式为：YYYY/MM/DD，如：2018/03/18");
		return false;
	}
			return true;
}

function onchangeOpUser(){
	var teacherId = $("#teacher_id").val();
	if(teacherId != ""){
		$.getJSON({
			async:false,
			url:"${request.contextPath}/dc/teacher/object/" + teacherId,
			success:function(data){
				$("#dept_id").val(data.deptId);
			}
		});
	}
	return true;
}

<@dcm.dealScoreScript />
</script>
</@dcm.reportData>

