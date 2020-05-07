<#import "/fw/macro/webmacro.ftl" as w>
<div role="tabpanel" id="aa">
    <div class="filter filter-f16">
        <div class="filter-item">
            <span class="filter-name">应用名称：</span>
            <div class="filter-content">
                <select name="" id="serverList" class="form-control">
                    <#if serverList?exists && serverList?size gt 0>
                        <#list serverList as server>
                            <option value="${server.id!}" data-unittype="${server.unittype!}" data-desc="${server.description!}"  <#if serverId?exists && serverId==server.id >selected</#if>>${server.name }</option>
                        </#list>
                    </#if>    
                </select>
            </div>
        </div>
        <div class="filter-item">
            <span class="filter-name">授权状态：</span>
            <div class="filter-content">
                <select name="" id="statusSelect" class="form-control">
                    <option value="0" <#if authorizeStatus?exists && authorizeStatus==0 >selected</#if>>未授权</option>
                    <option value="1" <#if authorizeStatus?exists && authorizeStatus==1 >selected</#if>>已授权</option>
                </select>
            </div>
        </div>
        <div class="filter-item">
            <span class="filter-name">行政区划：</span>
            <div class="filter-content">
            <@w.region regionCode=regionCode regionName=regionName/>
            </div>
        </div>
        <div class="filter-item">
            <button class="btn btn-blue" id="searchBtn">查找</button>
        </div>
    </div>
    <div class="box box-graybg">
        <div class="box-graybg-title"><b>应用说明</b></div>
        <p id="serverDescription"></p>
        <#if unitAuthorizeList?exists && unitAuthorizeList?size gt 0>
          <p class="text-right">
              <button class="btn btn-blue" id="allAuthorize"><#if authorizeStatus==0>全部授权<#else>全部取消授权</#if></button>
          </p>
        </#if>
    </div>
    <table class="table table-striped table-hover no-margin">
        <thead>
            <tr>
                <th width="40">
                    <label class="inline">
                        <input class="wp" type="checkbox" id="selectAll">
                        <span class="lbl"></span>
                    </label>
                </th>
                <th>单位名称</th>
                <th>单位编号</th>
                <th>行政区划</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <#if unitAuthorizeList?exists && unitAuthorizeList?size gt 0>
                <#list unitAuthorizeList as unit>
                  <tr>
                      <td>
                          <label class="inline">
                              <input class="wp checkUnit" data-id="${unit.unitId! }" type="checkbox">
                              <span class="lbl"></span>
                          </label>
                      </td>
                      <td>${unit.unitName!}</td>
                      <td>${unit.unionCode!}</td>
                      <td>${unit.regionName!}</td>
                      <td>
                        <#if unit.authorizeStatus==0>未授权<#else>已授权</#if>
                      </td>
                      <td>
                          <a href="javascript:;" data-id="${unit.unitId! }" data-status="${unit.authorizeStatus}" class="table-btn color-blue authorize"><#if unit.authorizeStatus==0>授权<#else>取消授权</#if></a>
                      </td>
                  </tr>
                </#list>
            </#if>
        </tbody>
    </table>
    <div class="clearfix">
        <#if unitAuthorizeList?exists && unitAuthorizeList?size gt 0>
          <nav class="nav-toolBar pull-left">
              <button class="btn btn-blue" id="batchAuthorize"><#if authorizeStatus==0>批量授权<#else>批量取消授权</#if></button>
          </nav>
          <@w.pagination2  container="#TabContentDiv" pagination=pagination page_index=2/>
        </#if>
    </div>
</div>

<script>
  //初始化应用信息
  $("#serverDescription").text($("#serverList option:selected").data("desc"));
  
  //应用信息变更
  $("#serverList").on("change",function(){
    var description = $("#serverList option:selected").data("desc");
    $("#serverDescription").text(description);
  });

  //搜索
  $("#searchBtn").on('click',function(){
    var $selectedServer = $("#serverList option:selected");
    var serverId = $selectedServer.val();
    var unitType = $selectedServer.data("unittype");
    var authorizeStatus = $("#statusSelect option:selected").val();
    var region = $("#regionCode").val();
    var regionName = $("#regionName").val();
    if(regionName == ""){
      region = "";
      $("#regionCode").val("");
    }
    var url = "${request.contextPath}/system/serverAuthorize/server/serverAuthorizeList?serverId="+serverId+"&unitType="+unitType+"&authorizeStatus="+authorizeStatus+"&region="+region+"&regionName="+regionName;
    
    tabContentLoad(encodeURI(encodeURI(url)));
  });
  
  //全选
  $("#selectAll").on("click",function(){
    var is_checked = $(this).prop("checked"); 
    $('.checkUnit').prop("checked",is_checked); 
  });
  
  //单个授权
  $(".authorize").on("click",function(){
    var $this = $(this);
    var unitId = $this.data("id");
    var serverId = $("#serverList option:selected").val();
    var unitType = $("#serverList option:selected").data("unittype");
    var status = $this.data("status");
    var authorizeStatus = 0;    
    var region = $("#regionCode").val();
    var regionName = $("#regionName").val();
    if(regionName == ""){
      region = "";
      $("#regionCode").val("");
    }
    if(status == 0){//单位未授权，操作状态为授权操作
      authorizeStatus = 1;
    }
    $.ajax({
      url:"${request.contextPath}/system/serverAuthorize/server/"+serverId+"/batchAuthorize",
      data:{
        'unitIds':unitId,
        'authorizeStatus':authorizeStatus
      },
      dataType: "json",
      success:function(result){
        showMsgSuccess(result.msg,"操作成功!",function(index){
          layer.close(index);
          var url = "${request.contextPath}/system/serverAuthorize/server/serverAuthorizeList?serverId="+serverId+"&unitType="+unitType+"&authorizeStatus="+$("#statusSelect option:selected").val()+"&region="+region+"&regionName="+regionName;
          tabContentLoad(encodeURI(encodeURI(url)));
        });
      }
    });
    
  });
  
  //批量授权
  $("#batchAuthorize").on("click",function(){
    //按钮设为不可点击
    $("#batchAuthorize").attr("disabled","disabled");
    
    var unitIds = "";
    $check=$('.checkUnit:checkbox:checked');
    $check.each(function(i, e) {
        unitIds +=$(e).data("id")+",";
    });
    
    
    if (unitIds.length) {
      unitIds = unitIds.substring(0, unitIds.length-1);
      var $selectedServer = $("#serverList option:selected");
      var serverId = $selectedServer.val();
      var unitType = $selectedServer.data("unittype");
      var status = $("#statusSelect option:selected").val();
      var region = $("#regionCode").val();
      var regionName = $("#regionName").val();
      if(regionName == ""){
        region = "";
        $("#regionCode").val("");
      }
      var authorizeStatus = 0;    
      if(status == 0){//单位未授权，操作状态为授权操作
        authorizeStatus = 1;
      }
      
      $.ajax({
        url:"${request.contextPath}/system/serverAuthorize/server/"+serverId+"/batchAuthorize",
        data:{
          'unitIds':unitIds,
          'authorizeStatus':authorizeStatus
        },
        dataType: "json",
        success:function(result){
          showMsgSuccess(result.msg,"操作成功!",function(index){
            layer.close(index);
            var url = "${request.contextPath}/system/serverAuthorize/server/serverAuthorizeList?serverId="+serverId+"&unitType="+unitType+"&authorizeStatus="+status+"&region="+region+"&regionName="+regionName;
            tabContentLoad(encodeURI(encodeURI(url)));
          });
        }
      });
    }else{
      showMsgError("请选择单位","错误信息",function(index){
        layer.close(index);
      });
    }
  });
  
  //全部授权
  $("#allAuthorize").on("click",function(){
    //按钮设为不可点击
    $("#allAuthorize").attr("disabled","disabled");
    
    var $selectedServer = $("#serverList option:selected");
    var serverId = $selectedServer.val();
    var unitType = $selectedServer.data("unittype");
    var status = $("#statusSelect option:selected").val();
    var region = $("#regionCode").val();
    var regionName = $("#regionName").val();
    if(regionName == ""){
      region = "";
      $("#regionCode").val("");
    }
    var authorizeStatus = 0;    
    if(status == 0){//单位未授权，操作状态为授权操作
      authorizeStatus = 1;
    }
    
    $.ajax({
      url:"${request.contextPath}/system/serverAuthorize/server/"+serverId+"/allAuthorize",
      data:{
        'unitType':unitType,
        'authorizeStatus':authorizeStatus
      },
      dataType: "json",
      success:function(result){
        showMsgSuccess(result.msg,"操作成功!",function(index){
          layer.close(index);
          var url = "${request.contextPath}/system/serverAuthorize/server/serverAuthorizeList?serverId="+serverId+"&unitType="+unitType+"&authorizeStatus="+status+"&region="+region+"&regionName="+regionName;
          tabContentLoad(encodeURI(encodeURI(url)));
        });
      }
    });
    
  });
  
</script>
