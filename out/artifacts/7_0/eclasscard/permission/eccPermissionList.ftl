<#import "/fw/macro/popupMacro.ftl" as popup />
		<input type="hidden" id="eccNameVal" value="">
		<input type="hidden" id="eccNamesVal" value="">
		<table class="table table-striped layout-fixed">
			<thead>
				<tr>
					<th><label><input type="checkbox" class="wp" id="checkAll"><span class="lbl">序号</span></label></th>
					<th>设备号</th>
					<th>用途</th>
					<th>安装场地</th>
					<th width="30%">管理人员</th>
				</tr>
			</thead>
			<tbody>
				<#if infoDtos?exists&&infoDtos?size gt 0>
				    <#list infoDtos as item>
					<tr>
						<td><label><input type="checkbox" class="wp checked-input" value="${item.eccInfo.name!}"><span class="lbl">${item_index+1}</span></label></td>
						<td>${item.eccInfo.name!}</td>
						<td>${usedForMap[item.eccInfo.type!]!}</td>
						<td>${item.eccInfo.placeName!}</td>
						<td>
						<!-- @popup.selectMoreTeacherUser clickId="userName${item_index+1}" id="userIds${item_index+1}" name="userName${item_index+1}" handler="savePermission(\"${item.eccInfo.name!}\",\"userIds${item_index+1}\")">
						</@popup.selectMoreTeacherUser -->
							<input type="hidden" id="userIds${item.eccInfo.name!}" value="${item.permisionUserIds!}">
							<input type="text" id="userName${item.eccInfo.name!}" class="form-control" value="${item.permisionUserNames!}"  onclick="editTeaId('${item.eccInfo.name!}')">
						</td>
					</tr>
				    </#list>
				<#else>
					<tr>
						<td  colspan="5" align="center">
						暂无数据
						</td>
					<tr>
				</#if>
			</tbody>
		</table>
<div style="display: none;">
	<@popup.selectMoreTeacherUser clickId="userName" id="userIds" name="userName" handler="savePermission()">
		<input type="hidden" id="userIds" value="">
		<input type="text" id="userName" class="form-control" value="">
	</@popup.selectMoreTeacherUser>
</div>
<script type="text/javascript">
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

function editALL() {
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
		layerTipMsg(false,"","请选择批量设置的班牌");
		return;
	}
	isAll = true;
	$("#eccNamesVal").val(ids);
	$('#userName').val("");
	$('#userIds').val("");
	$('#userName').click();
}

function editTeaId(eccName){
	var userIds=$("#userIds"+eccName).val();
	var userName=$("#userName"+eccName).val();
	isAll = false;
	$('#userName').val(userName);
	$('#userIds').val(userIds);
	$('#eccNameVal').val(eccName);
	$('#userName').click();
}

isSubmit = false;
function savePermission(){
	if(isSubmit){
        return;
    }
    var eccName = $("#eccNameVal").val();
    var eccNames = $("#eccNamesVal").val();
    var userIds = $("#userIds").val();
    var userName = $("#userName").val();
	isSubmit = true;
	var options = {
			url : "${request.contextPath}/eclasscard/permission/save",
			data:{eccName:eccName,eccNames:eccNames,userIds:userIds,isAll:isAll},
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存失败",data.msg);
		 		}else{
		 			if (isAll) {
		 				showbpytList();
		 			} else {
						$("#userIds"+eccName).val(userIds);
						$("#userName"+eccName).val(userName);
		 			}
    			}
    			isSubmit = false;
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
	$.ajax(options);
}
</script>