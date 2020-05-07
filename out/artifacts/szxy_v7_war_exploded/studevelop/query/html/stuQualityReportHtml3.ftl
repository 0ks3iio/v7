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
	</head>

	<body class="no-skin">
		<!-- #section:basics/navbar.layout -->
		<div id="navbar" class="navbar navbar-default navbar-fixed-top">
			<!-- /section:basics/sidebar -->
			<div class="main-content">
				<div class="main-content-inner">
					<div class="page-content">
						<div class="row">
							<div class="col-xs-12">
								<!-- PAGE CONTENT BEGINS -->
								<div class="box box-default">
									<div class="box-body">
										<div class="print">
								           <div class="row">
												<div class="col-sm-6" style="float:left;width:50%;height:50%;">
												    <h4 class="text-center"><b>道德健康素质</b></h4>
													<table class="table table-condensed table-print text-center">
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
															<#if bodyList?exists && bodyList?size gt 0 >
																<#list bodyList as body>
																	<tr>
																		<#list body as dto>
																		   <td>${dto.nameOrValue!}</td>
																		</#list>
											                        </tr>
																</#list>
															</#if>
															<tr>
															    <td>视力</td>
															    <td td colspan="3">左眼：${stuHealthRecordDto.leftEye!}   右眼：${stuHealthRecordDto.rightEye!}</td>
															</tr>
															<tr>
															    <td>身高</td>
															    <td>${stuHealthRecordDto.height!}</td>
															    <td>体重</td>
															<td>${stuHealthRecordDto.weight!}</td>
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
														<#if subjectList?exists && subjectList?size gt 0 >
															<#list subjectList as subject>
																<#if subject.cateGoryList ?exists >
																	<#assign num = subject.cateGoryList?size >
																<#else>
																	<#assign num = -1 >
																</#if>
											
											
																<#if subject.cateGoryList ?exists  && subject.cateGoryList?size gt 0>
																	<#assign num = subject.cateGoryList?size >
																	<#list subject.cateGoryList as cateGory>
																	<tr>
																		<#if cateGory_index == 0>
																			<td rowspan="${num}"> ${subject.name!}</td>
																		</#if>
																		<td>${cateGory.categoryName!}</td>
																		<#if cateGory.categoryName == '测试' >
																		<td colspan="2">${cateGory.stuSubjectAchiDto.qmachi!}</td>
																			<#else>
																		<td>${cateGory.stuSubjectAchiDto.psachi!}</td>
																		<td>${cateGory.stuSubjectAchiDto.qmachi!}</td>
																		</#if>
											
																		<td></td>
											                        </tr>
																	</#list>
																<#else>
																	<tr>
											
																		<td colspan="2"> ${subject.name!}</td>
																		<#if subject.stuSubjectAchiDto?exists>
																			<td>${subject.stuSubjectAchiDto.psachi!}</td>
																			<td>${subject.stuSubjectAchiDto.qmachi!}</td>
																		<#else>
																			<td></td>
																			<td></td>
																		</#if>
											
											                            <td></td>
											                        </tr>
																</#if>
															</#list>
														</#if>
														</tbody>
													</table>
												</div>
											</div>
										</div>				 				
									</div>
								</div>	
								<!-- PAGE CONTENT ENDS -->
							</div><!-- /.col -->
						</div><!-- /.row -->
					</div><!-- /.page-content -->
				</div>
			</div><!-- /.main-content -->
		</div><!-- /.main-container -->
	</body>
</html>
