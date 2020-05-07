<table class="tables">
    <tbody>
    <tr>
        <td class="text-right"><font color="red">*</font>模式：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="配置表信息或者自定义sql">
        </td>
        <td>
            <select class="form-control" onchange="changeIsCustom()" id="isCustomSelect">
                <option value="config" selected="selected">
                    配置表信息
                </option>
                <option value="custom">
                    自定义sql
                </option>
            </select>
        </td>
    </tr>

    <tr>
        <td class="text-right"><font color="red">*</font>表名：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="需要同步的表"></td>
        <td>
            <input id="readerTable" name="readerTable" type="text"
                   value="${dataxJobInsParamMap['readerTable']!}"
                   class="form-control jobParam required"
                   placeholder="请输入表名"/>
        </td>
    </tr>
    <tr>
        <td class="text-right">切分主键：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right"
                    title="数据抽取时，如果指定splitPk，表示用户希望使用splitPk代表的字段进行数据分片，DataX因此会启动并发任务进行数据同步，这样可以大大提供数据同步的效能">
        </td>
        <td>
            <input name="readerSplitPk" type="text" value="${dataxJobInsParamMap['readerSplitPk']!}"
                   class="form-control jobParam"
                   placeholder="请输入切分主键（非必填）"/>
        </td>
    </tr>
    <tr>
        <td class="text-right">筛选条件：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right"
                    title="筛选条件，实际业务场景中，往往会选择当天的数据进行同步，可以将where条件指定为gmt_create > $bizdate 。增量同步时，必须配置creationTime > '${r'${start_time}'}' and creationTime < '${r'${end_time}'}',(必须加上引号！否则会读取不到数据) 注意：不可以将where条件指定为limit 10，limit不是SQL的合法where子句。">
        </td>
        <td>
            <input id="readerWhere" name="readerWhere" type="text"
                   value="${dataxJobInsParamMap['readerWhere']!}"
                   class="form-control jobParam"
                   placeholder="请输入筛选条件（非必填）"/>
        </td>
    </tr>

    <tr>
        <td class="text-right">每次批量数据获取条数：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right"
                    title="数据库服务器端每次批量数据获取条数，该值决定了DataX和服务器端的网络交互次数，能够较大的提升数据抽取性能">
        </td>
        <td>
            <input name="readerFetchSize" type="text"
                   value="${dataxJobInsParamMap['readerFetchSize']!}" class="form-control jobParam"
                   placeholder="请输入每次批量数据获取条数（非必填）"
                   onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                   onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"/>

        </td>
    </tr>
    </tbody>
</table>
<script>
    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    })
</script>