<link rel="stylesheet" href="${request.contextPath}/bigdata/v3/static/fonts/iconfont.css">
<ul class="tabs tabs-basics bi-tabs" tabs-group="g1">
    <li class="tab active"><a href="javascript:void(0);" id="all_screen">全部大屏</a></li>
    <li class="tab"><a href="javascript:void(0);" id="group_screen">分组大屏</a></li>
</ul>
<div class="tabs-panel active" id="all">
    <div class="row-made">
        <#if cockpits?exists && cockpits?size gt 0>
            <#list cockpits as cockpit>
                <div class="col-made-6">
                    <div class="screen-box">
                        <div class="box-img clearfix">
                            <img src="${fileUrl!}/bigdata/datav/${cockpit.unitId!}/${cockpit.id!}.jpg?${.now}">
                            <div class="cover-box" data-id="${cockpit.id!}">
                                <div class="btn-look-over" <#if cockpit.url?exists>onclick="openSelfCockpit('${cockpit.url!}');"<#else>onclick="openCockpit('${cockpit.id!}', '${cockpit.name!}');"</#if>>
                                    <span>查看</span>
                                </div>
                            </div>
                        </div>
                        <div class="box-text">
                            <span>${cockpit.name!}</span>
                        </div>
                    </div>
                </div>
            </#list>
        </#if>
    </div>
</div>
<div class="tabs-panel" id="group">


</div>



<script>
    function openCockpit(id, name) {
        window.open('${request.contextPath}/bigdata/datav/screen/view/' + id, name);
    }
    function openSelfCockpit(url) {
        window.open(url);
    }

    $(function () {
        $('#all_screen').on('click', function () {
            show('all');
            $(this).parent().addClass('active').siblings('li.active').removeClass('active');
        });
        $('#group_screen').on('click', function () {
            show('group');
            $(this).parent().addClass('active').siblings('li.active').removeClass('active')
        });

        function show(id) {
            $('#' + id).addClass('active').siblings('div').removeClass('active');
        }
        cardH();
        $('#group').load('${request.contextPath}/bigdata/cockpitQuery/groupIndex', function () {
            cardH();
        });
        $(window).resize(cardH);
    })

    function cardH() {
        var $t = $('.tabs-panel').eq($('.tab.active').index()).find('.box-img')
        var h = parseInt($t.width() * 9 / 16);
        $('.box-img').each(function (index, ele) {
            $(this).height(h)
        });
        $('.add-group').height(h + 40);
    }

</script>