<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>考场门贴</title>

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
									</div> -->
									<div class="">
										<#if emPlaceStudents?exists && emPlaceStudents?size gt 0>
										<#list emPlaceStudents as item>
											<#if item_index %2 == 0>
											<div class="row" style="page-break-inside:avoid;">
											</#if>
											<div class="col-xs-6" style="page-break-inside:avoid;">
												<table class="table table-bordered">
													<tbody>
														<tr>
															<td colspan="2">考号：${item.examNumber!}</td>
															<td width="123" rowspan="4"><img width="123" height="180" src="${request.contextPath}${item.studentFilePath!}"></td>
														</tr>
														<tr>
															<td colspan="2">姓名：${item.studentName!}</td>
														</tr>
														<tr>
															<td colspan="2">考点：${optionName!}</td>
														</tr>
														<tr>
															<td>考场：${placeName!}</td>
															<td>座位号：${item.seatNum!}</td>
														</tr>
													</tbody>
												 </table>
											</div>	
											<#if item_index %2 == 1>
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
