<div class="box box-default">
	<div class="box-header">
		<div class="curricula-header-title">
			${newGkChoice.choiceName!}
		</div>
		<div class="curricula-header-sort">
			<span>选课数：${newGkChoice.chooseNum!'3'}</span>
			<span>选课时间：${newGkChoice.startTime!}至 ${newGkChoice.endTime!} </span>
		</div>
	</div>
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li class="active" role="presentation"><a href="javascript:void(0)" onClick="itemShowList()" role="tab" data-toggle="tab">学生选课</a></li>
			<#if isOpen?default(false)>
			<li role="presentation"><a href="javascript:void(0)" onClick="itemShowCount()" role="tab" data-toggle="tab">选课统计</a></li>
			</#if>
		</ul>
		<div class="tab-content">
			<div id="itemShowDivId" role="tabpanel">
			</div>
		</div>
	</div>
</div>
<script>
	$(function(){
		itemShowList();
		showBreadBack(goBack,true,"学生选课");
	})
	function goBack(){
		var url =  '${request.contextPath}/newgkelective/stuChooseSubject/head/page';
		$("#showList").load(url);
	}
	function itemShowList(){
	    var url = '${request.contextPath}/newgkelective/stuChooseSubject/list/page?choiceId=${newGkChoice.id!}';
        $("#itemShowDivId").load(url);
	}
	
	<#if isOpen?default(false)>
	function itemShowCount(){
		var url = '${request.contextPath}/newgkelective/stuChooseSubject/count/page?choiceId=${newGkChoice.id!}';
        $("#itemShowDivId").load(url);
	}
	</#if>
</script>