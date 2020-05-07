<div class="main-content-inner">
    <div class="page-content">
        <div class="row">
            <div class="col-xs-12">
                
                <div class="box box-default">
                <div class="box-body">
                    <form class="form-horizontal margin-10" role="form" id="appInfo">
                        <div class="form-group">
                            <label class="col-sm-2 control-title no-padding" for="form-field-1">基本信息</label>

                        </div>
                        
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-padding" for="form-field-1">*&nbsp;Appid</label>

                            <div class="col-sm-6">
                                <input type="text" id="form-field-1" value="" name="appId"  disabled="disabled" class="form-control">
                                <span class="control-disabled">系统生成不可修改</span>
                            </div>
                            <div class="col-sm-4 control-tips">
                                <span class="has-tips">
                                    <i class="fa fa-question-circle" id="appIdDescribe"></i>
                                </span>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label for="form-field-2" class="col-sm-2 control-label no-padding">*&nbsp;Appkey</label>
                            <div class="col-sm-6">
                                <input type="text" name="" id="form-field-2" value="" name="serverKey" disabled="disabled" class="form-control">
                                <span class="control-disabled">系统生成不可修改</span>
                            </div>
                            <div class="col-sm-4 control-tips">
                                <span class="has-tips">
                                    <i class="fa fa-question-circle" id="appKeyDescribe"></i>
                                </span>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label for="form-field-4" class="col-sm-2 control-label no-padding">*&nbsp;应用名称</label>
                            <div class="col-sm-6">
                                <input type="text" name="name" id="form-field-4" maxLength="20" class="form-control check">
                                <span class="control-disabled">不超过10个汉字</span>
                            </div>
                            <div class="col-sm-4 control-tips"></div>
                        </div>
                        
                        <div class="form-group">
                            <label for="form-field-5" class="col-sm-2 control-label no-padding">*&nbsp;应用简介</label>
                            <div class="col-sm-6">
                                <textarea id="form-field-5"  style="resize:none"  name="description" minLength="60" maxLength="200" class="form-control check"></textarea>
                                <span class="control-disabled">不超过100个汉字</span>
                            </div>
                            <div class="col-sm-4 control-tips"></div>
                        </div>
                        
                        <div class="form-group">
                            <label for="form-field-6" class="col-sm-2 control-label no-padding">*&nbsp;应用图标</label>
                            <div class="col-sm-6" id="iconDiv">
                                <a href="javascript:void(0);" id="upFile" class="form-file pull-left">
                                    <i class="fa fa-plus"></i>
                                    <input type="file" name="file" id="sourceFile" onchange="addIcon(this)">
                                </a>
                                <span class="form-file-tips pull-left">图标尺寸建议512px*512px</span>
                            </div>
                            <div class="col-sm-4 control-tips"></div>
                            <input type="hidden" name="iconUrl" id="iconUrl" />
                            <input type="hidden" name="icon" id="iconName" />
                        </div>
                        
                        <div class="form-group">
                            <label for="form-field-15" class="col-sm-2 control-label no-padding">*&nbsp;域名</label>
                            <div class="col-sm-6" style="width:110px">
                                <input type="text" name="protocol" id="form-field-15-1" value="http"  class="form-control check">
                            </div>
                            <div class="col-sm-6" style="width:300px">
                                <input type="text" name="domain"  id="form-field-15-2" class="form-control check">
                            </div>
                            <div class="col-sm-6" style="width:110px">
                                <input type="text" name="port" id="form-field-15-3" value="80"  class="form-control check">
                            </div>
                            <div class="col-sm-4 control-tips"></div>
                        </div>
                        
                        <div class="form-group">
                            <label for="form-field-16" class="col-sm-2 control-label no-padding">上下文</label>
                            <div class="col-sm-6" style="width:150px;">
                                <input type="text" name="context" id="form-field-16" class="form-control">
                            </div>
                            <div class="col-sm-4 control-tips"></div>
                        </div>
                        
                        <div class="form-group">
                            <label for="form-field-8" class="col-sm-2 control-label no-padding">*&nbsp;首页URL</label>
                            <div class="col-sm-6">
                                <input type="text" name="indexUrl" id="form-field-8" maxLength="500" class="form-control check">
                            </div>
                            <div class="col-sm-4 control-tips">
                                <span class="has-tips">
                                    <i class="fa fa-question-circle" id="indexUrl"></i>
                                </span>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label for="form-field-9" class="col-sm-2 control-label no-padding">*&nbsp;登录URL</label>
                            <div class="col-sm-6">
                                <input type="text" name="verifyUrl" id="form-field-9" maxLength="500" class="form-control">
                            </div>
                            <div class="col-sm-4 control-tips">
                                <span class="has-tips">
                                    <i class="fa fa-question-circle" id="verifyUrl"></i>
                                </span>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label for="form-field-10" class="col-sm-2 control-label no-padding">*&nbsp;退出URL</label>
                            <div class="col-sm-6">
                                <input type="text" name="invalidateUrl" id="form-field-10" maxLength="500" class="form-control">
                            </div>
                            <div class="col-sm-4 control-tips">
                                <span class="has-tips">
                                    <i class="fa fa-question-circle" id="invalidateUrl"></i>
                                </span>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label class="col-sm-2 control-title no-padding">订阅信息</label>
                        </div>
                        <div class="form-group">
                              <label for="form-field-11" class="col-sm-2 control-label no-padding">*&nbsp;订阅类型</label>
                              <div class="col-sm-6">
                                  <p>
                                    <#--  <label class="inline">
                                          <input type="radio" class="wp orderType"  name="orderType" value="1"/>
                                          <span class="lbl"> 系统订阅个人需授权</span>
                                      </label>
                                     -->
                                      <label class="inline">
                                          <input type="radio" class="wp orderType"  name="orderType" value="2" />
                                          <span class="lbl"> 系统订阅个人免费</span>
                                      </label>
                                      <label class="inline">
                                          <input type="radio" class="wp orderType"  name="orderType" value="3" />
                                          <span class="lbl"> 单位订阅个人免费</span>
                                      </label>
                                      <#--
                                      <label class="inline">
                                          <input type="radio" class="wp orderType"  name="orderType" value="4" />
                                          <span class="lbl"> 单位订阅个人需授权</span>
                                      </label>
                                      -->
                                  </p>
                              </div>
                              <div class="col-sm-4 control-tips"></div>
                          </div>
                        
                        <div class="form-group">
                            <label for="form-field-12" class="col-sm-2 control-label no-padding">*&nbsp;适用机构</label>
                            <div class="col-sm-6">
                                <label class="inline">
                                    <input type="checkbox" name="unitTypeArray" value="1" class="wp unitType" />
                                    <span class="lbl">教育局</span>
                                </label>
                                <label class="inline">
                                    <input type="checkbox" name="unitTypeArray" value="2" class="wp unitType" />
                                    <span class="lbl">学校</span>
                                </label>
                            </div>
                            <div class="col-sm-4 control-tips" id="unitTypeTips"></div>
                        </div>
                        
                        <div class="form-group">
                            <label for="form-field-13" class="col-sm-2 control-label no-padding">*&nbsp;适用对象</label>
                            <div class="col-sm-6">
                                <label class="inline">
                                    <input type="checkbox" name="userTypeArray" value="1" id="student" class="wp userType" />
                                    <span class="lbl"> 学生</span>
                                </label>
                                <label class="inline">
                                    <input type="checkbox" name="userTypeArray" value="2" id="teacher" class="wp userType" />
                                    <span class="lbl"> 教师</span>
                                </label>
                                <label class="inline">
                                    <input type="checkbox" name="userTypeArray" value="3" id="parent" class="wp userType" />
                                    <span class="lbl"> 家长</span>
                                </label>
                            </div>
                            <div class="col-sm-4 control-tips" id="userTypeTips"></div>
                        </div>
                        
                        <div class="form-group">
                            <label for="form-field-14" class="col-sm-2 control-label no-padding">*&nbsp;适用学段</label>
                            <div class="col-sm-6">
                                <label class="inline">
                                    <input type="checkbox" name="sectionsArray" value="0" class="wp sections" />
                                    <span class="lbl"> 幼儿园</span>
                                </label>
                                <label class="inline">
                                    <input type="checkbox" name="sectionsArray"  value="1" class="wp sections" />
                                    <span class="lbl"> 小学</span>
                                </label>
                                <label class="inline">
                                    <input type="checkbox" name="sectionsArray"  value="2" class="wp sections" />
                                    <span class="lbl"> 初中</span>
                                </label>
                                <label class="inline">
                                    <input type="checkbox"  name="sectionsArray" value="3" class="wp sections" />
                                    <span class="lbl"> 高中</span>
                                </label>
                            </div>
                            <div class="col-sm-4 control-tips" id="sectionsTips"></div>
                        </div>
                        
                        <div class="form-group">
                              <label for="form-field-17" class="col-sm-2 control-label no-padding">*&nbsp;打开方式</label>
                              <div class="col-sm-6">
                                  <p>
                                      <label class="inline">
                                          <input type="radio" class="wp openType" value="1" name="openType"  />
                                          <span class="lbl">div</span>
                                      </label>
                                      <label class="inline">
                                          <input type="radio" class="wp openType" value="2" name="openType"   />
                                          <span class="lbl">iframe</span>
                                      </label>
                                      <label class="inline">
                                          <input type="radio" class="wp openType" value="3" name="openType" checked  />
                                          <span class="lbl">新开页面</span>
                                      </label>
                                  </p>
                              </div>
                              <div class="col-sm-4 control-tips" id="openTypeTips"></div>
                          </div>
                          
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-padding" for="form-field-1"></label>

                            <div class="col-sm-6">
                                <a href="javascript:void(0)" class="btn btn-blue" id="save">&nbsp;保存&nbsp;</a>
                                <a href="javascript:moduleContentLoad('${request.contextPath}/system/server/appList');" class="btn btn-blue" id="cancel">&nbsp;取消&nbsp;</a>
                            </div>
                            <div class="col-sm-4 control-tips">
                            </div>
                        </div>
                    </form>
                    
                </div>
            </div>
            <!-- PAGE CONTENT ENDS -->
        </div><!-- /.col -->
    </div><!-- /.row -->
</div><!-- /.page-content -->
</div>
<script>
$(function(){
  $('#appIdDescribe').on('click',function(){
    layer.tips('应用在平台的唯一标识符', '#appIdDescribe',{
        tips: [2, '#4C4C4C'],
        time: 3000, //3s后自动关闭
        maxWidth: 360
    });
  });
  
  $('#appKeyDescribe').on('click',function(){
    layer.tips('约定字符串,双方根据一定规则，通过该字段生成对应的校验串', '#appKeyDescribe',{
        tips: [2, '#4C4C4C'],
        time: 3000, //3s后自动关闭
        maxWidth: 360
    });
  });
  
  $('#indexUrl').on('click',function(){
    layer.tips('跳转第三方应用的入口地址', '#indexUrl',{
        tips: [2, '#4C4C4C'],
        time: 3000, //3s后自动关闭
        maxWidth: 360
    });
  }); 
  
  
  $('#verifyUrl').on('click',function(){
    layer.tips('调用该接口通知第三方应用完成登录', '#verifyUrl',{
        tips: [2, '#4C4C4C'],
        time: 3000, //3s后自动关闭
        maxWidth: 360
    });
  }); 
  
  
  $('#invalidateUrl').on('click',function(){
    layer.tips('调用该接口通知第三方应用完成退出', '#invalidateUrl',{
        tips: [2, '#4C4C4C'],
        time: 3000, //3s后自动关闭
        maxWidth: 360
    });
  }); 
  
  $('.check').on('blur',validator);//校验
  $('.unitType').on('click',selectUnitType);//选择单位类型
  //$('.orderType').on('click',selectOrderType);//订阅类型选择
  $('#save').on('click',save);//保存
});

//保存
function save(){
  $('.has-error').remove();
  $('.check').trigger('blur');
  if($('#iconUrl').val() == ""){
    showTips("请上传图标",$('#upFile').next());
  }
  //var orderTypeCheckedNum = $('.orderType:checked').length;//订阅类型选中的数量
  var unitTypeCheckedNum = $('.unitType:checked').length ;//单位类型选中的数量
  var userTypeCheckedNum = $('.userType:checked').length ;//用户类型选中的数量
  var userTypeDisableNum = $('.userType:disabled').length;//用户类型禁用状态
  var sectionsCheckedNum = $('.sections:checked').length ;//学段选中的数量
  var sectionsDisabledNum = $('.sections:disabled').length ;//学段禁用状态
  var openTypeCheckNum = $('.openType:checked').length;
  
 // if(orderTypeCheckedNum == 0){
  //  showTips("请选择订阅类型",$('#orderTypeTips'));
  //}
  if(unitTypeCheckedNum == 0){
    showTips("请选择单位分类",$('#unitTypeTips'));
  }
  
  if(userTypeDisableNum == 0 && userTypeCheckedNum == 0){
    showTips("请选择适用对象",$('#userTypeTips'));
  }
  
  if(sectionsDisabledNum == 0 && sectionsCheckedNum == 0){
    showTips("请选择学段",$('#sectionsTips'));
  }
  
  if(openTypeCheckNum == 0){
    showTips("请选择打开方式",$('#openTypeTips'));
  }
  
  var hasError = $('.has-error').length;//页面有错误的状态数
  if(hasError>0){
    return;
  }
  
  $('#save').off('click');//解除绑定
  $.post("${request.contextPath}/system/server/saveAppInfo",$('#appInfo').serialize(),function(result){
    $('#save').on('click',save);//重新绑定
    var json =  JSON.parse(result);
    if(json.code != -1){
      moduleContentLoad('${request.contextPath}/system/server/appList');
    }else{
      showMsgError("新增应用失败","",function(){
        layer.closeAll('dialog');
      });
    }
  });
}


function addIcon(e){
  $('#sourceFile').upload({
    url:"${request.contextPath}/system/server/upload",
    dataType:"json",
    onComplate: function (result) {
      var $tips = $('#iconDiv').find('span');
      if(result.code == 1){
        $('#iconUrl').val(result.iconUrl);
        var html = "<a href='javascript:void(0);' id='imgA' class='pull-left'><img src="+result.fullUrl+" width='94px' height='94px' /></a>";
        if($('#imgA')){
          $('#imgA').remove();
        }
          $('#iconDiv').find('#upFile').after(html);
          $tips.empty();
      }else{
        $tips.html('<span class="has-error"><i class="fa fa-times-circle"></i>&nbsp;'+result.msg+'</span>');
      }
    }
  });
  $("#sourceFile").upload("ajaxSubmit");
}

//选择订阅类型
function selectOrderType(){
  var orderType = $(this).val();
  var isOnlyEducation = false;//是否只选中了教育局
  var unitTypeCheckedNum = $('.unitType:checked').length;
  if(unitTypeCheckedNum == 1){
    var unitType = $('.unitType:checked').val();
    if(unitType == 1){
      isOnlyEducation = true;
    }
  }
  
  if(orderType == 4){
    $('.userType:not("#teacher")').removeAttr('checked');
    if(!$('#teacher').is(":checked")){
      $('#teacher').click();
    }
    $('.userType').attr('disabled','disabled');
  }else{
    if(!isOnlyEducation){
      $('.userType').removeAttr('disabled');
    }
  }
}

//选择单位类型
function selectUnitType(){
  var unitType = $(this).val();
  //var orderType = $('.orderType:checked').val();
  var isChecked = $(this).is(":checked");//当前的选中状态
  var unitTypeCheckedNum = $('.unitType:checked').length ;//单位类型选中的数量
  if(unitTypeCheckedNum == 2){//两个都选中
    //if(orderType != 2){
      $('.userType').removeAttr('checked');
      $('.userType').removeAttr('disabled');
    //}
    $('.sections').removeAttr('checked');
    $('.sections').removeAttr('disabled');
  }else if(unitTypeCheckedNum == 1){//只选中一个
    if(unitType == 1){
      if(isChecked){
        $('.userType:not("#teacher")').removeAttr('checked');
        $('.sections').removeAttr('checked');
        if(!$('#teacher').is(":checked")){
          $('#teacher').click();
        }
        $('.userType').attr('disabled','disabled');
        $('.sections').attr('disabled','disabled');
      }else{
        //if(orderType != 2){
          $('.userType').removeAttr('checked');
          $('.userType').removeAttr('disabled');
       // }
        $('.sections').removeAttr('checked');
        $('.sections').removeAttr('disabled');
      }
    }else{
      if(!isChecked){
        $('#teacher').click();
        $('.userType').attr('disabled','disabled');
        $('.sections').attr('disabled','disabled');
      }
    }
  }else{//都没选中
    //if(orderType != 2){
      $('.userType').removeAttr('checked');
      $('.userType').removeAttr('disabled');
    //}
    $('.sections').removeAttr('checked');
    $('.sections').removeAttr('disabled');
  }
  
}

//校验
var msgDomain = "success";
var msgProtocol = "success";
var msgPort = "success";
function validator(){
  var value = $.trim($(this).val());
  var id = $(this).attr('id');
  var $tips = $(this).parents('.form-group').find('.control-tips');
  var msg = "";
  if(id!=null && id!=""){
    if(id == "form-field-4"){//应用名称校验
      var maxLength = $(this).attr("maxLength");
      msg = validateAppName(value,maxLength);
    }else if(id == "form-field-5"){//应用描述校验
      var maxLength = $(this).attr("maxLength");
      var minLength = $(this).attr("minLength");
      msg = validateDescribe(value,minLength,maxLength);
    }else if(id == "form-field-8" ){//首页url校验
      var maxLength = $(this).attr("maxLength");
      msg = validateUrl(value,maxLength,id);
    }else if(id == "form-field-15-1"||id == "form-field-15-2"||id == "form-field-15-3"){
      var protocolVal = $("#form-field-15-1").val();
      var domainVal = $("#form-field-15-2").val();
      var portVal = $("#form-field-15-3").val();
      msgProtocol = validateProtocol(protocolVal);
      msgDomain = validateDomain(domainVal);
      msgPort = validatePort(portVal);
    }
    
    if(id == "form-field-15-1"||id == "form-field-15-2"||id == "form-field-15-3"){
      if(msgProtocol!="success"){
        msg = msgProtocol;
      }else if(msgDomain!="success"){
        msg = msgDomain;
      }else if(msgPort!="success"){
        msg = msgPort;
      }else{
        msg = "success";
      }
    }
    
  }
  showTips(msg,$tips);
}

function validateUrl(value,maxLength,id){
  if(value==""){
    return "请填写url地址";
  }
  
  if(getLength(value)> maxLength){
    return "内容不能超过" + maxLength + "个字节（一个汉字为两个字节）";
  }
  
  /* if(id == "form-field-8"){
    if(!/^((http|https):\/\/)/.test(value)){
      return "请填写正确的协议头,如http://";
    }
  } */
  return "success";
}

function validateDomain(domain){
  if(domain == ""){
    return "请填写域名";
  }
  return "success";
}

function validateIp(ip){
  if(ip == "" || !/^((2[0-4]\d|25[0-5]|[01]?\d\d?)\.){3}(2[0-4]\d|25[0-5]|[01]?\d\d?)$/.test(ip)){
    return "请填写正确的ipv4地址,如127.0.0.1";
  }
  return "success";
}

function validatePort(port){
  if(port == "" || !/^([0-9]|[1-9]\d{1,3}|[1-5]\d{4}|6[0-5]{2}[0-3][0-5])$/.test(port)){
    return "请填写正确的端口号,如80";
  }
  return "success";
}

function validateProtocol(protocol){
  if(protocol == ""||!/^(http|https)$/.test(protocol)){
    return "请填写正确的http协议头,如http";
  }
  return "success";
}

function validateDescribe(value,minLength,maxLength){
  if(value == ""){
    return "请填写应用描述";
  }else if(getLength(value)> maxLength){
    return "内容不能超过" + maxLength + "个字节（一个汉字为两个字节）";
  }else if(getLength(value)<minLength){
    return "为了更好的推广您的应用，请维护"+(minLength/2)+"字以上的简介";
  }
  return "success";
}

function validateAppName(value,maxLength){
  if(value == ""){
    return "请填写应用名称";
  }else if(getLength(value)> maxLength){
    return "内容不能超过" + maxLength + "个字节（一个汉字为两个字节）";
  }
  
  var msg = "success";
   $.ajax({
    url:"${request.contextPath}/system/server/checkAppName",
    data:{'appName':value},
    type:"post",
    async:false,//这里使ajax请求变为同步
    success:function(result){
      var json =  JSON.parse(result);
      if(json.code == -1){
        msg =  "已存在相同应用";
      }
    }
  }); 
  return msg;
  
}

function showTips(msg,$tips){
  var tipsLength = $tips.find('.has-tips').length;
  if(tipsLength > 0){
    $tips = $tips.find('.has-tips');
    $tips.next().remove();
    if(msg == "success"){
      $tips.after('<span class="has-success"><i class="fa fa-check-circle"></i>&nbsp;正确</span>');
    }else if(msg == "nocheck"){
      $tips.next().remove();
    }else{
      $tips.after('<span class="has-error"><i class="fa fa-times-circle"></i>&nbsp;'+msg+'</span>');
    }
  }else{
    if(msg == "success"){
      $tips.html('<span class="has-success"><i class="fa fa-check-circle"></i>&nbsp;正确</span>');
    }else if(msg == "nocheck"){
      $tips.empty();
    }else{
      $tips.html('<span class="has-error"><i class="fa fa-times-circle"></i>&nbsp;'+msg+'</span>');
    }
  }
}
</script>