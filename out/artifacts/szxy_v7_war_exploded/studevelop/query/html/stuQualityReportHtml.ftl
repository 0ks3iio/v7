<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>学生成长档案</title>

		<meta name="description" content="" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

		<link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap/dist/css/bootstrap.css" />
		<link rel="stylesheet" href="${request.contextPath}/static/components/font-awesome/css/font-awesome.css" />
		<link rel="stylesheet" href="${request.contextPath}/static/components/chosen/chosen.min.css">
		<link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap-datepicker/dist/css/bootstrap-datepicker3.css">
		<link rel="stylesheet" href="${request.contextPath}/static/css/iconfont.css">
		<link rel="stylesheet" href="${request.contextPath}/static/css/components.css">
		<link rel="stylesheet" href="${request.contextPath}/static/css/page-desktop.css">
		<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css">
		<script src="${request.contextPath}/static/assets/js/ace-extra.js"></script>
		<style>
			.prt-report-wrap{width:100%;}
			.prt-report{width:1000px;margin:30px auto;font-size:16px;}
			.prt-report-left{float:left;width:50%;text-align:left;height:670px;}
			.prt-report-right{float:right;width:50%;text-align:right;height:670px;}
			.prt-report table{width:95%;border-collapse:collapse;}
			.prt-report table th,.prt-report table td{border:2px solid #484848;color:#000;}
			.prt-report table th{font-size:16px;text-align:center;font-weight:700;}
			.prt-report table td{font-size:14px;padding:3px 0;}
			.prt-report table td.tt{text-align:center;font-weight:700;}
			.prt-report table.prt-table td{text-align:center;}
			.prt-report table.prt-table td.t-left{text-align:left;padding:3px 5px;}
			.prt-report .lk{padding:10px 30px 0 0;margin-top:-10px;margin-bottom:0;font-weight:700;text-align:right;}
			.prt-report .tips{padding:0px 0 0 35px;margin-bottom:0;overflow:hidden;zoom:1;}
			.prt-report .tips span{float:left;display:inline-block;height:40px;line-height:40px;}
			.prt-report .tips span.time{width:120px;}
			.prt-report .tips span.sk{width:40px;line-height:20px;}
			.prt-report h1,.prt-report h2{padding-top:100px;margin-top:0;margin-bottom:12px;text-align:center;font-size:18px;font-weight:700;color:#000;}
			.prt-report h2{padding-top:30px;font-size:16px;}
			.prt-report .list{padding-top:150px;}
			.prt-report .list li{height:35px;padding-left:100px;text-align:left; vertical-align:text-bottom}
			.prt-report .list li span{display:inline-block;width:150px;border-bottom:1px solid #000;vertical-align:bottom;}
			.prt-report .list li.last{padding-left:0;margin-top:20px;text-align:center;}
			.prt-report .list li.last span{display:inline-block;width:60px;margin-right:10px;vertical-align:bottom;}
		</style>
	</head>

	<body class="no-skin">
		<!-- #section:basics/navbar.layout -->
		<!-- PAGE CONTENT BEGINS -->
			<div class="box-body">
				<div class="print">
					<div class="prt-report-wrap">
					    <div class="prt-report print clearfix" style="width:1000px;">
					    	<!--第一部分 左上-->
					        <div class="prt-report-left" style="padding-top: 70px;">
					        	<table>
					        		<tbody>
					        			<tr>
						        			<th width="30" height="150" align="center">奖<br>惩<br>记<br>载</th>
						        			<td style="font-size:14px">
												<div style="padding-left:5px;padding-right: 5px">
													<#if stuDevelopRewardsList?exists && (stuDevelopRewardsList?size > 0)>
													 奖励：
												        <#list stuDevelopRewardsList as item>
												            ${item.rewardsname!} <#if item.remark?exists && item.remark?default('')!='' >,</#if>${item.remark!}&nbsp;&nbsp;&nbsp;
												        </#list>
												    </#if>
												    <br>
												    <#if stuDevelopPunishmentList?exists && (stuDevelopPunishmentList?size > 0)>
												             惩罚：
												        <#list stuDevelopPunishmentList as item>
												             ${item.punishname!}<#if item.remark?exists && item.remark?default('')!='' >,</#if>${item.remark!}&nbsp;&nbsp;&nbsp;
												        </#list>
												    </#if>
						                        </div>
						        			</td>
						        		</tr>
						                <tr>
						                	<th height="210" align="center">教<br>师<br>寄<br>语</th>
						                	<td style="font-size:14px">
						                        <div style="padding-left:5px;padding-right: 5px">
						                			<#if stuEvaluateRecord?exists>
								                         ${stuEvaluateRecord.teacherEvalContent!}
												    </#if>
						                        </div>
						                	</td>
						                </tr>
						                <tr>
						                	<th height="120" align="center">父<br>母<br>的<br>期<br>望</th>
						                	<td><#if stuEvaluateRecord?exists>
							                         ${stuEvaluateRecord.parentEvalContent!}
											    </#if>
											</td>
						                </tr>
					        		</tbody>
					        	</table>
					            <p class="tips"><span><#if stuCheckAttendance.registerBegin?exists>
											${stuCheckAttendance.registerBegin!}
											<#else>${sem.registerDate?string("yyyy-MM-dd")!}
											</#if>报到 定于
											<#if stuCheckAttendance.studyBegin?exists>${stuCheckAttendance.studyBegin!}
											<#else>${sem.semesterBegin?string("yyyy-MM-dd")!}</#if>开学
											</span></p>
					            <p class="lk">${regionName!}教育局监制</p>
					        </div>
					        <!--第二部分 右上-->
					        <div class="prt-report-right" style="padding-top: 70px;position:relative;left:20px;">
					        	<h1 style="font-size:25px;padding-top: 20px;">
					           		${regionName!}
								</h1>
					        	<h1 style="font-size:25px;padding-top: 20px;">小 学 生 素 质 报 告 单</h1>
					            <h2 style="padding-top: 40px;"><span class="xn">${acadyear!}</span>学年<span class="xq">${mcodeSetting.getMcode("DM-XQ",semester?default('1'))}</span></h2>
					            <ul class="list" style="padding-top: 210px;">
					                <li style="padding-left: 150px;padding-top: 5px;">学&nbsp;&nbsp;校<span style="min-width:150px;width:auto;text-align:center;">${school.schoolName!}</span></li>
					                <li style="padding-left: 150px;padding-top: 5px;">班&nbsp;&nbsp;级<span style="min-width:150px;width:auto;text-align:center;">${className!}</span></li>
					                <li style="padding-left: 150px;padding-top: 5px;">姓&nbsp;&nbsp;名<span style="min-width:150px;width:auto;text-align:center;">${student.studentName!}</span></li>
					                <li class="last">校长<span>${school.schoolmaster!}</span>教导主任<span>${school.partyMaster!}</span>班主任<span>${teacher.teacherName!}</span></li>
					            </ul>
					        </div>
					        <!--第三部分 左下-->
					  		<div class="prt-report-left" style="page-break-before:always;padding-top: 20px;"> 
					    		<table class="prt-table" style="height: 430px;">
						    		<tbody>
							    		<tr>
							    			<th rowspan="12" width="5%">思<br>想<br>品<br>德<br>素<br>质</th>
							    			<td rowspan="2" width="77%" class="tt">项&nbsp;&nbsp;目</td>
							    			<td colspan="3" width="18%" class="tt">评 估 结 果</td>
							    		</tr>
							    		<tr>
							            	<td class="tt" width="6%">好</td>
							            	<td class="tt" width="6%">较好</td>
							            	<td class="tt" width="6%">需努力</td>
							            </tr>
							    		<#list 0..9 as x>
											<#if qualityOfMind?size gt x>
					                			<tr>
					                                <td class="t-left">${x+1}、${(qualityOfMind[x])[0]!}</td>
					                                <td>
								                		<#if (qualityOfMind[x][1]!"") == 'A'>√<#else>&nbsp;</#if>
					                                </td>
					                                <td>
								                		<#if (qualityOfMind[x][1]!"") == 'D'>√<#else>&nbsp;</#if>
					                                </td>
					                                <td>
								                		<#if (qualityOfMind[x][1]!"") == 'E'>√<#else>&nbsp;</#if>
					                                </td>
					                            </tr>
											<#else>
					                			<tr>
								                	<td class="t-left">&nbsp;</td>
								                	<td>&nbsp;</td>
								                	<td>&nbsp;</td>
								                	<td>&nbsp;</td>
									            </tr>
											</#if>
										</#list>
					    			</tbody>
					    		</table>
					        	<table class="prt-table" style="height: 200px;margin-top:-2px;border-top:0px">
						        	<tbody>
						        		<#if stuHealthRecordDtoList?exists && stuHealthRecordDtoList?size gt 0 >
						                    <#list 0..7 as ind>
						                        <tr>
						                            <#if ind_index == 0 >
						                                <th width="5%" style="border-top: none;" rowspan="8">身<br>体<br>心<br>理<br>素<br>质</th>
						                            </#if>
						                            <#if stuHealthRecordDtoList[ind]?exists>
					                                 	<#assign dtoList = stuHealthRecordDtoList[ind] />
							                            <#if ind_index == 6 >
								                            <td class="tt">身 高</td>
											            	<td colspan="2">${dtoList[1].nameOrValue!}cm</td>
											            	<td class="tt" rowspan="2">视 力</td>
											            	<td class="tt">左</td>
											            	<td colspan="2">${dtoList[3].nameOrValue!}</td>
								                        <#elseif ind_index == 7>
								                        	<td class="tt">体 重</td>
											            	<td colspan="2">${dtoList[1].nameOrValue!}kg</td>
											            	<td class="tt">右</td>
											            	<td colspan="2">${dtoList[3].nameOrValue!}</td>
								                        <#else>
						                                    <#list dtoList as dto>
						                                    	<td style="border-top: none;" <#if dto_index==0>width="11%" class="tt"<#elseif dto_index%2==0>width="8%" align="center"<#else>width="20%" class="tt"</#if>>
						                                    		<#if dto.nameOrValue?default('') == "selected" >√<#else>${dto.nameOrValue!}</#if>
						                                    	</td>
						                                    </#list>
							                            </#if>
						                            <#else>
						                                <td colspan="7"></td>
						                            </#if>
						                        </tr>
						                    </#list>
						                </#if>
					        		</tbody>
					    		</table>
							</div>
							<!--第四部分 -->
					    	<div class="prt-report-right" style="padding-top: 20px;position:relative;left:20px;">
						    	<table class="prt-table" style="height: 380px;">
						        	<tbody>
							        	<tr>
							        		<th width="6%" rowspan="8">文<br>化<br>素<br>质</th>
							        		<td width="20%" class="tt">学 科</td>
							        		<td width="9%" class="tt">平时成绩</td>
							        		<td width="9%" class="tt">期末成绩</td>
							        		<td width="9%" class="tt">学习态度</td>
							        		<td width="20%" class="tt">学 科</td>
							        		<td width="9%" class="tt">平时成绩</td>
							        		<td width="9%" class="tt">期末成绩</td>
							        		<td width="9%" class="tt">学习态度</td>
							        	</tr>
							        	<#list 0..6 as i>
							        		<tr>
								        	<#if achilist?exists && (achilist?size>i*2)>
						            		    <td style="word-break: break-all;word-wrap: break-word;" class="tt">${achilist[i*2].subname!}</td>
							                	<td>${achilist[i*2].psachi!}</td>
							                	<td>${achilist[i*2].qmachi!}</td>
							                	<td>${achilist[i*2].xxtd!}</td>
								           <#else>
								           		<td class="tt">&nbsp;</td>
							                	<td>&nbsp;</td>
							                	<td>&nbsp;</td>
							                	<td>&nbsp;</td>
								           </#if>
								           <#if (achilist?size>i*2+1)>
							                	<td style="word-break: break-all;word-wrap: break-word;" class="tt">${achilist[i*2+1].subname!}</td>
							                	<td>${achilist[i*2+1].psachi!}</td>
							                	<td>${achilist[i*2+1].qmachi!}</td>
							                	<td>${achilist[i*2+1].xxtd!}</td>
								            <#else>
							           		    <td class="tt">&nbsp;</td>
							                	<td>&nbsp;</td>
							                	<td>&nbsp;</td>
							                	<td>&nbsp;</td>
											</#if>    
											</tr>
										</#list>      	
						        	</tbody>
						  	  	</table>
					 	  		<table class="prt-table" style="height: 150px;margin-top:-2px;border-top:0px">
						        	<tbody>
						        		<tr>
							        		<th width="6%" style="border-top: none;">兴<br>趣<br>爱<br>好</th>
							        		<td width="44%" style="border-top: none;"><#if stuEvaluateRecord?exists>${stuEvaluateRecord.hobby!}</#if></td>
							        		<td width="6%" class="tt" style="border-top: none;">特<br>长</td>
							        		<td width="44%" style="border-top: none;"><#if stuEvaluateRecord?exists>${stuEvaluateRecord.strong!}</#if></td>
						        		</tr>
						        	</tbody>
						        </table>
						     	<table class="prt-table" style="height: 132px;margin-top:-2px;border-top:0px">
						        	<tbody>
						        		<tr>
							        		<th width="6%" style="border-top: none;" rowspan="2">出<br>勤<br>情<br>况</th>
							        		<td width="19%" style="border-top: none;" class="tt">本学期上课</td>
							        		<td width="15%" style="border-top: none;">${stuCheckAttendance.studyDate!}天</td>
							        		<td width="15%" class="tt" style="border-top: none;">事假</td>
							        		<td width="15%" style="border-top: none;">${stuCheckAttendance.businessVacation!}天</td>
							        		<td width="15%" class="tt" style="border-top: none;">旷课</td>
							        		<td width="15%" style="border-top: none;">${stuCheckAttendance.wasteVacation!}节</td>
						    			</tr>
						       		 	<tr>
						       		 		<#assign lateAndleaveEarly = stuCheckAttendance.late+stuCheckAttendance.leaveEarly>
							            	<td class="tt">出席</td>
							            	<td>${stuCheckAttendance.studyDate-stuCheckAttendance.businessVacation-stuCheckAttendance.illnessVacation}天</td>
							            	<td class="tt">病假</td>
							            	<td>${stuCheckAttendance.illnessVacation!}天</td>
							            	<td class="tt">迟到<br>早退</td>
							            	<td>${lateAndleaveEarly!}次</td>
						           		 </tr>
						       		 </tbody>
							 	</table>
							</div>
						</div>
					</div>
				</div>				 				
		</div>	
		<!-- PAGE CONTENT ENDS -->
	</body>
</html>
