<form id="eventTypeForm">
    <div class="form-horizontal">
        <input type="hidden" name="id" value="${eventType.id!}">
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>名称：</label>
            <div class="col-sm-6">
                <input type="text" name="typeName" id="typeName" class="form-control" nullable="false" maxlength="50"
                       value="${eventType.typeName!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>排序号：</label>
            <div class="col-sm-6">
                <input type="text" name="orderId" id="orderId" class="form-control" nullable="true"
                       onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       maxlength="3" value="${eventType.orderId!}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">备注：</label>
            <div class="col-sm-6">
                <textarea name="remark" id="remark" type="text/plain" nullable="true" maxlength="200"
                          style="width:100%;height:50px;resize: none;">${eventType.remark!}</textarea>
            </div>
        </div>
    </div>
</form>
