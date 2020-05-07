<div class="row">
	<div class="col-sm-6" style="width:99%;height:100%;">
	    <h4><b>
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    班级：${className!}   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
	          姓名：${student.studentName!}</b></h4>
		<table class="table table-condensed table-print text-center">
			<tbody>
			    <#if achilist?exists && achilist?size gt 0>
			        <#if achilist?size gt 7>
			           <#assign sumtd = (achilist?size+3)>
			        <#else>
			           <#assign sumtd = 12>
			        </#if>
			    <#else>
			        <#assign sumtd = 12>
			    </#if>
				<tr>
				    <td colspan="2" rowspan="${sumtd!}" height="600" width="35%" class="table-print-textarea">
				            操行评语：
				    <#if stuEvaluateRecord?exists>
                         ${stuEvaluateRecord.teacherEvalContent!}
				    </#if>
				    </td>
				    <td colspan="3" rowspan="2" width="35%">各科成绩</td>
				    <td colspan="2">身心健康</td>
				</tr>
				<tr> 
				    <td>项目</td>
				    <td>等第</td>
				</tr>
				<tr>
				    <td>学科</td>
				    <td>期末成绩</td>
				    <td>单科排名</td>
				    <td>体育与健康</td>
				    <td>${reportScore!}</td>
				</tr>
				
				<#if achilist?exists && achilist?size gt 0>
				    <#if achilist?size gt 7>
				        <#assign s=achilist?size-7>
				        <#list 0..6 as i>
				        <tr>
				            <td>${achilist[i].subname!}</td>
				            <td>${achilist[i].qmachi!}</td>
				            <td>${achilist[i].schPlace!}</td>
				            <#if i==0>
				               <td>二课一操</td>
				               <td>${erkeScore!}</td>
				            <#elseif i==1>
				               <td colspan="2">身体素质</td>
				            <#elseif i==2>
				               <td>项目</td>
				               <td>检查情况</td>
				            <#elseif i==3>
				               <td>身高</td>
				               <td>cm</td>
				            <#elseif i==4>
				               <td>体重</td>
				               <td>kg</td>
				            <#elseif i==5>
				               <td rowspan="2">视力</td>
				               <td>左：</td>
				            <#elseif i==6>
				               <td>右：</td>
				            </#if>
				        </tr>
				        </#list>
				        <#list 7..6+s as t>
				        <tr>
				            <td>${achilist[t].subname!}</td>
				            <td>${achilist[t].qmachi!}</td>
				            <td>${achilist[t].schPlace!}</td>
				            <#if t==7>
				               <td rowspan="${s+1!}" class="table-print-title">个性特点</td>
				               <td rowspan="${s+1!}"><#if stuEvaluateRecord?exists>
									${stuEvaluateRecord.strong!}
									</#if></td>
				            </#if>
				        </tr>
				        </#list>
				    <#else>
				        <#list 0..6 as i>
				        <tr>
				            <#if achilist[i]?exists>
				               <td>${achilist[i].subname!}</td>
				               <td>${achilist[i].qmachi!}</td>
				               <td>${achilist[i].schPlace!}</td>
				            <#else>
				               <td>&nbsp;</td>
				               <td>&nbsp;</td>
				               <td>&nbsp;</td>
				            </#if>
				            <#if i==0>
				               <td>二课一操</td>
				               <td>${erkeScore!}</td>
				            <#elseif i==1>
				               <td colspan="2">身体素质</td>
				            <#elseif i==2>
				               <td>项目</td>
				               <td>检查情况</td>
				            <#elseif i==3>
				               <td>身高</td>
				               <td>${stuHealthRecord.height!}cm</td>
				            <#elseif i==4>
				               <td>体重</td>
				               <td>${stuHealthRecord.weight!}kg</td>
				            <#elseif i==5>
				               <td rowspan="2">视力</td>
				               <td>左：${stuHealthRecord.leftEye!}</td>
				            <#elseif i==6>
				               <td>右：${stuHealthRecord.rightEye!}</td>
				            </#if>
				        </tr>
				        </#list>
				        <tr>
				           <td>&nbsp;</td>
				           <td>&nbsp;</td>
				           <td>&nbsp;</td>
				           <td rowspan="3" class="table-print-title">个性特点</td>
				           <td rowspan="3"><#if stuEvaluateRecord?exists>
									${stuEvaluateRecord.strong!}
									</#if></td>
				       </tr>
				       <tr>
				           <td>&nbsp;</td>
				           <td>&nbsp;</td>
				           <td>&nbsp;</td>
				       </tr>
				    </#if>
				<#else>
				       <#list 0..6 as i>
				        <tr>
				               <td>&nbsp;</td>
				               <td>&nbsp;</td>
				               <td>&nbsp;</td>
				            <#if i==0>
				               <td>二课一操</td>
				               <td>${erkeScore!}</td>
				            <#elseif i==1>
				               <td colspan="2">身体素质</td>
				            <#elseif i==2>
				               <td>项目</td>
				               <td>检查情况</td>
				            <#elseif i==3>
				               <td>身高</td>
				               <td>${stuHealthRecord.height!}cm</td>
				            <#elseif i==4>
				               <td>体重</td>
				               <td>${stuHealthRecord.weight!}kg</td>
				            <#elseif i==5>
				               <td rowspan="2">视力</td>
				               <td>左：${stuHealthRecord.leftEye!}</td>
				            <#elseif i==6>
				               <td>右：${stuHealthRecord.rightEye!}</td>
				            </#if>
				        </tr>
				        </#list>
				        <tr>
				           <td>&nbsp;</td>
				           <td>&nbsp;</td>
				           <td>&nbsp;</td>
				           <td rowspan="3" class="table-print-title">个性特点</td>
				           <td rowspan="3"><#if stuEvaluateRecord?exists>
									${stuEvaluateRecord.strong!}
									</#if>
						   </td>
				       </tr>
				       <tr>
				           <td>&nbsp;</td>
				           <td>&nbsp;</td>
				           <td>&nbsp;</td>
				       </tr>
				</#if>
				
				
				<tr>
				    <td height="100" colspan="2">操行等第：
				    <#if stuEvaluateRecord?exists>
                         ${stuEvaluateRecord.evaluateLevel!}
				    </#if>
				    </td>
				    <td colspan="3" class="table-print-textarea">兴趣爱好：${studentexDto.remark!}</td>
				</tr>
				<tr>
				    <td colspan="7" height="100" class="table-print-textarea">奖惩记载(学科获奖等情况)：<br>
				    <#if stuDevelopRewardsList?exists && (stuDevelopRewardsList?size > 0)>
					  奖励：
				        <#list stuDevelopRewardsList as item>
				            ${item.rewardsname!},${item.remark!}&nbsp;&nbsp;&nbsp;
				        </#list>
				    </#if>
				    <br>
				    <#if stuDevelopPunishmentList?exists && (stuDevelopPunishmentList?size > 0)>
				             惩罚：
				        <#list stuDevelopPunishmentList as item>
				             ${item.punishname!},${item.remark!}&nbsp;&nbsp;&nbsp;
				        </#list>
				    </#if>				    
				    </td>
				</tr>
				<tr>
				    <td height="100">附告</td>
				    <td colspan="6" class="table-print-textarea">
				        1．下学期定于    ${sem.registerDate?string("yyyy-MM-dd")!}    报到注册。  定于 ${sem.semesterBegin?string("yyyy-MM-dd")!}开学<br>
                        2．利用假期合理安排时间进行文化课学习，积极参加社会实践活动。<br>  
                        3．请家长督促子女注意假期安全，遵守社会公共秩序，使子女过一个文明、安全而有意义的假期。
				    </td>
				</tr>
			</tbody>
		</table>
		<h4><b>

		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		校长：${school.schoolmaster!}  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		   教务主任：${school.partyMaster!}
		  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
		 班主任：${teacher.teacherName!}
		</b></h4>
	</div>
</div>