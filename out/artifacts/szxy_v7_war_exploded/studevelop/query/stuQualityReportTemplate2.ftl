<div class="row">
	<div class="col-sm-6" style="float:left;width:50%;height:50%;">
		<table class="table table-bordered table-condensed table-print text-center">
			<tbody>
				<tr>
					<td colspan="4">体检情况</td>
					<td colspan="4">出勤情况</td>
					<td width="25%">特长爱好</td>
				</tr>
				<tr height="90">
					<td rowspan="2">身高(cm)</td>
					<td rowspan="2">${stuHealthRecordDto.height!}<br/></td>
					<td rowspan="4">视力</td>
					<td rowspan="2">左：${stuHealthRecordDto.leftEye!}</td>
					<td rowspan="2">本学<br>期共<br>上课</td>
					<td rowspan="2">${stuCheckAttendance.studyDate!}天</td>
					<td rowspan="2">出席</td>
					<td rowspan="2">${stuCheckAttendance.studyDate-stuCheckAttendance.businessVacation-stuCheckAttendance.illnessVacation}天</td>
					<td class="table-print-textarea" rowspan="4">
					<#--<#if studentexDto?exists && '${studentexDto.remark!}' != ''>-->
					<#--爱好：${studentexDto.remark!}<br>-->
					<#--</#if>-->
					<#--<#if studentexDto?exists && '${studentexDto.strong!}' != ''>-->
					<#--特长：${studentexDto.strong!}-->
					<#--</#if>-->
					<#if stuEvaluateRecord?exists>
					    爱好：${stuEvaluateRecord.hobby!}
                        特长：${stuEvaluateRecord.strong!}
					</#if>
					</td>
				</tr>
				<tr>

				</tr>
				<tr height="90">
				    <#assign lateAndleaveEarly = stuCheckAttendance.late+stuCheckAttendance.leaveEarly>
					<td rowspan="2">体重(kg)</td>
					<td rowspan="2">${stuHealthRecordDto.weight!}</td>
					<td rowspan="2">右：${stuHealthRecordDto.rightEye!}</td>
					<td>病事假</td>
					<td>${stuCheckAttendance.illnessVacation+stuCheckAttendance.businessVacation}天</td>
					<td>旷课</td>
					<td>${stuCheckAttendance.wasteVacation!}节</td>
				</tr>
				<tr>
					<td>迟到<br>早退</td>
					<td colspan="3">${lateAndleaveEarly!}次</td>
				</tr>
				<tr>
					<td class="table-print-title">教师寄语</td>
					<td class="table-print-textarea" <#if teaEvalContent?exists && teaEvalContent?length <=200 > style="font-size:15px" <#else> style="font-size:14px"</#if> colspan="8" height="200">
					<#if stuEvaluateRecord?exists>
                         ${stuEvaluateRecord.teacherEvalContent!}
				    </#if>
					</td>
				</tr>
				<tr>
					<td class="table-print-title">奖惩记录</td>
					<td colspan="5" height="200">
					<#if stuDevelopRewardsList?exists && (stuDevelopRewardsList?size > 0)>
					  奖励：
				        <#list stuDevelopRewardsList as item>
				            ${item.rewardsname!} <#if item.remark?default('') !=''>,${item.remark!} </#if>&nbsp;&nbsp;&nbsp;
				        </#list>
				    </#if>
				    <br>
				    <#if stuDevelopPunishmentList?exists && (stuDevelopPunishmentList?size > 0)>
				             惩罚：
				        <#list stuDevelopPunishmentList as item>
				             ${item.punishname!} <#if item.remark?default('') !=''>,${item.remark!}</#if>&nbsp;&nbsp;&nbsp;
				        </#list>
				    </#if>
					</td>
					<td class="table-print-title">操行等第</td>
					<td colspan="2" height="200">
					${stuEvaluateRecord.evaluateLevel?default('')}
					</td>
				</tr>
			</tbody>
		</table>
        <p>下学期定于  ${sem.registerDate?string("yyyy-MM-dd")!}  报到  定于  ${sem.semesterBegin?string("yyyy-MM-dd")!}开学</p>
		<p class="text-right f16">${regionName!}教育局监制</p>
	</div>
	<div class="col-sm-6" style="float:left;width:50%;height:50%">
		<table class="table table-bordered table-condensed table-print text-center">
			<tbody>
				<tr>
					<td height="160" class="no-border-bottom">
						<h2 class="report-title">${regionName!}</h2>
						<h2 class="report-title">初中生素质报告单</h2>
						<h5 class="report-subtitle">${acadyear!}学年${mcodeSetting.getMcode("DM-XQ",semester?default('1'))}</h5>
					</td>
				</tr>
				<tr>
					<td height="460" class="no-border-bottom">
						<ul class="report-info-list">
							<li>学校：${school.schoolName!}</li>
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
	<div class="col-sm-6" style="float:left;width:50%;">
		<table class="table table-bordered table-condensed table-print text-center" style="font-size:10.5px;">
			<tbody>
			   <tr style="font-size:10.5px;">
					<td colspan="13"><b>思想品德素质</b></td>
				</tr>
				<tr style="font-size:10.5px;">
					<td colspan="10" rowspan="2" style="height:20px;">项目</td>
					<td colspan="3">评估结果</td>
				</tr>
				<tr style="font-size:10.5px;">
					<td >好</td>
					<td >较好</td>
					<td >需努力</td>
				</tr>
				<#list 0..9 as x>
                		<#if qualityOfMind?exists && qualityOfMind?size gt x>
                			<tr style="font-size:10.5px;">
			                	<td colspan="10" class="text-left f12">${x+1}、${(qualityOfMind[x])[0]!}</td>
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
                			<tr style="font-size:10.5px;">
			                	<td class="t-left" height="25" colspan="10">&nbsp;</td>
			                	<td>&nbsp;</td>
			                	<td>&nbsp;</td>
			                	<td>&nbsp;</td>
			                	
			           </tr>
                		</#if>
                </#list>
				<tr>
					<td style="font-size:10.5px;" colspan="13">文化素质</td>
				</tr>
				
				<tr style="font-size:10.5px;">
				    <td rowspan="2" width="5%" >学科</td>
					<td rowspan="2" width="15%">要素</td>
					<td rowspan="2" width="8%">平时成绩</td>
					<td rowspan="2" width="8%">期末成绩</td>
					<td colspan="3" width="20%" >学习态度</td>
					<td colspan="3" width="22%">交流与合作能力</td>
					<td colspan="3" width="22%">发现、提出问题能力</td>
				</tr>
				
				<tr style="font-size:10.5px;">
				    <td>好</td>
					<td>较好</td>
					<td>需努力</td>
					<td>强</td>
					<td>较强</td>
					<td>需加强</td>
					<td>强</td>
					<td>较强</td>
					<td>需加强</td>
				</tr>
				
				<#--<#if stuSubjectList?exists && stuSubjectList?size gt 0 >-->
            	<#--<#if stuSubjectList?size gt 3 >-->
            		<#--<#assign rowsNum = 3 >-->
            	<#--<#else >-->
            		<#--<#assign rowsNum=(stuSubjectList?size -1) >-->
            	<#--</#if >-->

            	<#--<#list 0..rowsNum  as j>-->
            <#--<#assign num = (stuSubjectList[j].stuPartInfoScoreList?size) >-->
            <#--<tr>-->
            		<#--<td rowspan="${num!}"  <#if !stuSubjectList[j].hasPart > colspan="2" </#if>   >${stuSubjectList[j].subjectName!}</td>-->
            			<#--<#if stuSubjectList[j].stuPartInfoScoreList?exists && stuSubjectList[j].stuPartInfoScoreList?size gt 0  &&  stuSubjectList[j].hasPart >-->
		            				 <#--<td style="font-size:10.5px;">${stuSubjectList[j].stuPartInfoScoreList[0].partName!}</td>-->
		            				 <#--<#if stuSubjectList[j].stuPartInfoScoreList[0].partName =="测试" >-->
		            				  <#--<td>${stuSubjectList[j].stuPartInfoScoreList[0].partPsScore!}</td>-->
			                	     <#--<td>${stuSubjectList[j].stuPartInfoScoreList[0].partQmScore!}</td>-->
			                	     	<#--<#else>-->
			                	     <#--<td colspan="2">${stuSubjectList[j].stuPartInfoScoreList[0].partQmScore!}</td>-->
		            				 <#--</#if>-->

		            	<#--<#else>-->
		            	    <#--<td>${stuSubjectList[j].psScore!}</td>-->
		 					<#--<td>${stuSubjectList[j].score!}</td>-->
            			<#--</#if>-->

	                	<#--<td rowspan="${num!}"  >-->
	                		<#--<#if  stuSubjectList[j].manner?default('')== "1" >-->
                                <#--√-->
			                <#--<#else>-->
			                	<#--&nbsp;-->
	                		<#--</#if>-->
	                	<#--</td>-->
	                	<#--<td rowspan="${num!}"  >-->
	                	   <#--<#if  stuSubjectList[j].manner?default('') == "2" >-->
                               <#--√-->
			                <#--<#else>-->
			                	<#--&nbsp;-->
	                		<#--</#if>-->
	                		<#--</td>-->
	                	<#--<td rowspan="${num!}" >-->
	                	<#--<#if  stuSubjectList[j].manner?default('') == "3"  ||  stuSubjectList[j].manner?default('') == "4">-->
                            <#--√-->
			                <#--<#else>-->
			                	<#--&nbsp;-->
	                		<#--</#if>-->
	                		<#--</td>-->
	                	<#--<td rowspan="${num!}" >-->
	                		<#--<#if  stuSubjectList[j].communication?default('') == "1" >-->
                                <#--√-->
			                <#--<#else>-->
			                	<#--&nbsp;-->
	                		<#--</#if>-->
	                	<#--</td>-->
	                	<#--<td rowspan="${num!}"   >-->
	                	   <#--<#if  stuSubjectList[j].communication?default('') == "2" >-->
	                		<#--√-->
			                <#--<#else>-->
			                	<#--&nbsp;-->
	                		<#--</#if>-->
	                		<#--</td>-->
	                	<#--<td rowspan="${num!}"   >-->
	                	<#--<#if  stuSubjectList[j].communication?default('') == "3" >-->
                            <#--√-->
			                <#--<#else>-->
			                	<#--&nbsp;-->
	                		<#--</#if>-->
	                		<#--</td>-->
	                	<#--<td rowspan="${num!}"  >-->
	                		<#--<#if  stuSubjectList[j].discovery?default('') == "1" >-->
                                <#--√-->
			                <#--<#else>-->
			                	<#--&nbsp;-->
	                		<#--</#if>-->
	                	<#--</td>-->
	                	<#--<td rowspan="${num!}"   >-->
	                	   <#--<#if  stuSubjectList[j].discovery?default('') == "2" >-->
                               <#--√-->
			                <#--<#else>-->
			                	<#--&nbsp;-->
	                		<#--</#if>-->
	                		<#--</td>-->
	                	<#--<td rowspan="${num!}"  >-->
	                	<#--<#if  stuSubjectList[j].discovery?default('') == "3" >-->
                            <#--√-->
			                <#--<#else>-->
			                	<#--&nbsp;-->
	                		<#--</#if>-->
	                		<#--</td>-->


		<#--</tr>-->
				<#--<#if (num-1) gt 0 >-->
				<#--<#list 1..num-1 as i >-->
					 <#--<tr  >-->
					 	<#--<td>${stuSubjectList[j].stuPartInfoScoreList[i].partName!}</td>-->
				         <#--<#if stuSubjectList[j].stuPartInfoScoreList[i].partName =="测试" >-->
		            		<#--<td style="font-size:10.5px;">${stuSubjectList[j].stuPartInfoScoreList[i].partPsScore!}</td>-->
			                <#--<td>${stuSubjectList[j].stuPartInfoScoreList[i].partQmScore!}</td>-->
			                <#--<#else>-->
			                <#--<td colspan="2">${stuSubjectList[j].stuPartInfoScoreList[i].partQmScore!}</td>-->
		            	<#--</#if>-->
					 <#--</tr>-->
				<#--</#list>-->

				<#--</#if>-->
            <#--</#list>-->

            <#--</#if>-->

			<#if subjectList?exists && subjectList?size gt 0 >
				<#if subjectList?size gt 3 >
					<#assign rowsNum = 3 >
				<#else >
					<#assign rowsNum=(subjectList?size -1) >
				</#if >
				<#list 0..rowsNum  as j>
					<#if subjectList[j].cateGoryList?exists >
						<#assign num = (subjectList[j].cateGoryList?size) >
					<#else>
						<#assign num = 1 >
					</#if>

            <tr>
                <td rowspan="${num!}" <#if num ==1 > colspan="2" </#if> style="font-size:10.5px;"   >${subjectList[j].name!}</td>
            			<#if subjectList[j].cateGoryList?exists && subjectList[j].cateGoryList?size gt 0  >
		            				 <td style="font-size:10.5px;">${subjectList[j].cateGoryList[0].categoryName!}</td>
							<#if subjectList[j].cateGoryList[0].stuSubjectAchiDto?exists >
								<#assign dto = subjectList[j].cateGoryList[0].stuSubjectAchiDto />
								<#if subjectList[j].cateGoryList[0].categoryName =="测试" >

									 <td>${dto.psachi!}</td>
			                	     <td>${dto.qmachi!}</td>

								<#else>
			                	     <td colspan="2">${dto.qmachi!}</td>
								</#if>
							<#else>
								<td></td>
								<td></td>
							</#if>


						<#else>
		            	    <td>${subjectList[j].stuSubjectAchiDto.psachi!}</td>
		 					<td>${subjectList[j].stuSubjectAchiDto.qmachi!}</td>
						</#if>

                <td rowspan="${num!}"  >
							<input type="hidden" value="${subjectList[j].stuSubjectAchiDto.manner!}" />
	                		<#if  subjectList[j].stuSubjectAchiDto.xxtd?default('')== "好" >
                                √
							<#else>
			                	&nbsp;
							</#if>
                </td>
                <td rowspan="${num!}"  >
	                	   <#if  subjectList[j].stuSubjectAchiDto.xxtd?default('') == "较好" >
                               √
						   <#else>
			                	&nbsp;
						   </#if>
                </td>
                <td rowspan="${num!}" >
	                	<#if  subjectList[j].stuSubjectAchiDto.xxtd?default('') == "需努力"  >
                            √
						<#else>
			                	&nbsp;
						</#if>
                </td>
                <td rowspan="${num!}" >
	                		<#if  subjectList[j].stuSubjectAchiDto.coomunication?default('') == "强" >
                                √
							<#else>
			                	&nbsp;
							</#if>
                </td>
                <td rowspan="${num!}"   >
	                	   <#if  subjectList[j].stuSubjectAchiDto.coomunication?default('') == "较强" >
                               √
						   <#else>
			                	&nbsp;
						   </#if>
                </td>
                <td rowspan="${num!}"   >
	                	<#if  subjectList[j].stuSubjectAchiDto.coomunication?default('') == "需加强" >
                            √
						<#else>
			                	&nbsp;
						</#if>
                </td>
                <td rowspan="${num!}"  >
	                		<#if  subjectList[j].stuSubjectAchiDto.discovery?default('') == "强" >
                                √
							<#else>
			                	&nbsp;
							</#if>
                </td>
                <td rowspan="${num!}"   >
	                	   <#if  subjectList[j].stuSubjectAchiDto.discovery?default('') == "较强" >
                               √
						   <#else>
			                	&nbsp;
						   </#if>
                </td>
                <td rowspan="${num!}"  >
	                	<#if  subjectList[j].stuSubjectAchiDto.discovery?default('') == "需加强" >
                            √
						<#else>
			                	&nbsp;
						</#if>
                </td>


            </tr>
					<#if (num-1) gt 0 >
						<#list 1..num-1 as i >
					 <tr  >

                         <td style="font-size:10.5px;" >${subjectList[j].cateGoryList[i].categoryName!}</td>
							<#if subjectList[j].cateGoryList[i].categoryName =="测试" >
		            				  <td>${subjectList[j].cateGoryList[i].stuSubjectAchiDto.psachi!}</td>
			                	     <td>${subjectList[j].cateGoryList[i].stuSubjectAchiDto.qmachi!}</td>
							<#else>
			                	     <td colspan="2">${subjectList[j].cateGoryList[i].stuSubjectAchiDto.qmachi!}</td>
							</#if>
                     </tr>
						</#list>

					</#if>
				</#list>

			</#if>
			</tbody>
		</table>
	</div>
	<div class="col-sm-6" style="float:left;width:50%;height:50%">
		<table class="table table-bordered table-condensed table-print text-center" style="font-size:10.5px;">
			<tbody>
				<tr style="font-size:10.5px;">

                    <td rowspan="2" width="8%" >学科</td>
                    <td rowspan="2" width="12%">要素</td>
                    <td rowspan="2" width="8%">平时成绩</td>
                    <td rowspan="2" width="8%">期末成绩</td>
                    <td colspan="3" width="20%" >学习态度</td>
                    <td colspan="3" width="22%">交流与合作能力</td>
                    <td colspan="3" width="22%">发现、提出问题能力</td>
				</tr>
				
				<tr style="font-size:10.5px;">
				    <td >好</td>
					<td >较好</td>
					<td >需努力</td>
					<td >强</td>
					<td >较强</td>
					<td >需加强</td>
					<td >强</td>
					<td >较强</td>
					<td >需加强</td>
				</tr>
				
				<#--<#if stuSubjectList?exists && stuSubjectList?size gt 3 >-->


            	<#--<#list 3..(stuSubjectList?size-1) as j>-->
            <#--<#assign num = (stuSubjectList[j].stuPartInfoScoreList?size) >-->
            <#--<tr   >-->
            		<#---->
            		<#--<td height="15" rowspan="${num!}"  <#if !stuSubjectList[j].hasPart > colspan="2" </#if>  style="font-size:10.5px;" >${stuSubjectList[j].subjectName!}</td>-->
            			<#--<#if stuSubjectList[j].stuPartInfoScoreList?exists && stuSubjectList[j].stuPartInfoScoreList?size gt 0 &&  stuSubjectList[j].hasPart  >-->
		            				 <#--<td style="font-size:10.5px;" >${stuSubjectList[j].stuPartInfoScoreList[0].partName!}</td>-->
		            				 <#--<#if stuSubjectList[j].stuPartInfoScoreList[0].partName?default('') =="测试" >-->
		            				  <#--<td>${stuSubjectList[j].stuPartInfoScoreList[0].partPsScore!}</td>-->
			                	     <#--<td>${stuSubjectList[j].stuPartInfoScoreList[0].partQmScore!}</td>-->
			                	     	<#--<#else>-->
			                	     <#--<td colspan="2">${stuSubjectList[j].stuPartInfoScoreList[0].partQmScore!}</td>-->
		            				 <#--</#if>-->
			                	    <#---->
		            	<#--<#else>-->
		            	    <#--<td>${stuSubjectList[j].psScore!}</td>-->
		            		<#--<td>${stuSubjectList[j].score!}</td>-->
            			<#--</#if>-->
            			<#---->
	                	<#--<td rowspan="${num!}"  >-->
	                		<#--<#if  stuSubjectList[j].manner?default('')== "1" >-->
                                <#--√-->
			                <#--<#else>-->
			                	<#--&nbsp;-->
	                		<#--</#if>-->
	                	<#--</td>-->
	                	<#--<td rowspan="${num!}"  >-->
	                	   <#--<#if  stuSubjectList[j].manner?default('') == "2" >-->
                               <#--√-->
			                <#--<#else>-->
			                	<#--&nbsp;-->
	                		<#--</#if>-->
	                		<#--</td>-->
	                	<#--<td rowspan="${num!}" >-->
	                	<#--<#if  stuSubjectList[j].manner?default('') == "3"  ||  stuSubjectList[j].manner?default('') == "4">-->
                            <#--√-->
			                <#--<#else>-->
			                	<#--&nbsp;-->
	                		<#--</#if>-->
	                		<#--</td>-->
	                	<#--<td rowspan="${num!}" >-->
	                		<#--<#if  stuSubjectList[j].communication?default('') == "1" >-->
                                <#--√-->
			                <#--<#else>-->
			                	<#--&nbsp;-->
	                		<#--</#if>-->
	                	<#--</td>-->
	                	<#--<td rowspan="${num!}"   >-->
	                	   <#--<#if  stuSubjectList[j].communication?default('') == "2" >-->
                               <#--√-->
			                <#--<#else>-->
			                	<#--&nbsp;-->
	                		<#--</#if>-->
	                		<#--</td>-->
	                	<#--<td rowspan="${num!}"   >-->
	                	<#--<#if  stuSubjectList[j].communication?default('') == "3" >-->
                            <#--√-->
			                <#--<#else>-->
			                	<#--&nbsp;-->
	                		<#--</#if>-->
	                		<#--</td>-->
	                	<#--<td rowspan="${num!}"  >-->
	                		<#--<#if  stuSubjectList[j].discovery?default('') == "1" >-->
                                <#--√-->
			                <#--<#else>-->
			                	<#--&nbsp;-->
	                		<#--</#if>-->
	                	<#--</td>-->
	                	<#--<td rowspan="${num!}"   >-->
	                	   <#--<#if  stuSubjectList[j].discovery?default('') == "2" >-->
                               <#--√-->
			                <#--<#else>-->
			                	<#--&nbsp;-->
	                		<#--</#if>-->
	                		<#--</td>-->
	                	<#--<td rowspan="${num!}"  >-->
	                	<#--<#if  stuSubjectList[j].discovery?default('') == "3" >-->
                            <#--√-->
			                <#--<#else>-->
			                	<#--&nbsp;-->
	                		<#--</#if>-->
	                		<#--</td>-->
		<#--</tr>-->
				<#--<#if (num-1) gt 0 >-->
				<#--<#list 1..num-1 as i >-->
					 <#--<tr   >-->
					 	<#--<td style="font-size:10.5px;">${stuSubjectList[j].stuPartInfoScoreList[i].partName!}</td>-->
				         <#--<#if stuSubjectList[j].stuPartInfoScoreList[i].partName =="测试" >-->
		            		<#--<td>${stuSubjectList[j].stuPartInfoScoreList[i].partPsScore!}</td>-->
			                <#--<td>${stuSubjectList[j].stuPartInfoScoreList[i].partQmScore!}</td>-->
			                <#--<#else>-->
			                <#--<td colspan="2">${stuSubjectList[j].stuPartInfoScoreList[i].partQmScore!}</td>-->
		            	<#--</#if>-->
					 <#--</tr>-->
				<#--</#list>-->
				<#---->
				<#--</#if>-->
            <#--</#list>-->
            <#---->
            <#--</#if>-->
				
				<#if subjectList?exists && subjectList?size gt 3 >


					<#list 3..(subjectList?size-1) as j>
						<#if subjectList[j].cateGoryList?exists >
							<#assign num = (subjectList[j].cateGoryList?size) >
						<#else>
							<#assign num = 1 >
						</#if>

            <tr   >

                <td height="15" rowspan="${num!}"  <#if num ==1 > colspan="2" </#if>  style="font-size:10.5px;" >${subjectList[j].name!}</td>
            			<#if subjectList[j].cateGoryList?exists && subjectList[j].cateGoryList?size gt 0  >
		            				 <td style="font-size:10.5px;" >${subjectList[j].cateGoryList[0].categoryName!}</td>
							<#if subjectList[j].cateGoryList[0].categoryName?default('') =="测试" >
		            				  <td>${subjectList[j].cateGoryList[0].stuSubjectAchiDto.psachi!}</td>
			                	     <td>${subjectList[j].cateGoryList[0].stuSubjectAchiDto.qmachi!}</td>
							<#else>
			                	     <td colspan="2">${subjectList[j].cateGoryList[0].stuSubjectAchiDto.qmachi!}</td>
							</#if>

						<#else>
		            	    <td>${subjectList[j].stuSubjectAchiDto.psachi!}</td>
		            		<td>${subjectList[j].stuSubjectAchiDto.qmachi!}</td>
						</#if>

                <td rowspan="${num!}"  >
	                		<#if  subjectList[j].stuSubjectAchiDto.xxtd?default('')== "好" >
                                √
							<#else>
			                	&nbsp;
							</#if>
                </td>
                <td rowspan="${num!}"  >
	                	   <#if  subjectList[j].stuSubjectAchiDto.xxtd?default('') == "较好" >
                               √
						   <#else>
			                	&nbsp;
						   </#if>
                </td>
                <td rowspan="${num!}" >
	                	<#if  subjectList[j].stuSubjectAchiDto.xxtd?default('') == "需努力"  >
                            √
						<#else>
			                	&nbsp;
						</#if>
                </td>
                <td rowspan="${num!}" >
	                		<#if  subjectList[j].stuSubjectAchiDto.coomunication?default('') == "强" >
                                √
							<#else>
			                	&nbsp;
							</#if>
                </td>
                <td rowspan="${num!}"   >
	                	   <#if  subjectList[j].stuSubjectAchiDto.coomunication?default('') == "较强" >
                               √
						   <#else>
			                	&nbsp;
						   </#if>
                </td>
                <td rowspan="${num!}"   >
	                	<#if  subjectList[j].stuSubjectAchiDto.communication?default('') == "需加强" >
                            √
						<#else>
			                	&nbsp;
						</#if>
                </td>
                <td rowspan="${num!}"  >
	                		<#if  subjectList[j].stuSubjectAchiDto.discovery?default('') == "强" >
                                √
							<#else>
			                	&nbsp;
							</#if>
                </td>
                <td rowspan="${num!}"   >
	                	   <#if  subjectList[j].stuSubjectAchiDto.discovery?default('') == "较强" >
                               √
						   <#else>
			                	&nbsp;
						   </#if>
                </td>
                <td rowspan="${num!}"  >
						<input type="hidden" value="${subjectList[j].stuSubjectAchiDto.discovery?default('') }">
	                	<#if  subjectList[j].stuSubjectAchiDto.discovery?default('') == "需加强" >
                            √
						<#else>
			                	&nbsp;
						</#if>
                </td>
            </tr>
						<#if (num-1) gt 0 >
							<#list 1..num-1 as i >
					 <tr   >
                         <td style="font-size:10.5px;">${subjectList[j].cateGoryList[i].categoryName!}</td>
				         <#if subjectList[j].cateGoryList[i].categoryName =="测试" >
		            		<td>${subjectList[j].cateGoryList[i].stuSubjectAchiDto.psachi!}</td>
			                <td>${subjectList[j].cateGoryList[i].stuSubjectAchiDto.qmachi!}</td>
						 <#else>
			                <td colspan="2">${subjectList[j].cateGoryList[i].stuSubjectAchiDto.qmachi!}</td>
						 </#if>
                     </tr>
							</#list>

						</#if>
					</#list>

				</#if>


			</tbody>
		</table>
		<div style="font-size:10.5px;" >
            <p>注 1、表中除测试栏填分数外，其他均填等第；2、等第分为优秀、良好、合格、待合格四级；</p>
            <p>3、学习态度等三项请班主任根据任课教师提供的情况在相应的栏目打“√”</p>
		</div>

	</div>
</div>