<div class="table-container-body">
	<table class="table table-striped">
		<thead>
			<tr>
				<th>角色名称</th>
				<th>角色类型</th>
				<th>第三方ap的名称</th>
				<th>是否激活</th>
				<th>描述</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		   <#if apRoleDtos?exists && apRoleDtos?size gt 0>
              <#list apRoleDtos as apRole>
                  <tr>
					<td>${apRole.role.name!}</td>
					<td><#if apRole.type == '1'>默认<#else>第三方ap</#if></td>
					<td><#if apRole.type == '2'>${apRole.server.name!}</#if> </td>
					<td><#if apRole.role.isActive == '0'>否<#else>是</#if></td>  
					<td>${apRole.roelDescription!}</td>                 
					<td>
					  	<a href="javascript:void(0);" class="js-role-checkIn"  value="${apRole.role.id!}">编辑</a>
						<a href="javascript:void(0);" class="color-green js-role-appoint"  value="${apRole.role.id!}">委派</a>
						<a href="javascript:void(0);" class="color-red js-del"  value="${apRole.role.id!}">删除</a>
					</td>
					<input type="hidden"  class="detailedId"  value="${apRole.role.id!}"/>
				  </tr>
              </#list>
           <#else>
               <tr>
					<td  colspan="88" align="center">
					暂无角色记录
					</td>
			   <tr>
           </#if>   
		</tbody>
	</table>
</div>
<script>
    
//删除角色
$('.js-del').on('click', function(e){
	        var roleId = $(this).attr("value");
			e.preventDefault();
			var that = $(this);
			var index = layer.confirm("是否删除这个角色？", {
			 btn: ["确定", "取消"]
			}, 
			function(){
				$.ajax({
		            url:"${request.contextPath}/system/ap/role/deleteRole?roleId="+roleId,
		            data:{},
		            dataType:'json',
		            contentType:'application/json',
		            type:'GET',
		            success:function (data) {
		                if(data.success){
		                    showSuccessMsgWithCall(data.msg,showRoleList);
		                }else{
		                    showErrorMsg(data.msg);
		                }
		            }
		        });
			  layer.close(index);
			})
	});
//编辑
$('.js-role-checkIn').on('click', function(e){
	    var roleId = $(this).attr("value");
	    var isSee = "false";
	    var isEditor = "true";
		$('.layer-role-detailedId').load("${request.contextPath}/system/ap/role/register/role/page?roleId="+roleId+"&isSee="+isSee+"&isEditor="+isEditor,function(){	
	    layer.open({
				type: 1,
				shade: .5,
				title: '登记角色',
				area: '500px',
				btn: ['确定','取消'],
				scrollbar: false,
				yes:function(index,layero){
				   saveRole("${request.contextPath}/system/ap/role/saveRole?roleId="+roleId);
	            },
				content: $('.layer-role-detailedId')
	           })
        });
})
//查看
$('.js-show-role').on('click', function(e){
	    var roleId = $(this).attr("value");
	    var isSee = "true";
		$('.layer-role-detailedId').load("${request.contextPath}/system/ap/role/register/role/page?roleId="+roleId+"&isSee="+isSee,function(){			
	    layer.open({
				type: 1,
				shade: .5,
				title: '查看角色',
				area: '500px',
				scrollbar: false,
				yes:function(index,layero){
	            },
				content: $('.layer-role-detailedId')
	           })
        });
})

//角色委派
$('.js-role-appoint').on('click', function(e){
    var roleId = $(this).attr("value");
    showRoleUserIndex(roleId);
});

</script>