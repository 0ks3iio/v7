<div class="row">
	<div class="col-sm-6" style="float:left;width:50%;height:50%;">
		<table class="table table-bordered table-condensed table-print">
			<tbody>
				<tr>
					<td class="table-print-title">奖惩记载</td>
					<td class="table-print-textarea" height="210">
					<#if stuDevelopRewardsList?exists && (stuDevelopRewardsList?size > 0)>
				        <#list stuDevelopRewardsList as item>
				                                        奖励：${item.rewardsname!},${item.remark!}<br>
				        </#list>
				    </#if>
				    <#if stuDevelopPunishmentList?exists && (stuDevelopPunishmentList?size > 0)>
				        <#list stuDevelopPunishmentList as item>
				                                       惩罚：${item.punishname!},${item.remark!}<br>
				        </#list>
				    </#if>
					</td>
				</tr>
				<tr>
					<td class="table-print-title">教师寄语</td>
					<td class="table-print-textarea" height="210">
					<#if stuEvaluateRecord?exists>
                         ${stuEvaluateRecord.teacherEvalContent!}
				    </#if>
					</td>
				</tr>
				<tr>
					<td class="table-print-title">父母的期望</td>
					<td class="table-print-textarea" height="210">
					<#if stuEvaluateRecord?exists>
                         ${stuEvaluateRecord.parentEvalContent!}
				    </#if>
					</td>
				</tr>
				<tr>
					<td colspan="2" height="110">
						<p>下学期定于  
						<#if sem.registerDate?exists>
						${sem.registerDate?string("yyyy-MM-dd")!}
						</#if>
						报到</p>
						<p class="text-right f16">慈溪市教育局监制</p>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="col-sm-6" style="float:left;width:50%;height:50%">
		<table class="table table-bordered table-condensed table-print text-center">
			<tbody>
				<tr>
					<td height="160" class="no-border-bottom">
						<h2 class="report-title">慈溪市小学生素质报告单</h2>
						<h5 class="report-subtitle">${acadyear!}学年${mcodeSetting.getMcode("DM-XQ",semester?default('1'))}</h5>
					</td>
				</tr>
				<tr>
					<td height="460" class="no-border-bottom">
						<ul class="report-info-list">
							<li>学校：${unit.unitName!}</li>
							<li>班级：${className!}</li>
							<li>姓名：${student.studentName!}</li>
						</ul>
					</td>
				</tr>
				<tr>
					<td height="120">
						<div class="report-footer"><span>校长：${school.schoolmaster!}</span><span>教导主任：${school.partyMaster!}</span><span>班主任：${teacher.teacherName!}</span></div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<div class="row">
	<div class="col-sm-6" style="float:left;width:50%;height:50%">
		<table class="table table-bordered table-condensed table-print text-center">
			<tbody>
				<tr>
					<td rowspan="12" class="table-print-title">思想品德素质</td>
					<td colspan="4" rowspan="2" style="height:62px;">项目</td>
					<td colspan="3">评估结果</td>
				</tr>
				<tr>
					<td width="12%">好</td>
					<td width="12%">较好</td>
					<td width="12%">需努力</td>
				</tr>
				
				<#list 0..9 as x>
                		<#if qualityOfMind?size gt x>
                			<tr>
			                	<td colspan="4" class="text-left f12">${x+1}、${(qualityOfMind[x])[0]}</td>
			                	<td>
			                		<#if (qualityOfMind[x][1]!"") == 'A'>
			                			√
			                		<#else>
			                			&nbsp;
			                		</#if>
			                	</td>
			                	<td>
			                		<#if (qualityOfMind[x][1]!"") == 'D'>
			                			√
			                		<#else>
			                			&nbsp;
			                		</#if>
			                	</td>
			                	<td>
			                		<#if (qualityOfMind[x][1]!"") == 'E'>
			                			√
			                		<#else>
			                			&nbsp;
			                		</#if>
			                	</td>
			           </tr>
                		<#else>
                			<tr>
			                	<td class="t-left" height="25">&nbsp;</td>
			                	<td>&nbsp;</td>
			                	<td>&nbsp;</td>
			                	<td>&nbsp;</td>
			                	<td>&nbsp;</td>
			                	<td>&nbsp;</td>
			                	<td>&nbsp;</td>
			           </tr>
                		</#if>
                </#list>
				
				
				
				<tr>
					<td rowspan="8" class="table-print-title">身体心理素质</td>
					<td>注意</td>
					<td>集中</td>
					<td width="12%"><#if (stuHealthRecord.attention?default(''))=='1'>√</#if></td>
					<td>较集中</td>
					<td><#if (stuHealthRecord.attention?default(''))=='2'>√</#if></td>
					<td>需集中</td>
					<td><#if (stuHealthRecord.attention?default(''))=='3'>√</#if></td>
				</tr>
				<tr>
					<td>观察</td>
					<td>仔细</td>
					<td><#if (stuHealthRecord.observation?default(''))=='1'>√</#if></td>
					<td>较仔细</td>
					<td><#if (stuHealthRecord.observation?default(''))=='2'>√</#if></td>
					<td>需仔细</td>
					<td><#if (stuHealthRecord.observation?default(''))=='3'>√</#if></td>
				</tr>
				<tr>
					<td>记忆</td>
					<td>强</td>
					<td><#if (stuHealthRecord.memory?default(''))=='1'>√</#if></td>
					<td>较强</td>
					<td><#if (stuHealthRecord.memory?default(''))=='2'>√</#if></td>
					<td>需加强</td>
					<td><#if (stuHealthRecord.memory?default(''))=='3'>√</#if></td>
				</tr>
				<tr>
					<td>思维</td>
					<td>活跃</td>
					<td><#if (stuHealthRecord.thinking?default(''))=='1'>√</#if></td>
					<td>较活跃</td>
					<td><#if (stuHealthRecord.thinking?default(''))=='2'>√</#if></td>
					<td>需活跃</td>
					<td><#if (stuHealthRecord.thinking?default(''))=='3'>√</#if></td>
				</tr>
				<tr>
					<td>情绪</td>
					<td>积极</td>
					<td><#if (stuHealthRecord.mood?default(''))=='1'>√</#if></td>
					<td>较积极</td>
					<td><#if (stuHealthRecord.mood?default(''))=='2'>√</#if></td>
					<td>需积极</td>
					<td><#if (stuHealthRecord.mood?default(''))=='3'>√</#if></td>
				</tr>
				<tr>
					<td>意志</td>
					<td>坚强</td>
					<td><#if (stuHealthRecord.will?default(''))=='1'>√</#if></td>
					<td>较坚强</td>
					<td><#if (stuHealthRecord.will?default(''))=='2'>√</#if></td>
					<td>需坚强</td>
					<td><#if (stuHealthRecord.will?default(''))=='3'>√</#if></td>
				</tr>
				<tr>
					<td>身高</td>
					<td colspan="2">${stuHealthRecord.height!}</td>
					<td rowspan="2">视力</td>
					<td>左</td>
					<td colspan="2">${stuHealthRecord.leftEye!}</td>
				</tr>
				<tr>
					<td>体重</td>
					<td colspan="2">${stuHealthRecord.weight!}</td>
					<td>右</td>
					<td colspan="2">${stuHealthRecord.rightEye!}</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="col-sm-6" style="float:left;width:50%;height:50%">
		<table class="table table-bordered table-condensed table-print text-center">
			<tbody>
				<tr>
					<td rowspan="8" class="table-print-title">文化素质</td>
					<td width="11%">学科</td>
					<td width="11%">平时<br>成绩</td>
					<td width="11%">期末<br>成绩</td>
					<td width="11%">学习<br>态度</td>
					<td width="11%">学科</td>
					<td width="11%">平时<br>成绩</td>
					<td width="11%">期末<br>成绩</td>
					<td width="11%">学习<br>态度</td>
				</tr>
				<#list 0..6 as i>
            <tr>
            	<#if achilist?exists && (achilist?size>i*2)>
            		    <td>${achilist[i*2].subname!}</td>
	                	<td>${achilist[i*2].psachi!}</td>
	                	<td>${achilist[i*2].qmachi!}</td>
	                	<td>
                            ${mcodeSetting.getMcode("DM-XXTD", achilist[i*2].xxtd!?default("1"))}
	                	</td>
	           <#else>
	           		<td>&nbsp;</td>
	                	<td>&nbsp;</td>
	                	<td>&nbsp;</td>
	                	<td>&nbsp;</td>
	           </#if>
	           <#if (achilist?size>i*2+1)>
	                	<td>${achilist[i*2+1].subname!}</td>
	                	<td>${achilist[i*2+1].psachi!}</td>
	                	<td>${achilist[i*2+1].qmachi!}</td>
	                	<td>
                            ${mcodeSetting.getMcode("DM-XXTD", achilist[i*2+1].xxtd!?default("1"))}
	                	</td>
	            <#else>
	           		    <td>&nbsp;</td>
	                	<td>&nbsp;</td>
	                	<td>&nbsp;</td>
	                	<td>&nbsp;</td>
			</#if>          	
		</tr>
		</#list>		
				<tr>
					<td class="table-print-title">兴趣爱好</td>
					<td colspan="8" class="table-print-textarea">${studentexDto.remark!}</td>
				</tr>
				<tr>
					<td class="table-print-title">特长</td>
					<td colspan="8" class="table-print-textarea">${studentexDto.strong!}</td>
				</tr>
				<tr>
					<td rowspan="2" class="table-print-title">出勤情况</td>
					<td colspan="2" width="16%">本学期课程</td>
					<td colspan="2" width="16%">${stuCheckAttendance.studyDate!}</td>
					<td width="16%">事假</td>
					<td width="16%">${stuCheckAttendance.businessVacation!}</td>
					<td width="16%">旷课</td>
					<td width="16%">${stuCheckAttendance.wasteVacation!}</td>
				</tr>
				<tr>
				    <#assign lateAndleaveEarly = stuCheckAttendance.late+stuCheckAttendance.leaveEarly>
					<td colspan="2">出席</td>
					<td colspan="2">${stuCheckAttendance.studyDate-stuCheckAttendance.businessVacation-stuCheckAttendance.illnessVacation}</td>
					<td>病假</td>
					<td>${stuCheckAttendance.illnessVacation!}</td>
					<td>迟到早退</td>
					<td>${lateAndleaveEarly!}</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>