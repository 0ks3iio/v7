<div class=" layer-addTerm layer-change" style="display:block;" id="myDiv">
    <div class="layer-body">
        <div class="filter clearfix">
            <div class="filter-item">
                <label for="" class="filter-name">学校：</label>
                <div class="filter-content">
                    <select  class="form-control" nullable="false" id="schoolId" style="width: 200px">
						<#if unitList?exists && (unitList?size>0)>
		                    <#list unitList as item>
			                     <option value="${item.id!}">${item.unitName!}</option>
                            </#list>
                        <#else>
		                    <option value="">未设置</option>
                        </#if>
                    </select>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="layer-footer">
    <a href="javascript:" class="btn btn-blue" id="arrange-commit">确定</a>
    <a href="javascript:" class="btn btn-white" id="arrange-close">取消</a>
</div>
<script>
    // 取消按钮操作功能
    $("#arrange-close").on("click", function(){
        doLayerOk("#arrange-commit", {
            redirect:function(){},
            window:function(){layer.closeAll()}
        });
    });
    $("#arrange-commit").on("click", function(){
        var acadyear=$("#acadyear").val();
        var semester=$("#semester").val();
        var schoolId= $("#schoolId").val();
        if(!schoolId){
            layerTipMsg(false,"提示!","没有需要加入的学校!");
            return;
        }
        var examId=$("#examId").val();
        $.ajax({
            url:"${request.contextPath}/exammanage/edu/enrollment/addEnrollment",
            data:{acadyear:acadyear,semester:semester,unitId:schoolId,examId:examId},
            success:function(data) {
                layer.closeAll();
                var jsonO = JSON.parse(data);
                if(jsonO.success){
                    layer.msg(jsonO.msg, {
                        offset: 't',
                        time: 2000
                    });
                    changeTime();
                }else{
                    layerTipMsg(jsonO.success,"失败",jsonO.msg);
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    });
</script>