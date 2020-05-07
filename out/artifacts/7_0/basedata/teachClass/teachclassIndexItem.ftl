<div class="box box-default">
	<div class="box-body">
		<div class="filter-item-right">
			<a type="button" class="btn btn-blue" onclick="teachClassImport()">教学班导入</a>
			<a type="button" class="btn btn-blue" onclick="teachClassStuImport()">教学班学生导入</a>
		</div>
		<ul class="nav nav-tabs nav-tabs-1 mynav" role="tablist">
			<li <#if showTabType?default('1')!='2'>class="active"</#if> role="presentation" id="1"><a href="#aa" role="tab" data-toggle="tab" onclick="changeTabList('1')">必修课</a></li>
			<li <#if showTabType?default('1')=='2'>class="active"</#if> role="presentation" id="2"><a href="#bb" role="tab" data-toggle="tab" onclick="changeTabList('2')">选修课</a></li>
			<#--
				<#if isShowSeven>
					<li <#if tabType?default('1')=='0'>class="active"</#if> role="presentation" id="0"><a href="#cc" role="tab" data-toggle="tab" onclick="changeTabList('0')">7选3</a></li>
				</#if>
			-->
		</ul>
		<div class="tab-content" id="tablistDiv">
			
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function(){
		<#if showTabType?default('1')=='2'>
			changeTabList('2');
		<#else>
			changeTabList('1');
		</#if>
	});
	function changeTabList(tabType){
		var url =  '${request.contextPath}/basedata/teachclass/head/page?showTabType='+tabType;
		$("#tablistDiv").load(url);
	}
	function teachClassImport(){
		var showTabType=$(".mynav li.active").attr("id");
		if(!showTabType){
			showTabType="1";
		}
		var url =  '${request.contextPath}/basedata/teachclass/importhead/page?type=1&showTabType='+showTabType;
		$("#indexContent").load(url);
	}
	function teachClassStuImport(){
		var showTabType=$(".mynav li.active").attr("id");
		if(!showTabType){
			showTabType="1";
		}
		var url =  '${request.contextPath}/basedata/teachclass/importhead/page?type=2&showTabType='+showTabType;
		$("#indexContent").load(url);
	}
</script>