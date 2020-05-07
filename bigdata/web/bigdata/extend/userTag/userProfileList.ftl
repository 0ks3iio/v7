<#list userProfileList as item>
        <div class="box box-default shadow float-left margin-r-20 pick-id">
            <div class="box-body">
                <div class="fix-width">
                    <img src="${request.contextPath}/static/bigdata/images/${item.code!}.png"
                         onclick="userTagManage('${item.id!}')" alt="" width="243" height="332">
                </div>
            </div>
        </div>
</#list>
<script type="text/javascript">
    function userTagManage(id) {
        var href = '/bigdata/userProfile/userTagManage?profileId=' + id;
        routerProxy.go({
            path: href,
            level: 2,
            name: '标签管理'
        }, function () {
            $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
        });
    }
</script>





















