<script src="${request.contextPath}/static/components/html5shiv/dist/html5shiv.min.js"></script>
<script src="${request.contextPath}/static/components/respond/dest/respond.min.js"></script>
<div>
    <button class="btn btn-blue mr10 font-14" onclick="showpdf()">
        导出PDF
    </button>
</div>
<div class="evaluate-item mt10">
    <div class="filter">
        <div class="filter-item">
            <span class="filter-name">学年：</span>
            <div class="filter-content">
                <select name="" id="acadyear" class="form-control evaluate-control" onchange="searchStuReport()">
                    <#if acadyearList?exists && (acadyearList?size>0)>
                        <#list acadyearList as item>
                            <option <#if acadyear?default("")==item>selected</#if>>${item!}</option>
                        </#list>
                    </#if>
                </select>
            </div>
        </div>
        <div class="filter-item">
            <span class="filter-name">学期：</span>
            <div class="filter-content">
                <select name="" id="semester" class="form-control evaluate-control" onchange="searchStuReport()">
                    <option <#if semester?default("")=="1">selected</#if> value="1">第一学期</option>
                    <option <#if semester?default("")=="2">selected</#if> value="2">第二学期</option>
                </select>
            </div>
        </div>
    </div>
</div>
<div class="evaluate-item" id="eva-inquiry-box">
    <table class="table table-bordered eva-inquiry-table">
        <thead>
        <tr>
            <th colspan="8">学生综合信息</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td width="80">姓名</td>
            <td width="160">${student.studentName!}</td>
            <td width="80">性别</td>
            <td width="160">${mcodeSetting.getMcode("DM-XB","${student.sex!}")}</td>
            <td width="80">学号</td>
            <td width="160">${student.studentCode!}</td>
            <td width="80">班级</td>
            <td width="160">${className!}</td>
        </tr>
        </tbody>
    </table>

    <table class="table table-bordered eva-inquiry-table">
        <thead>
        <tr>
            <th colspan="6">道德与法治（阳光德育）</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td width="80">学科</td>
            <td width="150">评价内容</td>
            <td width="80">评价项得分</td>
            <td width="80">日常水平</td>
            <td width="80">期末水平</td>
            <td width="80">综合水平</td>
        </tr>
        <#if hasStatDaoList?exists && hasStatDaoList?size gt 0 && itemIdOptionListMap?exists>
            <#list hasStatDaoList as item>
                <#if itemIdOptionListMap[item.id]?exists>
                    <#assign optionList=itemIdOptionListMap[item.id]>
                    <#list optionList as option>
                        <tr>
                            <#if option_index==0><td rowspan="${optionList?size}">${item.itemName!}</td></#if>
                            <td>${option.optionName!}</td>
                            <td>
                                <ul class="starul" data-score="${optionIdScoreMap[option.id]?default(0.0)}">
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                </ul>
                            </td>
                            <#if option_index==0>
                                <td rowspan="${optionList?size}">
                                    <ul class="starul" data-score="${item.dayScore?default(0.0)}">
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                    </ul>
                                </td>
                                <td rowspan="${optionList?size}">
                                    <ul class="starul" data-score="${item.result?default(0.0)}">
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                    </ul>
                                </td>
                                <td rowspan="${optionList?size}">
                                    <ul class="starul" data-score="${item.avgScore?default(0.0)}">
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                    </ul>
                                </td>
                            </#if>
                        </tr>
                    </#list>
                </#if>
            </#list>
        </#if>
        </tbody>
    </table>

    <table class="table table-bordered eva-inquiry-table">
        <thead>
        <tr>
            <th colspan="6">基础课程</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td width="80">学科</td>
            <td width="150">评价内容</td>
            <td width="80">评价项得分</td>
            <td>日常水平</td>
            <td width="80">期末水平</td>
            <td width="80">综合水平</td>
        </tr>
        <#if hasStatSubList?exists && hasStatSubList?size gt 0 && itemIdOptionListMap?exists>
            <#list hasStatSubList as item>
                <#if itemIdOptionListMap[item.id]?exists>
                    <#assign optionList=itemIdOptionListMap[item.id]>
                    <#assign dataValue="">
                    <#assign dataXValue="">
                    <#assign optionNum=1>
                    <#list optionList as option>
                        <#if optionIdScoreMap[option.id]?exists>
                            <#assign dataValue+=(optionIdScoreMap[option.id]?default(0.0)+",")>
                            <#assign dataXValue+=optionNum+",">
                            <#assign optionNum++>
                        </#if>
                    </#list>
                    <#list optionList as option>
                        <tr>
                            <#if option_index==0><td rowspan="${optionList?size}">${item.itemName!}</td></#if>
                            <td>${option.optionName!}</td>
                            <td>
                                <ul class="starul" data-score="${optionIdScoreMap[option.id]?default(0.0)}">
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                </ul>
                            </td>

                            <#if option_index==0>
                                <td rowspan="${optionList?size}" style="position: relative;">
                                    <div class="eva-inquiry-linechartlayer-box">
                                        <div class="eva-inquiry-linechartlayer" id="linechart${item_index}-2" data-value="${dataValue}" data-xValue="${dataXValue}"></div>
                                    </div>
                                    <div class="eva-inquiry-chart eva-inquiry-linechart" id="linechart${item_index}-1"></div>
                                </td>
                                <td rowspan="${optionList?size}">
                                    <ul class="starul" data-score="${item.result?default(0.0)}">
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                    </ul>
                                </td>
                                <td rowspan="${optionList?size}">
                                    <ul class="starul" data-score="${item.avgScore?default(0.0)}">
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                    </ul>
                                </td>
                            </#if>
                        </tr>
                    </#list>
                </#if>
            </#list>
        </#if>
        </tbody>
    </table>

    <table class="table table-bordered eva-inquiry-table">
        <thead>
        <tr>
            <th>各科目能力分析</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td style="height: 400px;">
                <div class="eva-inquiry-chart" id="mychart1"></div>
            </td>
        </tr>
        </tbody>
    </table>

    <table class="table table-bordered eva-inquiry-table">
        <thead>
        <tr>
            <th colspan="4">拓展课程</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <#if hasStatOneList?exists && hasStatOneList?size gt 0>
                <#list hasStatOneList as item>
                    <td>${item.itemName!}</td>
                    <td>
                        <ul class="starul" data-score="${itemIdScoreMap[item.id]?default(0.0)}">
                            <li class="emptyStar"></li>
                            <li class="emptyStar"></li>
                            <li class="emptyStar"></li>
                            <li class="emptyStar"></li>
                            <li class="emptyStar"></li>
                        </ul>
                    </td>
                </#list>
            </#if>
        </tr>
        </tbody>
    </table>

    <table class="table table-bordered eva-inquiry-table">
        <thead>
        <tr>
            <th colspan="4">品德行为</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td width="80">评价项目</td>
            <td width="150">评价内容</td>
            <td width="80">评价项得分</td>
            <td width="80">日常水平</td>
        </tr>
        <#if noStatItemList?exists && noStatItemList?size gt 0 && itemIdOptionListMap?exists>
            <#list noStatItemList as item>
                <#if itemIdOptionListMap[item.id]?exists>
                    <#assign optionList=itemIdOptionListMap[item.id]>
                    <#assign avgScore=0.0>
                    <#assign l=0>
                    <#list optionList as option>
                        <#if optionIdScoreMap[option.id]?exists>
                            <#assign avgScore+=optionIdScoreMap[option.id]?default(0.0)>
                            <#assign l++>
                        </#if>
                    </#list>
                    <#list optionList as option>
                        <tr>
                            <#if option_index==0><td rowspan="${optionList?size}" >${item.itemName!}</td></#if>
                            <td>${option.optionName!}</td>
                            <td>
                                <ul class="starul" data-score="${optionIdScoreMap[option.id]?default(0.0)}">
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                </ul>
                            </td>
                            <#if option_index==0>
                                <td rowspan="${optionList?size}">
                                    <ul class="starul" data-score="<#if l==0>0<#else>${(avgScore/l)?default(0.0)}</#if>">
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                        <li class="emptyStar"></li>
                                    </ul>
                                </td>
                            </#if>
                        </tr>
                    </#list>
                </#if>
            </#list>
        </#if>
        </tbody>
    </table>

    <table class="table table-bordered eva-inquiry-table">
        <thead>
        <tr>
            <th colspan="2">兴趣特长（拓展课程记录）</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>综合实践活动</td>
            <td>
                ${itemHobbyName!}
            </td>
        </tr>
        </tbody>
    </table>

    <table class="table table-bordered eva-inquiry-table">
        <thead>
        <tr>
            <th colspan="4">身体素质发展水平</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>项目</td>
            <td>本学期达标标准</td>
            <td>本学期评测结果</td>
            <td>上学期评测结果</td>
        </tr>
        <#if healthList?exists && healthList?size gt 0>
            <#list healthList as health>
                <tr>
                    <td>${health.healthName!}</td>
                    <td><#if healthIdOptionMap?exists&&healthIdOptionMap[health.id]?exists>${healthIdOptionMap[health.id].healthStandard!}</#if></td>
                    <td><#if healthIdResultMap?exists&& healthIdResultMap[health.id]?exists>${healthIdResultMap[health.id].healthResult!}</#if></td>
                    <td><#if beHealthIdResultMap?exists&&beHealthIdResultMap[health.id]?exists>${beHealthIdResultMap[health.id].healthResult!}</#if></td>
                </tr>
            </#list>
        </#if>
        </tbody>
    </table>

    <table class="table table-bordered eva-inquiry-table">
        <tbody>
        <tr>
            <td class="eva-inquiry-th">本学期获奖次数</td>
            <td>${myRewardSize!}</td>
        </tr>
        <tr>
            <td class="eva-inquiry-th">班主任寄语</td>
            <td>${teacherContent!}</td>
        </tr>
        <tr>
            <td class="eva-inquiry-th">家长寄语</td>
            <td></td>
        </tr>
        <tr>
            <td class="eva-inquiry-th">学校公告</td>
            <td>${notice!}</td>
        </tr>
        </tbody>
    </table>
</div>
<div class="em-stamp-box" id="mypdfshow" style="display: none;">
        <div class="em-stamp-title">
            ${student.studentName!}综合评价报告单
        </div>
        <div class="em-stamp-twotitle">${acadyear!}学年 第${semester!}学期</div>
        <div class="em-stamp-sort clearfix">
            <div><span>学校：</span> ${schoolName!}</div>
            <div><span>班级：</span> ${className!}</div>
            <div><span>姓名：</span> ${student.studentName!}</div>
            <div><span>性别：</span> ${mcodeSetting.getMcode("DM-XB","${student.sex!}")}</div>
        </div>
        <div>
            <table class="table table-bordered stamp-table stamp-min-table">
                <thead>
                <tr>
                    <th>评价项目</th>
                    <th>评价内容</th>
                    <th>项目得分</th>
                    <th>综合得分</th>
                </tr>
                </thead>
                <tbody>
                <#if noStatItemList?exists && noStatItemList?size gt 0 && itemIdOptionListMap?exists>
                    <#list noStatItemList as item>
                        <#if itemIdOptionListMap[item.id]?exists>
                            <#assign optionList=itemIdOptionListMap[item.id]>
                            <#assign avgScore=0.0>
                            <#assign l=0>
                            <#list optionList as option>
                                <#if optionIdScoreMap[option.id]?exists>
                                    <#assign avgScore+=optionIdScoreMap[option.id]?default(0.0)>
                                    <#assign l++>
                                </#if>
                            </#list>
                            <#list optionList as option>
                                <tr>
                                    <#if option_index==0><td rowspan="${optionList?size}" class="stampth">${item.itemName!}</td></#if>
                                    <td>${option.optionName!}</td>
                                    <td>
                                        <ul class="starul" data-score="${optionIdScoreMap[option.id]?default(0.0)}">
                                            <li class="emptyStar"></li>
                                            <li class="emptyStar"></li>
                                            <li class="emptyStar"></li>
                                            <li class="emptyStar"></li>
                                            <li class="emptyStar"></li>
                                        </ul>
                                    </td>
                                    <#if option_index==0>
                                        <td rowspan="${optionList?size}">
                                            <ul class="starul" data-score="<#if l==0>0<#else>${(avgScore/l)?default(0.0)}</#if>">
                                                <li class="emptyStar"></li>
                                                <li class="emptyStar"></li>
                                                <li class="emptyStar"></li>
                                                <li class="emptyStar"></li>
                                                <li class="emptyStar"></li>
                                            </ul>
                                        </td>
                                    </#if>
                                </tr>
                            </#list>
                        </#if>
                    </#list>
                </#if>
                </tbody>
            </table>
            <table class="table table-bordered stamp-table stamp-table-two stamp-min-table">
                <tbody>
                <tr>
                    <td rowspan="2" class="stampth mycenter" style="width: 50px;">综合实践活动</td>
                    <td class="stampth mycenter">兴趣特长（拓展课程记录）</td>
                </tr>
                <tr>
                    <td style="height: 60px">${itemHobbyName!}</td>
                </tr>
                </tbody>
            </table>
            <table class="table table-bordered stamp-table stamp-table-two">
                <tbody>
                <tr>
                    <td class="stampth">出勤记录</td>
                    <td><#--本学期总天数（ ），-->病假（${sickLeave!} ）天，事假（${casualLeave!} ）天，其他请假（${otherLeave!} ）天</td>
                </tr>
                </tbody>
            </table>
            <table class="table table-bordered stamp-table stamp-table-two stamp-min-table">
                <thead>
                <tr>
                    <th class="mycenter" colspan="11">身体素质发展水平</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="stamptwoth mycenter" style="width: 95px;">项目</td>
                    <#if healthList?exists && healthList?size gt 0>
                        <#list healthList as health>
                            <td class="stamptwoth" <#if health.healthName=="视力">style="width: 65px;font-size: 12px;" <#else> style="font-size: 12px;"</#if>>${health.healthName!}</td>
                        </#list>
                    </#if>
                </tr>
                <tr>
                    <td class="stamptwoth">达标参考</td>
                    <#if healthList?exists && healthList?size gt 0>
                        <#list healthList as health>
                            <td style="height: 50px;"><#if healthIdOptionMap?exists && healthIdOptionMap[health.id]?exists>${healthIdOptionMap[health.id].healthStandard!}</#if></td>
                        </#list>
                    </#if>
                </tr>
                <tr>
                    <td class="stamptwoth">评测结果</td>
                    <#if healthList?exists && healthList?size gt 0>
                        <#list healthList as health>
                            <td  style="height: 50px;"><#if healthIdResultMap?exists && healthIdResultMap[health.id]?exists>${healthIdResultMap[health.id].healthResult!}</#if></td>
                        </#list>
                    </#if>
                </tr>
                <tr>
                    <td class="stamptwoth mycenter">上学期情况</td>
                    <#if healthList?exists && healthList?size gt 0>
                        <#list healthList as health>
                            <td  style="height: 50px;"><#if beHealthIdResultMap?exists && beHealthIdResultMap[health.id]?exists>${beHealthIdResultMap[health.id].healthResult!}</#if></td>
                        </#list>
                    </#if>
                </tr>
                </tbody>
            </table>
            <table class="table table-bordered stamp-table stamp-table-two stamp-min-table">
                <tbody>
                <tr style="height: 130px;">
                    <td class="stamptwoth mycenter" style="width: 80px;">班主任寄语</td>
                    <td>${teacherContent!}</td>
                </tr>
                <tr style="height: 70px;">
                    <td class="stamptwoth mycenter" style="width: 80px;" >家长寄语</td>
                    <td></td>
                </tr>
                <tr style="height: 70px;">
                    <td class="stamptwoth mycenter" style="width: 80px;">写给自己的话</td>
                    <td></td>
                </tr>
                <tr style="height: 70px;">
                    <td class="stamptwoth mycenter" style="width: 80px;">学校公告</td>
                    <td>${notice!}
                    </td>
                </tr>
                </tbody>
            </table>
            <div class="em-stamp-sort clearfix" style="padding-top: 10px;padding-bottom: 60px;">
                <div style="width: 33%;"><span>校长：${schoolmaster!}</span></div>
                <div style="width: 33%;"><span>教导主任：</span>${gradeTeacherName!}</div>
                <div style="width: 33%;"><span>班主任：</span>${teacherName!}</div>
            </div>
            <table class="table table-bordered stamp-table stamp-nopadd-table">
                <thead>
                <tr>
                    <th style="width: 100px;">学科</th>
                    <th style="width: 140px;">项目评价内容</th>
                    <th>项目得分</th>
                    <th>项目日常水平</th>
                    <th>项目期末水平</th>
                    <th>项目综合水平</th>
                </tr>
                </thead>
                <tbody>
                <#if hasStatMoreList?exists && hasStatMoreList?size gt 0 && itemIdOptionListMap?exists>
                    <#list hasStatMoreList as item>
                        <#if itemIdOptionListMap[item.id]?exists>
                            <#assign optionList=itemIdOptionListMap[item.id]>
                            <#list optionList as option>
                                <tr>
                                    <#if option_index==0><td rowspan="${optionList?size}" class="stampth">${item.itemName!}</td></#if>
                                    <td>${option.optionName!}</td>
                                    <td>
                                        <ul class="starul" data-score="${optionIdScoreMap[option.id]?default(0.0)}">
                                            <li class="emptyStar"></li>
                                            <li class="emptyStar"></li>
                                            <li class="emptyStar"></li>
                                            <li class="emptyStar"></li>
                                            <li class="emptyStar"></li>
                                        </ul>
                                    </td>
                                    <#if option_index==0>
                                        <td rowspan="${optionList?size}">
                                            <ul class="starul" data-score="${item.dayScore?default(0.0)}">
                                                <li class="emptyStar"></li>
                                                <li class="emptyStar"></li>
                                                <li class="emptyStar"></li>
                                                <li class="emptyStar"></li>
                                                <li class="emptyStar"></li>
                                            </ul>
                                        </td>
                                        <td rowspan="${optionList?size}">
                                            <ul class="starul" data-score="${item.result?default(0.0)}">
                                                <li class="emptyStar"></li>
                                                <li class="emptyStar"></li>
                                                <li class="emptyStar"></li>
                                                <li class="emptyStar"></li>
                                                <li class="emptyStar"></li>
                                            </ul>
                                        </td>
                                        <td rowspan="${optionList?size}">
                                            <ul class="starul" data-score="${item.avgScore?default(0.0)}">
                                                <li class="emptyStar"></li>
                                                <li class="emptyStar"></li>
                                                <li class="emptyStar"></li>
                                                <li class="emptyStar"></li>
                                                <li class="emptyStar"></li>
                                            </ul>
                                        </td>
                                    </#if>
                                </tr>
                            </#list>
                        </#if>
                    </#list>
                </#if>
                <#if hasStatOneList?exists && hasStatOneList?size gt 0>
                    <#list hasStatOneList as item>
                        <tr>
                            <td class="stampth mycenter">${item.itemName!}</td>
                            <td>${item.shortName!}</td>
                            <td>
                                <ul class="starul" data-score="${itemIdScoreMap[item.id]?default(0.0)}">
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                    <li class="emptyStar"></li>
                                </ul>
                            </td>
                            <#if item_index==0>
                                <td rowspan="${hasStatOneList?size+5}" colspan="3">
                                    <div class="stamp-sum-box clearfix">
                                        <div class="stamp-sum-left">
                                            <div class="stampth">学分汇总</div>
                                            <div class="stamp-bai-box" data-score="${(goodStat.allScore?default(0.0)*20)}">
                                                <div class="stamp-bai-item">
                                                    <div class="stamp-bai-min active"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                </div>
                                                <div class="stamp-bai-item">
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                </div>
                                                <div class="stamp-bai-item">
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                </div>
                                                <div class="stamp-bai-item">
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                </div>
                                                <div class="stamp-bai-item">
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                </div>
                                                <div class="stamp-bai-item">
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                </div>
                                                <div class="stamp-bai-item">
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                </div>
                                                <div class="stamp-bai-item">
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                </div>
                                                <div class="stamp-bai-item">
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                </div>
                                                <div class="stamp-bai-item">
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                    <div class="stamp-bai-min"></div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="stamp-sum-right" id="mychartPdf" ></div>
                                    </div>
                                </td>
                            </#if>
                        </tr>
                    </#list>
                </#if>
                <tr>
                    <td class="stampth mycenter" rowspan="4">
                        获奖情况
                    </td>
                    <td>班级</td>
                    <td>
                        <#if number1?exists && number1 gt 0>
                            <ul class="starul allstarul" data-score="${number1!}">
                                <#if number1 gt 5>
                                    <li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li>
                                    <li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li>
                                <#else >
                                    <li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li>
                                </#if>
                            </ul>
                        </#if>
                    </td>
                </tr>
                <tr>
                    <td>校级</td>
                    <td>
                        <#if number2?exists && number2 gt 0>
                            <ul class="starul allstarul" data-score="${number2!}">
                                <#if number2 gt 5>
                                    <li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li>
                                    <li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li>
                                <#else >
                                    <li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li>
                                </#if>
                            </ul>
                        </#if>
                    </td>
                </tr>
                <tr>
                    <td>市级</td>
                    <td>
                        <#if number3?exists && number3 gt 0>
                            <ul class="starul allstarul" data-score="${number3!}">
                                <#if number3 gt 5>
                                    <li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li>
                                    <li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li>
                                <#else >
                                    <li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li><li class="emptyStar" ></li>
                                </#if>
                            </ul>
                        </#if>
                    </td>
                </tr>
                <tr <#--style="height: 80px;"-->>
                    <td>合计</td>
                    <td>
                        <#if allNumber?exists && allNumber gt 0>
                            <ul class="starul allstarul" data-score="${allNumber!}">
                                <li class="emptyStar"></li>
                                <li class="emptyStar"></li>
                                <li class="emptyStar"></li>
                                <li class="emptyStar"></li>
                                <li class="emptyStar"></li>
                                <li class="emptyStar"></li>
                                <li class="emptyStar"></li>
                                <li class="emptyStar"></li>
                                <li class="emptyStar"></li>
                                <li class="emptyStar"></li>
                            </ul>
                        </#if>

                    </td>
                </tr>
                <tr style="height: 80px;">
                    <td class="stampth mycenter">
                        荣誉称号
                    </td>
                    <td colspan="2"><#if goodStat?exists && goodStat.haveGood?default(0)==1>全优生</#if></td>
                </tr>
                </tbody>
            </table>
        </div>
</div>
<div class="chart-modal">
    <div id="" style="height: 100%;"></div>
</div>
<script src="${request.contextPath}/static/components/echarts/echarts.min.js"></script>
<!--转pdf-->
<script src="${request.contextPath}/static/components/jspdf/html2canvas.js"></script>
<script src="${request.contextPath}/static/components/jspdf/jspdf.min.js"></script>

<script type="text/javascript">

    var arr = []
    function resizeChart() {
        for (var i = 0; i < arr.length; i++) {
            arr[i].resize()
        }
    }
    //窗口变化，图表resize
    $(window).resize(function() {
        resizeChart()
    })
    $(function() {
        initstar()
        $(".stamp-bai-item").each(function(i, v) {
            $(v).css("width", i * 8 + 10);
        });
        $(".stamp-bai-min").removeClass("active");
        $(".stamp-bai-min").each(function(i, v) {
            if (i < parseInt($(".stamp-bai-box").attr("data-score"))) {
                $(v).addClass("active");
            } else {
                return;
            }
        });
        <#if hasStatMoreList?exists && hasStatMoreList?size gt 0>
        var myChart1 = echarts.init(document.getElementById('mychart1'))
        arr.push(myChart1)
        var option1 = {
            tooltip: {},
            color: ['#87B5E6'],
            radar: {
                indicator: [

                    <#list hasStatMoreList as item>
                    { name: '${item.itemName!}', max: 5 },
                    </#list>
                ],
                name: {
                    textStyle: {
                        color: '#333',
                    },
                },
            },
            series: [
                {
                    type: 'radar',
                    areaStyle: { normal: { opacity: 0.2 } },
                    data: [
                        {
                            value: [
                                <#list hasStatMoreList as item>
                                <#if item_index!=0>,</#if>${(item.avgScore?default(0))?string("#.#")}
                                </#list>
                            ],
                            name: '学科成绩',
                            label: {
                                normal: {
                                    show: true,
                                    formatter: function(params) {
                                        return params.value
                                    },
                                },
                            },
                        },
                    ],
                },
            ],
        }
        myChart1.setOption(option1)
        </#if>
        $('.eva-inquiry-linechart').hover(
            function() {
                $(this).siblings('.eva-inquiry-linechartlayer-box').css('display', 'block')
            },
            function() {
                $(this).siblings('.eva-inquiry-linechartlayer-box').css('display', 'none')
            }
        )

        $('.eva-inquiry-linechartlayer').each(function(i, e) {
            var value=$('#linechart' + i + '-2').attr("data-value");
            var xValue=$('#linechart' + i + '-2').attr("data-xValue");
            if(xValue && xValue.length>0){
                var values=value.split(",");
                var xValues=xValue.split(",");
                var layerChart = echarts.init(document.getElementById('linechart' + i + '-2'))
                var layeroption = {
                    color: ['#87B5E6'],
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: xValues,
                        axisLabel: {
                            color: '#ddd',
                        },
                        axisLine: {
                            lineStyle: {
                                color: '#ddd',
                            },
                        },
                        axisTick: {
                            show: false,
                        },
                        splitLine: {
                            lineStyle: {
                                color: '#ddd',
                            },
                        },
                    },
                    yAxis: {
                        name: '星级',
                        type: 'value',
                        axisLabel: {
                            color: '#ddd',
                        },
                        axisLine: {
                            lineStyle: {
                                color: '#ddd',
                            },
                        },
                        axisTick: {
                            show: false,
                        },
                        splitLine: {
                            show: false,
                        },
                    },
                    series: [
                        {
                            data: values,
                            type: 'line',
                            areaStyle: {
                                normal: {
                                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                                        {
                                            offset: 0,
                                            color: '#317eeb',
                                        },
                                        {
                                            offset: 1,
                                            color: '#E7F3FC',
                                        },
                                    ]),
                                },
                            },
                            markLine: {
                                data: [{ type: 'average', name: '平均值' }],
                            },
                        },
                    ],
                }
                layerChart.setOption(layeroption)
            }
        })

        $('.eva-inquiry-linechart').each(function(i, e) {
            var value=$('#linechart' + i + '-2').attr("data-value");
            var xValue=$('#linechart' + i + '-2').attr("data-xValue");
            if(xValue && xValue.length>0) {
                var values=value.split(",");
                var xValues=xValue.split(",");
                var mylineChart = echarts.init(document.getElementById('linechart' + i + '-1'))
                arr.push(mylineChart)
                var lineoption = {
                    color: ['#87B5E6'],
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: xValues,
                        axisLabel: {
                            show: false,
                        },
                        axisLine: {
                            show: false,
                        },
                        axisTick: {
                            show: false,
                        },
                        splitLine: {
                            show: false,
                        },
                    },
                    yAxis: {
                        type: 'value',
                        axisLabel: {
                            show: false,
                        },
                        axisLine: {
                            show: false,
                        },
                        axisTick: {
                            show: false,
                        },
                        splitLine: {
                            show: false,
                        },
                    },
                    series: [
                        {
                            data: values,
                            type: 'line',
                            areaStyle: {
                                normal: {
                                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                                        {
                                            offset: 0,
                                            color: '#317eeb',
                                        },
                                        {
                                            offset: 1,
                                            color: '#E7F3FC',
                                        },
                                    ]),
                                },
                            },
                            markLine: {
                                data: [{ type: 'average', name: '平均值' }],
                            },
                        },
                    ],
                }
                mylineChart.setOption(lineoption)
            }
        })
    })
    function closeinput(that) {
        $(that).siblings('.form-control').val('')
        $(that).siblings('.form-control').change()
    }
    //星星初始化
    function initstar() {
        $(".starul").each(function() {
            var isall = false;
            if($(this).hasClass("allstarul")){
                isall = true;
            }
            var score = $(this).attr("data-score") * 2;
            $(this).siblings(".star-tip").html($(this).attr("data-score") + "星");
            $(this).find("li").each(function(i, e) {
                if (2 * i + 2 <= score) {
                    $(e).attr("class", "fullStar");
                } else if (2 * i + 1 <= score) {
                    $(e).attr("class", "halfStar");
                } else {
                    if(isall) {
                        $(e).attr("class", "emptyStarnew");
                    }
                    else{
                        $(e).attr("class", "emptyStar");
                    }
                }
            });
        });
    }

    function showpdf() {
        /*html2canvas生成图片，jspdf生成PDF文件*/
        $("#mypdfshow").css("display","block");
        <#if hasStatMoreList?exists && hasStatMoreList?size gt 0>
            var mychartPdf = echarts.init(document.getElementById("mychartPdf"));
            var optionPdf = {
                tooltip: {},
                color: ["#f10215"],
                radar: {
                    indicator: [
                        <#list hasStatMoreList as item>
                        { name: '${item.itemName!}', max: 5 },
                        </#list>
                    ],
                    name: {
                        textStyle: {
                            color:'#333'
                        }
                    },
                },
                series: [
                    {
                        type: "radar",
                        areaStyle: { normal: {opacity:0.2} },
                        data: [
                            {
                                value: [
                                    <#list hasStatMoreList as item>
                                    <#if item_index!=0>,</#if>${(item.avgScore?default(0))?string("#.#")}
                                    </#list>
                                ],
                                name: "学生测评",
                                label: {
                                    normal: {
                                        show: true,
                                        formatter: function(params) {
                                            return params.value;
                                        }
                                    }
                                },
                            }
                        ]
                    }
                ]
            };
            mychartPdf.setOption(optionPdf);
        </#if>
        setTimeout(function(){
            html2canvas($("#mypdfshow"), {
                background: '#fff',
                allowTaint: true,
                taintTest: false,
                onrendered: function(canvas) {
                    var contentWidth = canvas.width
                    var contentHeight = canvas.height
                    var pageHeight = (contentWidth / 572.28) * 821.89
                    var leftHeight = contentHeight
                    var position = 0
                    var imgWidth = 575.28
                    var imgHeight = (572.28 / contentWidth) * contentHeight
                    var pageData = canvas.toDataURL('image/jpeg', 1.0)
                    var img = new Image()
                    img.src = pageData
                    var pdf = new jsPDF('p', 'pt', 'a4')
                    img.onload = function() {
                        if (leftHeight < pageHeight) {
                            pdf.addImage(pageData, 'JPEG', 10, 0, imgWidth, imgHeight)
                        } else {
                            while (leftHeight > 0) {
                                pdf.addImage(pageData, 'JPEG', 10, position, imgWidth, imgHeight)
                                leftHeight -= pageHeight
                                position -= 841.89
                                if (leftHeight > 0) {
                                    pdf.addPage()
                                }
                            }
                        }
                        pdf.save('${student.studentName!}综合评价报告单.pdf')
                        $("#mypdfshow").css("display","none");
                    }
                },
            })
        },1000)
    }
    function searchStuReport() {
        var studentId = "${student.id!}";
        var acadyear = $("#acadyear").val();
        var semester = $("#semester").val();
        var url = '${request.contextPath}/stutotality/report/common/getStuTotalityReportPdf?studentId=' + studentId+"&acadyear="+acadyear+"&semester="+semester;
        $("#showTabDiv").load(url);
    }
</script>
