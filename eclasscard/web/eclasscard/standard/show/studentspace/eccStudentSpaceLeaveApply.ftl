<ul class="leave-paper-type clearfix">
	<li class="active"><a href="javascript:void(0);" onclick="changeLeaveType(1)">请假单</a></li>
	<li><a href="javascript:void(0);" onclick="changeLeaveType(4)">长期通校申请单</a></li>
</ul>
<form id="leaveDetail">
<div class="leave-paper">
	<table class="table table-leave">
		<thead>
			<tr>
				<th class="text-center" colspan="6">请假单</th>
				<input type="hidden" id="studentId" name="studentId" value="${studentId!}"/>
				<input type="hidden" id="classId" name="classId" value="${classId!}"/>
				<input type="hidden" id="schoolId" name="schoolId" value="${schoolId!}"/>
				<input type="hidden" id="gradeId" name="gradeId" value="${gradeId!}"/>
				<input type="hidden" id="leaveType" name="leaveType" value="1"/>
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
					<input style="height:40px;" type="text" name="startTime" id="startTime" nullable="false" class="datatimepicker" placeholder="请选择">
				</td>
				<th width="14.28%">结束时间：</th>
				<td width="19.28%">
					<input style="height:40px;" type="text" name="endTime" id="endTime" nullable="false" class="datatimepicker" placeholder="请选择">
				</td>
				<th width="14.28%">天数：</th>
				<td width="19.28%">
					<input type="hidden" id="days" name="days" value="0"/>
					<span id="daysTxt">0</span>天
				</td>
			</tr>
			<tr id="reasons">
				<th class="cell-big" width="14.28%">事由：</th>
				<td width="83.33%" colspan="5">
					<input style="height:40px;" type="text" placeholder="请输入" id="remark" name="remark" autocomplete="off" value="">
				</td>
			</tr>
			<tr id="bed" style="display:none">
				<th class="cell-big" width="14.28%">床位是否保留：</th>
				<td width="83.33%" colspan="5">
					<label><input type="radio" name="hasBed" class="wp" value="1"><span class="lbl"> 保留</span></label>
					<label><input type="radio" name="hasBed" class="wp" value="0" checked><span class="lbl"> 不保留</span></label>
				</td>
			</tr>
			<tr id="message" style="display:none">
				<th width="14.28%">通校居住地：</th>
				<td width="19.28%">
					<input style="height:40px;" type="text" name="address" id="address" autocomplete="off" placeholder="请输入">
				</td>
				<th width="14.28%">陪住人姓名：</th>
				<td width="19.28%">
					<input style="height:40px;" type="text" name="mateName" id="mateName" autocomplete="off" placeholder="请输入">
				</td>
				<th width="14.28%">与本人关系：</th>
				<td width="19.28%">
					<input style="height:40px;" type="text" name="mateGx" id="mateGx" autocomplete="off" placeholder="请输入">
				</td>
			</tr>
			<tr>
				<th width="14.28%">选择流程：</th>
				<td width="83.33%" colspan="5">
					<select name="flowId" id="flowId" class="form-control" style="width:30%;height:40px;">
					</select>
				</td>
			</tr>
		</tbody>
	</table>
	<div class="leave-paper-btn"><button class="btn btn-lg" id="subLeave">提交申请</button></div>
</div>
</form>
<script>
$(document).ready(function(){
	// 请假选择时间
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
		// invalid: ['w0', 'w6', '5/1', '12/24', '12/25'],
		minWidth: 150,
		dateFormat: 'yy-mm-dd'
	});

	$('.leave-paper-type a').on('click', function(e){
		e.preventDefault();
		$(this).parent().addClass('active').siblings().removeClass('active');
	})
	
	changeLeaveType(1);
});

function changeLeaveType(type) {
	$("#leaveType").val(type);
	$("#startTime").val("");
	$("#endTime").val("");
	$("#remark").val("");
	$("#days").val("");
	$("#daysTxt").text(0);
	if (type == 1) {
		$("#reasons").removeAttr("style");
		$("#bed").attr("style","display:none");
		$("#message").attr("style","display:none");
	} else {
		$("#reasons").attr("style","display:none");
		$("#bed").removeAttr("style");
		$("#message").removeAttr("style");
	}
	var schoolId = $("#schoolId").val();
	var gradeId = $("#gradeId").val();
	var flowId = $("#flowId");
	$.ajax({
		url:"${request.contextPath}/eccShow/eclasscard/standard/studentSpace/leaveflow",
		data:{"gradeId":gradeId,"schoolId":schoolId,"leaveType":type},
		dataType: "json",
		success: function(data){
			flowId.html("");
			if($.isEmptyObject(data)){
				flowId.append("<option value='' >--请选择--</option>");
			}else{
				for(var key in data){
					flowId.append("<option value='"+ key +"' >"+data[key]+"</option>");
				}
			}
		}
	});
}
	
$("#startTime").change(function(){
    var startDate = $("#startTime").val();
	var endDate = $("#endTime").val();
	var leaveType = $("#leaveType").val();
	if(startDate!=null&&startDate!="" && endDate!=null&&endDate!=""){
		if(startDate>endDate){
			showMsgTip("开始时间不能大于结束时间");
			return;
		}
		if (leaveType == 4) {
		var num = countTimeLength('D',startDate,endDate);
		$("#days").val(num);
		$("#daysTxt").text(num);
		} else {
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
		}
	}
});

$("#endTime").change(function(){
    var startDate = $("#startTime").val();
	var endDate = $("#endTime").val();
	var leaveType = $("#leaveType").val();
	if(startDate!=null&&startDate!="" && endDate!=null&&endDate!=""){
		if(startDate>endDate){
			showMsgTip("开始时间不能大于结束时间");
			return;
		}
		if (leaveType == 4) {
			var num = countTimeLength('D',startDate,endDate);
			$("#days").val(num);
			$("#daysTxt").text(num);
		} else {
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
		}
	}
});

function countTimeLength(interval, date1, date2) {
	var leaveType = $("#leaveType").val();
	if (leaveType == 4) {
		 date1=/\d{4}-\d{1,2}-\d{1,2}/g.exec(date1);
		date1 = date1 + ' 00:00';
		 date2=/\d{4}-\d{1,2}-\d{1,2}/g.exec(date2);
		date2 = date2 + ' 23:59';
	}
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

var isSubmit = false;
$("#subLeave").unbind().on("click", function(e){
	e.preventDefault();
	saveLeave();
})

function saveLeave(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	var leaveType = $("#leaveType").val();
	
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
	if (leaveType == 1) {
	if(days > 30){
		showMsgTip("一般请假天数不能超过30天");
		isSubmit = false;
		return;
	}
	} else {
	if(days > 180){
		showMsgTip("通校申请天数不能超过6个月");
		isSubmit = false;
		return;
	}
	}
	
	var flowId= $("#flowId").val();
	if(!flowId||flowId==""){
		 showMsgTip("请选择一个流程");
		 isSubmit = false;
		 return;
	}
	
	if (leaveType != 4) {
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
	} else {
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
	}
	//提交数据
	var options = {
		url : '${request.contextPath}/eccShow/eclasscard/studentLeave/applyPage/save',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
				studentLeaveTab(1,leaveType);
	 		}else{
	 			showMsgTip("失败："+data.msg);
	 			isSubmit=false;
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){} 
	};
	$("#leaveDetail").ajaxSubmit(options);
}
</script>