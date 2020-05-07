<form id="qualityDimSubmitForm">
 <input type="hidden" name="id" value="${dim.id!}">
<div class="form-horizontal form-made">
	<div class="form-group">
		<label class="col-sm-2 control-label">名称：</label>
		<div class="col-sm-8">
			<div class="mt-7">${dim.name!}</div>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label"><font style="color:red;">*</font>权重：</label>
		<div class="col-sm-8">
			<input type="text" name="weight" id="weight" class="form-control" nullable="true"
                   onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                   onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                   maxlength="2" value="${dim.weight!}">
		</div>
	</div>
</div>
</form>