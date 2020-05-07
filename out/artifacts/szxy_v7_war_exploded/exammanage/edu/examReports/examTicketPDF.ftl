<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>准考证</title>

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
			<div class="main-content" style="background:#fff;">
				<div class="main-content-inner">
					<div class="">
						<div class="">
							<div class="tab-pane active" role="tabpanel">
								<div class="">
									<#-- <div class="box-header">
										<h3 class="box-title"><span class="mr70">学校：${schoolName!}</span><span class="mr70">班级：${className!}</span></h3>
									</div> -->
									<div class="">
			<#if dtos?exists && dtos?size gt 0>
				<#list dtos as dto>
			<div class="row"  style="page-break-inside:avoid;">
					<div class="col-xs-6">
					<table class="table table-bordered exam-table">
							<tbody>
								<tr>
									<td align="center" style="font-size: 26px;">准考证</td>
									<td width="120" rowspan="5"><img width="120" height="174" src="${request.contextPath}${dto.studentFilePath!}"></td>
								</tr>
								<tr>
									<td>考号:${dto.examNumber!}</td>
								</tr>
								<tr>
									<td>姓名:${dto.name!}</td>
								</tr>
								<tr>
									<td>学号:${dto.unitiveCode!}</td>
								</tr>
								<tr>
									<td>学校:${dto.schoolName!}</td>
								</tr>
								<tr>
									<td colspan="2"><span class="mr30">考区:${dto.examRegionName!}</span>&nbsp;&nbsp;<span>考点:${dto.examOptionName!}</span></td>
								</tr>
								<tr>
									<td colspan="2"><span class="mr30">考场:${dto.examPlace!}</span>&nbsp;&nbsp;<span>座位号:${dto.seatNum!}</span>&nbsp;&nbsp;<span>考点地址:${dto.examPlaceAdd!}</span></td>
								</tr>
							</tbody>
						 </table>
						<#-- <table class="table table-bordered">
							<tbody>
								<tr>
									<td>考号：${dto.examNumber!}</td>
									<td width="71" rowspan="3"><img width="71" height="99" src="${request.contextPath}${dto.studentFilePath!}"></td>
								</tr>
								<tr>
									<td>姓名：${dto.name!}</td>
								</tr>
								<tr>
									<td>学校：${dto.schoolName!}</td>
								</tr>
								<tr>
									<td colspan="2">学籍号：${dto.unitiveCode!}</td>
								</tr>
								<tr>
									<td colspan="2"><span class="mr10">考区：${dto.examRegionName!}</span><span>考点：${dto.examOptionName!}</span></td>
								</tr>
								<tr>
									<td colspan="2"><span class="mr10">考场：${dto.examPlace!}</span><span>考点地址：${dto.examPlaceAdd!}</span></td>
								</tr>
							</tbody>
						 </table> -->
					</div>
					<div class="col-xs-6">
					<#if subDtos?exists && subDtos?size gt 0>
						<table class="table table-bordered text-center exam-table">
						<tbody>
							<tr>
								<td colspan="${subDtos?size}" style="height:50px;font-size: 16px;">${examName!}【日程表】</td>
							</tr>
							<tr>
								<#list subDtos as subDateDto>
								<td>${subDateDto.date!}</td>
								</#list>
							</tr>
							<#list 1..maxTr as i>
								<tr>
									<#list 1..subDtos?size as j>
									<#if subDtos[j-1].subDtos?size gt (i-1)>
										<td><b>${subDtos[j-1].subDtos[i-1].subName!}</b><br>${subDtos[j-1].subDtos[i-1].startDate!}-${subDtos[j-1].subDtos[i-1].endDate!}</td>
									<#else>
										<td><b></b><br></td>
									</#if>
									</#list>
								</tr>
							</#list>
						</tbody>
					</table>
					</#if>
				</div>
			</div>
				</#list>
			</#if>	
		</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
