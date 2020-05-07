<ul class="dropdown-made">
    <#list modelParams as item>
    <li>
        <a href="javacript:void(0);" id="${item.id!}">
            ${item.name!}
        </a>
        <#if item.isFilter?default(0) == 0>
            <img class="pos-right-c" style="display:block;margin-right: 20px;" src="${request.contextPath}/static/bigdata/images/show-icon.png" onclick="changeIsFilter(this)">
        <#else>
            <img class="pos-right-c" style="display:block;margin-right: 20px;" src="${request.contextPath}/static/bigdata/images/hide-icon.png" onclick="changeIsFilter(this)">
        </#if>
    </li>
    </#list>
</ul>
<script>
    function changeIsFilter(e) {
        var status = 1;
        var id = $(e).prev().attr('id');
        if($(e).attr('src').indexOf('show-icon') == -1){
            status = 0;
        }

        $.ajax({
            url: '${request.contextPath}/bigdata/model/showOrHideDataModelParam',
            type: 'POST',
            data: {
                id: id,
                status:status
            },
            dataType: 'json',
            success: function (val) {
                if (!val.success) {
                    layer.msg(val.message, {icon: 2});
                }
                else {
                    if($(e).attr('src').indexOf('show-icon') == -1){
                        $(e).attr('src',showIcon);
                        $(e).closest('.catalog').children('span').removeClass('active')
                        status = 0;
                    }else{
                        $(e).attr('src',hideIcon);
                        $(e).closest('.catalog').children('span').addClass('active')
                    }
                }
            }
        });
    }
</script>