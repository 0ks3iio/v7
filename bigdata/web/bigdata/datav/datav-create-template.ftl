<!--模板选择-->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <title>大数据中心-模板选择</title>
    <link rel="stylesheet" type="text/css"
          href="${request.contextPath}/static/components/bootstrap/dist/css/bootstrap.css"/>
    <link rel="stylesheet" href="${request.contextPath}/static/components/font-awesome/css/font-awesome.css"/>
    <link rel="stylesheet" href="${request.contextPath}/static/bigdata/css/style02.css">
    <style>
        @font-face {
            font-family: Quartz;
            src: url("${request.contextPath}/static/bigdata/css/fonts/Quartz Regular.ttf");
        }
    </style>
</head>
<body>
<!--头部 S-->
<div class="data-center row">
    <div class="logo-name col-md-6">
        <div class="">
            <b>大数据中心</b>
        </div>
    </div>
</div><!--头部 E-->

<!--主体 S-->
<div class="main-container clearfix">
    <!--左侧模板选择 S-->
    <div class="temp-show col-cell scroll-height">
        <div class="pic-wrap active">
				<span class="pic-border">
					<img src="${request.contextPath}/static/bigdata/datav/images/temp01.png"/>
				</span>
            <p>自定义</p>
        </div>

        <#if libriaries?? && libriaries?size gt 0>
            <#list libriaries as libriary>
                <div class="pic-wrap" data-id="${libriary.id!}">
                    <span class="pic-border">
                        <img src="${request.contextPath}/static/bigdata/datav/images/${libriary.iconPath!}"/>
                    </span>
                    <p>${libriary.name!}</p>
                </div>
            </#list>
        </#if>


    </div><!--左侧模板类型 E-->

    <!--中间模板展示 S-->
    <div class="main-show col-cell no-padding">
        <div class="head clearfix">
            <p class="col-cell"></p>
            <div class="float-right">
                <a href="javascript:void(0);" id="createFromTemplate">
                    <button class="btn btn-primary bg-00cce3 no-border">创建</button>
                </a>
            </div>
        </div>

        <div class="centered scroll-height">
            <img id="blank" src="${request.contextPath}/static/bigdata/datav/images/blank.jpg" style="width: 1024px;height: 576px;" />
            <#if libriaries?? && libriaries?size gt 0>
                <#list libriaries as libriary>
                    <img id="${libriary.id}" src="${request.contextPath}/static/bigdata/datav/images/${libriary.fullIconPath!}" class="hidden" alt=""/>
                </#list>
            </#if>
        </div>
    </div><!--中间模板展示 E-->
</div><!--主体 E-->


<script src="${request.contextPath}/static/components/jquery/dist/jquery.min.js"></script>
<script src="${request.contextPath}/static/components/layer/layer.js"></script>
<script src="${request.contextPath}/static/components/bootstrap/dist/js/bootstrap.min.js"></script>
<script>
    $(document).ready(function () {
        $('.scroll-height').each(function (index, ele) {
            $(this).css({
                height: $(window).height() - $(this).offset().top,
                overflowY: 'auto'
            })
        });
        //选择模板
        $('.pic-wrap').on('click', function () {
            $(this).addClass('active').siblings().removeClass('active');
            $('.head>p').text($(this).find('p').text());
            let id = $(this).data('id');
            if (!id) {
                id = "blank";
            }
            $('#' + id).removeClass('hidden').siblings('img').addClass('hidden');
        });
        $('#createFromTemplate').on('click', function () {
            let templateId = $('.pic-wrap.active').data('id');
            layer.prompt({title: '请输入大屏名称', formType: 3, maxlength: 50}, function (cockpitName, index) {
                layer.close(index);
                if (templateId) {
                    $.ajax({
                        url: '${request.contextPath}/bigdata/datav/screen/create-from-template',
                        type: 'POST',
                        data: {
                            templateId: templateId,
                            name: cockpitName,
                        },
                        success: function (res) {
                            if (res.success) {
                                window.location.href = "${request.contextPath}/bigdata/datav/screen/" + res.data;
                            } else {
                                layer.msg(res.msg, {icon: 2});
                            }
                        },
                        error: function () {
                            layer.msg("网络异常", {icon: 2});
                        }
                    });
                } else {
                    $.ajax({
                        url: '${request.contextPath}/bigdata/datav/screen/',
                        data: {
                            name: cockpitName
                        },
                        type: 'POST',
                        dataType: 'json',
                        async: false,
                        success: function (data) {
                            if (data.success) {
                                window.location.href = "${request.contextPath}/bigdata/datav/screen/" + data.data;
                            }
                            else {
                                layer.msg("驾驶舱创建失败", {icon: 3, time: 1000});
                            }
                        }
                    });
                }
            });

        });
    })
</script>
</body>
</html>