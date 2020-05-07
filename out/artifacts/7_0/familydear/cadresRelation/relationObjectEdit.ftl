
<div class="layer-content">
    <div class="tab-content">
        <div class="form-horizontal" style="height:500px;overflow-y:auto;overflow-x: hidden;" >
            <form id = "subForm">
                <input type="hidden" name="id" value="${familyDearObject.id!}" />
                <input type="hidden" name="creationTime" value="${familyDearObject.creationTime!}" />
                <input type="hidden" name="state" value="${familyDearObject.state!}" />
                <input type="hidden" name="teacherId" value="${familyDearObject.teacherId!}" />
                <input type="hidden" name="deptId" value="${familyDearObject.deptId!}" />
                <div class="form-group" >
                    <label for="" class="control-label no-padding-right col-sm-2"><span style="color:red">*</span>结亲村：</label>
                    <div class="col-sm-3" >
                        <select name="villageValue" id="villageValue" class="form-control" notnull="false"  style="width:168px;">
                        ${mcodeSetting.getMcodeSelect("DM-XJJQC", familyDearObject.villageValue, "1")}
                        </select>
                        <#--<input type="text" name="village" nullable="false" maxLength="200" id="village" class="form-control  " value="${familyDearObject.village!}" style="width:168px;"/>-->
                    </div>
                    <#--<label for="" class="control-label no-padding-right col-sm-2"><span style="color:red">*</span>单位：</label>-->
                    <#--<div class="col-sm-3" >-->
                        <#--<input type="text" name="unitName" nullable="false" maxLength="200" id="unitName" class="form-control  " value="${familyDearObject.unitName!}" style="width:168px;"/>-->
                    <#--</div>-->
                    <label for="" class="control-label no-padding-right col-sm-2"><span style="color:red">*</span>结亲对象：</label>
                    <div class="col-sm-3" >
                        <input type="text" name="name" maxLength="100" nullable="false" id="name" class="form-control  " value="${familyDearObject.name!}" style="width:168px;"/>
                    </div>
                </div>

                <div class="form-group" >
                    <label for="" class="control-label no-padding-right col-sm-2"><span style="color:red">*</span>类别：</label>
                    <div class="col-sm-9" >
                        <#assign ind = 1 />
                        <#assign type = familyDearObject.type?default('') />
                        <#if mcodeDetails?exists && mcodeDetails?size gt 0>
                            <#list mcodeDetails as detail>
                                <#if detail_index %5 == 0>
                                    <div class="row form-group" >

                                </#if>

                                <label class="pos-rel form">
                                    <input name="typeArray" type="checkbox" class="wp col-sm-2" <#if type?contains(detail.thisId!) > checked</#if> value="${detail.thisId!}">
                                    <span class="lbl">${detail.mcodeContent!}</span>
                                </label>
                                <#if ind %5 == 0>
                                    </div>
                                </#if>
                                <#assign ind = ind +1 />
                            </#list>
                        </#if>
                    </div>
                </div>
                <div class="form-group" >

                    <label for="" class="control-label no-padding-right col-sm-2"><span style="color:red">*</span>民族：</label>
                    <div class="col-sm-3" >
                        <select name="nation"  id="nation" nullable="false" class="form-control" notnull="false" style="width:168px;">
                            ${mcodeSetting.getMcodeSelect("DM-MZ", '${familyDearObject.nation!}', "1")}
                        </select>
                    </div>
                    <label for="" class="control-label no-padding-right col-sm-2">性别：</label>
                    <div class="col-sm-3" >
                        <select name="sex"  id="sex"  class="form-control" notnull="false" style="width:168px;">
                            ${mcodeSetting.getMcodeSelect("DM-XB", '${familyDearObject.sex!}', "1")}
                        </select>
                    </div>
                </div>
                <div class="form-group" >
                    <label for="" class="control-label no-padding-right col-sm-2">身份证号：</label>
                    <div class="col-sm-3" >
                        <input type="text" name="identityCard"  maxLength="18" id="identityCard" class="form-control  "  placeholder="请输入身份证件号" onblur="checkIdentityCard();" value="${familyDearObject.identityCard!}" style="width:168px;"/>
                    </div>
                    <div class="col-sm-4 control-tips tip-false"></div>
                </div>
                <div class="form-group" >
                    <label for="" class="control-label no-padding-right col-sm-2">联系电话：</label>
                    <div class="col-sm-3" >
                        <input type="text" name="mobilePhone"  maxLength="30" id="mobilePhone" class="form-control  " onblur="checkMobilePhone();" placeholder="请输入联系电话" value="${familyDearObject.mobilePhone!}" style="width:168px;"/>
                    </div>
                    <div class="col-sm-4 control-tips tip-false"></div>
                </div>
                <#--<div class="form-group" >-->
                    <#--&lt;#&ndash;<label for="" class="control-label no-padding-right col-sm-2"><span style="color:red">*</span>政治面貌：</label>&ndash;&gt;-->
                    <#--&lt;#&ndash;<div class="col-sm-3" >&ndash;&gt;-->
                        <#--&lt;#&ndash;<select name="background"  id="background" nullable="false" class="form-control" notnull="false" style="width:168px;">&ndash;&gt;-->
                            <#--&lt;#&ndash;${mcodeSetting.getMcodeSelect("DM-ZZMM", '${familyDearObject.background!}', "1")}&ndash;&gt;-->
                        <#--&lt;#&ndash;</select>&ndash;&gt;-->
                    <#--&lt;#&ndash;</div>&ndash;&gt;-->
                    <#---->
                <#--</div>-->

                <div class="form-group" >
                    <label for="" class="control-label no-padding-right col-sm-2"><span style="color:red">*</span>家庭住址：</label>
                    <div class="col-sm-3" >
                        <input type="text" name="homeAddress" nullable="false" maxLength="300" id="homeAddress" class="form-control  " value="${familyDearObject.homeAddress!}" style="width:168px;"/>
                    </div>
                </div>
                <div class="form-group" >
                    <label for="" class="control-label no-padding-right col-sm-2">备注：</label>
                    <div class="col-sm-9" >
                        <textarea name="remark" maxLength="500" id="remark" cols="63" rows="3" style="width:494px;" class="form-control  " value="">${familyDearObject.remark!}</textarea>
                    </div>
                </div>
            </form>
        </div>
        <div class="layer-footer" style="vertical-align: middle">
            <button class="btn btn-lightblue" id="arrange-commit">确定</button>
            <button class="btn btn-grey" id="arrange-close">取消</button>
        </div>
    </div>
</div>
<script>
    function checkIdentityCard(){
        var identityCard = $("#identityCard").val();
        var identityCardReg = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
        if(identityCardReg.test(identityCard)){
            addSuccess("identityCard");
            return true;
        }else{
            addError("identityCard","身份证件号不符合身份证规则");
            return false;
        }
    }

    function checkMobilePhone(){
        var mobilePhone = $("#mobilePhone").val();
        var mobilePhoneReg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
        var phoneNumReg = /^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}$/;
        if(mobilePhoneReg.test(mobilePhone)){
            addSuccess("mobilePhone");
            return true;
        }else{
            debugger;
            if(phoneNumReg.test(mobilePhone)){
                addSuccess("mobilePhone");
                return true;
            }else {
                addError("mobilePhone","联系电话不符合规则");
                return false;
            }
        }
    }

    function addError(id,errormsg){
        if(!errormsg){
            errormsg='错误';
        }
        $("#"+id).parent().siblings(".control-tips").addClass("tip-false");
        $("#"+id).parent().siblings(".control-tips").html('<span class="has-error"><i class="fa fa-times-circle"></i>'+errormsg+'</span>');
    }

    function addSuccess(id,msg){
        if(!msg){
            msg='正确';
        }
        $("#"+id).parent().siblings(".control-tips").removeClass("tip-false");
        $("#"+id).parent().siblings(".control-tips").html('<span class="has-success"><i class="fa fa-check-circle"></i>&nbsp;'+msg+'</span>');
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
        var flag1 = true;
        var flag2 = true;
        if ($("#identityCard").val() != '') {
            flag1 = checkIdentityCard();
        }
        if($("#mobilePhone").val() != ''){
            flag2 = checkMobilePhone();
        }
        if (!flag1 || !flag2) {
            return;

        }
        var villageVal = $("#villageValue").val();
        if(!villageVal){
            layerTipMsgWarn("提示","请选择结亲村！")
            return;
        }
        var length = $(' [name="typeArray"]:checkbox:checked').length;
        if (length == 0) {
            layerTipMsgWarn("提示","至少选择一个类别！")
            return;
        }
        var check = checkValue('#subForm');
        if(!check){
            $(this).removeClass("disabled");
            isSubmit=false;
            return;
        }
        var options = {
            url : "${request.contextPath}/familydear/cadresRelation/object/save",
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

    function　searchRelationList(){
        var objName = $('#objName').val();
        var cadreName = $('#cadreName').val();
        var deptId = $("#deptId").val();
        var options=$("#villageName option:selected");
        var villageName = "";
        if(options.val()) {
            villageName = options.text();
        }
        // var villageName = $('#villageName').val();
        var label = $('#label').val();
        var type = $('#type').val();
        var teacherId = $('#teacherId').val();
        var currentPageIndex = ${currentPageIndex!};
        var currentPageSize = ${currentPageSize!};
        var tabType = $("#tabType").val();
        var str = "?tabType="+tabType+"&objName="+encodeURIComponent(encodeURIComponent(objName))+"&cadreName="+encodeURIComponent(encodeURIComponent(cadreName))+"&villageName="+villageName+"&label="+label + "&type="+type +"&teacherId="+teacherId+"&_pageIndex="+currentPageIndex+"&_pageSize="+currentPageSize+"&deptId="+deptId;
        var url = "${request.contextPath}/familydear/cadresRelation/list"+str;

        $("#showList").load(url);
    }
</script>