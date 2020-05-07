<form id="myProject">
	<div class="form-horizontal projectDetail">
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">学年：</label>
			<div class="col-sm-9">
				<p class="form-group-txt">${project.acadyear!}</p>
				<input type="hidden" value="${project.acadyear!}" name="acadyear">
				<input type="hidden" value="${project.id!}" name="id">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">学期：</label>
			<div class="col-sm-9">
				<p class="form-group-txt">第<#if project.semester == '1'>一<#else>二</#if>学期</p>
				<input type="hidden" value="${project.semester!}" name="semester">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">评教类型：</label>
			<div class="col-sm-9">
			<#if readonly == '1'>
				<input type="hidden" value="${project.evaluateType?default('')}" name="evaluateType">
				<p class="form-group-txt">${mcodeSetting.getMcode("DM-PJLX","${project.evaluateType?default('')}")}</p>
			<#else>
				<select class="form-control" id="evaluateType" name="evaluateType" onchange="setGradeDiv();">
					 ${mcodeSetting.getMcodeSelect("DM-PJLX", project.evaluateType, "0")}
				</select>
			</#if>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">开始时间：</label>
			<div class="col-sm-9">
				<div class="input-group">
					<input class="form-control date-picker startTime-date date-picker-time" vtype="data" type="text" nullable="true" name="beginTime" id="beginTime" placeholder="开始时间" value="${((project.beginTime)?string('yyyy-MM-dd HH:mm'))?if_exists}">
					<span class="input-group-addon">
						<i class="fa fa-calendar bigger-110"></i>
					</span>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">截止时间：</label>
			<div class="col-sm-9">
				<div class="input-group">
					<input class="form-control date-picker endTime-date date-picker-time" vtype="data" type="text" nullable="true" name="endTime" id="endTime" placeholder="截止时间" value="${((project.endTime)?string('yyyy-MM-dd HH:mm'))?if_exists}">
					<span class="input-group-addon">
						<i class="fa fa-calendar"></i>
					</span>
				</div>
			</div>
		</div>
		<#if readonly == '1' >
			<#if project.evaluateType?default('')=="11" || project.evaluateType?default('')=="12">
			<div class="form-group" id="gradeDiv">
				<label class="col-sm-3 control-label no-padding-right">参评年级：</label>
				<div class="col-sm-9">
					<p class="form-group-txt">${gardeName!}</p>
				</div>
			</div>
			</#if>
		<#else>
			<div class="form-group" id="gradeDiv" style="display:none">
				<label class="col-sm-3 control-label no-padding-right">参评年级：</label>
				<div class="col-sm-9" style="max-height:100px;overflow:auto;">
				<#if gradeList?exists && gradeList?size gt 0>
				<#list gradeList as item>
					<label><input name="gradeIds" value="${item.id!}"  type="checkbox"  class="wp checked-input"><span class="lbl">${item.gradeName!}</span></label>
				</#list>
				</#if>
				</div>
			</div>
		</#if>
	</div>
</form>	
<#-- 确定和取消按钮 -->
<div class="layer-footer">
	<#if readonly == '0'>
	</#if>
	<button class="btn btn-lightblue" id="item-commit">保存</button>
	<button class="btn btn-grey" id="item-close" onclick="layer.closeAll();">取消</button>
</div>
<script>
$(function(){
	//初始化日期控件
	var viewContent={
		'format' : 'yyyy-mm-dd hh:ii',
		'minView' : '0',
		'startDate' : '${startTime?string('yyyy-MM-dd HH:mm')}',
		'endDate':'${endTime?string('yyyy-MM-dd HH:mm')}'
	};
	initCalendarData("#myProject",".date-picker",viewContent);
});
function setGradeDiv(){
	var evaluateType=$("#evaluateType").val();
	if(evaluateType=="10" || evaluateType=="13"){
		$("#gradeDiv").hide();
	}else{
		$("#gradeDiv").show();
	}
}
// 确定按钮操作功能
var isSubmit=false;
$("#item-commit").on("click", function(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	$(this).addClass("disabled");
	var check = checkValue('.projectDetail');
	if(!check){
	 	$(this).removeClass("disabled");
	 	isSubmit=false;
	 	return;
	}
	// 提交数据
	var ii = layer.load();
	var options = {
		url : '${request.contextPath}/evaluate/project/save',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
				layerTipMsg(data.success,"成功",data.msg);
				$(".model-div").load("${request.contextPath}/evaluate/project/index/page");
	 		}else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			$("#item-commit").removeClass("disabled");
	 			isSubmit=false;
			}
			layer.close(ii);
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){} 
	};
	$("#myProject").ajaxSubmit(options);
 });	
 
</script>