<!-- ajax layout which only needs content area -->
<#if dtos?exists && (dtos?size>0)>
	<#list dtos as item>
		
		</#list>
	<#else>
	<div class="no-data-container">
		<div class="no-data no-data-hor">
			<span class="no-data-img">
				<img src="${request.contextPath}/gkelective/images/noSelectSystem.png" alt="">
			</span>
			<div class="no-data-body">
				<h3>暂无设置分班方案</h3>
			</div>
		</div>
	</div>
</#if>
<a href="javascript:" class="flow" onclick="dodo();">测试选课
</a>
<script>
	function dodo(){
		var url =  '${request.contextPath}/newgkelective/18B231A7845D4031B6DB3132E1195766/5D0BB06CA415399AE050A8C09B006E38/chosen/tabHead/page';
		
		$("#showList").load(url);
	}
</script>