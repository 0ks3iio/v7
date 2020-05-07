<div class="no-internet vetically-center">
	<div class="login-stu-table center">
		<div class="login-info-main">
			<img src="${request.contextPath}/static/eclasscard/standard/verticalshow/images/card-guide.png" alt="">
			<div class="login-info-tip">查看课表请先刷卡登录</div>
		</div>
	</div>
</div>
<script>
$(document).ready(function(){
	$("#clock-tab-type").val(3);
})
function stuScheduleLogin(cardNumber){
	$.ajax({
    	url:'${request.contextPath}/eccShow/eclasscard/standard/stuLoginUser/page',
    	data:{"cardId":_cardId,"cardNumber":cardNumber,"type":"schedule"},
    	type:'post',
    	success:function(data){
        	var jsonO = JSON.parse(data);
        	if(jsonO.success){
            	var studentSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/studentSpace/index?cardId="+_cardId+"&type=schedule"+"&view="+_view;
       			$("#mainContainerDiv").load(studentSpaceUrl);
       			$("#clock-tab-type").val(0);
       		 }else{
       		 	submit = false;
            	showMsgTip(jsonO.msg);
        	}
    	},
   		error : function(XMLHttpRequest, textStatus, errorThrown) {
        	submit = false;
        	showMsgTip('登录失败，请联系系统管理员');
   		 }
	});
}
</script>
