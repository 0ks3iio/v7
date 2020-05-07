<div role="tabpanel" class="tab-pane active" id="aa">
    <form class="form-horizontal margin-10" role="form">
        <#if baseOptionCodes??>
            <#list baseOptionCodes as optionCodeEnum>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding" for="form-field-1">*&nbsp;${optionCodeEnum.getName()}</label>
                    <div class="col-sm-6">
                        <#if optionCodeEnum.getHtmlType()="input">
                            <input type="text" value="<#if codeAndOptionMap[optionCodeEnum.getCode()]??>${codeAndOptionMap[optionCodeEnum.getCode()].nowValue!}</#if>" 
                                placeholder="${optionCodeEnum.getName()}" class="form-control option" data-code="${optionCodeEnum.getCode()}">
                            <span class="control-disabled"><#if codeAndOptionMap[optionCodeEnum.getCode()]??>${codeAndOptionMap[optionCodeEnum.getCode()].description!}</#if></span>
                        <#elseif optionCodeEnum.getHtmlType()="file">
                            <#if codeAndOptionMap[optionCodeEnum.getCode()]??&&codeAndOptionMap[optionCodeEnum.getCode()].nowValue??>
                                <a href='javascript:void(0);' id="upFile" class='form-file pull-left' style='padding:0px'>
                                    <img src='${codeAndOptionMap[optionCodeEnum.getCode()].nowValue}' width='95px' height='95px' />
                                </a>
                            <#else>
                                <a href="javascript:void(0);" class="form-file pull-left" id="upFile">
                                    <i class="fa fa-plus"></i>
                                </a>
                            </#if>
                            <span class="form-file-tips pull-left fileTips">图标尺寸512px*512px</span>
                            <input type="hidden" class="form-control option" id="logoUrl"  data-code="${optionCodeEnum.getCode()}" value ="<#if codeAndOptionMap[optionCodeEnum.getCode()]??>${codeAndOptionMap[optionCodeEnum.getCode()].nowValue!}</#if>"/>
                            
                        <#elseif optionCodeEnum.getHtmlType()="textarea">
                            <textarea class="form-control base-hljs option" style="height: 250px;" data-code="${optionCodeEnum.getCode()}"><#if codeAndOptionMap[optionCodeEnum.getCode()]??>${codeAndOptionMap[optionCodeEnum.getCode()].nowValue!}</#if></textarea>
                        </#if>
                    </div>
                    <div class="col-sm-4 control-tips"></div>
                </div>
            </#list>
        </#if>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding" for="form-field-1"></label>
            <div class="col-sm-6">
                <input  class="btn btn-blue saveBtn" value="  保存  " type="button"/>
                <!--<button class="btn btn-blue">&nbsp;保存&nbsp;</button>-->
            </div>
            <div class="col-sm-4 control-tips">
            </div>
        </div>
    </form>
</div>
<form style="display:none" id="fileForm" enctype="multipart/form-data">
      <input type="file" name="file" id="hiddenUpload">
      <input type="text" name="prePath" value="/upload/system/image/">
</form>
<script>
    $(function(){
        $(".saveBtn").on("click",saveOption);
        $("#upFile").on("click",upload);
        $(".form-control.option").on("blur",checkOption);
    });
    
    function checkOption(){
        var value = $.trim($(this).val());
        var code = $(this).data("code");
        //$(this).next(".has-error").remove();
        var tipDiv = $(this).parent().next(".col-sm-4.control-tips");
        var valLength =value.length;
        var html="";
        tipDiv.empty();
        if(valLength==0){
            html = '<span class="has-error"><i class="fa fa-times-circle"></i>不能为空</span>';
        }
        else if((code=="plat.name")&&valLength>30){
            html = '<span class="has-error"><i class="fa fa-times-circle"></i>内容过长，不能超过30个字</span>';
        }
        else if(code =="plat.abbreviation"&&valLength>25){
            html = '<span class="has-error"><i class="fa fa-times-circle"></i>内容过长，不能超过25个字</span>';
        }
        else if(code =="plat.bottom"&&jbkLength(value)>1000){
            html = '<span class="has-error"><i class="fa fa-times-circle"></i>内容过长,不能超过1000个字符,一个汉字2个字符</span>';
        }
        if(html!=""){
            tipDiv.html(html);
        }
    }
    
    function saveOption(){
        var optionMap = {};
        $(".form-control.option").each(function(i){
            $(this).blur();
            var code = $(this).data("code");
            if($.trim($(this).val())!=""){
                optionMap[code] = $(this).val();
            }
        });
        if($(".has-error").length>0||$(".form-control.option").length!=Object.keys(optionMap).length){
            layer.alert("请填写正确的参数值");
            return;
        }
        
        //for(var prop in optionMap){
         //   if(optionMap.hasOwnProperty(prop)){
         //       alert(prop+","+optionMap[prop]);
         //   }
        //}
        //alert(JSON.stringify(optionMap));
       // return;
      $.ajax({
        url:"${request.contextPath}/system/sysOption/saveBaseOptions",
        type:"post",
        data:{'codeAndValues':JSON.stringify(optionMap)},
        dataType: "json",
        success:function(result){
            showMsgSuccess(result.msg,"提示",function(index){
                layer.close(index);
            });
        }
      });
    }
    
    function upload(){
      var tipDiv = $(this).parent().next(".col-sm-4.control-tips");
      $('#hiddenUpload').unbind();
      $('#hiddenUpload').click().change(function(){
      $('#fileForm').ajaxSubmit({
        url:"/system/server/upload",
        type:"post",
        dataType:"json",
        success:function(result){
            var $tips = $('.fileTips');
            if(result.code == 1){
              $('#logoUrl').val(result.fullUrl);
              var html = "<a href='javascript:void(0);' id='upFile' class='form-file pull-left' style='padding:0px'><img src="+result.fullUrl+" width='95px' height='95px' /></a>";
              if($('#imgA')){
                $('#imgA').remove();
              }
                $(".col-sm-6 .form-file").replaceWith(html);
                tipDiv.empty();
                $tips.empty();
            }else{
              $tips.html('<span class="has-error"><i class="fa fa-times-circle"></i>&nbsp;'+result.msg+'</span>');
            }
          },
          beforeSend:function(){
            $('#upFile').off('click');//禁用图片上传
          },
          complete:function(){
            $('#upFile').on('click',upload);//上传图片
          }
        });
      });
    }
    
    //获取jbk编码字节数
    function jbkLength(str){
        return str.replace(/[^x00-xFF]/g,'**').length;
    }
    
    function getUtfLength(inputStr){
        var i = 0;
        var totalLength = 0;
        /* 计算utf-8编码情况下的字符串长度 */
        for ( i = 0 ; i < inputStr.length ; i++){
               if ( inputStr.charCodeAt(i) <= parseInt("0x7F") ){
                     totalLength += 1;
               }
               else if (inputStr.charCodeAt(i) <= parseInt("0x7FF")){
                     totalLength += 2;
               }
               else if (inputStr.charCodeAt(i) <= parseInt("0xFFFF")){
                     totalLength += 3;
               }
               else if (inputStr.charCodeAt(i) <= parseInt("0x1FFFFF")){
                     totalLength += 4;
               }
               else if (inputStr.charCodeAt(i) <= parseInt("0x3FFFFFF")){
                     totalLength += 5;
               }
               else{
                     totalLength += 6;
               }
         }
         return totalLength;
    }
</script>