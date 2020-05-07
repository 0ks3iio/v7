<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no" />
	<title>${(title)!"我的课程表"}</title>
	<script type="text/javascript" src="${request.contextPath}/static/openapi/js/jquery-1.8.3.min.js"></script>
    <style>
        body{ margin:0; padding:0; font-size:0.9em;}
        .courseTable{ border-collapse:collapse; border-spacing:0; text-align:center;}
        .courseTable thead th{ background:#797979; color:#fff; line-height:24px; font-weight:normal;}
        .courseTable thead th.active{ background:#53b53e;}
        .courseTable tbody{ color:#999; line-height:28px;}
        .courseTable tbody th{ padding:24px 2px; background:#dbdbdb; font-weight:normal;}
        .courseTable tbody td{ padding:24px 0; border:0.07em solid #e7e7e7; border-top:none;}
        .courseTable tbody td.active{ background:#edf7eb;}
        .coursetab {width: 100%;display: flex;margin-bottom: 0.5em;justify-content: space-evenly;}
        .coursetab_item {text-align: center;padding: 0.8em 1em;color: #999;}
        .coursetab_item.active {color: #317eeb;border-bottom: 1px solid #317eeb;}
        .coursesai {margin-bottom: 0.5em;text-align: right;padding: 0 0.8em;display: none;}
        .coursesai select {width: 8em;height: 2em;border-radius: 4px;}
    </style>
</head>
<#assign isHeadTeacher=clazzList?exists && clazzList?size gt 0>
<body>
    <#if isHeadTeacher>
    <div class="coursetab">
        <div class="coursetab_item active" data-id="1">教师课表</div>
        <div class="coursetab_item" data-id="2">班主任课表</div>
    </div>
    <div class="coursesai">
        <select>
            <#list clazzList as clazz>
            <option value="${clazz.id!}">${clazz.classNameDynamic!}</option>
            </#list>
        </select>
    </div>
    </#if>
    <div id="showDetailDiv">
    </div>
</body>
<script>
    $(function () {
        <#if isHeadTeacher>
        $('.coursetab').find('.coursetab_item').click(function() {
            $(this).siblings().removeClass('active');
            $(this).addClass('active');
            var id = $(this).attr('data-id');
            if (id == '1') {
                $('.coursesai').css('display', 'none');
            } else {
                $('.coursesai').css('display', 'block');
            }
            showDetail();
        });

        $('.coursesai select').change(function(e) {
            var classId = $(this).children('option:selected').val();
            showDetail();
        });
        </#if>

        function showDetail() {
            <#if isHeadTeacher>
            var tabType = $(".coursetab_item.active").attr('data-id');
            var classId = $('.coursesai select').val();
            var url = "${request.contextPath}/basedata/showTimetable/detail/page?&ownerId=${ownerId}&type=${type!}&tabType="+tabType+"&classId="+classId;
            <#else>
            var url = "${request.contextPath}/basedata/showTimetable/detail/page?tabType=1&ownerId=${ownerId}&type=${type!}"
            </#if>
            $("#showDetailDiv").load(url);
        }

        showDetail();
    })
</script>

</html>
