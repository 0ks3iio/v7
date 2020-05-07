<#import "/fw/macro/webmacro.ftl" as w>
<div role="tabpanel" id="aa">
    <div class="filter filter-f16">
        <input type="hidden" class="form-control" id="mcodeName" value="${mcodeName!}" style="width:160px;"/>
        <div class="filter-item">
            <button class="btn btn-blue" id="backBtn">返回</button>
        </div>
    </div>
    
    <table class="table table-outline">
        <thead>
            <tr>
                <th class="text-center" width="25%">排序编号</th>
                <th class="text-center" width="25%">代码</th>
                <th class="text-center" width="25%">代码名称</th>
                <th class="text-center" width="25%">状态</th>
                
            </tr>
        </thead>
        <tbody>
            <#if mcodeDetails?exists && mcodeDetails?size gt 0>
                <#list mcodeDetails as mcodeDetail>
                  <tr>
                      <td class="text-center">${mcodeDetail.displayOrder!}</td>
                      <td class="text-center">${mcodeDetail.thisId!}</td>
                      <td class="text-center">${mcodeDetail.mcodeContent!}</td>
                      <td class="text-center">
                        <label class="switch-label">
                            <input class="wp wp-switch checkUsing" type="checkbox" data-id="${mcodeDetail.id}" <#if mcodeDetail.isUsing = 1>checked="checked"</#if>>
                            <span class="lbl"></span>
                        </label>
                      </td>
                  </tr>
                </#list>
            </#if>
        </tbody>
    </table>
    <div class="clearfix">
        <#if mcodeDetails?exists && mcodeDetails?size gt 0>
        <@w.pagination2  container="#TabContentDiv" pagination=pagination page_index=2/>
        </#if>
    </div>
</div>

<script>
    $(function(){
        $("#backBtn").on("click",backUrl);
        $(".checkUsing").on("click",updateUsing);
    });
    
    function backUrl(){
        var mcodeName = $("#mcodeName").val();
        var url = "${request.contextPath}/system/mcode/mcodeLists";
        //$("#configModuleContentDiv").load(document.referrer);//返回上一页（无类型列表）
        //$("#moduleContentDiv").load(url,{"mcodeName":mcodeName});//返回上一页（有类型列表）
        tabContentLoad(url,{"mcodeName":mcodeName});
    }
    
    function updateUsing(){
        var id = $(this).data("id");
        var isUsing;
        if($(this).prop('checked')){
            isUsing = "1";
        }
        else{
            isUsing = "0";
        }
        $.ajax({
          url:"${request.contextPath}/system/mcode/modifyIsUsing",
          data:{
            'id':id,
            'isUsing':isUsing
          },
          dataType: "json",
          success:function(result){
            showMsgSuccess(result.msg,"操作成功!",function(index){
              layer.close(index);
            });
          }
        });
    }
</script>
