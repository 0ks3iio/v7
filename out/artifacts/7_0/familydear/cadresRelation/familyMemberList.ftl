
<#assign OBJECT_STATE_RELATIONED = stack.findValue("@net.zdsoft.familydear.common.FamDearConstant@OBJECT_STATE_RELATIONED") />
<#assign OBJECT_STATE_FLOZEN = stack.findValue("@net.zdsoft.familydear.common.FamDearConstant@OBJECT_STATE_FLOZEN") />
<#assign OBJECT_STATE_INITIA = stack.findValue("@net.zdsoft.familydear.common.FamDearConstant@OBJECT_STATE_INITIA") />
<div class="layer-addTerm layer-change" style="display:block;" id="myDiv">
    <form id="subForm">
        <input type="hidden" name="id" value="${objId!}" />
        <#if familyDearObject.state?default('0') != OBJECT_STATE_FLOZEN>
            <div class="row">
                <div class="col-sm-12" >
                    <div class="box-body">
                        <div class="filter ">
                            <div class="row">
                                <div class="filter-item">
                                    <a href="javascript:addTr();" class="btn btn-blue btn-seach">+新增</a>
                                    <a href="javascript:batchDeleta();" class="btn btn-blue btn-seach">批量删除</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </#if>


        <#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
        <div class="table-container-body" style="height:400px;overflow-y:auto;overflow-x: hidden;" >
            <table class="table table-bordered table-striped table-hover " >
                <thead>
                <tr>
                    <th width="10%" >
                        <label class="pos-rel form">
                            <input name="typeArray" id="selectAllMember" type="checkbox" class="wp col-sm-2" value="">
                            <span class="lbl"></span>
                        </label></th>
                    <th width="30%" >姓名</th>
                    <th width="20%" >与结亲对象关系</th>
                    <th width="30%" >联系电话</th>
                    <th width="10%" >工作单位</th>
                </tr>
                </thead>
                <tbody id="familyMemberTable">
                <#if  memberList?exists && (memberList?size gt 0)>
                    <#list memberList as item>
                        <tr id="trTemp${item.id!}">
                            <input type="hidden" name="familyTempList[${item_index}].id" value="${item.id!}" />
                            <input type="hidden" name="familyTempList[${item_index!}].creationTime" value="${item.creationTime!}" />
                            <input type="hidden" name="familyTempList[${item_index!}].objectId" value="${item.objectId!}" />
                            <td ><label class="pos-rel form">
                                    <input name="itemId" type="checkbox" class="wp col-sm-2" value="${item.id!}">
                                    <span class="lbl"></span>
                                </label></td>
                            <td ><input type="text" name="familyTempList[${item_index!}].name" id="name${item_index!}" nullable="false" maxLength="100"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="${item.name!}" style="width:168px;"/></td>
                            <td ><select name="familyTempList[${item_index!}].relation" nullable="false"   id="relation${item_index!}"  class="form-control">
                                    ${mcodeSetting.getMcodeSelect("DM-GX", '${item.relation!}', "1")}
                                </select></td>
                            <td ><input type="text" name="familyTempList[${item_index!}].company"  id="company${item_index!}"   maxLength="200"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="${item.company!}" style="width:168px;"/></td>
                            <td ><input type="text" name="familyTempList[${item_index!}].mobilePhone"   id="mobilePhone${item_index!}"  maxLength="30"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="${item.mobilePhone!}" style="width:168px;"/></td>
                        </tr>
                    </#list>
                <#else>
                    <tr  id="trTemp0">
                        <td ><label class="pos-rel form">
                                <input name="itemId" type="checkbox" class="wp col-sm-2" value="">
                                <span class="lbl"></span>
                            </label></td>
                        <td ><input type="text" name="familyTempList[0].name" nullable="false"  id="name0"  maxLength="100"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:168px;"/></td>
                        <td >  <select name="familyTempList[0].relation" nullable="false" id="relation0"  class="form-control">
                                ${mcodeSetting.getMcodeSelect("DM-GX", '', "1")}
                            </select></td>
                        <td ><input type="text" name="familyTempList[0].company"   id="company0"  maxLength="200"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:168px;"/></td>
                        <td ><input type="text" name="familyTempList[0].mobilePhone" id="mobilePhone0"   maxLength="30"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:168px;"/></td>
                    </tr>
                </#if>

                </tbody>
            </table>

            <#--<#if  itemList?exists && (itemList?size gt 0)>-->
            <#--<@htmlcom.pageToolBar container="#itemDiv"/>-->
            <#--</#if>-->

        </div>
    </form>
</div>
<div class="layer-footer">

        <a class="btn btn-lightblue" <#if familyDearObject.state?default('0') == OBJECT_STATE_FLOZEN> style="display:none" </#if>  id="arrange-commit">确定</a>


    <a class="btn btn-grey" id="arrange-close">取消</a>
</div>
<script>
    var strOption = '<option value="">--- 请选择 ---</option>';
    <#if mcodelList?exists && mcodelList?size gt 0>
        <#list mcodelList as mcode>
        strOption += ' <option title="${mcode.mcodeContent!}" value="${mcode.thisId!}">${mcode.mcodeContent!}</option>'
        </#list>
    </#if>

    var index = 1;
    <#if  memberList?exists && (memberList?size gt 0)>
    index = ${memberList?size};
    </#if>
    function addTr() {
        var html = '<tr>' +
            '<td ><label class="pos-rel form">' +
            '<input name="itemId" type="checkbox" class="wp col-sm-2" value="">' +
            '                        <span class="lbl"></span>' +
            '                    </label></td>'+
                '   <td ><input type="text" name="familyTempList['+ index +

            '].name" id="name' +index+   '" nullable="false" maxLength="100"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:168px;"/></td>'+
                '<td >' +
            '                    <select name="familyTempList[' + index +'].relation" nullable="false" id="relation' +index+  '" class="form-control">'+strOption +
                ' </select></td>'+
                '<td ><input type="text" name="familyTempList['+index+'].company" id="company' + index+ '"  maxLength="200"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:168px;"/></td>'+
                '<td ><input type="text" name="familyTempList['+index+'].mobilePhone"  id="mobilePhone' +index+ '"  maxLength="30"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:168px;"/></td>' +
                '</tr>';
        $("#familyMemberTable").append(html);
        index++;
        return;
    }

    function batchDeleta() {
        var ids = ""
        var selEle = $(' [name="itemId"]:checkbox:checked');
        if(selEle.size()<1){
            layerTipMsg(false,"请选择一个家庭成员!","");
            return;
        }
        for(var i=0;i<selEle.size();i++){
            var value = selEle.eq(i).val();
            if (value == '') {
                selEle.eq(i).parent().parent().parent().remove();
                continue;
            }
            if (i == selEle.size() - 1) {
                ids +=selEle.eq(i).val();
            }else{
                ids +=selEle.eq(i).val() +",";
            }
        }
        if (ids == '') {
            return;
        }
        var options = {
            url : "${request.contextPath}/familydear/cadresRelation/objMemberDelete",
            dataType : 'json',
            data:{"ids":ids},
            success : function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
                    $("#arrange-commit").removeClass("disabled");
                    return;
                }else{
                    //layer.closeAll();
                    var arr = ids.split(",");
                    for (var i = 0; i< arr.length; i++) {
                        $('#trTemp' + arr[i]).remove();
                    }
                    layerTipMsg(jsonO.success,"删除成功",jsonO.msg);

                    // searchList();
                }
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        // $().ajaxSubmit(options);
        $.ajax(options)
    }
    // 取消按钮操作功能
    $("#arrange-close").on("click", function(){
        doLayerOk("#arrange-commit", {
            redirect:function(){},
            window:function(){layer.closeAll()}
        });
    });


    var isSubmit=false;
    $("#arrange-commit").on("click", function(){
        var check = checkValue('#subForm');
        if(!check){
            $(this).removeClass("disabled");
            isSubmit=false;
            return;
        }
        var options = {
            url : "${request.contextPath}/familydear/cadresRelation/objMemberAdd",
            dataType : 'json',
            success : function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                    $("#arrange-commit").removeClass("disabled");
                    return;
                }else{
                    layer.closeAll();
                    layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
                    searchRelationList();
                }
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $("#subForm").ajaxSubmit(options);
    });
    //全选
    $('#selectAllMember').on('click',function(){
        var total = $('[name="itemId"]:checkbox').length;
        var length = $(' [name="itemId"]:checkbox:checked').length;
        if(length != total){
            $('[name="itemId"]:checkbox').prop("checked", "true");
            $(this).prop("checked", "true");
        }else{
            $('[name="itemId"]:checkbox').removeAttr("checked");
            $(this).removeAttr("checked");
        }
    });

    function　searchRelationList(){
        var objName = $('#objName').val();
        var cadreName = $('#cadreName').val();
        var villageName = $('#villageName').val();
        var label = $('#label').val();
        var type = $('#type').val();
        var teacherId = $('#teacherId').val();
        var deptId = $("#deptId").val();
        var currentPageIndex = ${currentPageIndex!};
        var currentPageSize = ${currentPageSize!};
        var tabType = $("#tabType").val();
        var str = "?tabType="+tabType+"&objName="+encodeURIComponent(encodeURIComponent(objName))+"&cadreName="+encodeURIComponent(encodeURIComponent(cadreName))+"&villageName="+villageName+"&label="+label + "&type="+type +"&teacherId="+teacherId+"&_pageIndex="+currentPageIndex+"&_pageSize="+currentPageSize+"&deptId="+deptId;
        var url = "${request.contextPath}/familydear/cadresRelation/list"+str;

        $("#showList").load(url);
    }
</script>