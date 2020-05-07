<div class="box box-default">
	<div class="box-body">
		<div class="nav-tabs-wrap">
		    <ul class="nav nav-tabs" role="tablist">
		        <li role="presentation" class="active"><a href="#"  role="tab" data-toggle="tab" onclick="itemShowList(1)">指标设置</a></li>
		        <li role="presentation" ><a href="#" role="tab" data-toggle="tab" onclick="itemShowList(2)">项目设置</a></li>
		    </ul>
		</div>
		<div class="tab-content" id="itemShowDivId">
		</div>
	</div>
</div>
<script>
	$(function(){
		itemShowList(1);
	});
	function itemShowList(tabType){
		var url = '';
		if(tabType == 1){
	        url =  '${request.contextPath}/stuwork/health/item/list';
		}else if(tabType == 2){
	        url =  '${request.contextPath}/stuwork/health/project/list';
		}
        $("#itemShowDivId").load(url);
	}
</script>