<div class="box">
    <div class="box-header clearfix">
        <span class="fake-btn fake-btn-blue no-radius-right no-border-right" id="all_screen">全部大屏</span>
        <span class="fake-btn fake-btn-default no-radius-left no-border-left" id="group_screen">大屏分组</span>
    </div>
    <div class="box-body padding-top-20">
        <!-- 大屏列表 -->
        <div class="card-list clearfix report" id="all">
            <#if cockpits?exists && cockpits?size gt 0>
                <#list cockpits as cockpit>
                    <div class="card-item">
                        <div class="bd-view">
                            <a href="javascript:void(0);" id="${cockpit.id!}">
                                <div class="drive  no-padding">
                                    <img src="${fileUrl!}/bigdata/datav/${cockpit.unitId!}/${cockpit.id!}.jpg?${.now}"
                                         alt="">
                                    <div class="coverage">
                                        <div class="vetically-center">
                                            <button class="btn btn-blue btn-long" type="button"
                                                    <#if cockpit.url?exists>
                                                    onclick="openSelfCockpit('${cockpit.url!}');"
                                                    <#else>
                                                    onclick="openCockpit('${cockpit.id!}', '${cockpit.name!}');"
                                                    </#if>
                                                >
                                                查看
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                <div class="padding-side-20">
                                    <h3 style="overflow: hidden;">${cockpit.name!}</h3>
                                </div>
                            </a>
                        </div>
                    </div>
                </#list>
            <#else>
                <p style="margin-left: 10px">
                    列表空空～
                </p>
            </#if>
        </div>
        <!--分组-->
        <div class="card-list clearfix report hide" id="group">

        </div>
    </div>
</div>
<div class="layer layer-team height-1of1" id="layer-group">

</div>

<script>
    function openCockpit(id, name) {
        window.open('${request.contextPath}/bigdata/datav/screen/view/' + id, name);
    }
    function openSelfCockpit(url) {
        window.open(url);
    }
    function screenReHeight() {
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
    }
    $(function () {
        screenReHeight();
        $(window).resize(function () {
            screenReHeight()
        });

        $('#all_screen').on('click', function () {
            show('all');
            $(this).removeClass('fake-btn-default').addClass('fake-btn-blue').siblings('.fake-btn').removeClass('fake-btn-blue').addClass('fake-btn-default');
            screenReHeight();
        });
        $('#group_screen').on('click', function () {
            show('group');
            $(this).removeClass('fake-btn-default').addClass('fake-btn-blue').siblings('.fake-btn').removeClass('fake-btn-blue').addClass('fake-btn-default');
            screenReHeight();
        });

        function show(id) {
            $('#' + id).removeClass('hide').siblings('div').addClass('hide');
        }

        $('#group').load('${request.contextPath}/bigdata/cockpitQuery/groupIndex', screenReHeight);
    })
</script>