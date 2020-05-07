<div class="goClass filter">
<input type="hidden" id = "subjectName" value = "${subjectName! }"/>
<input type="hidden" id = "teachModel" value = "${teachModel! }"/>
    <div class="filter-item block">
        <span class="filter-name">科目：</span>
        <div class="filter-content">
          <p id="subjectId" name="subjectId">${subjectId!}</p>
        </div>
    </div>
    <div class="filter-item block">
        <span class="filter-name">老师：</span>
        <div class="filter-content">
<ul class="label-list clearfix">
    <#list classTeacherList as classTeacher>
    <li>
        <label>
          <input type="checkbox" name="teacher-checkbox" id="teacher-checkbox" checked="true" class="wp" value="${classTeacher.teacherId}">
          <span class="lbl">${teacherMap["${classTeacher.teacherId}"]!classTeacher.teacherId}</span>
          </input>
        </label>
    </li>
    </#list>
</ul>
        </div>
    </div>
</div>
<#-- 确定和取消按钮 -->
<div class="layer-footer">
    <button class="btn btn-lightblue" id="arrange-commit">确定</button>
    <button class="btn btn-grey" id="arrange-close">取消</button>
</div>
<!-- /.row -->
<!--[if lte IE 8]>
  <script src="${request.contextPath}/static/ace/js/excanvas.js"></script>
<![endif]-->

<script type="text/javascript">

// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
// 确定按钮操作功能
var isSubmit=false;
$("#arrange-commit").on("click", function(){
    if(isSubmit){
        return;
    }
    isSubmit=true;
    $(this).addClass("disabled");
    var check = checkValue('.goClass');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
    var obj = new Object();
    // 获取此控件下所有的可输入内容，并组织成json格式
    // obj，是因为url所对应的接收对象是个dto，数据是存在dto
    var subjectId = $("#subjectId").html();
    var chk_value =[]; 
    $('input[name="teacher-checkbox"]:checked').each(function(){ 
    chk_value.push("'"+$(this).val().replace(/(\s*$)/g,"")+"'"); 
    }); 
    var subjectName = $("#subjectName").val();
    var teachModel = $("#teachModel").val();
    var ii = layer.load();
    $.ajax({
        url:'${request.contextPath}/gkelective/${arrangeId}/goClass/gkSubjectArrangeEx/save',
        data: "{'subjectId':'"+subjectId+"','teacherIds':["+chk_value+"],'subjectName':'"+subjectName+"','teachModel':'"+teachModel+"'}",  
        type:'post',  
        cache:false,  
        contentType: "application/json",
        success:function(data) {
            var jsonO = JSON.parse(data);
            if(jsonO.success){
                layer.closeAll();
                layerTipMsg(jsonO.success,"成功",jsonO.msg);
                itemShowList();
            }
            else{
                layerTipMsg(jsonO.success,"失败",jsonO.msg);
                $("#arrange-commit").removeClass("disabled");
                isSubmit=false;
            }
            layer.close(ii);
         }
    });

 });    

</script>
            
