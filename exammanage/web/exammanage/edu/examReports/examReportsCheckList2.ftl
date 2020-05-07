<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>pdf</title>

		<meta name="description" content="" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap/dist/css/bootstrap.css" />
		<link rel="stylesheet" href="${request.contextPath}/static/components/font-awesome/css/font-awesome.css" />
		
		<link rel="stylesheet" href="${request.contextPath}/static/css/iconfont.css">
		<link rel="stylesheet" href="${request.contextPath}/static/css/components.css">
		<link rel="stylesheet" href="${request.contextPath}/static/css/page-desktop.css">
		<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css">
	</head>

	<body class="no-skin">
			<div class="main-content"  style="background:#fff;">
				<div class="main-content-inner">
					<div class="">
						<div class="">
							<div class="tab-pane active" role="tabpanel">
								<div class="">
									<#-- <div class="box-header">
										<h3 class="box-title"><span class="mr70">考区：${regionName!}</span><span class="mr70">考点：${optionName!}</span><span>考场：${placeName!}</span></h3>
									</div>-->
									<div class="">
										<#if emPlaceStudents?exists && emPlaceStudents?size gt 0>
										<#list emPlaceStudents as item>
											<#if item_index %3 == 0>
											<div class="row" style="page-break-inside:avoid;">
											</#if>
											<div class="col-xs-4">
												<table class="table table-bordered">
													<tbody>
														<tr>
															<td>
																<p>考号：</p>
																<p>${item.examNumber!}</p>
																<p>姓名：${item.studentName!}</p>
																<p>学校：${item.schName!}</p>
															</td>
															<td width="63"><img width="63" height="92" src="${request.contextPath}${item.studentFilePath!}"></td>
														</tr>
													</tbody>
												 </table>
											</div>
											<#if item_index %3 == 2>
											</div>
											</#if>
											</#list>
										</#if>
										
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div><!-- /.main-content -->
		</div><!-- /.main-container -->
	</body>
</html>
