<table class="tables">
    <tbody>

    <tr>
        <td class="text-right">esAccessId：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="http auth中的user"></td>
        <td>
            <input name="esAccessId" type="text"
                   value="${dataxJobInsParamMap['esAccessId']!}"
                   class="form-control jobParam"
                   placeholder="输入accessId（非必填）"/>
        </td>
    </tr>

    <tr>
        <td class="text-right">esAccessKey：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="http auth中的password"></td>
        <td>
            <input name="esAccessKey" type="text"
                   value="${dataxJobInsParamMap['esAccessKey']!}"
                   class="form-control jobParam"
                   placeholder="输入accessKey（非必填）"/>
        </td>
    </tr>

    <tr>
        <td class="text-right"><font color="red">*</font>index：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="elasticsearch中的index名"></td>
        <td>
            <input name="esIndex" type="text"
                   value="${dataxJobInsParamMap['esIndex']!}"
                   class="form-control jobParam required"
                   placeholder="输入index名"/>
        </td>
    </tr>

    <tr>
        <td class="text-right">type：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="elasticsearch中index的type名"></td>
        <td>
            <input name="esType" type="text"
                   value="${dataxJobInsParamMap['esType']!}"
                   class="form-control jobParam"
                   placeholder="输入type名"/>
        </td>
    </tr>

    <tr>
        <td class="text-right">是否删除原表：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="是否删除原表">
        </td>
        <td>
            <select name="esCleanup" class="form-control jobParam required">
                <option value="false"
                        <#if 'false' == dataxJobInsParamMap['esCleanup']?default('false')>selected="selected"</#if> >
                    否
                </option>
                <option value="true"
                        <#if 'true' == dataxJobInsParamMap['esCleanup']?default('false')>selected="selected"</#if>>
                    是
                </option>
            </select>
        </td>
    </tr>

    <tr>
        <td class="text-right">批量提交数据条数：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="elasticsearch中index的type名"></td>
        <td>
            <input name="batchSize" type="text"
                   value="${dataxJobInsParamMap['batchSize']!}"
                   class="form-control jobParam"
                   placeholder="输入批量获取数据条数" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                   onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"/>
        </td>
    </tr>

    <tr>
        <td class="text-right">失败后重试次数：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="失败后重试次数"></td>
        <td>
            <input name="esTrySize" type="text"
                   value="${dataxJobInsParamMap['esTrySize']?default('30')}"
                   class="form-control jobParam"
                   placeholder="输入失败后重试次数" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                   onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"/>
        </td>
    </tr>

    <tr>
        <td class="text-right">客户端超时时间：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="right" title="客户端超时时间"></td>
        <td>
            <input name="esTimeout" type="text"
                   value="${dataxJobInsParamMap['esTimeout']?default('600000')}"
                   class="form-control jobParam"
                   placeholder="输入客户端超时时间" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                   onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"/>
        </td>
    </tr>

    <tr>
        <td class="text-right">是否启用节点发现：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="是否启用节点发现">
        </td>
        <td>
            <select name="esDiscovery" class="form-control jobParam">
                <option value="false"
                        <#if 'false' == dataxJobInsParamMap['esDiscovery']?default('false')>selected="selected"</#if> >
                    否
                </option>
                <option value="true"
                        <#if 'true' == dataxJobInsParamMap['esDiscovery']?default('false')>selected="selected"</#if>>
                    是
                </option>
            </select>
        </td>
    </tr>

    <tr>
        <td class="text-right">是否开启压缩：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="是否启用节点发现">
        </td>
        <td>
            <select name="esCompression" class="form-control jobParam">
                <option value="false"
                        <#if 'false' == dataxJobInsParamMap['esCompression']?default('true')>selected="selected"</#if> >
                    否
                </option>
                <option value="true"
                        <#if 'true' == dataxJobInsParamMap['esCompression']?default('true')>selected="selected"</#if>>
                    是
                </option>
            </select>
        </td>
    </tr>

    <tr>
        <td class="text-right">是否多线程：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="http请求，是否有多线程">
        </td>
        <td>
            <select name="esMultiThread" class="form-control jobParam">
                <option value="false"
                        <#if 'false' == dataxJobInsParamMap['esMultiThread']?default('true')>selected="selected"</#if> >
                    否
                </option>
                <option value="true"
                        <#if 'true' == dataxJobInsParamMap['esMultiThread']?default('true')>selected="selected"</#if>>
                    是
                </option>
            </select>
        </td>
    </tr>

    <tr>
        <td class="text-right">忽略写入错误：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="忽略写入错误，不重试，继续写入">
        </td>
        <td>
            <select name="esIgnoreWriteError" class="form-control jobParam">
                <option value="false"
                        <#if 'false' == dataxJobInsParamMap['esIgnoreWriteError']?default('false')>selected="selected"</#if> >
                    否
                </option>
                <option value="true"
                        <#if 'true' == dataxJobInsParamMap['esIgnoreWriteError']?default('false')>selected="selected"</#if>>
                    是
                </option>
            </select>
        </td>
    </tr>

    <tr>
        <td class="text-right">忽略解析数据格式错误：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="忽略解析数据格式错误，继续写入">
        </td>
        <td>
            <select name="esIgnoreParseError" class="form-control jobParam">
                <option value="false"
                        <#if 'false' == dataxJobInsParamMap['esIgnoreParseError']?default('true')>selected="selected"</#if> >
                    否
                </option>
                <option value="true"
                        <#if 'true' == dataxJobInsParamMap['esIgnoreParseError']?default('true')>selected="selected"</#if>>
                    是
                </option>
            </select>
        </td>
    </tr>

    <tr>
        <td class="text-right">写入别名：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="数据导入完成后写入别名">
        </td>
        <td>
            <input type="text" name="esAlias" placeholder="输入写入别名（非必填）"
                   value="${dataxJobInsParamMap['esAlias']!}"
                   class="form-control jobParam"/>
        </td>
    </tr>

    <tr>
        <td class="text-right">增加别名模式：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="数据导入完成后增加别名的模式，append(增加模式), exclusive(只留这一个)">
        </td>
        <td>
            <select name="esAliasMode" class="form-control jobParam">
                <option value="append"
                        <#if 'append' == dataxJobInsParamMap['esAliasMode']?default('append')>selected="selected"</#if> >
                    增加模式
                </option>
                <option value="exclusive"
                        <#if 'exclusive' == dataxJobInsParamMap['esAliasMode']?default('append')>selected="selected"</#if>>
                    只留这一个
                </option>
            </select>
        </td>
    </tr>

    <tr>
        <td class="text-right">settings：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="创建index时候的settings, 与elasticsearch官方相同">
        </td>
        <td>
            <input type="text" name="esSettings" placeholder="输入settings（非必填）"
                   value="${dataxJobInsParamMap['esSettings']!}"
                   class="form-control jobParam"/>
        </td>
    </tr>

    <tr>
        <td class="text-right">分隔符：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="如果插入数据是array，就使用指定分隔符">
        </td>
        <td>
            <input type="text" name="esSplitter" placeholder="输入分隔符（非必填）"
                   value="${dataxJobInsParamMap['esSplitter']!}"
                   class="form-control jobParam"/>
        </td>
    </tr>

    <tr>
        <td class="text-right">es自动mappings：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="不使用datax的mappings，使用es自己的自动mappings">
        </td>
        <td>
            <select name="esDynamic" class="form-control jobParam">
                <option value="false"
                        <#if 'false' == dataxJobInsParamMap['esDynamic']?default('false')>selected="selected"</#if> >
                    否
                </option>
                <option value="true"
                        <#if 'true' == dataxJobInsParamMap['esDynamic']?default('false')>selected="selected"</#if>>
                    是
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