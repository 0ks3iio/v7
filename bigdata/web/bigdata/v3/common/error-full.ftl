<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="UTF-8">
    <title>系统提示</title>
    <meta name="description" content=""/> 
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <link rel="shortcut icon"  href="${request.contextPath}/bigdata/v3/static/images/public/wanshu-icon16.png" >
    <link rel="stylesheet" type="text/css"
          href="${request.contextPath}/bigdata/v3/static/css/bootstrap.css"/>
    <link rel="stylesheet"
          href="${request.contextPath}/bigdata/v3/static/font-awsome/css/font-awesome.css"/>
    <link rel="stylesheet" type="text/css"
          href="${request.contextPath}/bigdata/v3/static/css/iconfont.css"/>
    <link rel="stylesheet" type="text/css"
          href="${request.contextPath}/bigdata/v3/static/fonts/iconfont.css"/>
    <link rel="stylesheet" type="text/css"
          href="${request.contextPath}/bigdata/v3/static/css/all.css"/>
    <link rel="stylesheet" type="text/css"
          href="${request.contextPath}/bigdata/v3/static/css/style.css"/>
</head>
<body style="height:100%">
<div class="main-content" >
	<div class="main-content-inner">
		<div class="page-content">
			<div class="no-data-404">
					<img id="errorImageId" src=""/>
				<div class="ml-30">
					<div class="word-404"> 
						${errorMsg?default('哎呀~您访问的页面开小差啦')}
					</div>
				</div>
			</div>
		</div>
	</div>
</div>				
</body>									
<script src="${request.contextPath}/bigdata/v3/static/js/jquery/jquery.min.js" type="text/javascript"
        charset="utf-8"></script>
<script>
	$(document).ready(function(){
		var imageUrl="${request.contextPath}/bigdata/v3/static/images/public/no-data-face.png";
		$("#errorImageId").attr("src",imageUrl);
	});
</script>
</html>