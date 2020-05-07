<form id="myItem">
	<div class="form-horizontal itemDetail">
		
		
		<div class="filter-item">
			<div class="filter-content">
				<div class="input-group " style="width: 600px;">
					<label class="col-sm-2 control-label no-padding-right" for="itemName">类型：</label>
			        <div class="pos-rel pull-left">
			        	<input id="rewardClasses" nullable='false' maxlength="25" type="text" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入奖励类型">
			        </div>
				    <select name="" id="rewardClassesSelect" class="form-control" onchange="flashSelect(1)" style="width: 200px;"  disabled="disabled">
			        	<option>没有历史类型</option>
			        </select>
				    <div class="input-group-btn">
				    	<button type="button" class="btn btn-default"  style="
						    padding-left: 1px;
						    padding-right: 10px;
						    padding-bottom: 0px;
						    padding-top: 0px;
						    border-bottom-width: 0px;
						    border-top-width: 0px;
						">
					    </button>
				    </div>
				</div>
			</div>
		</div>
		
		<div class="filter-item">
			<div class="filter-content">
				<div class="input-group " style="width: 600px;">
					<label class="col-sm-2 control-label no-padding-right" for="itemName">名称：</label>
			        <div class="pos-rel pull-left">
			        	<input id="projectName" nullable="false" maxlength="25" type="text" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入奖励名称">
			        </div>
				    <select name="" id="projectNameSelect" class="form-control" onchange="flashSelect(2)" style="width: 200px;"  disabled="disabled"> 
			        	<option>没有历史名称</option>
			        </select>
				    <div class="input-group-btn">
				    	<button type="button" class="btn btn-default"  style="
						    padding-left: 1px;
						    padding-right: 10px;
						    padding-bottom: 0px;
						    padding-top: 0px;
						    border-bottom-width: 0px;
						    border-top-width: 0px;
						">
					    </button>
				    </div>
				</div>
			</div>
		</div>
		
		<div class="filter-item">
			<div class="filter-content">
				<div class="input-group " style="width: 600px;">
					<label class="col-sm-2 control-label no-padding-right" for="itemName">级别：</label>
			        <div class="pos-rel pull-left">
			        	<input id="rewardGrade" nullable="false" maxlength="25" type="text" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入奖励级别">
			        </div>
				    <select name="" id="rewardGradeSelect" class="form-control" onchange="flashSelect(3)" style="width: 200px;"  disabled="disabled">
				    	<option>没有历史级别</option>
			        </select>
				    <div class="input-group-btn">
				    	<button type="button" class="btn btn-default"  style="
						    padding-left: 1px;
						    padding-right: 10px;
						    padding-bottom: 0px;
						    padding-top: 0px;
						    border-bottom-width: 0px;
						    border-top-width: 0px;
						">
					    </button>
				    </div>
				</div>
			</div>
		</div>
		
		<div class="filter-item">
			<div class="filter-content">
				<div class="input-group " style="width: 600px;">
					<label class="col-sm-2 control-label no-padding-right" for="itemName">奖级：</label>
			        <div class="pos-rel pull-left">
			        	<input id="rewardLevel" nullable="false" maxlength="25" type="text" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入奖励奖级">
			        </div>
				    
				</div>
			</div>
		</div>
		
		
	</div>
</form>
<#-- 确定和取消按钮 -->
<div class="layer-footer">
	<button class="btn btn-lightblue" id="item-commit">确定</button>
	<button class="btn btn-grey" id="item-close">取消</button>
</div>
<!-- /.row -->
<!-- page specific plugin scripts -->
<!--[if lte IE 8]>
  <script src="${request.contextPath}/static/ace/js/excanvas.js"></script>
<![endif]-->

<script type="text/javascript">


var hisobj = ${hisObj!};
//var hisobj={};
//if($.isEmptyObject(hisobj)){
//	var hisobj ={'没有历史类型':{'没有历史名称':{'没有历史级别':'没有历史级别'}}}
//}
// 取消按钮操作功能
$("#item-close").on("click", function(){
	doLayerOk("#item-commit", {
	redirect:function(){},
	window:function(){layer.closeAll()}
	});
 });
 
//<option>没有历史名称</option>
 // 	rewardClassesSelect
// projectNameSelect
// rewardGradeSelect
 
 	$(function(){
 		if($.isEmptyObject(hisobj)){
 			
	     }else{
	     	$("#rewardClassesSelect").attr("disabled",false);
	     	$("#projectNameSelect").attr("disabled",false);
	     	$("#rewardGradeSelect").attr("disabled",false);
	     	flashSelect(0);
	     }
		 <#if rewardClasses?default('')!=''>
		 	$('#rewardClasses').val('${rewardClasses?default('')}');
		 </#if>
		 <#if projectName?default('')!=''>
		 	$('#projectName').val('${projectName?default('')}');
		 </#if>
		 <#if rewardGrade?default('')!=''>
		 	$('#rewardGrade').val('${rewardGrade?default('')}');
		 </#if>
		 <#if rewardLevel?default('')!=''>
		 	$('#rewardLevel').val('${rewardLevel?default('')}');
		 </#if>
	});
 
 function flashSelect(type){
	var html ="";
 	if(type==0){
 		for(var key in hisobj){
			html +='<option value="'+key+'">'+key+'</option>';
			 $("#rewardClassesSelect").html(html);
		}
		flashSelect(1);
		var name = $("#rewardClassesSelect").val();
		$("#rewardClasses").val(name);
 	}else if(type==1){
 		var name = $("#rewardClassesSelect").val();	
 		for(var key in hisobj[name]){
 			html +='<option value="'+key+'">'+key+'</option>';
 			 $("#projectNameSelect").html(html);
 		}
 		flashSelect(2);
 		var name = $("#rewardClassesSelect").val();
		$("#rewardClasses").val(name);
 		
 	}else if(type==2){
 		var name = $("#rewardClassesSelect").val();	
 		var name2 = $("#projectNameSelect").val();	
 		for(var key in hisobj[name][name2]){
 			html +='<option value="'+key+'">'+key+'</option>';
 			 $("#rewardGradeSelect").html(html);
 		}
 		flashSelect(3);
 		var name2 = $("#projectNameSelect").val();	
 		$("#projectName").val(name2);
 	}else if(type==3){
 		var name3 = $("#rewardGradeSelect").val();	
 		$("#rewardGrade").val(name3);
 	}
 }
 
 
 
 
 function hasScore(hasTotal){
 	
 	if(hasTotal == 0){
 		$('#totalScore').attr("disabled","disabled");
 		$('#totalScore').attr("value",'0');
 	}else{
 		$('#totalScore').removeAttr("disabled");
 	}
 }
 
// 确定按钮操作功能
var isSubmit=false;
$("#item-commit").on("click", function(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	$(this).addClass("disabled");
	var check = checkValue('.itemDetail');
	if(!check){
	 	$(this).removeClass("disabled");
	 	isSubmit=false;
	 	return;
	}
	// 提交数据
	$.ajax({
		url:"${request.contextPath}/stuwork/studentReward/studentRewardGameSave",
		data: { 'rewardClasses':$('#rewardClasses').val(),'projectName':$('#projectName').val(),'rewardGrade':$('#rewardGrade').val(),'rewardLevel':$('#rewardLevel').val(),'projectId':'${projectId?default('')}','settingId':'${settingId?default('')}'},  
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
				if(jsonO.success){
					layer.closeAll();
					layerTipMsg(jsonO.success,"成功",jsonO.msg);
					$(".model-div").load("${request.contextPath}/stuwork/studentReward/studentRewardSettingPage?classesType=1");
				}else{
					isSubmit = false;
					layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {
 			
		}
	});
 });	
</script>