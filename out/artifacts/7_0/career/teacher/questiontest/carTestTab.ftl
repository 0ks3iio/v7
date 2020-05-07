<script src="${request.contextPath}/static/components/echarts/echarts.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/js/LodopFuncs.js"></script>
<div class="page-content" id="showTestResultListDiv">
	<div class="row">
		<div class="col-xs-12">
			<div class="box box-default">
				<div class="box-body clearfix">
                	<div class="tab-container">
						<div class="tab-header clearfix">
							<ul class="nav nav-tabs nav-tabs-1">
								<li class="active">
									<a data-toggle="tab" href="#" onclick="showTestTab('1',this)">评测结果</a>
								</li>
								<li class="">
									<a data-toggle="tab" href="#" onclick="showTestTab('2',this)">评测内容</a>
								</li>
								<li class="">
									<a data-toggle="tab" href="#" onclick="showTestTab('3',this)">评测解析</a>
								</li>
							</ul>
						</div>
						<div id="carTestTab1" class="tab-content carTab">
						</div>
						<div id="carTestTab2" class="tab-content carTab" style="display:none">
						</div>
						<div id="carTestTab3" class="tab-content carTab" style="display:none">
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div id="showTestResultDiv" style="display:none">
	
</div>
<script>
$(function(){
	$("#carTestTab1").load("${request.contextPath}/careerplan/teacher/testresult/head");
	$("#carTestTab2").load("${request.contextPath}/careerplan/teacher/testresult/content");
	$("#carTestTab3").load("${request.contextPath}/careerplan/teacher/testresult/analysis");
});

function showTestTab(type,objthis) {
	$(objthis).parent().attr("class","active").siblings().removeAttr("class");
	$("#carTestTab"+type).attr("style","display:block").siblings(".carTab").attr("style","display:none");
}

var oneTime;

function layerTime() {
	oneTime = layer.msg('加载中', {
  		icon: 16,
  		shade: 0.01,
  		time: 60*1000
	});
}

function layerClose() {
	layer.close(oneTime);
}
</script>