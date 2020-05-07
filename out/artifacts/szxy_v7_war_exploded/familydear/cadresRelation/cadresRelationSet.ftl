
<#import "/fw/macro/popupMacro.ftl" as popupMacro>
<div class="layer-addTerm layer-change" style="display:block;" id="myDiv">
    <form id="subForm">
        <div class="layer-body">
            <div class="filter clearfix">
                <div class="filter clearfix">



                </div>
            </div>
        </div>
    </form>
</div>
<div class="layer-content">
    <div class="tab-content">
        <div class="form-horizontal" style="height:500px;overflow-y:auto;overflow-x: hidden;" >
            <form id = "subForm">
                <input type="hidden" name="objId" id="objId" value="${objId!}" />
                <#--<div class="form-group">-->
                    <#--<label for="" class="control-label no-padding-right col-sm-3">结亲干部：</label>-->
                    <#--<div class="col-sm-8">-->
                        <#--<@popupMacro.selectOneTeacher clickId="teacherName" id="teacherId" name="teacherName">-->
                            <#--<div class="input-group">-->
                                <#--<input type="hidden" id="teacherId" name="teacherId" value="" nullable="false" >-->
                                <#--<input type="text" id="teacherName" nullable="false"  class="form-control teachTeacherId" style="width:100%;" value="">-->
                            <#--</div>-->
                        <#--</@popupMacro.selectOneTeacher>-->
                    <#--</div>-->
                <#--</div>-->
                <div class="filter" style="">
                    <div class="filter-item">
                        <span class="filter-name">结亲干部：</span>
                        <div class="filter-content">
                            <@popupMacro.selectOneTeacher clickId="teacherName" id="teaId" name="teacherName">
                                <div class="input-group">
                                    <input type="hidden" id="teaId" name="teaId" value=""  >
                                    <input type="text" id="teacherName"  class="form-control teachTeacherId" style="width:100%;" value="">
                                </div>
                            </@popupMacro.selectOneTeacher>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="layer-footer">
    <a class="btn btn-lightblue" id="arrange-commit">确定</a>
    <a class="btn btn-grey" id="arrange-close">取消</a>
</div>
<script>

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
        var teacherId = $("#teaId").val();
        if (teacherId == '') {
            layerTipMsgWarn("提示","请选择一名结亲干部！");
            return;
        }
        var objId = $("#objId").val();
        var options = {
            url : "${request.contextPath}/familydear/cadresRelation/cadreAdd",
            dataType : 'json',
            data:{"teacherId":teacherId,"objId":objId},
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
        // $.ajaxSubmit(options);
        $.ajax(options);
    });

    function　searchRelationList(){
        var objName = $('#objName').val();
        var cadreName = $('#cadreName').val();
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
        var deptId = $("#deptId").val();
        var str = "?tabType="+tabType+"&objName="+encodeURIComponent(encodeURIComponent(objName))+"&cadreName="+encodeURIComponent(encodeURIComponent(cadreName))+"&villageName="+villageName+"&label="+label + "&type="+type +"&teacherId="+teacherId+"&_pageIndex="+currentPageIndex+"&_pageSize="+currentPageSize+"&deptId="+deptId;
        var url = "${request.contextPath}/familydear/cadresRelation/list"+str;

        $("#showList").load(url);
    }
</script>