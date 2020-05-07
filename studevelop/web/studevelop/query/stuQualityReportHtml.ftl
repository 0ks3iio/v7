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

	<#assign DEPLOY_REGION_BINJIANG = stack.findValue("@net.zdsoft.studevelop.data.constant.StuDevelopConstant@DEPLOY_BINJIANG") >
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
								            <#--&&  stuDevelopQualityReportSet?exists-->
										<#if deployRegion?default('') != DEPLOY_REGION_BINJIANG &&  stuDevelopQualityReportSet?exists >
										    <#if stuDevelopQualityReportSet.section == 1>
										         <#if stuDevelopQualityReportSet.template == '1'>
										             <#include "/studevelop/query/stuQualityReportTemplate.ftl">
										         <#elseif stuDevelopQualityReportSet.template == '2'>
										             <#include "/studevelop/query/stuQualityReportTemplate3.ftl">
										         </#if>
										    <#elseif stuDevelopQualityReportSet.section == 2>
										         <#if stuDevelopQualityReportSet.template == '1'>
										             <#include "/studevelop/query/stuQualityReportTemplate2.ftl">
										         <#elseif stuDevelopQualityReportSet.template == '2'>
										             <#include "/studevelop/query/stuQualityReportTemplate4.ftl">
										         </#if>
										    </#if>
										 <#else>
											<#include "/studevelop/query/stuQualityReportTemplate5.ftl">
										</#if>
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
