<form id="metadataForm">
    <div class="form-horizontal">
        <input type="hidden" name="id" value="${ruleRelation.id!}">
        <input type="hidden" name="metadataId" value="${metadataId!}">
        <input type="hidden" name="mdType" value="${mdType!}">
        <input type="hidden" name="tableName" value="${tableName!}">
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>规则名称：</label>
            <div class="col-sm-6">
                <input type="text" name="name" id="ruleName" class="form-control" nullable="false" maxlength="30"
                       value="${ruleRelation.name!}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>规则类型：</label>
            <div class="col-sm-6">
                <select id="ruleType" class="form-control" name="ruleType" onchange="changeRuleType(this)">
                    <#if mdType?default('table') == 'table' >
                    <option value="2" <#if ruleRelation.ruleType?default(1) == 2>selected="selected"</#if>>
                        字段规则
                    </option>
                    </#if>
                    <#if mdType?default('table') != 'table' >
                        <option value="3" <#if ruleRelation.ruleType?default(1) == 3>selected="selected"</#if>>
                            任务规则
                        </option>
                    </#if>
                </select>
            </div>
        </div>

        <#if mdType?default('table') == 'table'>
        <div class="form-group" id="columnInput" <#if ruleRelation.ruleType?default(1) == 1>hidden="hidden"</#if>>
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>字段名：</label>
            <div class="col-sm-6">
                <select id="columnSelect" class="form-control" name="columnId">
                    <#list tableColumns as item>
                        <option value="${item.id!}"
                                <#if ruleRelation.columnId?default('')== item.id>selected="selected"</#if>>${item.name!}</option>
                    </#list>
                </select>
            </div>
        </div>
        </#if>

        <div class="form-group" id="ruleInput">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>规则模版：</label>
            <div class="col-sm-6">
                <select id="ruleTemplateSelect" class="form-control" name="ruleTemplateId"
                        onchange="changeRuleTemplate(this)">
                    <option value="">自定义规则</option>
                </select>
            </div>
        </div>

        <div class="form-group" id="dimInput">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>所属维度：</label>
            <div class="col-sm-6">
                <select id="dimCode" class="form-control" name="dimCode">
          			<#list dimList as dim>
                        <option value="${dim.code!}"
                                <#if ruleRelation.dimCode! == dim.code!>selected="selected"</#if>>${dim.name!}</option>
                    </#list>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>计算规则：</label>
            <div class="col-sm-6">
                <select id="computerType" class="form-control" name="computerType" onchange="changeComputerType(this.value,false)">
                    ${mcodeSetting.getMcodeSelect("DM-BG-JSGZ", (ruleRelation.computerType?default('notnull'))?string, "0")}
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">规则明细：</label>
            <div class="col-sm-6">
                <textarea name="detail" id="detail" type="text/plain" nullable="true" maxlength="1000"
                          style="width:100%;height:80px;resize: none;">${ruleRelation.detail!}</textarea>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">规则说明：</label>
            <div class="col-sm-6">
				<div class="mt-7" style="color:blue;" id="ruleSampleDiv"></div>
			</div>
        </div>
    </div>
</form>
<script>
    $(function () {
        ruleType($('#ruleType').val());
        changeComputerType('${ruleRelation.computerType?default('notnull')}',true);
        if ('${ruleRelation.ruleTemplateId!}' != '') {
            selectRuleTemplate('${ruleRelation.ruleTemplateId!}');
        }
    });
    
    function changeComputerType(computerType,init) {
    	var msg;
        if (computerType == "between") {
        	msg='数据格式如下：{min:1,max:100}'
        }else if (computerType == "in" || computerType == "notin"){
        	msg='数据格式如下：data1,data2,data3';
        }else if (computerType == "gt" || computerType == "gte" ||computerType == "lt" ||computerType == "lte"){
        	msg='请维护数值';
        }else if (computerType == "regex"){
        	msg='请维护正则表达式';
        }else {
       		 msg='无需维护数据';
        }
		if(!init) {
			 $("#detail").val('');
		}
        $('#ruleSampleDiv').html(msg);
    }

    function changeRuleType(e) {
        if ($(e).val() == "1") {
            // 表规则
            $('#columnInput').hide();
        } else {
            $('#columnInput').show();
        }
        ruleType($(e).val());
    }

    function changeRuleTemplate(e) {
        if ($(e).val() == "") {
            $("#dimCode").removeAttr("disabled").css("background-color", "none");
            $("#computerType").removeAttr("disabled").css("background-color", "none");
            $("#detail").removeAttr("disabled").css("background-color", "none");
            $("#detail").val('');
        } else {
            selectRuleTemplate($(e).val());
        }
    }

    function ruleType(ruleType) {
        // 查询规则模版
        $.ajax({
            url: '${request.contextPath}/bigdata/metadata/getAllQualityRule?ruleType=' + ruleType,
            type: 'GET',
            dataType: 'json',
            asycn: false,
            success: function (response) {
                if (!response.success) {
                    layer.msg(response.message, {icon: 2});
                } else {
                    var data = response.data;
                    $('#ruleTemplateSelect').empty();
                    $('#ruleTemplateSelect').append("<option value=''>自定义规则</option>");
                    $.each(data, function (i, v) {
                        $('#ruleTemplateSelect').append("<option value='" + v.id + "'>" + v.ruleName + "</option>")
                    });
                    if ('${ruleRelation.ruleTemplateId!}' != '') {
                        $('#ruleTemplateSelect').val('${ruleRelation.ruleTemplateId!}');
                    }
                }
            }
        });
    }

    function selectRuleTemplate(templateId) {
        // 查询规则模版
        $.ajax({
            url: '${request.contextPath}/bigdata/metadata/getQualityRule?templateId=' + templateId,
            type: 'GET',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    layer.msg(response.message, {icon: 2});
                } else {
                    var data = response.data;
                    $('#dimCode').val(data.dimCode);
                    $('#computerType').val(data.computerType);
                    $('#detail').val(data.detail);
                    $("#dimCode").attr("disabled", "disabled").css("background-color", "#EEEEEE;");
                    $("#computerType").attr("disabled", "disabled").css("background-color", "#EEEEEE;");
                    $("#detail").attr("disabled", "disabled").css("background-color", "#EEEEEE;");
                }
            }
        });
    }

</script>