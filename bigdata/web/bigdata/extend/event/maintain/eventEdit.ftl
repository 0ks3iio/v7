<form id="eventForm">
    <div class="form-horizontal">
        <input type="hidden" name="id" value="${event.id!}">
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>事件组：</label>
            <div class="col-sm-6">
                <select id="eventTypeSelect" name="typeId" class="form-control">
                    <#list eventTypes as item>
                        <option value="${item.id!}" <#if item.id == event.typeId?default('')>
                                selected="selected"</#if> >${item.typeName}</option>
                    </#list>
                </select>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>名称：</label>
            <div class="col-sm-6">
                <input type="text" name="eventName" id="eventName" class="form-control" nullable="false" maxlength="50"
                       value="${event.eventName!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>CODE：</label>
            <div class="col-sm-6">
                <input type="text" name="eventCode" id="eventCode" class="form-control" nullable="false" maxlength="50"
                       value="${event.eventCode!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>表名：</label>
            <div class="col-sm-6">
                <input type="text" name="tableName" id="tableName" class="form-control" nullable="false" maxlength="50"
                       value="${event.tableName!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>TOPIC NAME：</label>
            <div class="col-sm-6">
                <input type="text" name="topicName" id="topicName" class="form-control" nullable="false" maxlength="50"
                       value="${event.topicName!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>聚合粒度：</label>
            <div class="col-sm-6">
                <select name="granularity" class="form-control" id="granularitySelect">
                    <option <#if event.granularity?default('day') == 'second'>selected="selected"</#if> value="second">
                        按秒
                    </option>
                    <option <#if event.granularity?default('day') == 'minute'>selected="selected"</#if> value="minute">
                        按分钟
                    </option>
                    <option <#if event.granularity?default('day') == 'fifteen_minute'>selected="selected"</#if>
                            value="fifteen_minute">按15分钟
                    </option>
                    <option <#if event.granularity?default('day') == 'thirty_minute'>selected="selected"</#if>
                            value="thirty_minute">按30分钟
                    </option>
                    <option <#if event.granularity?default('day') == 'hour'>selected="selected"</#if> value="hour">按小时
                    </option>
                    <option <#if event.granularity?default('day') == 'day'>selected="selected"</#if> value="day">按天
                    </option>
                    <option <#if event.granularity?default('day') == 'week'>selected="selected"</#if> value="week">按周
                    </option>
                    <option <#if event.granularity?default('day') == 'month'>selected="selected"</#if> value="month">
                        按月
                    </option>
                    <option <#if event.granularity?default('day') == 'quarter'>selected="selected"</#if>value="quarter">
                        按季度
                    </option>
                    <option <#if event.granularity?default('day') == 'year'>selected="selected"</#if> value="year">按年
                    </option>
                </select>
            </div>
        </div>
        <!--
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>数据采集时间间隔：</label>
            <div class="col-sm-6">
                <input type="text" name="intervalTime" id="intervalTime" class="form-control" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" nullable="false" maxlength="50" value="${event.intervalTime!}">
            </div>
        </div>
		-->
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">时间属性：</label>
            <div class="col-sm-6" style="height: 30px; margin-bottom: 0px;">
                <label class="switch switch-txt mt-5">
                    <input name="switch-field-1" id="timeSwitch" <#if event.timeProperty?default(0) == 1>
                       checked="checked"</#if> type="checkbox"
                           onchange="changeTimeProperty(this)">
                    <span class="switch-name">显示</span>
                </label>
                <input type="hidden" id="timeProperty" name="timeProperty" value="${event.timeProperty!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">用户属性：</label>
            <div class="col-sm-6" style="height: 30px; margin-bottom: 0px;">
                <label class="switch switch-txt mt-5">
                    <input name="switch-field-1" id="timeSwitch" <#if event.userProperty?default(0) == 1>
                       checked="checked"</#if> type="checkbox"
                           onchange="changeUserProperty(this)">
                    <span class="switch-name">显示</span>
                </label>
                <input type="hidden" id="userProperty" name="userProperty" value="${event.userProperty!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">系统属性：</label>
            <div class="col-sm-6" style="height: 30px; margin-bottom: 0px;">
                <label class="switch switch-txt mt-5">
                    <input name="switch-field-1" id="timeSwitch" <#if event.envProperty?default(0) == 1>
                       checked="checked"</#if> type="checkbox"
                           onchange="changeEvnProperty(this)">
                    <span class="switch-name">显示</span>
                </label>
                <input type="hidden" id="envProperty" name="envProperty" value="${event.envProperty!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">导入数据：</label>
            <div class="col-sm-6" style="height: 30px; margin-bottom: 0px;">
                <label class="switch switch-txt mt-5">
                    <input name="switch-field-1" id="timeSwitch" <#if event.importSwitch?default(0) == 1>
                       checked="checked"</#if> type="checkbox"
                           onchange="changeImportSwitch(this)">
                    <span class="switch-name">显示</span>
                </label>
                <input type="hidden" id="importSwitch" name="importSwitch" value="${event.importSwitch!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>排序号：</label>
            <div class="col-sm-6">
                <input type="text" name="orderId" id="orderId" class="form-control" nullable="true"
                       onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       maxlength="3" value="${event.orderId!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">备注：</label>
            <div class="col-sm-6">
                <textarea name="remark" id="remark" type="text/plain" nullable="true" maxlength="200"
                          style="width:100%;height:50px;resize: none;">${event.remark!}</textarea>
            </div>
        </div>
    </div>
</form>
<script>
    function changeTimeProperty(e) {
        if ($(e).is(':checked')) {
            $('#timeProperty').val('1');
        } else {
            $('#timeProperty').val('0');
        }
    }

    function changeUserProperty(e) {
        if ($(e).is(':checked')) {
            $('#userProperty').val('1');
        } else {
            $('#userProperty').val('0');
        }
    }

    function changeEvnProperty(e) {
        if ($(e).is(':checked')) {
            $('#envProperty').val('1');
        } else {
            $('#envProperty').val('0');
        }
    }

    function changeImportSwitch(e) {
        if ($(e).is(':checked')) {
            $('#importSwitch').val('1');
        } else {
            $('#importSwitch').val('0');
        }
    }

</script>