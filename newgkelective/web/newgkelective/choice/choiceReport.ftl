<div class="filter">
    <div class="filter-item">
        <label for="" class="filter-name">选课名称：</label>
        <div class="filter-content">
            <select vtype="selectOne" class="form-control" id="choiceId" onChange="reportShow()" style="width: 500px">
            <#if allList?exists && allList?size gt 0>
                <#list allList as e>
                    <option value="${e.id!}" <#if choiceId! == e.id!>selected</#if>>${e.choiceName!}</option>
                </#list>
            <#else>
                <option value="">---未进行任何选课---</option>
            </#if>
            </select>
        </div>
    </div>
</div>
<div id="reportDetail">

</div>


<script>
    $(function () {
        reportShow();
    });

    function reportShow() {
        if ($("#choiceId option:selected").val() == "") {
            return;
        }
        var url = '${request.contextPath}/newgkelective/report/choice/detail/page?gradeId=${gradeId!}&choiceId=' + $("#choiceId option:selected").val();
        $("#reportDetail").load(url);
    }
</script>