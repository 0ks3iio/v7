<style>
.statistics-container{position: fixed;top: 47px;bottom: 57px;right: 0;z-index: 1000; width: 0;background: #fff;box-shadow: 0px 1px 10px rgba(207,210,212,0.6);}
.statistics{position:absolute;top: calc(50% - 77px);left:-40px;width:40px;padding:4px 10px;border-radius: 8px 0 0 8px;background: #317eeb;color: #fff;text-align: center;cursor: pointer;}

</style>
<div class="stepsContainer no-margin-bottom">
	<ul class="steps-default clearfix">
		<li class="active"><span><i>1</i>分行政班</span></li>
		<li class="active"><span><i>2</i>分选考班</span></li>
		<li <#if planType! == "B">class="active"</#if>><span><i>3</i>分学考班</span></li>
	</ul>
</div>
<div class="box box-default myBox">
	<div class="box-body">
		<div class="filter-item-right autojxbbutton" style="margin-top:10px;">
			<a type="button" class="btn btn-blue" onclick="autoBath()">自动安排批次</a>
		</div>
		<ul class="nav nav-tabs nav-tabs-1 plan_ul">
			<li class="active" id="myfloatingPlanId"><a href="#aa" data-toggle="tab" onclick="floatingPlan()">走班安排</a></li>
			<li><a href="#aa" data-toggle="tab" onclick="buildTeachClass()">教学班设置</a></li>
		</ul>
		
		<div class="tab-content">
			<div id="aa" class="tab-pane active position-relative">
				<#-- 你的内容 -->
				
			</div>
		</div>
	</div>
</div>
<div class="navbar-fixed-bottom opt-bottom">
	<a class="btn btn-default" href="javascript:void(0);" onclick="initPlan();">重置</a>
	<a class="btn btn-default" href="javascript:void(0);" onclick="prevStep();">上一步</a>
	<a class="btn btn-blue" href="javascript:void(0);" onclick="nextStep();"><#if planType! == "B">完成开班<#else>下一步</#if></a>
</div>

<div class="statistics-container">
	<span class="statistics">需走班同学统计</span>
	<div class="box box-default" style="box-shadow: none;">
		<div class="box-header">
			<h4 class="box-caption">需走班同学统计</h4>
		</div>
		<div class="box-body">
			<table class="table table-striped table-bordered table-hover no-margin">
				
			</table>
		</div>
	</div>
</div>
<!-- page specific plugin scripts --> 
<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<script>
var showRight=false;
$(function(){
	//底部操作按钮
	if ($(".opt-bottom").length > 0) {
		$(".page-content").css("padding-bottom","77px")
	}
	<#assign batchstr = "选考">
	<#if planType! != "A">
		<#assign batchstr = "学考">
	</#if>
	showBreadBack(backFun,false,"${batchstr}走班安排");
	
	// 需走班同学统计
	$(".statistics").click(function(){
		var width = $(this).parent().width()
		if (width === 0) {
			$(this).parent().width(310);
			moveStuCount();
			showRight=true;
		}else{
			$(this).parent().width(0)
			showRight=false;
		}
		makeWidth();
	});

	$("ul.plan_ul .active a").click();
});

function makeWidth(){
	var width1=$(".myBox .box-body").width();
	var width2=$(".statistics-container").width();
	if(showRight){
		$("#aa").width(width1-width2+5);
		$(".autojxbbutton").css({"margin-right":width2});
	}else{
		$("#aa").width(width1);
		$(".autojxbbutton").css({"margin-right":0});
	}
}

function backFun(){
	var url =  '${request.contextPath}/newgkelective/${divide.gradeId!}/goDivide/index/page';
	$("#showList").load(url);
}
// 走班学生统计
function moveStuCount(){
	var url =  '${request.contextPath}/newgkelective/${divide.id!}/floatingPlan/stuMoveCount?planType=${planType?default(A)}';
	$(".statistics-container .box-body").load(url);
	
}

function floatingPlan(){
	$(".autojxbbutton").show();
	$(".statistics-container").show();
	var url =  '${request.contextPath}/newgkelective/${divide.id!}/floatingPlan/page?planType=${planType?default(A)}';
	$("#aa").load(url);
}
function refresh(){
	var url =  '${request.contextPath}/newgkelective/${divide.id!}/floatingPlan/index?planType=${planType!}';
	$("#showList").load(url);
}
function buildTeachClass(){
	$(".autojxbbutton").hide();
	$(".statistics-container").hide();
	if(showRight){
		$(".statistics-container .statistics").click();
	}
	$("#aa").load("${request.contextPath}/newgkelective/${divide.id!}/floatingPlan/teachClassSet?planType=${planType!}");
}
var isInitPlan=false;
function initPlan(){
	 layer.confirm('确定重置?<br>重置将删除所有手动开班，并且初始化走班安排', function(index){
	 	if(isInitPlan){
	 		return;
	 	}
	 	isInitPlan=true;
	 	var ii = layer.load();
		$.ajax({
		    url: '${request.contextPath}/newgkelective/${divide.id!}/floatingPlan/initPlan?planType=${planType!?default(A)}',
	        type:"post",
	        dataType:'json',
	        success:function(jsonO){
	        	layer.close(ii);
	            if (jsonO.success) {
	            	isInitPlan=false;
	                layer.msg(jsonO.msg,{offset:'t',time:2000});
	                $("ul.plan_ul li:eq(0) a").click();
	            }else{
	            	isInitPlan=false;
	            	layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
	            }
	        }
		});
    },function(){
    	isInitPlan=false;
    });
}
function prevStep(){
	if("${planType!}" == "A"){
		// 分行政班
		var url =  '${request.contextPath}/newgkelective/${divide.id!}/divideClass/item';
		$("#showList").load(url);
	}else if("${planType!}" == "B"){
		var url =  '${request.contextPath}/newgkelective/${divide.id!}/floatingPlan/index?planType=A';
		$("#showList").load(url);
	}
}
function nextStep(){
	if("${planType!}" == "A"){
		var url =  '${request.contextPath}/newgkelective/${divide.id!}/floatingPlan/index?planType=B';
		$("#showList").load(url);
	}else if("${planType!}" == "B"){
		// 检测分班 完成
		$.ajax({
		    url: '${request.contextPath}/newgkelective/${divide.id!}/floatingPlan/finishDivide',
	        type:"post",
	        dataType:'json',
	        success:function(jsonO){
	            if (jsonO.success) {
	                layer.msg(jsonO.msg,{offset:'t',time:2000});
	               	backFun();
	            }else{
	            	layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
	            }
	        }
		});
	}
}
var isReS=false;
function autoBath(){
	var subjectType='${planType!}';
	var subjectTypeName=subjectType=="A"?'选考':'学考';
	if(isReS){
		return;
	}
	isReS=true;
	var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
	showConfirm("是否需要重新安排批次，如果选择确定，将会清除"+subjectTypeName+"已经安排的数据",options,function(){
		layer.closeAll();
		var ii = layer.load();
		repeatArrange(subjectType,"1");	
	},function(){
		isReS=false;
	});
	isReS=false;
}


function repeatArrange(subjectType,actionIndex){
	$.ajax({
		url:'${request.contextPath}/newgkelective/${divideId!}/divideClass/autoArrangeBath',
		data:{'subjectType':subjectType},
		dataType : 'json',
		type:'post',
		success:function(data) {
			var jsonO = data;
	 		if(data.stat=="success"){
	 			if(actionIndex=="1"){
 					 repeatArrange(subjectType,"0");
 				}else{
					<#--本页面刷新-->
					isReS=false;
					layer.closeAll();
					layer.msg("安排批次成功！", {
						offset: 't',
						time: 2000
					});
					$("#myfloatingPlanId a").click();
				}
 			}else if(data.stat=="error"){
 				if(actionIndex=="1"){
 					 repeatArrange(subjectType,"0");
 				}else{
 					layer.closeAll();
	 				isReS=false;
	 				layerTipMsg(false,"失败","原因："+data.message);
	 				$("#myfloatingPlanId a").click();
 				}
 			}else{
 				<#--分班中 进去首页-->
 				//backFun();
 				<#--循环-->
 				setTimeout(function(){
					repeatArrange(subjectType,"0");
				},5000);
 				
 			}	
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}
</script>