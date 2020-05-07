<form id="submitForm">
<input type="hidden" name="studentId" id="studentId" value="${studentId!}">
<input type="hidden" name="id" id="id" value="${eva.id!}">
<input type="hidden" id="acadyear" name="acadyear" value="${acadyear!}">
<input type="hidden" id="semester" name="semester" value="${semester!}">
<div class="layer layer-add">
	<div class="layer-content">
		<div class="form-horizontal">
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">姓名：</label>
				<div class="col-sm-8">
					<p class="form-group-txt">${studentName!}</p>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>操行等第：</label>
				<div class="col-sm-8">
					<select name="gradeId" id="gradeId" class="form-control" nullable="false"  style="width:120px">
						<option value="">--请选择--</option>
                    	<#if boptionList?exists && boptionList?size gt 0>
                    		<#list boptionList as item>
		                        <option value="${item.id!}" <#if item.id==eva.gradeId?default("")>selected="selected"</#if>>${item.optionName!}</option>
                    		</#list>
                    	</#if>
                    </select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">期末评语：</label>
				<div class="col-sm-8">
					<textarea name="remark" id="remark" rows="4"   maxlength="1000" class="form-control">${eva.remark!}</textarea>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">社团工作：</label>
				<div class="col-sm-8">
					<textarea name="association" id="association" rows="4"  maxlength="1000" class="form-control">${eva.association!}</textarea>
				</div>
			</div>
		</div>
	</div>
</div>
</form>
<div class="layer-footer">
    <a href="javascript:" class="btn btn-lightblue" id="result-commit" onclick="doSaveEva()">确定</a>
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
	function doSaveEva(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var check = checkValue('#submitForm');
		if(!check){
		 	isSubmit=false;
		 	return;
		}
		var studentId=$("#studentId").val();
		var id=$("#id").val();
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var options = {
			url : "${request.contextPath}/stuwork/evaluation/stu/doSaveEva?"+searchUrlValue("#submitForm")+"&studentId="+studentId+"&id="+id+"&semester="+semester+"&acadyear="+acadyear,
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
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#submitForm").ajaxSubmit(options);
	}
	var isSubmit=false;
	function doSaveEva(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var check = checkValue('#submitForm');
		if(!check){
		 	isSubmit=false;
		 	return;
		}
		var options = {
			url : "${request.contextPath}/stuwork/evaluation/stu/doSaveEva",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			isSubmit = false;
		 		}else{
		 			layer.closeAll();
					layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
				  	doSearch();
    			}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#submitForm").ajaxSubmit(options);
	}
</script>