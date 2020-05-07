<div class="layer layer-addTerm layer-change" style="display:block;" id="myDiv">
    <form id="subForm">
        <input type="hidden" name="id" id="famDearPlanId" <#if famDearPlan??>value="${famDearPlan.id!}</#if> ">
        <div class="layer-body" style="height:450px;overflow:auto;">
            <div class="filter clearfix">
                <div class="filter clearfix">
                    <div class="filter-item" style="width:270px;">
                        <label for="" class="filter-name" ><span style="color:red">*</span>年度：</label>
                        <select class="form-control" id="acadyear" name="year">
                            <#if acadyearList?exists && (acadyearList?size>0)>
                                <#list acadyearList as item>
                                    <#if (nowYear?eval <=item?eval)><option value="${item!}" <#if year?default('a')==item?default('b')>selected</#if>>${item!}</option></#if>
                                </#list>
                            <#else>
                                <option value="">未设置</option>
                            </#if>
                        </select>
                    </div>
                    <div class="filter-item">
                        <label for="" class="filter-name">创建人：</label>
                        <label class="filter-content">
                            ${applyUserName!}
                        </label>
                    </div>
                    <div class="filter-item">
                        <label for="" class="filter-name"><span style="color:red">*</span>标题：</label>
                        <div class="filter-content" style="width:470px;">
                            <input type="text" class="form-control" id="title1" name="title" maxlength="100" style="width:100%;"/>
                        </div>
                    </div>
                    <div class="filter-item">
                        <label for="" class="filter-name">指导思想：</label>
                        <div class="filter-content" style="width:470px;">
                            <textarea class="form-control" maxlength="500" id="guideThought" name="guideThought"   style="width:100%;"></textarea>
                        </div>
                    </div>
                    <div class="filter-item">
                        <label for="" class="filter-name">工作目标：</label>
                        <div class="filter-content" style="width:470px;">
                            <textarea type="text" class="form-control" maxlength="500" id="workObjective" name="workObjective"   style="width:100%;"  ></textarea>
                        </div>
                    </div>
                    <div class="filter-item">
                        <label for="" class="filter-name">主要内容：</label>
                        <div class="filter-content" style="width:470px;">
                            <textarea type="text" class="form-control" maxlength="500" id="mainTask" name="mainTask"    style="width:100%;" ></textarea>
                        </div>
                    </div>
                    <div class="filter-item">
                        <label for="" class="filter-name">年度活动安排：</label>
                        <div class="filter-content" style="width:470px;">
                            <textarea type="text" class="form-control"  maxlength="1000" id="activityArrange" name="activityArrange"   style="width:100%;" ></textarea>
                        </div>
                    </div>
                <#--
                <div class="filter-item">
                    <label for="" class="filter-name">非正常成绩是否统分：</label>
                    <div class="filter-content">
                        <label>
                            <input name="isTotalScore" type="radio" <#if '${examInfo.isTotalScore!}'=='0' || '${examInfo.isTotalScore!}'==''>checked</#if> class="ace" value="0"/>
                            <span class="lbl"> 否</span>
                        </label>&nbsp;&nbsp;
                        <label>
                            <input name="isTotalScore" type="radio" <#if '${examInfo.isTotalScore!}'=='1'>checked</#if> class="ace" value="1"/>
                            <span class="lbl"> 是</span>
                        </label>
                    </div>
                </div>
                -->
                </div>
            </div>
        </div>
    </form>
</div>
<div class="layer-footer">
    <button class="btn btn-lightblue" id="arrange-commit">确定</button>
    <button class="btn btn-grey" id="arrange-close">取消</button>
</div>
<script>
    $("#arrange-close").on("click", function(){
        doLayerOk("#arrange-commit", {
            redirect:function(){},
            window:function(){layer.closeAll()}
        });
    });

    var isSubmit=false;

    $("#arrange-commit").on("click",function () {
        isSubmit=true;
        var acadyear = $("#acadyear").val();
        if(!acadyear){
            layerTipMsg(false,"提示!","年份不能为空!");
            isSubmit=false;
            return;
        }
        var title =$("#title1").val();
        if(!title){
            layerTipMsg(false,"提示!","标题不能为空!");
            isSubmit=false;
            return;
        }
        // var guideThought = $("#guideThought").val();
        // if(!guideThought){
        //     layerTipMsg(false,"提示!","指导思想不能为空!");
        //     isSubmit=false;
        //     return;
        // }
        // var workObjective= $("#workObjective").val();
        // if(!workObjective){
        //     layerTipMsg(false,"提示!","工作目标不能为空!");
        //     isSubmit=false;
        //     return;
        // }
        // var mainTask= $("#mainTask").val();
        // if(!mainTask){
        //     layerTipMsg(false,"提示!","主要内容不能为空!");
        //     isSubmit=false;
        //     return;
        // }
        // var activityArrange= $("#activityArrange").val();
        // if(!activityArrange){
        //     layerTipMsg(false,"提示!","工作安排不能为空!");
        //     isSubmit=false;
        //     return;
        //}
        var options = {
            url : "${request.contextPath}/familydear/plan/edu/savePlan",
            dataType : 'json',
            success : function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                    $("#arrange-commit").removeClass("disabled");
                    isSubmit=false;
                    return;
                }else{
                    layer.closeAll();
                    layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
                    isSubmit=false;
                    showPlanList();
                }
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $("#subForm").ajaxSubmit(options);
    })

    function showPlanList(){
        var year = $("#searchYear").val();
        var title = $("#title").val();
        var url =  '${request.contextPath}/familydear/plan/famDearPlanList?year='+year+"&title="+title;
        $("#showList1").load(url);
    }

</script>