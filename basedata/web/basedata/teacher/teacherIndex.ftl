<title>教师管理</title>
<#import "/fw/macro/webmacro.ftl" as w>
<#import "/fw/macro/treemacro.ftl" as treemacro>
<link rel="stylesheet" href="${request.contextPath}/static/ace/css/ui.jqgrid.css" />
<script src="${request.contextPath}/static/ace/js/jqGrid/jquery.jqGrid.src.js"></script>
<script src="${request.contextPath}/static/ace/js/jqGrid/i18n/grid.locale-cn.js"></script>
<#-- ztree引入文件 -->
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>

<!-- ajax layout which only needs content area -->
<div class="row">
	<div class="col-lg-6 col-md-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="well well-sm">
			相关功能：
			<@w.pageRef url="${request.contextPath}/basedata/unit/page" name="单位管理" />
			<@w.arrowRight />
			<@w.pageRef url="${request.contextPath}/basedata/dept/page" name="部门管理" />	
			<@w.arrowRight />
			<@w.pageRef url="${request.contextPath}/basedata/user/page" name="用户管理" />
		</div>
	</div>
	<div class="col-lg-6 col-md-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="well well-sm">
			显示教师列表及详细信息
		</div>
	</div>
</div>

<div class="row">
	<div class="col-sm-3">
		<@treemacro.deptForUnitInsetTree height="550" class="widget-color-blue2" click="onTreeClick">
			<div class="widget-header">
				<h4 class="widget-title lighter smaller">请选择部门</h4>
			</div>
		</@treemacro.deptForUnitInsetTree>
	</div>
	
	<div class="col-sm-9 listDiv">
	</div>
</div>
<!-- page specific plugin scripts -->
<script type="text/javascript">
	var indexDiv = 0;
	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		$(".listDiv").load("${request.contextPath}/basedata/unit/${unitId!}/teacher/page");
	});

	function onTreeClick(event, treeId, treeNode, clickFlag){
		if(treeNode.type == "unit"){
			$(".listDiv").load("${request.contextPath}/basedata/unit/" + treeNode.id + "/teacher/page");
		}
		else{
		 	$(".listDiv").load("${request.contextPath}/basedata/dept/" + treeNode.id + "/teacher/page");
		}
	}

</script>
