<!-- 大屏列表 -->
<div class="main-content right-coming">
    <div class="row-made">
        <#if ScreenDemos?exists && ScreenDemos?size gt 0>
            <#list ScreenDemos as ScreenDemo>
        <div class="col-made-6" onclick="viewCockpit('${ScreenDemo.url!}');">
            <div class="screen-box">
                <div class="box-img clearfix">
                    <img src="${request.contextPath}${ScreenDemo.thumbnail!}" >
                </div>
                <div class="box-text">
                    <span>${ScreenDemo.name!}</span>
                </div>
            </div>
        </div>
            </#list>
        </#if>
    </div>

</div>
<script>

    $(function () {
         <#if ScreenDemos?exists && ScreenDemos?size gt 0>
            $("#businessType-99-num").html('${ScreenDemos?size}');
         </#if>
    });

    function viewCockpit(url) {
        window.open('${request.contextPath}' + url);
    }

</script>