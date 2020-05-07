<div class="box box-default" >
	<div class="chosenHeaderClass">
		<div class="filter filter-f16">
			<div class="filter-item">
		        <span class="filter-name">视图类型：</span>
		        <div class="filter-content">
		        	<label class="pos-rel">
						<input type="radio" class="wp form-control form-radio" name="searchViewTypeRedio" value="1" checked="checked" onclick="findByCondition()">
						<span class="lbl"> 按${PCKC!}查看</span>
					</label>
					<label class="pos-rel">
						<input type="radio" class="wp form-control form-radio" name="searchViewTypeRedio" value="2" onclick="findByCondition()">
						<span class="lbl"> 按科目查看</span>
					</label>
					<span class="lbl"> &nbsp;&nbsp;&nbsp;&nbsp;</span>
		        </div>
		    </div>
		</div>
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
			<div class="filter-item">
				<span class="filter-name">${PCKC!}：</span>
				<div class="filter-content">
					<select name="searchBatch" id="searchBatch" class="form-control" onchange="findByCondition()">
						<option value="" >全部</option>
					 	<#list 1..batchSize?default(1) as bs>
					 		<option value="${bs}" >${PCKC!}${bs}</option>
					 	</#list>
					 </select> 
				</div>
			</div>
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
			<div class="filter-item">
				<span class="filter-name">安排情况：</span>
				<div class="filter-content">
					<select name="searchArrange" id="searchArrange" class="form-control" onchange="findByCondition()">
						<option value="">全部</option>
	 					<option value="0">未完成安排</option>
					 </select> 
				</div>
			</div>
			
		</div>
	</div>
	<div class="itemShowDivId ">
	</div>
</div>
<script>
	$(function(){
		findByCondition();
	});
	function findByCondition(){
	    var url = '${request.contextPath}/gkelective/${arrangeId!}/arrangePlan/resultList/page?'+searchUrlValue('.chosenHeaderClass')+'&planId=${planId!}';
	    $(".itemShowDivId").load(url);
	 }
</script>
