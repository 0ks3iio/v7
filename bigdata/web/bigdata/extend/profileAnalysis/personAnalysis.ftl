<input type="hidden" id="userId" value="${userId!}"/>
<input type="hidden" id="unitId" value="${user.unitId!}">
<input type="hidden" id="avatarUrl" value="${user.avatarUrl!}">
<div class="facing" id="personAnalysisDiv">
    <div class="box-bg"
         style="background: url('${request.contextPath}/static/bigdata/images/pic-head.png') no-repeat;background-size: 101% 110%;background-position: -5px;">
        <div class="man-detail">
            <div class="man-h text-center">
                        <span><img style="border-radius: 50%" src="${user.avatarUrl!}"
                                   onerror="this.src='${request.contextPath}/static/bigdata/images/person-<#if user.sex?exists><#if user.sex == '男'>man<#else>woman</#if><#else>man</#if>.png'"/></span>
            </div>
            <h3 class="man-name text-center">
                <span style="background: url('${request.contextPath}/static/bigdata/images/<#if user.sex?exists><#if user.sex == '男'>man<#else>woman</#if><#else>man</#if>-icon.png') no-repeat right center;">${user.studentName!}</span>
            </h3>
            <p class="no-margin text-center white">
                <span>${user.unitName!}</span>&nbsp;&nbsp;|&nbsp;&nbsp;
                <span>${user.gradeName!}${user.className!}</span>
            </p>
        </div>
    </div>

    <div class="bg-fff height-50">
        <ul class="nav nav-tabs nav-tabs-1">
            <li class="active js-load" data-name="onePage" id="baseInfo">
                <a data-toggle="tab" style="cursor: pointer" onclick="baseInfo(this)">基本信息</a>
            </li>
                    <#if profileCode?default('') != 'teacher'>
                    <li class="js-load" data-name="twoPage" id="scoreInfo">
                        <a data-toggle="tab" style="cursor: pointer" onclick="scoreInfo(this)">成绩</a>
                    </li>
                    </#if>
        </ul>
    </div>
    <div class="tab-content man-content" id="showContent">

    </div>
</div>
<script>
    $(function () {
        baseInfo();
    });

    function baseInfo() {
        $.ajax({
            url: '${request.contextPath}/bigdata/personAnalysis/baseInfo',
            data: {userId: $('#userId').val()},
            type: 'POST',
            dataType: 'html',
            success: function (response) {
                $('#showContent').empty().append(response);
                $('#scoreInfo').removeClass('active');
                $('#baseInfo').addClass('active');
            }
        });
    }

    function scoreInfo() {
        $.ajax({
            url: '${request.contextPath}/bigdata/personAnalysis/scoreInfo',
            data: {userId: $('#userId').val(), unitId: $('#unitId').val()},
            type: 'POST',
            dataType: 'html',
            success: function (response) {
                $('#showContent').empty().append(response);
                $('#baseInfo').removeClass('active');
                $('#scoreInfo').addClass('active');
            }
        });
    }
</script>
