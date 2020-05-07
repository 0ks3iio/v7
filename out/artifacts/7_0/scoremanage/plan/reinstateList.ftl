<div class="box box-default">
    <div class="box-body">
        <!-- PAGE CONTENT BEGINS -->
        <div class="filter clearfix">
            <div class="filter-item pull-right">
                <a class="btn btn-blue js-addTerm" onclick="goBack();">返回</a>
                <a class="btn btn-blue js-addTerm" onclick="addReinstate();">新增插入</a>
            </div>
        </div><!-- 筛选结束 -->
        <div class="table-wrapper" id="showItemDiv">
            <table class="table table-bordered table-striped table-hover">
                <thead>
                <tr>
                    <th>学生姓名</th>
                    <th>身份证号</th>
                    <th>休学前考试</th>
                    <th>当前所在年级的考试</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <#if dtoList?exists && dtoList?size gt 0>
                    <#list dtoList as dto>
                        <tr>
                            <td>${studentName!}</td>
                            <td>${identityCard!}</td>
                            <td>${dto.oldExamName!}</td>
                            <td>${dto.examName!}</td>
                            <td>
                                <a href="javascript:deleteReinstate('${dto.reinstateId!}');" class="table-btn color-red">删除</a>
                            </td>
                        </tr>
                    </#list>
                <#else>
                    <tr>
                        <td colspan="5" class="text-center">暂无相关数据！</td>
                    </tr>
                </#if>
                </tbody>
            </table>
        </div>
    </div>
</div>
<script>
    function goBack(){
        var str = "?acadyear=${acadyear!}&semester=${semester!}";
        var url = '${request.contextPath}/scoremanage/plan/reStudent/list/page'+str;
        $("#showList").load(url);
    }

    function addReinstate(){
        var str = "?acadyear=${acadyear!}&semester=${semester!}&studentId=${studentId!}&studentName=${studentName!}&identityCard=${identityCard!}&oldGradeId=${oldGradeId!}&gradeId=${gradeId!}";
        var url = "${request.contextPath}/scoremanage/plan/reinstate/add/page"+str;
        indexDiv = layerDivUrl(url,{title: "新增插入",width:500,height:320});
    }

    var isSubmit=false;
    function deleteReinstate(reinstateId){
        if(isSubmit){
            return;
        }
        isSubmit=true;
        layer.confirm('确定删除吗？', function(index){
            var ii = layer.load();
            $.ajax({
                url:'${request.contextPath}/scoremanage/plan/reinstate/delete',
                data: {'reinstateId':reinstateId},
                type:'post',
                success:function(data) {
                    var jsonO = JSON.parse(data);
                    if(jsonO.success){
                        layer.closeAll();
                        layer.msg(jsonO.msg,{
                            offset: 't',
                            time: 2000
                        });
                        var str = "?acadyear=${acadyear!}&semester=${semester!}&studentId=${studentId!}&studentName=${studentName!}&identityCard=${identityCard!}&oldGradeId=${oldGradeId!}&gradeId=${gradeId!}";
                        var url = "${request.contextPath}/scoremanage/plan/reinstate/list/page"+str;
                        $("#showList").load(url);
                    }
                    else{
                        layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
                        isSubmit=false;
                    }
                    layer.close(ii);
                },
                error:function(XMLHttpRequest, textStatus, errorThrown){}
            });

        },function(){
            isSubmit=false;
        })
    }

</script>
