<div class="card-list clearfix report" id="cockpit-list">
    <#if libries?? && libries?size gt 0>
    <#list libries as diagram>
        <div class="card-item" id="${diagram.id!}">
            <div class="bd-view">
                <a href="javascript:void(0);">
                    <div class="drive  no-padding">
                        <img src="${request.contextPath}/static/bigdata/datav/images/chart-${diagram.diagramType}.png" alt="">
                        <div class="coverage">
                            <div class="vetically-center">
                                    <img src="${request.contextPath}/static/bigdata/images/remove.png"
                                         onclick="deleteLibrary('${diagram.id!}');" width="20" height="20"
                                         class="remove"/>
                            </div>
                        </div>
                    </div>
                    <div class="padding-side-20">
                        <h3 style="overflow: hidden;">${diagram.name!}</h3>
                    </div>
                </a>
            </div>
        </div>
    </#list>
    <#else >
        <p style="margin-left: 10px">
            列表空空，去新建吧～
        </p>
    </#if>
</div>
<script>

    function deleteLibrary(id) {
        layer.confirm("确认删除收藏？", {btn: ['确定', '取消'], title: '确认信息', icon: 3, closeBtn: 0}, function (index) {
            layer.close(index);
            $.ajax({
                url:  '${request.contextPath}/bigdata/diagramLibrary/delete/collect/' + id,
                type: 'get',
                success: function (res) {
                    if (res.success){
                        $('#' + id).remove();
                        layer.msg(res.message, {icon: 1, time: 500})
                    } else {
                        layer.msg(res.message|res.msg, {icon: 2, time: 2000});
                    }
                }
            })
        })

    }
    $(function () {
        var $dh = ($('.drive').first().width()/4*3).toFixed(0);
        $('.drive').each(function (index,ele){
            $(this).children('img').first().css({
                'height': $dh
            })
        });
        //分组图片
        var $sh = ($('.screen-team').first().width()/4*3).toFixed(0);
        $('.screen-team').each(function (index, ele) {
            $(this).css({
                'height': parseInt($sh) + 20
            });
            $(this).children('img').css({
                'height': $sh
            });
        });
    })
</script>
