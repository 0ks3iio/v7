<div class="col-sm-9">
    <div class="box " >
        <div class="box-body" id="print" >
            <div style="background:#fff">

                <p class="text-center" style="font-size: 28px;">
                    ${schName!}${sectionName!}阶段成绩汇总表</p>
                <div class="row" style="font-size: 18px;">
                <span class="col-sm-2">
                    姓名: ${name!}
                </span>
                    <span class="col-sm-2">
                    性别: ${sex!}
                </span>
                    <span class="col-sm-3">
                    班级: ${className!}
                </span>
                    <span class="col-sm-3">
                    学号: ${stuCode!}
                </span>
                </div>
                <table class="table table-print no-margin">
                    <tbody>
                    <tr style="height:20px;"">
                    <#if titleList?? && titleList?size gt 0>
                        <#list titleList as title>
                            <th <#if title_index==0>width="20%"</#if> style="line-height: 1.4;border: 2px solid #333;">${title!}</th>
                        </#list>
                    </#if>
                    </tr>
                    <#if infoList?? && infoList?size gt 0>
                        <#list infoList as subList>
                            <tr style="height:20px;">
                                <#list subList as sub>
                                    <td style="line-height: 1.4;border: 2px solid #333;">${sub!}</td>
                                </#list>
                            </tr>
                        </#list>
                    </#if>

                    </tbody>

                </table>
                <div style="text-align:right;font-size: 20px;">
                    <b> ${schName!} (盖章)</b>
                    <br>
                    <b style="text-align:right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日</b>

                </div>
            </div>

        </div>
    </div>
</div>