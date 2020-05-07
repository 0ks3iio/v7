<title>应用管理</title>
<#import "/fw/macro/webmacro.ftl" as w>
<#import "/fw/macro/treemacro.ftl" as t>
<link rel="stylesheet" href="${request.contextPath}/static/ace/css/ui.jqgrid.css" />
<script src="${request.contextPath}/static/ace/js/jqGrid/jquery.jqGrid.src.js"></script>
<script src="${request.contextPath}/static/ace/js/jqGrid/i18n/grid.locale-cn.js"></script>
<#-- sweetalert引入文件  -->
<link rel="stylesheet" href="${request.contextPath}/static/sweetalert/sweetalert.css" />
<script src="${request.contextPath}/static/sweetalert/sweetalert.min.js"></script>

<div class="row">
	<div class="col-sm-12" style="margin-left:12px;">
		<@w.btn btnId="btn-addSubsystem" btnValue="新增应用" btnClass="fa-plus" />
	</div>
</div>

<div class="col-sm-12 listDiv" style="margin-top:8px;">
</div>

<script type="text/javascript">
	var indexDiv = 0;
	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		$(".listDiv").load("${request.contextPath}/basedata/subsystem/list/page");
		$("#btn-addSubsystem").on("click", function(){
			indexDiv = layerDivUrl("${request.contextPath}/basedata/subsystem/add/page");
		});
	});
</script>
	
	
	