<form id="nodeServerSubmitForm">
 <input type="hidden" name="id" value="${nodeServer.id!}">
<div class="form-horizontal form-made">
	<div class="form-group">
		<label class="col-sm-3 control-label"><font style="color:red;">*</font>名称：</label>
		<div class="col-sm-8">
			<input type="text" name="name" id="name" class="form-control" nullable="false" maxlength="25"
                   value="${nodeServer.name!}">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label"><font style="color:red;">*</font>类型：</label>
		<div class="col-sm-8">
			<select id="type" class="form-control" name="type">
				<#if nodeServerTypes?? && nodeServerTypes?size gt 0>
					<#list nodeServerTypes as nodeServerType>
						<option value="${nodeServerType.code}"
                                <#if nodeServer.type??&&nodeServer.type==nodeServerType.code>selected</#if>
                        >${nodeServerType.name}</option>
					</#list>
				</#if>
            </select>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label"><font style="color:red;">*</font>状态：</label>
		<div class="col-sm-8">
			<select id="status" class="form-control" name="status">
				<option value="1" <#if nodeServer.status?default(1) == 1>selected="selected"</#if>>
					可用
				</option>
				<option value="0" <#if nodeServer.status?default(1) == 0>selected="selected"</#if>>
					不可用
				</option>
			</select>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label">备注：</label>
		<div class="col-sm-8">
			<textarea rows="2" class="form-control" name="remark" id="remark" nullable="true" style="height:100px" maxlength="250">${nodeServer.remark!}</textarea>
		</div>
	</div>
</div>
</form>
<script>
</script>