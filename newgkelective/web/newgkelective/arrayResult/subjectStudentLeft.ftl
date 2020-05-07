<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<#import "/fw/macro/treemacro.ftl" as treemacro>
<!-- ztree -->
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title"><#if openType?default('')=='1'>查看学生<#else>查看课表</#if></h3>
		<div class="filter-item filter-item-right">
		<a href="javascript:" class="btn btn-blue" onclick="backSubjectClassResult()" >返回</a>
		<@htmlcomponent.printToolBar container=".print"  printDirection='true' printUp=0 printLeft=0 printBottom=0 printRight=0/>
		</div>
	</div>
	<div class="box-body">
		<div class="tree-wrap">
		 	<h4>班级列表</h4>
		 	<@treemacro.findTree_1 id="subjectClassForDivideTree" class="" height="645" url="/newgkelective/${arrayId!}/tree/subjectClazzForDivide/page" parameter="" click="onTreeClick" chooseTreeNodeId="${classId!}"/>
		</div>
		<div id="classSchedule" style="min-height:680px;">
			<div class="no-data-container">
				<div class="no-data">
					<span class="no-data-img">
						<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
					</span>
					<div class="no-data-body">
						<h3>请选择要查看的班级</h3>
					</div>
				</div>
			</div>
		</div>	

		
	</div>
</div>

<script>
	function backSubjectClassResult(){
		var arrayId = '${arrayId!}';
	    var url = '${request.contextPath}/newgkelective/'+arrayId+'/arrayResult/subjectClassResult/tabHead/page';
		$("#tableList").load(url);
	}
	
	$(function(){
		var clazzId='${classId!}';
		if(clazzId!=""){
			//树结构中默认选中
			searchFindListByClassId(clazzId);
		}
	})
	function onTreeClick(event, treeId, treeNode, clickFlag){
	  var id = treeNode.id;
	  var pid = treeNode.pId
	  if(treeNode.type == "subject"){
	    	//$("#classSchedule").html(makeNoDate());
	  }else{
	  		searchFindListByClassId(id);
	  }
	}
	function makeNoDate(){
		var hh='<div class="no-data-container"><div class="no-data">'
					+'<span class="no-data-img"><img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt=""></span>'
					+'<div class="no-data-body"><h3>请选择要查看的班级</h3></div></div></div>';
		return hh;
	}
	
	function searchFindListByClassId(clazzId){
		//debugger;
		<#if openType?default('')=='1'>
			 var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/subjectStudentResult?classId='+clazzId;
		<#else>
			 var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/subjectTimetableResult/getDetailData?classType=2&classId='+clazzId;
		</#if>
		$("#classSchedule").load(encodeURI(url));
	}
	
</script>
