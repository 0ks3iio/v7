<#--<div>-->
    <#--<button class="btn btn-blue mr10 font-14" onclick="showpdf()">-->
        <#--导出PDF-->
    <#--</button>-->
<#--</div>-->
<#if itemList?exists&&(itemList?size>0)>
<table class="table table-bordered eva-inquiry-table">
    <thead>
    <tr>
        <th>项目</th>
        <th>评价内容</th>
        <th>项目得分</th>
        <th>综合得分</th>
    </tr>
    </thead>
    <tbody>
       <#list itemList as item>

        <tr>
           <#if optionMap?exists&&(optionMap?size>0)>
               <#if optionMap[item.id]??>
                   <#if optionMap[item.id]?size gt 0>
                       <#assign avgResult=0>
                       <#assign avgSize=0>
                       <#list optionMap[item.id] as item1>
                            <#if item1.classAverageResult?exists>
                                <#assign avgResult=avgResult+item1.classAverageResult>
                                <#assign avgSize=avgSize+1>
                            </#if>
                       </#list>
                       <#list optionMap[item.id] as item1>
                           <#if item1_index==0>
                            <td rowspan=${optionMap[item.id]?size}>
                                <label class="pos-rel">
                                    <span class="lbl">${item.itemName!}</span>
                                </label>
                            </td>
                           </#if>
                        <td>
                            <div>
                                <label class="pos-rel">
                                    <span class="lbl">${item1.optionName!}</span>
                                </label>
                        </td>
                         <td>
                             <label class="pos-rel">
                                 <span class="lbl"><#if item1.classAverageResult?exists>${item1.classAverageResult?string("#.#")}</#if></span>
                             </label>
                         </td>
                           <#if item1_index==0>
                            <td rowspan=${optionMap[item.id]?size}>
                                <label class="pos-rel">
                                    <span class="lbl"><#if avgSize?default(0)!=0>${(avgResult/avgSize)?string("#.#")}</#if></span>
                                </label>
                            </td>
                           </#if>
                    </tr>
                       </#list>
                   <#--<#else >-->
                   <#--<td rowspan="1">-->
                   <#--<label class="pos-rel">-->
                   <#--<span class="lbl">${item.itemName!}</span>-->
                   <#--</label>-->
                   <#--</td>-->
                   <#--<td></td>-->
                   <#--<td></td>-->
                   <#--<td></td>-->
                   <#--</tr>-->
                   </#if>
               </#if>
           </#if>
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