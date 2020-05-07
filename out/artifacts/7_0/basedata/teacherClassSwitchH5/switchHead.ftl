<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>调课管理</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/font-awesome.css" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/mui.css"/>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/pages.css"/>
</head>
<body>
<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" id="cancelId" style="display:none"></a>

<!-- 管理页 -->
<div class="layer layer-index">
    <div class="fn-flex select-container">
        <div class="fn-flex-auto year" acadyear="${semesterObj.acadyear}">
            <span class="name">${semesterObj.acadyear}</span>
            <span class="mui-icon mui-icon-arrowdown"></span>
        </div>
        <div class="fn-flex-auto term" semester="${semesterObj.semester}">
            <span class="name"><#if semesterObj.semester == 1>第一学期<#elseif semesterObj.semester == 2>第二学期</#if></span>
            <span class="mui-icon mui-icon-arrowdown"></span>
        </div>
        <div class="fn-flex-auto week" week="${semesterObj.week?default(1)}">
        <#if weekList?exists && weekList?size gt 0>
        <span class="name">第${semesterObj.week?default(1)}周</span>
        <#else>
        <span class="name">未设置</span>
        </#if>
            <span class="mui-icon mui-icon-arrowdown"></span>
        </div>
    </div>
    <div class="option-container-layer"></div>
    <div class="option-container">
        <ul data-type="year">
        <#if acadyearList?exists && acadyearList?size gt 0>
        <#list acadyearList as item>
            <li acadyear="${item}" class="yearList"><span>${item}</span><#if item == semesterObj.acadyear><i
                    class="fa fa-check-circle"></i></#if></li>
        </#list>
        </#if>
        </ul>
        <ul data-type="term">
            <li semester="1" class="termList"><span>第一学期</span><#if semesterObj.semester == 1><i
                    class="fa fa-check-circle"></i></#if></li>
            <li semester="2" class="termList"><span>第二学期</span><#if semesterObj.semester == 2><i
                    class="fa fa-check-circle"></i></#if></li>
        </ul>
        <ul data-type="week">
        <#if weekList?exists && weekList?size gt 0>
            <li week="" class="weekList"><span>全部</span></li>
        <#list weekList as item>
            <li week="${item}" class="weekList"><span>第${item}周</span><#if item == semesterObj.week><i
                    class="fa fa-check-circle"></i></#if></li>
        </#list>
        </#if>
        </ul>
    </div>
    <div class="mui-content adjustDtoList" style="padding-top:45px;padding-bottom: 51px;">

    </div>
    <nav class="mui-bar mui-bar-tab">
        <a id="switchapply" class="mui-tab-item mui-active f-16" href="#">申请调课</a>
    </nav>
</div>

<!-- 详情页 -->
<div class="layer layer-detail" style="display: none">
    <div class="mui-content add-form" style="padding-bottom: 51px;">
        <ul class="mui-table-view">
            <li style="display: none" class="adjustedId"></li>
            <li class="mui-table-view-cell">
                <span class="f-16 f-left">审核状态</span>
                <span class="f-16 f-right c-blue switch-state">已通过</span>
            </li>
            <li class="mui-table-view-cell">
                <span class="f-16 f-left">需调教师</span>
                <span class="f-16 f-right adjustingTeacherName">张启明</span>
            </li>
            <li class="mui-table-view-cell">
                <span class="f-16 f-left">需调节次</span>
                <span class="f-16 f-right adjustingName">第11周周三第2节</span>
            </li>
            <li class="mui-table-view-cell">
                <span class="f-16 f-left">班级</span>
                <span class="f-16 f-right className">1班</span>
            </li>
            <li class="mui-table-view-cell">
                <span class="f-16 f-left">需调节次</span>
                <span class="f-16 f-right beenAdjustedName">第11周周三第2节</span>
            </li>
            <li class="mui-table-view-cell">
                <span class="f-16 f-left">被调教师</span>
                <span class="f-16 f-right beenAdjustedTeacherName">张启明</span>
            </li>
        </ul>
        <ul class="mui-table-view">
            <li class="mui-table-view-cell">
                <span class="f-16">备注</span>
            </li>
            <li class="mui-table-view-cell remark">今天有事要请假</li>
        </ul>
    </div>
    <nav class="mui-bar mui-bar-tab switchCancel">
        <!--<a class="mui-tab-item f-16" href="#">不通过</a>
        <a class="mui-tab-item mui-active f-16" href="#">通过</a>-->
        <a class="mui-tab-item f-16" href="#" style="background: #2f7bff;color: #fff;">撤销</a>
    </nav>
</div>
<script src="${request.contextPath}/static/mui/js/jquery-1.9.1.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/static/mui/js/common.js"></script>
<script src="${request.contextPath}/static/mui/js/mui.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/static/mui/js/weike.js"></script>
<script src="${request.contextPath}/static/mui/js/myWeike.js"></script>
<script type="text/javascript">

    $(function(){

        $('#cancelId').off('click').on('click',function(){
            if($(".layer-index").is(":hidden")){
                $(".layer-detail").hide();
                $(".layer-index").show();
            }else{
                var url = '${request.contextPath}/mobile/open/adjusttipsay/modelList/page?unitId=${unitId!}&teacherId=${teacherId!}&userId=${userId!}';
                loadByHref(url);
            }
        });

        // select
        var select = $(".select-container div");
        select.click(function(){
            var index = select.index(this);
            if($(this).parent().siblings(".option-container").children().eq(index).css("display") === "block"){
                $(".option-container-layer").hide();
                $(".option-container").hide();
                $(".option-container ul").hide();
            }else{
                $(".option-container-layer").hide();
                $(".option-container").hide();
                $(".option-container ul").hide();
                $(this).parent().siblings(".option-container-layer").show().siblings(".option-container").show().children().eq(index).show();
            }
        });

        $(".option-container li").click(function(){
            var option_type = $(this).parent().attr("data-type");
            var text = $(this).children("span").text();
            $(this).children("i").addClass("fa fa-check-circle").parent().siblings().children("i").removeClass("fa fa-check-circle");
            $(this).parents(".option-container").siblings(".select-container").find("." + option_type).children(".name").text(text);
            $(".option-container-layer").hide();
            $(".option-container").hide();
            $(".option-container ul").hide();
        });

        $(".option-container-layer").click(function(){
            $(".option-container-layer").hide();
            $(".option-container").hide();
            $(".option-container ul").hide();
        });

        $(".yearList").each(function () {
            $(this).on("tap", function () {
                if ($(this).find("i").length > 0) {
                    return;
                }
                $(this).siblings().find("i").remove();
                $(this).append('<i class="fa fa-check-circle"></i>');
                $(".year").attr("acadyear", $(this).attr("acadyear"));
                var acadyear = $(".year").attr("acadyear");
                var semester = $(".term").attr("semester");
                var parm = '&acadyear='+acadyear+'&semester='+semester;
                var url = "${request.contextPath}/mobile/open/adjusttipsay/showList/page?unitId=${unitId!}&userId=${userId!}&teacherId=${teacherId!}&adminType=${adminType!}&type=3" + parm;
                loadByHref(url);
            });
        });

        $(".termList").each(function () {
            $(this).on("tap", function () {
                if ($(this).find("i").length > 0) {
                    return;
                }
                $(this).siblings().find("i").remove();
                $(this).append('<i class="fa fa-check-circle"></i>');
                $(".term").attr("semester", $(this).attr("semester"));
                var acadyear = $(".year").attr("acadyear");
                var semester = $(".term").attr("semester");
                var parm = '&acadyear='+acadyear+'&semester='+semester;
                var url = "${request.contextPath}/mobile/open/adjusttipsay/showList/page?unitId=${unitId!}&userId=${userId!}&teacherId=${teacherId!}&adminType=${adminType!}&type=3" + parm;
                loadByHref(url);
            });
        });

        $(".weekList").each(function () {
            $(this).on("tap", function () {
                if ($(this).find("i").length > 0) {
                    return;
                }
                $(this).siblings().find("i").remove();
                $(this).append('<i class="fa fa-check-circle"></i>');
                $(".week").attr("week", $(this).attr("week"));
                showSwitchList();
            });
        });

        $("#switchapply").on("tap", function () {
            var parm = '&acadyear=${semesterObj.acadyear}&semester=${semesterObj.semester}';
            var url = "${request.contextPath}/mobile/open/adjusttipsay/switch/apply?unitId=${unitId!}&userId=${userId!}&teacherId=${teacherId!}&adminType=${adminType!}&type=3" + parm;
            loadByHref(url);
        });

        showSwitchList();
    });

    function showSwitchList() {
        var acadyear = $(".year").attr("acadyear");
        var semester = $(".term").attr("semester");
        var week = $(".week").attr("week");
        var parm = '&acadyear='+acadyear+'&semester='+semester+'&week='+week;
        var url = "${request.contextPath}/mobile/open/adjusttipsay/switch/list/page?unitId=${unitId!}&userId=${userId!}&teacherId=${teacherId!}&adminType=${adminType!}" + parm;
        $(".adjustDtoList").load(url);
    }
</script>
</body>
</html>
