<div class="table-show">
    <table class="tables tables-border no-margin">
        <tbody>
        <#if excelList??>
            <#list excelList as list>
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
    <button class="btn btn-blue center" id="closeExcel">关闭</button>
</div>
<script>
    $(function () {
        $("#closeExcel").click(function () {
            $("#importExcelDiv").removeClass("hide")
            $("#showExcelDiv").addClass('hide')
        })
    })
</script>