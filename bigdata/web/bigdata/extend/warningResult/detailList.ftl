<#list warningResults as item>
    <li class="clearfix">
        <div class="col-xs-3 data-left">
            <p>${item.warnDate!}
                <#if item.isRead == 0 >
                <span class="badge badge-red">new</span>
                </#if>
            </p>
        </div>
        <div class="col-xs-9 data-right">
            <p><b>${item.tips!}</b></p>
            <p>${item.remark!}</p>
        </div>
    </li>
</#list>
<#if hasNextPage == true>
    <div class="text-center" style="margin-top: 20px" >
        <a href="javascript:void(0);" onclick="detailList('${page.pageIndex + 1}');" id="moreDetail">查看更多 > ></a>
    </div>
</#if>