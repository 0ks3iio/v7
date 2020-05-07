<div class="box box-lightblue box-moral">
					<div class="box-header">
						<h4 class="box-title">昨日班级德育</h4>
						<div class="flags"><#if showWs> <span class="flag flag-health"></span></#if> <#if showJl><span class="flag flag-discipline"></span></#if></div>
					</div>
					<div class="box-body no-padding" style="height:427px;">
						<table class="table table-moral">
							<tbody>
							<#if weekCheak?exists && weekCheak?size gt 0>
							<#list weekCheak as item>
							    <#if item[0] == '1'>
								<tr>
									<td>
										<span class="icon icon-broom"></span>值周卫生
									</td>
									<td>
										<b class="color-red">${item[1]!}</b>
										<p class="table-txt">备注：${item[2]!}</p>
									</td>
								</tr>
								<#else>
								<tr>
									<td>
										<span class="icon icon-broom"></span>值周纪律
									</td>
									<td>
										<b class="color-red">${item[1]!}</b>
										<p class="table-txt">备注：${item[2]!}</p>
									</td>
								</tr>
								</#if>
							</#list>
							</#if>
							
							<#if courseRecord?exists && courseRecord?size gt 0>
							<#list courseRecord as item>
							    <#if item[0] == '3'>
							    <tr>
									<td>
										<span class="icon icon-star"></span>上课日志
									</td>
									<td>
										<b class="color-green">+${item[1]!}</b>
										<p class="table-txt">备注：${item[2]!}</p>
									</td>
								</tr>
							    <#else>
							    <tr>
									<td>
										<span class="icon icon-star"></span>晚自习日志
									</td>
									<td>
										<b class="color-green">+${item[1]!}</b>
										<p class="table-txt">备注：${item[2]!}</p>
									</td>
								</tr>
							    </#if>
							</#list>
							</#if>
			                <#if classResult?exists && classResult?size gt 0>
							<#list classResult as item>
								<tr>
									<td>
										<span class="icon icon-star"></span>寝室日志
									</td>
									<td>
										<b class="color-red">${item[1]!}</b>
										<p class="table-txt">备注：${item[2]!}</p>
									</td>
								</tr>
							</#list>
							</#if>
							<#if classRemind?exists && classRemind?size gt 0>
							<#list classRemind as item>
								<tr>
									<td>
										<span class="icon icon-star"></span>提醒事项
									</td>
									<td>
										<b class="color-red">${item[1]!}</b>
										<p class="table-txt">${item[2]!}</p>
									</td>
								</tr>
							</#list>
							</#if>
							</tbody>
						</table>
					</div>
				</div>