<div id="logListId">
    <#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
    <div class="filter-item filter-item-right">
        <div class="filter-content">
            <div class="input-group input-group-search">
                <select name="searchType" id="logSearchType" class="form-control">
                    <option value="studentCode" <#if searchType?default("")=="studentCode">selected</#if>>学号</option>
                    <option value="studentName" <#if searchType?default("")=="studentName">selected</#if>>姓名</option>
                    <option value="operatorName" <#if searchType?default("")=="operatorName">selected</#if>>操作人</option>
                </select>
                <div class="pos-rel pull-left">
                    <input type="text" class="typeahead scrollable form-control" autocomplete="off"
                           data-provide="typeahead" name="searchTex" id="logSearchText" value="${searchText!}">
                </div>

                <div class="input-group-btn">
                    <a href="javascript:" type="button" class="btn btn-default" onclick="findLogByCondition()">
                        <i class="fa fa-search"></i>
                    </a>
                </div>
            </div><!-- /input-group -->
        </div>
    </div>
    <table class="table table-bordered table-striped no-margin">
        <thead>
        <tr>
            <th>姓名</th>
            <th>学号</th>
            <th>操作人</th>
            <th>变更记录</th>
            <th>变更时间</th>
        </tr>
        </thead>
        <tbody>
        <#if logList?exists && logList?size gt 0>
            <#list logList as log>
                <tr>
                    <td>${log.studentName!}</td>
                    <td>${log.studentCode!}</td>
                    <td>${log.operatorName!}</td>
                    <td>${log.remark!}</td>
                    <td>${log.modifyTime!}</td>
                </tr>
            </#list>
        <#else>
            <tr>
                <td colspan="5">无任何教师操作记录</td>
            </tr>
        </#if>
        </tbody>
    </table>
    <#if logList?exists && logList?size gt 0>
        <@htmlcomponent.pageToolBar container="#logListId" class="noprint"/>
    </#if>
</div>

<script>
    function findLogByCondition() {
        var url = '${request.contextPath}/newgkelective/${choiceId!}/change/log/list/page?' + $("#logSearchType").val() + "=" + $("#logSearchText").val();
        $("#chooseDiv").load(url);
    }
</script>