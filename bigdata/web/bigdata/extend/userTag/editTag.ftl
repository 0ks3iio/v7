<form id="tagForm">
    <div class="form-horizontal form-made">
        <input type="hidden" id="tag-edit-id" name="id" value="${tag.id!}">
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>名称：</label>
            <div class="col-sm-8">
                <input type="text" name="name" id="tag-edit-name" class="form-control" nullable="false" maxlength="50"
                       value="${tag.tagName!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>标签列名：</label>
            <div class="col-sm-8">
                <input type="text" name="tableName" id="tag-edit-target-column" class="form-control" nullable="false" maxlength="50"
                       value="${tag.targetColumn!}">
            </div>
        </div>
    </div>
</div>