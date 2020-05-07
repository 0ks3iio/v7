<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta charset="UTF-8">
	<title>404</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
	<link rel="stylesheet" href="plugins/slick/slick.css">
	<link rel="stylesheet" href="css/style.css">
</head>
<body>
	<header class="header">
		<div class="logo"><img src="images/logo.png" alt=""></div>
		<div class="date">
			<span class="time"></span>
			<div class="right">
				<span class="day"></span>
				<span class="week"></span>
			</div>
		</div>
	</header>
	<div class="main-container">
		<div class="box">
			<div class="box-body">
				<div class="no-data center">
					<div class="page-error">
						<img src="images/error.png" alt="">
						<p class="page-error-tips">网络链接错误，请检查网络设置</p>
						<a class="btn" href="">刷新</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<script src="plugins/jquery/jquery.min.js"></script>
	<script src="js/myscript.js"></script>
	<script>
		$(document).ready(function(){
			var container = $('.box:last-of-type > .box-body');
			container.css({
				height: $(window).height() - container.offset().top - 160
			})
		})
	</script>
</body>
</html>