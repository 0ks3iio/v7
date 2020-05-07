<link rel="stylesheet" href="${request.contextPath}/static/components/datatables/media/css/jquery.dataTables.min.css">
<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<form id="classForm">
<div class="layer layer-addTopClass" id="layerAdd">
	<div class="layer-content">
		<div class="explain">
			<p>说明：新增后可查看与调整具体学生</p>
		</div>
		<input type="hidden" name="classType" id="classType" value="${classType!}">
		<#if classType?default("")=="1">
		<div class="filter">
			<div class="filter-item block">
				<span class="filter-name">最多开班：</span>
				<div class="filter-content">
					<p>${maxClassNum!}</p>
				</div>
			</div>
			<div class="filter-item block">
				<span class="filter-name">班级数：</span>
				<div class="filter-content">
					<input type="text" class="form-control" vtype="int" min="1" max="${maxClassNum!}" nullable="false" id="classNum" name="classNum">
				</div>
			</div>
		</div>
		<#elseif classType?default("")=="0">
		<div style="max-height:400px;overflow:auto;">
		<table class="table table-bordered table-striped table-hover table-groupClass">
			<thead>
				<tr>
					<th>组合名称</th>
					<th>选课人数</th>
					<th>开设班级数</th>
				</tr>
			</thead>
			<tbody>
				<#if newDtoList?exists && newDtoList?size gt 0>
				<#list newDtoList as item>
					<tr>
						<td>${item.subShortNames!}</td>
						<td>${item.sumNum!}</td>
						<td><input type="text" id="classNum${item_index}"  vtype="int" min="1" max="${item.classNum!}" name="newDtoList[${item_index}].classNum" class="form-control" placeholder="最多开班${item.classNum!}"></td>
						<input type="hidden" name="newDtoList[${item_index}].subjectIdstr" value="${item.subjectIdstr!}">
						<input type="hidden" name="newDtoList[${item_index}].subShortNames" value="${item.subShortNames!}">
					</tr>
				</#list>
				</#if>
			</tbody>
		</table>
		</div>
		</#if>
	</div>
</div>
</form>
<div class="layer-footer">
    <a href="javascript:" class="btn btn-lightblue" onclick="saveClass('${divideId!}');">确定</a>
    <a href="javascript:" class="btn btn-grey" id="result-close">取消</a>
</div>
<script>
	$(function(){
		$("#layerAdd").show();
		// 取消按钮操作功能
		$("#result-close").on("click", function(){
		    layer.closeAll();
		 });
	})
	var isSubmit=false;
	function saveClass(divideId){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var check = checkValue('#classForm');
		if(!check){
		 	isSubmit=false;
		 	return;
		}
		var ii = layer.load();
		var options = {
			url : "${request.contextPath}/newgkelective/"+divideId+"/divideClass/saveClass",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			isSubmit = false;
		 		}else{
		 			layer.closeAll();
					//layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
					var classType=$("#classType").val();
					var divideClassId=jsonO.msg;
					$("#showList").load("${request.contextPath}/newgkelective/"+divideId+"/divideClass/toStudentIndex?classType="+classType+"&divideClassId="+divideClassId);
    			}
    			layer.close(ii);
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#classForm").ajaxSubmit(options);
	}
</script>