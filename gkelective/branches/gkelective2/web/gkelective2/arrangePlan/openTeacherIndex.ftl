<div class="box-body">
	<div class="box-header chosenSubjectHeaderClass" >
		<div class="filter filter-f16">
			<div class="filter-item">
				<span class="filter-name">选考类型：</span>
				<div class="filter-content">
					<select name="searchGkType" id="searchGkType" class="form-control" onchange="findByCondition()">
						<option value="" >全部</option>
					 	<option value="A" >选考</option>
					 	<option value="B" >学考</option>
					 </select> 
				</div>
			</div>
			<#if viewtype=="1">
				<div class="filter-item">
					<span class="filter-name">科目：</span>
					<div class="filter-content">
						<select name="searchCourseId" id="searchCourseId" class="form-control" onchange="findByCondition()">
							<option value="" >全部</option>
							<#if coursesList?exists && coursesList?size gt 0>
		 						<#list coursesList as course>
		 							<option value="${course.id!}" >${course.subjectName!}</option>
		 						</#list>
		 					</#if>
						 </select> 
					</div>
				</div>
			<#else>
				<div class="filter-item">
					<span class="filter-name">${PCKC!}：</span>
					<div class="filter-content">
						<select name="searchBatch" id="searchBatch" class="form-control" onchange="findByCondition()">
							<option value="" >全部</option>
							<#if batchSize?exists && (batchSize>0)>
		 						<#list 1..batchSize as bath>
		 							<option value="${bath}" >${PCKC!}${bath}</option>
		 						</#list>
		 					</#if>
						 </select> 
					</div>
				</div>
			</#if>
			<div class="filter-item">
				<span class="filter-name">安排情况：</span>
				<div class="filter-content">
					<select name="searchArrange" id="searchArrange" class="form-control" onchange="findByCondition()">
						<option value="">全部</option>
	 					<option value="0" >未安排</option>
					 </select> 
				</div>
			</div>
		</div>
	</div>
	<div class="myitemShowDivId" id="myitemShowDivId">
	</div>
</div>
<script>
	$(function(){
		findByCondition();
	})
	function findByCondition(){
	    var url = '${request.contextPath}/gkelective/${arrangeId!}/arrangePlan/showList/page?'+searchUrlValue('.chosenSubjectHeaderClass')+'&planId=${planId!}&viewtype=${viewtype}';
	    $("#myitemShowDivId").load(url);
	 }
</script>
