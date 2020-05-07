<#-- 全走班单科分层以及 全固定、半固定等模式的分班结果 导航页面 -->
<#--
<a href="javascript:void(0)" class="page-back-btn" onclick="goback()">
	<i class="fa fa-arrow-left"></i>返回
</a>-->
<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist" id="tablist">
			<#if showXZB>
				<li <#if !(type??) || type! == 'X'>class="active"</#if> role="presentation"><a onclick="showContent('X')" href="javascript:void(0)" role="tab" data-toggle="tab">新行政班</a></li>
			<#else>
				<li <#if !(type??) || type! == 'Y'>class="active"</#if> role="presentation"><a onclick="showContent('Y')" href="javascript:void(0)" role="tab" data-toggle="tab">原行政班</a></li>
			</#if>
			<li <#if type! == 'A' >class="active"</#if> role="presentation"><a onclick="showContent('A')" href="javascript:void(0)" role="tab" data-toggle="tab">选考班</a></li>
			<li <#if type! == 'B'> class="active" </#if> role="presentation"><a onclick="showContent('B')" href="javascript:void(0)" role="tab" data-toggle="tab">学考班</a></li>
			
			<#if showOther>
			<li <#if type! == 'C'> class="active" </#if> role="presentation"><a onclick="showContent('C')" href="javascript:void(0)" role="tab" data-toggle="tab">高二学考</a></li>
			<li <#if type! == 'O'> class="active" </#if> role="presentation"><a onclick="showContent('O')" href="javascript:void(0)" role="tab" data-toggle="tab">语数外</a></li>
			</#if>
			<#--
			<li <#if type! == 'count'> class="active" </#if> role="presentation"><a onclick="showContent('count')" href="javascript:void(0)" role="tab" data-toggle="tab">班级统计</a></li>
			-->
			<li <#if type! == 'count1'> class="active" </#if> role="presentation"><a onclick="showContent('count1')" href="javascript:void(0)" role="tab" data-toggle="tab">教学班学生组成统计</a></li>
			
			<#--<li <#if type! == 'change'> class="active" </#if> role="presentation"><a onclick="showContent('change')" href="javascript:void(0)" role="tab" data-toggle="tab">学生调班</a></li>-->
			<li <#if type! == 'stuchange'> class="active" </#if> role="presentation"><a onclick="showContent('stuchange')" href="javascript:void(0)" role="tab" data-toggle="tab">学生调班</a></li>
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
		var url;
		if(type=="change"){
            if (${fromSolve?default("0")} == '1') {
                var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/changeClass/page?fromSolve=${fromSolve?default("0")}';
            } else {
                var url = '${request.contextPath}/newgkelective/${divideId!}/arrayResult/changeClass/page?type=1';
            }
		}else if(type=="stuchange"){
			if (${fromSolve?default("0")} == '1') {
                var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/changeStuClass/page?fromSolve=${fromSolve?default("0")}';
            } else {
                var url = '${request.contextPath}/newgkelective/${divideId!}/arrayResult/changeStuClass/page?type=1';
            }
		}else if(type=="count1"){
			url = '${request.contextPath}/newgkelective/${divideId!}/showDivideCountResult/page?fromSolve=${fromSolve?default("0")}&arrayId=${arrayId?default("")}&type='+type;
		}else{
			url = '${request.contextPath}/newgkelective/${divideId!}/showDivideResult/page?fromSolve=${fromSolve?default("0")}&arrayId=${arrayId?default("")}&type='+type;
		}
		$("#aa").load(url);
	}
	
	function showDetail(classId,type){
		var url = '${request.contextPath}/newgkelective/${divideId!}/showClassDetail/page?classId='+classId+'&type='+type;
		$("#aa").load(url);
	}
</script>