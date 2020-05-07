<#if folderDetails?exists && folderDetails?size gt 0>
    <ul class="folder-box-wrap auto clearfix js-result">
        <#list folderDetails as item>
            <li onclick="view('${item.id!}')" style="cursor: pointer"
                id="${item.id!}" businessId="${item.businessId!}"
                businessName="${item.businessName!}"
                businessType="${item.businessType!}"><img
                    class="pos-left"
                    src="${request.contextPath}/bigdata/v3/static/images/my-view/${imgMap[item.businessType]}"/><span
                    title="${item.businessName!}">${item.businessName!}</span>
            </li>
        </#list>
    </ul>
<#else>
    <div class="no-data-common">
        <div class="text-center">
            <img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-search-100.png"/>
            <p class="color-999">暂无您要的数据</p>
        </div>
    </div>
</#if>
