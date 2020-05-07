<title>权限管理</title>
<#import "/fw/macro/webmacro.ftl" as w>
<#import "/fw/macro/treemacro.ftl" as t>

<#-- ztree引入文件 -->
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>

<!-- ajax layout which only needs content area -->
<div class="row">
	<div class="col-lg-12 col-md-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="well well-sm">
			相关功能：
			<@w.pageRef url="${request.contextPath}/basedata/unit/page" name="单位管理" />
			<@w.arrowRight />
			<@w.pageRef url="${request.contextPath}/basedata/teacher/page" name="教师管理" />	
			<@w.arrowRight />
			<@w.pageRef url="${request.contextPath}/basedata/grade/page" name="年级管理" />
		</div>
	</div>
</div>

<div class="row right">
	<div class="col-sm-12">
		<@w.btn btnId="btn-add-permission" btnValue="新增" btnClass="fa-plus" />
		<@w.btn btnId="btn-add-teacher" btnValue="新增教师" btnClass="fa-user-plus" />
		<@w.btn btnId="btn-show-role" btnValue="用户组" btnClass="fa-user" />
	</div>
</div>

<div class="row">
	<div class="col-sm-3 col-lg-3">
		<div class="widget-box widget-color-blue2">
			<div class="widget-header">
				<h4 class="widget-title lighter smaller">请选择</h4>
			</div>
			<div class="widget-body">
				<div class="widget-main padding-8">
					<ul id="permissionTree" class="ztree"></ul>
				</div>
			</div>
		</div>
	</div>
	
	<div class="col-sm-9 col-lg-9 listDiv">
	</div>
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">

	var indexDiv = 0;
	$(window).bind("resize",function(){
		resizeLayer(indexDiv, 1000, 350);
	});

	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		//加载树

		$("#btn-add-permission").on("click", function(){
			indexDiv = layerDivUrl("${request.contextPath}/basedata/permission/add/page?parentId=" + currentTreeId + "&subsystemCode=" + currentSubsystemCode, {height:350});
		});
		$("#btn-add-teacher").on("click", function(){
			indexDiv = layerDivUrl("${request.contextPath}/basedata/teacher/add/page");
		});

		$("#btn-show-role").on("click", function(){
			reloadRoleTree();
		});
	});
	
	function reloadTree(){
		var setting = {
			check:{
				enable:true
			},
			data: {				
				simpleData: {
					enable: true
				}
			},
			callback: {
				onClick: onTreeClick
			}
		};
		$.ajax({
			url:"${request.contextPath}/basedata/permission/ztree",
			success:function(data){
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			$.fn.zTree.init($("#permissionTree"), setting, JSON.parse(jsonO.msg));
		 		}
			}
		});
	}
	
	function reloadRoleTree(){
		var setting = {
			check:{
				enable:true
			},
			data: {				
				simpleData: {
					enable: true
				}
			},
			callback: {
				onClick: onRoleTreeClick
			}
		};
		$.ajax({
			url:"${request.contextPath}/basedata/role/ztree",
			success:function(data){
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			$.fn.zTree.init($("#permissionTree"), setting, JSON.parse(jsonO.msg));
		 		}
			}
		});
	}
	
	var currentTreeId;
	var currentSubsystemCode;
	function onTreeClick(event, treeId, treeNode, clickFlag){
		if(treeNode.type == "system"){
			$(".listDiv").load("${request.contextPath}/basedata/permission/list/page?subSystemCode=" + treeNode.id);
			currentTreeId = "";
			currentSubsystemCode = treeNode.id;
		}
		else if(treeNode.type == "all"){
			$(".listDiv").load("${request.contextPath}/basedata/permission/list/page");
			currentTreeId = "";
		}
		else{
			$(".listDiv").load("${request.contextPath}/basedata/permission/list/page?parentId=" + treeNode.id);
			currentTreeId = treeNode.id;
		}		
	}

	function onRoleTreeClick(event, treeId, treeNode, clickFlag){
		if(treeNode.type == "user"){
			$(".listDiv").load("${request.contextPath}/basedata/user/" + treeNode.id + "/permission/list/page");
		}
		else if(treeNode.type == "role"){
			$(".listDiv").load("${request.contextPath}/basedata/role/" + treeNode.id + "/permission/list/page");
		}
	}
	
	reloadTree();
	
	$(".listDiv").load("${request.contextPath}/basedata/permission/list/page");
</script>
