<div class="layer layer-add" style="display:block;" style="display:block;">
	<div class="layer-content">
		<div class="form-horizontal">
			<form id="checkType">
				<div class="form-group">
					<input type="hidden" name="examId" value="${examId!}">
					<input type="hidden" name="statObjectId" value="${statObjectId!}">
					<#if courses?exists && courses?size gt 0>
					<#list courses as item>
						<label class="col-sm-3 control-label no-padding-right" style="width:100px">${item.subjectName!}</label>
						<div class="col-sm-9">
							<input type="text" name="abiList[${item_index}].weights" nullable="false"  class="form-control" maxlength="100" id="abiList${item_index}" value="<#if abiMap[item.id]?exists>${abiMap[item.id].weights!}<#else>1</#if>">
							<input type="hidden" name="abiList[${item_index}].subjectId"    value="${item.id!}">
						</div>
					</#list>
					</#if>
				</div>
			</form>
		</div>
	</div>
</div>
<div class="layer-footer">
    <a href="javascript:" class="btn btn-blue" id="result-commit" onclick="doSaveAbi();return false;">保存并计算</a>
    <a href="javascript:" class="btn btn-white" id="result-close">取消</a>
</div>
<script>
	$(function(){
		 $("#result-close").on("click", function(){
		    doLayerOk("#arrange-commit", {
		    redirect:function(){},
		    window:function(){layer.closeAll()}
		    });     
		 });
	});
	var isSubmit=false;
	function doSaveAbi(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var check = checkValue('#checkType');
		if(!check){
		 	isSubmit=false;
		 	return;
		}
		var ii = layer.load();
		var options = {
            url:'${request.contextPath}/examanalysis/examNewGrade/abiSave',
            dataType : 'json',
            type : 'post',
            success : function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"失败",jsonO.msg);
                    isSubmit = false;
                }else{
                    layerTipMsg(jsonO.success,"成功",jsonO.msg);
                    isSubmit = false;
                }
                layer.close(ii);
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $('#checkType').ajaxSubmit(options);
	}
</script>