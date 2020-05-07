<div class="main-content-inner">
<div class="page-content">
      <div class="row">
        <div class="col-xs-12">
      <#if !apps??||apps?size=0>
              <!-- PAGE CONTENT BEGINS -->
          <div class="no-data-container">
              <div class="no-data no-data-hor">
                  <span class="no-data-img">
                      <img src="${resourceUrl}/images/7choose3/noSelectSystem.png" alt="">
                  </span>
                  <div class="no-data-body">
                      <h3>暂无上线应用</h3>
                      <p class="no-data-txt">如需上线应用，请增加应用</p>
                      <a class="btn btn-lg btn-blue" href="javascript:loadPage('${request.contextPath}/appManage/addApp');">增加应用</a>
                  </div>
              </div>
          </div>
       <#else>
          <!--无记录-->
          <p>
              <a href="javascript:loadPage('${request.contextPath}/appManage/addApp');" class="btn btn-blue">增加应用</a>
          </p>
          
          <div class="row">
            <#list apps as app>
              <div class="col-sm-6" data-id="${app.systemId!}">
                  <div class="box box-default">
                      <div class="row box-body base-apply">
                        <#if app.status == 3|| app.status == 4||app.status == 2||app.status == 0>
                          <i class="fa fa-trash"></i>
                         </#if>
                          <div class="col-md-2 col-sm-12">
                              <div class="base-apply-img"><img src="${app.fullIcon! }" alt="" /></div>
                          </div>
                          <div class="col-md-10 col-sm-12">
                              <div class="base-apply-title">${app.name!}
                              <span class="badge <#if app.status == 3>badge-lightblue<#elseif app.status == 4>badge-red<#elseif app.status == 5>badge-blue<#elseif app.status==2>badge-yellow<#elseif app.status==1>badge-lightgreen<#else>badge-grey</#if>">${app.statusName}</span></div>
                              <div class="base-apply-time">${app.timeStr!}</div>
                              <div class="base-apply-des">${app.description!}</div>
                              <div class="text-right">
                                <#if app.status == 3||app.status == 4>
                                  <a href="javascript:void(0)" class="btn btn-sm btn-lightblue submit">提交</a>
                                 </#if>
                                 <#if app.status == 2||app.status == 3 || app.status == 4>
                                  <a href="javascript:void(0)" class="btn btn-sm btn-lightgreen modify">修改</a>
                                  </#if>
                                  <#if app.status == 0||app.status == 1||app.status == 5>
                                    <a href="javascript:void(0)" class="btn btn-sm btn-lightgreen look">查看</a>
                                  </#if>
                                  <!-- <#if app.status == 1 || app.status == 2>
                                    <a href="javascript:void(0)" class="btn btn-sm btn-blue count">统计数据</a>
                                  </#if> -->
                              </div>
                          </div>
                      </div>
                  </div>
              </div>
              </#list>
          </div>
          </#if>
          <!-- PAGE CONTENT ENDS -->
      </div><!-- /.col -->
  </div><!-- /.row -->
</div><!-- /.page-content -->
</div>
<script>
$(function(){
  $('.fa').on('click',deleteApp);//删除应用
  $('.submit').on('click',submitApp);//提交应用
  $('.modify').on('click',modifyApp);//修改应用
  $('.look').on('click',lookApp);//查看应用
  $('.count').on('click',countData);//统计数据
});

function countData(){
  var id = $(this).parents('.col-sm-6').data('id');
  loadPage('${request.contextPath}/appManage/countData?id='+id);
}

function lookApp(){
  var id = $(this).parents('.col-sm-6').data('id');
  loadPage('${request.contextPath}/appManage/modifyApp?id='+id+"&isLook=1");
}

function  modifyApp(){
  var id = $(this).parents('.col-sm-6').data('id');
  loadPage('${request.contextPath}/appManage/modifyApp?id='+id+"&isLook=0");
}

function submitApp(){
  var id = $(this).parents('.col-sm-6').data('id');
  $.post("${request.contextPath}/appManage/submitAudit",{"id":id},function(result){
    loadPage('${request.contextPath}/appManage/appList');
  });
}

function deleteApp(){
  var id = $(this).parents('.col-sm-6').data('id');
  showConfirm("确认要删除吗？",{btn: ["确定","取消"],title:"提示", icon:3,closeBtn:0},function(){
      $.post("${request.contextPath}/appManage/delApp",{"id":id},function(result){
        loadPage('${request.contextPath}/appManage/appList');
        layer.closeAll('dialog');
      });
    },function(){
      layer.closeAll('dialog');
    });
}
</script>