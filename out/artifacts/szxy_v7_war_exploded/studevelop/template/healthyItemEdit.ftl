

<div class="layer-content">
    <#--<h4>身心项目</h4>-->
    <div class="filter-item block">
        <span class="filter-name">名称：</span>
        <div class="filter-content">
            <input type="hidden" name="schoolId" value="${healthProject.schoolId!}" >
            <input type="hidden" name="id" id="projectId" value="${healthProject.id!}" >
            <input type="hidden" name="projectType" value="${healthProject.projectType!}" >
            <input type="hidden" name="acadyear" value="${healthProject.acadyear!}" >
            <input type="hidden" name="semester" value="${healthProject.semester!}" >
            <input type="hidden" name="schSection" value="${healthProject.schSection!}" >
            <input type="hidden" name="isClosed" value="${healthProject.isClosed!}" >
            <input type="hidden" name="creationTime" value="${(healthProject.creationTime?string('yyyy-MM-dd HH:mm:ss'))?default('')}" >
            <input type="text" id="projectName" class="form-control" name="projectName" value="${healthProject.projectName!}" nullable="false"  maxlength="30">
        </div>
        <#if healthProject.projectType?default('') == '2' >
        <span class="filter-name">计量单位：</span>
        <div class="filter-content">
            <#--onblur="onChack();"-->
            <input type="text" id="projectUnit" class="form-control" name="projectUnit" value="${healthProject.projectUnit!}"   maxlength="10">
        </div>
        <#else>
            <input type="hidden" name="projectUnit" value="" >
        </#if>
    </div>

</div>
<script type="text/javascript">

    function onCheck() {
        var acadyear = $("#queryAcadyear").val();
        var semester = $("#querySemester").val();
        var schSection = $("#section").val();
        var schoolId = $("#schoolId").val();
        var projectType = $("#healthType").find("li[class = 'active']").find("a").attr("val");
        var id=$("#projectId").val();
        var projectName = $("#projectName").val();
        var flag =false;
        var options={
            url:"${request.contextPath}/stuDevelop/healthyHeart/checkItemName",
            dataType:"json",
            type:"post",
            data:{"acadyear":acadyear ,"semester":semester,"schSection":schSection ,"schoolId":schoolId,"projectType":projectType,"id":id ,"projectName":projectName},
            success:function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layer.tips("身心项目名称在该学段下已存在，请修改！", "#projectName", {
                        tipsMore: true,
                        tips:3
                    });
                    flag = false;
                }else{
                    flag = true;
                }
            },
            error:function(XMLHttpRequest ,textStatus,errorThrown){}

        };
        $.ajax(options);
        return flag;
    }
    $(document).ready(function () {

    })
</script>