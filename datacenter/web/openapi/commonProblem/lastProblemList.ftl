<div class="faq-content-header">
    <span class="faq-content-header-title">最新问题</span>
</div>
<div class="faq-content-body">
    <ul class="faq-content-list">
        <#if byTimeList??>
            <#list byTimeList as timeProblem>
                <li><a href="#" onclick="rightContentLoad('${request.contextPath}/problem/problemDetail?id=${timeProblem.id!}')">${timeProblem.question}</a></li>
            </#list>
        </#if>
    </ul>
</div>
<div class="faq-content-header">
    <span class="faq-content-header-title">最热问题</span>
</div>
<div class="faq-content-body">
    <ul class="faq-content-list">
        <#if byViewNumList??>
            <#list byViewNumList as viewNumProblem>
                <li><a href="#" onclick="rightContentLoad('${request.contextPath}/problem/problemDetail?id=${viewNumProblem.id!}')">${viewNumProblem.question}</a></li>
            </#list>
        </#if>
    </ul>
</div>