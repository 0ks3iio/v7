<form id="myform">
<input type="hidden" name="subjectIds" value="${newGkDivideClass.subjectIds!}" />
<input type="hidden" name="stuIdStr" id="stuIdStr" value="" />
<input type="hidden" name="classType" id="classType" value="2" />
<div class="row schedulingDetail filter" style="height:113px">	
	<div class="filter-item block">
		<span class="filter-name">班级名称：</span>
		<div class="filter-content">
			   <input type="text" class="form-control" nullable="false"  name="className" id="className" value="${newGkDivideClass.className!}" maxlength="20"> 
		</div>
	</div>
</div>
</form>
<#-- 确定和取消按钮 -->
<div class="layui-layer-btn">
	<a class="layui-layer-btn0" id="scheduling-commit">确定</a>
	<a class="layui-layer-btn1" id="scheduling-close">取消</a>
</div>
<!-- /.row -->
<!-- page specific plugin scripts -->
<!--[if lte IE 8]>
  <script src="${request.contextPath}/static/ace/js/excanvas.js"></script>
<![endif]-->

<script type="text/javascript">
$(function(){
	if(chooseStuIdsToAdd && chooseStuIdsToAdd!=""){
		$("#stuIdStr").val(chooseStuIdsToAdd);
		chooseStuIdsToAdd="";
	}
})
// 取消按钮操作功能
$("#scheduling-close").on("click", function(){
	doLayerOk("#scheduling-commit", {
	redirect:function(){},
	window:function(){layer.closeAll()}
	});
 });
// 确定按钮操作功能
var isSubmit=false;
$("#scheduling-commit").on("click", function(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	$(this).addClass("disabled");
	var check = checkValue('.schedulingDetail');
	if(!check){
	 	$(this).removeClass("disabled");
	 	isSubmit=false;
	 	return;
	}
	// 提交数据
	var options = {
		url : '${request.contextPath}/newgkelective/${divideId!}/divideGroup/saveClass',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
	 			layer.msg("保存成功！", {
					offset: 't',
					time: 2000
				});
				
				var url =  '${request.contextPath}/newgkelective/${divideId!}/divideGroup/jzbGroupDetail/page?subjectId=${newGkDivideClass.subjectIds!}';
				$("#showList").load(url);
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			$("#scheduling-commit").removeClass("disabled");
	 			isSubmit=false;
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){} 
	};
	$("#myform").ajaxSubmit(options);
 });	

</script>

