<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>开发者注册</title>
<link rel="stylesheet" href="${resourceUrl}/remote/openapi/css/public.css">
<link rel="stylesheet" href="${resourceUrl}/remote/openapi/css/style.css">
</head>
<style>
.outdiv{
    width: 300px;
    height: 200px;
    position: absolute;
    top: 20%;
    left: 50%;
    background: #cfcfcf;
}
.checkbox{
    margin:15px;
}
</style>
<body>
<div id="header">数据管理</div>
<div id="container" class="">
    <#list openApiApplys as type>
       <div>${type.type} --- ${type.status}</div> 
    </#list>
</div>
<div class="outdiv">
    <#list openApiApplys as type>
    <input class="checkbox" type="checkbox" ${(type.status==1)?string('checked disabled','')}>${type.type}
    </#list>
    <br>
    <input id="apply" type="button" value="submit"/>
</div>
<script type="text/javascript" src="${resourceUrl}/remote/openapi/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${resourceUrl}/remote/openapi/js/myscript.js"></script>
<script type="text/javascript" src="${resourceUrl}/remote/openapi/js/json2.js"></script>
<script type="text/javascript" src="${resourceUrl}/remote/openapi/js/jquery.Layer.js"></script>
<script type="text/javascript">
$('#apply').click(function(){
  
});
</script>
</body>
</html>
