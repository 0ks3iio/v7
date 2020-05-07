<div style="margin-top: 10px">
    <spam>学生人数：</span>
    <span>总人数：</span>${total!}
    <span>男：</span>${maleTotal!}
    <span>女：</span>${femaleTotal!}
</div>
<#assign border = (subjectsList?size-1) / 2 +1>
<div data-id="view-table" style="margin-top: 20px;margin-bottom: 20px">
    <div class="row">
        <div class="col-sm-6">
            <div>
                <table class="table table-striped table-hover no-margin">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>选课组合</th>
                        <th>总人数</th>
                        <th>男</th>
                        <th>女</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list 0..<border as index>
                    <tr>
                        <td>${index+1}</td>
                        <td>${subjectsList[index].subShortNames}</td>
                        <td>${subjectsList[index].sumNum}</td>
                        <td>${subjectsList[index].scoreMap.male}</td>
                        <td>${subjectsList[index].scoreMap.female}</td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="col-sm-6">
            <div>
                <table class="table table-striped table-hover no-margin">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>选课组合</th>
                        <th>总人数</th>
                        <th>男</th>
                        <th>女</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list border..<subjectsList?size as index>
                    <tr>
                        <td>${index+1}</td>
                        <td>${subjectsList[index].subShortNames}</td>
                        <td>${subjectsList[index].sumNum}</td>
                        <td>${subjectsList[index].scoreMap.male}</td>
                        <td>${subjectsList[index].scoreMap.female}</td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<#if notReport?default(true)>
    <a href="javascript:void(0)" class="btn btn-blue"  data-value="" data-stat="1" onclick="reportUpload('${choiceId!}')" >上报</a>
<#else>
    <a href="javascript:void(0)" class="btn btn-blue"  data-value="" data-stat="1" onclick="reReport()" >重新上报</a>
</#if>

<script>
    <#if notReport?default(true)>
    function reportUpload(choiceId){
        layer.confirm("确定要上报该次选课吗？", {
            title: ['提示','font-size:20px;'],
            btn: ['确认','取消'] //按钮
        }, function(){
            doReportUpload(choiceId);
        }, function(){
            layer.closeAll();
        });
    }

    function doReportUpload(choiceId) {
        var ii = layer.load();
        $.ajax({
            url:'${request.contextPath}/newgkelective/report/choice/upload',
            data:{
                "gradeId":"${gradeId!}",
                "choiceId":choiceId
            },
            type:'post',
            success:function(data) {
                var jsonO = JSON.parse(data);
                if(jsonO.success){
                    layer.closeAll();
                    layer.msg(jsonO.msg, {offset: 't',time: 2000});
                    $("#aa").load("${request.contextPath}/newgkelective/report/choice/index/page?gradeId=${gradeId!}&withMaster=1");
                }
                else{
                    layer.closeAll();
                    layerTipMsg(jsonO.success,"失败",jsonO.msg);
                }
                layer.close(ii);
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    }
    <#else>
    function reReport() {
        $("#aa").load("${request.contextPath}/newgkelective/report/choice/index/page?gradeId=${gradeId!}&reReport=1");
    }
    </#if>
</script>