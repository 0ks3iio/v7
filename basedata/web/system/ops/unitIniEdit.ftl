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
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">开启定时开关机：</label>
			<div class="col-sm-8">
				<label><input type="radio" name="openClose" class="wp" <#if openClose==1>checked</#if> value=1><span class="lbl" >是</span></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<label><input type="radio" name="openClose" class="wp" <#if openClose==0>checked</#if>  value=0><span class="lbl" >否</span></label>
			</div>
		</div>
		<div class="form-group factoryType-group">
			<label class="col-sm-3 control-label no-padding-right">班牌厂家：</label>
			<div class="col-sm-8">
				<select class="form-control" name="factoryType">
						<option value="">请选择</option>
                        <option value="1"  <#if factoryType?default('')=='1'>selected</#if>>华瑞安</option>
                        <option value="2" <#if factoryType?default('')=='2'>selected</#if>>希沃</option>  
                        <option value="3" <#if factoryType?default('')=='3'>selected</#if>>海康一代</option>
                        <option value="4" <#if factoryType?default('')=='4'>selected</#if>>海康二代</option>
                        <option value="5" <#if factoryType?default('')=='5'>selected</#if>>深圳一德</option>
                        <option value="6" <#if factoryType?default('')=='6'>selected</#if>>冠品信</option>
                        <option value="99" <#if factoryType?default('')=='99'>selected</#if>>其他</option>
                 </select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">显示综合评价：</label>
			<div class="col-sm-8">
				<label><input type="radio" name="showzhpj" class="wp" <#if showzhpj==1>checked</#if> value=1><span class="lbl" >是</span></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<label><input type="radio" name="showzhpj" class="wp" <#if showzhpj==0>checked</#if>  value=0><span class="lbl" >否</span></label>
			</div>
		</div>
	</div>
</div>
</form>
<script>
function saveUnitIni(index){
	var factoryType=$(".factoryType-group select").val();
	if(factoryType==""){
		layerTipMsg(false,"班牌厂家","请选择班牌厂家");
		return;
	}
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