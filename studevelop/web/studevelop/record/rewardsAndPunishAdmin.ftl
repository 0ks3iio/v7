<!-- chosen -->
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div id="showDiv" class="box box-default">
	<div class="nav-tabs-wrap clearfix">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li role="presentation" class="active"><a href="#aa" onclick="itemShowList('1')" role="tab" data-toggle="tab">奖励信息</a></li>
			<li role="presentation"><a href="#bb" role="tab" onclick="itemShowList('2')" data-toggle="tab">处分信息</a></li>
		</ul>
	</div>
	<div id="itemShowDivId">
		
	</div>
</div>
<script type="text/javascript">
	$(function(){
		itemShowList('1');
	});
	function itemShowList(tabIndex){
		if(!tabIndex || tabIndex == '1'){
			var url =  '${request.contextPath}/studevelop/rewardsRecord/head?tabType=1';
		}else if(tabIndex == '2'){
			var url =  '${request.contextPath}/studevelop/rewardsRecord/head?tabType=2';
		}
		$("#itemShowDivId").load(url);
	}
</script>
