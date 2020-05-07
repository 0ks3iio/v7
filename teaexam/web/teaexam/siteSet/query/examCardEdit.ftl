<div class="layer-content tree">
	<h3 class="text-center">${examName!}</h3>
	<h2 class="text-center">准 考 证</h2>
	<table width="100%">
		<tr>
			<td width="70" class="text-right">考号：</td>
			<td>${cardNo!}</td>
			<td rowspan="5"><img width="71" height="99" src="${avatarUrl!}"></td>
		</tr>
		<tr>
			<td class="text-right">姓名：</td>
			<td>${teacherName!}</td>
		</tr>
		<tr>
			<td class="text-right">单位：</td>
			<td>${unitName!}</td>
		</tr>
		<tr>
			<td class="text-right">身份证号：</td>
			<td>${idCard!}</td>
		</tr>
		<tr>
			<td class="text-right">报考科目：</td>
			<td>
			<#if subList?exists && subList?size gt 0>
			    <#list subList as item>
				    <span class="mr20 inline-block">${item.subjectName!}（
                    <#if item.section == 1>
                    小学
                    <#elseif item.section == 0>
					学前
                    <#elseif item.section == 2>
                                                   初中
                    <#elseif item.section == 3> 
                                                  高中 
                    </#if>                          
                                                 ）</span>
				</#list>
			</#if>
			</td>
		</tr>
	</table>
	<div>
		<h4><b>日程表</b></h4>
		<div class="table-container">
			<div class="table-container-body">
				<table class="table table-bordered table-striped table-hover">
					<tbody>
					<#if regList?exists && regList?size gt 0>
					    <#list regList as reg>
						<tr>
						    <td>${reg.startTime?string('yyyy-MM-dd')}</td>
						    <td>${reg.startTime?string('HH:mm')}  ~${reg.endTime?string('HH:mm')}</td>
						    <td>${reg.subName!}</td>
						    <td>${reg.unitName!}</td>
						    <td>${reg.roomNo!}考场${reg.seatNo!}座</td>
						</tr>
						</#list>
					</#if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div>
		<h4><b>注意事项</b></h4>
		<div>1、考生必须验证进入考场，证件不全者不准进入考场。</div>
		<div>2、考生进入考场后应按指定的座位就座，并将身份证原件摆在桌面左上角。</div>
		<div>3、迟到15分钟不得入场，开考30分钟后，考生方可交卷。</div>
		<div>4、考生在答题前将本人姓名等信息写在试卷规定位置，答题一律用蓝色、黑色水笔，其他颜色笔答题无效。</div>
		<div>5、考生拿到试卷后，应检查试卷有无错页、漏页等，如遇试卷分发错误，页码序号不对等问题，应举手询问。</div>
		<div>6、服从考试工作人员管理，接受监考人员的监督和检查。</div>
	</div>
</div>