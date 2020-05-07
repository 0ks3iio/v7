<#import "/fw/macro/htmlcomponent.ftl" as htmlcomponent>
<#import "/fw/macro/treemacro.ftl" as treemacro>
<!-- ztree -->
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/ztree/js/jquery.ztree.exhide-3.5.min.js"></script>


<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">查看课表</h3>
		<div class="filter-item filter-item-right">
			<a href="javascript:" class="btn btn-blue" onclick="goBackStudentClass()" >返回</a>
			<@htmlcomponent.printToolBar container=".print" printDirection='true' printUp=0 printLeft=0 printBottom=0 printRight=0/>
		</div>
	</div>
	<div class="box-body">
		<div class="tree-wrap">
		 <h4>学生列表</h4>
		 <div class="input-group">
			<input type="text" id="searchStudentName" class="form-control" placeholder="输入学生姓名查询">
			<div class="input-group-btn btn-search">
				<button type="button" class="btn btn-default">
					<i class="fa fa-search"></i>
				</button>
			</div>
		</div>
		 <@treemacro.findTree_1 id="clazzStudentForDivideTree" class="" height="645" url="/newgkelective/${arrayId!}/tree/clazzStudentForDivide/page" parameter="" click="onTreeClick" chooseTreeNodeId="${studentId!}"/>
		</div>
		<div id="studentSchedule"  style="min-height:680px;">
			<div class="no-data-container" >
				<div class="no-data">
					<span class="no-data-img">
						<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
					</span>
					<div class="no-data-body">
						<h3>请选择要查看的学生</h3>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script>
function goBackStudentClass(){
    var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/studentClassResultHead';
	$("#tableList").load(url);
}
$(function(){
	var studentId='${studentId!}';
	var studentName='${studentName!}';
	if(studentId!=""){
		//树结构中默认选中
		searchFindListByStudentId(studentId,studentName);
	}
	$('#searchStudentName').bind('keyup', function(event) {
		if (event.keyCode == "13") {
			//回车执行查询
			filter();
		}
	});
	$('.btn-search').on('click',function(){
		filter();
	});
})
function toStudentTreeByName(){
	var searchStudentName=$("#searchStudentName").val();
	var url = '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/studentClassResultDetailLeft?searchStudentName='+searchStudentName;
	$("#tableList").load(encodeURI(url));
}

	var hiddenNodes=[]; //用于存储被隐藏的结点
	
	//过滤ztree显示数据
	function filter(){
		var valueObj=$("#searchStudentName").val();
		if(valueObj!=""){
			valueObj=valueObj.trim();
		}
		//显示上次搜索后背隐藏的结点
	    var zTreeObj = $.fn.zTree.getZTreeObj("clazzStudentForDivideTree");
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
	        if(node.isParent || node.type != "student" || node.name.indexOf(valueObj)!=-1) {
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
  var idName=treeNode.name;
  if(treeNode.type == "clazz"){
    	
  }else{
  		searchFindListByStudentId(id,idName);
  }
}
function searchFindListByStudentId(studentId,studentName){
    var url = '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/studentClassResultDetail?studentId='+studentId+'&studentName='+studentName;
	$("#studentSchedule").load(encodeURI(url));
}
</script>