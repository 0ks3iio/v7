
<form id="myform">
<div class="win-box gradeset">
	<div class="box-header">
		<h4 class="box-title showExamNameClass" style="display:none">
		</h4>
		<h4 class="box-title">
			综合能力报表
		</h4><br>
	</div>
	<div class="box-body">
				<div class="gradeset-body">
				    <h3 class="gradeset-name">奖励信息</h3>
					<table class="table table-bordered table-striped table-hover no-margin">
		                <thead>
				           <tr>
					          <th>序号</th>
					          <th>奖励名称</th>
					          <th>奖励级别</th>
					          <th>奖励日期</th>
                              <th>获奖名次</th>
                              <th>颁奖单位</th>
                              <th>备注</th>
				          </tr>
		               </thead>
		               <tbody>
		               <#if stuDevelopRewardsList?exists && (stuDevelopRewardsList?size > 0)>
				          <#list stuDevelopRewardsList as item>
		                  <tr>
				              <td>${item_index+1!}</td>
				              <td>${item.rewardsname!}</td>
				              <td>${mcodeSetting.getMcode("DM-JLJB", item.rewardslevel?default("1"))}</td>
				              <td>${(item.rewardsdate?string('yyyy-MM-dd'))!}</td>
				              <td>${item.rewardPosition!}</td>
				              <td>${item.rewardsunit!}</td>
				              <td title="${item.remark!}"><p style="margin:0 10px;width:200px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">${item.remark!}</p></td>
				         </tr>
				         </#list>
			          </#if>
		              </tbody>
	              </table>
				
					<h3 class="gradeset-name">惩处信息</h3>
					<table class="table table-bordered table-striped table-hover no-margin">
		                <thead>
				           <tr>
					          <th>序号</th>
					          <th>惩处名称</th>
					          <th>惩处原因</th>
					          <th>惩处类型</th>
                              <th>惩处日期</th>
                              <th>撤销日期</th>
                              <th>备注</th>
				          </tr>				          
		               </thead>
		               <tbody>
		               <#if stuDevelopPunishmentList?exists && (stuDevelopPunishmentList?size > 0)>
				           <#list stuDevelopPunishmentList as item>
		                  <tr>
				              <td>${item_index+1!}</td>
				              <td>${item.punishname!}</td>
				              <td title="${item.punishreason!}"><p style="margin:0 10px;width:200px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">${item.punishreason!}</p></td>
				              <td>${mcodeSetting.getMcode("DM-CFMC",item.punishType?default('1'))}</td>
				              <td>${(item.punishdate?string('yyyy-MM-dd'))!}</td>
				              <td>${(item.canceldate?string('yyyy-MM-dd'))!}</td>
				              <td title="${item.remark!}"><p style="margin:0 10px;width:200px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">${item.remark!}</p></td>
				         </tr>
				         </#list>
				       </#if>
		              </tbody>
	              </table>		
	              
	              <h3 class="gradeset-name">任职情况</h3>
					<table class="table table-bordered table-striped table-hover no-margin">
		                <thead>
				           <tr>
					          <th>序号</th>
					          <th>任职开始时间</th>
					          <th>结束时间</th>
					          <th>所任职位</th>
                              <th>工资表现情况</th>
                              <th>备注</th>
				          </tr>
		               </thead>
		               <tbody>
		               <#if studevelopDutySituationList?exists && (studevelopDutySituationList?size > 0)>
				          <#list studevelopDutySituationList as item>
		                  <tr>
				              <td>${item_index+1!}</td>
				              <td>${(item.openTime?string('yyyy-MM-dd'))?if_exists}</td>
				              <td>${(item.endTime?string('yyyy-MM-dd'))?if_exists}</td>
				              <td>${item.dutyName!}</td>
				              <td title="${item.dutySituation!}"><p style="margin:0 10px;width:200px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">${item.dutySituation!}</p></td>
				              <td title="${item.remark!}"><p style="margin:0 10px;width:200px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">${item.remark!}</p></td>
				         </tr>
				         </#list>
				       </#if>
		              </tbody>
	              </table>
	              
	              <h3 class="gradeset-name">社团活动情况</h3>
					<table class="table table-bordered table-striped table-hover no-margin">
		                <thead>
				           <tr>
					          <th style="width:5px;">序号</th>
					          <th style="width:100px;">社团名称</th>
					          <th style="width:80px;">参加日期</th>
					          <th>活动内容</th>
                              <th>备注</th>
				          </tr>
		               </thead>
		               <tbody>
		               <#if stuLeagueRecordList?exists && (stuLeagueRecordList?size > 0)>
				          <#list stuLeagueRecordList as item>
		                  <tr>
				              <td>${item_index+1!}</td>
				              <td>${item.leagueName!}</td>
				              <td>${(item.joinDate?string('yyyy-MM-dd'))?if_exists}</td>
				              <td class="table-print-textarea" style="word-break:break-all;">${item.leagueContent!}</td>
				              <td class="table-print-textarea" style="word-break:break-all;">${item.remark!}</td>
				         </tr>
				         </#list>
				       </#if>
		              </tbody>
	              </table>	
	              
	              <h3 class="gradeset-name">身心健康</h3>
					<table class="table table-bordered table-striped table-hover no-margin">
		               <tbody>
		                  <tr>
				              <th>身高</th>
				              <td colspan="4">${stuHealthRecord.height!}</td>				              
				              <th>体质测试</th>
				              <td>${stuHealthRecord.physique!}</td>
				              <th rowspan="3">心理素质</th>
				              <th>自制力</th>
				              <td>${stuHealthRecord.selfControl!}</td>
				         </tr>
				         <tr>
				              <th>体重（千克）</th>
				              <td colspan="4">${stuHealthRecord.weight!}</td>				            
				              <th>成长阅读</th>
				              <td>${stuHealthRecord.groupRead!}</td>
				              <th>自信心</th>
				              <td>${stuHealthRecord.confidence!}</td>
				         </tr>
				         <tr>
				              <th>视力</th>
				              <th>左</th>
				              <td>${stuHealthRecord.leftEye!}</td>
				              <th>右</th>
				              <td>${stuHealthRecord.rightEye!}</td>
				              <th>社会实践</th>
				              <td>${stuHealthRecord.socialPractice!}</td>
				              <th>合作交往</th>
				              <td>${stuHealthRecord.contact!}</td>
				         </tr>
		              </tbody>
	              </table>	
	              
	              <h3 class="gradeset-name">期末评语</h3>
					<table class="table table-bordered table-striped table-hover no-margin">
		               <tbody>
		                  <tr>
				              <th style="width:100px;">学生的荣誉</th>
				              <td colspan="9" class="table-print-textarea" style="word-break:break-all;">
				              ${stuEvaluateRecord.stuHonorContent!}
				              </td>
				         </tr>
				         <tr>
				              <th>学生的收获</th>
				              <td colspan="9" class="table-print-textarea" style="word-break:break-all;">
				              ${stuEvaluateRecord.stuGatherContent!}
				              </td>
				         </tr>
				         <tr>
				              <th>学生的愿望</th>
				              <td colspan="9" class="table-print-textarea" style="word-break:break-all;">
				              ${stuEvaluateRecord.stuWishContent!}
				              </td>
				         </tr>
				         <tr>
				              <th>老师寄语</th>
				              <td colspan="9" class="table-print-textarea" style="word-break:break-all;">
				              ${stuEvaluateRecord.teacherEvalContent!}
				              </td>
				         </tr>
				         <tr>
				              <th>家长回音</th>
				              <td colspan="9" class="table-print-textarea" style="word-break:break-all;">
				              ${stuEvaluateRecord.parentEvalContent!}
				              </td>
				         </tr>				     
		              </tbody>
	              </table>			
			</div>				
	</div>
</div><!-- 分数线设定部分结束 -->
<!-- 分段成绩设定部分开始 -->

</form>

<script type="text/javascript">

</script>