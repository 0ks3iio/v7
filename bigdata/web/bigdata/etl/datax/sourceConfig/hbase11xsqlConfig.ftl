<table id="hbase11xsqlreader" class="tables">
    <tbody>
    <tr>
        <td class="text-right"><font color="red">*</font>表名：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="编写Phoenix中的表名,如果有namespace，该值设置为'namespace.tablename'"></td>
        <td>
            <input id="phoenixTable" name="phoenixTable" type="text"
                   value="${dataxJobInsParamMap['phoenixTable']!}"
                   class="form-control jobParam required"
                   placeholder="请输入表名"/>
        </td>
    </tr>
    </tbody>
</table>
<script>
    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    })
</script>