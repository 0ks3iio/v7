<form id="userDatasetForm">
    <div class="form-horizontal form-made">
        <input type="hidden" name="id" value="${dataset.id!}">
        <input type="hidden" name="modelId" value="${dataset.modelId!}">
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>名称：</label>
            <div class="col-sm-7">
                <input type="text" name="dsName" id="dsName" class="form-control" nullable="false" maxlength="50"
                       value="${dataset.dsName!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>条件sql：</label>
            <div class="col-sm-7">
                <textarea name="dsConditionSql" id="dsConditionSql" type="text/plain" nullable="true" maxlength="200"
                          style="width:100%;height:70px;">${dataset.dsConditionSql!}</textarea>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>排序号：</label>
            <div class="col-sm-7">
                <input type="text" name="orderId" id="orderId" class="form-control" nullable="true"
                       onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       maxlength="5" value="${dataset.orderId!}">
            </div>
        </div>

    </div>
</form>