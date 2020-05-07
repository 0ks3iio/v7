<div class="table-container-body">
	<table class="table table-bordered table-striped layout-fixed">
		<thead>
			<tr>
				<th>类型</th>
				<th>接口名称</th>
				<th>接口地址</th>
				<th>参数字段</th>
				<th>数据库参数</th>
				<th>参数描述</th>
				<th>是否必填</th>
				<th>微代码</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		   <#if openParamDtos??&& openParamDtos?size gt 0>
             <#list openParamDtos as dto>
                <tr data-paramid="${dto.id!}">
                   <td>${dto.type!} </td>
                   <td>${dto.interfaceName!} </td>
                   <td>${dto.uri!} </td>
                   <td>${dto.paramName!} </td>
                   <td>${dto.paramColumnName!} </td>
                   <td>${dto.description!} </td>
                   <td><#if dto.mandatory == 1>是<#else>否</#if></td>
                   <td>${dto.mcodeId!} </td>
                   <td>
                       <a href="javascript:;" class="table-btn color-lightblue modify">修改</a>
		               <a href="javascript:;" class="table-btn color-red delete">删除</a>
		           </td>
				</tr>
            </#list>
          <#else>
			<tr>
				<td  colspan="9" align="center">
				 暂无参数
				</td>
			<tr>
		  </#if>
		</tbody>
	</table>
</div>

<script>
//删除参数
$('.delete').on('click', function(e){
	        var paramId = $(this).parents('tr').data('paramid');
			e.preventDefault();
			var that = $(this);
			var index = layer.confirm("是否删除这条参数？", {
			 btn: ["确定", "取消"]
			}, 
			function(){
				$.ajax({
		            url:"${request.contextPath}/system/interface/param/delParam?paramId="+paramId,
		            data:{},
		            dataType:'json',
		            contentType:'application/json',
		            type:'GET',
		            success:function (data) {
		                if(data.success){
		                    showSuccessMsgWithCall(data.msg,showParamList);
		                }else{
		                    showErrorMsg(data.msg);
		                }
		            }
		        });
			  layer.close(index);
			})
});
// 修改参数
$('.modify').on('click', function(e){
	var paramId = $(this).parents('tr').data('paramid');
	$('.layer-param-detailedId').load("${request.contextPath}/system/interface/param/editParam?paramId="+paramId,function(){			
	layer.open({
				type: 1,
				shade: .5,
				title: '修改参数',
				area: '500px',
				btn: ['确定','取消'],
				yes:function(index,layero){
			       saveParam("${request.contextPath}/system/interface/param/saveParam");
	            },
				content: $('.layer-param-detailedId')
	           })
    });
})
</script>
