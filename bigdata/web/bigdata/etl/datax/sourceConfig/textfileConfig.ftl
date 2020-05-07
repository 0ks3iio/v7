<table id="hbase11xsqlreader" class="tables">
    <tbody>
    <tr>
        <td class="text-right"><font color="red">*</font>路径：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="本地文件系统的路径信息，注意这里可以支持填写多个路径,当指定单个本地文件，TxtFileReader暂时只能使用单线程进行数据抽取。二期考虑在非压缩文件情况下针对单个File可以进行多线程并发读取。
    当指定多个本地文件，当指定通配符，TxtFileReader尝试遍历出多个文件信息。例如: 指定/*代表读取/目录下所有的文件，指定/bazhen/*代表读取bazhen目录下游所有的文件。TxtFileReader目前只支持*作为文件通配符。"></td>
        <td>
            <input name="textfilePath" type="text"
                   value="${dataxJobInsParamMap['textfilePath']!}"
                   class="form-control jobParam required"
                   placeholder="请输入路径"/>
        </td>
    </tr>

    <tr>
        <td class="text-right"><font color="red">*</font>字段分隔符：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="读取的字段分隔符"></td>
        <td>
            <input name="fieldDelimiter" type="text"
                   value="${dataxJobInsParamMap['fieldDelimiter']!}"
                   class="form-control jobParam required"
                   placeholder="请输入分隔符"/>
        </td>
    </tr>

    <tr>
        <td class="text-right">文本压缩类型：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="文本压缩类型，默认不填写意味着没有压缩。支持压缩类型为zip、gzip、bzip2"></td>
        <td>
            <select name="textfileCompress" class="form-control jobParam">
                <option value=""
                        <#if '' == dataxJobInsParamMap['textfileCompress']?default('')>selected="selected"</#if> >
                    无压缩
                </option>
                <option value="zip"
                        <#if 'zip' == dataxJobInsParamMap['textfileCompress']?default('')>selected="selected"</#if>>
                    zip
                </option>
                <option value="gzip"
                        <#if 'gzip' == dataxJobInsParamMap['textfileCompress']?default('')>selected="selected"</#if>>
                    gzip
                </option>
                <option value="bzip2"
                        <#if 'bzip2' == dataxJobInsParamMap['textfileCompress']?default('')>selected="selected"</#if>>
                    bzip2
                </option>
            </select>
        </td>
    </tr>
    </tbody>
</table>
<script>
    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    })
</script>