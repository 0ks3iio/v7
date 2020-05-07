<form id="myform">
<div class="layer-syncScore roundsDetail">
	<div class="filter">
		<div class="filter-item block">
			<span class="filter-name">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;选课轮次：</span>
			<div class="filter-content">
				<select class="form-control" id="choiceRounds" style="width: 139px" name="choiceRounds">
					<option value="1">第1轮</option>
					<option value="2">第2轮</option>
				</select>
			</div>
		</div>
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
			<span class="filter-name">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;分班模式：</span>
			<div class="filter-content">
				<select class="form-control" id="roundsType" style="width: 139px" name="roundsType" onchange="selectRoundsType()">
					<option value="1">全走班模式</option>
					<option value="2">全固定模式</option>
					<option value="3">半固定模式</option>
					<option value="4">全手动模式</option>
				</select>
			</div>
		</div>
		<input type="hidden" id="openClassType" name="openClassType" value="0">
		<input type="hidden" id="openTwo" name="openTwo" value="0">
		<input type="hidden" id="openHand" name="openHand" value="0">
		<#--
		<div class="filter-item block">
			<span class="filter-name">是否重组行政班：</span>
			<div class="filter-content">
				<select class="form-control" id="openClassType" style="width: 139px" name="openClassType" onchange="selectOpenType()">
					<option value="1">是</option>
					<option value="0">否</option>
				</select>
			</div>
		</div>
		<div class="filter-item block two_class">
			<span class="filter-name">&nbsp;&nbsp;&nbsp;是否2+X模式：</span>
			<div class="filter-content">
				<select class="form-control" id="openTwo" style="width: 139px" name="openTwo" onchange="selectOpenHand()">
					<option value="0">否</option>
					<option value="1">是</option>
				</select>
			</div>
		</div>
		<div class="filter-item block two_class hand_class">
			<span class="filter-name">&nbsp;&nbsp;&nbsp;是否手动模式：</span>
			<div class="filter-content">
				<select class="form-control" id="openHand" style="width: 139px" name="openHand">
					<option value="0">否</option>
					<option value="1">是</option>
				</select>
			</div>
		</div>
		-->
		<div class="filter-item block myCopy_class" style="display:none">
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
<#--
<em>温馨提示：
<p class="two_class">采用2+X模式：所有学生通过三科固定或2科固定为新行政班，选考科目只走一科，且固定在第一个批次</p>
<#if roundsList?exists && (roundsList?size>0)>
<p>复制手动排班，选择某一轮次，进行复制手动排班结果</p>
</#if>
</em>
-->
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
 <#--
 function selectOpenType(){
 	var openClassType=$("#openClassType").val();
 	if(openClassType=='0'){
 		$(".myCopy_class").hide();
 		$(".two_class").hide();
 	}else{
 		$(".myCopy_class").show();
 		$(".two_class").show();
 	}
 }
  function selectOpenHand(){
 	var openTwo=$("#openTwo").val();
 	if(openTwo=='0'){
 		$(".hand_class").show();
 	}else{
 		$(".hand_class").hide();
 	}
 }
 -->
 function selectRoundsType(){
 	var roundsType=$("#roundsType").val();
 	if(roundsType=='1'){
 		$(".myCopy_class").hide();
 		$("#openClassType").val("0");
 		$("#openTwo").val("0");
 		$("#openHand").val("0");
 	}else if(roundsType=='2'){
 		$(".myCopy_class").show();
 		$("#openClassType").val("1");
 		$("#openTwo").val("1");
 		$("#openHand").val("0");
 	}else if(roundsType=='3'){
 		$(".myCopy_class").show();
 		$("#openClassType").val("1");
 		$("#openTwo").val("0");
 		$("#openHand").val("0");
 	}else if(roundsType=='4'){
 		$(".myCopy_class").show();
 		$("#openClassType").val("1");
 		$("#openTwo").val("0");
 		$("#openHand").val("1");
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

