<div class="table-container-body">
	<table class="table table-striped">
		<thead>
			<tr>
				<th>地区</th>
				<th>单位名称</th>
				<th>域名</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		   <#if domainDtos?exists && domainDtos?size gt 0>
              <#list domainDtos as domain>
                  <tr>
					<td>${domain.regionName!}</td>
					<td>${domain.unitName!}</td>
					<td>${domain.regionAdmin!} </td>
					<td>
					  	<a href="javascript:void(0);" class="js-domain-checkIn"  value="${domain.unitId!}">修改</a>
						<a href="javascript:void(0);" class="color-red js-del"  value="${domain.unitId!}">删除</a>
					</td>
				  </tr>
              </#list>
           <#else>
               <tr>
					<td  colspan="88" align="center">
					             暂无域名记录
					</td>
			   <tr>
           </#if>   
		</tbody>
	</table>
</div>
<script>
    
//删除角色
$('.js-del').on('click', function(e){
	        var unitId = $(this).attr("value");
			e.preventDefault();
			var that = $(this);
			var index = layer.confirm("是否删除这条配置？", {
			 btn: ["确定", "取消"]
			}, 
			function(){
				$.ajax({
		            url:"${request.contextPath}/system/ops/loginDomain/delete?unitId="+unitId,
		            data:{},
		            dataType:'json',
		            contentType:'application/json',
		            type:'GET',
		            success:function (data) {
		                if(data.success){
		                    showSuccessMsgWithCall(data.msg,showDomainList);
		                }else{
		                    showErrorMsg(data.msg);
		                }
		            }
		        });
			  layer.close(index);
			})
	});
//编辑
$('.js-domain-checkIn').on('click', function(e){
	    var unitId = $(this).attr("value");
	    e.preventDefault();
	    showLoginSet(unitId);
})
</script>