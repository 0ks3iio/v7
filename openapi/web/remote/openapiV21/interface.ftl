<script type="text/javascript" src="${resourceUrl}/remote/openapi/js/myscript.js"></script>
	<h3 id="UPL">${currentInterfaceType?upper_case!}</h3> 
	<#assign defaultMcodeId = "" />
	<#list openApiInterfaces as openApi>
    <div class="app-wrap">
    	<p class="dt">
        	<span class="method">${openApi.methodType?upper_case!}</span>
            <span class="path">${openApi.newUri!}</span>
            <span class="description">${openApi.description!}</span>
        </p>
        <div class="dd">
        	<h4>数据属性（${openApi.resultType?upper_case!}）</h4>
            <table class="table-list public-table">
                <tr>
                	<#if adminLogin?default("0") == "1">
                    <th width="60">操作</th>
                    </#if>
                    <th width="120">属性名</th>
                    <th width="110">参数名</th>
                    <th width="70">数据类型</th>
                    <th width="70">是否可为空</th>
                    <th>说明</th>
                    <th width="120">数据字典</th>
                    
                </tr>
                <#assign entities = openApiEntities[openApi.resultType!]?if_exists>
                <#list entities as entity>
                <tr class="tr-${entity.id!}">
                	<#if adminLogin?default("0") == "1">
                    <td>
                    <a href="javascript:void(0);" onclick="javascript:hideEntity('${entity.entityName!}', '${entity.type!}', '${entity.id!}');">隐藏</a>
                    <a href="javascript:void(0);" onclick="javascript:addParam('${openApi.newUri!}', '${entity.entityName!}', '${entity.type!}', '${entity.id!}');">参数</a>
                    </td>
                    </#if>
                    <td>${entity.entityName!}</td>
                    <td>${entity.displayName!}</td>
                    <td>${entity.entityType!}</td>
                    <td><#if entity.mandatory?default(0) == 0>否<#else>是</#if></td>
                    <td>${entity.entityComment!}</td>
                    <td id="td-${entity.id!}">
                    <#if entity.mcodeId?default("") == "" || entity.mcodeId?default("")[0..2] != "DM-">
						<#if adminLogin?default("0") == "1">
					<input type="text" id="it-${entity.id!}" style="width:80px;">&nbsp;<a href="javascript:void(0);" onclick="javascript:changeMcode('${entity.id!}');">确定</a>
						<#else>
					&nbsp;
						</#if>
                    <#else>
                    <a href="javascript:void(0);" class="data-show" onclick="javascript:openModeId('${entity.mcodeId!}' );">查看字典</a>
                    <#if adminLogin?default("0") == "1">
					<input type="text" id="it-${entity.id!}" style="width:80px;">&nbsp;<a href="javascript:void(0);" onclick="javascript:changeMcode('${entity.id!}');">确定</a>
					</#if>
                    </#if>
                    </td>
					
                </tr>
                </#list>
            </table>
            <h4>调用参数</h4>
            <table class="table-list public-table">
                <tr>
                	<#if adminLogin?default("0") == "1">
                    <th width="60">操作</th>
                    </#if>
                    <th width="120">参数名</th>
                    <th width="110">参数值</th>
                    <th>说明</th>
                    <th width="80">是否必填</th>
                </tr>
                
                <#assign params = openApiParameters[openApi.uri!]?if_exists>
                <#list params as param>
                <tr id="tr-${param.id!}">
                	<#if adminLogin?default("0") == "1">
                    <td>
                    <a href="javascript:void(0);" onclick="javascript:deleteParam('${param.newUri!}', '${param.paramName!}', '${param.id!}');">取消</a>
                    </td>
                    </#if>
                    <td>${param.paramName}</td>
                    <#if param.mcodeId?default("") != "" && mcodeMap[param.mcodeId!]?exists>
                    	<td><select onchange="javascript:inputChanged('${openApi.id!}', '${openApi.newUri!}');" class="input-sel" style="width:120px;" id="${param.paramName}" name="${param.paramName}" txttype="${openApi.id!}">
                    		<option value="">--- 请选择 ---</option>
                    		<#assign mcodes = mcodeMap[param.mcodeId!]?if_exists>
                    		<#list mcodes as m>
                    		<option value="${m.thisId}">${m.mcodeContent!}</option>
                    		</#list>
                    		</select></td>
                    <#else>
                    <td><input onblur="javascript:inputChanged('${openApi.id!}', '${openApi.newUri!}');" mandatory="${param.mandatory?default(0)}" class="input-txt params" style="width:112px;" txttype="${openApi.id!}" id="${param.paramName}" name="${param.paramName}"></input></td>
                    </#if>
                    <td>${param.description!}</td>
                    <td><#if param.mandatory?default(0) == 0>否<#else>是</#if></td>
                </tr>
                </#list>
            </table>
            <h4>调用样例</h4>
            <div class="ui-tab">
                <ul class="ui-tab-list fn-clear">
                    <li class="current">Java</li>
                    <li>PHP</li>
                    <li>Curl</li> 
                </ul>
                <div class="ui-tab-wrap">
                    <div class="ui-tab-module">
                        <code class="rainbow">                                    
    Request.Get("${request.contextPath!"http://api.wanpeng.com"}<span class="${openApi.id!}-uri-span">${openApi.newUri!}</span><span class="openApId-span">?openApId=${apId!}</span><span class="${openApi.id!}-param-span"></span>").execute().returnContent().asString();</code>
                        <p class="mt-10"><a href="#" class="abtn-blue get-code" onclick="javascript:queryJson('${openApi.id!}', '${openApi.newUri!}');"><span class="query-button-span">获取</span></a></p>
                    </div>
                    <div class="ui-tab-module" style="display:none;">
                     <code class="rainbow">
	$ch = curl_init('${request.contextPath!"http://api.wanpeng.com"}<span class="${openApi.id!}-uri-span">${openApi.newUri!}</span><span class="openApId-span">?openApId=${apId!}</span><span class="${openApi.id!}-param-span"></span>');
	<br>curl_setopt($ch, CURLOPT_RETURNTRANSFER,1); 
	<br>$response = json_decode(curl_exec($ch));
					</code>
						<p class="mt-10"><a href="#" class="abtn-blue get-code" onclick="javascript:queryJson('${openApi.id!}', '${openApi.newUri!}');"><span class="query-button-span">获取</span></a></p>
					</div>
                    <div class="ui-tab-module" style="display:none;">
                    <code class="rainbow">
		curl -X GET ${request.contextPath!"http://api.wanpeng.com"}<span class="${openApi.id!}-uri-span">${openApi.newUri!}</span><span class="openApId-span">?openApId=${apId!}</span><span class="${openApi.id!}-param-span"></span>
					</code>
						<p class="mt-10"><a href="#" class="abtn-blue get-code" onclick="javascript:queryJson('${openApi.id!}', '${openApi.newUri!}');"><span class="query-button-span">获取</span></a></p>
					</div>
                </div>
                <div class="get-code-wrap">
                    <h4>返回结果数据</h4>
				<pre>
					<span class="${openApi.id!}-jsonString"></span>
				</pre>
            	</div>
            </div>
        </div>
    </div>
    </#list>
    </div>
         

<div class="ui-layer" style="display:none;width:600px;">
	<p class="tt"><a href="#" class="close">关闭</a><span class="title">数据字典</span></p>
	<div class="wrap pa-10" style="max-height:200px;">
        <table class="table-list public-table" id="mcodeIdTable">
            <tr>
                <th width="10%">代码</th>
                <th width="35%">内容</th>
                <th width="10%">代码</th>
                <th width="35%">内容</th>
            </tr>
        </table>
    </div>
</div>


<script>
$(function(){
	
})

winW=parseInt($(window).width());
winH=parseInt($(window).height());
			
function openModeId(mcodeId){
	var newRow = "<tr><td></td><td></td><td></td><td></td></tr>";
	var aj = $.ajax({  
	    url:'${request.contextPath}/remote/openapi/show/mcode/' + mcodeId,
	    type:'get',  
	    cache:false,  
	    dataType:'json',
	    success:function(data) {  
	    	var count = 0;
	    	var newRow = "";
	    	$(data).each(function(){
	    		if(count % 2 == 0){
	    			newRow += "<tr>";
	    		}
	    		count ++;
	    		newRow += "<td>" + $(this).attr('thisId') + "</td><td>" + $(this).attr('mcodeContent') + "</td>";
	    		if(count % 2 == 0){
	    			newRow += "</tr>";
	    		}
	    	});
	    	if(count % 2 != 0){
	    		newRow += "<td>&nbsp;</td><td>&nbsp;</td></tr>";
	    	}
	    	$("#mcodeIdTable tr:gt(0)").remove();
	    	$("#mcodeIdTable tr:last").after(newRow);
	    	
	    	var layer = $(".ui-layer");
	    	var layerW=parseInt(layer.width());
			var layerH=parseInt(layer.height());
			var layerL=(winW-layerW)/2;
			var layerT=(winH-layerH)/2;
			var reTop=layerT;
	    	var docH=parseInt($(document).height());
			var winTop=parseInt($(window).scrollTop());
			
			layerT=reTop+winTop;
			(docH>winH) ? maskH=docH : maskH=winH;
			$(layer).show().css({top:layerT,left:layerL});
			if($(document).find('.ui-layer-mask').length==0){
				$('body').append('<div class="ui-layer-mask" style="height:'+maskH+'px"></div>');
			};
			$(window).scroll(function(){
				var sTop=parseInt($(window).scrollTop());
				layerT=reTop+sTop;
				layer.css({top:layerT});
			});
			
			layer.find(".close").click(function(e){
				e.preventDefault();
				layer.hide();
				$('.ui-layer-mask').remove();
			});
	     },  
	     error : function() {  
	          alert("异常！");     
	    }  
	});
	return false;	
}


$('.get-code').click(function(e){
		e.preventDefault();
		$('.get-code-wrap').show();
	});
	  
function queryJson(id, uri){

	if(apId == ""){
		alert("请更新apId！");
		$("#apId").focus();
		return;
	}
	
	var objsLimit = $("input[txttype='" + id+ "'][id='limit']");
	if(objsLimit.length > 0 && objsLimit.get(0).value > 1000){
		alert("limit不能超过1000");
		return; 
	}
	
	var index = uri.indexOf("{id}");
	var objs = $("input[txttype='" + id+ "'][id='id']");
	var unitId = "";
	if(objs.length > 0){
		unitId = objs.get(0).value;
	}
	if(index >= 0){
		if(unitId == "" || unitId.length != 32){
			alert("参数id不能为空，且必须为32位GUID！");
			return;
		}
		uri = uri.replace("{id}", unitId);
	}
	var tgs = ["input[txttype='" + id+ "']", "SELECT[txttype='" + id+ "']"];
	var len = tgs.length;
	var saveDataAry=[]; 
	var objs = {};
	for(var j = 0; j < len; j ++){
		var params = $(tgs[j]);
		for(var i = 0; i < params.size(); i ++){
			var v = params.get(i).value;
			var n = params.get(i).name;
			var mandatory = $(params.get(i)).attr("mandatory");
			if(mandatory == "1" && v == ""){
				alert(n + "为必填参数，不能为空！")
				return;
			}
			if(index >= 0){
				if(n == "id"){
					continue;
				}
			}
			if(v == ""){
				continue;
			}
			objs[n] = v;
		}
	}
	if(judgeIsNotEmpty(objs)){
		var objsJsonStr = JSON.stringify(objs);
		$.ajax({  
		    url:'${request.contextPath}/remote/openapi/show/handleParameter?openApId='+apId+'&parameters='+objsJsonStr,
		    type:'post',  
		    cache:false,  
		    dataType:'json',
		    beforeSend:function(XMLHttpRequest){
		    	$(".query-button-span").text("获取中……");
		    }, 
		    success:function(data) {  
		    	if(data.success){
		    		$("." + id + "-param-span").text('&openParam='+data.msg);
		    		$.ajax({  
					    url:'${request.contextPath}/remote'+uri+'?openApId='+apId+'&openParam='+data.msg,
					    type:'post',  
					    cache:false,  
					    dataType:'json',
					    success:function(datajson) {  
					    	$("." + id + "-jsonString").text(formatJson(datajson));
	    					$(".query-button-span").text("获取");
					    },  
					    error : function(XMLHttpRequest, textStatus, errorThrown) {  
					    	$("." + id + "-jsonString").text(formatJson(datajson));
					    	$(".query-button-span").text("获取");
					    }  
					});
		 		}
		 		else{
		 			alert(data.msg);
    			}
		    },  
		    error : function(XMLHttpRequest, textStatus, errorThrown) {  
		    	$("." + id + "-jsonString").text(formatJson(data));
		    	$(".query-button-span").text("获取");
		    }  
		});
	}else{
		$.ajax({  
		    url:'${request.contextPath}/remote'+uri+'?openApId='+apId,
		    type:'post',  
		    cache:false,  
		    dataType:'json',
		    beforeSend:function(XMLHttpRequest){
		    	$(".query-button-span").text("获取中……");
		    }, 
		    success:function(datajson) {  
		    	$("." + id + "-jsonString").text(formatJson(datajson));
				$(".query-button-span").text("获取");
		    },  
		    error : function(XMLHttpRequest, textStatus, errorThrown) {  
		    	$("." + id + "-jsonString").text(formatJson(datajson));
		    	$(".query-button-span").text("获取");
		    }  
		});
	}
	
}	
function judgeIsNotEmpty(obj){
	for(var i in obj){//不为空
		return true;
	}
	return false;
}

function hideEntity(entityName, type, id){
	var objs = new Object();
	objs.entityName = entityName;
	objs.type = type;
	objs.id = id;
	$.ajax({  
	    url:'${request.contextPath}/remote/openapi/show/entity/hide',
	    data: $.param(objs),  
	    type:'post',  
	    cache:false,  
	    dataType:'json',
	    beforeSend:function(XMLHttpRequest){
	    },  
	    success:function(data) {  
	    	$(".tr-" + id).hide();
	     },  
	     error : function(XMLHttpRequest, textStatus, errorThrown) {  
	     	alert("失败");
	    }  
	});
}

function deleteParam(uri, paramName, id){
	var objs = new Object();
	objs.id = id;
	$.ajax({  
	    url:'${request.contextPath}/remote/openapi/show/parameter/' + id,
	    data: $.param(objs),  
	    type:'delete',  
	    cache:false,  
	    dataType:'json',
	    beforeSend:function(XMLHttpRequest){
	    },  
	    success:function(data) {  
	    	$("#tr-" + id).hide();
	     },  
	     error : function(XMLHttpRequest, textStatus, errorThrown) {  
	     	alert("失败");
	    }  
	});
}

function addParam(uri, entityName, type, id){
	var objs = new Object();
	objs.entityName = entityName;
	objs.type = type;
	objs.id = id;
	objs.uri = uri;
	$.ajax({  
	    url:'${request.contextPath}/remote/openapi/show/parameter',
	    data: $.param(objs),  
	    type:'post',  
	    cache:false,  
	    dataType:'json',
	    beforeSend:function(XMLHttpRequest){
	    },  
	    success:function(data) {  
	    	
	     },  
	     error : function(XMLHttpRequest, textStatus, errorThrown) {  
	     	alert("失败");
	    }  
	});
}

function changeMcode(id){
	var mcodeId = $("#it-" + id).val();
	var objs = new Object();
	objs.id = id;
	objs.mcodeId = mcodeId;
	$.ajax({  
	    url:'${request.contextPath}/remote/openapi/show/entity/mcodeId',
	    data: $.param(objs),  
	    type:'post',  
	    cache:false,  
	    dataType:'json',
	    beforeSend:function(XMLHttpRequest){
	    },  
	    success:function(data) {  
	    	$("#td-" + id).html("<a href='javascript:void(0);' class='data-show' onclick='javascript:openModeId(\"" + mcodeId + "\" );'>查看字典</a>");
	     },  
	     error : function(XMLHttpRequest, textStatus, errorThrown) {  
	     	alert("失败");
	    }  
	});
}

var apId = "${apId!}";

function inputChanged(id, uri){
	var index = uri.indexOf("{id}");
	var objs = $("input[txttype='" + id+ "'][id='id']");
	var unitId = "";
	if(objs.length > 0){
		unitId = objs.get(0).value;
	}
	if(index >= 0){
		uri = uri.replace("{id}", unitId);
	}
	$("." + id + "-uri-span").text(uri);
	
	var tgs = ["input[txttype='" + id+ "']", "SELECT[txttype='" + id+ "']"];
	var len = tgs.length;
	var param = '';
	for(var j = 0; j < len; j ++){
		var params = $(tgs[j]);
		for(var i = 0; i < params.size(); i ++){
			var v = params.get(i).value;
			var n = params.get(i).name;
			if(index >= 0){
				if(n == "id"){
					continue;
				}
			}
			if(v == ""){
				continue;
			}
			param = param + "&";
			param = param + params.get(i).name + "=" + v; 
		}
	}
	if(param!=''){
		$("." + id + "-param-span").text('&openParam=参数加密');
	}else{
		$("." + id + "-param-span").text('');
	}
	
}

 
function formatJson(json, options){
	var reg = null,
	formatted = '',
	pad = 0,
	PADDING = '    '; // one can also use '\t' or a different number of spaces
 
	options = options || {};
	options.newlineAfterColonIfBeforeBraceOrBracket = (options.newlineAfterColonIfBeforeBraceOrBracket === true) ? true : false;
	options.spaceAfterColon = (options.spaceAfterColon === false) ? false : true;
 
	if (typeof json !== 'string') {
		json = JSON.stringify(json);
	} else {
		json = JSON.parse(json);
		json = JSON.stringify(json);
	}
 
	reg = /([\{\}])/g;
	json = json.replace(reg, '\r\n$1\r\n');
 
	reg = /([\[\]])/g;
	json = json.replace(reg, '\r\n$1\r\n');
 
	reg = /(\,)/g;
	json = json.replace(reg, '$1\r\n');
 
	reg = /(\r\n\r\n)/g;
	json = json.replace(reg, '\r\n');
 
	reg = /\r\n\,/g;
	json = json.replace(reg, ',');
 
	if (!options.newlineAfterColonIfBeforeBraceOrBracket) {			
		reg = /\:\r\n\{/g;
		json = json.replace(reg, ':{');
		reg = /\:\r\n\[/g;
		json = json.replace(reg, ':[');
	}
	if (options.spaceAfterColon) {			
		reg = /\:/g;
		json = json.replace(reg, ':');
	}
 
	$.each(json.split('\r\n'), function(index, node) {
		var i = 0,
			indent = 0,
			padding = '';
 
		if (node.match(/\{$/) || node.match(/\[$/)) {
			indent = 1;
		} else if (node.match(/\}/) || node.match(/\]/)) {
			if (pad !== 0) {
				pad -= 1;
			}
		} else {
			indent = 0;
		}
 
		for (i = 0; i < pad; i++) {
			padding += PADDING;
		}
 
		formatted += padding + node + '\r\n';
		pad += indent;
	});
	return formatted;
}

</script>
