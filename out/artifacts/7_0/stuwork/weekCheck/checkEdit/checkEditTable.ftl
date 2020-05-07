<#if blackAdmin == '1'>
<a href="javascript:" class="page-back-btn gotoRoleUserIndex" onclick="blackAdmin()"><i class="fa fa-arrow-left"></i> 返回</a>
</#if>
						<div class="box box-default">
							<div class="box-body">
								<table class="table table-striped">
									<thead>
										<tr>
										<#if roleType == '03'>
											<th>周次</th>
										</#if>
											<th>值周日期</th>
											<th>值周范围</th>
											<th>录分情况</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
									<#if teables?exists && teables?size gt 0>
									<#list teables as tab>
										<tr>
										<#if roleType == '03'>
											<td>第${tab.week}周</td>
										</#if>
											<td>
												<#if roleType == '02'>
												${tab.weekDate?string('yyyy-MM-dd')}
												<#elseif roleType == '03'>
												${tab.beginDate?string('yyyy-MM-dd')} 至 ${tab.endDate?string('yyyy-MM-dd')}
												</#if>
											</td>
											<td>
											${tab.sectionStr!}
											</td>
											<td>
											<#if tab.state == 0>
											<span class="label label-grey">未开始</span>
											<#elseif tab.state == 1>
											<span class="label label-info">未提交</span>
											<#elseif tab.state == 2>
											<span class="label label-blue">已提交</span>
											</#if>
											</td>
											<td>
											<#if tab.state != 0>
											<#if roleType == '02'>
											<a href="javascript:void(0)" onclick="toEdit('${tab.weekDate?string('yyyy-MM-dd')}','${tab.sec!}');" class="color-lightblue">修改</a>
											<#elseif roleType == '03'>
											<a href="javascript:void(0)" onclick="toEditStu('${tab.beginDate?string('yyyy-MM-dd')}','${tab.endDate?string('yyyy-MM-dd')}','${tab.sec}');" class="color-lightblue">修改</a>
											</#if>
											</#if>
											</td>
										</tr>
									</#list>
									<#else>
									<tr>
									<td colspan="5" align="center">无数据</td>
									</tr>
									</#if>
									</tbody>
								</table>
							</div>
						</div>
					</div><!-- /.model-div -->
<script>
function toEdit(dutyDate,sections){
	$(".model-div").load("${request.contextPath}/stuwork/checkweek/checkList/page?blackAdmin=${blackAdmin!}&blackTable=1&roleType=${roleType!}&sections="+sections+"&dutyDate="+dutyDate);
}
function toEditStu(beginDate,endDate,sections){
	$(".model-div").load("${request.contextPath}/stuwork/checkweek/checkList/page?blackAdmin=${blackAdmin!}&blackTable=1&roleType=${roleType!}&sections="+sections+"&beginDate="+beginDate+"&endDate="+endDate);
}
function blackAdmin(){
	$(".model-div").load("${request.contextPath}/stuwork/checkweek/checkEdit/page?blackAdmin=${blackAdmin!}&blackTable=${blackTable!}");
}
</script>