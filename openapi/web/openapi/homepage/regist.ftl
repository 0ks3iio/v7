<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
        <meta charset="utf-8" />
        <title>开放平台-注册</title>
        <#include "/openapi/homepage/head.ftl">
    </head>

    <body class="reg">
        <#include "/openapi/homepage/navigationBar.ftl">
        <!--  <div class="header">
            <div class="container">
                <div class="navbar-header">
                    <a href="/developer/index" class="navbar-brand">
                        <img class="logo" src="${logoUrl!}"  alt="">
                        <span>${logoName!}</span>
                    </a>
                </div>
                <div class="header-login">
                    已有账号？<a href="/developer/index?islogin=1">立即登录</a>
                </div>
            </div>
        </div>-->
       
        
        <div class="content">
            <div class="container">
                <h1>注册开发者账号</h1>
                <form name="developer" id="developer" class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-title no-padding" for="form-field-1">基本信息</label>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label no-padding" for="form-field-1"><span class="must">*</span>用户名</label>

                        <div class="col-sm-4">
                            <input name="username" type="text" value="" placeholder="3-16个字符组成，区分大小写，注册成功后不可修改" placemsg="3-16个字符组成，区分大小写，注册成功后不可修改" minLength="3" maxLength="16" class="form-control yz">
                        </div>
                        <div class="col-sm-4 control-tips" id="userNameError">
                        </div>
                    </div>
                     
                    <div class="form-group">
                        <label class="col-sm-4 control-label no-padding" for="form-field-2"><span class="must">*</span>开发者</label>
                        <div class="col-sm-4">
                            <input name="unitName" type="text" value="" placeholder="2-30个中文字符，填写企业名称或个人姓名" placemsg="2-30个中文字符，填写企业名称或个人姓名" minLength="4" maxLength="60"  class="form-control yz">
                        </div>
                        <div class="col-sm-4 control-tips" id="unitNameError">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label no-padding" for="form-field-1"><span class="must">*</span>密码</label>

                        <div class="col-sm-4">
                            <input id="password" name="password" type="text" onfocus="this.type='password'" value="" placeholder="6-16个字母或数字" placemsg="6-16个字母或数字" minLength="6" maxLength="16" class="form-control yz">
                        </div>
                        <div class="col-sm-4 control-tips" id="passwordError">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-title no-padding" for="form-field-1">联系方式</label>

                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label no-padding" for="form-field-3"><span class="must">*</span>联系人姓名</label>

                        <div class="col-sm-4">
                            <input name="realName" type="text" value="" placeholder="2-20个中文字符" placemsg="2-20个中文字符" minLength="4" maxLength="40" class="form-control yz">
                        </div>
                        <div class="col-sm-4 control-tips" id="realNameError">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label no-padding" for="form-field-4"><span class="must">*</span>联系人地址</label>

                        <div class="col-sm-4">
                            <input name="address" type="text" value="" placeholder="1-40个中文字符，请输入详细地址" placemsg="1-40个中文字符，请输入详细地址" minLength="2" maxLength="80" class="form-control yz">
                        </div>
                        <div class="col-sm-4 control-tips">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label no-padding" for="form-field-5"><span class="must">*</span>手机号码</label>

                        <div class="col-sm-4">
                            <input name="mobilePhone" type="text" value="" placeholder="填写11位数字号码" placemsg="手机号格式不正确" class="form-control yz">
                        </div>
                        <div class="col-sm-4 control-tips" id="mobileError">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label no-padding" for="email"><span class="must">*</span>邮箱</label>

                        <div class="col-sm-4">
                            <input maxlength="40" id="email" name="email" type="text" class="typeahead scrollable form-control yz" placeholder="请填写有效邮箱以便接收平台下发消息。" placemsg="请填写有效邮箱以便接收平台下发消息。" autocomplete="off" data-provide="typeahead">
                        </div>
                        <div id="emailErrorDiv" class="col-sm-4 control-tips">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label no-padding" for="form-field-7">官网地址</label>

                        <div class="col-sm-4">
                            <input id="homepage" name="homepage" type="text" value=""  class="form-control yz">
                        </div>
                        <div class="col-sm-4 control-tips">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="" class="sr-only">验证码</label>
                        <label class="col-sm-4 control-label no-padding" for="form-field-9"><span class="must">*</span>验证码</label>

                        <div class="col-sm-2">
                            <input name="verify" id="verify" type="text" value="" placeholder="填写验证码" placemsg="填写验证码" class="form-control yz">
                        </div>
                        <div class="col-sm-2">
                            <a href="javascript:;" class="verCode-img"><img id="verCode" src="${request.contextPath}/developer/verifyImage" alt=""></a>
                            <a href="javascript:;" id="refreshVerifyCode" class="verCode-change">点击换一张</a>
                        </div>
                        <div class="col-sm-4 control-tips">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label no-padding" for="form-field-1"></label>
                        <div class="col-sm-4 clearfix">
                            <label class="inline pull-left">
                                <input id="checkBox" type="checkbox" class="wp" checked/>
                                <span class="lbl no-margin"> 我已阅读并同意</span>
                            </label>
                            <a class="pull-left" target="_blank" href="${request.contextPath}/developer/showClause">《开放平台服务条款》</a>
                        </div>
                        <div class="col-sm-4" id="check-error">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label no-padding" for="form-field-1"></label>
                        <div class="col-sm-4">
                            <input type="button" id="save" class="btn btn-block btn-blue" value="同意并注册开发者"/>
                            <!--  
                            <button id="save" class="btn btn-block btn-blue">同意并注册开发者</button>
                            -->
                        </div>
                        <div class="col-sm-4 control-tips">
                        </div>
                    </div>
                </form>
            </div>
        </div>
        
        <#include "/openapi/homepage/foot.ftl">
        <script src="${resourceUrl}/openapi/js/jquery.mailAutoComplete.js"></script> <!-- 邮箱自动填充 -->
        <!-- page specific plugin scripts -->
<script type="text/javascript">
$(function(){
//邮箱自动填充
  $('#email').mailAutoComplete({
      emails:['qq.com','163.com','126.com','sina.com','sohu.com','yahoo.cn','gmail.com','hotmail.com','live.cn']
  });
  $('.ui-mail').on('click','#mailBox ul li',function(){
    $('#email').blur();
  });
  if (browser.ie){
    var p=$('#password').parent();
    $('#password').remove();
    p.append('<input name="password" type="password" value="" placeholder="6-16个字母或数字" placemsg="6-16个字母或数字" minLength="6" maxLength="16" class="form-control yz">')
  }
});
  
  $('#checkBox').click(function(){
    if($('#checkBox').prop("checked")==true){
      $('#check-error').html('');
    }
  });
  $('#save').click(function(){
    $('.yz').trigger('blur');
    if($('#checkBox').prop("checked")==false){
      promot($('#check-error'),3,"请阅读条款");
      return false;
    }
    var len=$('.has-success').length;
    if($('.has-warning').length>0 || $('.has-error').length>0){
      return false;
    }
    if(($('#homepage').val()=="" && len==8) || len==9){
      $.post('${request.contextPath}/developer/regist/save',$('#developer').serialize(),function(data){
        var jsonO=JSON.parse(data);
        if(jsonO.success){
          location.href='${request.contextPath}/developer/home';
        }else{
          var obj = jsonO.obj;
          if(obj == "phone"){
            promot($('#mobileError'),3,jsonO.msg);
          }
          
          if(obj == "email"){
            promot($('#emailErrorDiv'),3,jsonO.msg);
          }
          
          if(obj == "realname"){
            promot($('#realNameError'),3,jsonO.msg);
          }
          
          if(obj == "unitname"){
            promot($('#unitNameError'),3,jsonO.msg);
          }
          
          if(obj == "password"){
            promot($('#passwordError'),3,jsonO.msg);
          }
          
          if(obj == "username"){
            promot($('#userNameError'),3,jsonO.msg);
          }
          
          return false;
        }
      });
    }else{
      return false;
    }
  });
  $("#refreshVerifyCode").bind("click",function () {
    var srcUrl = $('#verCode').attr("src");
    $('#verCode').attr("src",srcUrl+"?date="+new Date().getTime());
  });
  $('.yz').blur(function(){
    var $this=$(this);
    var name=$this.attr("name");
    var val=$this.val();
    var isVal=(val && val !="");
    var ts=$this.parent().next();
    if(isVal==false && "verify"!=name && "homepage"!=name ){
      if("email"==name){
        ts=$('#emailErrorDiv');
      }
      promot(ts,3,$this.attr('placemsg'));
      return false;
    }
    if("homepage"==name && val==""){
      ts.empty();
    }
    if (name && name != "") {
      if(name=="username"){
        var t=isStr(val);
        var l=isLen($this,val);
        if(isStr(val) && isLen($this,val)){
          $.ajax({
            url:'${request.contextPath}/developer/regist/valid/paramName/'+val,
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
          promot(ts,3,$this.attr('placemsg'));
          return false;
        }         
      }else if(name=='unitName'){
        if(isLen($this,val)){
          promot(ts,1,'');
        }else{
          promot(ts,3,$this.attr('placemsg'));
        }
      }else if(name=='password'){
        if(isPsw(val) && isLen($this,val)){
          promot(ts,1,'');
        }else{
          promot(ts,3,$this.attr('placemsg'));
        }
      }else if(name=='realName'){
        if(isLen($this,val)){
          promot(ts,1,'');
        }else{
          promot(ts,3,$this.attr('placemsg'));
        }
      }else if(name=='address'){
        if(isLen($this,val)){
          promot(ts,1,'');
        }else{
          promot(ts,3,$this.attr('placemsg'));
        }
      }else if(name=='mobilePhone'){
        if (!/^\d+$/.test(val) || val.length!=11) {
          promot(ts,3,$this.attr('placemsg'));
        }else{
          promot(ts,1,'');
        }
      }else if(name=='email'){
        ts=$('#emailErrorDiv');
        if (!/^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/.test(val)) {
          promot(ts,3,$this.attr('placemsg'));
        }else{
          promot(ts,1,'');
        }
      }else if(name=='homepage' && isVal!=false){
        if (getLength(val) > 180) {
          promot(ts,3,'不能超过180个字符');
        }else{
          promot(ts,1,'');
        }
      }else if(name=='verify'){
        if(isVal==false){
          promot(ts.next(),3,$this.attr('placemsg'));
        }else{
          $.ajax({
            url:'${request.contextPath}/developer/regist/valid/verify/'+val,
            type:'GET',
            dataType:'json',
            success:function(msg){
              if(msg.success){
                promot(ts.next(),1,'');
              }else{
                promot(ts.next(),2,'验证码错误');
                var srcUrl = $('#verCode').attr("src");
                $('#verCode').attr("src",srcUrl+"?date="+new Date());
                $this.val('');
              }
            }
          });
        }
      }
    }
  });
  function promot($this,level,msg){
    if(level==1){
      $this.html('<span class="has-success"><i class="fa fa-check-circle"></i>&nbsp;</span>');
    }else if(level==2){
      $this.html('<span class="has-warning"><i class="fa fa-info-circle"></i>&nbsp;'+msg+'</span>');
    }else if(level==3){
      $this.html('<span class="has-error"><i class="fa fa-times-circle"></i>&nbsp;'+msg+'</span>');
    }
  }

  function isStr(value){
    var reg=new RegExp("[\\u4E00-\\u9FFF]+","g");
    return reg.test(value)?false:true;
  };
  
  function isPsw(value){
    var reg=/^[0-9a-zA-Z]*$/g;
    return reg.test(value);
  }
  
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
  function getLength(str) {
    if (str == null) return 0;
    if (typeof str != "string") {
        str += "";
    }
    return str.replace(/[^\x00-\xff]/g, "01").length;
  };
</script>
</body>
</html>
