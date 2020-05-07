<div class="filter">
        <div class="filter-item block">
            <input type="hidden" name="studentId" id="studentId" value="${student.id! }">
            <span class="filter-name">学号：</span>
            <div class="filter-content">
                <p>${student.studentCode! }</p>
            </div>
        </div>
        <div class="filter-item block">
            <span class="filter-name">姓名：</span>
            <div class="filter-content">
                <p>${student.studentName! }</p>
            </div>
        </div>
        <div class="filter-item block">
            <span class="filter-name">已选科目：</span>
            <div class="filter-content">
                <ul class="label-list clearfix">
                <#if coursesList?? && (coursesList?size>0)>
                   <#list coursesList as course>                     
                   <li>
                        <label>
                            <input type="checkbox" name="subjectId" id="subjectId" class="wp" value="${course.id! }" 
                                <#if chosenSubject?exists && chosenSubject[course.id?default("")]?exists>checked="checked"</#if>
                            >
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
    doLayerOk("#result-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
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
    var check = checkValue('.filter');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
    var obj = new Object();
    // 获取此控件下所有的可输入内容，并组织成json格式
    // obj，是因为url所对应的接收对象是个dto，数据是存在dto
    obj= JSON.parse(dealValue('.filter'));
    var studentId = obj['studentId'];
    var subjectId = obj['subjectId'];
    var strs= new Array();
    strs=subjectId.split(",");
    var num = '${subjectNum}';
    if(strs.length!=num){
      $("#t").text("请选择" + num + "门课程");
      $(this).removeClass("disabled");
      isSubmit=false;
      return;
    }
    $.ajax({
        url:'${request.contextPath}/gkelective/${arrangeId}/chosenSubject/save?studentId='+studentId+"&subjectId="+subjectId,
        cache:false,  
        contentType: "application/json",
        success:function(data) {
            var jsonO = JSON.parse(data);
            if(jsonO.success){
                layer.closeAll();
                layerTipMsg(jsonO.success,"成功",jsonO.msg);
                findByCondition();
            }
            else{
                $("#t").text(jsonO.msg);
                $("#result-commit").removeClass("disabled");
                isSubmit=false;
            }
         }
    });
 });    
   

</script>
