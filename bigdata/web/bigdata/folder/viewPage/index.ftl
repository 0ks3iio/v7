<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="UTF-8">
    <title>
        <#if folderDetail.businessType?default('3') == '1'>
            查看图表
        <#elseif folderDetail.businessType?default('3') == '3'>
            查看报表
        <#elseif folderDetail.businessType?default('3') == '5'>
            查看多维报表
        <#elseif folderDetail.businessType?default('3') == '6'>
            查看数据看板
        <#elseif folderDetail.businessType?default('3') == '7'>
            查看数据报告
        <#else>
        </#if>
        -${folderDetail.businessName!}
    </title>
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <link rel="shortcut icon"  href="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/public/wanshu-icon16.png" >
    <link rel="stylesheet" type="text/css"
          href="${springMacroRequestContext.contextPath}/bigdata/v3/static/css/bootstrap.css"/>
    <link rel="stylesheet"
          href="${springMacroRequestContext.contextPath}/bigdata/v3/static/font-awsome/css/font-awesome.css"/>
    <link rel="stylesheet" type="text/css"
          href="${springMacroRequestContext.contextPath}/bigdata/v3/static/css/iconfont.css"/>
    <link rel="stylesheet" type="text/css"
          href="${springMacroRequestContext.contextPath}/bigdata/v3/static/fonts/iconfont.css"/>
    <link rel="stylesheet" type="text/css"
          href="${springMacroRequestContext.contextPath}/bigdata/v3/static/css/all.css"/>
    <link rel="stylesheet" type="text/css"
          href="${springMacroRequestContext.contextPath}/bigdata/v3/static/css/style.css"/>
    <script>
        _contextPath = "${springMacroRequestContext.contextPath}";
    </script>
</head>
<body>
<div class="main-container">
    <!--内容 S-->
    <div class="main-content">
        <div class="main-content-inner">
            <div class="page-content">
            </div>
        </div>
    </div><!--内容 E-->
</div><!--主体 E-->
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/jquery/jquery.min.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/jquery/jquery.form.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/bootstrap/bootstrap.min.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/fonts/iconfont.js" async="async" defer="defer"
        type="text/javascript" charset="utf-8"></script>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/layer/layer.js" async="async" defer="defer"
        type="text/javascript" charset="utf-8"></script>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/echarts/echarts.4.1.0.rc2.min.js"
        type="text/javascript" charset="utf-8"></script>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/myscript.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/tool.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/laydate/laydate.min.js" type="text/javascript"
        charset="utf-8"></script>
<script src="http://www.jq22.com/jquery/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript">
    (function ($) {
        var opt;

        $.fn.jqprint = function (options) {
            opt = $.extend({}, $.fn.jqprint.defaults, options);

            var $element = (this instanceof jQuery) ? this : $(this);

            if (opt.operaSupport && $.browser.opera) {
                var tab = window.open("", "jqPrint-preview");
                tab.document.open();

                var doc = tab.document;
            }
            else {
                var $iframe = $("<iframe  />");

                if (!opt.debug) {
                    $iframe.css({position: "absolute", width: "0px", height: "0px", left: "-600px", top: "-600px"});
                }

                $iframe.appendTo("body");
                var doc = $iframe[0].contentWindow.document;
            }

            if (opt.importCSS) {
                if ($("link[media=print]").length > 0) {
                    $("link[media=print]").each(function () {
                        doc.write("<link type='text/css' rel='stylesheet' href='" + $(this).attr("href") + "' media='print' />");
                    });
                }
                else {
                    $("link").each(function () {
                        doc.write("<link type='text/css' rel='stylesheet' href='" + $(this).attr("href") + "' />");
                    });
                }
            }

            if (opt.printContainer) {
                doc.write($element.outer());
            }
            else {
                $element.each(function () {
                    doc.write($(this).html());
                });
            }

            doc.close();

            (opt.operaSupport && $.browser.opera ? tab : $iframe[0].contentWindow).focus();
            setTimeout(function () {
                (opt.operaSupport && $.browser.opera ? tab : $iframe[0].contentWindow).print();
                if (tab) {
                    tab.close();
                }
                $('.common-chart-view').each(function (i, v) {
                    // 获取echart
                    $(v).parent().find('.echartImg').remove();
                    $(v).css('display', 'block');
                    $('.condition-div').css('display', 'block');
                    $('.score-title').css('display', 'block');
                });
            }, 1000);
            $('.condition-div').css('display', 'block');
        }

        $.fn.jqprint.defaults = {
            debug: false,
            importCSS: true,
            printContainer: true,
            operaSupport: true
        };

        // Thanks to 9__, found at http://users.livejournal.com/9__/380664.html
        jQuery.fn.outer = function () {
            return $($('<div></div>').html(this.clone())).html();
        }
    })(jQuery);

    function print() {
        // echarts转成图片
        $('.common-chart-view').each(function (i, v) {
            // 获取echart
            var element = document.getElementById($(v).attr('id'));
            var currentInstance = echarts.getInstanceByDom(element);
            var dataUrl = "";
            try {
                dataUrl = currentInstance.getDataURL({type: 'jpeg',backgroundColor: '#fff'});
            } catch (e) {

            }
            var newImg = document.createElement("img");
            newImg.src = dataUrl;
            newImg.className='echartImg';
            $(v).parent().append(newImg);
            $(v).css('display', 'none');
        });
        $('.condition-div').css('display', 'none');
        $('.score-title').css('display', 'none');
        var businessType = '${folderDetail.businessType!}';
        if (businessType == '1') {
            $('.chart-main-view').jqprint();
        } else if (businessType == '6' || businessType == '7') {
            $('.kanban-wrap').jqprint();
        } else {
            $('.report-view').jqprint();
        }
    }
</script>
<script type="text/javascript">
    $(function () {
        if ('${hasDelete!?string('yes', 'no')}' == 'yes') {
            showLayerTips4Confirm('error', '该报表已经被删除', 't', null);
        } else {
            var url = '${springMacroRequestContext.contextPath}/bigdata/report/view?chartId=${folderDetail.businessId!}';
            var businessType = '${folderDetail.businessType!}';
            if (businessType == '1') {
                url = '${springMacroRequestContext.contextPath}/bigdata/chart/query/adapter/${folderDetail.businessId!}';
            } else if (businessType == '5') {
                url = '${springMacroRequestContext.contextPath}/bigdata/model/report/view?id=${folderDetail.businessId!}'
            } else if (businessType == '6' || businessType == '7') {
                url = '${springMacroRequestContext.contextPath}/bigdata/multireport/preview?reportId=${folderDetail.businessId!}&type=${folderDetail.businessType!}&containHeader=${containHeader?string('true', 'false')}';
            }


            $('.page-content').load(url, function () {
                if (businessType == '6' || businessType == '7') {
                    $('.page-content').prepend('<div class="box-header clearfix">\n' +
                        '\t\t\t\t\t\t\t\n' +
                        '\t\t\t\t\t\t\t<div class="float-right">\n' +
                        '\t\t\t\t\t\t\t\t<button class="btn btn-lightblue" onclick="print()">导出PDF</button>\n' +
                        '\t\t\t\t\t\t\t</div>\n' +
                        '\t\t\t\t\t\t</div>');
                } else if (businessType != '3') {
                    $('.page-content').prepend('<div class="box-header clearfix">\n' +
                        '\t\t\t\t\t\t\t\n' +
                        '\t\t\t\t\t\t\t<div class="text-center">\n' +
                        '\t\t\t\t\t\t\t\t<h1>'+'${folderDetail.businessName!}'+'</h1>\n' +
                        '\t\t\t\t\t\t\t</div>\n' +
                        '\t\t\t\t\t\t\t<div class="float-right">\n' +
                        '\t\t\t\t\t\t\t\t<button class="btn btn-lightblue" onclick="print()">导出PDF</button>\n' +
                        '\t\t\t\t\t\t\t</div>\n' +
                        '\t\t\t\t\t\t</div>');
                } else if (businessType == '3') {
                    $('.page-content').prepend('<div class="box-header clearfix">\n' +
                        '\t\t\t\t\t\t\t\n' +
                        '\t\t\t\t\t\t\t<div class="text-center pb-10">\n' +
                        '\t\t\t\t\t\t\t\t<h1>'+'${folderDetail.businessName!}'+'</h1>\n' +
                        '\t\t\t\t\t\t\t</div>\n' +
                        '\t\t\t\t\t\t</div>');
                }
            });
        }
    });

</script>
</body>
</html>

