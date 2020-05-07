<form id="myform">
<div class="layer-syncScore divideDiv">
	<div class="filter">
		<div class="filter-item block">
			<span class="filter-name">分班1：</span>
			<div class="filter-content">
				<select class="form-control" id="divideId1" style="width:280px" nullable="false" name="divideId1">
					<option value="">--请选择--</option>
					<#if dividelist?exists && dividelist?size gt 0>
						<#list dividelist as divide>
						<option value="${divide.id!}">${divide.divideName!}</option>
						</#list>
					</#if>
				</select>
			</div>
		</div>
		<div class="filter-item block">
			<span class="filter-name">分班2：</span>
			<div class="filter-content">
				<select class="form-control" id="divideId2" style="width: 280px"  nullable="false" name="divideId2">
					<option value="">--请选择--</option>
					<#if dividelist?exists && dividelist?size gt 0>
						<#list dividelist as divide>
						<option value="${divide.id!}">${divide.divideName!}</option>
						</#list>
					</#if>
				</select>
			</div>
		</div>
		<div class="filter-item block">
			<span class="filter-name">选课：</span>
			<div class="filter-content">
				
				<select class="form-control" id="chooseChoiceId" style="width: 280px" nullable="false" name="chooseChoiceId">
					
					<option value="">--请选择--</option>
					<#if choiceList?exists && choiceList?size gt 0>
						<#list choiceList as choice>
						<option value="${choice.id!}">${choice.choiceName!}</option>
						</#list>
					</#if>
				</select>
				
			</div>
		</div>
		
	</div>
</div>

</form>


<#-- 确定和取消按钮 -->
<div style="text-align: center;">
	<a class="btn btn-blue" id="divide-commit">确定</a>
	<a class="btn btn-white" id="divide-close">取消</a>
</div>
<!-- /.row -->
<!-- page specific plugin scripts -->
<!--[if lte IE 8]>
  <script src="${request.contextPath}/static/ace/js/excanvas.js"></script>
<![endif]-->

<script type="text/javascript">
	

// 取消按钮操作功能
$("#divide-close").on("click", function(){
	doLayerOk("#divide-commit", {
	redirect:function(){},
	window:function(){layer.closeAll()}
	});
 });
 
// 确定按钮操作功能
var isSubmit=false;
$("#divide-commit").on("click", function(){
	if(isSubmit){
		return;
	}

	isSubmit=true;
	$(this).addClass("disabled");
	var check = checkValue('.divideDiv');
	if(!check){
	 	$(this).removeClass("disabled");
	 	isSubmit=false;
	 	return;
	}
	var divideId1=$("#divideId1").val();
	var divideId2=$("#divideId2").val();
	var chooseChoiceId=$("#chooseChoiceId").val();
	if(divideId1==divideId2){
		layer.tips('不能选择跟分班1一样的方案', $("#divideId2"), {
			tipsMore: true,
			tips:3				
		});
		$(this).removeClass("disabled");
	 	isSubmit=false;
	 	return;
	}
	// 提交数据
	var ii = layer.load();
	
	$.ajax({
		url :"${request.contextPath}/newgkelective/${gradeId!}/divide/saveCombine",
		data:{"divideId1":divideId1,"divideId2":divideId2,"choiceId":chooseChoiceId},
		dataType: "json",
		success:function(data) {
			layer.close(ii);
			if(data.success){
				layer.closeAll();
	 			var url1 ="${request.contextPath}/newgkelective/${gradeId!}/goDivide/index/page";
	 			$("#showList").load(url1);
	 		}else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			$("#divide-commit").removeClass("disabled");
	 			isSubmit=false;
			}
			
		},
		type:'post',
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
	
	
	
 });	

</script>

