<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div class="filter filter-f16">
	<#--<div class="filter-item">-->
		<#--<span class="filter-name">考试名称：${examName!}</span>-->
	<#--</div>-->
</div>
<#if (findSByExamIdIn?exists && findSByExamIdIn?size>0)>
	<#if (subList?exists && subList?size>0)>
    <div class="filter-item">
		<@htmlcomponent.printToolBar container=".print"/>
    </div>
	</#if>
</#if>
			<div class="print">
				<h2 class="text-center">考试安排总表</h2>
                <div class="table-container-body" style="width:100%;overflow: auto;">
					<#if (findSByExamIdIn?exists && findSByExamIdIn?size>0)>
					<#if (subList?exists && subList?size>0)>
                	<table class="table table-bordered table-striped table-hover exaRoom-table2">
                        <thead>
							<tr>
								<th class="text-center" colspan="2">考场</th>
									<#list subList as sblist>
										<th class="text-center" colspan="${sblist.examSize!}">${sblist.daytime!}</th>
									</#list>
							</tr>
							<tr>
								<th class="text-center">编号</th>
								<th class="text-center">场地</th>
									<#list findSByExamIdIn as sb>
										<th class="text-center"><p>${sb.courseName!}</p>
										<#if isgk == '1'>
										<#if sb.gkSubType! == '0'>
											<#if sb.gkEndDate?exists>
												<p class="exaRoom-timesize">（学考：${(sb.gkStartDate?string('HH:mm'))?if_exists}-${(sb.gkEndDate?string('HH:mm'))?if_exists}&nbsp;选考：${(sb.startDate?string('HH:mm'))?if_exists}-${(sb.endDate?string('HH:mm'))?if_exists}）</p>
											<#else>
												<p class="exaRoom-timesize">（${(sb.startDate?string('HH:mm'))?if_exists}-${(sb.endDate?string('HH:mm'))?if_exists}）</p>
											</#if>
										<#elseif sb.gkSubType! == '1'>
										<p class="exaRoom-timesize">（选考：${(sb.startDate?string('HH:mm'))?if_exists}-${(sb.endDate?string('HH:mm'))?if_exists}）</p>
										<#elseif sb.gkSubType! == '2'>
										<p class="exaRoom-timesize">（学考：${(sb.gkStartDate?string('HH:mm'))?if_exists}-${(sb.gkEndDate?string('HH:mm'))?if_exists}）</p>
										</#if>
										<#else>
										<p class="exaRoom-timesize">（${(sb.startDate?string('HH:mm'))?if_exists}-${(sb.endDate?string('HH:mm'))?if_exists}）</p>
										</#if>
										</th>
									</#list>
							</tr>
						</thead>
						<tbody>
						<#if (examArrangeDtoList?exists && examArrangeDtoList?size>0)>
						<#list examArrangeDtoList as item>
							<tr>
								<td class="text-center">${item.examPlaceCode!}</td>
								<td class="text-center">${item.examPlaceName!}</td>
								<#list (item.invigilateList!) as list>
									<td class="text-center">${list!}</td>
								</#list>
							</tr>
						</#list>
						</#if>
							<tr>
								<td class="text-center" colspan="2">巡考老师</td>
								<#if (inspectorsTeacherList?exists && inspectorsTeacherList?size>0)>
									<#list inspectorsTeacherList as item>
										<td class="text-center">${item!}</td>
									</#list>
								</#if>
							</tr>
						</tbody>
					</table>
					</#if>
					</#if>
				</div>
			</div>
