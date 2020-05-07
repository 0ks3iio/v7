<table class="tables">
    <tbody>

    <tr>
        <td class="text-right">ftp服务器协议：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="ftp服务器协议，目前支持传输协议有ftp和sftp"></td>
        <td>
            <select name="ftpProtocol" class="form-control jobParam">
                <option value="ftp"
                        <#if 'ftp' == dataxJobInsParamMap['ftpProtocol']?default('ftp')>selected="selected"</#if> >
                    ftp
                </option>
                <option value="sftp"
                        <#if 'sftp' == dataxJobInsParamMap['ftpProtocol']?default('ftp')>selected="selected"</#if>>
                    sftp
                </option>
            </select>
        </td>
    </tr>

    <tr>
        <td class="text-right"><font color="red">*</font>ftp服务器地址：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="ftp服务器地址"></td>
        <td>
            <input name="ftpHost" type="text"
                   value="${dataxJobInsParamMap['ftpHost']!}"
                   class="form-control jobParam required"
                   placeholder="请输入ftp服务器地址"/>
        </td>
    </tr>

    <tr>
        <td class="text-right">ftp服务器端口：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="若传输协议是sftp协议，默认值是22；若传输协议是标准ftp协议，默认值是21 "></td>
        <td>
            <input name="ftpPort" type="text"
                   value="${dataxJobInsParamMap['ftpPort']?default('21')}"
                   class="form-control jobParam"
                   placeholder="请输入ftp服务器端口(默认21)" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                   onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"/>
        </td>
    </tr>

    <tr>
        <td class="text-right"><font color="red">*</font>用户名：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="ftp服务器访问用户名"></td>
        <td>
            <input name="ftpUsername" type="text"
                   value="${dataxJobInsParamMap['ftpUsername']!}"
                   class="form-control jobParam required"
                   placeholder="请输入用户名"/>
        </td>
    </tr>

    <tr>
        <td class="text-right"><font color="red">*</font>密码：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="ftp服务器访问密码"></td>
        <td>
            <input name="ftpPassword" type="text"
                   value="${dataxJobInsParamMap['ftpPassword']!}"
                   class="form-control jobParam required"
                   placeholder="请输入用户名"/>
        </td>
    </tr>

    <tr>
        <td class="text-right"><font color="red">*</font>路径：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="远程FTP文件系统的路径信息，注意这里可以支持填写多个路径。

当指定单个远程FTP文件，FtpReader暂时只能使用单线程进行数据抽取。二期考虑在非压缩文件情况下针对单个File可以进行多线程并发读取。

当指定多个远程FTP文件，FtpReader支持使用多线程进行数据抽取。线程并发数通过通道数指定。

当指定通配符，FtpReader尝试遍历出多个文件信息。例如: 指定/*代表读取/目录下所有的文件，指定/bazhen/*代表读取bazhen目录下游所有的文件。FtpReader目前只支持*作为文件通配符。

特别需要注意的是，DataX会将一个作业下同步的所有Text File视作同一张数据表。用户必须自己保证所有的File能够适配同一套schema信息。读取文件用户必须保证为类CSV格式，并且提供给DataX权限可读。

特别需要注意的是，如果Path指定的路径下没有符合匹配的文件抽取，DataX将报错。"></td>
        <td>
            <input name="ftpPath" type="text"
                   value="${dataxJobInsParamMap['ftpPath']!}"
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
            <select name="ftpCompress" class="form-control jobParam">
                <option value=""
                        <#if '' == dataxJobInsParamMap['ftpCompress']?default('')>selected="selected"</#if> >
                    无压缩
                </option>
                <option value="zip"
                        <#if 'zip' == dataxJobInsParamMap['ftpCompress']?default('')>selected="selected"</#if>>
                    zip
                </option>
                <option value="gzip"
                        <#if 'gzip' == dataxJobInsParamMap['ftpCompress']?default('')>selected="selected"</#if>>
                    gzip
                </option>
                <option value="bzip2"
                        <#if 'bzip2' == dataxJobInsParamMap['ftpCompress']?default('')>selected="selected"</#if>>
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