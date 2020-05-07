<p>确认删除，请输入验证码</p>
<div class="row">
	<div class="col-sm-8"><input type="text" id="verifyCode1" onkeydown="displayResult();" class="form-control col-sm-8" placeholder="请输入验证码"></div>
	<a  href="javascript:;"  id="refreshVerifyCode"><img id="verifyImage" src="${request.contextPath}/desktop/verifyImage" width="84" height="36" alt=""></a>
	<p><font color="#FF0000" size="1">&nbsp;&nbsp;&nbsp;&nbsp;删除项目会同步删除相关的结果记录和统计记录。</font></p>
</div>
<div class="layer-footer" style="margin-top:20px;">
		<button class="btn btn-lightblue" id="arrange-commit" onclick="doDeleteById('${projectId!}');">确定</button>
		<button class="btn btn-lightblue" id="arrange-close" onclick="layer.closeAll();">取消</button>
	</div>
<script>
function isBlank(s) {
    var re = /^\s*$/g;
    return re.test(s);
}
function doDeleteById(id){
	var verifyCode = $("#verifyCode1").val();
	 if(isBlank(verifyCode)){
	    layerTipMsg(false,"请输入验证码！","");
	    $("#verifyCode1").focus();
	    return ;
	}
	var ii = layer.load();
	$.ajax({
		url:'${request.contextPath}/evaluate/project/delete',
		data: {'projectId':id,"verifyCode":verifyCode},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
	 			layer.closeAll();
			  	changeAcadyear();
	 		}else{
	 			layerTipMsg(jsonO.success,jsonO.msg,"");
			}
			layer.close(ii);
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}

function displayResult(){	
	var x;
    if(window.event){
    	 // IE8 以及更早版本
    	x=event.keyCode;
    }else if(event.which){
    	// IE9/Firefox/Chrome/Opera/Safari
        x=event.which;
    }
    if(13==x){
        doDeleteById('${projectId!}');
    }
}

$(document).ready(function(){
	refreshVerifyCode();
});
function refreshVerifyCode(){
    $img = $("#refreshVerifyCode").find("img");
    var srcUrl = $img.attr("src");
    $img.attr("src",srcUrl+"?date="+new Date().getTime());
}
$("#refreshVerifyCode").unbind("click").bind("click",refreshVerifyCode);
	
</script>