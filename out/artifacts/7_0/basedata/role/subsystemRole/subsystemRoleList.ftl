<#import "/fw/macro/popupMacro.ftl" as popupMacro>
<div class="box box-default" style="overflow:auto">
	<div class="box-body">
		<table class="table table-bordered table-striped roleUser">
			<table-hover>
				<thead>
					<tr>
						<th width="20%">角色名称</th>
						<th>人员</th>
						<th width="10%">操作</th>
					</tr>
				</thead>
				<tbody id="list">
				<#if customRoleList?exists && customRoleList?size gt 0>
				<#list customRoleList as item>
					<tr>
						<td>${item.roleName}</td>
						<td><span id="showUserNames_${item.id!}">${item.userNames!}</span>
						<input type="hidden" name="roleId" value="${item.id!}">
						<input type="hidden" name="userIds" id="userIds_${item.id!}" value="${item.userIds!}">
						</td>
						<td><a class="table-btn js-editRoleUser" href="javascript:;">编辑</a></td>
					</tr>
				</#list>
				<#else>
					<tr>
						<td colspan="3">暂无数据</td>
						
					</tr>
				</#if>
				</tbody>
			</table-hover>
		</table>
	</div>
</div>
<@popupMacro.selectMoreTeacherUser clickId="userName" id="userId" name="userName" handler="saveUserIds();">
	<div class="input-group">
		<input type="hidden" id="userId" name="userId" value=""/>
		<input type="hidden" id="userName" class="form-control" value=""/>
	</div>
</@popupMacro.selectMoreTeacherUser>
<script  type="text/javascript">
	$(".roleUser").on("click",".js-editRoleUser",function(){
		var roleId=$(this).closest("tr").find('input[name="roleId"]').val();
		var userIds=$(this).closest("tr").find('input[name="userIds"]').val();
		var userNames=$(this).closest("tr").find('span').html();
		$("#userId").val(userIds);
		$("#userName").val(userNames);
		$("#popup-extra-param").val("");
		$("#userName").click();
		//额外参数
		$("#popup-extra-param").val(roleId);
	})
	var isSave=false;
	function saveUserIds(){
		if(isSave){
			return;
		}
		isSave=true;
		var roleId=$("#popup-extra-param").val();
		var userIds=$("#userId").val();
		var userNames=$("#userName").val();
		$.ajax({
			url:'${request.contextPath}/basedata/customrole/saveUsers',
			data:{'customRoleId':roleId,'userIds':userIds},
			type:'post', 
			dataType:'json',
			success:function(jsonO){
				layer.closeAll();
				if(jsonO.success){
					isSave=false;
					layer.msg("操作成功", {
							offset: 't',
							time: 2000
						});
					//$("#showUserNames_"+roleId).html(userNames);
					//$("#userIds_"+roleId).val(userIds);
					refeshList();
				}else{
					isSave=false;
					layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
					refeshList();
				}
			}
		});	
	}
	
	function refeshList(){
		var url =  '${request.contextPath}/basedata/customrole/customRoleList/page?subsystem=${subsystem!}';
		$("#showList").load(url);
	}
</script>