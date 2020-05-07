<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <title>多媒体</title>
    <script src="${request.contextPath}/static/eclasscard/mobileh5/multimediaPublishing/js/mui.min.js"></script>
    <link href="${request.contextPath}/static/eclasscard/mobileh5/multimediaPublishing/css/mui.min.css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/eclasscard/mobileh5/multimediaPublishing/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/eclasscard/mobileh5/multimediaPublishing/css/mui.picker.min.css"/>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/eclasscard/mobileh5/multimediaPublishing/css/feedback.css"/>
    <script type="text/javascript" charset="utf-8">
        var syncUserId = "${syncUserId!}";
        var unitId = "${unitId!}";
        var ownerId = "${ownerId!}";
        mui.init();
    </script>
</head>
<body>
    <header class="mui-bar mui-bar-nav blue-header">
        <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
        <h1 class="mui-title">多媒体</h1>
    </header>
    <div class="mui-content full-content" id="multimediaDiv">
    </div>
</body>
<script src="${request.contextPath}/static/eclasscard/mobileh5/multimediaPublishing/js/jquery-3.3.1.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/static/eclasscard/mobileh5/multimediaPublishing/js/mui.picker.min.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
    $(function(){
        var bodyUrl = "${request.contextPath}/mobile/open/eclasscard/multimediaHead?unitId="+unitId+"&syncUserId="+syncUserId+"&ownerId="+ownerId;
        $("#multimediaDiv").load(bodyUrl);
    })
</script>
</html>