<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="UTF-8">
    <title>公告-${notice.title!}</title>    
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
<body>
<div class="main-container">
	<div class="main-content">
		<div class="main-content-inner">
			<div class="page-content">
	            <div class="index">
	                <!-- 居中内容 -->
	                <div class="width-auto-100">
	                    <div class="detail-news">
	                        <div class="box box-default notice-inner">
	                            ${notice.content!}
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