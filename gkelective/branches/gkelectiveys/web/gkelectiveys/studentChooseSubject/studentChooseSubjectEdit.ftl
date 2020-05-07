    <table class="table table-striped">
<thead>
    <tr>
        <th colspan="2">您的选课信息为：</th>
    </tr>
</thead>
<tbody>
    <input id="arrangeId" type="hidden" value="${arrangeId}"/>
    <#list gkSubjectArrangeExList as gkSubjectArrangeEx>
    <tr>
    <input name="subjectId" type="hidden" value="${gkSubjectArrangeEx.subjectId}" />
        <td>${gkSubjectArrangeEx.subjectName}</td>
        <td><#if gkSubjectArrangeEx.teachModel == 1>走班</#if>
            <#if gkSubjectArrangeEx.teachModel == 0>不走班</#if>
        </td>
    </tr>
    </#list>
</tbody>
    </table>
<#-- 确定和取消按钮 -->
<div class="layer-footer">
    <button class="btn btn-lightblue" id="commit">确定</button>
    <button class="btn btn-grey" id="close">取消</button>
</div>
<script type="text/javascript">

// 取消按钮操作功能
$("#close").on("click", function(){
    doLayerOk("#commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
// 确定按钮操作功能
var isSubmit=false;
$("#commit").on("click", function(){
    if(isSubmit){
        return;
    }
    isSubmit=true;
    $(this).addClass("disabled");
    var subjectIds = new Array(); ;
    jQuery("input[name='subjectId']").each(function(){
    	subjectIds.push(jQuery(this).val());
    });
    var arrangeId = $("#arrangeId").val();
    var ii = layer.load();
    $.post("${request.contextPath}/gkelective/studentChooseSubject/save?subjectId="+subjectIds+"&arrangeId="+arrangeId,function(data){
        var jsonO = JSON.parse(data);
        if(jsonO.success){
            layer.closeAll();
            layerTipMsg(jsonO.success,"成功",jsonO.msg);
            itemShowList();
        }
        else{
            layerTipMsg(jsonO.success,"失败",jsonO.msg);
            $("#commit").removeClass("disabled");
            isSubmit=false;
        }
        layer.close(ii);
    })
 });    
</script>
    
