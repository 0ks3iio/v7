

<form id="subForm">
            <input type="hidden" name="id" value="${(dyStudentLeave.id)!}">
			<div class="layer-content" id="myDiv">
				<div class="form-horizontal">
				    <div class="form-group">
						<label class="col-sm-3 control-label no-padding-right"><em class="color-red">*</em>开始时间：</label>
						<div class="col-sm-8">
							<div class="input-group">
								<input type="text" class="form-control datetimepicker" nullable="false" name="startTime" id="startTime" value="${(dyStudentLeave.startTime?string('yyyy-MM-dd HH:mm'))!}">
								<span class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</span>
							</div>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right"><em class="color-red">*</em>结束时间：</label>
						<div class="col-sm-8">
							<div class="input-group">
								<input type="text" nullable="false" name="endTime" class="form-control datetimepicker" id="endTime" value="${(dyStudentLeave.endTime?string('yyyy-MM-dd HH:mm'))!}">
								<span class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</span>
							</div>
						</div>
					</div>
				
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right"><em class="color-red">*</em>共计天数：</label>
						<div class="col-sm-8">
							<input type="text" nullable="false" name="days" value="${(dyStudentLeave.days)!}" vtype="number" maxLength="5" decimalLength="1" min="0" max="9999" id="days" class="form-control">
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right"><em class="color-red">*</em>请假类型：</label>
						<div class="col-sm-8">
							<select name="leaveTypeId" id="leaveTypeId" class="form-control js-changeType" nullable="false">
							<#if dyLeaveTypeList?exists && dyLeaveTypeList?size gt 0>
							    <option value="">--请选择--</option>
							    <#list dyLeaveTypeList as item>
								   <option value="${item.id!}" <#if item.id == '${(dyStudentLeave.leaveTypeId)!}'>selected</#if>>${item.name!}</option>
								</#list>
							<#else>
							    <option value="">--请选择--</option>
						    </#if>
							</select>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right"><em class="color-red">*</em>请假原因：</label>
						<div class="col-sm-8">
							<textarea nullable="false" name="remark" id="remark" maxLength="200" rows="3" class="form-control">${(dyStudentLeave.remark)!}</textarea>
						</div>
					</div>
				</div>
			</div>
</form>
	<div class="layer-footer">
	<#if (dyStudentLeave?exists && dyStudentLeave.state == 1) || !dyStudentLeave?exists>
      <a class="btn btn-lightblue" id="arrange-commit" onClick="saveLeave(1);">保存</a>
      <a class="btn btn-lightblue" id="arrange-commit" onClick="saveLeave(2);">提交</a>
    </#if>
      <a class="btn btn-grey" id="arrange-close">取消</a>
    </div>
<script>
$(function(){
   $('#startTime').datetimepicker({
		language: 'zh-CN',
		format: 'yyyy-mm-dd hh:ii',
		autoclose: true
	});
	$('#endTime').datetimepicker({
		language: 'zh-CN',
		format: 'yyyy-mm-dd hh:ii',
		autoclose: true
	});
});

// 取消按钮操作功能
$("#arrange-close").on("click", function(){
   layer.closeAll();   
});

var isSubmit=false;
function saveLeave(state){
    var check = checkValue('#myDiv');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
    var startTime = $('#startTime').val();
    var endTime = $('#endTime').val();
    if(startTime>endTime){
       layerTipMsgWarn("提示","开始时间不能大于结束时间");
	   return;
    }
	var options = {
		url : "${request.contextPath}/stuwork/studentLeave/save?state="+state,
		dataType : 'json',
		success : function(data){
	 		var jsonO = data;
		 	if(!jsonO.success){
		 		layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 		$("#arrange-commit").removeClass("disabled");
		 		return;
		 	}else{
		 		layer.closeAll();
				layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
				searchList();
    		}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#subForm").ajaxSubmit(options);
}
</script>