<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div class="table-container">
	<div class="table-container-header text-right">
		<a href="javascript:" class="btn btn-blue"  onclick="restartName();">根据序号重新命名行政班名称</a>
		<a href="javascript:doExportAll();" class="btn btn-white">导出各班学生名单</a>
	</div>
	<div class="table-container-body">
		<table class="table table-bordered layout-fixed table-editable table-striped table-hover" data-label="不可排课">
			<thead>
				<tr>
					<th width="120"></th>
					<th >序号</th>
					<th >行政班</th>
					<#if !isNoOpenXzb>
					<th >所属组合班</th>
					</#if>
					<th >总人数</th>
					<th >男</th>
					<th >女</th>
					<#if !isNoOpenXzb>
					<th >学考上课科目</th>
					<#else>
					<th >选物理人数</th>
					<th >选历史人数</th>
					</#if>
					<th >查看学生</th>
				</tr>
			</thead>
			<tbody id="sortable">
			<#if dtoList?exists && dtoList?size gt 0 >
			<#list dtoList as dto>
				<tr>
					<td class="text-center my-handle" ><i class="fa fa-arrows"></i></td>
					<td data='${dto.classId!}'>${dto_index+1}</td>
					<td >${dto.className}</td>
					<#if !isNoOpenXzb>
					<td >${dto.relateName!'/'}</td>
					</#if>
					<td >${dto.allNum!}</td>
					<td >${dto.boyNum!}</td>
					<td >${dto.girlNum!}</td>
					<#if !isNoOpenXzb>
					<td >${dto.courseB!}</td>
					<#else>
					<td >${dto.wlStuNums!}</td>
					<td >${dto.liStuNums!}</td>
					</#if>
					<td ><a href="javascript:void(0)" onclick="showDetail('${dto.classId!}','${type!}')">查看</a></td>
				</tr>
			</#list>
			</#if>
			</tbody>
		</table>
	
	</div>
</div>

<script src="/static/sortable/Sortable.min.js"></script>

<script>
	var a = document.getElementById('sortable');
	Sortable.create(a,{
		handle: '.my-handle',
		animation: 150,
		onUpdate:function(){
			refreshIndex();
		}
	});
	
	function refreshIndex(){
		var all = $("#sortable tr");
		if(all.length <1){
			return;
		}
		var i = 0;
		var inf= '';
		all.each(function(){
			i = i+1;
			$(this).find('td:eq(1)').text(i);
			var classId = $(this).find('td:eq(1)').attr('data');
			inf = inf + classId+"-"+i+",";
		});
		updateOrder(inf.substr(0,inf.length-1));
	}
	
	function updateOrder(orderInf){
		$.ajax({
			url : '${request.contextPath}/newgkelective/${divideId!}/updateXzbClassOrder',
			type : 'POST',
			data : 'inf='+orderInf,
			dataType : "JSON",
			success:function(data){
				if(data.success){
					layer.msg("操作成功", {
							offset: 't',
							time: 2000
						});
				}else{
					layerTipMsg(data.success,"失败",data.msg);
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	var isName=false;
	function restartName(){
		if(isName){
			return;
		}
		isName=true;
		var all = $("#sortable tr");
		if(all.length <1){
			return;
		}
		var i = 0;
		var inf= '';
		all.each(function(){
			i = i+1;
			$(this).find('td:eq(1)').text(i);
			var classId = $(this).find('td:eq(1)').attr('data');
			inf = inf + classId+"-"+i+",";
		});
		inf=inf.substr(0,inf.length-1);
		$.ajax({
			url : '${request.contextPath}/newgkelective/${divideId!}/restartXzbname?fromSolve=${fromSolve?default("0")}&arrayId=${arrayId?default("")}',
			data : {'inf':inf},
			type : 'POST',
			dataType : "JSON",
			success:function(data){
				if(data.success){
					isName=false;
					<#if type! != 'Y'>
						showContent("X");
					<#else>
						showContent("Y");
					</#if>
					
					layer.msg("操作成功", {
							offset: 't',
							time: 2000
						});
				}else{
					isName=false;
					layerTipMsg(data.success,"失败",data.msg);
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	
	function doExportAll(){
		var url =  '${request.contextPath}/newgkelective/${divideId!}/exportAllClaStu';
	  	document.location.href=url;
	}
</script>

