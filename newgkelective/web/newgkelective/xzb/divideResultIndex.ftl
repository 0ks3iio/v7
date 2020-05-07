<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist" id="tablist">
			<li class="active" role="presentation"><a onclick="showContent('Y')" href="javascript:void(0)" role="tab" data-toggle="tab">行政班</a></li>
			<li role="presentation"><a onclick="showContent('A')" href="javascript:void(0)" role="tab" data-toggle="tab">教学班</a></li>
		</ul>
		<div class="tab-content">
			<div id="aa" class="tab-pane active" role="tabpanel">
				<#-- 加载内容 -->
			</div>
		</div>
	</div>
</div>
<script>
	init();
	function init(){
		showBreadBack(goback, false, "分班结果")
		$("#tablist li.active a").click();
	}
	
	function goback(){
		var url =  '${request.contextPath}/newgkelective/xzb/addArray/page?divideId=${divideId!}';
		<#if fromArray?default('') == '1'>
			url = '/newgkelective/xzb/arraySet/pageIndex?arrayId=${arrayId!}'; 
		</#if>
		$("#showList").load(url);
	}
	
	function showContent(type){
		var url;
		url = '${request.contextPath}/newgkelective/xzb/${divideId!}/divideResult/page?fromSolve=${fromSolve?default("0")}'
			 +'&arrayId=${arrayId?default("")}&type='+type+'&gradeId=${gradeId}';
		
		$("#aa").load(url);
	}
	
	function showDetail(classId,type){
		var url = '${request.contextPath}/newgkelective/xzb/${divideId!}/showClassDetail/page?classId='+classId+'&type='+type;
		//return;
		$("#aa").load(url);
	}
</script>