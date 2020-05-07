<#import "/fw/macro/popupMacro.ftl" as popup />
<div class="table-container-header text-right">
	<a class="btn btn-blue" href="javascript:void(0);" onclick="editALL()">批量设置</a>
</div>
<div class="table-container-body">
	<input type="hidden" id="classIdVal" value="">
	<table class="table table-striped layout-fixed">
		<thead>
			<tr>
				<th><label><input type="checkbox" class="wp" id="checkAll"><span class="lbl"> 全选</span></label></th>
				<th><#if classType == "1">行政班 <#else> 教学班 </#if></th>
				<th width="70%">管理人员</th>
				<input type="hidden" value="${resourceUrl}" />
			</tr>
		</thead>
		<tbody>
			<#if returnDtos?exists&&returnDtos?size gt 0>
	          	<#list returnDtos as item>
				<tr>
					<td>
						<label><input type="checkbox" class="wp checked-input" value="${item.classId!}"><span class="lbl"></span></label>
					</td>
					<td>${item.className!}</td>
					<td>
						<input type="hidden" id="userIds${item_index+1}" value="${item.permisionUserIds!}">
						<input type="text" id="userName${item_index+1}" class="form-control" value="${item.permisionUserNames!}"  onclick="editTeaId('${item.classId!}','${item_index+1}')">
					</td>
				</tr>
	      	    </#list>
	  	    <#else>
				<tr>
					<td  colspan="3" align="center">
					暂无数据
					</td>
				<tr>
	        </#if>
		</tbody>
	</table>
</div>
<div style="display: none;">
	<@popup.selectMoreTeacherUser clickId="userName" id="userIds" name="userName" handler="savePermission()">
		<input type="hidden" id="userIds" value="">
		<input type="text" id="userName" class="form-control" value="">
	</@popup.selectMoreTeacherUser>
</div>
<script type="text/javascript">
var index = "";
var isAll = false;
$(function(){
	$("#checkAll").click(function(){
		var ischecked = false;
    	if($(this).is(':checked')){
    		ischecked = true;
    	}
	  	$(".checked-input").each(function(){
	  		if(ischecked){
	  			$(this).prop('checked',true);
	  		}else{
	  			$(this).prop('checked',false);
	  		}
  		});
	});
})
function editTeaId(classId,number){
	index=number;
	isAll = false;
	var userIds=$("#userIds"+number).val();
	var userName=$("#userName"+number).val();
	$('#userName').val(userName);
	$('#userIds').val(userIds);
	$('#classIdVal').val(classId);
	$('#userName').click();
}
function editALL(){
	var ids = "";
	$(".checked-input").each(function(){
  		if($(this).is(':checked')){
  			if(ids==''){
  				ids = $(this).val();
  			}else{
  				ids+=','+$(this).val();
  			}
  		}
	});
	if(ids==""){
		layerTipMsg(false,"","请选择批量设置的班级");
		return;
	}
	isAll = true;
	$("#classIdVal").val(ids);
	$('#userName').val("");
	$('#userIds').val("");
	$('#userName').click();
}
isSubmit = false;
function savePermission(){
	if(isSubmit){
        return;
    }
    var classIds = $("#classIdVal").val();
    var userIds = $("#userIds").val();
    var userName = $("#userName").val();
	isSubmit = true;
	var options = {
			url : "${request.contextPath}/stuwork/permission/save",
			data:{classIds:classIds,userIds:userIds,isAll:isAll,classsType:"${classType!}",permissionType:"${permissionType!}"},
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存失败",data.msg);
		 		}else{
		 			if(isAll){
                        showPermList();
		 			}else{
						$("#userIds"+index).val(userIds);
						$("#userName"+index).val(userName);
		 			}
					layerTipMsg(data.success,data.msg,"");
    			}
    			isSubmit = false;
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
	$.ajax(options);
}
function showPermList(){
    var   url =  '${request.contextPath}/stuwork/permission/list.action?classType=${classType!}&permissionType=${permissionType!}';
    $("#showPermissionList").load(url);
}
</script>