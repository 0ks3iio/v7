<div class="box box-default">
    <div class="box-body">
        <!-- PAGE CONTENT BEGINS -->
        <div class="filter clearfix">
            <div class="filter-item pull-right">
                <a class="btn btn-blue js-addTerm" onclick="goBack();">返回</a>
            </div>
        </div><!-- 筛选结束 -->
        <div class="table-wrapper" id="showItemDiv">
            <table class="table table-bordered table-striped table-hover">
                <thead>
                <tr>
                    <th>学生姓名</th>
                    <th>当前所在年级</th>
                    <th>身份证号</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <#if dtoList?exists && dtoList?size gt 0>
                    <#list dtoList as dto>
                        <tr>
                            <td>${dto.studentName!}</td>
                            <td>${dto.gradeName!}</td>
                            <td>${dto.identityCard!}</td>
                            <td>
                                <a href="javascript:reinstateList('${dto.studentId!}','${dto.studentName!}','${dto.identityCard!}','${dto.oldGradeId!}','${dto.gradeId!}');" class="table-btn color-red">操作</a>
                            </td>
                        </tr>
                    </#list>
                <#else>
                    <tr>
                        <td colspan="4" class="text-center">暂无相关数据！</td>
                    </tr>
                </#if>
                </tbody>
            </table>
        </div>
    </div>
</div>
<script>
    function goBack(){
        var url = "${request.contextPath}/scoremanage/plan/head/page?acadyear=${acadyear!}&semester=${semester!}";
        $("#showList").load(url);
    }

    function reinstateList(studentId,studentName,identityCard,oldGradeId,gradeId){
        var str = "?acadyear=${acadyear!}&semester=${semester!}&studentId="+studentId+"&studentName="+studentName
            +"&identityCard="+identityCard+"&oldGradeId="+oldGradeId+"&gradeId="+gradeId;
        var url = "${request.contextPath}/scoremanage/plan/reinstate/list/page"+str;
        $("#showList").load(url);
    }

</script>
