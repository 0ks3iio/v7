<script src="${request.contextPath}/static/echarts/echarts.min.js"></script>
<#import "/fw/macro/chartstructure.ftl" as chartstructure>
<div class="tab-content" id="resultListDiv">
	<div id="aa" class="tab-pane active" role="tabpanel">
		<div class="row">
			<div class="col-sm-6">
				<h4 class="noprint">各科老师统计图</h4>
				<@chartstructure.pieChart divClass="noprint" loadingDivId="oneChart" divStyle="height:400px;border:1px solid #ddd;margin-bottom:20px;" jsonStringData="${jsonStringData1!}" isShowLegend=false isShowToolbox=false/>
			</div>
			<div class="col-sm-6">
				<h4 class="noprint">各${PCKC!}教室统计图</h4>
				<@chartstructure.pieChart divClass="noprint" loadingDivId="twoChart" divStyle="height:400px;border:1px solid #ddd;margin-bottom:20px;" jsonStringData="${jsonStringData2!}" isShowLegend=false isShowToolbox=false/>
			</div>
		</div>
	</div>
</div>
<div>
	<div class="chosenHeaderClass">
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
				<span class="filter-name">教师：</span>
				<div class="filter-content">
					<select name="searchTeacherId" vtype="selectOne" id="searchTeacherId" class="form-control" data-placeholder="选择教师" onchange="findByCondition()">
						<option value="" >全部</option>
						<#if teacherList?exists && teacherList?size gt 0>
	 						<#list teacherList as teacher>
	 							<option value="${teacher.id!}" >${teacher.teacherName!}</option>
	 						</#list>
	 					</#if>
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
		//初始化多选控件
		var viewContent={
			'allow_single_deselect':'false',//是否可清除，第一个option的text必须为空才能使用
			'select_readonly':'false',//是否只读
			'width' : '220px',//输入框的宽度
			'results_height' : '200px'//下拉选择的高度
		}
		initChosenOne(".chosenHeaderClass","",viewContent);
		
		findByCondition();
	});
	function findByCondition(){
	    var url = '${request.contextPath}/gkelective/${arrangeId!}/arrangePlan/resultPieList/page?'+searchUrlValue('.chosenHeaderClass')+'&planId=${planId!}';
	    $(".itemShowDivId").load(url);
	 }
</script>
