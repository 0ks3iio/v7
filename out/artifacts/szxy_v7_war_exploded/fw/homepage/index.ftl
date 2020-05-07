<!DOCTYPE html>
<html lang="en">
	<head>
		<script>
		_contextPath = "${request.contextPath}";
		</script>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<script>
		_contextPath = "${request.contextPath}";
		</script>
		<title>${frameworkEnv.getString("platform_name")!"首页"}</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
		<!--[if !IE]> -->
		<!-- <link rel="stylesheet" href="${request.contextPath}/static/ace/assets/css/pace.css" />
		<script data-pace-options='{ "ajax": true, "document": true, "eventLag": false, "elements": false }' src="${request.contextPath}/static/ace/components/PACE/pace.js"></script> -->
		<!-- <![endif]-->
		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="${request.contextPath}/static/ace/assets/css/bootstrap.css" />
		<link rel="stylesheet" href="${request.contextPath}/static/ace/components/font-awesome/css/font-awesome.css" />

		<!-- text fonts -->
		<link rel="stylesheet" href="${request.contextPath}/static/ace/assets/css/ace-fonts.css" />

		<!-- ace styles -->
		<link rel="stylesheet" href="${request.contextPath}/static/ace/components/chosen/chosen.css" />
		<link rel="stylesheet" href="${request.contextPath}/static/ace/assets/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" />

		<!--[if lte IE 9]>
			<link rel="stylesheet" href="${request.contextPath}/static/ace/assets/css/ace-part2.css" class="ace-main-stylesheet" />
		<![endif]-->
		<link rel="stylesheet" href="${request.contextPath}/static/ace/assets/css/ace-skins.css" />
		<link rel="stylesheet" href="${request.contextPath}/static/ace/assets/css/ace-rtl.css" />
	
		<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css">
		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="${request.contextPath}/static/ace/assets/css/ace-ie.css" />
		<![endif]-->
		<!-- ace settings handler -->
		<script src="${request.contextPath}/static/ace/assets/js/ace-extra.js"></script>
		<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

		<!--[if lte IE 8]>
		<script src="${request.contextPath}/static/ace/components/html5shiv/dist/html5shiv.min.js"></script>
		<script src="${request.contextPath}/static/ace/components/respond/dest/respond.min.js"></script>
		<![endif]-->
	</head>

	<body class="no-skin">
		<!-- #section:basics/navbar.layout -->
		<div id="navbar" class="navbar navbar-default ace-save-state">
			<div class="navbar-container ace-save-state" id="navbar-container">
				<!-- #section:basics/sidebar.mobile.toggle -->
				<div class="navbar-header pull-left">
					<a href="javascript:;" class="navbar-brand">
						<small>
							<i class="fa fa-leaf"></i>
							${frameworkEnv.getString("platform_name")!}
						</small>
					</a>
				</div>

				<div class="navbar-buttons navbar-header pull-right" role="navigation">
					<ul class="nav ace-nav">
						<#if showStat!true>
						<#--待办-->
						<li class="red" id="nf-stat" />
						</#if>
						<#if showTodo!false>
						<#--待办-->
						<li class="grey" id="nf-todo" />
						</#if>
						
						<#if showNotice!false>
						<#--通知-->
						<li class="purple" id="nf-notice" />
						</#if>
						
						<#if showMessage!false>
						<#--消息-->
						<li class="green" id="nf-message" />
						</#if>
						<#if showInfo!false>
						<#--个人-->
						<li class="light-blue" id="nf-info" />
						</#if>
					</ul>
				</div>

				<!-- /section:basics/navbar.dropdown -->
			</div><!-- /.navbar-container -->
		</div>

		<!-- /section:basics/navbar.layout -->
		<div class="main-container" id="main-container">
			<!-- #section:basics/sidebar -->
			<div id="sidebar" class="sidebar responsive ace-save-state">
			<#--
				</div>
				<div class="sidebar-shortcuts" id="sidebar-shortcuts">
			-->

				<ul class="nav nav-list" id="nf-navList" >
				</ul>

				<!-- #section:basics/sidebar.layout.minimize -->
				<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
					<i id="sidebar-toggle-icon" class="ace-icon fa fa-angle-double-left ace-save-state" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
				</div>
				<!-- /section:basics/sidebar.layout.minimize -->
			</div>

			<!-- /section:basics/sidebar -->
			<div class="main-content">
				<div class="main-content-inner">
					<!-- #section:basics/content.breadcrumbs -->
					<div class="breadcrumbs" id="breadcrumbs">
						<ul class="breadcrumb">
							<li>
								<i class="ace-icon fa fa-home home-icon"></i>
								<a href="javascript:;" onclick="javascript:gotoHomepage();">首页</a>
							</li>
						</ul><!-- /.breadcrumb -->
						<!-- /section:basics/content.searchbox -->
					</div>

					<!-- /section:basics/content.breadcrumbs -->
					<div class="page-content">
						<div class="page-content-area" data-ajax-content="true">
						</div><!-- /.page-content-area -->
					</div><!-- /.page-content -->
				</div>
			</div><!-- /.main-content -->

			<div class="footer">
				<div class="footer-inner">
					<!-- #section:basics/footer -->
					<div class="footer-content">
						<span class="bigger-120">
							<span class="blue bolder">${frameworkEnv.getString("footer")!}</span>
							${frameworkEnv.getString("subfooter")!}
						</span>
					</div>
					<!-- /section:basics/footer -->
				</div>
			</div>

			<a href="javascript:;" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
				<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
			</a>
		</div><!-- /.main-container -->

		<!-- basic scripts -->

		<!--[if !IE]> -->
		<script src="${request.contextPath}/static/ace/components/jquery/dist/jquery.js"></script>
		<!-- <![endif]-->
		
		<!--[if IE]>
		<script src="${request.contextPath}/static/ace/components/jquery.1x/dist/jquery.js"></script>
		<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${request.contextPath}/static/ace/components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>"+"<"+"/script>");
		</script>

		<script>
		<#if showInfo!false>
		$("#nf-info").load("${request.contextPath}/homepage/cus/info/list/page");
		</#if>
		<#if showMessage!false>
		$("#nf-message").load("${request.contextPath}/homepage/cus/nf-message/page");
		</#if>
		<#if showNotice!false>
		$("#nf-notice").load("${request.contextPath}/homepage/cus/nf-notice/page");
		</#if>
		<#if showTodo!false>
		$("#nf-todo").load("${request.contextPath}/homepage/cus/nf-todo/page");
		</#if>
		<#if showStat!false>
		$("#nf-stat").load("${request.contextPath}/homepage/cus/nf-stat/page");
		</#if>
		$("#nf-navList").load("${request.contextPath}/homepage/cus/nav/list/page");
		function gotoHomepage(){
			location.href = "${request.contextPath}/homepage/index/page#<#if request.contextPath?default('')!=''>/${request.contextPath}</#if>/basedata/studentFlowOut/welcome";
		}
		</script>
		<!--[if IE]>
<script src="${request.contextPath}/static/ace/components/jquery.1x/dist/jquery.js"></script>
<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${request.contextPath}/static/ace/components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>"+"<"+"/script>");
		</script>
		<link rel="stylesheet" href="${request.contextPath}/static/ace/components/_mod/jquery-ui.custom/jquery-ui.custom.css" />
		<script src="${request.contextPath}/static/ace/components/_mod/jquery-ui.custom/jquery-ui.custom.js"></script>
		<script src="${request.contextPath}/static/ace/components/jqueryui-touch-punch/jquery.ui.touch-punch.js"></script>
		<script src="${request.contextPath}/static/ace/components/bootstrap/dist/js/bootstrap.js"></script>
		<!-- ace scripts -->
		<script src="${request.contextPath}/static/ace/assets/js/src/elements.scroller.js"></script>
		<script src="${request.contextPath}/static/ace/assets/js/src/ace.js"></script>
		<script src="${request.contextPath}/static/ace/assets/js/src/ace.basics.js"></script>
		<script src="${request.contextPath}/static/ace/assets/js/src/ace.ajax-content.js"></script>
		<script src="${request.contextPath}/static/ace/assets/js/src/ace.sidebar.js"></script>
		<script src="${request.contextPath}/static/ace/assets/js/src/ace.sidebar-scroll-1.js"></script>
		<script src="${request.contextPath}/static/ace/assets/js/src/ace.submenu-hover.js"></script>
		<script src="${request.contextPath}/static/ace/assets/js/src/ace.widget-on-reload.js"></script>
		<script src="${request.contextPath}/static/ace/components/autosize/dist/autosize.js"></script>
		<#-- 提示框 
		<script src="${request.contextPath}/static/sweetalert/sweetalert-dev.js"></script>
		<link rel="stylesheet" href="${request.contextPath}/static/sweetalert/sweetalert.css" />-->
		<#-- 弹出框 -->
		<script src="${request.contextPath}/static/layer/layer.js"></script>
		<#-- <script src="${request.contextPath}/static/layer/extend/layer.ext.js"></script> -->
		<link rel="stylesheet" href="${request.contextPath}/static/layer/skin/layer.css" />
		<#-- <link rel="stylesheet" href="${request.contextPath}/static/layer/skin/layer.ext.css" /> -->
		<script src="${request.contextPath}/static/js/tool.js"></script>
		<script src="${request.contextPath}/static/js/md5.js"></script>
	</body>
</html>
