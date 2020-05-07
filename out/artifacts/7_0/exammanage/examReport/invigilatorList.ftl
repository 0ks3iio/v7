<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div id="ee" class="tab-pane fade active in">
	<div class="filter filter-f16">
		<#--<div class="filter-item">-->
			<#--<span class="filter-name">考试名称：${examName!}</span>-->
		<#--</div>-->
	</div>
<#if (invigilatorList?exists && invigilatorList?size>0)>
    <div class="filter-item">
		<@htmlcomponent.printToolBar container=".print"/>
    </div>
</#if>
	<!-- PAGE CONTENT BEGINS -->
	<div class="row print">
		<div class="col-sm-12">
		<#if (invigilatorList?exists && invigilatorList?size>0)>
		<div class="box box-default">
			<div class="box-body clearfix">
				<h2 class="text-center">监考老师安排</h2>
                <table class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th class="text-center" style="width:5%">序列</th>
							<th class="text-center" style="width:15%">监考老师</th>
							<th class="text-center" style="width:15%">监考科目</th>
							<th class="text-center" style="width:15%">考试日期</th>
							<th class="text-center" style="width:20%">考试时间段</th>
							<th class="text-center" style="width:10%">考场编号</th>
							<th class="text-center" style="width:20%">考试场地</th>
						</tr>
					</thead>
					<tbody>
			<#list invigilatorList as item>
						<#list (item.teachReportList!) as teach>
						<tr>
							<#if (teach_index) == 0>
							<td class="text-center" rowspan="${item.teachReportList?size}">${item_index+1}</td>
							<td class="text-center" rowspan="${item.teachReportList?size}">${teach.teacherNames!}</td>
							</#if>
							<td class="text-center">${teach.subjectName!}</td>
							<td class="text-center">${(teach.startTime?string('yyyy-MM-dd'))?if_exists}</td>
							<td class="text-center">${(teach.startTime?string('HH:mm'))?if_exists}-${(teach.endTime?string('HH:mm'))?if_exists}</td>
							<td class="text-center">${teach.examPlaceCode!}</td>
							<td class="text-center">${teach.examPlaceName!}</td>
						</tr>
						</#list>
					</tbody>
			</#list>
				</table>
			</div>
		</div>
		<#else>
			<div class="box box-default">
				<div class="box-body clearfix">
					<h2 class="text-center">监考老师安排</h2>
				</div>
			</div>
		</#if>
		</div>
	</div>
</div>