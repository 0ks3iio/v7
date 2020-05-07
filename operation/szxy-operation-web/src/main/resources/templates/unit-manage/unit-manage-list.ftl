<#import "../macro/pagination.ftl" as pagination />
<#if unitDtos??>
    <#if unitDtos.content?? && unitDtos.content?size gt 0>
        <table class="table table-bordered table-striped table-hover no-margin">
            <thead>
            <tr>
                <th>序号</th>
                <th>单位名称</th>
                <th>单位编号</th>
                <th>行政区</th>
                <th>单位类型</th>
                <th>单位性质</th>
                <th>单位到期时间</th>
                <th>系统到期统计</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="unitAccounts">
            <#list unitDtos.content as unit>
            <tr >
                <td>0</td>
                <td>${unit.unitName!}</td>
                <td>${unit.unionCode}</td>
                <td>${unit.regionName!}</td>
                <td>${unit.unitType!}</td>
                <td>${unit.usingNature!}</td>
                <td>${unit.expireTime!}</td>
                <td>${unit.count!}</td>
                <td></td>
            </tr>
            </#list>
            </tbody>
        </table>
        <@pagination.paginataion pages=unitDtos containerId='unitAccouts' pageCallFunction='doGetUnitAccountList' />
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
<script src="${springMacroRequestContext.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${springMacroRequestContext.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<script src="${springMacroRequestContext.contextPath}/static/components/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js"></script>
<script src="${springMacroRequestContext.contextPath}/static/components/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>