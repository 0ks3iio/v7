<div class="table-show">
    <table class="tables tables-border no-margin">
        <tbody>
        <#if csvList??>
            <#list csvList as list>
                <tr>
                    <#if list??>
                        <#list list as l>
                            <td>${l!}</td>
                        </#list>
                    </#if>
                </tr>
            </#list>
        </#if>
        </tbody>
    </table>
    <button id="closeCsv">关闭</button>
</div>
<script>
    $(function () {
        $("#closeCsv").click(function () {
            $("#importCsvDiv").removeClass("hide")
            $("#showCsvDiv").addClass('hide')
        })
    })
</script>