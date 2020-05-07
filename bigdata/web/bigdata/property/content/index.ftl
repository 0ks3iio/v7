<div class="content-nav">
    <ul>
        <ul>
            <li>
                <span>内容资产</span>
            </li>
            <#if folderTree?size gt 0>
                <#list folderTree as first>
                    <li class="" folder_id="${first.id!}">
                        <a href="javascript:void(0);">
                            <#if first.childFolder?size gt 0>
                                <span class="multilevel"><i class="iconfont icon-caret-down"></i></span>
                            </#if>
                            <span title="${first.folderName!}">${first.folderName!}</span>
                        </a>
                        <#if first.childFolder?size gt 0>
                            <ul class="collapse" id="${first.id!}">
                                <#list first.childFolder as second>
                                    <li class="" folder_id="${second.id!}">
                                        <a href="javascript:void(0);">
                                            <#if second.childFolder?size gt 0>
                                                <span class="multilevel"><i
                                                            class="iconfont icon-caret-down"></i></span>
                                            </#if>
                                            <span title="${second.folderName!}">${second.folderName!}</span>
                                        </a>
                                        <#if second.childFolder?size gt 0>
                                            <ul class="collapse" id="${second.id!}">
                                                <#list second.childFolder as third>
                                                    <li class="" folder_id="${third.id!}">
                                                        <a href="javascript:void(0);">
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
                <div class="no-data">
                    <img src="${request.contextPath}/bigdata/v3/static/images/folder-settings/img-directory.png"/>
                    <div>暂无目录，请添加</div>
                </div>
            </#if>
        <li>
    </ul>
</div>
<div class="content-nav-sibling scrollBar4" id="contentDiv">
    <div class="no-data">
        <img src="${request.contextPath}/bigdata/v3/static/images/folder-settings/img-folder.png"/>
        <div>请选择目录</div>
    </div>
</div>
<script>
    $(function () {
        //目录
        $('.content-nav').on('click', 'a', function (e) {
            e.stopPropagation();
            if ($(this).find('i:first').hasClass('icon-caret-down')) {
                $(this).find('i:first').removeClass('icon-caret-down').addClass('icon-caret-up');
            } else {
                $(this).find('i:first').removeClass('icon-caret-up').addClass('icon-caret-down');
            }
            $('.content-nav').find('a').removeClass('active');
            $(this).addClass('active');
            var folderId = $(this).parent().attr('folder_id');
            $('#' + folderId).slideToggle();

            $('#contentDiv').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
            $('#contentDiv').load('${springMacroRequestContext.contextPath}/bigdata/user/folderDetail/all/' + folderId);
        });
    });
</script>
