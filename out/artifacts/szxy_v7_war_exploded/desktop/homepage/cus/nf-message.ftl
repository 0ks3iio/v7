
<a href="#" class="js-dropbox-msg js-dropbox-toggle" onclick="readNotReadMessage(); " title="未读消息">
	<#if unreadNum?default(0) == 0>
	<i class="wpfont icon-bell"></i>
	<#else>
	<i class="wpfont icon-bell shake-bell"></i>
	</#if>
	<span class="badge badge-yellow">${unreadNum!}</span>
</a>
<script>
function readNotReadMessage() {
    openModel('69052','消息','2','${showUrl!}','','','','');
    
    setTimeout("refreshMsgNum()", 10000);
}
function refreshMsgNum(){
    $("#app-message").load("${request.contextPath}/desktop/index/header/message/page");
 }

</script>