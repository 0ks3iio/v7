<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div class="box box-default">
	<div class="box-body">
		<div class="nav-tabs-wrap">
		    <ul class="nav nav-tabs" role="tablist">
		        <li role="presentation" class="active"><a href="#aa" role="tab" data-toggle="tab" onclick="itemShowList(1)">寝室楼维护</a></li>
		        <li role="presentation" ><a href="#aa"  role="tab" data-toggle="tab" onclick="itemShowList(2)">寝室信息维护</a></li>
		        <li role="presentation" ><a href="#aa"  role="tab" data-toggle="tab" onclick="itemShowList(3)">学生住宿安排</a></li>
		        <li role="presentation" ><a href="#aa"  role="tab" data-toggle="tab" onclick="itemShowList(4)">教师住宿安排</a></li>
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
	        url =  '${request.contextPath}/stuwork/dorm/building/index/page';
		}else if(tabType == 2){
	        url =  '${request.contextPath}/stuwork/dorm/room/index/page';
		}else if(tabType == 3){
	        url =  '${request.contextPath}/stuwork/dorm/bed/index/page?roomProperty=1';//学生
		}else if(tabType==4){
			url =  '${request.contextPath}/stuwork/dorm/bed/index/page?roomProperty=2';//老师
		}
        $("#itemShowDivId").load(url);
	}
</script>