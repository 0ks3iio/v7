<form id="eventIndexForm">
    <div class="form-horizontal form-made">
        <input type="hidden" name="id" value="${eventIndicator.id!}">
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>事件：</label>
            <div class="col-sm-6">
                <select name="eventId" id="eventSelect" class="form-control">
                    <#list eventTypeMap?keys as key>
                        <optgroup label="${eventTypeMap[key].typeName!}"></optgroup>
                        <#if eventMap[key]?exists>
                            <#list eventMap[key] as item>
                              <option <#if eventIndicator.eventId?exists><#if eventIndicator.eventId == item.id>selected="selected"</#if></#if>
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
                <input type="text" name="indicatorName" id="indicatorName" class="form-control" nullable="false"
                       maxlength="50" value="${eventIndicator.indicatorName!}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>聚合方式：</label>
            <div class="col-sm-6">
                <select name="aggType" id="aggTypeSelect" onchange="changeAggTypeSelect()" class="form-control">
                    <option <#if eventIndicator.aggType?default('') == 'count'>selected="selected"</#if> value="count">
                        count
                    </option>
                    <option <#if eventIndicator.aggType?default('') == 'longSum'>selected="selected"</#if>
                            value="longSum">longSum
                    </option>
                    <option <#if eventIndicator.aggType?default('') == 'doubleSum'>selected="selected"</#if>
                            value="doubleSum">doubleSum
                    </option>
                    <option <#if eventIndicator.aggType?default('') == 'floatSum'>selected="selected"</#if>
                            value="floatSum">floatSum
                    </option>
                    <option <#if eventIndicator.aggType?default('') == 'doubleMin'>selected="selected"</#if>
                            value="doubleMin">doubleMin
                    </option>
                    <option <#if eventIndicator.aggType?default('') == 'doubleMax'>selected="selected"</#if>
                            value="doubleMax">doubleMax
                    </option>
                    <option <#if eventIndicator.aggType?default('') == 'floatMin'>selected="selected"</#if>
                            value="floatMin">floatMin
                    </option>
                    <option <#if eventIndicator.aggType?default('') == 'floatMax'>selected="selected"</#if>
                            value="floatMax">floatMax
                    </option>
                    <option <#if eventIndicator.aggType?default('') == 'longMin'>selected="selected"</#if>
                            value="longMin">longMin
                    </option>
                    <option <#if eventIndicator.aggType?default('') == 'longMax'>selected="selected"</#if>
                            value="longMax">longMax
                    </option>
                    <option <#if eventIndicator.aggType?default('') == 'doubleFirst'>selected="selected"</#if>
                            value="doubleFirst">doubleFirst
                    </option>
                    <option <#if eventIndicator.aggType?default('') == 'doubleLast'>selected="selected"</#if>
                            value="doubleLast">doubleLast
                    </option>
                    <option <#if eventIndicator.aggType?default('') == 'floatFirst'>selected="selected"</#if>
                            value="floatFirst">floatFirst
                    </option>
                    <option <#if eventIndicator.aggType?default('') == 'floatLas'>selected="selected"</#if>
                            value="floatLas">floatLas
                    </option>
                    <option <#if eventIndicator.aggType?default('') == 'longFirst'>selected="selected"</#if>
                            value="longFirst">longFirst
                    </option>
                    <option <#if eventIndicator.aggType?default('') == 'longLast'>selected="selected"</#if>
                            value="longLast">longLast
                    </option>
                    <option <#if eventIndicator.aggType?default('') == 'thetaSketch'>selected="selected"</#if>
                            value="thetaSketch">thetaSketch
                    </option>
                    <option <#if eventIndicator.aggType?default('') == 'hyperUnique'>selected="selected"</#if>
                            value="hyperUnique">hyperUnique
                    </option>
                </select>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;" id="aggFieldTitle"
                                                                         <#if eventIndicator.aggType?default('count') == 'count'>hidden="hidden"</#if>>*</font>聚合列：</label>
            <div class="col-sm-6">
                <input type="text" name="aggField" id="aggField" class="form-control" nullable="false" maxlength="50"
                       value="${eventIndicator.aggField!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>聚合输出字段名：</label>
            <div class="col-sm-6">
                <input type="text" name="aggOutputName" id="aggOutputName" class="form-control" nullable="false"
                       maxlength="50" value="${eventIndicator.aggOutputName!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>排序号：</label>
            <div class="col-sm-6">
                <input type="text" name="orderId" id="orderId" class="form-control" nullable="true"
                       onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       maxlength="3" value="${eventIndicator.orderId!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">备注：</label>
            <div class="col-sm-6">
                <textarea name="remark" id="remark" type="text/plain" nullable="true" maxlength="200"
                          style="width:100%;height:50px;resize: none;">${eventIndicator.remark!}</textarea>
            </div>
        </div>
    </div>
</form>
<script>
    function changeAggTypeSelect() {
        if ($('#aggTypeSelect').val() == 'count') {
            $('#aggFieldTitle').hide();
        } else {
            $('#aggFieldTitle').show();
        }
    }
</script>