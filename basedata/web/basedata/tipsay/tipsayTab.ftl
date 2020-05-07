<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs mynav nav-tabs-1 nav-tabs-table" role="tablist">
			<li <#if type?default('1')=='1'>class="active"</#if> role="presentation" id="1"><a href="#aa" role="tab" data-toggle="tab" onclick="showList('1')">代课申请</a></li>
			<#if isAdmin><li <#if type?default('1')=='2'>class="active"</#if> role="presentation" id="2"><a href="#bb" role="tab" data-toggle="tab" onclick="showList('2')">代课管理</a></li></#if>
		</ul>
		<div class="tab-content" id="showTabList">
			
		</div>
	</div>
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">
	$(document).ready(function(){
		hidenBreadBack();
		showList('${type?default('1')}');
	});
	function showList(type){
		var url =  '${request.contextPath}/basedata/tipsay/head/page?type='+type;
		$("#showTabList").load(url);
	}
</script>