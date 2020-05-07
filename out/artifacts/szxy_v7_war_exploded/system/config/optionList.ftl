
<div role="tabpanel" class="tab-pane active" id="aa">
    <h4 class="no-margin-top"><b>平台公共参数</b></h4>
    <table class="table table-outline">
        <thead>
            <tr>
                <th class="text-right" width="20%">参数</th>
                <th class="text-center" width="30%">设置</th>
                <th class="text-center" width="30%">默认值</th>
                <th class="text-left" width="15%">说明</th>
                <th class="text-left" width="10%">是否是按钮</th>
            </tr>
        </thead>
        <tbody>
            <#if systemOptionList??>
                <#list systemOptionList as systemOption>
                    <tr>
                        <td class="text-right td-lock"><span class="input-icon"><span class="txt-val">${systemOption.name!}（${systemOption.optionCode!}）</span></span></td>
                        <#if !systemOption.valueType??||systemOption.valueType==0>
                            <td class="text-center td-lock">
                                <span class="input-icon">
                                    <input type="text" value="" class="form-control" data-v="${systemOption.nowValue!}" data-code="${systemOption.optionCode}" data-isbase="true">
                                    <span class="txt-val">${systemOption.nowValue!}</span>
                                    <i class="fa fa-pencil"></i>
                                </span>
                            </td>
                            <td class="text-center td-lock"><span class="input-icon"><span class="txt-val">${systemOption.defaultValue!}</span></span></td>
                        <#else>
                            <td class="text-center">
                                <label class="switch-label">
                                    <input name="switch-field-1" class="wp wp-switch checkValue" type="checkbox" data-code="${systemOption.optionCode}" data-isbase="true" <#if systemOption.nowValue??&&systemOption.nowValue = "1">checked</#if>>
                                    <span class="lbl"></span>
                                </label>
                            </td>
                            <td class="text-center">
                                <label class="switch-label">
                                    <input class="wp wp-switch" type="checkbox" disabled="true" <#if systemOption.defaultValue??&&systemOption.defaultValue = "1">checked</#if>>
                                    <span class="lbl"></span>
                                </label>
                            </td>
                         </#if>
                        
                        <td class="text-left td-lock"><span class="input-icon"><span class="txt-val">${systemOption.description!}</span></span></td>
                        <td class="text-left td-lock"><span class="input-icon"><span class="txt-val">
                            <!--后期去掉start-->
                            <label class="switch-label">
                                <input name="switch-field-1" class="wp wp-switch modifyValueType" type="checkbox" data-code="${systemOption.optionCode}" data-isbase="true" <#if systemOption.valueType??&&systemOption.valueType!=0>checked</#if>>
                                <span class="lbl"></span>
                            </label>
                            <!--后期去掉end-->
                        </span></span></td>
                    </tr>
                </#list>
            </#if>
            
        </tbody>
    </table>
    
    <h4 class="no-margin-top"><b>系统参数</b></h4>
    <table class="table table-outline">
        <thead>
            <tr>
                <th class="text-right" width="20%">参数</th>
                <th class="text-center" width="30%">设置</th>
                <th class="text-center" width="30%">默认值</th>
                <th class="text-left" width="15%">说明</th>
                <th class="text-left" width="10%">是否是按钮</th>
            </tr>
        </thead>
        <tbody>
            <#if systemIniList??>
                <#list systemIniList as systemIni>
                    <tr>
                        <td class="text-right td-lock"><span class="input-icon"><span class="txt-val">${systemIni.name!}（${systemIni.iniid!}）</span></span></td>
                        <#if !systemIni.valueType??||systemIni.valueType=0>
                            <td class="text-center td-lock">
                                <span class="input-icon">
                                    <input type="text" value="" class="form-control" data-v="${systemIni.nowvalue!}" data-code="${systemIni.iniid}" data-isbase="false">
                                    <span class="txt-val">${systemIni.nowvalue!}</span>
                                    <i class="fa fa-pencil"></i>
                                </span>
                            </td>
                            <td class="text-center td-lock"><span class="input-icon"><span class="txt-val">${systemIni.dvalue!}</span></span></td>
                        <#else>
                            <td class="text-center">
                                <label class="switch-label">
                                    <input name="switch-field-1" class="wp wp-switch checkValue" type="checkbox" data-code="${systemIni.iniid}" data-isbase="false" <#if systemIni.nowvalue??&&systemIni.nowvalue = "1">checked</#if>>
                                    <span class="lbl"></span>
                                </label>
                            </td>
                            <td class="text-center">
                                <label class="switch-label">
                                    <input class="wp wp-switch" type="checkbox" disabled="true" <#if systemIni.dvalue??&&systemIni.dvalue = "1">checked</#if>>
                                    <span class="lbl"></span>
                                </label>
                            </td>
                         </#if>
                         
                        <td class="text-left td-lock"><span class="input-icon"><span class="txt-val">${systemIni.description!}</span></span></td>
                        <td class="text-left td-lock"><span class="input-icon"><span class="txt-val">
                            <!--后期去掉start-->
                            <label class="switch-label">
                                <input name="switch-field-1" class="wp wp-switch modifyValueType" type="checkbox" data-code="${systemIni.iniid}" data-isbase="false" <#if systemIni.valueType??&&systemIni.valueType!=0>checked</#if>>
                                <span class="lbl"></span>
                            </label>
                            <!--后期去掉end-->
                        </span></span></td>
                    </tr>
                </#list>
            </#if>
        </tbody>
    </table>
</div>
<!-- inline scripts related to this page -->
<script>
      $(function() {
        function lockSave(){
            $('.td-edit').find('.txt-val').text($('.td-edit').find('.form-control').val());
            $('.td-lock').removeClass('td-edit');
        };
        $('.td-lock i.fa').click(function(e){
            e.stopPropagation();
            lockSave();
            var txt=$(this).siblings('.txt-val').text();
            $(this).parents('.td-lock').addClass('td-edit').find('.form-control').val(txt);
        });
        $(document).click(function(event){
            var eo=$(event.target);
            if(!eo.hasClass('form-control') && !eo.parents('.td-lock').length){
                lockSave();
            };
        });
        
        $('.td-lock input.form-control').on("blur",{condition:"inputType"},updateValue);
        $('.checkValue').on("click",{condition:"checkType"},updateValue);
        $('.modifyValueType').on("click",modifyValueType);
      })
      
      function updateValue(event){
          var condition = event.data.condition;
          var code = $(this).data("code");
          var oldVal = $(this).data("v");
          var isbase=$(this).data("isbase");//是否是base_sys_option还是sys_option
          var _this = $(this);
          var val="";
          if(condition=="inputType"){
            val = $.trim($(this).val());
            if(jbkLength(val)>1000){
                layer.alert("内容过长，不能超过1000个字节，一个汉字两个字节");
                errorText(_this,oldVal);
                return;
            }
          }
          if(condition=="checkType"){
            if($(this).prop('checked')){
                val = "1";
            }
            else{
                val = "0";
            }
          }
          $.ajax({
          url:"${request.contextPath}/system/sysOption/updateNowValue",
          type:"post",
          data:{
            'code':code,
            'nowValue':val,
            'isBase':isbase
          },
          dataType: "json",
          success:function(result){
            showMsgSuccess(result.msg,"提示",function(index){
              layer.close(index);
              if(!result.success){
              errorText(_this,oldVal);
              }
            });
          }
        });
      }
      
      function modifyValueType(){
        var code = $(this).data("code");
        var isbase=$(this).data("isbase");//是否是base_sys_option还是sys_option
        var val =0;
        if($(this).prop('checked')){
            val = 1;
        }
        else{
            val = 0;
        }
        $.ajax({
          url:"${request.contextPath}/system/sysOption/updateValueType",
          type:"post",
          data:{
            'code':code,
            'valueType':val,
            'isBase':isbase
          },
          dataType: "json",
          success:function(result){
            showMsgSuccess(result.msg,"提示",function(index){
              layer.close(index);
              if(!result.success){
              errorText(_this,oldVal);
              }
            });
          }
        });
      }
      
      //获取jbk编码字节数
      //function jbkLength(str){
      //  return str.replace(/[^x00-xFF]/g,'**').length;
      //}
      //function errorText(_this,oldVal){
      //  _this.val(oldVal);
      //  _this.next('.txt-val').text(oldVal);
        //_this.siblings('.fa').click();
      //}
    </script>