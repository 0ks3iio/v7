<div class="filter-made mb-10">
	<div class="filter-item filter-item-right clearfix">
        <button class="btn btn-lightblue" onclick="editNodeServer();">新增节点服务</button>
	</div>
</div>
<#if nodeServerList?exists && nodeServerList?size gt 0>
		<table class="tables">
		    <thead>
		        <tr>
		            <th style="width:100px">名称</th>
		            <th>类型</th>
		            <th>状态</th>
					<th>备注</th>
		            <th>操作</th>
		        </tr>
		    </thead>
		    <tbody class="kanban-content">
			<#list nodeServerList as nodeServer>
				<tr>
					<td title="${nodeServer.name!?html}">
						<div style="width: 150px;" class="ellipsis">
							${nodeServer.name!?html}
						</div>
					</td>
					<td>${nodeServer.type!}</td>
					<td><#if nodeServer.status! == 1>可用
							<#else >不可用
						</#if></td>
					<td title="${nodeServer.remark!?html}">
						<div style="width: 150px;" class="ellipsis">
							${nodeServer.remark!?html}
						</div>
					</td>
					<td>
						<a href="javascript:void(0)" onclick="editNodeServer('${nodeServer.id!}')"  class="look-over">编辑</a><span class="tables-line">|</span>
						<a href="javascript:void(0)" onclick="deleteNodeServer('${nodeServer.id!}','${nodeServer.name!?html?js_string}');">删除</a>
					</td>
				</tr>
			</#list>
		    </tbody>
		</table>
<#else>
	<div class="no-data-common" style="height: 80%">
	<div class="text-center">
		<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
		<p class="color-999">
			该节点暂无任何服务
		</p>
	</div>
</div>
</#if>
<div class="layer layer-form layer-node-server-edit" id="nodeServerEditDiv">
</div>
<script>
	function deleteNodeServer(id,name){
		showConfirmTips('prompt',"提示","您确定要删除"+name+"吗？",function(){
			$.ajax({
				url:'${request.contextPath}/bigdata/node/server/delete',
				data:{
					'id':id
				},
				type:"post",
				dataType: "json",
				success:function(result){
					layer.closeAll();
					if(!result.success){
						showLayerTips4Confirm('error',result.message);
					}else{
						showLayerTips('success',result.message,'t');
						var url = '${request.contextPath}/bigdata/node/server/index?id='+'${nodeId!}';
						$('.page-content').load(url);
					}
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
		});
	}

	function editNodeServer(id) {
		$.ajax({
			url: '${request.contextPath}/bigdata/node/server/edit',
			type: 'GET',
			data: {
				id:id
			},
			dataType: 'html',
			beforeSend: function(){
				$('#nodeServerEditDiv').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
			},
			success: function (response) {
				$('#nodeServerEditDiv').html(response);
			}
		});
		var isSubmit = false;
		layer.open({
			type: 1,
			shade: .6,
			title: '节点服务编辑',
			btn: ['保存','取消'],
			yes:function(index, layero){
				if(isSubmit){
					showLayerTips('warn','数据保存中,请不要重复点击','t');
					return;
				}
				isSubmit = true;
				if($('#name').val()== '') {
					layer.tips("名称不能为空!", "#name", {
						tipsMore: true,
						tips: 3
					});
					isSubmit = false;
					return;
				}

				var  options = {
					url : "${request.contextPath}/bigdata/node/server/saveNodeServer",
					data:{
						nodeId:'${nodeId!}'
					},
					dataType : 'json',
					success : function(result){
						if(!result.success){
							showLayerTips4Confirm('error',result.message);
							isSubmit = false;
						}else{
							showLayerTips('success',"保存成功",'t');
							var url = '${request.contextPath}/bigdata/node/server/index?id='+'${nodeId!}';
							$('.page-content').load(url);
							layer.close(index);
						}
					},
					clearForm : false,
					resetForm : false,
					type : 'post',
					error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
				};
				$("#nodeServerSubmitForm").ajaxSubmit(options);
			},
			end:function(){
				$('#nodeServerEditDiv').empty();
			},
			area: ['600px','520px'],
			content: $('.layer-node-server-edit')
		});
		// $('.chosen-container').width('100%');
	}
</script>