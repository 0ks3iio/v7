<title>任职情况登记List</title>
<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-wrapper">
    <table class="table table-bordered table-striped table-hover no-margin">
        <thead>
        <tr>
            <th style="width:5%"  rowspan="2" class="text-center">序号</th>
            <th style="width:10%" rowspan="2" class="text-center" > 班级名称</th>
            <th style="width:25%" <#if months?exists> colspan="${months?size}"  </#if> class="text-center" >在校表现</th>
            <th style="width:7%" rowspan="2" class="text-center"  >班级活动</th>
            <th style="width:7%" rowspan="2" class="text-center" >主题活动</th>
            <th style="width:7%" rowspan="2"  class="text-center" >班级荣誉</th>
            <th style="width:9%" rowspan="2" class="text-center"  >家长参与数</th>
            <th style="width:15%" rowspan="2" class="text-center"  >家长上传图片数</th>
            <th style="width:15%" rowspan="2" class="text-center" >家长上传平均数</th>
        </tr>

            <#if months?exists && months?size gt 0 >
            <tr>
                <#list months as month>
                <th class="text-center" >${month?number}月</th>
                </#list>
            </tr>
            </#if>

        </thead>
        <tbody>
        <#if gradeStatisDtos?exists && (gradeStatisDtos?size > 0)>
            <#list gradeStatisDtos as dto>
            <tr>
                <td class="text-center"  >${dto_index +1}</td>
                <td class="text-center"  >${dto.className!}</td>
                <#if months?exists && months?size gt 0 >
                    <#list months as month>
                        <#if peopleNumMap[dto.classId + '_' + month?number]?exists>
                            <td class="text-center"  >${peopleNumMap[dto.classId + '_' + month?number]}人</td>
                            <#else>
                                <td>0人</td>
                        </#if>
                    </#list>
                </#if>
                <td class="text-center"  >${dto.classActivity?default('0个(0张)')}</td>
                <td class="text-center"  >${dto.themeActivity?default('0个(0张)')}</td>
                <td class="text-center"  >${dto.classHonor?default('0张')}</td>
                <td class="text-center"  >${dto.parentJoin!}</td>
                <td class="text-center"  >${dto.parentSubmitPic!}张</td>
                <td class="text-center"  >${dto.parentSubmitPicAve?default('0')}</td>
            </tr>
            </#list>
        </#if>
        </tbody>
    </table>
</div>

