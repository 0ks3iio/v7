<div class="tab-content">
    <div id="dd" class="tab-pane active">
        <input type="hidden" name="id" value="${multiReport.id!}" id="multiReportId">
        <input type="hidden" name="type" value="${multiReport.type!}">
        <input type="hidden" name="unitId" value="${multiReport.unitId!}">
        <div class="layer-title">
            <b>名称 </b>
        </div>
        <div class="layer-title">
            <input type="text" name="name" id="rName" class="form-control" nullable="false" maxlength="50"
                   value="${multiReport.name!}">
        </div>
        <div class="layer-title">
            <b>所属文件夹 </b>
        </div>
        <div class="layer-title">
            <select name="folderId" id="folderId" onchange="changeFolder()"
                    class="form-control new-input-style"
                    data-placeholder="<#if folderTree?exists && folderTree?size gt 0>请选择文件夹<#else>请先维护文件夹</#if>">
                                <#if folderTree?exists && folderTree?size gt 0>
                                    <#list folderTree as dir>
                                        <optgroup label="${dir.folderName!}">
                                            <#if folderMap[dir.id]?exists>
                                                <#list folderMap[dir.id] as item>
                                                     <option <#if folderDetail.folderId! == item.id!>selected</#if>
                                                             value="${item.id!}">
                                                         &nbsp;&nbsp;&nbsp;${item.folderName!}</option>
                                                </#list>
                                            </#if>
                                            <#list dir.childFolder as secondDir>
                                                <optgroup label="&nbsp;&nbsp;&nbsp;${secondDir.folderName!}">
                                                    <#if folderMap[secondDir.id]?exists>
                                                        <#list folderMap[secondDir.id] as secondDirItem>
                                                               <option
                                                                   <#if folderDetail.folderId! == secondDirItem.id!>selected</#if>
                                                                   value="${secondDirItem.id!}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${secondDirItem.folderName!}</option>
                                                        </#list>
                                                    </#if>
                                                    <#list secondDir.childFolder as thirdDir>
                                                        <optgroup
                                                                label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${thirdDir.folderName!}">
                                                            <#if folderMap[thirdDir.id]?exists>
                                                                  <#list folderMap[thirdDir.id] as thirdDirItem>
                                                                       <option
                                                                           <#if folderDetail.folderId! == thirdDirItem.id!>selected</#if>
                                                                           value="${thirdDirItem.id!}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${thirdDirItem.folderName!}</option>
                                                                  </#list>
                                                            </#if>
                                                        </optgroup>
                                                    </#list>
                                                </optgroup>
                                            </#list>
                                        </optgroup>
                                    </#list>
                                </#if>
            </select>
        </div>

        <div class="layer-title">
            <b>排序号 </b>
        </div>
        <div class="layer-title">
            <input type="text" name="orderId" id="rOrderId" class="form-control" nullable="true"
                   onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                   onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                   maxlength="3" value="${multiReport.orderId!}">
        </div>
        <div class="layer-title">
            <b>备注 </b>
        </div>
        <div class="layer-title">
            <textarea name="remark" id="remark" type="text/plain" nullable="true" maxLength="200"
                      style="width:100%;height:80px;">${multiReport.remark!}</textarea>
        </div>
    </div>
</div>
<input type="hidden" value="${type!}" id="type" />
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css"/>
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<script>

    var _chart_json = ${multiReport.toJSONString()};

    function changeFolder() {
        $.ajax({
            url: '${request.contextPath}/bigdata/folder/getMaxOrderId',
            data: {folderId: $('#folderId').val()},
            dataType: 'json',
            success: function (val) {
                if (val.success) {
                    $('#rOrderId').val(val.data);
                } else {
                    layer.msg(val.message, {icon: 2, time: 1000});
                }
            }
        });
    }

    if ($('#rOrderId').val() == '' || $('#rOrderId').val() == '0') {
        changeFolder();
    }

</script>
<script src="${request.contextPath}/bigdata/v3/static/js/chartEdit.basicEdit.js"/>