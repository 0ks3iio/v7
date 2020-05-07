<div class="layer layer-add">
	<div class="layer-content">
		<div class="form-horizontal" id="checkType">
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" style="width:100px"><font style="color:red;">*</font>寝室楼名称</label>
				<div class="col-sm-9">
					<input type="text" name="name" nullable="false"  class="form-control" maxlength="100" id="buildingName" value="${building.name!}">
					<input type="hidden" name="id" id="buildingId" value="${building.id!}">
				</div>
			</div>
		</div>
	</div>
</div>
<div class="layer-footer">
    <a href="javascript:" class="btn btn-lightblue" id="result-commit" onclick="doSaveBuilding();return false;">确定</a>
    <a href="javascript:" class="btn btn-lightblue" id="result-close">取消</a>
</div>
<script>
	$(function(){
		$(".layer-add").show();
		$("#result-close").on("click", function(){
		    layer.closeAll();
		 });
	});
	var isSubmit=false;
	function doSaveBuilding(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var check = checkValue('#checkType');
		if(!check){
		 	isSubmit=false;
		 	return;
		}
		var id=$("#buildingId").val();
		var name=$("#buildingName").val();
		var ii = layer.load();
		$.ajax({
			url:'${request.contextPath}/stuwork/dorm/building/save',
			data: {'id':id,'name':name},
			type:'post',
			success:function(data) {
				layer.closeAll();
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			layerTipMsg(jsonO.success,"成功",jsonO.msg);
			  		itemShowList(1);
		 		}else{
		 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
				layer.close(ii);
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
</script>