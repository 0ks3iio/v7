<div class=" layer-addTerm layer-change" style="display:block;" id="myDiv">
    <div class="layer-body">
        <div class="filter clearfix">
            <div class="filter-item">
                <label for="" class="filter-name">学生：</label>
                <div class="filter-content">
                    <select  class="form-control" nullable="false" id="studentId" style="width: 200px">
						<#if studentList?exists && (studentList?size>0)>
		                    <#list studentList as item>
			                     <option value="${item.id!}">${item.studentName!}</option>
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
    $(function () {
        $(studentId).chosen({
            width:'200px',
            no_results_text:"未找到",//无搜索结果时显示的文本
            allow_single_deselect:true,//是否允许取消选择
            disabch_contains:true,//模糊匹配，false是默认从第一个匹配
            //mle_search:false, //是否有搜索框出现
            searax_selected_options:1 //当select为多选时，最多选择个数
        });
    })
    // 取消按钮操作功能
    $("#arrange-close").on("click", function(){
        doLayerOk("#arrange-commit", {
            redirect:function(){},
            window:function(){layer.closeAll()}
        });
    });
    $("#arrange-commit").on("click", function(){
        var studentId=$("#studentId").val();
        if(!studentId){
            layerTipMsg(false,"提示!","没有需要加入面试的学生!");
            return;
        }
        var acadyear = $("#acadyear").val();
        var semester = $("#semester").val();
        var schoolId = $("#schoolList").val();
        var examId = $("#examId").val();
        $.ajax({
            url:"${request.contextPath}/exammanage/edu/audition/auditionSetSave",
            data:{acadyear:acadyear,semester:semester,schoolId:schoolId,examId:examId,studentId:studentId},
            success:function(data) {
                layer.closeAll();
                var jsonO = JSON.parse(data);
                if(jsonO.success){
                    layer.msg(jsonO.msg, {
                        offset: 't',
                        time: 2000
                    });
                    changeExamId();
                }else{
                    layerTipMsg(jsonO.success,"失败",jsonO.msg);
                }
                layer.close(ii);
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    });
</script>