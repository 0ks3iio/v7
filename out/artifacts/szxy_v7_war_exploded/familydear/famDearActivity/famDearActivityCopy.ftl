<div class="layer layer-addTerm layer-change" style="display:block;" id="myDiv">
    <form id="subForm">
        <input type="hidden" name="id" id="famDearPlanId" <#if famDearPlan??>value="${famDearPlan.id!}</#if> ">
        <div class="layer-body" style="height:450px;overflow:auto;">
            <div class="filter clearfix">
                <div class="filter clearfix">
                    <div class="filter-item" style="width:350px;">
                        <div class="filter ">
                            <div class="filter-item">
                                <span class="filter-name">年度：</span>
                                <div class="filter-content">
                                    <select class="form-control" id="searchYear2" onChange="yearChange1()" style="width: 200px">
                                        <#if acadyearList?exists && (acadyearList?size>0)>
                                            <#list acadyearList as item>
                                                <#if (nowYear?eval <=item?eval)><option value="${item!}" <#if nowYear?default('a')==item?default('b')>selected</#if>>${item!}</option></#if>
                                            </#list>
                                        <#else>
                                            <option value="">未设置</option>
                                        </#if>
                                    </select>
                                </div>
                            </div>
                            <div class="filter-item">
                                <span class="filter-name">计划：</span>
                                <div class="filter-content">
                                    <select onChange="planChange1()" id="planList2" class="form-control" style="width: 200px">
                                        <#--<#if planList?exists && (planList?size>0)>&ndash;&gt;-->
                                        <#--<#list planList as item>-->
                                        <#--<option style="overflow: hidden;text-overflow: ellipsis;white-space: nowrap;width: 200px;" value="${item.id!}"  >${item.title!}</option>-->
                                        <#--</#list>-->
                                        <#--<#else>-->
                                        <#--<option value="">未设置</option>-->
                                        <#--</#if>-->
                                    </select>
                                </div>
                            </div>
                            <div class="filter-item">
                                <label for="" class="filter-name float-left">访亲轮次：</label>
                                <select type="text" class="form-control" id="activityId" onChange="activityChange()" style="width: 200px">
                                    <#if activityList?exists && (activityList?size>0)>
                                        <#list activityList as item>
                                            <option value="${item.id!}">${item.title!}</option>
                                        </#list>
                                    <#else>
                                        <option value="">未设置</option>
                                    </#if>
                                </select>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<div class="layer-footer">
    <a class="btn btn-lightblue" id="arrange-commit">确定</a>
    <a class="btn btn-grey" id="arrange-close">取消</a>
</div>
<script>

    // 取消按钮操作功能
    $("#arrange-close").on("click", function(){
        doLayerOk("#arrange-commit", {
            redirect:function(){},
            window:function(){layer.closeAll()}
        });
    });

    $(function(){
        yearChange1();
    })


    var isSubmit=false;
    $("#arrange-commit").on("click", function(){
        var activityId = $("#activityId").val();
        var year = "${year!}";
        var planId = "${planId!}";
        if(!activityId){
            layerTipMsg(false,"提示!","没有可供复制的批次!");
            isSubmit=false;
            return;
        }
        var options = {
            url : "${request.contextPath}/familydear/activity/saveActivityCopy",
            data:{'activityId':activityId,'year':year,'planId':planId},
            dataType : 'json',
            success : function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                    $("#arrange-commit").removeClass("disabled");
                    return;
                }else{
                    layer.closeAll();
                    layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
                    showActivityList();
                }
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $("#subForm").ajaxSubmit(options);
    });

    function yearChange1(){
        var year = $("#searchYear2").val();
        var url = "${request.contextPath}/familydear/activity/edu/yearChange";
        $.ajax({
            url:url,
            data:{'year':year},
            dataType : 'json',
            type : 'post',
            success : function(data){
                var htmlStr="";
                $("#planList2").empty();
                if(data.length>0){
                    for(var i=0;i<data.length;i++){
                        htmlStr = htmlStr+"<option value='"+data[i].id+"'>"+data[i].name+"</option>";
                    }
                }else {
                    htmlStr = htmlStr+"<option value=''>未设置</option>";
                }
                $("#planList2").html(htmlStr);
                planChange1();
                // showList1();
            }

        })
    }

    function planChange1(){
        var year = $("#searchYear2").val();
        var planId = $("#planList2").val();
        var url = "${request.contextPath}/familydear/activity/edu/planChange";
        $.ajax({
            url:url,
            data:{'year':year,'planId':planId},
            dataType : 'json',
            type : 'post',
            success : function(data){
                var htmlStr="";
                $("#activityId").empty();
                if(data.length>0){
                    for(var i=0;i<data.length;i++){
                        htmlStr = htmlStr+"<option value='"+data[i].id+"'>"+data[i].name+"</option>";
                    }
                }else {
                    htmlStr = htmlStr+"<option value=''>未设置</option>";
                }
                $("#activityId").html(htmlStr);
                // showList1();
            }

        })
    }

    function showActivityList(){
        var year = $("#searchYear1").val();
        var plan = $("#planList").val();
        var village = $("#village").val();
        var startTime =$("#startTime").val();
        var endTime =$("#endTime").val();
        if(startTime&&endTime) {
            if (startTime > endTime) {
                layerTipMsg(false, "提示!", "报名开始时间不能大于报名结束时间!");
                return;
            }
        }
        <#--var currentPageIndex = ${currentPageIndex!};-->
        <#--var currentPageSize = ${currentPageSize!};-->
        var url =  '${request.contextPath}/familydear/activity/edufamDearActivityList?year='+year+"&planId="+plan+"&village="+village+"&endTime="+endTime+"&startTime="+startTime;
        $("#showList1").load(url);
    }
</script>