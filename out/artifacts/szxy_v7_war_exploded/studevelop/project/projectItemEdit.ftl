<form id="subForm" method="post">
    <input type="hidden" name="acadyear" value="${acadyear!}">
    <input type="hidden" name="semester" value="${semester!}">
    <input type="hidden" name="gradeId" value="${gradeId!}">
    <input type="hidden" name="section" value="${section!}">
    <input type="hidden" name="code" value="${code!}">
    <input type="hidden" name="isClosed" value="${studevelopTemplateItem.isClosed!}">
    <input type="hidden" name="templateId" value="${studevelopTemplateItem.templateId!}">
    <input type="hidden" name="creationTime" value="${studevelopTemplateItem.creationTime!}">
    <input type="hidden" name="id" value="${studevelopTemplateItem.id!}">
    <div class="layer-content" id="myDiv">
        <div class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">项目</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control" id="itemName" name="itemName" value="${studevelopTemplateItem.itemName!}" maxLength="100" nullable="false">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">排序号</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control" id="sortNumber" name="sortNumber" vtype="int" min="1" max="99" value="${studevelopTemplateItem.sortNumber!}"  nullable="false">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">显示对象</label>
                <div class="col-sm-9">
                    <select name="objectType" id="objectType" class="form-control" nullable="false" onChange="">
                        <option value="">--请选择--</option>
                        <option value="11" <#if studevelopTemplateItem?exists && studevelopTemplateItem.objectType?default('') == '11'>selected</#if>>仅对学科类别显示</option>
                        <option value="12" <#if studevelopTemplateItem?exists && studevelopTemplateItem.objectType?default('') == '12'>selected</#if>>仅对学科显示</option>
                    </select>
                </div>
            </div>

                    <div class="form-group" >
                        <label class="col-sm-2 control-label no-padding-right">维护方式</label>
                        <div class="col-sm-9 col-sm-offset-1">
                            <label><input type="radio" name="singleOrInput" class="wp" onchange="doChange(this);"  <#if studevelopTemplateItem?exists && studevelopTemplateItem.singleOrInput?default('1') == '1'>checked</#if>  value=1><span class="lbl" >输入</span></label>
                            <label><input type="radio" name="singleOrInput" class="wp" onchange="doChange(this);" <#if studevelopTemplateItem?exists && studevelopTemplateItem.singleOrInput?default('1') == '2'>checked</#if> value=2><span class="lbl" >单选</span></label>
                        </div>
                    </div>
                    <div class="form-group" id="singleContainer" style="display: none" >
                        <span class="col-sm-2 control-label no-padding-right">单选内容</span>
                        <div class="col-sm-9">
                            <table class="table table-bordered no-margin">
                                <tbody id="addHtml">
						<#if optionsList?exists && optionsList?size gt 0>
                            <#list optionsList as item>
						        <tr>
                                    <input type="hidden" name="templateOptions[${item_index!}].id" value="${item.id!}">
                                    <input type="hidden" name="templateOptions[${item_index!}].templateItemId" value="${item.templateItemId!}">
                                    <input type="hidden" name="templateOptions[${item_index!}].creationTime" value="${item.creationTime!}">
                                    <td><input type="text" class="form-control" style="width:160px;" name="templateOptions[${item_index!}].optionName" id="Name${item_index!}" value="${item.optionName!}" maxLength="100" nullable="false"></td>
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
        var v = $('input[name="singleOrInput"]:checked').val();
        if(v == '2'){
            var tr = $("#addHtml").find("tr").size();
            if(tr <= 0){
                layerTipMsgWarn("提示" ,"请维护1-3个选项");
                return ;
            } }
        var options = {
            url : "${request.contextPath}/stuDevelop/commonProject/projectItem/save",
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
                url:"${request.contextPath}/stuDevelop/commonProject/projectItem/optionDelete?id="+id,
                async:false,
                type: "post",
                success:function(data){
                    var jsonO = data;
                    if(!jsonO.success){
                        layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
                        return;
                    }else{
                        layer.msg(jsonO.msg, {
							offset: 't',
							time: 2000
						});
                    }

                },
                dataType : 'json',
                clearForm : false,
                resetForm : false,
                error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
            }
            $.ajax(options);
        }

        $(data).parent().parent("tr").remove();
    }

    var index = 1;
<#if optionsList?exists && optionsList?size gt 0>
    index = ${optionsList?size!};
</#if>
    $(function(){
        $('#js-add').on('click',function(e){
            e.preventDefault();
            var tr = $("#addHtml tr").size();
            if(tr > 3){
                layerTipMsgWarn("提示" ,"请维护1-3个选项");
                return ;
            }

            addCategory();
            index++;
        });
        <#if studevelopTemplateItem?exists &&  studevelopTemplateItem.singleOrInput?default('1') == '2' >
            $("#singleContainer").show();
        </#if>

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
