<div class="main-content-inner">
    <div class="page-content userInfoClass">
        <div class="row">
            <div class="col-xs-12">
                <!-- PAGE CONTENT BEGINS -->
                <div class="box box-default">
                    <div class="box-body">
                        <ul class="nav nav-tabs" role="tablist">
                            <li role="presentation" class="active">
                                <a href="#aa" role="tab" data-toggle="tab">
                                    开发者信息
                                </a>
                            </li>
                            <li role="presentation">
                                <a href="#bb" role="tab" id="modifyPwdTab" data-toggle="tab">
                                    修改密码
                                </a>
                            </li>
                        </ul>
                        <div class="tab-content">
                            <div role="tabpanel" class="tab-pane active" id="aa">
                                <form name="developer" id="developer" class="form-horizontal margin-10" role="form">
                                    <div class="form-group">
                                        <label class="col-sm-2 control-title no-padding" for="form-field-1">基本信息</label>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding" for="form-field-1">*&nbsp;用户名</label>

                                        <div class="col-sm-6">
                                            <input type="text" id="form-field-1" value="${user.username!}" placeholder="用户名" disabled="disabled" class="form-control">
                                            <span class="control-disabled">用户名不可修改</span>
                                        </div>
                                        <div class="col-sm-4 control-tips">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding" for="form-field-2">*&nbsp;开发者</label>

                                        <div class="col-sm-6">
                                            <input name="unitName" type="text" value="${user.unitName!}" placeholder="2-30个中文字符，填写企业名称或个人姓名" minLength="4" maxLength="60"  class="form-control yz">
                                        </div>
                                        <div class="col-sm-4 control-tips">
                                            <!-- <span class="has-warning"><i class="fa fa-info-circle"></i>&nbsp;警告</span> -->
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-title no-padding" for="form-field-1">联系方式</label>

                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding" for="form-field-3">*&nbsp;联系人姓名</label>

                                        <div class="col-sm-6">
                                            <input name="realName" type="text" value="${user.realName!}" placeholder="2-20个中文字符" minLength="4" maxLength="40" class="form-control yz">
                                        </div>
                                        <div class="col-sm-4 control-tips">
                                            <!-- <span class="has-success"><i class="fa fa-check-circle"></i>&nbsp;正确</span> -->
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding" for="form-field-4">*&nbsp;联系人地址</label>

                                        <div class="col-sm-6">
                                            <input name="address" type="text" value="${user.address!}"  placeholder="1-40个中文字符，请输入详细地址" minLength="2" maxLength="80" class="form-control yz">
                                        </div>
                                        <div class="col-sm-4 control-tips">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding" for="form-field-5">*&nbsp;手机号码</label>

                                        <div class="col-sm-6">
                                            <input name="mobilePhone" type="text" value="${user.mobilePhone!}" placeholder="填写11位数字号码" class="form-control yz">
                                        </div>
                                        <div id="phoneError" class="col-sm-4 control-tips">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding" for="form-field-6">*&nbsp;邮箱</label>

                                        <div class="col-sm-6">
                                            <input maxlength="40" id="email" name="email" type="text" value="${user.email!}" class="typeahead scrollable form-control yz" placeholder="请填写有效邮箱以便接收平台下发消息。" autocomplete="off" data-provide="typeahead">
                                        </div>
                                        <div id="emailErrorDiv"  class="col-sm-4 control-tips">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding" for="form-field-7">官网地址</label>
                                        <div class="col-sm-6">
                                            <input id="homepage" name="homepage" type="text" value="${user.homepage!}" class="form-control yz">
                                        </div>
                                        <div class="col-sm-4 control-tips">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding" for="form-field-7">白名单</label>
                                        <div class="col-sm-6">
                                            <input id="ips" name="ips" type="text" value="${user.ips!}" placeholder="多个ip之间用英文逗号分隔" class="form-control yz">
                                        </div>
                                        <div class="col-sm-4 control-tips">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding" for="form-field-1"></label>

                                        <div class="col-sm-6">
                                            <input type="button" class="btn btn-blue" value="保存" id="modifyUser">
                                        </div>
                                        <div class="col-sm-4 control-tips">
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div role="tabpanel" class="tab-pane" id="bb">
                                <form class="form-horizontal margin-10 mpwdDiv" role="form">
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding" for="form-field-8">*&nbsp;原有密码</label>

                                        <div class="col-sm-6" id="oldpwdDivPar">
                                            <input type="text" id="oldPassword" value="" placeholder="请输入原有密码" class="form-control" onfocus="this.type='password'">
                                        </div>
                                        <div class="col-sm-4 control-tips">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding" for="form-field-9">*&nbsp;新密码</label>

                                        <div class="col-sm-6">
                                            <span class="input-icon" id="eyeChange">
                                                <input type="text" id="newPwd" value="" placeholder="请输入新密码" class="form-control" onfocus="pwdFocus()">
                                                <i class="ace-icon fa fa-eye-slash" id="chageI"></i>
                                            </span>
                                        </div>
                                        <div class="col-sm-4 control-tips">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding" for="form-field-1"></label>

                                        <div class="col-sm-6">
                                            <input type="button" class="btn btn-blue" value="保存" id="updatePassword">
                                        </div>
                                        <div class="col-sm-4 control-tips">
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        
                    </div>
                </div>
                <!-- PAGE CONTENT ENDS -->
            </div><!-- /.col -->
        </div><!-- /.row -->
    </div><!-- /.page-content -->
</div>
<script src="${resourceUrl}/openapi/js/jquery.mailAutoComplete.js"></script> <!-- 邮箱自动填充 -->
            <!-- /.main-content -->
            <script>
            $(function(){
            //邮箱自动填充
              $('#email').mailAutoComplete({
                  emails:['qq.com','163.com','126.com','sina.com','sohu.com','yahoo.cn','gmail.com','hotmail.com','live.cn']
              });
              $('.ui-mail').on('click','#mailBox ul li',function(){
                $('#email').blur();
              });
              if(browser.ie){
                 $('#newPwd').remove();
                 $('#oldPassword').remove();
                 $('#oldpwdDivPar').append('<input type="password" id="oldPassword" value="" placeholder="请输入原有密码" class="form-control"');
                 $('#eyeChange').append(' <input type="password" id="newPwd" value="" placeholder="请输入新密码" class="form-control">')
                 $('#chageI').remove();
              }
               $('.userInfoClass').off();
               $('.userInfoClass').on('click','#eyeChange i.fa',function(){
                    if ($(this).hasClass('fa-eye-slash')) {
                        $(this).removeClass('fa-eye-slash').addClass('fa-eye');
                        $(this).prev('input').attr('type','text');
                    } else{
                        $(this).removeClass('fa-eye').addClass('fa-eye-slash');
                        $(this).prev('input').attr('type','password');
                    }
                });
                $('.userInfoClass').on('click','#modifyUser',function(){
                  $('.yz').trigger('blur');
                  if($('.has-warning').length>0 || $('.has-error').length>0){
                    //showMsgError('请按要求填写','警告');
                    return false;
                  }
                    $.post('${request.contextPath}/developer/modifyUser',$('#developer').serialize(),function(data){
                      var jsonO=JSON.parse(data);
                      if(jsonO.success){
                        showMsgSuccess(jsonO.msg,'',loadDiv('.main-content','/developer/userInfo'));
                      }else{
                        //showMsgError(jsonO.msg,'修改失败');
                          promot($('#phoneError'),3,jsonO.msg);
                        return false;
                      }
                    });
                });
                //验证密码
                $('.userInfoClass').on('blur','#oldPassword',function(){
                  if($(this).val()==''){
                    return;
                  }
                  var ts=$(this).parent().next();
                  $.ajax({
                    url:'${request.contextPath}/developer/regist/valid/oldParamVail/'+$(this).val(),
                    type:'GET',
                    dataType:'json',
                    success:function(msg){
                      if(msg.success){
                        promot(ts,1,'');
                      }else{
                        promot(ts,3,'密码错误');
                      }
                    }
                  });
                });
                
                $('.userInfoClass').on('blur','#newPwd',function(){
                  var $this=$(this);
                  var val=$this.val();
                  var ts=$this.parent().parent().next();
                  if(val==''){
                    promot(ts,3,$this.attr('placeholder'));
                    return;
                  }
                  if(isStr(val) && pswLen(getLength(val))){
                    promot(ts,1,'');
                  }else{
                    promot(ts,3,'新密码的长度应该控制在6-16位');
                  }
                });
                $('.userInfoClass').on('click','#modifyPwdTab',function(){
                  $('.mpwdDiv').find('input:not(#updatePassword)').val("");
                  $('.mpwdDiv').find('.control-tips').empty();
                });
                //修改密码
                $('.userInfoClass').on('click','#updatePassword',function(){
                  if($('#oldPassword').val()==''){
                    promot($('#oldPassword').parent().next(),3,'请填写密码');
                  }
                  $('#oldPassword').blur();
                  $('#newPwd').blur();
                  if($(this).parents('.mpwdDiv').find('.has-error').length==0){
                    $.post('${request.contextPath}/developer/modifyPwd',{'oldParam':$('#oldPassword').val(),'newParam':$('#newPwd').val()},function(result){
                      var data = JSON.parse(result);
                      if(data.success){
                        $('#modifyPwdTab').click();
                        showMsgSuccess('修改成功','');
                      }else{
                        showMsgError('修改失败','修改失败');
                      }
                    });
                  }
                });
                $('.userInfoClass').on('blur','.yz',function(){
                  var $this=$(this);
                  var name=$this.attr("name");
                  var val=$this.val();
                  var isVal=(val && val !="");
                  var ts=$this.parent().next();
                  if(isVal==false && "homepage"!=name && "ips"!=name ){
                    if("email"==name){
                      ts=$('#emailErrorDiv');
                    }
                    promot(ts,3,$this.attr('placeholder'));
                    return false;
                  }
                  if(("homepage"==name || "ips"==name) && val==""){
                    ts.empty();
                  }
                  if (name && name != "") {
                    if(name=="username"){
                      var t=isStr(val);
                      var l=isLen($this,val);
                      if(isStr(val) && isLen($this,val)){
                        $.ajax({
                          url:'${request.contextPath}/developer/regist/valid/username/'+val,
                          type:'GET',
                          dataType:'json',
                          success:function(msg){
                            if(msg.success){
                              promot(ts,1,'');
                            }else{
                              promot(ts,2,'用户已存在');
                            }
                          }
                        });
                        return false;
                      }else{
                        promot(ts,3,$this.attr('placeholder'));
                        return false;
                      }         
                    }else if(name=='unitName'){
                      if(isLen($this,val)){
                        promot(ts,1,'');
                      }else{
                        promot(ts,3,$this.attr('placeholder'));
                      }
                    }else if(name=='password'){
                      if(isStr(val) && isLen($this,val)){
                        promot(ts,1,'');
                      }else{
                        promot(ts,3,$this.attr('placeholder'));
                      }
                    }else if(name=='realName'){
                      if(isLen($this,val)){
                        promot(ts,1,'');
                      }else{
                        promot(ts,3,$this.attr('placeholder'));
                      }
                    }else if(name=='address'){
                      if(isLen($this,val)){
                        promot(ts,1,'');
                      }else{
                        promot(ts,3,$this.attr('placeholder'));
                      }
                    }else if(name=='mobilePhone'){
                      if (!/^\d+$/.test(val) || val.length!=11) {
                        promot(ts,3,'手机号格式错误');
                      }else{
                        promot(ts,1,'');
                      }
                    }else if(name=='email'){
                      ts=$('#emailErrorDiv');
                      if (!/^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/.test(val)) {
                        promot(ts,3,$this.attr('placeholder'));
                      }else{
                        promot(ts,1,'');
                      }
                    }else if(name=='homepage' && isVal!=false){
                      if (getLength(val) > 180) {
                        promot(ts,3,'不能超过180个字符');
                      }else{
                        promot(ts,1,'');
                      }
                    }else if(name=='ips' && isVal!=false){
                      if (getLength(val) > 900) {
                        promot(ts,3,'不能超过900个字符');
                      }else{
                        promot(ts,1,'');
                      }
                    }
                  }
                });
            });
            
            function promot($this,level,msg){
              if(level==1){
                $this.html('<span class="has-success"><i class="fa fa-check-circle"></i>&nbsp;</span>');
              }else if(level==2){
                $this.html('<span class="has-warning"><i class="fa fa-info-circle"></i>&nbsp;'+msg+'</span>');
              }else if(level==3){
                $this.html('<span class="has-error"><i class="fa fa-times-circle"></i>&nbsp;'+msg+'</span>');
              }
            };
            
            function isStr(value){
              var reg=new RegExp("[\\u4E00-\\u9FFF]+","g");
              return reg.test(value)?false:true;
            };
            
            function isLen($this,value){
              var maxLength = $this.attr("maxLength");
              if (maxLength && maxLength > 0 && getLength(value) > maxLength) {
                  return false;
              }
              var minLength = $this.attr("minLength");
              if (minLength && minLength > 0 && getLength(value) < minLength) {
                  return false;
              }
              return true;
            };
            
            function pswLen(len){
              if(len<6 || len>16){
                return false
              }
              return true;
            }
            
            function getLength(str) {
              if (str == null) return 0;
              if (typeof str != "string") {
                  str += "";
              }
              return str.replace(/[^\x00-\xff]/g, "01").length;
            };
            function pwdFocus(){
              if($('#chageI').attr('class')=='ace-icon fa fa-eye-slash'){
                $('#newPwd').attr('type','password');
              }else{
                $('#newPwd').attr('type','text');
              }
            };
        </script>