<div class="table-container-body no-padding">
	<div class="filter-made mb-10">
		<div class="filter-item">
            <input type="text"  id="name_search" value="${name!}" class="form-control" placeholder="请输入API名称">
		</div>
		<div class="filter-item">
			<div class="form-group">
                <div class="input-group">
                    <input type="text"  id="description_search" value="${description!}" class="form-control" placeholder="请输入API说明">
                    <a href="javascript:void(0);" class="input-group-addon" onclick="searchApi()"  hidefocus="true"><i class="wpfont icon-search"></i></a>
                </div>
            </div>
		</div>
	</div>	
	<table class="tables">
		<thead>
			<tr>
				<th class="text-left">API名称</th>
				<th>API说明</th>
				<th width="100px;">操作</th>
			</tr>
		</thead>
		<tbody>
			<#if apiList?exists&&apiList?size gt 0>
	          	<#list apiList as api>
					<tr>
						<td>${api.name!}</td>
						<td>${api.remark!}</td>
						<td>
							<a  href="javascript:void(0);" onclick="editApi('${api.id!}')">编辑</a><span class="tables-line">|</span>
							<a href="javascript:void(0);" onclick="deleteApi('${api.id!}')">删除</a>
						</td>
					</tr>
	      	    </#list>
	  	    <#else>
				<tr >
					<td  colspan="3" align="center">
					暂无API接口数据
					</td>
				<tr>
	        </#if>
		</tbody>
	</table>
</div>
<script>
function addApi(){
	$('#apiButton').hide();
	router.go({
        path: '/bigdata/datasource/api/edit',
        name: 'API新增',
        level: 3
    }, function () {
    	 var url =  '${request.contextPath}/bigdata/datasource/api/edit';
         $("#tableList").load(url);
    });
}

function editApi(id){
	$('#apiButton').hide();	
	$('#tableList').removeClass().addClass('box box-default');
	router.go({
        path: '/bigdata/datasource/api/edit?id='+id,
        name: 'API编辑',
        level: 3
    }, function () {
    	 var url =  '${request.contextPath}/bigdata/datasource/api/edit?id='+id;
         $("#tableList").load(url);
    });
}
    
function deleteApi(id){
	showConfirmTips('prompt',"提示","您确定要删除该API吗？",function(){
		$.ajax({
	            url:'${request.contextPath}/bigdata/datasource/api/delete',
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
					  	$("#tableList").load("${request.contextPath}/bigdata/datasource/list?dsType=2");
	    			}
	          },
	          error:function(XMLHttpRequest, textStatus, errorThrown){}
	    });
	});
}

function searchApi() {
	var name = $('#name_search').val();
	var description = $('#description_search').val();
    var url =  "${request.contextPath}/bigdata/datasource/apiSearch?name=" + name + "&description=" + description;
    $("#tableList").load(url);
}
</script>