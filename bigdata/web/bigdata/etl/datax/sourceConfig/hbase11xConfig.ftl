<table class="tables">
    <tbody>
    <tr>
        <td class="text-right"><font color="red">*</font>表名：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="要读取的 hbase 表名（大小写敏感） "></td>
        <td>
            <input id="hbaseTable" name="hbaseTable" type="text"
                   value="${dataxJobInsParamMap['hbaseTable']!}"
                   class="form-control jobParam required"
                   placeholder="请输入表名"/>
        </td>
    </tr>
    <tr>
        <td class="text-right"><font color="red">*</font>读取模式：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="读取hbase的模式，支持normal 模式、multiVersionFixedColumn模式"></td>
        <td>
            <select name="hbaseReadMode" class="form-control" id="hbaseReadMode">
                <option value="normal">normal</option>
                <option value="multiVersionFixedColumn">multiVersionFixedColumn</option>
            </select>
        </td>
    </tr>
    <tr>
        <td class="text-right">读取版本数：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="指定在多版本模式下的hbasereader读取的版本数，取值只能为－1或者大于1的数字，－1表示读取所有版本">
        </td>
        <td>
            <input type="text" name="hbaseMaxVersion" placeholder="输入版本数（默认-1）"
                   value="${dataxJobInsParamMap['hbaseMaxVersion']!}"
                   class="form-control jobParam"
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