<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<script src="${request.contextPath}/static/components/jquery-ui/jquery-ui.min.js"></script>
<#--<a href="javascript:void(0);" onclick="gobackArrayLesson()" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>-->
<div class="box box-default">
	<div class="box-header">
		学生冲突：${stuList?size}
	</div>
	<div class="box-body print" id="balanceClassListid">
		<#if stuList?exists && (stuList?size gt 0)>
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">学生：</span>
				<div class="filter-content">
					<select id="studentId" onChange="chooseSame()">
						<option value="">---请选择---</option>
						<#list stuList as stu>
							<option value="${stu.id!}">${stu.studentName!}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<#-- a class="btn btn-blue" onclick="chooseAllSame();">一键处理</a>
				注：一键处理：移动冲突学生到其他班级，且班级人数不超过${classNum!}人 -->
				<#--<a class="btn btn-blue" onclick="changeStudentId();">查看学生冲突</a>-->
				<#-- <a class="btn btn-blue" onclick="balanceClassList();">平衡班级人数</a>-->
			</div>
		</div>
		<#else>
			学生时间没有冲突
			<div class="filter-item">
				<a class="btn btn-blue" onclick="balanceClassList();">平衡班级人数</a>
			</div>
		</#if>
		<div id="chooseClassIds">
		</div>
	</div>

</div>
<script>

	showBreadBack(gobackArrayLesson,false,"学生冲突");
	
	function changeStudentId(){
		var studentId=$("#studentId").val();
		if(studentId==""){
			$("#chooseClassIds").html("");
		}else{
			var url='${request.contextPath}/newgkelective/${arrayId!}/arrayStudent/conflictList/page?studentId='+studentId;
			$("#chooseClassIds").load(url);
		}
	}
	function gobackArrayLesson(){
		var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayLesson/conflictIndex/page';
		$("#showList").load(url);
	}
	function chooseSame(){
		var studentId=$("#studentId").val();
		if(studentId==""){
			$("#chooseClassIds").html("");
		}else{
			var url='${request.contextPath}/newgkelective/${arrayId!}/arrayStudent/sameList/page?studentId='+studentId;
			$("#chooseClassIds").load(url);
		}
	}
	var isSubmitAll=false;
	function chooseAllSame(){
		if(isSubmitAll){
			return;
		}
		isSubmitAll=true;
		var url='${request.contextPath}/newgkelective/${arrayId!}/arrayStudent/saveAllBySameStudent';
		var ii = layer.load();
		$.ajax({
		    url:url,
			data: {},
			dataType : 'json',
		    success:function(data) {
		    	var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
		 			isSubmitAll = false;
		 		}else{
		 			layer.closeAll();
		 			layer.msg(jsonO.msg, {offset: 't',time: 2000});
					var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayStudent/conflictIndex/page';
					$("#showList").load(url);
    			}
		    	layer.close(ii);
		    }
		});
	}
	
	function balanceClassList(){
		var url='${request.contextPath}/newgkelective/${arrayId!}/arrayConflict/subjectBalanceIndex?divideId=${divideId!}';
		$("#balanceClassListid").load(url);
	}

</script>
