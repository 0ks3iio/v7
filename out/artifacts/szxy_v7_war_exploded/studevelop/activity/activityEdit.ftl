<title>活动编辑</title>
<div class="layer-content">
<form id="actEditForm" name="actEditForm" method="post">
<input type="hidden" name="id" value="${act.id!}">
<input type="hidden" name="acadyear" value="${act.acadyear!}">
<input type="hidden" name="semester" value="${act.semester!}">
<input type="hidden" name="unitId" value="${act.unitId!}">
<input type="hidden" name="rangeId" value="${act.rangeId!}">
<input type="hidden" name="rangeType" value="${act.rangeType!}">
<input type="hidden" name="actType" value="${act.actType!}">
<input type="hidden" name="newEn" value="${act.newEn?default(true)?string('true','false')}">
	<div id="formDiv">
		<div class="form-group">
	    	<label class="control-label">活动名称</label>
	    	<input type="text" name="actTheme" id="actTheme" class="form-control" nullable="false" maxLength="50" value="${act.actTheme!}">
		</div>
		<div class="form-group">
	    	<label class="control-label">活动说明</label>
    		<div class="textarea-container">
    			<textarea name="actRemark" id="actRemark" cols="30" rows="10" maxLength="400" class="form-control">${act.actRemark!}</textarea>
    			<span>200</span>
    		</div>
		</div>
	</div>
</form>
<div class="layer-footer">
	<button class="btn btn-lightblue" id="act-commit">确定</button>
	<button class="btn btn-grey" id="act-close">取消</button>
</div>	
</div>
<script>
// 取消按钮操作功能
$("#act-close").on("click", function(){
	doLayerOk("#act-commit", {
		redirect:function(){},
		window:function(){layer.closeAll()}
	});		
 });
// 确定按钮操作功能
var isSubmit=false;
$("#act-commit").on("click", function(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	
	var check = checkValue('#formDiv');
	if(!check){
	 	isSubmit=false;
	 	return;
	}
	// 提交数据
	var ii = layer.load();
	var options = {
		url : '${request.contextPath}/studevelop/activity/${actType!}/saveAct',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
				layerTipMsg(data.success,"成功",data.msg);
			  	refreshPage();
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			isSubmit=false;
			}
			layer.close(ii);
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){
			
			layer.close(ii);
			isSubmit=false;
		} 
	};
	$("#actEditForm").ajaxSubmit(options);
 });
</script>