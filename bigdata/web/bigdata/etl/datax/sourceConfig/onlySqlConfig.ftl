<table class="tables">
    <tbody>
    <tr>
        <td class="text-right"><font color="red">*</font>模式：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="写入数据到目标表采用插入或者替换或者更新">
        </td>
        <td>
            <select class="form-control" onchange="changeIsCustom()" id="isCustomSelect">
                <option value="config">
                    配置表信息
                </option>
                <option value="custom" selected="selected">
                    自定义sql
                </option>
            </select>
        </td>
    </tr>
    <tr>
        <td class="text-right"><font color="red">*</font>querySql：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="当where配置项不足以描述所筛选的条件
                                        ，用户可以通过该配置型来自定义筛选SQL"></td>
        <td class="td-wrap">
            <div class="input-div" id="querySql"
                 contenteditable="true">${dataxJobInsParamMap['querySql']!}</div>
        </td>
    </tr>
    </tbody>
</table>
<script>
    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    })
</script>