<div class=" layer-addTerm layer-change" style="display:block;" id="myDiv1">
    <form id="subForm">
        <input  type="hidden" nullable="false" name="id" id="id" style="width: 200px" value="${exammanageEnrollment.id!}">
        </input>
        <div class="layer-body">
            <div class="filter clearfix">
                <div class="filter-item">
                    <label for="" class="filter-name">统招计划人数：</label>
                    <div class="filter-content">
                        <input  class="form-control" nullable="false" id="unifiedNum" name="unifiedNum" style="width: 200px" value="${exammanageEnrollment.unifiedNum?default(0)}">
                        </input>
                    </div>
                </div>
            </div>
            <div class="filter clearfix">
                <div class="filter-item">
                    <label for="" class="filter-name">提招计划人数：</label>
                    <div class="filter-content">
                        <input  class="form-control" nullable="false" id="trickNum" name="trickNum" style="width: 200px" value="${exammanageEnrollment.trickNum?default(0)}">
                        </input>
                    </div>
                </div>
            </div>
            <div class="filter clearfix">
                <div class="filter-item">
                    <label for="" class="filter-name">特招计划人数：</label>
                    <div class="filter-content">
                        <input  class="form-control" nullable="false" id="specialNum" name="specialNum" style="width: 200px" value="${exammanageEnrollment.specialNum?default(0)}">
                        </input>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<div class="layer-footer">
    <a href="javascript:" class="btn btn-blue" id="arrange-commit">确定</a>
    <a href="javascript:" class="btn btn-white" id="arrange-close">取消</a>
</div>
<script>
    $("#arrange-close").on("click", function(){
        doLayerOk("#arrange-commit", {
            redirect:function(){},
            window:function(){layer.closeAll()}
        });
    });
    $("#arrange-commit").on("click", function(){
        var unifiedNum = $("#unifiedNum").val();
        if(!checkNum(unifiedNum)){
            layerTipMsg(false,"提示!","统招计划人数必须为正整数!");
            return;
        }
        if(unifiedNum>99999){
            layerTipMsg(false,"提示!","统招计划人数建议不超过99999!");
            return;
        }
        var trickNum = $("#trickNum").val();
        if(!checkNum(trickNum)){
            layerTipMsg(false,"提示!","提招计划人数必须为正整数!");
            return;
        }
        if(trickNum>99999){
            layerTipMsg(false,"提示!","提招计划人数建议不超过99999!");
            return;
        }
        var specialNum = $("#specialNum").val();
        if(!checkNum(specialNum)){
            layerTipMsg(false,"提示!","特招计划人数必须为正整数!");
            return;
        }
        if(specialNum>99999){
            layerTipMsg(false,"提示!","特招计划人数建议不超过99999!");
            return;
        }
        var options = {
            url : "${request.contextPath}/exammanage/edu/enrollment/editEnrollmentNumSave",
            dataType : 'json',
            success : function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                    $("#arrange-commit").removeClass("disabled");
                    isSubmit=false;
                    return;
                }else{
                    layer.closeAll();
                    layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
                    isSubmit=false;
                    showList();
                }
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $("#subForm").ajaxSubmit(options);
    });
    function checkNum(num){
        var r = /^\+?[1-9][0-9]*$/;　　//正整数
        var flag=r.test(num);
        return flag;
    }
</script>