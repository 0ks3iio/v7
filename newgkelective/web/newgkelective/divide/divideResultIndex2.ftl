<#-- 3+1+2固定导航页面 -->
<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist" id="tablist">
			<#if showXZB>
				<li <#if !(type??) || type! == 'X'>class="active"</#if> role="presentation"><a onclick="showContent('X')" href="javascript:void(0)" role="tab" data-toggle="tab">新行政班</a></li>
			<#else>
				<li <#if !(type??) || type! == 'Y'>class="active"</#if> role="presentation"><a onclick="showContent('Y')" href="javascript:void(0)" role="tab" data-toggle="tab">原行政班</a></li>
			</#if>
			<li <#if type! == 'J'>class="active"</#if> role="presentation"><a onclick="showContent('J')" href="javascript:void(0)" role="tab" data-toggle="tab">教学班</a></li>
			
			<li <#if type! == 'stuchange'>class="active"</#if> role="presentation"><a onclick="showContent('stuchange')" href="javascript:void(0)" role="tab" data-toggle="tab">学生调班</a></li>
				
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
		var url =  '${request.contextPath}/newgkelective/${gradeId!}/goDivide/index/page';
		<#if fromArray?default('') == '1'>
            <#if fromSolve?default('0') == '1'>
                url = '${request.contextPath}/newgkelective/${arrayId!}/arraySet/pageIndex';
			<#elseif arrayId?default('')==''>
			    url = '${request.contextPath}/newgkelective/${gradeId!}/goArrange/addArray/page?divideId=${divideId!}&lessArrayId=${lessArrayId!}&plArrayId=${plArrayId!}';
		    <#else>
			    url = '${request.contextPath}/newgkelective/${gradeId!}/goArrange/editArray/page?arrayId=${arrayId!}';
		    </#if>
		</#if>		
		$("#showList").load(url);
	}
	
	function showContent(type){
		if(type=="stuchange"){
			var	url = '${request.contextPath}/newgkelective/${divideId!}/arrayResult/changeStuClass/page?type=1';
			$("#aa").load(url);
		}else{
			var	url = '${request.contextPath}/newgkelective/${divideId!}/showDivideResult/page?fromSolve=${fromSolve?default("0")}&arrayId=${arrayId?default("")}&type='+type;
			$("#aa").load(url);
		}
		
		
	}
	
	function showDetail(classId,type){
		var url = '${request.contextPath}/newgkelective/${divideId!}/showClassDetail/page?classId='+classId+'&type='+type;
		$("#aa").load(url);
	}
</script>