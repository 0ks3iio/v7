<div role="tabpanel">
    <p class="text-right"><button class="btn btn-blue" id="addRoleBtn">新增用户组</button></p>
    <table class="table table-striped table-hover no-margin">
        <thead>
            <tr>
                <th>用户组名称</th>
                <th>成员数量</th>
                <th>更新时间</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <#if roleList?exists && roleList?size gt 0>
                <#list roleList as role>
                  <tr>
                      <td>${role.name! }</td>
                      <td><a href="javascript:;" class="table-btn color-blue js-showNum" style="position: relative;" data-userstr="${role.userStr! }"><#if role.userList?exists>${role.userList?size!}<#else>0</#if></a></td>
                      <td>${role.modifyTimeStr! }</td>
                      <td><a href="javascript:;" class="table-btn color-blue js-authorization" data-id="${role.roleId! }">授权</a></td>
                  </tr>
                </#list>
            </#if>    
        </tbody>
    </table>
</div>


<script>
  //用户组成员展示
  $('.js-showNum').on('click',function(){
      var userNum = $(this).text();
      if(userNum !="0"){
        var userStr = $(this).data("userstr");
        layer.tips(userStr, $(this),{
            tips: [2,'#4c4c4c'],
            time: 2000, //20s后自动关闭
            maxWidth: 360
        });
      }
  });
  
  $(".js-authorization").on("click",function(){
    var roleId = $(this).data("id");
    moduleContentLoad("${request.contextPath}/system/role/modifyRole?roleId="+roleId);
  });
  
  //新增用户组按钮
  $("#addRoleBtn").on("click",function(){
    moduleContentLoad("${request.contextPath}/system/role/addRole");
  });

</script>
