<#if nonemess?exists>
    <link href="${request.contextPath}/static/mui/css/pages.css" rel="stylesheet"/>
    <div class="ui-page">
        <!--两种无记录了状态-->
        <p class="page-nodata">${nonemess?default("没有相关内容，敬请期待。")}</p>
    </div>
<#else>

    <table class="courseTable" width="100%">
        <thead>
        <tr>
            <th width="2%" colspan="2">&nbsp;</th>
            <th width="14%">周一</th>
            <th width="14%">周二</th>
            <th width="14%">周三</th>
            <th width="14%">周四</th>
            <th width="14%">周五</th>
            <#if weekEnd?default(false)>
                <th width="14%">周六</th>
                <th width="14%">周日</th>
            </#if>
        </tr>
        </thead>
        <tbody>

        <#assign mc = 0/>
        <#list 1..allCourseNum?default(8) as x>
            <#assign day = "p" + x />
            <#assign v = (values[day])! />
            <tr>
                <#if x lte timeIntervalMap["1"] && x==1>
                    <td class="text-center" rowspan="${timeIntervalMap["1"]}">早自习</td>
                <#elseif x == (timeIntervalMap["1"]+1) >
                    <td class="text-center" rowspan="${timeIntervalMap["2"]!}">上午</td>
                    <#assign mc = x - 1/>
                <#elseif x == (timeIntervalMap["2"]+timeIntervalMap["1"] + 1) >
                    <td class="text-center" rowspan="${timeIntervalMap["3"]}">下午</td>
                    <#assign mc = x - 1/>
                <#elseif x == (timeIntervalMap["3"]+timeIntervalMap["2"]+timeIntervalMap["1"] + 1) >
                    <td class="text-center" rowspan="${timeIntervalMap["4"]}">晚上</td>
                    <#assign mc = x - 1/>
                </#if>
                <td class="text-center">${x - mc}</td>
                <td class="text-center" id="td${x}-0">
                    ${v.courseName1!}<br/>${v.className1!}</br>
                    <span title="${v.placeName1!}">${v.placeName1!}</span>
                    <#if v.courseNameRe1?exists>
                        ${v.courseNameRe1!}<br/>${v.classNameRe1!}</br>
                        <span title="${v.placeNameRe1!}">${v.placeNameRe1!}</span>
                    </#if>
                </td>
                <td class="text-center" id="td${x}-1">
                    ${v.courseName2!}<br/>${v.className2!}</br>
                    <span title="${v.placeName2!}">${v.placeName2!}</span>
                    <#if v.courseNameRe2?exists>
                        ${v.courseNameRe2!}<br/>${v.classNameRe2!}</br>
                        <span title="${v.placeNameRe2!}">${v.placeNameRe2!}</span>
                    </#if>
                </td>
                <td class="text-center" id="td${x}-2">
                    ${v.courseName3!}<br/>${v.className3!}</br><span
                            title="${v.placeName3!}">${v.placeName3!}</span>
                    <#if v.courseNameRe3?exists>
                        ${v.courseNameRe3!}<br/>${v.classNameRe3!}</br>
                        <span title="${v.placeNameRe3!}">${v.placeNameRe3!}</span>
                    </#if>
                </td>
                <td class="text-center" id="td${x}-3">
                    ${v.courseName4!}<br/>${v.className4!}</br>
                    <span title="${v.placeName4!}">${v.placeName4!}</span>
                    <#if v.courseNameRe4?exists>
                        ${v.courseNameRe4!}<br/>${v.classNameRe4!}</br>
                        <span title="${v.placeNameRe4!}">${v.placeNameRe4!}</span>
                    </#if>
                </td>
                <td class="text-center" id="td${x}-4">
                    ${v.courseName5!}<br/>${v.className5!}</br>
                    <span title="${v.placeName5!}">${v.placeName5!}</span>
                    <#if v.courseNameRe5?exists>
                        ${v.courseNameRe5!}<br/>${v.classNameRe5!}</br>
                        <span title="${v.placeNameRe5!}">${v.placeNameRe5!}</span>
                    </#if>
                </td>
                <#if weekEnd?default(false)>
                    <td class="text-center" id="td${x}-5">
                        ${v.courseName6!}<br/>${v.className6!}</br>
                        <span title="${v.placeName6!}">${v.placeName6!}</span>
                        <#if v.courseNameRe6?exists>
                            ${v.courseNameRe6!}<br/>${v.classNameRe6!}</br>
                            <span title="${v.placeNameRe6!}">${v.placeNameRe6!}</span>
                        </#if>
                    </td>
                    <td class="text-center" id="td${x}-6">
                        ${v.courseName7!}<br/>${v.className7!}</br>
                        <span title="${v.placeName7!}">${v.placeName7!}</span>
                        <#if v.courseNameRe7?exists>
                            ${v.courseNameRe7!}<br/>${v.classNameRe7!}</br>
                            <span title="${v.placeNameRe7!}">${v.placeNameRe7!}</span>
                        </#if>
                    </td>
                </#if>
            </tr>
        </#list>
        </tbody>
    </table>
    <script>
        var dayOfWeek = "${dayOfWeek?default('')}";
        var section = "${allCourseNum?default('')}";
        $(document).ready(function(){
            $("tr:first").children("th:eq("+(parseInt(dayOfWeek)+1)+")").addClass("active");
            for(var i=1;i<=parseInt(section);i++){
                $("#td"+i+"-"+(dayOfWeek)).addClass("active");
            }
        });
    </script>
</#if>