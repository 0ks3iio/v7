<!--[if !IE]> -->
<script src="${request.contextPath}/static/components/jquery/dist/jquery.js"></script>

<!-- <![endif]-->

<!--[if IE]>
<script src="${request.contextPath}/static/components/jquery.1x/dist/jquery.js"></script>
<![endif]-->
<script src="${request.contextPath}/static/js/login/jquery.cookies.js"></script>
<script src="${request.contextPath}/static/js/login/md5.js"></script>
<script src="${request.contextPath}/static/js/login/sha1.js"></script>
<script src="${request.contextPath}/static/js/login/login.js?v=1.2"></script>
<script src="${request.contextPath}/static/js/login/login_ext.js?v=1.4"></script>
<script src="${request.contextPath}/static/js/login/constants.js"></script>
<script src="${request.contextPath}/static/js/jquery-browser.update.min.js"></script>
<script>
	$(function(){
		var scriptLogin = document.getElementById("scriptLogin");
        if (scriptLogin) {
            scriptLogin.parentNode.removeChild(scriptLogin);
        }

        // 创建登录脚本元素
        scriptLogin = document.createElement("script");
        scriptLogin.id = "scriptLogin";
        scriptLogin.type = "text/javascript";
        scriptLogin.src = '${passportLoginUrl!}';
        document.body.appendChild(scriptLogin);

        isSubmitting = true;
	})
</script>