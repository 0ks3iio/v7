<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>选择角色</title>

		<meta name="description" content="" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap/dist/css/bootstrap.css" />
		<link rel="stylesheet" href="${request.contextPath}/static/components/font-awesome/css/font-awesome.css" />

		<link rel="stylesheet" href="${request.contextPath}/static/css/components.css">
		<link rel="stylesheet" href="${request.contextPath}/static/css/basic-data.css">
		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="${request.contextPath}/static/css/pages-ie.css" />
		<![endif]-->
		<!-- inline styles related to this page -->

		<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

		<!--[if lte IE 8]>
			<script src="${request.contextPath}/static/components/html5shiv/dist/html5shiv.min.js"></script>
			<script src="${request.contextPath}/static/components/respond/dest/respond.min.js"></script>
		<![endif]-->
	</head>

	<body class="super">
		<div class="box box-default-dq">
			<div class="box-body-dq">
				<div class="box-title-dq">请选择角色</div>
				<ul class="role-select">
					<li>
						<a href="${request.contextPath}/homepage/register/registerInfo?ownerType=2">
							<img src="${request.contextPath}/static/images/daqing/role-teacher.png" alt="我是老师" />
							<button class="btn btn-blue">我是老师</button>
						</a>
					</li>
					<li>
						<a href="${request.contextPath}/homepage/register/registerInfo?ownerType=1">
							<img src="${request.contextPath}/static/images/daqing/role-student.png" alt="我是学生" />
							<button class="btn btn-blue">我是学生</button>
						</a>
					</li>
					<li>
						<a href="${request.contextPath}/homepage/register/registerInfo?ownerType=3">
							<img src="${request.contextPath}/static/images/daqing/role-parent.png" alt="我是家长" />
							<button class="btn btn-blue">我是家长</button>
						</a>
					</li>
				</ul>
			</div>
		</div>

		<!--[if !IE]> -->
		<script src="${request.contextPath}/static/components/jquery/dist/jquery.js"></script>

		<!-- <![endif]-->

		<!--[if IE]>
		<script src="${request.contextPath}/static/components/jquery.1x/dist/jquery.js"></script>
		<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='../components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>"+"<"+"/script>");
		</script>
		<script src="${request.contextPath}/static/components/bootstrap/dist/js/bootstrap.js"></script>

		<!-- page specific plugin scripts -->
		<script src="${request.contextPath}/static/components/layer/layer.js"></script>

		<!-- inline scripts related to this page -->
		<script>
			$(function(){
				function winLoad(){
					var window_h=$(window).height();
					var login_h=$('.box-default-dq').outerHeight();
					var login_top=parseInt((window_h-login_h)/2);
					$('.box-default-dq').css('margin-top',login_top);
				};
				winLoad();
				$(window).resize(function(){
					winLoad();		
				});
			})
		</script>
	</body>
</html>
