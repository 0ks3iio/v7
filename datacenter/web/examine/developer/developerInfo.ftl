<a href="javascript:moduleContentLoad('${request.contextPath}/datacenter/examine/developer/index');" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="main-content-inner">
    <div class="">
        <div class="row">
            <div class="col-xs-12">
                <!-- PAGE CONTENT BEGINS -->
                <input id="developerId" type="hidden" value="${developerDto.id!}">
                <input id="ticketKey" type="hidden" value="${developerDto.ticketKey!}">
                <div class="box box-default">
                    <div class="box-body">
                        <h4><b>基本信息</b></h4>
                        <table class="table table-show">
                            <tbody>
                                <tr>
                                    <th>用户名：</th>
                                    <td>${developerDto.username!}</td>
                                </tr>
                                <tr>
                                    <th>开发者：</th>
                                    <td>
                                        <div class="td-lock">
                                            <span class="input-icon">
                                                <input type="text" value="" class="form-control modifyName" id="unitName" style="display: none;">
                                                <span class="txt-val">${developerDto.unitName!}</span>
                                                <i class="fa fa-pencil"></i>
                                            </span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>白名单：</th>
                                    <td>
                                        <div class="td-lock">
                                            <span class="input-icon">
                                                <input type="text" value="" placeholder="多个ip之间用英文逗号分隔" class="form-control modifyIps" id="ips" style="display: none;">
                                                <span class="txt-val">${developerDto.ips!}</span>
                                                <i class="fa fa-pencil"></i>
                                            </span>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <h4><b>联系方式</b></h4>
                        <table class="table table-show">
                            <tbody>
                                <tr>
                                    <th>联系人姓名：</th>
                                    <td>${developerDto.realName!}</td>
                                </tr>
                                <tr>
                                    <th>联系人地址：</th>
                                    <td>${developerDto.address!}</td>
                                </tr>
                                <tr>
                                    <th>手机号码：</th>
                                    <td>${developerDto.mobilePhone!}</td>
                                </tr>
                                <tr>
                                    <th>邮箱：</th>
                                    <td>${developerDto.email!}</td>
                                </tr>
                                <tr>
                                    <th>apKey：</th>
                                    <td>${developerDto.apKey!}</td>
                                </tr>
                            </tbody>
                        </table>
				    </div>
				</div>
                <!-- PAGE CONTENT ENDS -->
            </div><!-- /.col -->
        </div><!-- /.row -->
    </div><!-- /.page-content -->
</div>
<div class="layer layer-scrollContainer layer-sensitive">

</div>
<script>
            $(function(){
                $('.td-lock i.fa').on('click',function(e){
                    e.stopPropagation();
                    var txt=$(this).siblings('.txt-val').text();
                    $(this).hide().siblings('.txt-val').hide().siblings('.form-control').show().val(txt);
                });
                $('.main-content-inner').click(function(event){
                    var eo=$(event.target);
                    if(!eo.hasClass('form-control') && !eo.parents('.td-lock').length){
                        if($('.td-lock .form-control:visible').length>0){
                          var val=$('.td-lock .form-control:visible').val();
                          var type = $('.td-lock .form-control:visible').attr('id');
                          $('.td-lock .form-control:visible').hide().siblings('.txt-val').show().text(val).siblings('i.fa').show();
                          
                          if(type == "unitName"){
                            if(typeof(val)=='string' && val!=''){
                              $.post('${request.contextPath}/datacenter/examine/developer/modifyUnitName',{'developerId':$('#developerId').val(),'unitName':val},function(reslut){
                                var jsonO = JSON.parse(reslut);
                                if(!jsonO.success){
                                  showMsgError(jsonO.msg,'');
                                }
                              });
                            }
                          }
                          
                          if(type == "ips"){
                            if(typeof(val)=='string'){
                              $.post('${request.contextPath}/datacenter/examine/developer/modifyIps',{'developerId':$('#developerId').val(),'ips':val},function(reslut){
                                var jsonO = JSON.parse(reslut);
                                if(!jsonO.success){
                                  showMsgError(jsonO.msg,'');
                                }
                              });
                            }
                          }
                        }
                    }
                });
            });
               
            function loadPage2(url){
              moduleContentLoad(url);
            }
            function showConfirm(content,options,yesFunction,cancelFunction){
              layer.confirm(content, options,yesFunction,cancelFunction);
            }
            function showMsgError(content,title,yesFunction){
              if(!content)content='';
              if(!title)title='错误';
              if(!(typeof yesFunction === "function")){
                  yesFunction = function (index){layer.close(index);}
              }
              options = {btn: ['确定'],title:title, icon: 2,closeBtn:0};
              showConfirm(content, options,yesFunction, function(){});
          }
</script>
