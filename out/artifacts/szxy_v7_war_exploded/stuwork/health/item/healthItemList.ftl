<div class="main-content">
	
	<div class="main-content-inner">
		<div class="table-container">
			<div class="table-container-header text-right">
				<button class="btn btn-blue" onclick="editItem('')">新增</button>
			</div>
			<div class="table-container-body">
				<table class="table table-striped table-hover table-layout-fixed no-margin">
					<thead>
						<tr>
							<th>序号</th>
							<th>体检项</th>
							<th>单位</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<#if itemList?exists && itemList?size gt 0>
						<#list itemList as item>
							<tr>
								<td>${item_index+1}</td>
								<td>${item.itemName!}</td>
								<td><#if item.itemUnit?default("")=="">/<#else>${item.itemUnit!}</#if></td>
								<td>
									<a href="javascript:" class="color-lightblue"  onclick="editItem('${item.id!}');">修改</a>
  	 								<a href="javascript:" class="color-blue"  onclick="deleteItem('${item.id!}');">删除</a>
								</td>
							</tr>
						</#list>
						<#else>
							<td colspan="4" align="center">暂无数据</td>
						</#if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div><!-- /.main-content -->
<script>
	function editItem(id){
		var url = "${request.contextPath}/stuwork/health/item/edit?id="+id;
		indexDiv = layerDivUrl(url,{title: "指标设置维护",width:500,height:350});
	}
	function deleteItem(id){
		showConfirmMsg('确认删除？','提示',function(){
			var ii = layer.load();
			$.ajax({
				url:'${request.contextPath}/stuwork/health/item/delete',
				data: {'id':id},
				type:'post',
				success:function(data) {
					layer.closeAll();
					var jsonO = JSON.parse(data);
			 		if(jsonO.success){
					   itemShowList(1);
			 		}else{
			 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
					}
					layer.close(ii);
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
		});
	}
</script>
