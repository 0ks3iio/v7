<form id="myform">
<div class="row arrangeDetail filter">	
		<div class="filter-item block">
			<span class="filter-name">年级：</span>
			<div class="filter-content">
				<select class="form-control" name="gradeId" id="gradeId" type="text" nullable="false">
				<#if gradeList?? && (gradeList?size>0)>
					<#list gradeList as item>
						<option value="${item.id}">${item.gradeName!}</option>
					</#list>
				<#else>
					<option value="">暂无数据</option>
				</#if>
				</select>
			</div>
		</div>
		
</div>
</form>
<#-- 确定和取消按钮 -->
<div class="layer-footer">
	<button class="btn btn-lightblue" id="arrange-commit">确定</button>
	<button class="btn btn-grey" id="arrange-close">取消</button>
</div>
<!-- /.row -->



<!-- page specific plugin scripts -->

<!--[if lte IE 8]>
  <script src="${request.contextPath}/static/ace/js/excanvas.js"></script>
<![endif]-->

<script type="text/javascript">

// 取消按钮操作功能
$("#arrange-close").on("click", function(){
	doLayerOk("#arrange-commit", {
	redirect:function(){},
	window:function(){layer.closeAll()}
	});		
 });
// 确定按钮操作功能
var isSubmit=false;
$("#arrange-commit").on("click", function(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	$(this).addClass("disabled");
	var check = checkValue('.arrangeDetail');
	if(!check){
	 	$(this).removeClass("disabled");
	 	isSubmit=false;
	 	return;
	}
	// 提交数据
	var ii = layer.load();
	var options = {
		url : '${request.contextPath}/gkelective/arrange/save',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
				layerTipMsg(data.success,"成功",data.msg);
			  	showList();
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			$("#arrange-commit").removeClass("disabled");
	 			isSubmit=false;
			}
			layer.close(ii);
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){} 
	};
	$("#myform").ajaxSubmit(options);
 });	

</script>

