<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<#import "/fw/macro/treemacro.ftl" as treemacro>
<!-- ztree -->
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/ztree/js/jquery.ztree.exhide-3.5.min.js"></script>

<#if courseList?exists && courseList?size gt 0>
<style>
<#list courseList as course>
<#assign colo = course.bgColor!?split(",")>
<#if colo?size gt 0>
	.myteachertable .${course.subjectName}${arrayId!}{background:${colo[0]!};border-color:${colo[1]!}}
</#if>
</#list>
</style>
</#if>

<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">查看教师课表</h3>
		<div class="filter-item filter-item-right">
			<a href="javascript:" class="btn btn-blue" onclick="backTeacher()" >返回</a>
			<@htmlcomponent.printToolBar container=".print" printDirection='true' printUp=0 printLeft=0 printBottom=0 printRight=0/>
		</div>
	</div>
	<div class="box-body">
		<div class="tree-wrap">
		 	<h4>教师列表</h4>
		 	<div class="input-group">
			 	<input type="text" id="searchTeacherName" class="form-control" placeholder="输入教师姓名查询">
				<div class="input-group-btn btn-search">
					<button type="button" class="btn btn-default">
						<i class="fa fa-search"></i>
					</button>
				</div>
			</div>
		 	<@treemacro.findTree_1 id="subjectClassForDivideTree" class="" height="645" url="/newgkelective/${arrayId!}/tree/subjectTeacherForDivide/page" parameter="" click="onTreeClick" chooseTreeNodeId="${oneSubjectId!}_${teacherId!}"/>
		</div>
		<div id="teacherSchedule" style="min-height:720px;">
			<div class="no-data-container">
				<div class="no-data">
					<span class="no-data-img">
						<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
					</span>
					<div class="no-data-body">
						<h3>请选择要查看的教师</h3>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	$(function(){
		var teacherId='${teacherId!}';
		if(teacherId!=""){
			//树结构中默认选中
			findByTeacherId(teacherId);
		}
		$('#searchTeacherName').bind('keyup', function(event) {
			if (event.keyCode == "13") {
				//回车执行查询
				filter();
			}
		});
		$('.btn-search').on('click',function(){
			filter();
		});
	})
	
	
	var hiddenNodes=[]; //用于存储被隐藏的结点
	
	//过滤ztree显示数据
	function filter(){
		var valueObj=$("#searchTeacherName").val();
		if(valueObj!=""){
			valueObj=valueObj.trim();
		}
		//显示上次搜索后背隐藏的结点
	    var zTreeObj = $.fn.zTree.getZTreeObj("subjectClassForDivideTree");
	    if(hiddenNodes.length>0){
	    	zTreeObj.showNodes(hiddenNodes);
	    }
		if(valueObj==""){
			hiddenNodes=[];
			zTreeObj.expandAll(false);
			return;
		}
	    
	    //查找不符合条件的叶子节点
	    function filterFunc(node){
	        if(node.isParent || node.type != "teacher" || node.name.indexOf(valueObj)!=-1) {
	        	return false;
	        }
	        return true;
	    };
	    //获取不符合条件的叶子结点
	    hiddenNodes=zTreeObj.getNodesByFilter(filterFunc);
	    //隐藏不符合条件的叶子结点
	    zTreeObj.hideNodes(hiddenNodes);
	    zTreeObj.expandAll(true);
	};

	function onTreeClick(event, treeId, treeNode, clickFlag){
		  var id = treeNode.id;
		  var pid = treeNode.pId;
		  if(treeNode.type == "subject"){
		    	
		  }else{
		  	var arr=id.split("_");
		  	findByTeacherId(arr[1]);
		  }
	}
	function findByTeacherId(teacherId){
		var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/'+teacherId+'/teacher/page';
		$("#teacherSchedule").load(url);
	}
	function backTeacher(){
    	var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/teacher/page?subjectId=${subjectId!}&teacherName=${teacherName!}';
		$("#tableList").load(url);
	}
</script>