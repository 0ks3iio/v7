<form id="metadataForm">
    <div class="form-horizontal form-made">

        <input type="hidden" name="id" value="<#if relatedTable??>${relatedTable.id!}</#if>"/>
        <input type="hidden" name="creationTime"value="<#if relatedTable??>${relatedTable.creationTime!}</#if>">
        <input type="hidden" name="modifyTime" value="">
        <input type="hidden" name="mdId" value="${masterTable.id}"/>
         <#--<<input type="hidden" name="metadataId" value="${masterTable.metadataId!}"/>
        <input type="hidden" name="mdType" value="${masterTable.mdType!}"/>-->
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">主表：</label>
            <div class="col-sm-6">
                <input type="text" disabled="disabled" name="" id="masterTableName"  class="form-control" nullable="false" maxlength="30"
                       value="${masterTable.tableName!}"/></div>
            </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>关联表：</label>
            <div class="col-sm-6">
                <select id="relatedFollowerTable" class="form-control" name="relatedMdId" onchange="changeFollowerTable(this)">
                    <#if followerTableList??>
                        <#list followerTableList as followerTable>
                            <#if relatedTable?? && followerTable.id == relatedTable.relatedMdId>
                                <option value="${followerTable.id!}" selected="selected">${followerTable.tableName!}</option>
                            <#else>
                                <option value="${followerTable.id!}">${followerTable.tableName!}</option>
                            </#if>
                    </#list>
                    </#if>
                </select>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font color="red">*</font>主表列：</label>
            <div class="col-sm-6">
                <select id="masterTableColumn" class="form-control" name="mdColumnId">
                    <#if masterTableColumns??>
                        <#list masterTableColumns as column>
                            <#if relatedTable?? && column.id == relatedTable.mdColumnId>
                                <option value="${column.id!}" selected="selected">${column.columnName!}</option>
                            <#else>
                                <option value="${column.id!}">${column.columnName!}</option>
                            </#if>
                        </#list>
                    </#if>
                </select>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font color="red">*</font>关联表列：</label>
            <div class="col-sm-6">
                <select id="followerTableColumn" class="form-control" name="relatedColumnId">
                    <#if relatedTable??>
                        <option value="${relatedTable.relatedColumnId!}"></option>
                        <#else>
                            <option value="">请选择对应的列</option>
                    </#if>
                </select>
            </div>
        </div>

    </div>
</form>
<script>
    $(function () {
        /*编辑关联关系，先加载关联从表列*/
        var element = document.getElementById("relatedFollowerTable");
        changeFollowerTable(element);
    })
    function selectFollowerColumn() {

    }
</script>