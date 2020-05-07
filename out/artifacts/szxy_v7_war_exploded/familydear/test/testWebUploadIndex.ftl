<script type="text/javascript" src="../familydear/test/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../familydear/test/js/img-adapter.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/remote/openapi/js/jquery.Layer.js"></script>
<link rel="stylesheet" type="text/css" href="../familydear/test/css/style1.css">
<link rel="stylesheet" type="text/css" href="../familydear/test/css/webuploader.css">
<input id="testId" value="${id!}">
<div id="images">
</div>
<script>
    $(function () {
        refreshPic();
    })
    function refreshPic(){
        var id = $("#testId").val();
        $("#images").load("${request.contextPath}/test/edit?id=" + id);
    }
</script>