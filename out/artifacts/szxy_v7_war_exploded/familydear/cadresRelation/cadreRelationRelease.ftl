<#--<div class="layer-addTerm layer-change" style="display:block;" id="myDiv">-->
    <#--<form id="subForm">-->
        <#--<div class="layer-body">-->
            <#--<div class="filter clearfix">-->
                <#--<div class="filter clearfix">-->
                    <#--<input type="hidden" name="objIds" value="${objIds!}" />-->
                    <#--<div class="filter-item">-->
                        <#--<label for="" class="filter-name float-left">原因：</label>-->
                        <#--<div class="filter-content">-->
                            <#--<textarea name="remark" maxLength="500" nullable="false"  id="remark" cols="63" rows="4" style="width:250px;" class="form-control col-xs-10 col-sm-10 col-md-10 " value=""></textarea>-->
                        <#--</div>-->
                    <#--</div>-->

                <#--</div>-->
            <#--</div>-->
        <#--</div>-->
    <#--</form>-->
<#--</div>-->
<div class="layer-content">
    <div class="tab-content">
        <div class="form-horizontal" style="height:500px;overflow-y:auto;overflow-x: hidden;" >
            <form id = "subForm">

                            <div class="filter ">
                                <input type="hidden" name="objIds" value="${objIds!}" />
                                <div class="filter-item">
                                    <label for="" class="filter-name float-left">原因：</label>
                                    <div class="filter-content">
                                        <textarea name="remark" maxLength="500" nullable="false"  id="remark" cols="63" rows="4" style="width:350px;" class="form-control col-xs-10 col-sm-10 col-md-10 " value=""></textarea>
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
        var options = {
            url : "${request.contextPath}/familydear/cadresRelation/cadreRelease",
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
        var str = "?tabType="+tabType+"&objName="+objName+"&cadreName="+encodeURIComponent(encodeURIComponent(cadreName))+"&villageName="+encodeURIComponent(encodeURIComponent(villageName))+"&label="+label + "&type="+type +"&teacherId="+teacherId+"&_pageIndex="+currentPageIndex+"&_pageSize="+currentPageSize+"&deptId="+deptId;
        var url = "${request.contextPath}/familydear/cadresRelation/list"+str;

        $("#showList").load(url);
    }
</script>