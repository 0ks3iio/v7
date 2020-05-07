<div class="box-body">
	<div class="nav-tabs-wrap">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li role="presentation" class="active"><a href="#aa" role="tab" id="aa" data-toggle="tab" onclick="itemShowList(1)">安排结果</a></li>
			<li role="presentation"><a href="#ee" role="tab" id="ee" data-toggle="tab" onclick="itemShowList(2)">结果统计</a></li>
		</ul>
	</div>
	<div class="tab-content" id="itemShowDivId">
	</div>
</div>
<script>
	$(function(){
		itemShowList(1);
	});
	function itemShowList(tabType){
		var url = '';
		if(tabType == 1){
	        url = '${request.contextPath}/gkelective/${arrangeId!}/arrangePlan/resultListHead/page?planId=${planId!}';
		}else if(tabType == 2){
			url = '${request.contextPath}/gkelective/${arrangeId!}/arrangePlan/resultPie/page?planId=${planId!}';
		}
        $("#itemShowDivId").load(url);
	}
	
</script>