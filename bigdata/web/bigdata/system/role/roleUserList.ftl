<div class="filter-made mb-10">
	<div class="filter-item filter-item-right clearfix">
        <button class="btn btn-lightblue" onclick="addRoleUser();">添加用户</button>
	</div>
</div>
<#if  userRoleList?exists &&userRoleList?size gt 0>
<table class="tables">
	<thead>
		<tr>
			<th>用户名</th>
			<th>真实姓名</th>
			<th>性别</th>
			<th>部门</th>
			<th>单位</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		
          	<#list userRoleList as user>
				<tr>
					<td>${user.username!}</td>
					<td>${user.realName!}</td>
					<td>${mcodeSetting.getMcode("DM-XB","${user.sex!}")}</td>
					<td>${user.deptName!}</td>
					<td>${user.unitName!}</td>
					<td>
					<a class="js-delete" href="javascript:void(0);" onclick="deleteRoleUser('${user.userId!}','${user.username!}')">
					删除</a>&nbsp;	
					</td>
				</tr>
      	    </#list>
	</tbody>
</table>
<#else>
	<div class="no-data-common">
		<div class="text-center">
			<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
			<p class="color-999">暂无用户</p>
		</div>
	</div>
</#if>
<script>
	function addRoleUser(){
			var url =  '${request.contextPath}/bigdata/role/roleUserSelect?roleId=${roleId!}';
			$("#roleUserListDiv").load(url);
	}

	function deleteRoleUser(id,name){
		showConfirmTips('prompt',"提示","您确定要删除"+name+"吗？",function(){
			$.ajax({
		            url:'${request.contextPath}/bigdata/role/deleteUser',
		            data:{
		              'roleId':'${roleId!}',
		              'userId':id
		            },
		            type:"post",
		            dataType: "json",
		            success:function(data){
		            	layer.closeAll();
				 		if(!data.success){
				 			showLayerTips4Confirm('error',data.msg);
				 		}else{
				 			showLayerTips('success',data.msg,'t');
						  	$('#roleUserListDiv').addClass('active').siblings().removeClass('active');
							var url =  "${request.contextPath}/bigdata/role/roleUserList?roleId=${roleId!}";
							$("#roleUserListDiv").load(url);
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
		});
	}
</script>