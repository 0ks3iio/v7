<#import "/fw/macro/webmacro.ftl" as w>
<div role="tabpanel" >
    <div class="filter filter-f16">
      <div class="filter-item">
            <span class="filter-name">行政区划：</span>
            <div class="filter-content">
            <@w.region regionCode=regionCode regionName=regionName/>
            </div>
        </div>
        
        <div class="filter-item">
            <span class="filter-name">单位名称：</span>
            <div class="filter-content">
                <input type="text" class="form-control" id="unitName" value="${unitName }"/>
            </div>
        </div>
        <div class="filter-item">
            <button class="btn btn-blue" id="searchBtn">查找</button>
        </div>
    </div>
    <table class="table table-striped table-hover no-margin">
      <thead>
          <tr>
              <th>单位名称</th>
              <th>单位编号</th>
              <th>行政区划</th>
              <th>应用数量</th>
              <th>操作</th>
          </tr>
      </thead>
      <tbody>
            <#if unitAuthorizeList?exists && unitAuthorizeList?size gt 0>
                <#list unitAuthorizeList as unit>
                  <tr>
                      <td>${unit.unitName!}</td>
                      <td>${unit.unionCode!}</td>
                      <td>${unit.regionName!}</td>
                      <td>
                        <a href="javascript:;" class="table-btn color-blue js-showNum" data-serverstr="${unit.serverStr }"><#if unit.authorizeServerList?exists>${unit.authorizeServerList?size!}<#else>0</#if></a>
                      </td>
                      <td>
                          <a href="javascript:;" class="table-btn color-blue js-modify" data-id="${unit.unitId}" data-type="${unit.unitClass}">修改</a>
                      </td>
                  </tr>
                </#list>
            </#if>
        </tbody>
    </table>
    <#if unitAuthorizeList?exists && unitAuthorizeList?size gt 0>
        <@w.pagination2  container="#TabContentDiv" pagination=pagination page_index=2/>
    </#if>
</div>
<!-- 单位授权修改弹层 -->
<div class="layer layer-modify">
    <div class="filter">
        <div class="filter-item block">
            <span class="filter-name">应用名称：</span>
            <div class="filter-content" id="serverListDiv">
            </div>
        </div>
    </div>
</div>

              
<script>
  $("#unitName").bind('keydown',function(event){  
    if(event.keyCode == "13")      
    {  
      search();
    }  
  });  

  //搜索
  $('#searchBtn').on('click',search);
  
  function search(){
    var unitName = $("#unitName").val();
    var region = $("#regionCode").val();
    var regionName = $("#regionName").val();
    if(regionName == ""){
      region = "";
      $("#regionCode").val("");
    }
    var url = "${request.contextPath}/system/serverAuthorize/unitAuthorize/unitList?unitName="+unitName+"&region="+region+"&regionName="+regionName;
    tabContentLoad(encodeURI(encodeURI(url)));
  }

  //已授权应用展示
  $('.js-showNum').on('click',function(){
      var serverNum = $(this).text();
      if(serverNum !="0"){
        var serverStr = $(this).data("serverstr");
        layer.tips(serverStr, $(this),{
            tips: [2,'#4c4c4c'],
            time: 2000, //20s后自动关闭
            maxWidth: 360
        });
      }
  });

  //修改单位授权
  $('.js-modify').click(function(){
      var $this = $(this);
      var unitId = $this.data("id");
      var unitClass = $this.data("type");
      
      $.ajax({
        url:"${request.contextPath}/system/serverAuthorize/unitAuthorize/modifyUnitAuthorize?unitId="+unitId+"&unitClass="+unitClass,
        type:'post', 
        cache:false,  
        contentType: "application/json",
        success:function(data) {
          var dataJson = JSON.parse(data);
          
          //拼装授权应用列表
          $("#serverListDiv").empty();
          if(dataJson.serverList && dataJson.serverList.length>0){
            for(var i=0;i<dataJson.serverList.length;i++){
              var serverObj = dataJson.serverList[i];
              var serverHtml = '<label class="inline">';
              if(serverObj.hasAuthorized == 1){//已授权
                serverHtml += '<input class="wp" type="checkbox" data-id="'+serverObj.id+'" checked="checked">';
              }else{
                serverHtml += '<input class="wp" type="checkbox" data-id="'+serverObj.id+'">';
              }
              serverHtml += '<span class="lbl">'+serverObj.name+'</span></lable>';
              $("#serverListDiv").append(serverHtml);
            }
          }
          
          //显示弹层
          layer.open({
            type: 1,
            shade: .5,
            title: ['修改','font-size:20px;'],
            area: '500px',
            btn: ['确定','取消'],
            btnAlign: 'c',
            content: $('.layer-modify'),
            yes:function(index, layero){//保存授权应用
              var serverIds = "";
              var serverNum = 0;
              var serverStr = "";
            
              $checkServer=$('#serverListDiv .wp:checkbox:checked');
              if($checkServer.length>0){
                serverNum = $checkServer.length
                $checkServer.each(function(i, e) {
                  serverIds += $(e).data("id");
                  serverStr += $(e).next().text();
                  if(i<$checkServer.length-1){
                    serverIds += ",";
                    serverStr += "</br>";
                  }
                });
              }
              
              $.ajax({
                url:"${request.contextPath}/system/serverAuthorize/unitAuthorize/"+unitId+"/saveUnitAuthorize",
                data:{
                  'serverIds':serverIds
                },
                dataType: "json",
                success:function(result){
                  var $serverNumTd = $this.parents("tr:first").find("td:eq(3)");
                  $serverNumTd.find("a").text(serverNum);
                  $serverNumTd.find("a").data("serverstr", serverStr);
                  
                  showMsgSuccess(result.msg,"操作成功!",function(index2){
                    layer.close(index);
                    layer.close(index2);
                  });
                }
              });
              
            }
          });
          $('.layer-modify').parent().css('overflow','auto');
        }
    });
      
      
  });
</script>              

