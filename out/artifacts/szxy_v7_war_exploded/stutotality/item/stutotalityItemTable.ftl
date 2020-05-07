<#if listStutotality?exists && listStutotality?size gt 0>
<div class="evaluate-table-heared">
    <#if canSave == 0>
    <button class="btn btn-white evaluate-table-edit detailshow" onclick="hidedetail()" >
        编辑
    </button>

    </#if>

    <button class="btn btn-blue evaluate-table-edit detailhide" onclick="showdetail()" >
        保存
    </button>
    <div class="evaluate-table-title itemName">

        ${acadyear!}学年${mcodeSetting.getMcode("DM-XQ",semester?default('1'))}${gradeName!}年级评价项目表
    </div>
</div>
<table id="maintable" class="table table-bordered table-striped table-hover">
    <thead><tr> <th style="width: 130px;">类别</th> <th  style="width: 118px;">项目</th><th  style="width: 118px;">项目内容</th><th>评价标准</th> <th  style="width: 78px;">分值</th> </tr> </thead>
    <tbody>
        <#list listStutotality as type>
            <tr>
            <#assign  typeRow=0>
            <#if type.stutotalityItems?exists && type.stutotalityItems?size gt 0>
                <#list type.stutotalityItems as item>
                    <#if item.stutotalityItemOptions?exists && item.stutotalityItemOptions?size gt 0><#assign  typeRow+=item.stutotalityItemOptions?size></#if>
                </#list>
            </#if>
            <#if type.stutotalityItems?exists && type.stutotalityItems?size gt 0>
            <td rowspan="${typeRow!}">
                <span class="detailshow">${type.typeName!}</span>
            </td>

             <#list type.stutotalityItems as item>
                        <#if item.stutotalityItemOptions?exists && item.stutotalityItemOptions?size gt 0>
                    <td rowspan="${item.stutotalityItemOptions?size}">
                        <span class="detailshow">${item.itemName!}</span>
                    </td>
                        <#list item.stutotalityItemOptions as option>
                            <#if option_index!=0><tr></#if>
                            <td>
                                <span class="detailshow">${option.optionName!}</span>
                            </td>
                            <td>
                                <span class="detailshow">${option.description!}</span>
                            </td>
                            <td>
                                <span class="detailshow">5</span>
                            </td>
                            </tr>
                        </#list>
                    </#if>
                </#list>
                <#else >
                    <td rowspan="1">
                        <span class="detailshow">${type.typeName!}</span>
                    </td>
                        <td rowspan="1"></td> <td></td> <td></td> <td></td>
            </#if>
        </#list>
    </tbody>
</table>
<#else >
    <div class="eva-no-content">
        <img src="/static/images/evaluate/nocontent.png">
        <div class="mb10">可同步上届该年级已维护的数据或新建数据哦~</div>
        <#if canSync == 0>
            <button class="btn btn-blue mr10" onclick="syncTemplate('before')">同步上届数据</button>
        <#else >
            <button class="btn btn-blue mr10 disabled" >同步上届数据</button>
        </#if>
        <button class="btn btn-white" onclick="syncTemplate('now')">同步模板数据</button>
    </div>

</#if>
<script type="text/javascript">

    $(function() {
        $(window).resize(function() {
            $(".evaluate-body").css("min-height", $("#sidebar").height() - 103);
        });
    })
    //编辑
    function hidedetail() {
        var acadyear = $('#acadyear').val();
        var semester = $('#semester').val();
        var gradeId =$(".outter").find("a.selected").attr("data-id");
        var gradeName =$(".outter").find("a.selected").attr("data-name");
        var url = '${request.contextPath}/stutotality/typetemplate/show?acadyear='+acadyear+'&semester='+semester+'&gradeId='+gradeId+'&gradeName='+gradeName;
        $('#templateTabShow').load(url);
    }

    //同步模板
    var isSubmit=false;
    function syncTemplate(syncType) {
        if(isSubmit){
            return;
        }
        isSubmit=true;
        var acadyear = $('#acadyear').val();
        var semester = $('#semester').val();
        var gradeId =$(".outter").find("a.selected").attr("data-id");
        if(!gradeId){
            layerTipMsg(false,"提示","年级不能为空");
            isSubmit=false;
            return;
        }
        var gradeCode =$(".outter").find("a.selected").attr("data-code");
        $.ajax({
            url:"${request.contextPath}/stutotality/template/sync",
            data:{"acadyear":acadyear,"semester":semester,"gradeId":gradeId,'syncType':syncType,'gradeCode':gradeCode},
            type:'post',
            dataType:'json',
            success:function(data) {
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                    isSubmit=false;
                    return;
                }else{
                    layer.closeAll();
                    layer.msg("同步成功", {
                        offset: 't',
                        time: 2000
                    });
                    isSubmit=false;
                    itemTableShow();
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                layer.msg(XMLHttpRequest.status);
            }
        });
    }
</script>