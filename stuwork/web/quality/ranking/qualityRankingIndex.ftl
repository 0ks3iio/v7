<div class="box box-default">
    <div class="box-body">
        <div  class="tab-pane active">
            <div class="filter">
                <div class="filter-item">
                    <span class="filter-name">年级：</span>
                    <div class="filter-content">
                        <select class="form-control" id="gradeId" onChange="showClass()">
                            <option value="">---请选择---</option>
                            <#if gradeList?exists && gradeList?size gt 0>
                                <#list gradeList as item>
                                    <option value="${item.id!}" <#if item.id?default("")==gradeId?default("")>selected="selected"</#if>>${item.gradeName!}</option>
                                </#list>
                            </#if>
                        </select>
                    </div>
                </div>
                <div class="filter-item">
                    <span class="filter-name">班级：</span>
                    <div class="filter-content">
                        <select class="form-control" id="classId" onChange="showList()">

                        </select>
                    </div>
                </div>
                <div class="filter-item">
                    <a a href="javascript:void(0);" onclick="doExport()" class="btn btn-blue">导出Excel</a>
                </div>
                <div class="filter-item filter-item-right">
                    <span id="statisticsTime"><#if qualityScore?exists>上次统计时间：${qualityScore.statisticalTime?string('yyyy-MM-dd HH:mm')}<#else>请统计总分</#if>
                    <a a href="javascript:void(0);" onclick="statisticsScore()" class="btn btn-blue">统计</a>
                </div>
            </div>
        </div>
        <input type="hidden" id="unitId" value="${unitId!}">
        <input type="hidden" id="userId" value="${userId!}">
        <div id="showListDiv">

        </div>
    </div>
</div>
<script type="text/javascript">
    $(function(){
        showClass();
    })
    function showClass(){
        var gradeId=$("#gradeId").val();
        var classIdClass=$("#classId");
        if(gradeId==""){
            var htmlStr = '<option value="">---请选择---</option>';
            classIdClass.html(htmlStr);
            classIdClass.val('');
            showList();
        }else{
            var unitId=$("#unitId").val();
            var userId=$("#userId").val();
            $.ajax({
                url:"${request.contextPath}/quality/common/getClassList",
                data:{gradeId: gradeId,userId: userId,unitId: unitId},
                dataType: "json",
                success: function(json){
                    var array=json.array;
                    classIdClass.html("");
                    var classComHtml="";
                    if(array && array.length>0){
                        classComHtml+='<option value="">全部</option>';
                        for(var i=0;i<array.length;i++){
                            classComHtml+='<option value="'+array[i].id+'">';
                            classComHtml+=array[i].className;
                            classComHtml+='</option>';
                        }
                        classIdClass.append(classComHtml);
                    }
                    showList();
                }
            });
        }
    }
    function showList(){
        var gradeId=$("#gradeId").val();
        var classId=$("#classId").val();
        if(!classId){
            classId='';
        }
        var str="?gradeId="+gradeId+"&classId="+classId;
        var url="${request.contextPath}/quality/ranking/list/page"+str;
        $("#showListDiv").load(url);
    }

    function doExport(){
        var gradeId=$("#gradeId").val();
        var classId=$("#classId").val();
        if(!classId){
            classId='';
        }
        var str="?gradeId="+gradeId+"&classId="+classId;
        var url="${request.contextPath}/quality/ranking/list/export"+str;
        document.location.href = url;
    }

    var isSubmit = false;
    function  statisticsScore(){
        isSubmit = true;
        var ii = layer.load();
        $.ajax({
            url:"${request.contextPath}/quality/ranking/statistics",
            data:{},
            dataType:'json',
            contentType:'application/json',
            type:'POST',
            success:function (data) {
                layer.close(ii);
                if(data.success){
                    showList();
                    $("#statisticsTime").html("上次统计时间："+new Date().format('yyyy-MM-dd hh:mm'));
                    layerTipMsg(data.success,data.msg,"");
                }else{
                    layerTipMsg(data.success,data.msg,"");
                }
                isSubmit = false;
            }
        });
    }
</script>