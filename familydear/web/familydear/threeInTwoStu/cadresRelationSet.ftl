
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
                <div class="filter" style="">
                    <div class="filter-item">
                        <span class="filter-name">设置干部：</span>
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
            layerTipMsgWarn("提示","请选择一名干部！");
            return;
        }
        var objId = $("#objId").val();
        var options = {
            url : "${request.contextPath}/familydear/threeInTwoStu/cadreAdd",
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
        $.ajax(options);
    });

    function　searchRelationList(){
        var stuName = $("#stuName").val();
        var ganbName = $("#ganbName").val();
        var stuPhone = $("#stuPhone").val();
        var url =  '${request.contextPath}/familydear/threeInTwoStu/edu/stuManage/List?stuName='+encodeURIComponent(encodeURIComponent(stuName))+"&ganbName="+encodeURIComponent(encodeURIComponent(ganbName))+"&stuPhone="+stuPhone+"&currentPageIndex="+'${currentPageIndex!}'+"&currentPageSize="+'${currentPageSize!}';
        $("#showList1").load(url);
    }
</script>