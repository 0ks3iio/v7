<div class="faq-content-body">
    <#if pList ?size gt 0>
    <ul class="faq-search-list">
        <#list pList as p>
        <li class="faq-search-list-item">
            <div class="faq-search-list-tt">${p.question!}</div>
            <div class="faq-search-list-dd">${p.answer!}</div>
        </li>
        </#list>
    </ul>
    <#else>
        <div class="faq-search-noData">没有搜索到相关问题，请尝试搜索其他关键词</div>
    </#if>
</div>