<div class="filter">
        <div class="filter-item block">
            <span class="filter-name">科目：</span>
            <div class="filter-content">
                <ul class="label-list clearfix">
                <#if coursesList?? && (coursesList?size>0)>
                   <#list coursesList as course>                     
                   <li>
                        <label>
                            <input type="checkbox" name="subjectId" id="subjectId" class="wp" value="${course.id!}">
                            <span class="lbl">
                            ${course.subjectName!}
                            </span>
                        </label>
                    </li>
                    </#list>
                </#if> 
                </ul>
            </div>
        </div>
        <p class="no-margin text-center" style="height: 21px"><em id="t"></em></p>
    </div>
    
        <#-- 确定和取消按钮 -->
<div class="layer-footer">
    <a href="javascript:" class="btn btn-lightblue" id="result-commit">确定</a>
    <a href="javascript:" class="btn btn-grey" id="result-close">取消</a>
</div>

<script type="text/javascript">

// 取消按钮操作功能
$("#result-close").on("click", function(){
    layer.closeAll();    
});
//确定按钮操作功能
var isSubmit=false;
$("#result-commit").on("click", function(){
    if(isSubmit){
        return;
    }
    $("#t").text("");
    isSubmit=true;
    $(this).addClass("disabled");
    var obj = new Object();
    // 获取此控件下所有的可输入内容，并组织成json格式
    // obj，是因为url所对应的接收对象是个dto，数据是存在dto
    obj= JSON.parse(dealValue('.filter'));
    var subjectId = obj['subjectId'];
    var strs= new Array();
    strs=subjectId.split(",");
    if(strs.length!=3){
      $("#t").text("请选择3门课程");
      $(this).removeClass("disabled");
      isSubmit=false;
      return;
    }
    $.ajax({
        url:'${request.contextPath}/gkelective/${arrangeId}/selectSet/save?subjectId='+subjectId,
        cache:false,  
        contentType: "application/json",
        success:function(data) {
            var jsonO = JSON.parse(data);
            if(jsonO.success){
                layer.closeAll();
                layerTipMsg(jsonO.success,"成功",jsonO.msg);
                url =  '${request.contextPath}/gkelective/${arrangeId}/selectSet/List/page';
				$("#showList").load(url);
            }else{
                $("#t").text(jsonO.msg);
                $("#result-commit").removeClass("disabled");
                isSubmit=false;
            }
         }
    });
 });    
   

</script>
