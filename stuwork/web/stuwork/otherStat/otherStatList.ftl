<div class="table-container-body">
	<table class="table table-striped">
		<thead>
			<tr>
				<th class="text-center" style="width:15%">行政班级</th>
				<th class="text-center" style="width:15%">考核分</th>
				<th class="text-center" style="width:45%">考核内容</th>
				<th class="text-center" style="width:15%">考核时间</th>
				<th class="text-center" style="width:10%">操作</th>
			</tr>
		</thead>
		<tbody>
		<#if checksList?exists && checksList?size gt 0>
			<#list checksList as item>
			<tr>
				<td class="text-center">${item.className!}</td>
				<td class="text-center">${(item.score)?string("0.#")}</td>
				<td class="text-center" title="${item.remark!}"><#if (item.remark!) != "" && (item.remark!)?length gt 20>${(item.remark!)?substring(0,20)}...<#else>${item.remark!}</#if></td>
				<td class="text-center">${(item.checkTime?string('yyyy-MM-dd'))!}</td>
				<td class="text-center">
					<a href="javascript:void(0);" onclick="editStat('${item.id!}')">编辑</a>
					<a href="javascript:void(0);" onclick="deleteStat('${item.id!}')">删除</a>
				</td>
			</tr>
			</#list>
		<#else>
			<tr>
				<td colspan="8" align="center">暂无数据</td>
			</tr>
		</#if>  
		</tbody>
	</table>
</div>
<script>
	function editStat(id){
		var acadyear = $("#acadyear").val();
		var semester = $("#semester").val();
		var str = "?acadyear="+acadyear+"&semester="+semester+"&id="+id;
		var url = "${request.contextPath}/stuwork/otherStart/edit/page"+str;
		indexDiv = layerDivUrl(url,{title: "编辑",width:520,height:380});
	}
	
	function deleteStat(id){
		showConfirmMsg('确定删除么？','提示',function(ii){
	 		var ii = layer.load();
     		$.ajax({
				url:'${request.contextPath}/stuwork/otherStart/delete',
				data: {'id':id},
				type:'post',
				success:function(data) {
					var jsonO = JSON.parse(data);
		 			if(jsonO.success){
                    	layer.closeAll();
						layerTipMsg(jsonO.success,"删除成功",jsonO.msg);
				  		changeList();
		 			}
		 			else{
		 				layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
					}
					layer.close(ii);
					},
	 			error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
		});
	}
</script>