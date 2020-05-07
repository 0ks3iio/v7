<form id="roleSubmitForm">
    <input type="hidden" name="id" value="${role.id!}">
    <input type="hidden" name="unitId" value="${role.unitId!}">
<div class="form-horizontal form-made">
	<div class="form-group">
		<label class="col-sm-2 control-label"><font style="color:red;">*</font>组名称：</label>
		<div class="col-sm-8">
			<input type="text" name="name" id="name" class="form-control" nullable="false" maxlength="16"
                       value="${role.name!}">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label"><font style="color:red;">*</font>所属端：</label>
		<div class="col-sm-8">
			<select id="belong" class="form-control" name="belong">
                <option value="0" <#if role.belong?default(0) == 0>selected="selected"</#if>>
                    后端
                </option>
                <option value="1" <#if role.belong?default(0) == 1>selected="selected"</#if>>
                	前端
                </option>
            </select>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label"><font style="color:red;">*</font>排序号：</label>
		<div class="col-sm-8">
			<input type="text" name="orderId" id="orderId" class="form-control" nullable="true"
                       onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       maxlength="3" value="${role.orderId!}">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">备注：</label>
		<div class="col-sm-8">
			<textarea rows="2" class="form-control" name="description" id="" nullable="true" maxlength="200">${role.description!}</textarea>
		</div>
	</div>
</div>
</form>