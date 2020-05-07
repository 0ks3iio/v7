<form id="eventForm">
    <div class="form-horizontal form-made">
        <input type="hidden" name="id" value="${eventProperty.id!}">
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>事件：</label>
            <div class="col-sm-6">
                <select id="eventSelect" name="eventId" class="form-control">
                    <option <#if eventId?exists><#if eventId == '00000000000000000000000000000env'>selected="selected"</#if></#if>
                            value="00000000000000000000000000000env">系统
                    </option>
                    <option <#if eventId?exists><#if eventId == '0000000000000000000000000000time'>selected="selected"</#if></#if>
                            value="0000000000000000000000000000time">时间
                    </option>
                    <option <#if eventId?exists><#if eventId == '0000000000000000000000000000user'>selected="selected"</#if></#if>
                            value="0000000000000000000000000000user">用户
                    </option>
                    <#list eventTypeMap?keys as key>
                        <optgroup label="${eventTypeMap[key].typeName!}"></optgroup>
                        <#if eventMap[key]?exists>
                            <#list eventMap[key] as item>
                              <option <#if eventProperty.eventId?exists><#if eventProperty.eventId == item.id>selected="selected"</#if></#if>
                                      value="${item.id!}">&nbsp;&nbsp;&nbsp;${item.eventName!}</option>
                            </#list>
                        </#if>
                    </#list>
                </select>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>名称：</label>
            <div class="col-sm-6">
                <input type="text" name="propertyName" id="propertyName" class="form-control" nullable="false"
                       maxlength="50" value="${eventProperty.propertyName!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>字段名：</label>
            <div class="col-sm-6">
                <input type="text" name="fieldName" id="fieldName" class="form-control" nullable="false" maxlength="50"
                       value="${eventProperty.fieldName!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">自定义排序：</label>
            <div class="col-sm-6">
                <textarea name="orderJson" id="orderJson" type="text/plain" nullable="true" maxlength="1000"
                          placeholder='请确保所有值维护的排序号位数都是3位，否则结果有可能乱掉。自定义排序格式：{"星期一":"001","星期二":"002","星期三":"003","星期四":"004","星期五":"005","星期六":"006","星期日":"007"}'
                          style="width:100%;height:40px;resize: none">${eventProperty.orderJson!}</textarea>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">数据字典：</label>
            <div class="col-sm-6">
            	<textarea name="dataDictionary" id="dataDictionary" type="text/plain" nullable="true" maxlength="1000"
                          placeholder='格式：{"1":"xxx","2":"xxx","3":"xxx","4":"xxx"...}'
                          style="width:100%;height:45px;resize: none;">${eventProperty.dataDictionary!}</textarea>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">是否显示：</label>
            <div class="col-sm-6" style="height: 20px; margin-bottom: 0px;">
                <label class="switch mt-5">
                    <input name="switch-field-1" id="timeSwitch" <#if eventProperty.isShowChart?default(0) == 1>
                       checked="checked"</#if> type="checkbox"
                           onchange="changeIsShowChart(this)">
                    <span class="switch-name"></span>
                </label>
                <input type="hidden" id="isShowChart" name="isShowChart" value="${eventProperty.isShowChart!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">是否时序字段：</label>
            <div class="col-sm-6" style="height: 20px; margin-bottom: 0px;">
                <label class="switch mt-5">
                    <input name="switch-field-1" id="timeSwitch" <#if eventProperty.isSequential?default(0) == 1>
                       checked="checked"</#if> type="checkbox"
                           onchange="changeIsSequential(this)">
                    <span class="switch-name"></span>
                </label>
                <input type="hidden" id="isSequential" name="isSequential" value="${eventProperty.isSequential!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>排序号：</label>
            <div class="col-sm-6">
                <input type="text" name="orderId" id="orderId" class="form-control" nullable="true"
                       onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       maxlength="3" value="${eventProperty.orderId!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">备注：</label>
            <div class="col-sm-6">
                <textarea name="remark" id="remark" type="text/plain" nullable="true" maxlength="200"
                          style="width:100%;height:40px;resize: none;">${eventProperty.remark!}</textarea>
            </div>
        </div>
    </div>
</form>
<script>
    function changeIsShowChart(e) {
        if ($(e).is(':checked')) {
            $('#isShowChart').val('1');
        } else {
            $('#isShowChart').val('0');
        }
    }

    function changeIsSequential(e) {
        if ($(e).is(':checked')) {
            $('#isSequential').val('1');
        } else {
            $('#isSequential').val('0');
        }
    }
</script>