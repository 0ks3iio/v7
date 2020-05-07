<form id="myform">
<div class="layer-syncScore roundsDetail">
	<div class="filter">
		<div class="filter-item block">
			<span class="filter-name">是否开设学考班：</span>
			<div class="filter-content">
				<select class="form-control" id="openClass" style="width: 139px" name="openClass">
					<option value="1">是</option>
					<option value="0">否</option>
				</select>
			</div>
		</div>
		<div class="filter-item block">
			<span class="filter-name">是否重组行政班：</span>
			<div class="filter-content">
				<select class="form-control" id="openClassType" style="width: 139px" name="openClassType" onchange="selectOpenType()">
					<option value="1">是</option>
					<option value="0">否</option>
				</select>
			</div>
		</div>
		<div class="filter-item block myCopy_class">
		<#if roundsList?exists && (roundsList?size>0)>
			<span class="filter-name">&nbsp;&nbsp;&nbsp;复制手动排班：</span>
			<div class="filter-content">
				<select class="form-control" id="copyRoundIds" style="width: 139px" name="copyRoundIds">
					<option value="">不选择复制</option>
						<#list roundsList as item>
							<option value="${item.id}">第${item.orderId}轮</option>
						</#list>
				</select>
			</div>
			</#if>
		</div>
		
	</div>
</div>
</form>
<#if roundsList?exists && (roundsList?size>0)>
<em>温馨提示：复制手动排班，选择某一轮次，进行复制手动排班结果</em>
</#if>
<#-- 确定和取消按钮 -->
<div class="layer-footer">
	<button class="btn btn-lightblue" id="rounds-commit">确定</button>
	<button class="btn btn-grey" id="rounds-close">取消</button>
</div>
<!-- /.row -->
<!-- page specific plugin scripts -->
<!--[if lte IE 8]>
  <script src="${request.contextPath}/static/ace/js/excanvas.js"></script>
<![endif]-->

<script type="text/javascript">
	$(function(){
		//初始化日期控件
		var viewContent={
			'format' : 'yyyy-mm-dd',
			'minView' : '2',
			'startDate' : '${startDate!}',
			'endDate' : '${endDate!}',
		};
		initCalendarData("#myform",".date-picker",viewContent);
	});

// 取消按钮操作功能
$("#rounds-close").on("click", function(){
	doLayerOk("#rounds-commit", {
	redirect:function(){},
	window:function(){layer.closeAll()}
	});
 });
 function selectOpenType(){
 	var openClassType=$("#openClassType").val();
 	if(openClassType=='0'){
 		$(".myCopy_class").hide();
 	}else{
 		$(".myCopy_class").show();
 	}
 }
// 确定按钮操作功能
var isSubmit=false;
$("#rounds-commit").on("click", function(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	$(this).addClass("disabled");
	var check = checkValue('.roundsDetail');
	if(!check){
	 	$(this).removeClass("disabled");
	 	isSubmit=false;
	 	return;
	}
	// 提交数据
	var ii = layer.load();
	var options = {
		url : '${request.contextPath}/gkelective/${arrangeId}/arrangeRounds/save',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
				layerTipMsg(data.success,"成功",data.msg);
			  	doEnterSetp('${arrangeId}',4);
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			$("#rounds-commit").removeClass("disabled");
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

