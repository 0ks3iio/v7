<div class="box box-lightblue box-moral">
					<div class="box-header">
						<h4 class="box-title">昨日扣分寝室</h4>
						<div class="flags"></div>
					</div>
					<div class="box-body no-padding" style="height:427px;">
						<table class="table table-bedroom-warn">
							<tbody>
							<#if dormscoreList?exists && dormscoreList?size gt 0>
							<#list dormscoreList as item>
								<tr>
									<td>
										${item[0]!}
									</td>
									<td>
										<b class="color-red">${item[1]!}</b>
										<p class="table-txt">备注：${item[2]!}</p>
									</td>
								</tr>
							</#list>
							</#if>								
							</tbody>
						</table>
					</div>
				</div>