<form id="qualityRuleSubmitForm">
 <input type="hidden" name="id" value="${rule.id!}">
<div class="form-horizontal form-made">
	<div class="form-group">
		<label class="col-sm-3 control-label"><font style="color:red;">*</font>规则名称：</label>
		<div class="col-sm-8">
			<input type="text" name="ruleName" id="ruleName" class="form-control" nullable="false" maxlength="30"
                   value="${rule.ruleName!?html}">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label"><font style="color:red;">*</font>规则类型：</label>
		<div class="col-sm-8">
			<select id="ruleType" class="form-control" name="ruleType">
                <option value="2" <#if rule.ruleType?default(1) == 2>selected="selected"</#if>>
                    字段规则
                </option>
                <option value="3" <#if rule.ruleType?default(1) == 3>selected="selected"</#if>>
                	任务规则
                </option>
            </select>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label"><font style="color:red;">*</font>所属维度：</label>
		<div class="col-sm-8">
			<select id="dimCode" class="form-control" name="dimCode">
      			<#list dimList as dim>
                <option value="${dim.code!}" <#if rule.dimCode! == dim.code!>selected="selected"</#if>>${dim.name!}</option>
                </#list>           
            </select>
		</div>
	</div>
	        <div class="form-group">
            <label class="col-sm-3 control-label"><font style="color:red;">*</font>计算规则：</label>
            <div class="col-sm-8">
                <select id="computerType" class="form-control" name="computerType" onchange="changeComputerType(this.value,false)">           
                  ${mcodeSetting.getMcodeSelect("DM-BG-JSGZ", (rule.computerType?default('notnull'))?string, "0")}
                </select>
            </div>
        </div>
	<div class="form-group">
		<label class="col-sm-3 control-label">规则明细：</label>
		<div class="col-sm-8">
			<textarea rows="2" class="form-control" name="detail" id="detail" nullable="true">${rule.detail!}</textarea>
		</div>
	</div>
	<div class="form-group">
        <label class="col-sm-3 control-label ">规则说明：</label>
        <div class="col-sm-8">
			<div class="mt-7" style="color:blue;" id="ruleSampleDiv"></div>
		</div>
    </div>
	<div class="form-group">
		<label class="col-sm-3 control-label"><font style="color:red;">*</font>排序号：</label>
		<div class="col-sm-8">
			<input type="text" name="orderId" id="orderId" class="form-control" nullable="true"
                   onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
				   oninput="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                   onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                   maxlength="3" value="${rule.orderId!}">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label">备注：</label>
		<div class="col-sm-8">
			<textarea rows="2" class="form-control" name="remark" id="remark" nullable="true">${rule.remark!}</textarea>
		</div>
	</div>
</div>
</form>
<script>

	$(function () {
        changeComputerType('${rule.computerType?default('notnull')}',true);
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
			$('#detail').val('');
		}
        $('#ruleSampleDiv').html(msg);
    }
</script>