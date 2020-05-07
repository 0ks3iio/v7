<#--<a href="javascript:void(0)" onclick="gobackGroup()" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>-->
<div class="box box-default">
	<div class="box-header">
		<div class="filter">
			<div class="filter-item">
				<h3 class="box-title">3科组合开班情况</h3>
			</div>
			<#-- <div class="filter-item filter-item-right">
				<#if canEdit?default(false)><a class="btn btn-white" href="javascript:" onclick="reArrangeBath();">重新安排</a></#if>
				<a class="btn btn-white" href="javascript:" onclick="beforeToAll();">上一步</a>
				<a class="btn btn-blue" href="javascript:" onclick="nextToB();">下一步</a>
			</div>  -->
		</div>
	</div>
	<div class="box-body">
		<div class="table-container">
			<div class="table-container-body">
				<table class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th>序号</th>
							<th>班级名称</th>
							<th>班级类型</th>
							<th>总人数</th>
							<th>男生</th>
							<th>女生</th>
							<th>行政班科目（时间点）</th>
						</tr>
					</thead>
					<tbody>
					<#if threeClsList?exists && threeClsList?size gt 0>
					<#list threeClsList as tcls>
					<tr>
					<td>${tcls_index+1}</td>
					<td>
					<input class="table-input" type="text" data-clazzId="${tcls.id!}" value="${tcls.className!}" old-value="${tcls.className!}" onblur="updateGroupClassName(this,'${tcls.id!}')">
					</td>
					<td>组合班</td>
					<td>${tcls.studentCount}</td>
					<td>${tcls.boyCount}</td>
					<td>${tcls.girlCount}</td>
					<td>${tcls.relateName!}</td>
					</tr>
					</#list>
					</#if>
					</tbody>
				</table>
			</div>		
		</div>
	</div>
	<#if twoClsList?exists && twoClsList?size gt 0>
	<div class="box-header">
		<div class="filter">
			<div class="filter-item">
				<h3 class="box-title">2科组合开班情况</h3>
				<em>默认2科组合下学生剩下一门科目固定在时间${maxCount!}</em>
			</div>
		</div>
	</div>
	<div class="box-body">
		<div class="table-container">
			<div class="table-container-body">
				<table class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th>序号</th>
							<th>班级名称</th>
							<th>班级类型</th>
							<th>总人数</th>
							<th>男生</th>
							<th>女生</th>
							<th>行政班科目（时间点）</th>
						</tr>
					</thead>
					<tbody>
					
					<#list twoClsList as tcls>
					<tr>
					<td>${tcls_index+1}</td>
					<td>
					<input class="table-input" type="text" data-clazzId="${tcls.id!}" value="${tcls.className!}" old-value="${tcls.className!}" onblur="updateGroupClassName(this,'${tcls.id!}')">
					</td>
					<td>组合班</td>
					<td>${tcls.studentCount}</td>
					<td>${tcls.boyCount}</td>
					<td>${tcls.girlCount}</td>
					<td>${tcls.relateName!}</td>
					</tr>
					</#list>
					
					</tbody>
				</table>
			</div>		
		</div>
	</div>
	</#if>
	<div class="box-header">
		<h3 class="box-title">混合组合开班情况</h3>
	</div>
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li class="active" role="presentation"><a href="javascript:void(0)" onclick="clsBatch();" role="tab" data-toggle="tab">行政班科目时间点</a></li>
			<li role="presentation"><a href="javascript:void(0)" onclick="subBatch();" role="tab" data-toggle="tab">走班科目时间点</a></li>
			<li role="presentation"><a href="javascript:void(0)" onclick="stuStat();" role="tab" data-toggle="tab">走班学生统计</a></li>
			<li role="presentation"><a href="javascript:void(0)" onclick="stuDistribute();" role="tab" data-toggle="tab">走班学生分布</a></li>
		</ul>
		<div class="tab-content" id="batchDiv">
		</div>
		
		<#--<div class="text-center">
		    <a class="btn btn-white" onclick="gobackGroup();" href="javascript:;">上一步</a>
		</div>-->
	</div>
</div>
<div class="navbar-fixed-bottom opt-bottom">
	<#if canEdit?default(false)><a class="btn btn-white" href="javascript:" onclick="reArrangeBath();">重新安排</a></#if>
	<a class="btn btn-white" href="javascript:" onclick="beforeToAll();">上一步</a>
	<a class="btn btn-blue" href="javascript:" onclick="nextToB();">下一步</a>
</div>
<script>
function gobackDivideIndex(){
	var url =  '${request.contextPath}/newgkelective/${gradeId!}/goDivide/index/page';
	$("#showList").load(url);
}

function clsBatch(){
	var url = '${request.contextPath}/newgkelective/clsBatch/${divideId!}/clsBatch/page';
	$("#batchDiv").load(url);
}

function subBatch(){
	var url = '${request.contextPath}/newgkelective/clsBatch/${divideId!}/subBatch/page';
	$("#batchDiv").load(url);
}

function stuStat(){
	var url = '${request.contextPath}/newgkelective/${divideId!}/manualDivide/index';
	$("#batchDiv").load(url);
}

function stuDistribute(){
	var url = '${request.contextPath}/newgkelective/${divideId!}/showStudentDistribution/page';
	$("#batchDiv").load(url);
}

$(function(){
	showBreadBack(gobackDivideIndex,false,"分班安排");
	clsBatch();
})

function beforeToAll(){
	var url = '${request.contextPath}/newgkelective/${divideId!}/divideGroup/groupIndex/page';
	$("#showList").load(url);
}

var isUpdateName=false;
function updateGroupClassName(obj, id){
	if(isUpdateName){
		return;
	}
	isUpdateName=true;
	var oldName=$(obj).attr("old-value");
	var nn = $.trim(obj.value);
	if(nn==''){
		isUpdateName=false;
		layer.tips('名称不能为空！',$(obj), {
				tipsMore: true,
				tips: 3
			});
		return;	
	}
	if(getLength(nn)>80){
		isUpdateName=false;
		layer.tips('名称内容不能超过80个字节（一个汉字为两个字节）！',$(obj), {
				tipsMore: true,
				tips: 3
			});
		return;
	}
	if(nn==oldName){
		isUpdateName=false;
		return;
	}
	
	$.ajax({
		url:'${request.contextPath}/newgkelective/${divideId!}/divideClass/updateGroupClassName',
		data: {'classId':id,'className':nn},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
	 			$(obj).attr("old-value",nn);
	 			layer.closeAll();
			  	layer.msg(jsonO.msg, {
					offset: 't',
					time: 2000
				});
				<#--reloadBatch();-->
	 		}else{
	 			obj.value=oldName;
	 			layer.tips(jsonO.msg,$(obj), {
					tipsMore: true,
					tips: 3
				});
			}
			isUpdateName=false;
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}

var isMyCheck=false;
function nextToB(){
	if(isMyCheck){
		return ;
	}
	isMyCheck=true;
	var ii = layer.load();
	//验证
	var url = '${request.contextPath}/newgkelective/${divideId!}/manualDivide/checkStudent';
	$.ajax({
		url:url,
		dataType: "JSON",
		success: function(data){
			layer.closeAll();
			if(data.success){
				$("#showList").load("${request.contextPath}/newgkelective/BathDivide/${divideId!}/singleList3/page");
			}else{
				isMyCheck=false;
				layerTipMsg(data.success,"失败",data.msg);
			}
		},
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	});
}
var isDeleteAll=false;
function reArrangeBath(){
	if(isDeleteAll){
		return;
	}
	isDeleteAll=true;
	var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
	showConfirm("是否确定重新安排",options,function(){
		$.ajax({
			url:"${request.contextPath}/newgkelective/clsBatch/${divideId!}/deleteDivideAllJxbClass",
			data:{},
			dataType : 'json',
			type:'post',
			success:function(data) {
				layer.closeAll();
				var jsonO = data;
		 		if(jsonO.success){
		 			layer.msg("操作成功！", {
						offset: 't',
						time: 2000
					});
					isDeleteAll=false;
		 			var url="${request.contextPath}/newgkelective/clsBatch/${divideId!}/index";
		 			$("#showList").load(url);
		 		}
		 		else{
		 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
		 			isDeleteAll=false;
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
			
	},function(){
		isDeleteAll=false;
	});
}
</script>