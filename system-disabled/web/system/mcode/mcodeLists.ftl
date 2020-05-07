<#import "/fw/macro/webmacro.ftl" as w>
<div role="tabpanel" id="aa">
    <div class="filter filter-f16">
        <div class="filter-item">
            <span class="filter-name">微代码类型：</span>
            <div class="filter-content">
                <input type="text" class="form-control" placeholder="输入关键字" id="mcodeName" value="${mcodeName!}" style="width:160px;"/>
            </div>
        </div>
        <div class="filter-item">
            <button class="btn btn-blue" id="searchBtn">查找</button>
        </div>
    </div>
    
    <table class="table table-outline">
        <thead>
            <tr>
                <th class="text-center" width="30%">类型名称</th>
                <th class="text-center" width="30%">类型ID</th>
                <th class="text-center" width="30%">类型描述</th>
                <th class="text-center" width="30%">操作</th>
            </tr>
        </thead>
        <tbody>
            <#if mcodeLists?exists && mcodeLists?size gt 0>
                <#list mcodeLists as mcodeList>
                  <tr>
                      <td class="text-center">${mcodeList.mcodeName!}</td>
                      <td class="text-center">${mcodeList.mcodeId!}</td>
                      <td class="text-center">${mcodeList.mcodeRemark!}</td>
                      <td class="text-center"><a class="btn mcodeDetailBtn" data-mid="${mcodeList.mcodeId}">微代码详情列表</a>
                      </td>
                  </tr>
                </#list>
            </#if>
        </tbody>
    </table>
    <div class="clearfix">
        <#if mcodeLists?exists && mcodeLists?size gt 0>
        <@w.pagination2  container="#TabContentDiv" pagination=pagination page_index=2/>
        </#if>
    </div>
</div>

<script>
    $(function(){
        $("#searchBtn").on("click",findMcodeLists);
        $(".mcodeDetailBtn").on("click",findMcodeDetals);
    });
    
    function findMcodeLists(){
        var mcodeName = $("#mcodeName").val();
        var url = "${request.contextPath}/system/mcode/mcodeLists";
        tabContentLoad(url,{"mcodeName":mcodeName});
        //$("#moduleContentDiv").load(url,{"mcodeName":mcodeName});
    }
    
    function findMcodeDetals(){
        var mcodeId = $(this).data("mid");
        var mcodeName = $("#mcodeName").val();
        var url = "${request.contextPath}/system/mcode/mcodeDetails";
        tabContentLoad(url,{"mcodeId":mcodeId,"mcodeName":mcodeName});
        //$("#moduleContentDiv").load(url,{"mcodeId":mcodeId,"mcodeName":mcodeName});
    }
</script>
