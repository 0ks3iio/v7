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
					<div class="page-content">
						<div class="tab-content">
							<div class="tab-pane active" role="tabpanel">
								<div class="box box-primary">
									<h1 class="text-center"><span class="mr150">考区：${regionName!}</span><span class="mr150">考点：${optionName!}</span><span>考场：${placeName!}</span></h1>
									<div class="box-body door-post">
										<p class="p1">第${placeName!}考场</p>
										<p class="p2">${firstNum!}-${lastNum!}</p>
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
