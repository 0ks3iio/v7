<div class="row">
	<div class="col-sm-6" style="float:left;width:50%;height:50%;">
	    <h4 class="text-center"><b>道德健康素质</b></h4>
		<table class="table table-bordered table-condensed table-print text-center">
			<tbody>
				<tr>
				    <td>班级：${className!}</td>
				    <td>姓名：${student.studentName!}</td>
				    <td colspan="2">操行等第：${stuEvaluateRecord.evaluateLevel?default('')}</td>
				</tr>
				<tr>
				    <td colspan="4">教师寄语</td>
				</tr>
				<tr>
				    <td colspan="4" height="300" class="table-print-textarea">
				       <#if stuEvaluateRecord?exists>
                         ${stuEvaluateRecord.teacherEvalContent!}
				       </#if>
				    </td>
				</tr>
				<tr>
				    <td colspan="4">任职及奖惩（争章情况等）</td>
				</tr>
				<tr>
				    <td colspan="4" height="200" class="table-print-textarea">
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
				    <td>视力</td>
				    <td td colspan="3">左眼：${stuHealthRecord.leftEye!}   右眼：${stuHealthRecord.rightEye!}</td>
				</tr>
				<tr>
				    <td>身高</td>
				    <td>${stuHealthRecord.height!}CM</td>
				    <td>体重</td>
				    <td>${stuHealthRecord.weight!}KG</td>
				</tr>
				<tr>
				    <td>校长</td>
				    <td>${school.schoolmaster!}</td>
				    <td>班主任</td>
				    <td>${teacher.teacherName!}</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="col-sm-6" style="float:left;width:50%;height:50%">
	    <h4 class="text-center"><b>科学文化素质</b></h4>
		<table class="table table-bordered table-condensed table-print text-center">
			<tbody>
				<tr>
				    <td rowspan="2">学科</td>
				    <td rowspan="2">项目\等级</td>
				    <td colspan="2">综合成绩</td>
				    <td rowspan="2">描述性评语</td>
				</tr>
				<tr>
				    <td>平时</td>
				    <td>期末</td>
				</tr>
				<#if stuSubjectList?exists && stuSubjectList?size gt 0 >
                 <#assign rowsNum=(stuSubjectList?size -1) >

            	
            	<#list 0..rowsNum  as j>
            <#assign num = (stuSubjectList[j].stuPartInfoScoreList?size) >
            <tr>
            		<td rowspan="${num!}"  <#if !stuSubjectList[j].hasPart > colspan="2" </#if>   >${stuSubjectList[j].subjectName!}</td>
            			<#if stuSubjectList[j].stuPartInfoScoreList?exists && stuSubjectList[j].stuPartInfoScoreList?size gt 0  &&  stuSubjectList[j].hasPart >
		            				 <td style="font-size:10.5px;">${stuSubjectList[j].stuPartInfoScoreList[0].partName!}</td>
		            				 <#if stuSubjectList[j].stuPartInfoScoreList[0].partName =="测试" >
		            				  <td>${stuSubjectList[j].stuPartInfoScoreList[0].partPsScore!}</td>
			                	     <td>${stuSubjectList[j].stuPartInfoScoreList[0].partQmScore!}</td>
			                	     	<td rowspan="${num!}"></td>
			                	     	<#else>
			                	     <td colspan="2">${stuSubjectList[j].stuPartInfoScoreList[0].partQmScore!}</td>
		            				 </#if>
			                	    
		            	<#else>
		 					<td colspan="2" >${stuSubjectList[j].score!}</td>
		 					<td></td>
            			</#if>            				              	            	
		</tr>
				<#if (num-1) gt 0 >
				<#list 1..1 as i >
					 <tr  >
					 	<td>${stuSubjectList[j].stuPartInfoScoreList[i].partName!}</td>
				         <#if stuSubjectList[j].stuPartInfoScoreList[i].partName =="测试" >
		            		<td style="font-size:10.5px;">${stuSubjectList[j].stuPartInfoScoreList[i].partPsScore!}</td>
			                <td>${stuSubjectList[j].stuPartInfoScoreList[i].partQmScore!}</td>
			                
			                <#else>
			                <td colspan="2">${stuSubjectList[j].stuPartInfoScoreList[i].partQmScore!}</td>
		            	</#if>
					 </tr>
				</#list>
				</#if>
            </#list>
            
            </#if>

			</tbody>
		</table>
	</div>
</div>