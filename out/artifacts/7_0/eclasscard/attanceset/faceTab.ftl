<div class="filter">
		<div class="filter-item" id="gradeDiv" >
			<span class="filter-name">年级：</span>
			<div class="filter-content">
				<select name="" id="gradeId" class="form-control" onchange="changeGrade()">
					<#if grades?exists&&grades?size gt 0>
	                  	<#list grades as item>
						<option value="${item.id!}">${item.gradeName!}</option>
	              	    </#list>
	              	<#else>
	              		<option value="">--暂无年级--</option>
	                </#if>
				</select>
			</div>
		</div>
		<div class="filter-item" id="classIdDiv" >
			<span class="filter-name">班级：</span>
			<div class="filter-content">
				<select name="" id="classId" class="form-control" onChange="changeSelect()">
					<option value="">---请选择---</option>
				</select>
			</div>
		</div>
		<div class="filter-item" id="statusDiv" >
			<span class="filter-name">状态：</span>
			<div class="filter-content">
				<select name="" id="status" class="form-control" onChange="changeSelect()">
					<option value=0>全部</option>
					<option value=1>已上传</option>
					<option value=2>未上传</option>
				</select>
			</div>
		</div>
		<div class="filter-item filter-item-right">
			<span id="show-uploading" class="uploading-word color-blue hide"><i class="wpfont icon-icon-test bg-loading"></i>正在上传</span>
			<button class="btn btn-blue" onclick="uploadParamPage('1');">批量上传照片</button>
			<button class="btn btn-white" onclick="faceLowerHair();">照片下发</button>
		</div>
</div>
<div class="filter">
	<div id="showNotUpload" class="filter-item">
	</div>
	<div class="filter-item filter-item-right">
		<div id="showUploadOver" class="">
		</div>
	</div>
</div>
<div id="showStuListDiv">
</div>
<script type="text/javascript">
$(function(){
	changeGrade();
});

function uploadParamPage(type,ownerId){
	var url =  '${request.contextPath}/eclasscard/face/upload/param?type='+type+'&ownerId='+ownerId;
	$("#uploadDiv-show").load(url);
}
function faceLowerHair(){
	var url =  '${request.contextPath}/eclasscard/face/lower/hair';
	$("#tabList").load(url);
}

function deletePicture(ownerId,thisObj) {
	$.ajax({
		url:'${request.contextPath}/eclasscard/faceinfo/delete',
		type:'post',
		data:{'ownerId':ownerId},
		success:function(data) {
			var jsonO = JSON.parse(data);
			if(jsonO.success){
				layer.msg("操作成功!");
				$(thisObj).parent().prev().prev().html("");
				$(thisObj).parent().prev().html("");
				$(thisObj).parent().html('<a href="javascript:void(0);" onclick="uploadParamPage(\'2\',\'' +  ownerId + '\')">上传</a>');
			}else{
				layerTipMsgWarn("操作失败","");
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}

function changeSelect(){
	var gradeId = $("#gradeId").val();
	if(gradeId==""){
		$("#showStuListDiv").html("");
		return;
	}
	var classId = $("#classId").val();
	var status = $("#status").val();
	var url =  '${request.contextPath}/eclasscard/face/student/list?gradeId='+gradeId+'&status='+status+'&classId='+classId;
	$("#showStuListDiv").load(url);
}

function changeGrade(){
	var gradeId = $("#gradeId").val();
	if(gradeId==""){
		var htmlStr = '<option value="">---请选择---</option>';
		$("#classId").html(htmlStr);
		$("#classId").val('');
		return;
	}
	$.ajax({
        url:"${request.contextPath}/eclasscard/get/classall/page",
        data:{'gradeId':gradeId},
        dataType:'json',
        async: true,
        type:'POST',
        success: function(data) {
			var array = data;
			var htmlStr = '<option value="">---请选择---</option>';
			if(array.length > 0){
    			$.each(array, function(index, json){
    				htmlStr += '<option value="'+json.id+'">'+json.name+'</option>';
    			});
    		}
			$("#classId").html(htmlStr);
			$("#classId").val('');
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
		}
    })
	changeSelect();
}
</script>