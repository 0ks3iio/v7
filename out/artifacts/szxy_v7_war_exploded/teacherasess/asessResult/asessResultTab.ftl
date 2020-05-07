<div class="box box-default">
	<div class="box-body clearfix">
		<div class="tab-container">
			<div class="tab-header clearfix">
				<ul class="nav nav-tabs">
					<li id="li1" class="active">
						<a href="javascript:void(0);" data-toggle="tab" aria-expanded="true" onclick="itemShowTab(1)">名次系数分考核表</a>
					</li>
					<li id="li2">
						<a href="javascript:void(0);" data-toggle="tab" aria-expanded="true" onclick="itemShowTab(2)">单双上线分析表</a>
					</li>
					<li id="li3">
						<a href="javascript:void(0);" data-toggle="tab" aria-expanded="true" onclick="itemShowTab(3)">名次线考核表</a>
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
		$("#li3").removeAttr("class");
		url =  '${request.contextPath}/teacherasess/asessResult/asessResultIndex/page?teacherAsessId='+'${assessId!}';
	}else if("2"==type){
		$("#li1").removeAttr("class");
		$("#li2").attr("class","active");
		$("#li3").removeAttr("class");
		url =  '${request.contextPath}/teacherasess/asessLine/index?assessId='+'${assessId!}';
	}else if("3"==type){
		$("#li1").removeAttr("class");
		$("#li2").removeAttr("class");
		$("#li3").attr("class","active");
		url =  '${request.contextPath}/teacherasess/asessCheck/index?assessId='+'${assessId!}';
	}
	$("#showTabDiv").load(url);
}
</script>