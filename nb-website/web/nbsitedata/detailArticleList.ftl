<!--普通模块文章列表-->
<div class="model" style="height:640px;overflow:hidden;overflow-y:scroll;">
    <ul class="nsy-news-ul">
        <#if articles?exists && articles?size gt 0>
            <#list articles as article>
                <li class="nsy-news-li">
                    <img class="nsy-newsFlag" src="${request.contextPath}/nbsitedata/images/list-icon02.png" alt=""/>
                    <#--<span class="nsy-news-span">小学语文 |  </span>-->
                    <a class="nsy-newsTitle" href="javascript:;" onclick="doArticleDetail('${article.id!}');">${article.title!}</a>
                    <p class="nsy-news-meta"><span class="nsy-newsTime">${(article.releaseTime?string('MM-dd'))?if_exists}</span></p>
                </li>
            </#list>
        </#if>
    </ul>
</div>