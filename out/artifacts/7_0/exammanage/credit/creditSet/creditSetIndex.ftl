<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div id="creditSetInfo">

</div>
<script>
    function checkNum(str){
        var reg = /^\d+(\.\d{1})?$/;
        var b =reg.test(str);
        if(str<0){
            b =false;
        }
        if(!isNaN(str)){
            b =false;
        }
        return b;
    }
    $(function(){
        search();
    })
    function save() {
        var usualScore = $("#usualScore").val();
        if(!usualScore){
            layerTipMsg(false,"提示!","平时成绩不能为空!");
            return;
        }
        var moduleScore = $("#moduleScore").val();
        if(!moduleScore){
            layerTipMsg(false,"提示!","模块成绩不能为空!");
            return;
        }
        var dailyScore = $("#dailyScore").val();
        if(!dailyScore){
            layerTipMsg(false,"提示!","日常表现分不能为空!");
            return;
        }
        var passLine = $("#passLine").val();
        if(!passLine){
            layerTipMsg(false,"提示!","及格线不能为空!");
            return;
        }
        if(passLine>Number(dailyScore)+Number(moduleScore)+Number(usualScore)){
            layerTipMsg(false,"提示!","及格线不能超过总分!");
            return;
        }
        if(!checkNum(usualScore)){
            layerTipMsg(false,"提示!","平时成绩格式不符合规则!");
            return;
        }
        if(!checkNum(moduleScore)){
            layerTipMsg(false,"提示!","模块成绩格式不符合规则!");
            return;
        }
        if(!checkNum(dailyScore)){
            layerTipMsg(false,"提示!","日常表现分格式不符合规则!");
            return;
        }
        if(!checkNum(passLine)){
            layerTipMsg(false,"提示!","及格线分格式不符合规则!");
            return;
        }
        var hasScore = true;
        var isNum = true;
        var dailyScoreSum=0;
        $("#tab").find('tr').each(function () {
            if ($(this).find('td').find("input[class='form-control creditScore']").length > 0) {
                var score = $(this).find('td').find("input[class='form-control creditScore']").val();
                if (!score) {
                    hasScore = false;
                }else {
                    debugger;
                    if(!checkNum(score)){
                        isNum = false;
                    }else {
                        dailyScoreSum = Number(dailyScoreSum)+Number(score);
                    }
                }
            }
        });
        if(!hasScore){
            layerTipMsg(false,"提示!","满分值不能有空!");
            isSubmit=false;
            return;
        }
        if(!isNum){
            layerTipMsg(false,"提示!","满分值格式不符合规则!");
            isSubmit=false;
            return;
        }
        if(dailyScoreSum>dailyScore){
            layerTipMsg(false,"提示!","日常表现评分项总分不能大于日常表现分!");
            isSubmit=false;
            return;
        }
        var url="${request.contextPath}/exammanage/credit/creditExamSet/save?"
        var options = {
            url : url,
            dataType : 'json',
            success : function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                    isSubmit=false;
                    return;
                }else{
                    layer.msg(jsonO.msg, {
                        offset: 't',
                        time: 2000
                    });
                    search();
                }
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $("#subForm").ajaxSubmit(options);
    }
    function search(){
        var url =  '${request.contextPath}/exammanage/credit/creditSetInfo?';
        $("#creditSetInfo").load(url);
    }
</script>
