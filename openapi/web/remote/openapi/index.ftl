<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>OpenApi</title>
<link rel="stylesheet" href="${resourceUrl}/remote/openapi/css/public.css">
<link rel="stylesheet" href="${resourceUrl}/remote/openapi/css/style.css">
</head>

<body>
<div id="header">万朋开放平台-基础数据</div>

<div id="container" class="fn-clear">
	<div id="sidebar">
    	<div class="item item-open">
    		<p class="tt"><a href="#Data">Data API</a></p>
    		<ul>
    			<#list interfaceTypes as interfaceType>
    			<li id="li_${interfaceType!}"><a href="javascript:;" onclick="javascript:goUrl('${interfaceType!}')">${interfaceType?upper_case!}</a></li>
    			</#list>
    		</ul>
    	</div>
    </div>
	<div id="content">
    	<div class="item">
    		<h2>Data API</h2>
            <div class="h2-des">
                <p>输入ticketKey点击更新按钮后，才能看到具体的字段以及接口。</p>
                <p>如果接口返回的数据很多，超过了设定的条数限制（默认是100， 最大为1000），则会进行分批返回数据。在返回的数据中，会多出一个节点<font color='red'>nextDataUri</font>，根据此节点的地址，获取下一批数据，如果此节点不存在，则说明数据已经全部获取完毕。
                <p class="fn-clear mt-5"><input type="text" class="input-txt" id="ticketKey" name="ticketKey" value="${ticketKey!}"><a onclick="javascript:updateTicketKey();" href="javascript:void(0);" class="abtn-blue">更新</a><span><font color='red'>&nbsp;${errorMsg!}</font></span></p>
            </div>
            <div class="item-inner" id="interfaceContent">
            </div>
            
    	</div>
        
        
    </div>
</div>

<div id="footer">浙江万朋教育科技股份有限公司 提供技术支持 </div>
<script type="text/javascript" src="${resourceUrl}/remote/openapi/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${resourceUrl}/remote/openapi/js/myscript.js"></script>
<script type="text/javascript" src="${resourceUrl}/remote/openapi/js/json2.js"></script>
<script type="text/javascript" src="${resourceUrl}/remote/openapi/js/jquery.Layer.js"></script>

<script>
function goUrl(type){
	$("li").removeClass("current");
	$("#li_" + type).addClass("current");
	<#if ticketKey?default("") != "" && errorMsg?default("") == "">
	$("#interfaceContent").load("${request.contextPath}/remote/openapi/showInterface?ticketKey=${ticketKey!}&currentInterfaceType=" + type);
	</#if>
}

function updateTicketKey(){
	if($("#ticketKey").val() == ""){
		alert("请输入有效的TicketKey！");
		return;
	}
	location.href="${request.contextPath}/remote/openapi/updateTicketKey?currentInterfaceType=${currentInterfaceType!}&ticketKey=" + $("#ticketKey").val();
}

goUrl("${currentInterfaceType!}");
</script>
</body>
</html>
