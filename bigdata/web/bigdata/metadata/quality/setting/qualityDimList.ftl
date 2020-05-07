<table class="tables">
	<thead>
		<tr>
			<th >维度名称</th>
			<th>权重</th>
			<th >操作</th>
		</tr>
	</thead>
	<tbody>
		<#if  dimList?exists &&dimList?size gt 0>
          	<#list dimList as dim>
				<tr>
					<td>${dim.name!}</td>
					<td>${dim.weight!}</td>
					<td><a class="look-over" href="javascript:void(0);" onclick="editQualityDim('${dim.id!}')">
					编辑</a>
					</td>
				</tr>
      	    </#list>
        </#if>
	</tbody>
</table>
<div class="layer layer-quality-dim-edit" id="qualityDimEditDiv">
</div>
<script>
	function editQualityDim(id) {
    	    $.ajax({
	            url: '${request.contextPath}/bigdata/setting/quality/dimEdit',
	            type: 'POST',
	            data: {id:id},
	            dataType: 'html',
	            beforeSend: function(){
			      	$('#qualityDimEditDiv').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
			    },
	            success: function (response) {
	                $('#qualityDimEditDiv').html(response);
	            }
            });
            var dimSubmit = false;
            layer.open({
            	type: 1,
            	shade: .6,
            	title: '质量维度编辑',
            	btn: ['保存','取消'],
            	yes:function(index, layero){
               		if(dimSubmit){
               			showLayerTips('warn','数据保存中,请不要重复点击','t');
						return;
					}
			  		dimSubmit = true;
   		       		if($('#weight').val()== '') {
                    	layer.tips("权重不能为空!", "#weight", {
	                        tipsMore: true,
	                        tips: 3
	                    });
                    	dimSubmit = false;
                    	return;
                	}

					var  options = {
					url : "${request.contextPath}/bigdata/setting/quality/saveDim",
					dataType : 'json',
					success : function(data){
					 	if(!data.success){
					 		showLayerTips4Confirm('error',data.message);
					 		dimSubmit = false;
					 	}else{
					 		showLayerTips('success',"保存成功",'t');
					 		showList('dim');
					 		layer.close(index);
			    		}
					},
					clearForm : false,
					resetForm : false,
					type : 'post',
					error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
				};
				$("#qualityDimSubmitForm").ajaxSubmit(options);
            },
            end:function(){
               	$('#qualityDimEditDiv').empty();
             },
            area: '600px',
            content: $('.layer-quality-dim-edit')
        });
	}
</script>