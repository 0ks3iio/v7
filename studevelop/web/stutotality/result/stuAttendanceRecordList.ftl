<#if studentDto?exists&&(studentDto?size>0)>
    <table class="table  table-striped table-hover typein-table">
        <thead>
        <th>序号</th>
        <th>姓名</th>
        <th>学号</th>
        <th>事假天数</th>
        <th>病假天数</th>
        <th>其他请假</th>
        <th>操作</th>
        </thead>
        <tbody>
        <#list studentDto as item>
            <tr>
                <td rowspan="1">${item_index+1}</td>
                <td>${item.studentName!}</td>
                <td>${item.studentCode!}</td>
                <td>${item.casualLeave!}</td>
                <td>${item.sickLeave!}</td>
                <td>${item.otherLeave!}</td>
                <td class="td-text-over">
                    <a
                            href="javascript:;"
                            class="evaluate-btn-text"
                            onclick="showeditarea(this,'${item.finalId!}','${item.id!}','${item.studentName!}','${item.studentCode!}','${item.casualLeave!}','${item.sickLeave!}','${item.otherLeave!}',10,10)"
                    >编辑</a
                    >
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
<#else >
    <div class="no-data-container">
        <div class="no-data">
            <span class="no-data-img">
                <img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
            </span>
            <div class="no-data-body">
                <p class="no-data-txt">暂无数据</p>
            </div>
        </div>
    </div>
</#if>
<div class="layer layer-area-edit">
    <div class="layer-content">
        <div class="form-horizontal layer-edit-body">
            <div class="row" style="margin:0;">
                <input type="hidden" id="finalId"/>
                <input type="hidden" id="studentId"/>
                <div class="col-xs-6 layer-attend-item">
                    <label>姓名：</label>
                    <span id="stuName"></span>
                </div>
                <div class="col-xs-6 layer-attend-item">
                    <label>学号：</label>
                    <span id="stuCode"></span>
                </div>
                <div class="col-xs-6 layer-attend-item">
                    <label>事假天数：</label>
                    <div>
                        <div class="form-shuzi form-shuzi-lg" style="width: 120px;" data-step="0.5" data-max="999">
                            <input type="text"  id="casualLeave"  class="form-control evaluate-control" style="height: 36px;"
                                   numtype="" placeholder="请输入"/>
                            <i class="wpfont icon-close-round-fill evaluate-shuzi-close" onclick="closenuminput(this)"></i>
                            <div class="btn btn-sm btn-default btn-block form-shuzi-add">
                                <i class="fa fa-angle-up"></i>
                            </div>
                            <div class="btn btn-sm btn-default btn-block form-shuzi-sub">
                                <i class="fa fa-angle-down"></i>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-xs-6 layer-attend-item">
                    <label> 病假天数 ：</label>
                    <div class="form-shuzi form-shuzi-lg" style="width: 120px;" data-step="0.5" data-max="999">
                        <input type="text"  id="sickLeave"  class="form-control evaluate-control" style="height: 36px;"
                               numtype="" placeholder="请输入"/>
                        <i class="wpfont icon-close-round-fill evaluate-shuzi-close" onclick="closenuminput(this)"></i>
                        <div class="btn btn-sm btn-default btn-block form-shuzi-add">
                            <i class="fa fa-angle-up"></i>
                        </div>
                        <div class="btn btn-sm btn-default btn-block form-shuzi-sub">
                            <i class="fa fa-angle-down"></i>
                        </div>
                    </div>
                </div>
                <div class="col-xs-6 layer-attend-item">
                    <label> 其他请假 ：</label>
                    <div class="form-shuzi form-shuzi-lg" style="width: 120px;" data-step="0.5" data-max="999">
                        <input type="text"  id="otherLeave"  class="form-control evaluate-control" style="height: 36px;"
                               numtype="" placeholder="请输入"/>
                        <i class="wpfont icon-close-round-fill evaluate-shuzi-close" onclick="closenuminput(this)"></i>
                        <div class="btn btn-sm btn-default btn-block form-shuzi-add">
                            <i class="fa fa-angle-up"></i>
                        </div>
                        <div class="btn btn-sm btn-default btn-block form-shuzi-sub">
                            <i class="fa fa-angle-down"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="layer-evaluate-right">
            <button class="btn btn-white mr10 font-14" onclick="hidelayer()">
                取消
            </button>
            <button class="btn btn-blue font-14" onclick="save()">
                确定
            </button>
        </div>
    </div>
</div>
<script type="text/javascript">
    var editareabtn = null;
    $(function() {

    });

    //显示编辑
    function showeditarea(that,finalId,studentId,studentName,studentCode,casualLeave,sickLeave,otherLeave) {
        // debugger;
        $("#onetext").val("");
        $("#finalId").val(finalId);
        $("#studentId").val(studentId);
        $("#stuName").html(studentName);
        $("#stuCode").html(studentCode);
        $("#casualLeave").val(casualLeave);
        $("#sickLeave").val(sickLeave);
        $("#otherLeave").val(otherLeave);
        layer.open({
            type: 1,
            shadow: 0.5,
            title: "编辑",
            area: "500px",
            content: $(".layer-area-edit")
        });
    }

    //包含小数点数字输入框
    $('.form-shuzi input.form-control').keyup(function(){
        var num = /^\d+(\.\d{0,2})?$/;
        var max = parseFloat($(this).parent('.form-shuzi').attr("data-max"));
        if (max === undefined) {
            max = 999;
        } else{
            max = parseFloat(max);
        }
        if($(this).attr("numtype") == "int") {
            num = /^\d*$/;
        }else{
            num = /^\d+(\.\d{0,2})?$/;
        }
        if(!num.test($(this).val())){
            $(this).val('');
            $(this).change();
        };
        if($(this).val() > max) {
            $(this).val(max);
            $(this).change();
        }
    });
    $('.form-shuzi > .btn').click(function(e){
        e.preventDefault();
        var $num = $(this).siblings('input.form-control');
        var max = parseFloat($(this).parent('.form-shuzi').attr("data-max"));
        var val = $num.val();
        if (!val ) val = 0;
        var num = parseFloat(val);
        var step = $num.parent('.form-shuzi').attr('data-step');
        if (step === undefined) {
            step = 1;
        } else{
            step = parseFloat(step);
        }
        if ($(this).hasClass('form-shuzi-add')) {
            num = parseFloat(numAdd(num,step));
        } else{
            num = parseFloat(numSub(num,step));
            if (num <= 0) num = 0;
        }
        if(num > max) {
            $num.val(max);
            $num.change();
        }else{
            $num.val(num);
            $num.change();
        }
    });

    $(".form-shuzi").find("input.form-control").bind("input propertychange", function(event) {
        if($(this).val() == ""){
            $(this).siblings(".evaluate-shuzi-close").removeClass("active");
        }else{
            $(this).siblings(".evaluate-shuzi-close").addClass("active");
        }
    });
    $(".form-shuzi").find("input.form-control").on("change", function(event) {
        if($(this).val() == ""){
            $(this).siblings(".evaluate-shuzi-close").removeClass("active");
        }else{
            $(this).siblings(".evaluate-shuzi-close").addClass("active");
        }
    });

    $(".form-shuzi").find("input.form-control").focus(function() {
        if($(this).val() == ""){
            $(this).siblings(".evaluate-shuzi-close").removeClass("active");
        }else{
            $(this).siblings(".evaluate-shuzi-close").addClass("active");
        }
    })
    var isSubmit= false;
    function save() {
        var finalId = $("#finalId").val();
        var studentId = $("#studentId").val();
        var casualLeave = $("#casualLeave").val();
        var sickLeave = $("#sickLeave").val();
        var otherLeave = $("#otherLeave").val();
        // var laterLeave = $("#laterLeave").val();
        // var earlyLeave = $("#earlyLeave").val();
        if(casualLeave){
            if(!CheckInputIntFloat(parseFloat(casualLeave))){
                layer.tips('请输入0.5的整数倍天数!', $("#casualLeave"), {
                    time:3000,
                    tipsMore: true,
                    tips: 2
                });
                return;
            }
        }else{
            casualLeave="";
        }
        if(sickLeave) {
            if (!CheckInputIntFloat(parseFloat(sickLeave))) {
                layer.tips('请输入0.5的整数倍天数!', $("#sickLeave"), {
                    time: 3000,
                    tipsMore: true,
                    tips: 2
                });
                return;
            }
        }else{
            sickLeave="";
        }
        if(otherLeave) {
            if (!CheckInputIntFloat(parseFloat(otherLeave))) {
                layer.tips('请输入0.5的整数倍天数!', $("#otherLeave"), {
                    time: 3000,
                    tipsMore: true,
                    tips: 2
                });
                return;
            }
        }else{
            otherLeave="";
        }
        if(isSubmit){
            return;
        }
        isSubmit=true;
        $.ajax({
            url:"${request.contextPath}/stutotality/result/saveAttendanceRecord",
            data:{'finalId':finalId,'studentId':studentId,'casualLeave':casualLeave,'sickLeave':sickLeave,'otherLeave':otherLeave},
            type:'post',
            dataType:'json',
            success:function(data) {
                var jsonO = data;
                if(!jsonO.success) {
                    layerTipMsg(jsonO.success, "保存失败！", jsonO.msg);
                    isSubmit = false;
                    return;
                }else {
                    layer.msg("保存成功", {
                        offset: 't',
                        time: 2000
                    });
                    var gradeId = "";
                    $("#outter1").find("a").each(function () {
                        if($(this).attr("class")=="selected"){
                            gradeId = $(this).attr("value")
                        }
                    })
                    var classId = "";
                    $("#outter2").find("a").each(function () {
                        if($(this).attr("class")=="selected"){
                            classId = $(this).attr("value")
                        }
                    })
                    if(!classId){
                        classId="";
                    }
                    isSubmit = false;
                    if(gradeId) {
                        getAttendanceList(gradeId, classId);
                    }
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
            }
        })
        hidelayer();
    }
    function checkNum(str){
        var reg = /^\d+(\.\d{1})?$/;
        return reg.test(str);
    }
    function CheckInputIntFloat(el) {
        //正的，小数部分以5结尾的浮点数  或  正整数
        var reg = /^[1-9]\d*\.[5]$|0\.[5]$|^[1-9]\d*$/;
        return el==0||reg.test(el);
    }
</script>