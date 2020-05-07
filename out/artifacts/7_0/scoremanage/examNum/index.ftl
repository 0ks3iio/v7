<title>考号设置</title>
<#import "/fw/macro/webmacro.ftl" as w>
<link rel="stylesheet" href="${request.contextPath}/static/ace/css/ui.jqgrid.css" />
<!-- ajax layout which only needs content area -->
<div class="row">
	<div class="col-lg-12 col-md-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="well well-sm">
			相关功能：
			<@w.pageRef url="${request.contextPath}/basedata/unit/page" name="单位管理" />
			<@w.arrowRight />
			<@w.pageRef url="${request.contextPath}/basedata/teacher/page" name="教师管理" />	
		</div>
	</div>
</div>
<div class="row">
	<div class="col-xs-12">
		<div class="tab-container">
			<div class="tab-header clearfix">
				<ul class="nav nav-tabs pull-left" id="tabId">
				 	<li class="active">
				 		<a data-toggle="tab" href="#a" onclick="chooseTab(1)">考号设置</a>
				 	</li>
				 	<li>
				 		<a data-toggle="tab" href="#b" onclick="chooseTab(2)">不排考设置</a>
				 	</li>
				 	<li>
				 		<a data-toggle="tab" href="#c" onclick="chooseTab(3)">不统分设置</a>
				 	</li>
				 </ul>
			</div>
			<div class="tab-content ">
				
			</div>
		</div>
	</div>
</div>
<div id="tabDiv">	
</div>
<!-- page specific plugin scripts -->
<script type="text/javascript">
	// 需要用到的js脚本，延迟加载
	var scripts = [null,
	"${request.contextPath}/static/ace/components/chosen/chosen.jquery.js",null
	];

	$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
		$("#tabDiv").load("${request.contextPath}/scoremanage/examNum/index/page");
	});
	function chooseTab(tab){
		if(tab==1){
			$("#tabDiv").load("${request.contextPath}/scoremanage/examNum/index/page?tabType=1");
		}else if(tab==2){
			$("#tabDiv").load("${request.contextPath}/scoremanage/examNum/index/page?tabType=2");
		}else if(tab==3){
			$("#tabDiv").load("${request.contextPath}/scoremanage/examNum/index/page?tabType=3");
		}
	}
	
</script>
