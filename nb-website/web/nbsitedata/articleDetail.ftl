<div class="breadcrumb">
    <div class="model">当前位置：${navigator!}</div>
</div>
<div class="module news">
    <div class="module-body">
        <!--S 文章内容 -->
        <div class="article">
            <div class="article-title model">
                <#if article.titleLink?default('')!=''>
                    <a href="javascript:;" onclick="javascript:window.open('${article.titleLink!}');">${article.title!}</a>
                <#else >
                    ${article.title!}
                </#if>
            </div>
            <p class="article-meta model">来源：${article.articleSource!}  |  发布日期：${(article.releaseTime?string('yyyy-MM-dd'))?if_exists}</p>
            <div class="article-content">
                <div class="model">
                    <td>${article.content!}</td>
                </div>
            </div>
            <div class="articleUpDown">
                <div class="model"></div>
            </div>
        </div><!--E 文章内容 -->
    </div>
</div>