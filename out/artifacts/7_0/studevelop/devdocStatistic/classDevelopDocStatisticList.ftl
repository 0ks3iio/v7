<title></title>
<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-wrapper">
    <table class="table table-bordered table-striped table-hover no-margin">
        <thead>
        <tr>
            <th style="width:10%"  >序号</th>
            <th style="width:15%"  >姓名</th>
            <th style="width:15%"  >幸福一家</th>
            <th style="width:15%"  >校外表现</th>
            <th style="width:15%"  >孩子荣誉</th>
            <th style="width:15%"  >假期生活</th>
            <th style="width:15%"  >上传图片数</th>

        </tr>

        </thead>
        <tbody>
        <#if claStatisticDtos?exists && (claStatisticDtos?size > 0)>
            <#list claStatisticDtos as dto>
            <tr>
                <td>${dto_index+1}</td>
                <td>${dto.studentName!}</td>
                <td>${dto.happyHome?default('未完成')}</td>
                <td>${dto.outSideSchool?default('0') + '张'}</td>
                <td>${dto.honor?default('0')+ '张'}</td>
                <td>${dto.holiday?default('0')+ '张'}</td>
                <td>${dto.parentSubmitPic?default('0')+ '张'}</td>
            </tr>
            </#list>
        </#if>
        </tbody>
    </table>
</div>
<script>
    function doDelete(id){
        var stuId=$("#studentId").val();
        showConfirmMsg('确认删除任职情况信息？','提示',function(){
            $.ajax({
                url:'${request.contextPath}/studevelop/dutySituation/del',
                data: {id:id},
                type:'post',
                success:function(data) {
                    var jsonO = JSON.parse(data);
                    if(jsonO.success){
                        layer.closeAll();
                        layerTipMsg(jsonO.success,"删除成功",jsonO.msg);
                        if(stuId==""){
                            DutyList();
                        }else{
                            changeStuId();
                        }
                    }
                    else{
                        layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
                    }
                },
                error:function(XMLHttpRequest, textStatus, errorThrown){}
            });
        });
    }

    function updateExam(id){
        var stuId=$("#studentId").val();
        var acadyear=$("#acadyear").val();
        var semester=$("#semester").val();
        var ass = '?acadyear='+acadyear+'&semester='+semester+'&stuId='+stuId+'&id='+id;
        var url='${request.contextPath}/studevelop/dutySituation/edit'+ass;
        indexDiv = layerDivUrl(url,{title: "编辑",width:750,height:350});
    }
</script>
