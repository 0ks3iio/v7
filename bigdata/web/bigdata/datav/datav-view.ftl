<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="UTF-8">
    <title>${screen.name!}</title>
    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <link rel="stylesheet" type="text/css"
          href="${request.contextPath}/bigdata/v3/static/css/bootstrap.css"/>
    <link rel="stylesheet" href="${request.contextPath}/bigdata/v3/static/font-awsome/css/font-awesome.css"/>
    <link rel="stylesheet" href="${request.contextPath}/bigdata/v3/static/fonts/iconfont.css">
    <link rel="stylesheet" href="${request.contextPath}/bigdata/v3/static/datav/css/style.css">
    <link rel="stylesheet" href="${request.contextPath}/bigdata/v3/static/datav/css/style02.css">
    <link rel="stylesheet" href="${request.contextPath}/bigdata/v3/static/datav/border/css/border.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/icolor/icolor.css"/>
<#--<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css">-->
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/components/slick/slick.css"/>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/components/slick/slick-theme.css"/>
    <style>
        @font-face {
            font-family: Quartz;
            src: url(${request.contextPath}/static/bigdata/css/fonts/Quartz Regular.ttf");
        }
    </style>
    <script>
        _fileUrl = "${fileUrl}";
    </script>
</head>
<body>
<!--主体 S-->
<div class="main-container clearfix temp-pick" style="width: ${screenStyle.width?default(1024)}px;height: ${screenStyle.height?default(768)}px;">

    <!--中间呈现区域 S-->
    <div class="main-show limit-wrap scrollbar-made no-padding col-cell container-height" style="border: none;">

        <div class="temp-wrap-big flex-wrap no-padding block" >
        <#--<div class="box-resize box-outside box-ud box-bottom js-none"></div>-->
        <#--<div class="box-resize box-outside box-lr box-right js-none"></div>-->

            <!--主内容区 S-->
            <div class="temp-wrap js-unbind" style="border:none; width: ${screenStyle.width?default(1024)}px;height: ${screenStyle.height?default(768)}px;background-color: ${screenStyle.backgroundColor?default("#072956")};">
                <div class="box-data full-screen text-center clearfix js-full">
                    <div class="width-1of1 text-center float-left">
                        <img src="${request.contextPath}/static/bigdata/images/computer.png" width="20" height="18">
                    </div>
                    <span class="inline-block float-left width-1of1">全屏</span>
                </div>

                <div class="chart-part clearfix relative">
                    <div class="bg-show"></div>

                    <div id="box-container">

                    </div>
                </div>
            </div><!--主内容区 E-->
        </div>
    </div><!--中间呈现区域 E-->
</div><!--主体 E-->


<script src="${request.contextPath}/bigdata/v3/static/js/jquery/jquery.min.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/layer/layer.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/bootstrap/bootstrap.min.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/echarts/echarts.4.1.0.rc2.min.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/echarts/echarts-wordcloud.min.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/zresize/jquery.ZResize.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/slick/slick.js"></script>

<script src="${request.contextPath}/bigdata/v3/static/datav/datav-common.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/datav-f11.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/datav-ui.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/datav-net.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/datav-task.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/datav-diagram-render-echarts.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/datav-diagram-render.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/datav-diagram-render-other.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/datav-diagram-config.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/datav-dymaic-number.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/datav-tab.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/counter/tweenLite.min.js" defer="defer"
        type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/counter/lem-counter.js" defer="defer" type="text/javascript"
        charset="utf-8"></script>
<script>
    _contextPath = "${request.contextPath}";
    _screenId = "${screenId}";
    _preview = true;
</script>
<script type="text/javascript">
    //伸缩
    window.onload=function(){
        var wSelf=document.getElementById('w-self');
        var hSelf=document.getElementById('h-self');
        var bg=document.getElementsByClassName('table-bg')[0];
        var outside=document.getElementsByClassName("box-outside");
        var cut=document.getElementsByClassName("flex-wrap")[0];
        var cutWidth=0;
        var cutHeight=0;
        var startX=0;
        var startY=0;
        var top=0;
        var left=0;
        var dir="";
        for(var i=0;i<outside.length;i++){
            outside[i].onmousedown=function(e){
                startX=e.clientX;
                startY=e.clientY;
                cutWidth=cut.offsetWidth;
                cutHeight=cut.offsetHeight;
                top=cut.offsetTop;
                left=cut.offsetLeft;
                var className=this.className;
                if(className.indexOf("box-right")>-1){
                    dir="E";
                }
                else if(className.indexOf("box-left")>-1){
                    dir="W";
                }
                else if(className.indexOf("box-bottom")>-1){
                    dir="S";
                }
                else if(className.indexOf("box-top")>-1){
                    dir="N";
                }
                else if(className.indexOf("box-top-left")>-1){
                    dir="NW";
                }
                else if(className.indexOf("box-top-right")>-1){
                    dir="NE";
                }
                else if(className.indexOf("box-bottom-left")>-1){
                    dir="SW";
                }
                else if(className.indexOf("box-bottom-right")>-1){
                    dir="SE";
                }
                document.addEventListener('mousemove',test);
                e.preventDefault();
            }
        }

        document.onmouseup=function(e){
            dir="";
            document.removeEventListener('mousemove',test);
            e.preventDefault();
        };

        function test(e){
            var width=e.clientX-startX;
            var height=e.clientY-startY;
            if(dir=="E"){
                cut.style.width=cutWidth+width+"px";
                wSelf.value=cutWidth+width-60+"px";
            }
            else if(dir=="S"){
                cut.style.height=cutHeight+height+"px";
                hSelf.value=cutHeight+height-60+"px";
            }
        }

    };

    $(document).ready(function(){



        $('.temp-wrap-big').addClass('no-padding');
        $('.temp-wrap').css('border','none');
        if($(window).width()>$('.temp-wrap').width()){
            $('.temp-wrap').width($(window).width())
        }
        if($(window).height()>$('.temp-wrap').height()){
            $('.temp-wrap').height($(window).height())
        }
        $('.main-show').removeClass('win-height').css({
            width: $('.temp-wrap').width(),
            height: $('.temp-wrap').height(),
            //overflow: 'hidden'
        });

        setDateTimeStyle(${screenStyle.dateTimeStyle?default(1)});
        function setDateTimeStyle(val) {
            if(val==1){
                $('.date').removeClass('right-20').removeClass('hide')
            }else if(val==2){
                $('.date').addClass('right-20').removeClass('hide')
            }else{
                $('.date').addClass('hide')
            }
        }
        //时间日期
        function getDate(){
            var date = new Date();
            var week = {
                0 : '日',
                1 : '一',
                2 : '二',
                3 : '三',
                4 : '四',
                5 : '五',
                6 : '六'
            };
            var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
            var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
            var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
            var month = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
            var year = date.getFullYear();
            var day = date.getDate();
            var cur_week = '星期' + week[date.getDay()];

            $('.date .time').text(hours + ":" + minutes + ":" + seconds);
            $('.date .day').text(year +'-'+ month + '-' + day);
            $('.date .week').text(cur_week);
        }
        getDate();
        setInterval(getDate, 1000);
    });
</script>
<script>
    <#if diagramVos?? && diagramVos?size gt 0>
        <#list diagramVos as diagramVo>
            dataVUI.addExistsDiagram({
                diagramId: "${diagramVo.diagramId}",
                x: "${diagramVo.x}",
                y: "${diagramVo.y}",
                diagramType: ${diagramVo.diagramType},
                width: "${diagramVo.width}",
                height: "${diagramVo.height}",
                level: "${diagramVo.level}",
                backgroundColor: "${diagramVo.backgroundColor?default("")}"
            });
            <#if diagramVo.updateInterval?default(-1) gt 0>
                dataVTimer.addTask(function () {
                    dataVRender.doRender({screenId: _screenId, diagramId: '${diagramVo.diagramId}', dispose:false});
                }, '${diagramVo.diagramId}', ${diagramVo.updateInterval} * 1000);
            </#if>
        </#list>
    </#if>
</script>
</body>
</html>