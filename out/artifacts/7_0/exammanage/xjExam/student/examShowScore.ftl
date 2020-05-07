<div class="main-content">
		<div class="main-content-inner">
			<div class="page-content">
				<div class="box box-default">
					<div class="box-header">
						<h4 class="box-title">成绩通知单</h4>
					</div>
					<div class="box-body">
						<div class="exam-notice-paper stuScore">
							<h2 class="exam-notice-paper-title">${title!}通知单</h2>
							<ul class="exam-notice-paper-list exam-notice-paper-list-1of4">
								<li>考生姓名：${examInfos.stuName!}</li>
								<li>准考证号：${examInfos.admission!}</li>
								<li>年级：${examInfos.gradeName!}</li>
								<li>班级：${examInfos.className!}</li>
							</ul>
							<div style="overflow-x:auto;width:100%;">
								<table class="table table-bordered table-striped text-center">
									<thead>
										<tr>
										    <#if columnKeys?exists && columnKeys?size gt 0>
									          	<#list columnKeys as key>
										          	<th class="text-center">${ecMap[key]!}</th>
									      	    </#list> 
								      	    </#if> 
										</tr>
									</thead>
									<tbody>
									    <tr>
										     <#if columnKeys?exists && columnKeys?size gt 0>
									          	<#list columnKeys as key>
										          	<td class="text-center">${scoreMap[key]!}</td>
									      	    </#list> 
									  	     <#else>
												<td  colspan="88" align="center">
												暂无成绩
												</td>
									         </#if>
								        </tr>
									</tbody>
								</table>
							</div>
							<h4 class="exam-notice-paper-warning">注意事项</h4>
			  				<#if careful?exists && careful?size gt 0>
								   <#list careful as care>
								     <p>${care!}</p>
								   </#list>
							</#if>
							<p class="text-right">学军中学考试中心</p>
						</div>
					</div>
				</div>
			</div><!-- /.page-content -->
		</div>
	</div><!-- /.main-content -->
</div><!-- /.main-container -->
