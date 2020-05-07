<div class="layer-delSystem">
	<div class="layer-content">
		<p>确认删除，请输入验证码</p>
		<div class="clearfix">
			<input type="text" id="verifyCode1"  onkeydown="displayResult();" class="form-control" placeholder="请输入验证码">
			<a href="javascript:void(0);"  id="refreshVerifyCode" ><img src="${request.contextPath}/gkelective/verifyImage" width="84" height="36" alt=""></a>
		</div>
	</div>

	<div class="layer-footer" style="margin-top:20px;">
		<button class="btn btn-lightblue" id="arrange-commit" onclick="doDeleteById('${deleteId!}');">确定</button>
		<button class="btn btn-lightblue" id="arrange-close" onclick="layer.closeAll();">取消</button>
	</div>
</div>
<script>
	$(document).ready(function(){
		refreshVerifyCode();
	});
	
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
            doDeleteById('${deleteId!}');
        }
    }
	
	function refreshVerifyCode(){
        $img = $("#refreshVerifyCode").find("img");
        var srcUrl = $img.attr("src");
        $img.attr("src",srcUrl+"?date="+new Date());
    }
	$("#refreshVerifyCode").unbind("click").bind("click",refreshVerifyCode);
	
</script>
