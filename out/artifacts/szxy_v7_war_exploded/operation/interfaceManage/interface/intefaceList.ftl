<div class="table-container-body">
  <table class="table table-striped table-hover no-margin">
      <thead>
          <tr>
              <th>接口名称</th>
              <th>结果类型</th>
              <th>调用类型</th>
              <th>接口地址</th>
              <th>接口方法</th>
              <th>状态</th>
              <th>操作</th>
          </tr>
      </thead>
      <tbody>
          <#if openInterfaceDtos?exists &&(openInterfaceDtos?size gt 0)>
              <#list openInterfaceDtos as interface>
                  <tr data-interid="${interface.id!}">
                      <td>${interface.interfaceName!}</td>
                      <td>${interface.resultType!}</td>
                      <td>${interface.dataTypeName!}</td>
                      <td>${interface.urlString!}</td>
                      <td>${interface.methodType!}</td>
                      <td>${interface.state!}</td>
                      <td>
                         <a href="javascript:;" class="table-btn color-lightblue modify">修改</a>
                         <a href="javascript:;" class="table-btn color-red delete">删除</a>
                         <#if interface.isUsing == 0>
                           <a href="javascript:;" class="table-btn color-green start">启用</a>
                         <#else>
                           <a href="javascript:;" class="table-btn color-red stop">停用</a>
                         </#if>
                         <a href="javascript:;" class="table-btn color-lightblue modifyDetails">修改详情</a>
                      </td>
                  </tr>
              </#list>
          </#if>
      </tbody>
  </table>
</div>
<script type="text/javascript">
$(function(){
  $('.delete').on('click',deleteInte);//删除接口
  $('.stop').on('click',stopInte);//停用应用
  $('.start').on('click',startInte);//启用应用
  $('.modify').on('click',modifyApp);//修改应用
  $('.modifyDetails').on('click',modifyDetails);//修改应用
  $('#interfaceName').keyup(function(event) {
    if (event.keyCode == 13) {
      $('#findInterface').trigger("click");
    }
  });
});
</script>