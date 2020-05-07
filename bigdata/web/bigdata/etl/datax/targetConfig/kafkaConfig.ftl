<table class="tables">
    <tbody>
    <tr>
        <td class="text-right"><font color="red">*</font>主题：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="kafka对应的主题">
        </td>
        <td>
            <input type="text" name="kafkaTopic" placeholder="输入kafka主题"
                   value="${dataxJobInsParamMap['kafkaTopic']!}"
                   class="form-control jobParam required"/>
        </td>
    </tr>

    <tr>
        <td class="text-right"><font color="red">*</font>集群地址：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="kafka对应的集群地址">
        </td>
        <td>
            <input type="text" name="kafkaServers" placeholder="输入kafka集群地址"
                   value="${dataxJobInsParamMap['kafkaServers']!}"
                   class="form-control jobParam required"/>
        </td>
    </tr>

    <tr>
        <td class="text-right">批量提交的记录数：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="kafka批量提交的记录数">
        </td>
        <td>
            <input type="text" name="batchSize" placeholder="输入一次性批量提交的记录数（非必填）"
                   value="${dataxJobInsParamMap['batchSize']!}"
                   class="form-control jobParam"
                   onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                   onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"/>
        </td>
    </tr>

    <tr>
        <td class="text-right">自动创建主题：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="如果选择是，那么没有主题的情况下将自动创建主题">
        </td>
        <td>
            <label class="choice">
                <input type="radio" <#if dataxJobInsParamMap['kafkaCreateTopic']?default('false') == 'true'>checked="checked"</#if>
                       name="kafkaCreateTopic" value="true">
                <span class="choice-name">是</span>
            </label>
            <label class="choice">
                <input type="radio" <#if dataxJobInsParamMap['kafkaCreateTopic']?default('false') == 'false'>checked="checked"</#if>
                       name="kafkaCreateTopic" value="false">
                <span class="choice-name">否</span>
            </label>
        </td>
    </tr>

    <tr>
        <td class="text-right"><font color="red">*</font>主题分区数：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="kakfa对应的分区数">
        </td>
        <td>
            <input type="text" name="kafkaPartition" placeholder="输入kakfa对应的分区数"
                   value="${dataxJobInsParamMap['kafkaPartition']!}"
                   class="form-control jobParam required"
                   onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                   onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"/>
        </td>
    </tr>

    <tr>
        <td class="text-right"><font color="red">*</font>主题备份数：<img
                    src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                    data-toggle="tooltip" data-placement="left"
                    title="kakfa对应的备份数">
        </td>
        <td>
            <input type="text" name="kafkaReplicationFactor" placeholder="输入kakfa对应的分区数"
                   value="${dataxJobInsParamMap['kafkaReplicationFactor']!}"
                   class="form-control jobParam required"
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