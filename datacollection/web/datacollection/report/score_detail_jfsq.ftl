<@dcm.reportData  reportCode=_report.reportCode dataId=_dataId>
<p ></p>
<#if editMode?default(false)>
<@dcm.dcEditTemplate value="编辑本模板" reportId=_report.id dataId=_dataId templateType="detail" />
</#if>
<p align="left" style="padding-left:250px;">
<br />为了便于输入，19=19:00；19.15=19:15。日期输入格式，2107-1-1，2017.1.1，2017/1/1都支持。<font color='red'>积分会自动计算</font>。
<br />如果是正常工作日，开始时间为上班打卡时间+9小时，譬如上班打卡时间是8:50，那么开始时间就是17:50。
<br />如果是周末加班，开始时间直接就是上班打卡时间。
</p>

<input type="hidden" commitData="true" value="${_deptId}" id="dept_id"/>
<input type="hidden" commitData="true" value="${_teacherId}" id="teacher_id"/>
<input type="hidden" commitData="true" value="1" id="state"/>
<table align="center" class="table-bordered table-hover">


<tr>
<#if _updatable>
<th width="150" style="padding:5px" class="thForm"><font color='red'>审核人</font></th>
<td style="padding:5px">
<@p.selectOneTeacherUser clickId="operation_user_Name" id="operation_user_id" name="operation_user_Name" handler="">
<input readonly type="text" class="form-control" id="operation_user_Name" value="<@dcm.dcObjVal type="user" id=_data.operation_user_id! columnId="realName" ignoreSpan=true />" >
<input commitData="true" msgName="审核人" notNull=true  type="hidden" id="operation_user_id" value="${_data.operation_user_id!}"/>
</@p.selectOneTeacherUser>
<#else>
<@dcm.rowOne readonly=!_updatable nullable=false label="审核人" type="select" value=_data.operation_user_id! columnId="operation_user_id" vsql="select id, real_name from base_user where is_deleted = 0 and  unit_id = '" + _unitId + "' order by real_name" />
</#if>
</td>
</tr>

<tr><th width="150" style="padding:5px" class="thForm"><font color='red'>最终审核人</font></th>
<td style="padding:5px">
<input commitData="true" notNull=true type="hidden" msgName="最终审核人"  id="final_operation_user_id" value="${_data.final_operation_user_id?default("FF8080813A3EEE1C013A43152ACB09F0")}"/>
<input readonly type="text" class="form-control" id="final_operation_user_name" value="<@dcm.dcObjVal type="user" id=_data.final_operation_user_id?default("FF8080813A3EEE1C013A43152ACB09F0") columnId="realName" ignoreSpan=true />" >
</td>
</tr>

<@dcm.rowOne readonly=!_updatable nullable=false label="类型" columnId="score_type" type="select" value=_data.score_type!6 cselect="6,加班;27,值班;3,团建;4,分享;5,考勤;10,开拓;11,拜访;12,出差;13,汇报;14,表扬;15,培训;16,建议;17,证书;18,招聘;19,育人;20,会议;21,计划;22,投诉;23,批评;24,违纪;25,协作;26,方案" onChange="doChangeType()" />

<tr> 
<@dcm.columnOne readonly=!_updatable onChange="doChangeTime()" nullable=false label="发生日期" dataType="date" columnId="wo_date" value=_data.wo_date! />
</tr>

<tr id="workOver0" style="display:none;"> 
</tr>

<#--
<tr id="workOver0" style="display:none;"> 
<@dcm.columnOne readonly=!_updatable onChange="doChangeTime()" nullable=false label="【加班】日期" dataType="date" columnId="wo_date" value=_data.wo_date! />
</tr>
-->
<tr id="workOver1" style="display:none;"> 
<@dcm.columnOne readonly=!_updatable onChange="doChangeTime()" nullable=false label="【加班】开始时间" dataType="string" columnId="wo_start_time" value=_data.wo_start_time! />
</tr>
<tr id="workOver2" style="display:none;"> 
<@dcm.columnOne readonly=!_updatable onChange="doChangeTime()" nullable=false label="【加班】结束时间" dataType="string" columnId="wo_end_time" value=_data.wo_end_time! />
</tr>
<tr id="workOver3" style="display:none;"> 
<@dcm.columnOne readonly=!_updatable onChange="doChangeTime()" nullable=false label="【加班】成果" dataType="integer"  columnId="wo_effection" type="select" value=_data.wo_effection!"2"  cselect="1,重要成果;2,正常"/>
</tr>
<@dcm.rowOne readonly=!_updatable nullable=false label="积分" onChange="doScoreChange()" dataType="integer" onChange="doScoreChange()"  columnId="score" value=_data.score! />

<tr>
	<@dcm.columnOne ignoreSave="true" readonly=!_updatable nullable=false rowHeight="200" label="内容"  columnId="description2" type="textarea" value=_data.description! />
	<input commitData="true" type="hidden" id="description" name="description" />
</tr>
<#if !_updatable>
        <@dcm.rowOne readonly=!_updatable nullable=false label="状态" columnId="state" type="select" value=_data.state!"1" cselect="1,待审核;2,初审通过;3,终审通过" />
</#if>
</table>
<p ></p>
<p align="center">
<@dcm.saveButton updatable=_updatable reportId=_report.id preSave="doPreSave()" />
<@dcm.dcRdButton value="返回" reportId=_report.id />
</p>

<script>
scoreIsReadonly = true;

<@dcm.dealScoreScript />

function doPreSave(){
var scoreType = $("#score_type").val();
	if(scoreType == 6 || scoreType == 27){
		$("#description").val($("#preHtml_description2").html() + "【" + $("#description2").val() + "】");
	}
	else{
		$("#description").val($("#description2").val());
	}
	
	var woDate = $("#wo_date").val();
	if(/^\d{4}(\/)\d{2}(\/)\d{2}$/.test(woDate) == false){
		addFieldError("wo_date", "【发生日期】格式不对，格式为：YYYY/MM/DD，如：2018/03/18");
		return false;
	}
			return true;
}

function doLastStep(){
doChangeType();
doScoreChange();
}
</script>
</@dcm.reportData>

