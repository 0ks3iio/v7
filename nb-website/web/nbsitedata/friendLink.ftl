<!--友情链接-->

<div class="inner fn-clearfix">
    <div class="module-header">
        <h3 class="module-title">友情链接：</h3>
    </div>
    <div class="module-body">
        <div class="model">
            <#if friendLinks?? && friendLinks?size gt 0>
                <#list friendLinks as link>
                    <a class="nsy-link-a" href="javascript:;" onclick="openFriendLink('${link.titleLink!}')">${link.title!}</a>
                </#list>
            </#if>
        </div>
    </div>
</div>
<script>
    function openFriendLink(url){
        window.open(url);
    }
</script>