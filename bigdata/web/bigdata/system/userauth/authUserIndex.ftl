<div class="filter-made mb-10">
	<div class="filter-item filter-item-right clearfix">
        <button class="btn btn-lightblue" onclick="addBackgroundUser();">添加用户</button>
	</div>
</div>
<#if authList?exists &&authList?size gt 0>
<table class="tables">
	<thead>
		<tr>
			<th>用户名</th>
			<th>真实姓名</th>
			<th>性别</th>
			<th>是否超级用户</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
          	<#list authList as user>
				<tr>
					<td>${user.userName!}</td>
					<td>${user.realName!}</td>
					<td>${mcodeSetting.getMcode("DM-XB","${user.sex!}")}</td>
                    <td><#if user.isSuperUser! ==1>是<#else>否</else></#if></td>
					<td>
					<a class="js-delete" href="javascript:void(0);" onclick="deleteUserAuth('${user.id!}','${user.userName!}')">
					删除</a>&nbsp;
					<#if user.isSuperUser! ==1>
                        <a class="js-delete" href="javascript:void(0);" onclick="updateSuperUser('${user.id!}',0)">
                            取消超级用户</a>
					<#else>
						<a class="js-delete" href="javascript:void(0);" onclick="updateSuperUser('${user.id!}',1)">
                        设成超级用户</a>
					</#if>
					</td>
				</tr>
      	    </#list>
	</tbody>
</table>
<#else>
<div class="no-data-common height-calc-42">
	<div class="text-center">
		<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
		<p class="color-999">暂无用户</p>
	</div>
</div>
</#if>		
<script type="text/javascript">
	function addBackgroundUser(){
		router.go({
	        path: '/bigdata/user/auth/userSelect',
	        name: '添加用户',
	        level: 3
	    }, function () {
			var url =  '${request.contextPath}/bigdata/userauth/userSelect';
			$("#contentDiv").load(url);
    	});
	}
	
	function deleteUserAuth(id,name){
		showConfirmTips('prompt',"提示","您确定要删除"+name+"吗？",function(){
			$.ajax({
		            url:'${request.contextPath}/bigdata/userauth/deleteUser',
		            data:{
		              'id':id
		            },
		            type:"post",
		            dataType: "json",
		            success:function(data){
		            	layer.closeAll();
				 		if(!data.success){
				 			showLayerTips4Confirm('error',data.msg);
				 		}else{
				 			showLayerTips('success',data.msg,'t');
						  	$("#contentDiv").load("${request.contextPath}/bigdata/userauth/index");
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
		});
	}

    function updateSuperUser(id,isSuperUser){
		var title="您确定要设置该用户为超级用户吗？"
	    if(isSuperUser ==0)
            title="您确定要取消该用户的超级用户权限吗？"
        showConfirmTips('prompt',"提示",title,function(){
            $.ajax({
                url:'${request.contextPath}/bigdata/userauth/updateSuperUser',
                data:{
                    'id':id,
					'isSuperUser':isSuperUser
                },
                type:"post",
                dataType: "json",
                success:function(data){
                    layer.closeAll();
                    if(!data.success){
                        showLayerTips4Confirm('error',data.msg);
                    }else{
                        showLayerTips('success',data.msg,'t');
                        $("#contentDiv").load("${request.contextPath}/bigdata/userauth/index");
                    }
                },
                error:function(XMLHttpRequest, textStatus, errorThrown){}
            });
        });
    }

	$(document).ready(function(){

	});
</script>