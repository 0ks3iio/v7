<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<#--<a href="javascript:void(0);" class="page-back-btn" onclick="goChoiceAnalysis('${choiceId!}')"><i class="fa fa-arrow-left"></i> 返回</a>-->
<div class="box box-default">
	<div class="box-header">
		<div class="filter">
			<#--
				<div class="filter-item">
					<h3 class="box-title">选课结果统计明细</h3>
				</div>
			-->
			<div class="filter">
				<div class="filter-item">
					<@htmlcomponent.printToolBar container=".print"  printDirection='true' printUp=0 printLeft=0 printBottom=0 printRight=0/>
				</div>
			</div>
		</div>
	</div>
	<div class="box-body">
		<div class="table-container print">
			<div class="table-container-body">
				<table class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th>序号</th>
							<th>学科</th>
							<th>选课人数</th>
							<#if courseNameList?exists && courseNameList?size gt 0>
							<#list courseNameList as courseName>
							<th>${courseName!}</th>
							</#list>
							</#if>
						</tr>
					</thead>
					<tbody>
						<#if newConditionList3?exists && newConditionList3?size gt 0>
						<#list newConditionList3 as item>
						<tr>
						<td>${item_index+1}</td>
						<td>${item.subNames[0]!}、${item.subNames[1]!}、${item.subNames[2]!}</td>
							<#if item.limitSubject?default(false)==true>
							<td>不推荐</td>
								<#if courseNameList?exists && courseNameList?size gt 0>
								<#list courseNameList as courseName>
								<td></td>
								</#list>
								</#if>
							<#else>
								<td>${item.sumNum!}</td>
								<#if courseNameList?exists && courseNameList?size gt 0>
								<#list courseNameList as courseName>
									<td><#if item.subNames?seq_contains(courseName)>${item.sumNum!}</#if></td>
								</#list>
								</#if>
							</#if>
						</tr>
						</#list>
						</#if>
						<tr>
							<td></td>
							<td></td>
							<td>${sumNum?default(0)}</td>
							<#if courseNameList?exists && courseNameList?size gt 0 && courseNameCountMap?exists && courseNameCountMap?size gt 0>
							<#list courseNameList as courseName>
								<td>${courseNameCountMap['${courseName!}']?default(0)}</td>
							</#list>
							</#if>
						</tr>
					</tbody>
				</table>
			</div>		
		</div>
	</div>
</div>
<script>
	showBreadBack(goBackAnalysis,false,"选课结果统计明细");
	function goBackAnalysis(){
		goChoiceAnalysis('${choiceId!}');
	}
	
</script>