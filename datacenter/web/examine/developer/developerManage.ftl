    <div class="page-content">
        <div class="row">
            <div class="col-xs-12">
                <!-- PAGE CONTENT BEGINS -->
                <div class="box box-default">
                    <div class="box-body">
                        <h3 class="no-margin">开发者管理</h3>
                        <hr />
                        <table class="table table-striped table-hover no-margin">
                            <thead>
                                <tr>
                                    <!--  <th>单位名称</th>-->
                                    <th>开发者</th>
                                    <th>订阅数据</th>
                                    <th>注册时间</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                            <#if developerDtos?exists && developerDtos?size gt 0>
                            <#list developerDtos as d>
                                <tr>
                                    <td>${d.username!}</td>
                                    
                                    <td>
                                    <#if d.interfaceNames?exists && d.interfaceNames?size gt 0>
                                        <#assign sum=0>
                                        <#list d.interfaceNames as v>
                                            <#assign sum=sum+1>
                                            ${v!} <#if (sum%3==0)> <br> </#if>
                                        </#list>
                                    </#if>
                                    </td>
                                    <td>${d.creationTime!}</td>
                                    <td>
                                        <a href="javascript:loadPage2('${request.contextPath}/datacenter/examine/developer/info?developerId=${d.id!}');" class="table-btn color-blue">查看</a>
                                        <a href="javascript:modfiyPassword('${d.id!}')" class="table-btn color-blue">重置密码</a>
                                        <#if isPower?default(false)>
                                            <a href="javascript:doIsPower('${d.id!}');" class="table-btn color-blue">授权</a>
                                        </#if>
                                        <#if (d.isCheck==1)>
                                            <span class="badge badge-blue">待审核</span>
                                        </#if>
                                    </td>
                                </tr>
                            </#list>
                            </#if>
                            </tbody>
                        </table>
                    </div>
                </div>
                
            </div><!-- /.col -->
        </div><!-- /.row -->
    </div><!-- /.page-content -->
    <script>
    $(function(){
      //显示单位
     // $('.main-content').unbind().on('click','.js-showNum',function(){
      $('.js-showNum').on('click',function(){
          var data=$(this).data('servers');
          if(data==''){
            return;
          }
          layer.tips(data, $(this),{
              tips: [2,'#4c4c4c'],
              time: 1500, //10s后自动关闭
              maxWidth: 360
          });
      });
    });
    function loadPage2(url){
      moduleContentLoad(url);
    }
    function modfiyPassword(id){
      $.post('${request.contextPath}/datacenter/examine/developer/defaultPw?developerId='+id,function(msg){
        var data=JSON.parse(msg);
        if(data.success){
          showMsgSuccess('重置密码成功');
          return;
        }
        showMsgError('重置密码失败');
      });
    }
    
    var result;
    function doIsPower(id){
        loadPage2('${request.contextPath}/datacenter/examine/developer/interface/index?developerId='+id);
    }
    function openWin(url) {
      $('body').append($('<a href="'+url+'" target="_blank" id="openWin"></a>'))
      document.getElementById("openWin").click();//点击事件
      $('#openWin').remove();
    }
    </script>
            