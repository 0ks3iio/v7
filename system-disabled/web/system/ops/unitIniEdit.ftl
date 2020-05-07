<form id="submitForm">
<input type="hidden" name="unitId" value="${unitId!}">
<div class="layer-content">
	<div class="form-horizontal">
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">班牌版本：</label>
			<div class="col-sm-8">
				<label><input type="radio" name="useVersion" class="wp" <#if useVersion==1>checked</#if> value=1><span class="lbl" >标准版</span></label>
				<label><input type="radio" name="useVersion" class="wp" <#if useVersion==2>checked</#if>  value=2><span class="lbl" >德育版</span></label>
			</div>
		</div>
	</div>
</div>
</form>
<script>
function saveUnitIni(index){
	var options = {
			url : "${request.contextPath}/system/ops/sysOption/unitIni/save",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			isSubmit = false;
		 		}else{
		 			layer.closeAll();
					layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
    			}
    			layer.close(index);
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#submitForm").ajaxSubmit(options);
}
</script>