<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta name="format-detection" content="telephone=no,email=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-touch-fullscreen" content="yes" />
<meta name="ML-Config" content="fullscreen=yes,preventMove=no" />
<meta http-equiv="Pragma" content="no-cache">
<link href="${request.contextPath}/static/mui/css/pages.css" rel="stylesheet"/>
<title></title>
</head>

<body>
 <a class="mui-btn-close mui-icon mui-icon-left-nav mui-pull-left" style="display:none;"></a>
<div class="ui-page">
	<!--两种无记录了状态-->
    <p class="page-nodata">${nonemess?default("没有相关内容，敬请期待。")}</p>
</div>
</body>
</html>