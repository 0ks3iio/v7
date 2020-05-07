<div class="table-container">
	<div class="table-container-body table-scroll-x">
		<table class="table table-striped table-bordered table-hover">
				<#if rankDtos?exists && rankDtos?size gt 0>
				<thead>
					<tr role="row" style="height: 0px;">
						<th rowspan="2" class="sorting_disabled" style="width: 0px;">班级&emsp;</th>
						<#list rankDtos as tpl>
			        		<th>${tpl.name!}</th>
			        	</#list>
					</tr>
				</thead>
				<tbody>
				<#if classDtos?exists>
					<#list classDtos as tpl>
						<tr role="row" class="odd" style="height: 0px;">
							<td>
					        	${tpl.className!}
					        </td>
							<#list rankDtos as rank>
								
								<#if teaCheckMap['${tpl.classId!}_${rank.asessRankId!}']?exists>
									<#assign applyInfo = teaCheckMap['${tpl.classId!}_${rank.asessRankId!}']/>
									<td>
										<#if rank.lineType=='1'>
							       			${applyInfo.lineScoreStr!}
							       		<#else>
							       			${applyInfo.lineRank!}
							       		</#if>
						        	</td>
						        <#else>
						        	<td></td>
								</#if>
									
							</#list>
						</tr>
					</#list>
				</#if>
				</tbody>
			<#else>
				<div class="no-data-container">
					<div class="no-data">
						<span class="no-data-img">
							<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
						</span>
						<div class="no-data-body">
							<p class="no-data-txt">暂无记录或未设置AB分层</p>
						</div>
					</div>
				</div>
			</#if>
		</table>
	</div>
</div>
