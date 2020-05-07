<!-- 大屏列表 -->
<div class="box data-page">
    <div class="box-body no-padding-side" id="screen-list">
        <div class="card-list clearfix report" id="cockpit-list">
        <#if ScreenDemos?exists && ScreenDemos?size gt 0>
            <#list ScreenDemos as ScreenDemo>
            <div class="card-item" id="${ScreenDemo.id!}">
                <div class="bd-view">

                    <a href="javascript:void(0);">
                        <div class="drive  no-padding">
                            <img src="${request.contextPath}${ScreenDemo.thumbnail!}" alt="">
                            <div class="coverage">
                                <div class="vetically-center">
                                    <button class="btn btn-blue btn-long" type="button"
                                            onclick="viewCockpit('${ScreenDemo.url!}');">
                                        查看
                                    </button>
                                </div>
                            </div>
                        </div>
                        <div class="padding-side-20">
                            <h3 style="overflow: hidden;">${ScreenDemo.name!}</h3>
                        </div>
                    </a>
                </div>
            </div>
            </#list>
        <#else>
            <div class="no-data-common">
                <div class="text-center">
                    <img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
                    <p class="color-999">
                        暂无经典案例
                    </p>
                </div>
            </div>
        </#if>
        </div>
    </div>
</div>
<script>

    function viewCockpit(url) {
        window.open('${request.contextPath}' + url);
    }

</script>