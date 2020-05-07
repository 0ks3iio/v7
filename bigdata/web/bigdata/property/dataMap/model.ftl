<div class="bottom-explain-part">
    <p class="mb-15">${remark!'暂无描述！'}</p>
</div>
<div class="bottom-explain-detail scrollBar4">
    <b>指标：</b>
    <div class="my-15">
        <#if modelIndex?exists && modelIndex?size gt 0>
            <#list  modelIndex as index>
                <span class="badge badge-sm">${index.name!}</span>
            </#list>
        </#if>
    </div>
    <b>维度：</b>
    <div class="my-15">
        <#if modelDimension?exists && modelDimension?size gt 0>
            <#list  modelDimension as dimension>
                <span class="badge badge-sm">${dimension.name!}</span>
            </#list>
        </#if>
    </div>
</div>