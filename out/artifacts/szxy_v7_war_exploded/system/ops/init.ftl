<div class="box box-default">
    <div class="box-body">
        <ul id="breadcrumb" class="clearfix" <#if fromDesktop?default(false)>style="display: none"</#if>>
            <li id="passwd" class="active"><span>重设密码</span></li>
            <li id="option" class="active"><span>参数设置</span></li>
            <li id="license" class="active"><span>系统激活</span></li>
        </ul>
        <form class="form-horizontal" role="form">
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding" for="form-field-1">*&nbsp;顶级单位名称</label>

                <div class="col-sm-5">
                    <input id="unitName" type="text" id="form-field-1" value="<#if licenseInfo?exists>${licenseInfo.unitName!}</#if>" placeholder=""
                           class="form-control"> <span class="control-disabled">与序列号内一致才能激活</span>
                </div>
                <div class="col-sm-5 control-tips"></div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding" for="form-field-2">*&nbsp;省份</label>
                <div class="col-sm-5">
                    <select class="form-control" onchange="selectProvince(this)" id="province">
                    <#if provinceList?exists && provinceList?size gt 0>
                        <option value="">请选择</option>
                        <#list provinceList as p>
                            <option value="${p.fullCode!}" <#if province?exists && province.fullCode?default('')==p.fullCode!>selected</#if>>${p.regionName!}</option>
                        </#list>
                    </#if>
                    </select>
                </div>
                <div class="col-sm-5 control-tips"></div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding" for="form-field-2">*&nbsp;城市</label>
                <div class="col-sm-5">
                    <select class="form-control" id="city" onchange="selectCity(this);">
                    <#if cityList?exists && cityList?size gt 0>
                        <option value="">请选择</option>
                        <#list cityList as p>
                            <option value="${p.fullCode!}" <#if city ?exists && city.fullCode?default('')==p.fullCode!>selected</#if>>${p.regionName!}</option>
                        </#list>
                    </#if>
                    </select>
                </div>
                <div class="col-sm-5 control-tips"></div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding" for="form-field-2">*&nbsp;区县</label>
                <div class="col-sm-5">
                    <select class="form-control" id="country" onchange="selectCountry(this);">
                    <#if countryList?exists && countryList?size gt 0>
                        <option value="">请选择</option>
                        <#list countryList as p>
                            <option value="${p.fullCode!}" <#if country?exists && country.fullCode?default('')==p.fullCode!>selected</#if>>${p.regionName!}</option>
                        </#list>
                    </#if>
                    </select>
                </div>
                <div class="col-sm-5 control-tips"></div>
                <input type="hidden" id="regionCode" value="${regionCode!}"/>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label no-padding" for="licenseTxt">*&nbsp;输入序列号</label>

                <div class="col-sm-5">
                    <textarea id="licenseTxt" placeholder="" class="form-control" style="height: 200px;">${licenseTxt!}</textarea>
                    <span id="endTime" class="control-disabled"></span>
                </div>
                <div class="col-sm-5 control-tips"></div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding" for=""></label>

                <div class="col-sm-5">
                    <button type="button" class="btn btn-blue js-veri">激活序列号</button>
                </div>
                <div class="col-sm-5 control-tips"></div>
            </div>

            <div class="verification-wrap">
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding" for="form-field-4">*&nbsp;顶级单位账号</label>

                    <div class="col-sm-5">
                        <input id="username" type="text" id="form-field-4" value="<#if topAdmin?exists> ${topAdmin.username!} </#if>" placeholder="" class="form-control">
                    </div>
                    <div class="col-sm-5 control-tips"></div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding" for="form-field-5">*&nbsp;顶级单位密码</label>
                    <div class="col-sm-5">
                  <span class="input-icon" id="eyeChange"> <input id="password" type="password" id="form-field-9"
                                                                  value="" placeholder="请输入密码" class="form-control"> <i class="ace-icon fa fa-eye-slash"></i>
                  </span> <span class="control-disabled">密码只能以数字或字母开头，密码长度不得少于6位</span>
                    </div>
                    <div class="col-sm-5 control-tips"></div>
                </div>
                <#if !topAdmin?exists>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding" for=""></label>

                    <div class="col-sm-5">
                        <button id="createBtn" type="button" class="btn btn-blue">创建管理员</button>
                    </div>
                    <div class="col-sm-5 control-tips"></div>
                </div>
                </#if>
                <div id="serverList" <#if fromDesktop?default(false)>style="display: none"</#if>></div>
                <div class="text-right" <#if fromDesktop?default(false)>style="display: none"</#if>>
                    <a class="btn btn-white" href="${request.contextPath}/system/ops/sysOption">上一步</a>
                </div>
            </div>
        </form>
    </div>
</div>
  <script>
      $(function() {
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
        $('#eyeChange i.fa').click(function() {
          if ($(this).hasClass('fa-eye-slash')) {
            $(this).removeClass('fa-eye-slash').addClass('fa-eye');
            $(this).prev('input').attr('type', 'text');
          } else {
            $(this).removeClass('fa-eye').addClass('fa-eye-slash');
            $(this).prev('input').attr('type', 'password');
          }
        });
        if(${hasExpire?default(-1)}==0){
        $('#endTime').text('当前序列号有效时间至${expireDateFormat!}');
          $("#serverList").load("${request.contextPath}/system/ops/serverList");
        }else if(${hasExpire?default(-1)}==1){
          $('#endTime').text('当前序列号已过期');
        }
        $('.js-veri').click(function() {
            if( $("#province").val() == "" && $("#city").val() == "" && $("#country").val() =="" ) {
                layer.msg("请选择行政区划");
                return ;
            }
            $(".js-veri").attr("disabled","disabled");
          $.post({
            url : '${request.contextPath}/system/ops/verifyLicense',
            data : {
              'unitName' : $('#unitName').val(),
              'licenseTxt' : $('#licenseTxt').val(),
               'regionCode' : $("#regionCode").val()
            },
            success : function(msg) {
              var jsonO = JSON.parse(msg);
              if (jsonO.success) {
                $('#endTime').text(jsonO.msg);
                <#if !fromDesktop?default(false)>
                    $("#serverList").load("${request.contextPath}/system/ops/serverList");
                </#if>
                  $(".js-veri").removeAttr("disabled");
              } else {
                  $(".js-veri").removeAttr("disabled");
                layer.msg(jsonO.msg);
              }
            }
          });
        });

//        $("#serverList").on('click', 'button', function() {
//          var id = this.id;
//          $.post({
//            url : '/system/ops/modifyServer',
//            data : {
//              'id' : id,
//              'protocol' : $('#protocol_' + id).val(),
//              'domain' : $('#domain_' + id).val(),
//              'secondDomain' : $('#secondDomain_' + id).val(),
//              'port' : $('#port_' + id).val(),
//              'indexUrl' : $('#indexUrl_' + id).val(),
//              'context' : $('#context_' + id).val()
//            },
//            success : function(msg) {
//              var jsonO = JSON.parse(msg);
//              layer.msg(jsonO.msg);
//            }
//          });
//        });
        
        $("#createBtn").click(function(){
          $.post({
            url : '${request.contextPath}/system/ops/addAdmin',
            data : {
              'username' : $('#username').val(),
              'password' : $('#password').val()
            },
            success : function(msg) {
              var jsonO = JSON.parse(msg);
              layer.msg(jsonO.msg);
            }
          });
      	});
          
      });
      //行政区划事件绑定
      function selectProvince(obj) {
          var emptyHtml = '<option value="">请选择</option>';
          if ( $(obj).val() != "" ) {
              $("#regionCode").val($(obj).val());
              $.ajax({
                  type: "get",
                  url: "${request.contextPath}/region/subRegionList",
                  dataType: "json",
                  data: {fullRegionCode:$(obj).val()},
                  success:function (data) {
                      var cityHtml = emptyHtml;
                      var regionArray = JSON.parse(data.msg);
                      for( var i=0; i<regionArray.length; i++) {
                          cityHtml += '<option value="'+regionArray[i].fullCode+'">'+regionArray[i].regionName+'</option>'
                      }
                      $("#city").html(cityHtml);
                  }
              })
          } else {
              $("#city").html(emptyHtml);
          }
          $("#country").html(emptyHtml)
      }
      
      function selectCity(obj) {
          var emptyHtml = '<option value="">请选择</option>';
          if ( $(obj).val() != "" ) {
              $("#regionCode").val($(obj).val());
              $.ajax({
                  type: "get",
                  url: "${request.contextPath}/region/subRegionList",
                  dataType: "json",
                  data: {fullRegionCode:$(obj).val()},
                  success:function (data) {
                      var cityHtml = emptyHtml;
                      var regionArray = JSON.parse(data.msg);
                      for( var i=0; i<regionArray.length; i++) {
                          cityHtml += '<option value="'+regionArray[i].fullCode+'">'+regionArray[i].regionName+'</option>'
                      }
                      $("#country").html(cityHtml);
                  }
              })
          } else {
              $("#country").html(emptyHtml);
          }
      }
      
      function selectCountry(obj) {
          if ( $(obj).val() != "" ) {
              $("#regionCode").val($(obj).val());
          }
      }
  </script>
