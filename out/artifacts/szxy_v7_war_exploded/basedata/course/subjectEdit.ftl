<div class="layer-content">
	<div class="tab-content">
		<div class="form-horizontal">
			<form id="subjectForm">
				<input type="hidden" name="id" value="${subject.id!}"/>
				<div class="form-group">
					<label for="" class="control-label no-padding-right col-sm-3"><em>*</em>学科编号：</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" name="code" value="${subject.code!}" <#if subject.id?default('')!=''>readOnly</#if>>
					</div>
				</div>
				<div class="form-group">
					<label for="" class="control-label no-padding-right col-sm-3"><em>*</em>学科名称：</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" name="name" value="${subject.name!}">
					</div>
				</div>
				
			</form>
		</div>
		<div class="layer-footer" style="vertical-align: middle">
			<button class="btn btn-lightblue" id="subject-commit">确定</button>
			<button class="btn btn-grey" id="subject-close">取消</button>
		</div>
	</div>
</div>
<script>
$(function(){
	// 取消按钮操作功能
	$("#subject-close").on("click", function(){
	    doLayerOk("#subject-commit", {
	    redirect:function(){},
	    window:function(){layer.closeAll()}
	    });     
	 });
	 var isSubmit=false;
	 
	 // 确定按钮操作功能
	$("#subject-commit").on("click", function(){	
		if(isSubmit){
			return;
		}
		isSubmit=true;
		if(checkBlank()){
			isSubmit=false;
			return;
		}
		var options = {
			url : '${request.contextPath}/basedata/subject/save',
			dataType : 'json',
			success : function(data){
		 		if(data.success){
					layer.closeAll();
		 			layer.msg(data.msg, {offset: 't',time: 2000});
					refreshPage();
		 		}
		 		else{
		 			layerTipMsg(data.success,"失败",data.msg);
					isSubmit=false;
				}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#subjectForm").ajaxSubmit(options);
	});
})

function checkBlank(){
	var code = $('[name="code"]').val();
	var name = $('[name="name"]').val();
	
	var array = new Array(code,name);
	for(var i=0;i<array.length;i++){
		if(array[i]!=undefined && array[i] == ""){
			layerTipMsg(false,"失败",'带 * 为必填项');
			return true;
		}
	}
	if(code!=undefined && code.length > 50){
		layerTipMsg(false,"失败",'学科编号最长50位');
		return true;
	}
	var codeFormat = /^[0-9a-zA-Z]+$/g;
	if(!codeFormat.test(code)){
		layerTipMsg(false,"失败",'学科编号只支持数字、字母');
		return true;
	}
	if(name.length > 50){
		layerTipMsg(false,"失败",'学科名称最长50个汉字');
		return true;
	}
}
</script>