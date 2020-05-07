<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<script src="${request.contextPath}/static/components/jquery-ui/jquery-ui.min.js"></script>
<#--<a href="javascript:void(0);" onclick="goback()" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>-->
<div class="box box-default">
	<div class="conflict-tip">
		<i class="wpfont icon-exclamation-round"></i>
		当前排课冲突数：<em>${conNumber?default(0)}</em>
		<#--
		<#if conNumber?default(0)!=0>
			<a class="btn btn-blue" id="gostudentBtn" onclick="studentSet();">调整学生</a>
		</#if>
		-->
		<a class="btn btn-blue<#if conNumber?default(0)==0>disabled hide</#if>" id="goContinueBtn" onclick="showResult()">查看课表</a>
		<a class="btn btn-blue<#if conNumber?default(0)!=0>disabled hide</#if>" id="goContinueBtn" onclick="goContinue()">继续排课</a>
	</div>
	<input type="hidden" id="subjectId">
	<input type="hidden" id="classId">
	<div class="box-body">
	<#if conNumber?default(0)!=0>
		<p>提示：将以下课程移动到课表中空白的地方会降低冲突数(部分空白单元格处也可能有冲突，请多调整几次。)</p>
		<div class="clearfix">
			<div class="box box-primary box-conflict">
				<div class="box-body">
					<ul id="tree" class="ztree"></ul>
				</div>
			</div>
			<div class="table-container" id="conflictList">
					
			</div>
		</div>
		<#else>
		<p class="tip tip-big tip-grey" align="center">暂无冲突</p>
	</#if>
	</div>
</div>
<script>
	
	showBreadBack(goback,false,"排课冲突");
	function getConflictList(){
		var classId=$("#classId").val();
		var subjectId=$("#subjectId").val();
		$("#conflictList").load("${request.contextPath}/newgkelective/${arrayId!}/arrayLesson/conflictList/page?subjectId="+subjectId+"&classId="+classId);
	}
	function studentSet(){
		var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayStudent/conflictIndex/page';
		$("#showList").load(url);
	}
	
	
	$(function(){
		function zTreeFun(event, treeId, treeNode, clickFlag){
			if(treeNode.type == "class"){
				var classId = treeNode.id;
				var subjectId=treeNode.pId;
				$("#classId").val(classId);
				$("#subjectId").val(subjectId);
				getConflictList();
			}
		}
		var jsonO = JSON.parse('${treeData!}');
		var setting = {
			check:{
				enable:false
			},
			data: {				
				simpleData: {
					enable: true,
					idKey: "id",
					pIdKey: "pId"
				}
			},
			callback: {
				onClick: zTreeFun
			}
		};
		function zTreeFind(){
 			$.fn.zTree.init($("#tree"), setting, jsonO);
		}
		zTreeFind();	
	
	})
	
	var isGoContinue=false;
	function goContinue(){
		if($("#goContinueBtn").hasClass("disabled")){
			return;
		}
		if(isGoContinue){
			return;
		}
		isGoContinue=true;
		
		//安排老师
		$.ajax({
			url:"${request.contextPath}/newgkelective/${arrayId!}/arrayLesson/autoArrangeSameClass",
			dataType: "json",
			success: function(data){
				var jsonO = data;
		 		if(!jsonO.success){
		 			isGoContinue=false;
		 			layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
		 			return;
		 		}else{
		 			//进入
					goTeacherSolve();
				}
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
		
	}
	
	function goTeacherSolve(){
		var url =  '${request.contextPath}/newgkelective/${arrayId!}/array/teacher/page?gradeId=${gradeId}';
		$("#showList").load(url);
	}
	function goback(){
		<#if arrangeType?default('01')=='01'>
			var url =  '${request.contextPath}/newgkelective/${gradeId}/goArrange/index/page';
		<#else>
			var url =  '${request.contextPath}/newgkelective/xzb/index/page';
		</#if>
		$("#showList").load(url);
	}
	
	function showResult(){
		var url ='${request.contextPath}/newgkelective/${arrayId!}/arrayResult/pageIndex';
		url=url+'?type='+1;
		$("#showList").load(url);
	}
</script>
