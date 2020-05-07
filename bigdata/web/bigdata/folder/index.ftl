<li><span><#if identity?default('user') == 'user'>可视化报表<#else>报表仓库</#if></span></li>
<#if folderTree?size gt 0>
    <#list folderTree as first>
                            <li class="" folder_id="${first.id!}">
                                <a href="#/bigdata/user/folderDetail${type!}/${first.id!}">
                                    <#if first.childFolder?size gt 0>
                                        <i class="iconfont icon-caret-down"></i>
                                    </#if>
                                    <span title="${first.folderName!}">${first.folderName!}</span>
                                </a>
                                <#if first.childFolder?size gt 0>
                                    <ul class="collapse" id="${identity!}${first.id!}" aria-expanded="false">
                                        <#list first.childFolder as second>
                                            <li class="" folder_id="${second.id!}">
                                                <a href="#/bigdata/user/folderDetail${type!}/${second.id!}">
                                                    <#if second.childFolder?size gt 0>
                                                        <i class="iconfont icon-caret-down"></i>
                                                    </#if>
                                                    <span title="${second.folderName!}">${second.folderName!}</span>
                                                </a>
                                                <#if second.childFolder?size gt 0>
                                                    <ul class="collapse" id="${identity!}${second.id!}">
                                                        <#list second.childFolder as third>
                                                            <li class="" folder_id="${third.id!}">
                                                                <a href="#/bigdata/user/folderDetail${type!}/${third.id!}">
                                                                    <span title="${third.folderName!}">${third.folderName!}</span>
                                                                </a>
                                                            </li>
                                                        </#list>
                                                    </ul>
                                                </#if>
                                            </li>
                                        </#list>
                                    </ul>
                                </#if>
                            </li>
    </#list>

<#else>
      <div class="no-data-common" id="folder-no-data">
          <div class="text-center">
              <img src="${request.contextPath}/bigdata/v3/static/images/folder-settings/img-directory.png"/>
              <p style="color: #999">暂无目录</p>
          </div>
      </div>
</#if>
<script>
    <#if folderTree?size gt 0>
        <#list folderTree as first>
            router.add({
                path: '/bigdata/user/folderDetail${type!}/${first.id}',
                name: '${first.folderName}',
                level: 1
            }, function () {
                $('.dynamic-sidebar').find('a').removeClass('active');
                var $selectLi = $('.dynamic-sidebar').find('li[folder_id="${first.id}"]');
                $selectLi.find('a:first').addClass('active');
                if ($selectLi.find('i:first').hasClass('icon-caret-down')) {
                    $selectLi.find('i:first').removeClass('icon-caret-down').addClass('icon-caret-up');
                } else {
                    $selectLi.find('i:first').removeClass('icon-caret-up').addClass('icon-caret-down');
                }
                $('#' + '${identity!}${first.id}').slideToggle();
                $('.page-content').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
                $('.page-content').load('${springMacroRequestContext.contextPath}/bigdata/user/folderDetail${type!}/${first.id}');
            });
            <#if first.childFolder?size gt 0>
                <#list first.childFolder as second>
                    router.add({
                        path: '/bigdata/user/folderDetail${type!}/${second.id}',
                        name: '${second.folderName}',
                        level: 1
                    }, function () {
                        $('.dynamic-sidebar').find('a').removeClass('active');
                        var $selectLi = $('.dynamic-sidebar').find('li[folder_id="${second.id}"]');
                        $selectLi.find('a:first').addClass('active');
                        if ($selectLi.find('i:first').hasClass('icon-caret-down')) {
                            $selectLi.find('i:first').removeClass('icon-caret-down').addClass('icon-caret-up');
                        } else {
                            $selectLi.find('i:first').removeClass('icon-caret-up').addClass('icon-caret-down');
                        }
                        $('#' + '${identity!}${second.id}').slideToggle();
                        $('.page-content').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
                        $('.page-content').load('${springMacroRequestContext.contextPath}/bigdata/user/folderDetail${type!}/${second.id}')
                    });
                    <#if second.childFolder?size gt 0>
                        <#list second.childFolder as third>
                            router.add({
                                path: '/bigdata/user/folderDetail${type!}/${third.id}',
                                name: '${third.folderName}',
                                level: 1
                            }, function () {
                                $('.dynamic-sidebar').find('a').removeClass('active');
                                var $selectLi = $('.dynamic-sidebar').find('li[folder_id="${third.id}"]');
                                $selectLi.find('a:first').addClass('active');
                                $('.page-content').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
                                $('.page-content').load('${springMacroRequestContext.contextPath}/bigdata/user/folderDetail${type!}/${third.id}')
                            });
                        </#list>
                    </#if>
                </#list>
            </#if>
        </#list>
    </#if>
</script>