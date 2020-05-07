<form id="submitForm">
<div class="layer layer-add" style="display:block;">
	<div class="layer-content">
		<div class="form-horizontal">
			<input type="hidden" name="id" value="${item.id!}">
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>排序号</label>
				<div class="col-sm-8">
					<input type="text" name="orderId" vtype="int" min="1" class="form-control" max="99" nullable="false"  maxlength="2" id="orderId" value="${item.orderId!}">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>指标名称</label>
				<div class="col-sm-8">
					<input type="text" name="itemName" class="form-control" nullable="false"  maxlength="50" id="itemName" value="${item.itemName!}">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">指标单位</label>
				<div class="col-sm-8">
					<input type="text" name="itemUnit"  class="form-control"  maxlength="15" id="itemUnit" value="${item.itemUnit!}">
				</div>
			</div>
		</div>
	</div>
</div>
</form>
<div class="layer-footer">
    <a href="javascript:" class="btn btn-lightblue" id="result-commit" onclick="doSaveItem()">确定</a>
    <a href="javascript:" class="btn btn-lightblue" id="result-close">取消</a>
</div>
<script>
	$(function(){
		$("#result-close").on("click", function(){
		    layer.closeAll();
		 });
	});
	var isSubmit=false;
	function doSaveItem(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var check = checkValue('#submitForm');
		if(!check){
		 	isSubmit=false;
		 	return;
		}
		var ii = layer.load();
		var options = {
			url : "${request.contextPath}/stuwork/health/item/save",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			isSubmit = false;
		 		}else{
		 			layer.closeAll();
					layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
				  	itemShowList(1);
    			}
    			layer.close(ii);
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#submitForm").ajaxSubmit(options);
	}
</script>