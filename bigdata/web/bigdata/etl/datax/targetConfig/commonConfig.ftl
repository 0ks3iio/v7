<table class="tables">
    <tbody>
    <#if dbType?default('mysql') == 'mysql'>
        <tr>
            <td class="text-right"><font color="red">*</font>writeMode：<img
                        src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                        data-toggle="tooltip" data-placement="left"
                        title="写入数据到目标表采用插入或者替换或者更新">
            </td>
            <td>
                <select name="writeMode" class="form-control jobParam required">
                    <option value="insert"
                            <#if 'insert' == dataxJobInsParamMap['writeMode']!>selected="selected"</#if> >
                        插入
                    </option>
                    <option value="update"
                            <#if 'update' == dataxJobInsParamMap['writeMode']!>selected="selected"</#if>>
                        更新
                    </option>
                    <option value="replace"
                            <#if 'replace' == dataxJobInsParamMap['writeMode']!>selected="selected"</#if>>
                        替换
                    </option>
                </select>
            </td>
        </tr>
    </#if>
    <tr>
        <td class="text-right">session：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="获取连接时，执行session指定的SQL语句，修改当前connection session属性">
        </td>
        <td>
            <input type="text" name="writerSession" placeholder="输入connection session属性（非必填）"
                   value="${dataxJobInsParamMap['writerSession']!}"
                   class="form-control jobParam"/>
        </td>
    </tr>
    <tr>
        <td class="text-right">写入数据前sql：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="写入数据前，执行的sql语句">
        </td>
        <td>
            <input type="text" name="preSql" placeholder="输入写入数据前sql（非必填）"
                   value="${dataxJobInsParamMap['preSql']!}"
                   class="form-control jobParam"/>
        </td>
    </tr>
    <tr>
        <td class="text-right">写入数据后sql：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="写入数据到目的表后，会执行这里的标准语句。（原理同 preSql ） ">
        </td>
        <td>
            <input type="text" name="postSql" placeholder="输入写入数据后sql（非必填）"
                   value="${dataxJobInsParamMap['postSql']!}"
                   class="form-control jobParam"/>
        </td>
    </tr>
    <tr>
        <td class="text-right">批量提交的记录数：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="一次性批量提交的记录数大小，该值可以极大减少DataX与Mysql的网络交互次数，并提升整体吞吐量。但是该值设置过大可能会造成DataX运行进程内存溢出情况。默认1024">
        </td>
        <td>
            <input type="text" name="batchSize" placeholder="输入一次性批量提交的记录数（非必填）"
                   value="${dataxJobInsParamMap['batchSize']!}"
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