<#import "/fw/macro/webmacro.ftl" as w>
<div role="tabpanel" class="tab-pane active" id="aa">
    <div class="filter filter-f16">
        <div class="filter-item">
            <span class="filter-name">职工姓名：</span>
            <div class="filter-content">
                <input type="text" class="form-control" id="realName" value="${realName! }"/>
            </div>
        </div>
        <div class="filter-item">
            <button class="btn btn-blue" id="searchBtn">查找</button>
        </div>
    </div>
    <table class="table table-striped table-hover no-margin">
        <thead>
            <tr>
                <th>职工姓名</th>
                <th>账号</th>
                <th>账号状态</th>
                <th>所属用户组数量</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <#if userPermList?exists && userPermList?size gt 0>
                <#list userPermList as user>
                    <tr>
                        <td>${user.realName! }</td>
                        <td>${user.username! }</td>
                        <td>正常</td>
                        <td>
                          <a href="javascript:;" class="table-btn color-blue js-showNum" style="position: relative;" data-rolestr="${user.roleStr }"><#if user.roleList?exists>${user.roleList?size!}<#else>0</#if></a>
                        </td>
                        <td><a href="javascript:;" class="table-btn color-blue js-authorization" data-id="${user.userId! }">授权</a></td>
                    </tr>
                </#list>
            </#if>
        </tbody>
    </table>
    <#if userPermList?exists && userPermList?size gt 0>
        <@w.pagination2  container="#TabContentDiv" pagination=pagination page_index=2/>
    </#if>
</div>

<div class="layer layer-authorization" id="userPermTree">
    <div class="authorization-title clearfix">
        <label class="inline">
            <input class="wp" id="selectAll" type="checkbox">
            <span class="lbl"> 全选</span>
        </label>
    </div>
    <div class="authorization-body">
        <ul id="tree" class="ztree" style=" height:400px; overflow:auto;"></ul>
    </div>
</div>


<script>
  //搜索
  $('#searchBtn').on('click',search);
  
  $("#realName").bind('keydown',function(event){  
    if(event.keyCode == "13")      
    {  
      search();
    }  
  });
  
  function search(){
    var realName = $("#realName").val();
    var url = "${request.contextPath}/system/userPerm/userList?realName="+realName;
    tabContentLoad(encodeURI(encodeURI(url)));
  }

  //所属用户组展示
  $('.js-showNum').on('click',function(){
      var roleNum = $(this).text();
      if(roleNum !="0"){
        var roleStr = $(this).data("rolestr");
        layer.tips(roleStr, $(this),{
            tips: [2,'#4c4c4c'],
            time: 2000, //20s后自动关闭
            maxWidth: 360
        });
      }
  });
  
  
  var zTree;
  var setting = {
      view: {
          dblClickExpand: false,
          showLine: true,
          selectedMulti: false
      },
      data: {
          simpleData: {
              enable:true,
              idKey: "id",
              pIdKey: "pId",
              rootPId: ""
          }
      },
      check: {
        autoCheckTrigger: true,
        chkboxType: { "Y": "ps", "N": "ps" },
        chkStyle:"checkbox",
        enable:true
      },
      callback: {
        onCheck: function(){
          //选中节点数
          var nodes = zTree.getCheckedNodes(true);
          $("#userPermTree .authorization-title button").removeClass("btn-grey").removeClass("btn-blue");
          if(nodes.length>0){
            $("#userPermTree .authorization-title button").addClass("btn-blue");
          }else if(nodes.length==0){
            $("#userPermTree .authorization-title button").addClass("btn-grey");
          }
        }
        
      }
  };
  
  //全选
  $("#selectAll").on("click", function(){
    zTree.checkAllNodes($(this).prop("checked"));
    $("#userPermTree .authorization-title button").removeClass("btn-grey").removeClass("btn-blue");
    if($(this).prop("checked")){
      $("#userPermTree .authorization-title button").addClass("btn-blue");
    }else{
      $("#userPermTree .authorization-title button").addClass("btn-grey");
    }
  })
  
  //授权
  $('.js-authorization').click(function(){
      var userId = $(this).data("id");
      
      //树初始化
      $.ajax({
          url:"${request.contextPath}/system/userPerm/userPermZtree",
          data:{
            'userId':userId
          },
          type:'post',
          dataType:'json',
          success:function(data){
              if(data.success){
                  $.fn.zTree.init($("#tree"), setting, JSON.parse(data.msg));
                  zTree = $.fn.zTree.getZTreeObj("tree");
                  
                  var nodes = zTree.getCheckedNodes(true);//选中节点
                  if(nodes.length>0){
                    for(var i=0;i<nodes.length;i++){
                     zTree.checkNode(nodes[i], true, true);
                    }
                  }
                  
                  //var dsblNodes = zTree.getNodesByParam("chkDisabled", true);//禁用节点
                  //if(dsblNodes.length>0){
                    //for(var i=0;i<dsblNodes.length;i++){
                      //zTree.checkNode(dsblNodes[i], true, true);
                    //}
                  //}
                  
                  //全选
                  $("#selectAll").prop("checked", false);
                  //清空按钮初始化
                  $("#userPermTree .authorization-title button").remove();
                  if(nodes.length>0){
                    $("#userPermTree .authorization-title").prepend('<button type="button" id="clearBtn" class="btn btn-blue pull-right">清空</button>');
                  }else if(nodes.length==0){
                    $("#userPermTree .authorization-title").prepend('<button type="button" id="clearBtn"  class="btn btn-grey pull-right">清空</button>');
                  }
                  //清空按钮点击事件
                  $("#clearBtn").on("click",function(){
                    if($("#clearBtn").hasClass("btn-grey")){
                      return false;
                    }else if($("#clearBtn").hasClass("btn-blue")){
                      $("#clearBtn").removeClass("btn-blue").addClass("btn-grey");
                      $("#selectAll").prop("checked", false);
                      zTree.checkAllNodes(false);
                    }
                  });
                  
                  //显示弹层
                  layer.open({
                    type: 1,
                    shade: .5,
                    title: ['授权','font-size:20px;'],
                    area: '500px',
                    btn: ['确定','取消'],
                    btnAlign: 'c',
                    content: $('#userPermTree'),
                    yes:function(){
                      saveUserPerm(userId);
                    },
                    btn2:function(){
                      layer.close();
                    }
                });
              }
          }
      });
      
  });
  
  function saveUserPerm(userId, layero){
    //所选模块
    var modelIds="";
    var nodes = zTree.getCheckedNodes(true);//选中节点
    if(nodes.length>0){
      for(var i=0;i<nodes.length;i++){
        var nodeObj = nodes[i];
        if(!nodeObj.isParent && nodeObj.getParentNode()){
          modelIds += nodeObj.id + ",";
        }
      }
      modelIds = modelIds.substring(0, modelIds.length-1);
    }
    
    var allModelIds="";
    var allNodes = zTree.transformToArray(zTree.getNodes());
    if(allNodes.length>0){
      for(var i=0;i<allNodes.length;i++){
        allModelIds += allNodes[i].id+",";
      }
      allModelIds = allModelIds.substring(0, allModelIds.length-1);
    }
    
    $.ajax({
      url:"${request.contextPath}/system/userPerm/"+userId+"/saveUserPerm",
      data:{
        'modelIds':modelIds,
        'allModelIds':allModelIds
      },
      type: "post",
      dataType: "json",
      success:function(data){
          if(data.success){
            layer.alert(data.msg,{},function(){
              layer.closeAll(); 
            });
          }
      }
    });
  }


</script>
