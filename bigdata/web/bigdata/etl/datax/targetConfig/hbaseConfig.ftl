<table class="tables">
    <tbody>
    <tr>
        <td class="text-right">null值处理：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="读取的null值时，如何处理"></td>
        <td>
        <td>
            <select name="nullMode" class="form-control">
                <option value="Skip"
                        <#if 'Skip' == dataxJobInsParamMap['nullMode']!>selected="selected"</#if> >
                    跳过
                </option>
                <option value="Empty"
                        <#if 'Empty' == dataxJobInsParamMap['nullMode']!>selected="selected"</#if>>
                    写入空字符数组
                </option>
            </select>
        </td>
        </td>
    </tr>
    <tr>
        <td class="text-right">是否写WAL日志：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right"
                    title="是否写WAL日志，只有当WAL日志写成功后，再接着写MemStore，然后客户端被通知提交数据成功；如果写WAL日志失败，客户端则被通知提交失败。放弃写WAL日志，会提高数据写入的性能">
        </td>
        <td>
        <td>
            <select name="walFlag" class="form-control">
                <option value="false"
                        <#if 'false' == dataxJobInsParamMap['walFlag']!>selected="selected"</#if> >
                    否
                </option>
                <option value="true"
                        <#if 'true' == dataxJobInsParamMap['walFlag']!>selected="selected"</#if>>
                    是
                </option>
            </select>
        </td>
        </td>
    </tr>
    </tbody>
</table>
<script>
    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    })
</script>