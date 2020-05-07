<div class="table-show">
    <div class="filter-made mb-10">
		<div class="filter-item filter-item-right clearfix">
	        <button class="btn btn-lightblue" onclick="addFlinkJob();">新增</button>
		</div>
	</div>
	<table class="tables tables-border no-margin">
		<thead>
			<tr>
				<th>Job名称</th>
				<th>操作</th>	
			</tr>
		</thead>
		<tbody>
		<#if flinkList?exists&&flinkList?size gt 0>
			<#list flinkList as flink>
			<tr>
				<td>${flink.name!}</td>
				<td>
					<#if !submitFlag?seq_contains(flink.jobCode)&&!redisFlag?seq_contains(flink.id)>
						<a href="javascript:;" class="look-over" onclick="execFlinkJob('${flink.id!}','${flink.hasParam?default(0)}')">提交</a><span class="tables-line">|</span>
					</#if>
					<a class="look-over" href="javascript:void(0);" onclick="editFlinkJob('${flink.id!}')">编辑</a><span class="tables-line">|</span>
					<a class="remove" href="javascript:void(0);" onclick="deleteFlinkJob('${flink.id!}','${flink.name!}')">删除</a>
				</td>
			</tr>
			 </#list>
	 <#else>
			<tr >
				<td  colspan="2" align="center">
				暂无FlinkStream数据
				</td>
			<tr>
      </#if>
		</tbody>
	</table>
</div>
<script>
	function addFlinkJob(){
		var url =  '${request.contextPath}/bigdata/calculate/flink/edit';
		$("#flink-div").load(url);
	}
	
	function editFlinkJob(id){
		var url =  '${request.contextPath}/bigdata/calculate/flink/edit?id='+id;
		$("#flink-div").load(url);
	}
	
	function execFlinkJob(id,hasParam){
		 $.ajax({
				url:'${request.contextPath}/bigdata/calculate/job/exec',
				data:{
				  'id':id
				},
				type:"post",
				dataType: "json",
				success:function(data){
					if(!data.success){
						showLayerTips('success',data.msg,'t');
					}else{
						showLayerTips('success','JOB提交成功','t');
					}
                    var url =  "${request.contextPath}/bigdata/calculate/realtime/list?calculateType=8";
                    $("#flink-div").load(url);
			  }
		});
	}
    
	function deleteFlinkJob(id,name){
			showConfirmTips('prompt',"提示","您确定要删除"+name+"吗？",function(){
			 $.ajax({
		            url:"${request.contextPath}/bigdata/calculate/flink/delete",
		            data:{
		              'id':id
		            },
		            type:"post",
		            clearForm : false,
					resetForm : false,
		            dataType: "json",
		            success:function(data){
		            	layer.closeAll();
				 		if(!data.success){
	                        showLayerTips4Confirm('error',data.msg);
	                    }else{
	                        showLayerTips('success',data.msg,'t');
                            var url =  "${request.contextPath}/bigdata/calculate/realtime/list?calculateType=8";
                            $("#flink-div").load(url);
	                    }
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
		},function(){
	
		});
	}
</script>