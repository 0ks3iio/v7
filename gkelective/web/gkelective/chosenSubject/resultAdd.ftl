<div class="filter">
    <div class="filter-item block">
        <span class="filter-name">学号：</span>
        <div class="filter-content">
            <input type="text" class="form-control" name="stucode" id="stucode" value ="请输入学号Enter查找" onkeydown="displayResult()"  onblur="findStudent()" onclick="if(this.value == '请输入学号Enter查找')this.value='';"  maxlength="20">
        </div>
    </div>
    <div class="filter-item block">
        <span class="filter-name">姓名：</span>
        <div class="filter-content">
            <input type="text" class="form-control" name="stuName" id="stuName" value="" readonly>
        </div>
    </div>
    <div class="filter-item block">
        <span class="filter-name">行政班：</span>
        <div class="filter-content">
            <input type="text" class="form-control" name="className" id="className" value="" readonly>
        </div>
    </div>
    <div class="filter-item block">
        <span class="filter-name">选择科目：</span>
        <div class="filter-content">
            <ul class="label-list clearfix">
            <#if coursesList?? && (coursesList?size>0)>
               <#list coursesList as course>                     
               <li>
                    <label>
                        <input type="checkbox" name="subjectId" id="subjectId" class="wp" value="${course.id! }">
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
    <p class="no-margin text-center" style="height: 25px"><em id="t"></em></p>
</div>

    <#-- 确定和取消按钮 -->
<div class="layer-footer">
    <a href="javascript:" class="btn btn-lightblue" id="result-commit">确定</a>
    <a href="javascript:" class="btn btn-lightblue" id="result-close">取消</a>
</div>

<script type="text/javascript">
    function displayResult()
    {	var x;
        if(window.event) // IE8 以及更早版本
        {	x=event.keyCode;
        }else if(event.which) // IE9/Firefox/Chrome/Opera/Safari
        {
            x=event.which;
        }
        if(13==x){
            findStudent();
        }
    }

		
function findStudent(){
  var stuCode = $("#stucode").val();
  if(stuCode=="请输入学号Enter查找" || stuCode==""){
    return;
  }
  $("#t").text("");
  $("#stuName").attr("value","");
  $("#className").attr("value","");
  var objs = document.getElementsByName('subjectId');
  for(var j=0;j<objs.length;j++){
      objs[j].checked =false;
    }
  
  $.ajax({
    url:'${request.contextPath}/gkelective/${arrangeId}/chosenSubject/findDetail?stuCode='+stuCode,
    type:'post',  
    cache:false,  
    contentType: "application/json",
    success:function(data) {
        var jsonO = JSON.parse(data);
        if(jsonO.success==false){
            $("#t").text(jsonO.msg);
            $("#result-commit").removeClass("disabled");
            isSubmit=false;
        }else{
            $("#stuName").attr("value",jsonO.stuName);//填充内容
            $("#className").attr("value",jsonO.className);//填充内容
            if(jsonO.subjectId2Name.size!=0){
              var objs = document.getElementsByName('subjectId');
              for(var key in jsonO.subjectId2Name) {  
                 for(var j=0;j<objs.length;j++){
                     if(objs[j].value==key){
                       objs[j].checked =true;
                       break;
                     }
                 }
             }
            }
        }
     }
});
}

// 取消按钮操作功能
$("#result-close").on("click", function(){
    <#--doLayerOk("#result-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     -->
    layer.closeAll();
 });
// 确定按钮操作功能
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
    var stuCode = obj['stucode'];
    if(stuCode==""||stuCode==null||stuCode=='请输入学号Enter查找'){
      $("#t").text("学号不能为空");
      $(this).removeClass("disabled");
      isSubmit=false;
      return;
    }
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
        url:'${request.contextPath}/gkelective/${arrangeId}/chosenSubject/save?stuCode='+stuCode+"&subjectId="+subjectId,
        cache:false,  
        contentType: "application/json",
        success:function(data) {
            var jsonO = JSON.parse(data);
            if(jsonO.success){
                layer.closeAll();
                layerTipMsg(jsonO.success,"成功",jsonO.msg);
                var aid=$(".nav-tabs-1").find(".active").find("a").attr("id");
                if(aid && aid=="cc"){
                	itemShowList(3);
                }else if(aid && aid=="bb"){
                	itemShowList(2);
                }else{
                	itemShowList(1);
                }
            }
            else{
                /* layerTipMsg(jsonO.success,"失败",jsonO.msg); */
                $("#t").text(jsonO.msg);
                $("#result-commit").removeClass("disabled");
                isSubmit=false;
            }
         }
    });
 });    

</script>
