<#if screenDemos?exists && screenDemos?size gt 0>
		<table class="tables">
		    <thead>
		        <tr>
		            <th>名称</th>
					<th>排序号</th>
					<th>状态</th>
		            <th>操作</th>
		        </tr>
		    </thead>
		    <tbody class="kanban-content">
			<#list screenDemos as screenDemo>
				<tr>
					<td style="width: 300px;" title="${screenDemo.name!?html}">
						<div style="width: 150px;" class="ellipsis">
							${screenDemo.name!?html}
						</div>
					</td>
					<td>${screenDemo.orderId!}</td>
					<td><#if screenDemo.status! == 1>启用
							<#else >停用
						</#if></td>
					<td>
						<a href="javascript:void(0)" onclick="changeStatus('${screenDemo.id!}',${screenDemo.status!})"  class="look-over"><#if screenDemo.status ==1>停用<#else>启用</#if></a><span class="tables-line">|</span>
						<a href="javascript:void(0)" onclick="editScreenDemo('${screenDemo.id!}','${screenDemo.orderId}')"  class="look-over">编辑</a>
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
<div class="layer layer-editParam layui-layer-wrap" style="display: none;">
	<div class="layer-content">
		<div class="form-horizontal">
			<div class="form-group hide">
				<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>id：</label>
				<div class="col-sm-8">
					<input type="text" id="screenDemoId" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>排序号：</label>
				<div class="col-sm-8">
					<input type="text" id="screenDemoOrderId" class="form-control" maxlength="3" oninput = "value=value.replace(/[^\d]/g,'')">
				</div>
			</div>
		</div>
	</div>
</div>
<script>

	function changeStatus(id,status){
		if(status==1)
			status=0;
		else
			status=1;
		$.ajax({
			url:"${request.contextPath}/bigdata/cockpit/changeStatus",
			data:{
				'id':id,
				'status':status
			},
			type:"post",
			clearForm : false,
			resetForm : false,
			dataType: "json",
			success:function(data){
				layer.closeAll();
				if(!data.success){
					showLayerTips4Confirm('error',data.message);
				}else{
					if (status==0){
						showLayerTips('success',"停用成功",'t');
					}else {
						showLayerTips('success',"启用成功",'t');
					}
					$('.page-content').load('${request.contextPath}/bigdata/cockpit/classic');
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}

	function editScreenDemo(id,orderId) {
		$("#screenDemoId").val(id);
		$("#screenDemoOrderId").val(orderId);
		layer.open({
			type: 1,
			shade: 0.5,
			title: '修改排序号',
			area: '500px',
			btn: ['确定', '取消'],
			yes:function(index, layero){

				if ($('#screenDemoOrderId').val() == "") {
					layer.tips("不能为空", "#screenDemoOrderId", {
						tipsMore: true,
						tips: 3
					});
					return;
				}

				var params = {
					id:$("#screenDemoId").val(),
					orderId:$("#screenDemoOrderId").val()
				};

				$.ajax({
					url: '${request.contextPath}/bigdata/cockpit/editScreenDemoOrderId',
					type: 'POST',
					data: params,
					success: function (result) {
						if (!result.success) {
							showLayerTips4Confirm('error',result.message);
						} else {
							showLayerTips('success','修改成功!','t');
							layer.close(index);
							$('.page-content').load('${request.contextPath}/bigdata/cockpit/classic');
						}
					}
				});
			},
			content: $('.layer-editParam')
		})
	}
</script>