<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>考场桌贴</title>

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
									<div class="">
										<#if (dtoList?exists && dtoList?size>0)>
										<#list dtoList as item>
											<#if item_index % 3 == 0>
												<div class="row" style="page-break-inside:avoid;">
											</#if>
											 <div class="col-xs-4" style="page-break-inside:avoid;">
							                   <div class="box-boder exaRoom-border">
											         <h1 class="exaRoom-fontSize text-center">${item.seatNum!}</h1>
											         <table class="table table-bordered table-striped table-condensed table-hover exaRoom-table">
														<tbody>
															<tr>
																<td class="text-center">姓名：</td>
																<td>${item.student.studentName!}</td>
															</tr>
															<tr>
																<td class="text-center">考号：</td>
																<td>${item.examNumber!}</td>
															</tr>
															<tr>
																<td class="text-center">班级：</td>
																<td>${item.className!}</td>
															</tr>
															<tr>
																<td class="text-center">考场：</td>
																<td>${emPlace.examPlaceCode!}</td>
															</tr>
														</tbody>
													 </table>
											   </div>
										   </div>
										   <#if item_index % 3 == 2>
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
