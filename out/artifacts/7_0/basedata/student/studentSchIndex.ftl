<title>学生管理</title>
<#import "/fw/macro/webmacro.ftl" as w>
<#import "/fw/macro/treemacro.ftl" as treemacro>
<link rel="stylesheet" href="${request.contextPath}/static/ace/css/ui.jqgrid.css" />
<script src="${request.contextPath}/static/ace/js/jqGrid/jquery.jqGrid.src.js"></script>
<script src="${request.contextPath}/static/ace/js/jqGrid/i18n/grid.locale-cn.js"></script>
<#-- ztree引入文件 -->
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<#-- sweetalert引入文件  -->
<link rel="stylesheet" href="${request.contextPath}/static/sweetalert/sweetalert.css" />
<script src="${request.contextPath}/static/sweetalert/sweetalert.min.js"></script>
<!-- ajax layout which only needs content area -->
<div class="row">
	<div class="col-lg-12 col-md-12">
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
</div>


<div class="row">
	<div class="col-sm-3">
		<@treemacro.gradeClassForSchoolInsetTree height="550" click="onTreeClick">
			<div class="widget-header" style="padding:0;margin:0;border-color: #5090C1;">
				<input type="text" id="stuName"  name="stuName" class="form-control" placeholder="姓名--回车查询">
			</div>
		</@treemacro.gradeClassForSchoolInsetTree>
		
	</div>
	<div class="col-sm-9 listDiv">
	</div>
</div>
<!-- page specific plugin scripts -->
<script type="text/javascript">
	var indexDiv = 0;
	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		$("#stuName").bind("keypress",function(event){
			if(event.keyCode=='13'){
				doSearch();
			}
		});
		$(".listDiv").load("${request.contextPath}/basedata/student/stulist/page?unitId=${unitId!}&schoolId=${schoolId!}");
	});
	
	var classId;
	var gradeId;
	function onTreeClick(event, treeId, treeNode, clickFlag){
		if(treeNode.type == "class"){
			classId = treeNode.id;
			var stuName = $("#stuName").val();	
			$(".listDiv").load("${request.contextPath}/basedata/student/stulist/page?unitId=${unitId!}&schoolId=${schoolId!}&classId="+treeNode.id+"&stuName="+stuName);
			currentTreeId = "";
			currentSubsystemCode = treeNode.id;
		}
		else if(treeNode.type == 'grade'){
			gradeId = treeNode.id;
			classId="";
			var stuName = $("#stuName").val();	
			$(".listDiv").load("${request.contextPath}/basedata/student/stulist/page?unitId=${unitId!}&schoolId=${schoolId!}&gradeId="+treeNode.id+"&stuName="+stuName);
		}
		else if(treeNode.type == 'school'){
			searchSchoolId = treeNode.id;
		}
	}
	
	function doSearch(){
		if(typeof(classId)=="undefined"){
			classId="";
		}
		if(typeof(gradeId)=="undefined"){
			gradeId="";
		}
		
		var stuName = $("#stuName").val();	
		$(".listDiv").load(encodeURI("${request.contextPath}/basedata/student/stulist/page?unitId=${unitId!}&schoolId=${schoolId!}&gradeId="+gradeId+"&classId="+classId+"&stuName="+stuName));
	}
</script>
