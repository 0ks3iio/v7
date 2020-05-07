<div class="box box-default">
	<div class="box-body">
		<div class="nav-tabs-wrap">
		    <ul class="nav nav-tabs" role="tablist">
		        <li role="presentation" class="active"><a href="#aa" id="aa" role="tab" data-toggle="tab" onclick="itemShowList(1)">考核录入</a></li>
		        <li role="presentation" ><a href="#aa" id="aa" role="tab" data-toggle="tab" onclick="itemShowList(2)">个人寝室违纪</a></li>
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
	        url =  '${request.contextPath}/stuwork/dorm/check/list/page';
		}else if(tabType == 2){
	        url =  '${request.contextPath}/stuwork/dorm/check/listDis/page';
		}
        $("#itemShowDivId").load(url);
	}
</script>