<div class="box box-default">
	<div class="box-body clearfix">
		<div class="tab-container">
			<div class="tab-header clearfix">
				<ul class="nav nav-tabs">
					<li id="li1" class="active">
						<a href="javascript:void(0);" data-toggle="tab" aria-expanded="true" onclick="itemShowTab(1)">优秀生设置</a>
					</li>
					<li id="li2">
						<a href="javascript:void(0);" data-toggle="tab" aria-expanded="true" onclick="itemShowTab(2)">优秀生统计</a>
					</li>
				</ul>
			</div>
			<div class="tab-content" id="showTabDiv">
			</div>
		</div>
	</div>
</div>
<script>
$(function(){
	itemShowTab(1);
});

function itemShowTab(type){
	var url = "";
	if("1"==type){  
		$("#li1").attr("class","active");
		$("#li2").removeAttr("class");
		url =  '${request.contextPath}/exammanage/edu/goodStu/index';
	}else {
		$("#li1").removeAttr("class");
		$("#li2").attr("class","active");
		url =  '${request.contextPath}/exammanage/edu/goodStuStatistics/index';
	}
	$("#showTabDiv").load(url);
}
</script>