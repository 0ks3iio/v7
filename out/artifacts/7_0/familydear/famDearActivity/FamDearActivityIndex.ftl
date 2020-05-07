<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="box print" id="mydiv">
    <div class="tab-pane">
        <div class="box-body">
            <div class="filter filter-f16">
                <div class="filter-item">
                    <span class="filter-name">年度：</span>
                    <div class="filter-content">
                        <select class="form-control" id="searchYear1" name="searchYear" onChange="yearChange()">
                            <#if acadyearList?exists && (acadyearList?size>0)>
                                <#list acadyearList as item>
                                     <option value="${item!}" <#if nowYear?default('a')==item?default('b')>selected</#if>>${item!}</option>
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
                        <select onChange="planChange()" id="planList" name="planId" class="form-control" style="width:180px;">
                                <#if planList?exists && (planList?size>0)>-->
                                <#list planList as item>
                                <option style="overflow: hidden;text-overflow: ellipsis;white-space: nowrap;width: 200px;" value="${item.id!}"  >${item.title!}</option>
                                </#list>
                                <#else>
                                <option value="">未设置</option>
                                </#if>
                        </select>
                    </div>
                    <div class="filter-item">
                        <div class="filter-content">
                            <div class="input-group input-group-search">
                                <span class="filter-name">结亲村：</span>
                                <div class="pos-rel pull-left">
                                    <select name="village" id="village" class="form-control" notnull="false" onChange="showList1();" style="width:168px;">
                                    ${mcodeSetting.getMcodeSelect("DM-XJJQC", '', "1")}
                                    </select>
                                <#--<input type="text" name="village" id="village" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入结亲村">-->
                                </div>
                                <#--<div class="input-group-btn">-->
                                    <#--<button type="button" class="btn btn-default" onclick="showList1()">-->
                                        <#--<i class="fa fa-search"></i>-->
                                    <#--</button>-->
                                <#--</div>-->
                            </div>
                        </div>
                    </div>
                    <#--<div class="filter-content">-->
                        <#--<div class="input-group input-group-search">-->
                            <#--<span class="filter-name">计划：</span>-->
                            <#--<div class="filter-content">-->
                                <#--<select onChange="planChange()" id="planList" name="planId" class="form-control" style="width:180px;">-->
                                <#--<#if planList?exists && (planList?size>0)>&ndash;&gt;-->
                                <#--<#list planList as item>-->
                                <#--<option style="overflow: hidden;text-overflow: ellipsis;white-space: nowrap;width: 200px;" value="${item.id!}"  >${item.title!}</option>-->
                                <#--</#list>-->
                                <#--<#else>-->
                                <#--<option value="">未设置</option>-->
                                <#--</#if>-->
                                <#--</select>-->
                            <#--</div>-->
                                <#--&lt;#&ndash;<input type="text" name="plan" id="plan" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入计划" &lt;#&ndash;onkeydown="dispResInvigilate()"&ndash;&gt;>&ndash;&gt;-->
                        <#--</div>-->
                    <#--</div>-->
                </div>
            </div>
            <div class="filter filter-f16">
                <div class="filter-item">
                    <div class="filter-content">
                        <span class="filter-name">报名开始时间：</span>
                        <div class="pos-rel pull-left">
                            <input name="title"  class='form-control date-picker' vtype='data' id="startTime" autocomplete="off" data-provide="typeahead" placeholder="请输入报名开始时间" onchange="showList1()" " <#--onkeydown="dispResInvigilate()"-->>
                        </div>
                    </div>
                </div>
                <div class="filter-item">
                    <div class="filter-content">
                        <span class="filter-name">报名结束时间：</span>
                        <div class="pos-rel pull-left">
                            <input name="title" class='form-control date-picker' vtype='data' id="endTime" autocomplete="off" data-provide="typeahead" placeholder="请输入报名结束时间" onchange="showList1()" ">
                        </div>
                    </div>
                </div>
                <div class="filter-item ">
                    <div class="text-right">
                        <a class="btn btn-white" onclick="clearTime()">清空时间</a>
                    </div>
                </div>
            <#--<div class="filter-item">-->
            <#--<div class="filter-content">-->
            <#--<span class="filter-name">报名开始时间：</span>-->
            <#--<div class="pos-rel pull-left">-->
            <#--<input type="text" name="title" id="title" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入标题" &lt;#&ndash;onkeydown="dispResInvigilate()"&ndash;&gt;>-->
            <#--</div>-->
            <#--</div>-->
            <#--</div>-->
            <#--<div class="filter-item">-->
            <#--<div class="filter-content">-->
            <#--<span class="filter-name">报名结束时间：</span>-->
            <#--<div class="pos-rel pull-left">-->
            <#--<input type="text" name="title" id="title" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入标题" &lt;#&ndash;onkeydown="dispResInvigilate()"&ndash;&gt;>-->
            <#--</div>-->
            <#--</div>-->
            <#--</div>-->
                <#--<div class="filter-item ">-->
                    <#--<div class="text-right">-->
                        <#--<a class="btn btn-blue" onclick="showList1()">查询</a>-->
                    <#--</div>-->
                <#--</div>-->
                <div class="filter-item ">
                    <div class="text-right">
                        <#if hasPermission><a class="btn btn-blue" onclick="addPlan()">新增</a></#if>
                    </div>
                </div>
                <#if hasPermission>
                    <div class="filter-item ">
                        <div class="text-right">
                            <a class="btn btn-blue" onclick="copyPlan()">复制</a>
                        </div>
                    </div>
                </#if>
            </div>
            <div class="table-container" id="showList1">
                <#--<div class="table-container-body" id="showList1">-->
                <#--</div>-->
            </div>
        </div>
    </div>
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">
    function addPlan() {
        var year = $("#searchYear1").val();
        var plan = $("#planList").val();
        var url;
        if (!plan || plan == "本年暂无计划") {
            url = "${request.contextPath}/familydear/activity/edu/add?year=" + year
        } else {
            url = "${request.contextPath}/familydear/activity/edu/add?year=" + year + "&planId=" + plan;
        }
        indexDiv = layerDivUrl(url,{title: "新增结亲活动",width:900,height:540});
    }
    function copyPlan() {
        var year = $("#searchYear1").val();
        if(!year){
            layerTipMsg(false,"提示!","没有可供复制的年份!");
            isSubmit=false;
            return;
        }
        var planId = $("#planList").val();
        if(!planId){
            layerTipMsg(false,"提示!","没有可供复制的计划!");
            isSubmit=false;
            return;
        }
        var url = "${request.contextPath}/familydear/activity/activityCopy?year="+ year+"&planId="+planId;
        indexDiv = layerDivUrl(url,{title: "复制轮次",width:600,height:400});
    }
    function clearTime(){
        $("#startTime").val("");
        $("#endTime").val("");
        showList1();

    }
    $(function(){
        var viewContent={
            'format' : 'yyyy-mm-dd',
            'minView' : '2'
        };
        initCalendarData("#mydiv",".date-picker",viewContent);
        // var viewContent1={
        //     'format' : 'yyyy-mm-dd',
        //     'minView' : '2'
        // };
        // initCalendarData("#mydiv",".date-picker",viewContent1);
        showList1();
    });
    function showList1(){
        var year = $("#searchYear1").val();
        var plan = $("#planList").val();
        var options=$("#village option:selected");
        var village = "";
        if(options.val()) {
            village = options.text();
        }
        var startTime =$("#startTime").val();
        var endTime =$("#endTime").val();
        if(startTime&&endTime) {
            if (startTime > endTime) {
                layerTipMsg(false, "提示!", "报名开始时间不能大于报名结束时间!");
                return;
            }
        }
        var url =  '${request.contextPath}/familydear/activity/edufamDearActivityList?year='+year+"&planId="+plan+"&village="+village+"&endTime="+endTime+"&startTime="+startTime;
        $("#showList1").load(url);
    }

    function yearChange(){
        var year = $("#searchYear1").val();
        var url = "${request.contextPath}/familydear/activity/edu/yearChange";
        $.ajax({
            url:url,
            data:{'year':year},
            dataType : 'json',
            type : 'post',
            success : function(data){
                var htmlStr="";
                $("#planList").empty();
                if(data.length>0){
                    for(var i=0;i<data.length;i++){
                        htmlStr = htmlStr+"<option value='"+data[i].id+"'>"+data[i].name+"</option>";
                    }
                }else {
                    htmlStr = htmlStr+"<option value=''>未设置</option>";
                }
                $("#planList").html(htmlStr);
                showList1();
            }

        })
    }
    
    function planChange() {
        $("#startTime").val("");
        $("#endTime").val("");
        showList1();
    }

</script>

