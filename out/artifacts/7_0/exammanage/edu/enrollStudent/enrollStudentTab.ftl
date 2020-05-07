<div class="box box-default">
	<div class="box-body clearfix">
		<div class="tab-container">
			<div class="tab-header clearfix">
				<ul class="nav nav-tabs">
					<li id="li1" class="active">
						<a href="javascript:void(0);" data-toggle="tab" aria-expanded="true" onclick="itemShowTab(1)">报名名单</a>
					</li>
					<li id="li2">
						<a href="javascript:void(0);" data-toggle="tab" aria-expanded="true" onclick="itemShowTab(2)">报名统计</a>
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
		url =  '${request.contextPath}/exammanage/edu/examStu/enrollstu/index';
	}else {
		$("#li1").removeAttr("class");
		$("#li2").attr("class","active");
		url =  '${request.contextPath}/exammanage/edu/examStu/stustatistics/index';
	}
	$("#showTabDiv").load(url);
}
</script>