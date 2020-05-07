<#include "/eclasscard/show/showMsgtip.ftl">
<form id="leaveDetail">
<div class="box box-white leaveDetail">
	<div class="box-header">
		<h3 class="box-title">请假管理</h3>
	</div>
	<div class="box-body">
		<ul class="nav-tab" role="tablist" data-action="tab">
			<li class="active" role="presentation" ><a href="javascript:;" role="tab">请假申请</a></li>
			<li role="presentation" onclick="queryMsgShow(3)"><a href="javascript:;" role="tab">我的请假</a></li>
		</ul>
		<div class="tab-content">
			<div id="aaa" class="tab-pane active" role="tabpanel">
				<div class="type-choose">
					<span <#if leaveType == "1">class="active"<#else> onclick="queryMsgShow(4,1)"</#if>>一般请假</span>
					<#-- <span <#if leaveType == "2">class="active"<#else> onclick="queryMsgShow(4,2)"</#if>>临时出校申请单</span> -->
					<#-- <span <#if leaveType == "3">class="active"<#else> onclick="queryMsgShow(4,3)"</#if>>临时通校、住校申请单</span> -->
					<span <#if leaveType == "4">class="active"<#else> onclick="queryMsgShow(4,4)"</#if>>通校申请</span>
				</div>
				<div class="leave-paper">
					<table class="table table-leave">
						<thead>
							<tr>
								<th colspan="6"> <#if leaveType == "1">一般请假<#elseif leaveType == "4">通校申请</#if>
								<input type="hidden" id="studentId" name="studentId" value="${studentId!}"/>
								<input type="hidden" id="classId" name="classId" value="${classId!}"/>
								<input type="hidden" id="leaveType" name="leaveType" value="${leaveType!}"/>
								</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<th width="14.28%">学生姓名：</th>
								<td width="19.28%">${studentName!}</td>
								<th width="14.28%">班级：</th>
								<td width="19.28%">${className!}</td>
								<th width="14.28%">申请人：</th>
								<td width="19.28%">${studentName!}</td>
							</tr>
							<tr>
								<th width="14.28%">开始时间：</th>
								<td width="19.28%">
									<#-- <input name="startTime" id="startTime" type="date" /> -->
									<input type="text" name="startTime" id="startTime" nullable="false" class="datatimepicker" placeholder="请选择">
								</td>
								<th width="14.28%">结束时间：</th>
								<td width="19.28%">
									<#-- <input type="date" name="endTime" id="endTime" nullable="false"/> -->
									<input type="text" name="endTime" id="endTime" nullable="false" class="datatimepicker" placeholder="请选择">
								</td>
								<th width="14.28%">天数：</th>
								<td width="19.28%">
									<input type="hidden" id="days" name="days" value="0"/>
									<span id="daysTxt">0</span>天
								</td>
							</tr>
							<#if leaveType == "2">
							<tr>
								<th width="14.28%">联系电话：</th>
								<td width="83.33%" colspan="5">
									<input type="text" placeholder="请输入" id="linkPhone" name="linkPhone" value="">
								</td>
							</tr>
							</#if>
							<#if leaveType != "4">
							<tr>
								<th class="cell-big" width="14.28%">事由：</th>
								<td width="83.33%" colspan="5">
								<input type="text" placeholder="请输入" id="remark" name="remark" value="">
								</td>
							</tr>
							</#if>
							<#if leaveType == "3">
							<tr>
								<th class="cell-big" width="14.28%">申请类型：</th>
								<td width="83.33%" colspan="5">
									<label><input type="radio" name="applyType" class="wp" value="1"><span class="lbl"> 住校</span></label>
									<label><input type="radio" name="applyType" class="wp" value="2" checked><span class="lbl"> 通校</span></label>
								</td>
							</tr>
							</#if>
							<#if leaveType == "4">
							<tr>
								<th class="cell-big" width="14.28%">床位是否保留：</th>
								<td width="83.33%" colspan="5">
									<label><input type="radio" name="hasBed" class="wp" value="1"><span class="lbl"> 保留</span></label>
									<label><input type="radio" name="hasBed" class="wp" value="0" checked><span class="lbl"> 不保留</span></label>
								</td>
							</tr>
							<tr>
								<th width="14.28%">通校居住地：</th>
								<td width="19.28%">
									<input type="text" name="address" id="address" placeholder="请输入">
								</td>
								<th width="14.28%">陪住人姓名：</th>
								<td width="19.28%">
									<input type="text" name="mateName" id="mateName" placeholder="请输入">
								</td>
								<th width="14.28%">与本人关系：</th>
								<td width="19.28%">
									<input type="text" name="mateGx" id="mateGx" placeholder="请输入">
								</td>
							</tr>
							</#if>
							<tr>
								<th width="14.28%">选择流程：</th>
								<td width="83.33%" colspan="5">
								<select name="flowId" id="flowId" class="form-control" style="width:30%;">
									<option value="">--请选择--</option>
									<#list flows?keys as key> 
										<option value="${key!}">${flows[key]}</option>
									</#list>
								</select>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="text-center"><button type="button" class="btn" id="subLeave">提交审核</button></div>
			</div>
		</div>
	</div>
</div>
</form>
<script>
var isSubmit = false;
$("#subLeave").unbind().on("click", function(){
	saveLeave();
})

$(document).ready(function(){
	var now = new Date();
	$('.datatimepicker').mobiscroll().datetime({
        theme: 'mobiscroll',
        showLabel: true,
        lang: 'zh',
        display: 'bottom',
        steps: {
	    	minute: 10,
	    	zeroBased: false
	    },
        min: new Date(now.getFullYear(), now.getMonth(), now.getDate()),
        minWidth: 150,
        dateFormat: 'yy-mm-dd',
        <#if leaveType == "4" ||  leaveType == "3">
        timeFormat: '',
        timeWheels: 'B'
        </#if>
    });
})
//键盘提交按钮
$('#leaveDetail').submit(function () { 
	saveLeave();
	return false;
});

function saveLeave(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	var startTime = $('#startTime').val();
	
	if(startTime == null || startTime == ""){
		showMsgTip("开始时间为空");
		isSubmit = false;
		return;
	}
	var endTime = $('#endTime').val();
	if(endTime == null  || endTime == ""){
		showMsgTip("结束时间为空");
		isSubmit = false;
		return;
	}
	if(startTime!=null&&startTime!="" && endTime!=null&&endTime!=""){
		if(startTime>endTime){
			showMsgTip("开始时间不能大于结束时间");
			isSubmit = false;
			return;
		}
	}
	var days = $('#days').val();
	if(days == null  || days == ""){
		showMsgTip("天数不能为空");
		isSubmit = false;
		return;
	}
	<#if leaveType == "1">
	if(days > 30){
		showMsgTip("一般请假天数不能超过30天");
		isSubmit = false;
		return;
	}
	<#else>
	if(days > 180){
		showMsgTip("通校申请天数不能超过6个月");
		isSubmit = false;
		return;
	}
	</#if>
	var flowId= $("#flowId").val();
	if(!flowId||flowId==""){
		 showMsgTip("请选择一个流程");
		 isSubmit = false;
		 return;
	}
	<#if leaveType != "4">
	var remark= $("#remark").val();
	if(!remark||remark==""){
		 showMsgTip("请输入事由!");
		 isSubmit = false;
		 return;
	}else{
		if(remark.length > 100){
			showMsgTip("事由长度不能超过100个字符!");
			 isSubmit = false;
			 return;
		}
	}
	<#else>
	var mateName= $("#mateName").val();
	if(!mateName||mateName==""){
	}else{
		if(mateName.length > 25){
			showMsgTip("陪住人姓名长度不能超过25个字符!");
			 isSubmit = false;
			 return;
		}
	}
	var mateGx= $("#mateGx").val();
	if(!mateGx||mateGx==""){
	}else{
		if(mateGx.length > 25){
			showMsgTip("与本人关系长度不能超过25个字符!");
			 isSubmit = false;
			 return;
		}
	}
	var address= $("#address").val();
	if(!address||address==""){
	}else{
		if(address.length > 100){
			showMsgTip("通校居住地长度不能超过100个字符!");
			 isSubmit = false;
			 return;
		}
	}
	</#if>
	//提交数据
	var options = {
		url : '${request.contextPath}/eccShow/eclasscard/studentLeave/applyPage/save',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
				queryMsgShow(3)
	 		}else{
	 			//layerTipMsg(data.success,"失败",data.msg);
	 			showMsgTip("失败："+data.msg);
	 			isSubmit=false;
			}
			//layer.close(ii);
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){} 
	};
	$("#leaveDetail").ajaxSubmit(options);
}
$("#startTime").change(function(){
    $("#startTime").attr("value",$(this).val()); //赋值
    var startDate = $("#startTime").val();
	var endDate = $("#endTime").val();
	if(startDate!=null&&startDate!="" && endDate!=null&&endDate!=""){
		if(startDate>endDate){
			showMsgTip("开始时间不能大于结束时间")
			return;
		}
		<#if leaveType == "4">
		var num = countTimeLength('D',startDate,endDate);
		$("#days").val(num);
		$("#daysTxt").text(num);
		<#else>
		var classId = $("#classId").val();
		$.ajax({
				url:'${request.contextPath}/eccShow/eclasscard/studentLeave/countDays',
				data: {'classId':classId,'date1':startDate,'date2':endDate},
				type:'post',
				success:function(data) {
					var jsonO = JSON.parse(data);
			 		if(jsonO.success){
					  	$("#days").val(jsonO.msg);
						$("#daysTxt").text(jsonO.msg);
			 		}else{
			 			$("#days").val("");
			 			$("#daysTxt").text("");
			 			showMsgTip(jsonO.msg);
					}
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
			</#if>
	}
    
});
$("#endTime").change(function(){
    $("#endTime").attr("value",$(this).val()); //赋值
    var startDate = $("#startTime").val();
	var endDate = $("#endTime").val();
	if(startDate!=null&&startDate!="" && endDate!=null&&endDate!=""){
		if(startDate>endDate){
			showMsgTip("开始时间不能大于结束时间")
			return;
		}
		<#if leaveType == "4">
		var num = countTimeLength('D',startDate,endDate);
		$("#days").val(num);
		$("#daysTxt").text(num);
		<#else>
		var classId = $("#classId").val();
		$.ajax({
				url:'${request.contextPath}/eccShow/eclasscard/studentLeave/countDays',
				data: {'classId':classId,'date1':startDate,'date2':endDate},
				type:'post',
				success:function(data) {
					var jsonO = JSON.parse(data);
			 		if(jsonO.success){
					  	$("#days").val(jsonO.msg);
						$("#daysTxt").text(jsonO.msg);
			 		}else{
			 			$("#days").val("");
			 			$("#daysTxt").text("");
			 			showMsgTip(jsonO.msg);
					}
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
		</#if>
	}
});

function countTimeLength(interval, date1, date2) {
	<#if leaveType == "4" ||  leaveType == "3">
	date1 = date1 + ' 00:00';
	date2 = date2 + ' 23:59';
	</#if>
    var objInterval = {'D' : 1000 * 60 * 60 * 24, 'H' : 1000 * 60 * 60, 'M' : 1000 * 60, 'S' : 1000, 'T' : 1};  
    interval = interval.toUpperCase();  
    var dt1 = Date.parse(date1.replace(/-/g, "/"));  
    var dt2 = Date.parse(date2.replace(/-/g, "/"));  
    try{  
        return ((dt2 - dt1) / objInterval[interval]).toFixed(1);//保留两位小数点  
    }catch (e){
        return e.message;  
    }
} 

</script>
