<div class="form-group">
    <label class="col-sm-2 control-label no-padding-right">显示对象</label>
    <div class="col-sm-9">
        <select name="objectType" id="objectType" class="form-control" nullable="false" onChange="">
            <option value="">--请选择--</option>
            <option value="1" <#if studevelopTemplateItem?exists && studevelopTemplateItem.objectType?default('') == '1'>selected</#if>>仅对学科类别显示</option>
            <option value="2" <#if studevelopTemplateItem?exists && studevelopTemplateItem.objectType?default('') == '2'>selected</#if>>仅对学科显示</option>
        </select>
    </div>
</div>