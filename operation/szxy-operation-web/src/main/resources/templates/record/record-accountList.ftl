<#import "../macro/pagination.ftl" as pagination />
<#if pages??>
    <#if pages.content?? && pages.content?size gt 0>
        <table class="table table-bordered table-striped table-hover no-margin">
            <thead>
            <tr>
                <th>序号</th>
                <th width="20%">服务时间</th>
                <th>服务单位</th>
                <th>服务类型</th>
                <th>操作人</th>
                <th>服务内容</th>
            </tr>
            </thead>
            <tbody id="recordAccounts">

            <#list pages.content as recordAccount>
                <tr>
                    <td>${recordAccount_index+1} </td>
                    <td>${recordAccount.operateTime!}</td>
                    <td>${recordAccount.unitName!}</td>
                    <td>${recordAccount.operatorTypeName!}</td>
                    <td>${recordAccount.operatorName!}</td>
                    <td>${recordAccount.operateDetail!}</td>


                </tr>
            </#list>
            </tbody>
        </table>
        <@pagination.paginataion pages=pages containerId='recordAccounts' pageCallFunction='doGetRecordAccounts' />
    <#else >
        <div class="no-data-container">
            <div class="no-data">
            <span class="no-data-img">
                <img src="${springMacroRequestContext.contextPath}/static/images/public/nodata4.png" alt="">
            </span>
                <div class="no-data-body">
                    <p class="no-data-txt">没有相关数据</p>
                </div>
            </div>
        </div>
    </#if>
</#if>
