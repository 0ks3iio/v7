<div class="box box-default">
    <div class="box-body">
        <ul id="breadcrumb" class="clearfix" <#if fromDesktop?default(false)>style="display: none"</#if>>
            <li class="active" id="passwd"><span>重设密码</span></li>
            <li id="option"><span>参数设置</span></li>
            <li id="license"><span>系统激活</span></li>
        </ul>
        <form class="form-horizontal" role="form">
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding" for="form-field-8">*&nbsp;初始密码</label>

                <div class="col-sm-5">
                    <input id="oldPassword" type="text" id="form-field-8" value="" placeholder="请输入原有密码" class="form-control">
                </div>
                <div class="col-sm-5 control-tips" id="oldPasswordTip">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding" for="form-field-9">*&nbsp;新密码</label>

                <div class="col-sm-5">
									<span class="input-icon" id="eyeChange">
										<input id="newPassword"  type="password" id="form-field-9" value="" placeholder="请输入新密码" class="form-control">
										<i class="ace-icon fa fa-eye-slash"></i>
									</span>
                    <span class="control-disabled">密码只能以数字或字母开头，密码长度不得少于6位</span>
                </div>
                <div class="col-sm-5 control-tips" id="newPasswordTip">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding" for="form-field-1"></label>
                <div class="col-sm-5">
                    <a href="javascript:;" class="btn btn-blue" id="saveBtn">&emsp;保存&emsp;</a>
                </div>
                <div class="col-sm-5 control-tips">
                </div>
            </div>
        </form>
    </div>
</div>
<script>
    $(function(){
        $('#eyeChange i.fa').click(function(){
            if ($(this).hasClass('fa-eye-slash')) {
                $(this).removeClass('fa-eye-slash').addClass('fa-eye');
                $(this).prev('input').attr('type','text');
            } else{
                $(this).removeClass('fa-eye').addClass('fa-eye-slash');
                $(this).prev('input').attr('type','password');
            }
        });
        $("#breadcrumb #passwd").click(function(){
            $(this).addClass("active");
            $("#breadcrumb #option").removeClass("active");
            $("#breadcrumb #license").removeClass("active");
            $("#ops-container").load("${request.contextPath}/system/ops/resetPassword");
        });
        $("#breadcrumb #option").click(function () {
            $(this).addClass("active");
            $("#breadcrumb #passwd").addClass("active");
            $("#breadcrumb #license").removeClass("active");
            $("#ops-container").load("${request.contextPath}/system/ops/sysOption");
        });
        $("#breadcrumb #license").click(function () {
            $(this).addClass("active");
            $("#breadcrumb #passwd").addClass("active");
            $("#breadcrumb #option").addClass("active");
            $("#ops-container").load("${request.contextPath}/system/ops/init");
        });
    });
      $('#saveBtn').click(
          function() {
            $('.control-tips').empty();
            $.post({
              url : '${request.contextPath}/system/ops/saveNewPassword',
              data : {
                'oldPassword' : $('#oldPassword').val(),
                'newPassword' : $('#newPassword').val()
              },
              success : function(msg) {
                var jsonO = JSON.parse(msg);

                if (jsonO.success) {
                  //跳转
                <#if !fromDesktop?default(true)>
                  layer.confirm('重置密码成功，是否进入下一步？', {
                    btn: ['确定','取消'] //按钮
                  }, function(){
                    window.location.href='${request.contextPath}/system/ops/sysOption';
                  });
                  <#else>
                      layer.confirm('重置密码成功', {
                          btn: ['确定','取消'] //按钮
                      }, function(){
                          layer.closeAll();
                      });
                  </#if>
                } else {
                  $('#'+jsonO.code+'Tip').html(
                      '<span class="has-error"><i class="fa fa-info-circle"></i>' + jsonO.msg + '</span>');
                }
              }
            });
          });
</script>

