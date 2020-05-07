<!--[if !IE]> -->
<script src="${request.contextPath}/static/components/jquery/dist/jquery.js"></script>

<!-- <![endif]-->

<!--[if IE]>
<script src="${request.contextPath}/static/components/jquery.1x/dist/jquery.js"></script>
<![endif]-->
<script type="text/javascript">
	if('ontouchstart' in document.documentElement) document.write("<script src='${request.contextPath}/static/components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>"+"<"+"/script>");
</script>
<script src="http://g.alicdn.com/dingding/dingtalk-pc-api/2.7.0/index.js" ></script>
<script>

	DingTalkPC.config({
		agentId: '${agentId}',
		corpId: '${coprId}',
		timeStamp: '${timeStamp}',
		nonceStr: '${nonceStr}',
		signature: '${signature}',
		jsApiList: [
			'runtime.info', 'biz.contact.choose',
			'device.notification.confirm', 'device.notification.alert',
			'device.notification.prompt', 'biz.ding.post',
			'biz.util.openLink'
		]
	});
	DingTalkPC.ready(function () {
		DingTalkPC.runtime.permission.requestAuthCode({
			corpId:"${coprId}",
			onSuccess: function (result) {
				$.ajax({
					url : '${request.contextPath}/fpf/author/ding-talk',
					data: {'code':result.code},
					type: 'POST',
					dataType: 'json',
					success: function (info) {
						if (!info.success) {
						    //错误信息提示
							alert(info.errmsg);
						}
						else {
						    //在浏览器中打开
							DingTalkPC.biz.util.openLink({
								//次URL可能和mac远
								url:info.serverPrefix + '/fpf/execute/script?userId=' + info.redirectUrl
							});
						}
					}
				});

			},
			onFail: function (err) {
				alert("Get Ding code error");
			}
		})
	})

	DingTalkPC.error(function (err) {
		alert("Ding Config error: " + JSON.stringify(err));
	})
</script>