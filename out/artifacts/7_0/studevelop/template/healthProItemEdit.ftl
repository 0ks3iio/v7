<form id="subForm" method="post">
    <div class="layer-content" id="myDiv">
        <div class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">项目</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control" id="itemName" name="itemName" value="${studevelopTemplateItem.itemName!}" maxLength="225" nullable="false">
                </div>
            </div>
            <#--<div class="form-group">-->
                <#--<label class="col-sm-2 control-label no-padding-right">显示对象</label>-->
                <#--<div class="col-sm-9">-->
                    <#--<select name="objectType" id="objectType" class="form-control" nullable="false" onChange="">-->
                        <#--<option value="">--请选择--</option>-->
                        <#--<option value="1" <#if studevelopTemplateItem?exists && studevelopTemplateItem.objectType?default('') == '1'>selected</#if>>仅对学科类别显示</option>-->
                        <#--<option value="2" <#if studevelopTemplateItem?exists && studevelopTemplateItem.objectType?default('') == '2'>selected</#if>>仅对学科显示</option>-->
                    <#--</select>-->
                <#--</div>-->
            <#--</div>-->
                <div class="form-group" >
                    <label class="col-sm-2 control-label no-padding-right">维护方式</label>
                    <div class="col-sm-9 col-sm-offset-1">
                        <label><input type="radio" name="singleOrInput" class="wp" onchange="doChange(this);"  <#if studevelopTemplateItem?exists && studevelopTemplateItem.singleOrInput?default('1') == '1'>checked</#if>  value=1><span class="lbl" >输入</span></label>
                        <label><input type="radio" name="singleOrInput" class="wp" onchange="doChange(this);" <#if studevelopTemplateItem?exists && studevelopTemplateItem.singleOrInput?default('1') == '2'>checked</#if> value=2><span class="lbl" >单选</span></label>
                    </div>
                </div>
                <div class="form-group" id="singleContainer" style="display: none" >
                    <span class="col-sm-2 control-label no-padding-right">单选内容：</span>
                    <div class="col-sm-9">
                        <table class="table table-bordered no-margin">
                            <tbody id="addHtml">
						<#if optionsList?exists && optionsList?size gt 0>
                            <#list optionsList as item>
						        <tr>
                                    <input type="hidden" name="templateOptions[${item_index!}].id" value="${item.id!}">
                                    <input type="hidden" name="templateOptions[${item_index!}].templateItemId" value="${item.templateItemId!}">
                                    <input type="hidden" name="templateOptions[${item_index!}].creationTime" value="${item.creationTime!}">
                                    <td><input type="text" class="form-control" style="width:160px;" name="templateOptions[${item_index!}].optionName" id="Name${item_index!}" value="${item.optionName!}" maxLength="225" nullable="false"></td>
                                <#--td><label><input type="checkbox" class="wp" <#if item.state == '1'>checked</#if> onClick="changel(this);" name="stuDevelopCateGoryList[${item_index!}].state" id="state${item_index!}" value="${item.state!}"><span class="lbl"> 是否只取一个成绩</span></label></td-->
                                    <td><a href="javascript:void(0)" class="color-red" onClick="trDelete(this ,'${item.id!}')">删除</a></td>
                                </tr>
                            </#list>
                        <#else>
                             <tr>
                                 <input type="hidden" name="templateOptions[0].id" value="">
                                 <input type="hidden" name="templateOptions[0].templateItemId" value="">
                                 <input type="hidden" name="templateOptions[0].creationTime" value="">
                                 <td><input type="text" class="form-control" style="width:160px;" name="templateOptions[0].optionName" id="Name00" value="" maxLength="225" nullable="false"></td>
                             <#--td><label><input type="checkbox" class="wp" <#if item.state == '1'>checked</#if> onClick="changel(this);" name="stuDevelopCateGoryList[${item_index!}].state" id="state${item_index!}" value="${item.state!}"><span class="lbl"> 是否只取一个成绩</span></label></td-->
                                 <td><a href="javascript:void(0)" class="color-red" onClick="trDelete(this ,'')">删除</a></td>
                             </tr>
                        </#if>
                            </tbody>
                        </table>
                        <a href="javascript:void(0);" id="js-add">新增单选内容</a>
                    </div>

                </div>

            </div>
        </div>
    </div>
</form>
<script>
    var isSubmit=false;
    function saveProject(){
        var check = checkValue('#myDiv');
        if(!check){
            $(this).removeClass("disabled");
            isSubmit=false;
            return;
        }
        var options = {
            url : "${request.contextPath}/studevelop/projectItem/save",
            dataType : 'json',
            success : function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                    return;
                }else{
                    layer.closeAll();
                    layer.msg(jsonO.msg, {
						offset: 't',
						time: 2000
					});
                    searchList();
                }
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $("#subForm").ajaxSubmit(options);
    }
    function doChange(data) {
        var val = $(data).val();
        if(val == "1"){
            $("#singleContainer").hide();
        }else{
            $("#singleContainer").show();
        }


    }
    function trDelete(data ,id) {
        if(id != ""){
            var  options = {
                url:"${request.contextPath}/studevelop/projectItem/optionDelete",
                async:false,
                type:post,
                success:function(data){
                    var jsonO = data;
                    if(!jsonO.success){
                        layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
                        return;
                    }else{
                        layer.closeAll();
                        layer.msg(jsonO.msg, {
							offset: 't',
							time: 2000
						});
                        searchList();
                    }

                },
                dataType : 'json',
                clearForm : false,
                resetForm : false,
                error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
            }
        }
        $(this).parent("tr").remove();

    }

    var index = 0;
<#if optionsList?exists && optionsList?size gt 0>
    index = ${optionsList?size!};
</#if>
    $(function(){
        $('#js-add').on('click',function(e){
            e.preventDefault();
            addCategory();
            index++;
        });
    });
    function addCategory(){
        var addhtml = '<tr>\
                        <td><input type="text" class="form-control" style="width:160px;" name="templateOptions['+index+'].optionName" id="Name'+index+'" maxLength="225" nullable="false"></td>\
                        <#--td><label><input type="checkbox" class="wp" onClick="changel(this);" name="stuDevelopCateGoryList['+index+'].state" id="state'+index+'"><span class="lbl"> 是否只取一个成绩</span></label></td>\-->
                        <td><a href="javascript:void(0)" class="color-red" onClick="trDelete(this)">删除</a></td>\
                   </tr>';
    $('#addHtml').append(addhtml);
}

</script>
