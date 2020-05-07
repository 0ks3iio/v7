<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
        <meta charset="utf-8" />
        <title>开放平台</title>
        <#include "/openapi/homepage/head.ftl">
    </head>

    <body>
        <#include "/openapi/homepage/navigationBar.ftl">
        
        <div class="banner">
            <div class="container">
                <p class="join-in"><a href="${request.contextPath}/developer/regist" class="">立即加入</a></p>
            </div>
        </div>
        
        <div class="content">
            <div class="container">
                <div class="row">
                    <div class="col-sm-4">
                        <div class="feature-item">
                            <div class="feature-item-img">
                                <img src="${resourceUrl}/openapi/images/base/feature-1.png" alt="" />
                            </div>
                            <div class="feature-item-title">快速接入</div>
                            <div class="feature-item-des">可视化调试数据对接及单点登录对接</div>
                        </div>
                    </div>
                    <div class="col-sm-4">
                        <div class="feature-item">
                            <div class="feature-item-img">
                                <img src="${resourceUrl}/openapi/images/base/feature-2.png" alt="" />
                            </div>
                            <div class="feature-item-title">安全分享</div>
                            <div class="feature-item-des">监控数据读取，保障数据安全</div>
                        </div>
                    </div>
                    <div class="col-sm-4">
                        <div class="feature-item">
                            <div class="feature-item-img">
                                <img src="${resourceUrl}/openapi/images/base/feature-3.png" alt="" />
                            </div>
                            <div class="feature-item-title">易于集成</div>
                            <div class="feature-item-des">支持各类使用角色，覆盖各种应用<br />订阅及授权方式</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="content-gray">
            <div class="base-flow clearfix">
                <div class="base-flow-header">开发接入流程</div>
                <div class="base-flow-body">
                    <dl class="base-flow-item base-flow-item1 " style="cursor:pointer" onclick="window.open('${request.contextPath}/developer/regist','_blank')">
                        <dt>开发者注册</dt>
                        <dd>申请成为开发者，维护企业信息完善联系方式，同意平台服务协议</dd>
                    </dl>
                    <dl class="base-flow-item base-flow-item2" style="cursor:pointer" onclick="window.open('${request.contextPath}/developer/devDoc','_blank')">
                        <dt>了解开发指南</dt>
                        <dd>前往查看开发文档，了解平台接入开发细节内容</dd>
                    </dl>
                    <dl class="base-flow-item base-flow-item3" style="cursor:pointer" onclick="manageCenter(1)">
                        <dt>创建应用</dt>
                        <dd>维护应用信息，提交审核</dd>
                    </dl>
                    <dl class="base-flow-item base-flow-item4" style="cursor:pointer" onclick="window.open('${request.contextPath}/developer/devDoc#data','_blank')">
                        <dt>对接数据</dt>
                        <dd>申请基础数据接口，调试读取基本数据</dd>
                    </dl>
                    <dl class="base-flow-item base-flow-item5" style="cursor:default">
                        <dt>应用上线</dt>
                        <dd>调试测试完毕后联系应用上线</dd>
                    </dl>
                </div>
            </div>
        </div>
        
        <div class="content-gray">
            <div class="base-flow clearfix">
                <div class="base-flow-body" id = "showAllCount">
                
                </div>
            </div>
        </div>
        <#include "/openapi/homepage/foot.ftl">
    </body>
</html>

<script src="${resourceUrl}/components/echarts/echarts.min.js"></script>
<script>

  $(function(){
    <#if islogin = 1>
      $("#showAllCount").load("${request.contextPath}/developer/interface/count/showOpenapiCount/page");
    </#if>
  })



</script>