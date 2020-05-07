<style>
    /*.table-bordered > tbody > tr > td{border-width: 2px;}*/
    .table > tbody > tr > td{line-height: 1.0; border: 1px solid #333;}
</style>
                    <div class="box box-default">
                        <div class="box-body">
                            <p class="text-center" style="font-size: 28px;" ><b>${acadyear!}学年${mcodeSetting.getMcode("DM-XQ",semester?default('1'))}学生综合素质报告单</b></p>
                            <p class="report-inputs"><span><b>班级：${className!}</b></span><span><b>&nbsp;&nbsp;学生姓名：${student.studentName!}</b></span></p>
                            <table class="table  table-print text-center no-margin">

                                <tbody>
                                <tr>
                                    <td width="11%"><b>素质</b></td>
                                    <td width="27%"><b>思想道德素质</b></td>
                                    <#if templateItemList?exists && templateItemList?size gt 0>
                                        <#assign  subjectSize = templateItemList?size +2 >
                                    </#if>
                                    <td width="43%" colspan="${subjectSize?default(2)}"><b>科学文化素质</b></td>
                                    <td width="19%" colspan="2"><b>身心健康素质</b></td>
                                </tr>
                                <#assign totalCols = 2+subjectSize?default(5)+2 >
                                <tr>
                                    <td><b>操行<br/>等级</b></td>
                                    <td><b>${stuEvaluateRecord.evaluateLevel?default('')}</b></td>
                                    <td width="7%"><b>学科</b></td><#--1-->
                                    <td width="11%"><b>项目</b></td><#--1-->
                                    <#if templateItemList?exists && templateItemList?size gt 0>
                                        <#list templateItemList as item>
                                            <td style="word-break: break-all;word-wrap: break-word;"  <#if item_index==templateItemList?size-1>width="11%"<#else>width="7%"</#if> ><b>${item.itemName!}</b></td>
                                        </#list><#--1-->
                                    </#if>
                                    <td ><b>项目</b></td>
                                    <td width="7%"><b>等第</b></td>
                                </tr>
                                    <#assign ind=0 />
                                    <#assign healthIndex=0 />
                                    <#if subjectList?exists && subjectList?size gt 0 >
                                    <#list  subjectList as subject >
                                        <#assign flag=true />
                                        <#if subject.cateGoryList?exists && subject.cateGoryList?size gt 0>
                                            <#assign categoryList = subject.cateGoryList />
                                            <#assign categorySize = (subject.cateGoryList?size)?default(0) />
                                            <#list categoryList as category>
                                                <tr>
                                                    <#assign ind=ind+1 />
                                                    <#if ind == 1>
                                                        <td rowspan="${((subCategorySize/4)*3)?int}" colspan="2" width="40%" style="vertical-align:-2px;text-align: left;"  ><b>操行评语：</b><br />
                                                            <p style="text-indent: 2em;line-height: 26px;">
                                                            <#if stuEvaluateRecord?exists>
                                                        ${stuEvaluateRecord.teacherEvalContent!}
                                                        </#if>
                                                            </p>
                                                        </td>

                                                    </#if>
                                                    <#if ind == ((subCategorySize/4)*3)?int +1 >
                                                        <td rowspan="${subCategorySize -((subCategorySize/4)*3)?int}" colspan="2" style="vertical-align:-2px;text-align: left;"  ><b>任职及奖惩（争章、争卡情况等）：</b>
                                                            <br />
                                                            <#if dutySituationList?exists && dutySituationList?size gt 0>
                                                                任职：
                                                                <#list dutySituationList as duty>
                                                                     ${duty.dutyName!}
                                                                </#list>
                                                            </#if>
                                                            <#if stuDevelopRewardsList?exists && (stuDevelopRewardsList?size > 0)>
                                                           <br>


                                                            <br/>
                                                            奖励：
                                                            <#list stuDevelopRewardsList as item>
                                                            ${item.rewardsname!}<#if item.remark?default('') != ''>,${item.remark!}</#if>&nbsp;&nbsp;&nbsp;
                                                            </#list>
                                                           </#if>
                                                            <br>
                                                            <#if stuDevelopPunishmentList?exists && (stuDevelopPunishmentList?size > 0)>
                                                                惩罚：
                                                                <#list stuDevelopPunishmentList as item>
                                                                ${item.punishname!} <#if item.remark?default('') != ''>,${item.remark!}</#if>&nbsp;&nbsp;&nbsp;
                                                                </#list>
                                                            </#if>
                                                            </b>
                                                        </td>
                                                    </#if>


                                                    <#if flag?default(false)>
                                                        <#assign flag=false />
                                                        <td style="word-break: break-all;word-wrap: break-word;"  <#if categorySize gt 1 > rowspan="${categorySize}" </#if> ><b>${subject.name!}</b></td>
                                                    </#if>
                                                    <!-- 类别名称 -->
                                                    <td style="word-break: break-all;word-wrap: break-word;"  >${category.categoryName!}</td>
                                                    <#if templateItemList?exists && templateItemList?size gt 0>
                                                        <#list templateItemList as item>
                                                            <#assign key = subject.id +"_"+ category.id +"_"+ item.id  />
                                                            <#if item.objectType == '11'>
                                                                 <td >
                                                                     <#if templateResultMap[key]?exists  >
                                                                         ${templateResultMap[key].result!}
                                                                     </#if>

                                                                 </td>
                                                            <#else>
                                                                <!--  第一行 才会显示 -->
                                                                <#assign key = subject.id  +"_"+ item.id  />
                                                                <#if category_index == 0 >
                                                                    <td style="word-break: break-all;word-wrap: break-word;"  <#if categorySize gt 1 > rowspan="${categorySize}" </#if> >
                                                                        <input type="hidden" value="${item.objectType!}" />
                                                                        <input type="hidden" value="${item.itemName!}" />
                                                                     <#if templateResultMap[key]?exists  >
                                                                         ${templateResultMap[key].result!}
                                                                     </#if>
                                                                    </td>
                                                                </#if>

                                                            </#if>
                                                        </#list>
                                                    </#if>
                                                    <#if ind = healthProjectSize/2+1 >
                                                        <td height="100"  style="vertical-align:-2px;text-align: left;"   rowspan="${subCategorySize -healthProjectSize/2}" colspan="2"><b>个性特点:</b><br >
                                                            <p style="text-indent: 2em;line-height: 26px;">
                                                        <#--<#if studentexDto?exists && '${studentexDto.strong!}' != ''>-->
                                                            <#--<b>${studentexDto.strong!}</b>-->
                                                        <#--</#if>-->
                                                                <#if stuEvaluateRecord?exists>
                                                                ${stuEvaluateRecord.strong!}
                                                                </#if>
                                                            </p>

                                                        </td>
                                                    </#if>

                                                    <#if healthDtoList[(ind-1)*2]?exists >
                                                        <td>${healthDtoList[(ind-1)*2].nameOrValue!}</td>
                                                    </#if>
                                                    <#if healthDtoList[(ind-1)*2+1]?exists >
                                                        <td>${healthDtoList[(ind-1)*2+1].nameOrValue!}</td>
                                                    </#if>
                                                </tr>
                                            </#list>

                                        <#else>
                                        <tr>
                                            <#assign ind=ind+1 />
                                            <#if subject_index == 0>
                                                <td rowspan="${((subCategorySize/4)*3)?int}" colspan="2" width="40%" style="vertical-align:-2px;text-align: left;" ><b>操行评语：</b><br />
                                                    <p style="text-indent: 2em;line-height: 26px;">
                                                    <#if stuEvaluateRecord?exists>
                                                ${stuEvaluateRecord.teacherEvalContent!}
                                                </#if>
                                                    </p>
                                                </td>

                                            </#if>
                                            <#if ind == ((subCategorySize/4)*3)?int +1  >
                                                <td rowspan="${subCategorySize -((subCategorySize/4)*3)?int}" colspan="2"  style="vertical-align:-2px;text-align: left;" ><b>任职及奖惩（争章、争卡情况等）：
                                                    <br>

                                                    <#if dutySituationList?exists && dutySituationList?size gt 0>
                                                        任职：
                                                        <#list dutySituationList as duty>
                                                             ${duty.dutyName!}
                                                        </#list>
                                                    </#if>

                                                    <#if stuDevelopRewardsList?exists && (stuDevelopRewardsList?size > 0)>
                                                    奖励：
                                                    <#list stuDevelopRewardsList as item>
                                                    ${item.rewardsname!}<#if item.remark?default('') != ''>,${item.remark!}</#if>&nbsp;&nbsp;&nbsp;
                                                    </#list>
                                                </#if>
                                                    <br>
                                                    <#if stuDevelopPunishmentList?exists && (stuDevelopPunishmentList?size > 0)>
                                                        惩罚：
                                                        <#list stuDevelopPunishmentList as item>
                                                        ${item.punishname!}<#if item.remark?default('') != ''>,${item.remark!}</#if>&nbsp;&nbsp;&nbsp;
                                                        </#list>
                                                    </#if>
                                                </b>
                                                </td>
                                            </#if>

                                            <td colspan="2"><b>${subject.name!}</b></td>
                                              <#if templateItemList?exists && templateItemList?size gt 0>
                                                  <#list templateItemList as item>
                                                          <#assign key = subject.id  +"_"+ item.id  />
                                                          <td  ><#if templateResultMap[key]?exists  >
                                                              ${templateResultMap[key].result!}
                                                          </#if></td>
                                                  </#list>
                                              </#if>
                                              <#if healthDtoList[(ind-1)*2]?exists >
                                                        <td>${healthDtoList[(ind-1)*2].nameOrValue!}</td>
                                              </#if>
                                                    <#if healthDtoList[(ind-1)*2+1]?exists >
                                                        <td>${healthDtoList[(ind-1)*2+1].nameOrValue!}</td>
                                                    </#if>
                                            <#if ind = healthProjectSize/2+1 >
                                                <td  height="100" rowspan="${subCategorySize -healthProjectSize/2}" colspan="2" style="vertical-align:-2px;text-align: left;"  ><b>个性特点:</b><br>
                                                    <p style="text-indent: 2em;line-height: 26px;">
                                                        <#if stuEvaluateRecord?exists>
                                                            ${stuEvaluateRecord.strong!}
                                                            </#if>
                                                    </p>
                                            </td>
                                            </#if>
                                        </tr>
                                        </#if>

                                    </#list>

                                    </#if>

                                <tr>
                                    <td><b>通知</b></td>
                                    <td colspan="${totalCols!}" class="text-left table-print-textarea" >
                                        <p style="margin:0 10px;width:200px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">
                                            <#if schoolNotice?exists>
                                              ${schoolNotice.notice!}
                                            </#if>
                                        </p>
                                    </td>
                                </tr>
                                <tr>
                                    <td height="50"><b>家长<br/>意见</b></td>
                                    <td colspan="${totalCols!}"></td>
                                </tr>
                                </tbody>
                            </table>
                            <p class="report-inputs text-center">
                                <span><b>班主任：${teacher.teacherName!}</b></span>
                                <span class="ml-15"><b>校长：${school.schoolmaster!}</b></span>
                            </p>
                        </div>
                    </div>
