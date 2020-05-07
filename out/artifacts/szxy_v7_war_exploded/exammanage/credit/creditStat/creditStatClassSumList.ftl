<#if studentList??&&studentList?size gt 0>
    <table class="table table-striped table-bordered table-hover text-center no-margin js-table-scroll">
        <thead>
        <tr>
            <th rowspan="2" class="text-center">序号</th>
            <th rowspan="2" class="text-center">
                学号&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
            </th>
            <th rowspan="2" class="text-center">
                姓名&emsp;&emsp;&emsp;&emsp;
            </th>
            <#if parentList?? && parentList?size gt 0>
                <#list parentList as item>
                                    <th colspan=${item.subSetList?size+1} class="text-center">${item.name!}</th>
                </#list>
            </#if>
             <#if examSets?? && examSets?size gt 0>
                 <th colspan=${examSets?size+2} class="text-center">平时成绩</th>
             <#else >
                <th class="text-center" colspan="2">平时成绩</th>
             </#if>
            <th rowspan="2" class="text-center">
                过程<br />合计
            </th>

            <th class="text-center" colspan="2">模块考核</th>
        <#--<th colspan="2" class="text-center">模块考核</th>-->
            <th rowspan="2" class="text-center">总分</th>
            <th rowspan="2" class="text-center">学分</th>
            <th colspan="2" class="text-center">补考</th>
            <th rowspan="2" class="text-center">
                补修<br />总分
            </th>
            <th rowspan="2" class="text-center">学分</th>
        </tr>
        <tr>
            <#if parentList?? && parentList?size gt 0>
                <#list parentList as item>
                    <#list stringListMap[item.id] as item1>
                        <th class="text-center">${item1.name}</th>
                    </#list>
                <th class="text-center">分值</th>
                </#list>
            </#if>
    <#--//平时成绩-->
            <#if examSets?? && examSets?size gt 0>
                <#list examSets as item>
                    <th class="text-center">${item.name}&emsp;</th>
                </#list>
            </#if>

            <th class="text-center">平均</th>

            <th class="text-center">折分</th>
            <th class="text-center" >原始</th>
            <th class="text-center">比例</th>
            <th class="text-center" >原始</th>
            <th class="text-center">比例</th>
        </tr>
        </thead>
        <tbody>
        <#list studentList as item>
        <#assign processScoreAll=0>
        <#--//日常-->
            <tr>
                <td>${item_index+1}</td>
                <td>${item.studentCode!}</td>
                <td class="text-left">${item.studentName!}</td>
                <#if parentList?? && parentList?size gt 0>
                    <#list parentList as item2>
                        <#assign sumScore=0>
                        <#list stringListMap[item2.id] as item1>
                            <#if listMap1[item.id]??&&listMap1[item.id]?size gt 0>
                                <#assign hasScore = 0>
                                <#list listMap1[item.id] as item4>
                                    <#if item1.id==item4.subDailyId>
                                        <#assign hasScore = 1>
                                        <#assign sumScore=sumScore+item4.score!>
                                    <td class="text-center">${item4.score!}</td>
                                    </#if>
                                </#list>
                                <#if hasScore==0>
                                                    <td></td>
                                </#if>
                            <#else >
                            <td class="text-center"></td>
                            </#if>
                        <#--<th class="text-center">${item1.name}</th>-->
                        </#list>
                        <td class="text-center">${sumScore!}</td>
                        <#assign processScoreAll=processScoreAll+sumScore>
                    </#list>
                </#if>
            <#--//平时成绩-->
            <#assign indexDaily = 0>
                 <#if examSets?? && examSets?size gt 0>
                     <#list examSets as item2>
                         <#if listMap2[item.id]??>
                             <#assign hasScore = 0>
                             <#list listMap2[item.id] as item4>
                                 <#if item2.id==item4.examSetId>
                                     <#assign hasScore = 1>
                                     <#assign indexDaily = indexDaily+item4.score!>
                                        <td class="text-center">${item4.score!}</td>
                                 </#if>
                             </#list>
                             <#if hasScore==0>
                                 <td></td>
                             </#if>
                         <#else >
                            <td></td>
                         </#if>
                     </#list>
                 <#else>
                 </#if>
                <#assign processScore = processScoreAll!>
                <#if examSets?size gt 0>
                    <#assign scoreAvg = indexDaily/examSets?size!>
                <#--平均-->
                    <td>${scoreAvg?string("#.##")}</td>
                    <#assign scorePercent = scoreAvg*usualPercent!>
                <#--折分-->
                    <td><span class="color-blue">${scorePercent?string("#.##")}</span></td>
                    <#assign processScore = processScore+scorePercent!>
                <#--//过程合计-->
                    <td><span class="color-blue">${processScore?string("#.##")}</span></td>
                <#else >
                    <td></td>
                    <td></td>
                <#--//过程合计-->
                    <td><span class="color-blue">${processScore?string("#.##")}</span></td>
                </#if>
                <#assign studentId=item.id>
            <#--<>-->
                <#assign mouduleScore=listMap3[studentId]!>
                <#assign mouduleScorePercent=listMap4[studentId]!>
                <#if listMap3?size gt 0&&listMap3[studentId]??>
                <#--//模块分-->
                    <td>${mouduleScore?string("#.##")}</td>
                <#else >
                    <td></td>
                </#if>
            <#--//比例-->
                <#if listMap4?size gt 0&&listMap4[studentId]??>
                    <td>${mouduleScorePercent?string("#.##")}</td>
                <#else >
                    <td></td>
                </#if>
                 <#assign processScore=processScore?string("#.##")>
                <#assign processScore = processScore?number>
                <#assign totalScore=processScore+mouduleScorePercent>
                <#assign passScore=passScore?number>
                <#if passScore gt totalScore?number>
                    <td>
                        <span class="color-red">${totalScore!}</span>
                    </td>
                    <td><span class="color-blue"></span></td>
                    <#if listMap5?size gt 0&&listMap5[studentId]??>
                        <#assign resitScore = listMap5[studentId]*modulePercent>
                    <#--补考分数-->
                    <td>${listMap5[studentId]!}</td>
                    <td>${resitScore?string("#.##")}</td>
                        <#assign resitScore=resitScore?string("#.##")>
                        <#assign resitScore = resitScore?number>
                        <#assign resitTotalScore=resitScore+processScore>
                        <#assign passScore=passScore?number>
                    <#--//补考总分-->
                    <td><#if passScore gt resitTotalScore?number><span class="color-red">${resitTotalScore!}</span><#else ><span class="color-blue">60</span></#if></td>
                    <#--学分-->
                    <td>
                        <#if passScore gt resitTotalScore?number>
                            <span class="color-blue"></span>
                        <#else >
                            <#if item.initCredit??>
                                <#assign totalInitCredit=totalInitCredit+item.initCredit>
                            </#if>
                            <span class="color-blue">${course.initCredit!}</span>
                        </#if>
                    </td>
                    <#else >
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </#if>
                <#else >
                    <td>
                        <span class="color-blue">${totalScore!}</span>
                    </td>
                    <td><span class="color-blue">${course.initCredit!}</span></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </#if>
            </tr>
        </#list>
        </tbody>
    </table>
<#else >
    <div class="no-data-container">
        <div class="no-data">
                                        <span class="no-data-img">
                                            <img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
                                        </span>
            <div class="no-data-body">
                <p class="no-data-txt">暂无数据</p>
            </div>
        </div>
    </div>
</#if>
<script>
    $(function() {
        if ($(".js-table-scroll").hasClass("dataTable")) {
            $(".js-table-scroll")
                    .dataTable()
                    .fnClearTable(); //清空数据.fnClearTable();//清空数据
            $(".js-table-scroll")
                    .dataTable()
                    .fnDestroy(); //还原初始化了的datatable
        }
        <#if studentList??&&studentList?size gt 0>
        // 通过js添加table水平垂直滚动条
        page_dt = null;
        var page_dt = $(".js-table-scroll").DataTable({
            // 设置垂直方向高度
            //			      scrollY: 130,
            // scrollY与scrollCollapse同时使用时，scrollY相当于max-height
            // scrollY与scrollCollapse不同时使用时，scrollY相当于height
            //			      scrollCollapse: true,
            // 设置水平方向滚动条
            scrollX: true,
            // 禁用搜索
            searching: false,
            distroy:true,
            // 禁用排序
            ordering: false,
            // 禁止表格分页
            paging: false,
            // 禁止宽度自动
            autoWidth: false,
            info: false,
            language: {
                "sZeroRecords": "暂无相关数据"
            }
        });
         new $.fn.dataTable.FixedColumns( page_dt , {
             leftColumns: 3
         });
        </#if>
    });
</script>