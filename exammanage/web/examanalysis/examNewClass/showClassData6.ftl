<#import "/fw/macro/chartstructure.ftl" as chartstructure />
<#if reportType?default("1")=="1">
    <#if statList?exists && statList?size gt 0>
        <div class="explain">
            <p>
                注：进步度=本次考试标准分T(年级)-参照考试标准分T(年级)
            </p>
        </div>
        <div class="table-container">
            <div class="table-container-body" style="overflow-x: auto;">
                <form class="print">
                    <table class="table table-bordered table-striped table-hover">
                        <thead>
                        <tr>
                            <th class="text-center" colspan="12">${title}</th>
                        </tr>
                        <tr>
                            <th rowspan="2">学号</th>
                            <th rowspan="2">姓名</th>
                            <th rowspan="2">班级</th>
                            <th colspan="3">${examName!}</th>
                            <th colspan="3">${compareExamName!}</th>
                            <th rowspan="2">进步度</th>
                            <th rowspan="2">进步度班级排名</th>
                            <th rowspan="2">进步度年级排名</th>
                        </tr>
                        <tr>
                            <th >考试分</th>
                            <th >年级排名</th>
                            <th>标准分T(年级)</th>
                            <th >考试分</th>
                            <th >年级排名</th>
                            <th>标准分T(年级)</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list statList as stat>
                            <tr>
                                <td>${stat.studentCode}</td>
                                <td>${stat.studentName}</td>
                                <td>${stat.className}</td>
                                <td>${stat.score!}</td>
                                <td>${stat.gradeRank!}</td>
                                <td><#if stat.scoreT?exists>${stat.scoreT?default(0)?string('0.#')}</#if></td>
                                <td>${stat.compareExamScore!}</td>
                                <td>${stat.compareExamRank!}</td>
                                <td><#if stat.compareExamScoreT?exists>${stat.compareExamScoreT?default(0)?string('0.#')}</#if></td>
                                <td><#if stat.progressDegree?exists>${stat.progressDegree?default(0)?string('0.#')}</#if></td>
                                <td>${stat.progressDegreeRankClass!}</td>
                                <td>${stat.progressDegreeRankGrade!}</td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </form>
            </div>
        </div>
    <#else>
        <div class="no-data-container">
            <div class="no-data">
                <span class="no-data-img">
                    <img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
                </span>
                <div class="no-data-body">
                    <p class="no-data-txt">暂无记录</p>
                </div>
            </div>
        </div>
    </#if>
<#else>
<#if jsonStringData1?exists&&jsonStringData1!=""&&jsonStringData2?exists&&jsonStringData2!="">
    <div class="explain">
        <p>
            注：1、标准分T以50为成绩好与差的分界线（即整个年级群体标准分T的平均成绩为50）;
            2、进步度为正数说明进步,负数说明退步
        </p>
    </div>
    <@chartstructure.scatter loadingDivId="chart13" divStyle="width: 800px;height: 400px;" jsonStringData=jsonStringData1 />
    <@chartstructure.scatter loadingDivId="chart14" divStyle="width: 800px;height: 400px;" jsonStringData=jsonStringData2 />
<#else>
    <div class="no-data-container">
        <div class="no-data">
                    <span class="no-data-img">
                        <img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
                    </span>
            <div class="no-data-body">
                <p class="no-data-txt">暂无记录</p>
            </div>
        </div>
    </div>
</#if>
</#if>
<script>
    function doExport(){
        var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
        //设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
        LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".print")));
        var acadyear = $('#acadyear').val();
        LODOP.SAVE_TO_FILE('${title!}'+".xls");
    }
</script>