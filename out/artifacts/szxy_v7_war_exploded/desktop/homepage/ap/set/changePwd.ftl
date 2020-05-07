<#assign USER_LAYOUT_TWO2ONE =stack.findValue("@net.zdsoft.desktop.entity.UserSet@LAYOUT_TWO2ONE") />
<#assign USER_LAYOUT_DEFAULT =stack.findValue("@net.zdsoft.desktop.entity.UserSet@LAYOUT_DEFAULT") />
<div class="main-content-inner">
    <div class="page-content">
        <div class="row">
            <div class="col-xs-12">
                <!-- PAGE CONTENT BEGINS -->
                <div class="box box-default">
                    <div class="box-body">
                        <ul class="nav nav-tabs" role="tablist">
                            <li role="presentation" class="active">
                                <a href="#bb" role="tab" data-toggle="tab">修改密码</a>
                            </li>
                        </ul>
                        <!-- Tab panes -->
                        <div class="tab-content" id="userInfo">
                            <div role="tabpanel" class="tab-pane active" id="bb">
                                <form class="form-horizontal margin-10" role="form">
                                 	<div class="form-group">
                                        <label class="col-sm-2 control-label no-padding">*&nbsp;登录账号</label>
                                        <div class="col-sm-6">
                                            <input name="username" msgName="登录账号" nullable="false" id="username"
                                                   type="text" class="form-control" value="">
                                        </div>
                                        <div class="col-sm-4 control-tips"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding">*&nbsp;原有密码</label>
                                        <div class="col-sm-6">
                                            <input msgName="原密码" nullable="false" id="password" name="password"
                                                   type="password" class="form-control old_password">
                                        </div>
                                        <span class="old_err" style="color: red;"></span>
                                        <div class="col-sm-4 control-tips"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding">*&nbsp;新密码</label>
                                        <div class="col-sm-6">
                                            <input msgName="新密码" nullable="false" id="newPassword" name="newPassword"
                                                   type="password" class="form-control new_password">
                                        </div>
                                        <span class="new_err"></span>
                                        <div class="col-sm-4 control-tips"></div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding">*&nbsp;确认密码</label>
                                        <div class="col-sm-6">
                                            <input msgName="确认密码" nullable="false" id="cfNewPassword"
                                                   name="cfNewPassword" type="password"
                                                   class="form-control confirm_password">
                                        </div>
                                        <span class="confirm_err" style="color: red;"></span>
                                        <div class="col-sm-4 control-tips"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding"></label>

                                        <div class="col-sm-6">
                                            <input type="button" class="btn btn-blue save" value="保存" onclick="javascript:doSavePwd();"/>

                                        </div>
                                        <div class="col-sm-4 control-tips">
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <script>
                            // 执行保存操作
                            function doSavePwd() {
                            	var username = $("#username").val();
                            	var password = $("#password").val();
                            	var newPassword = $("#newPassword").val();
                            	var cfNewPassword = $("#cfNewPassword").val();
                            	if(newPassword != cfNewPassword){
                            		alert("两次新密码不一致，请重新输入！");
                            		$("#newPassword").focus();
                            		return false;
                            	}
                            	 $.ajax({
                                    url: "${request.contextPath}/desktop/ex/doPwdSave?username=" + username + "&password=" + password + "&newPassword=" + newPassword,
                                    contentType: "application/json",
                                    dataType: "json",
                                    type: "post",
                                    success: function (data) {
                                        if (data.success) {
                                            alert(data.msg);
                                        } else {
                                            alert(data.msg);
                                        }
                                    }
                                });
                            }
                        </script>