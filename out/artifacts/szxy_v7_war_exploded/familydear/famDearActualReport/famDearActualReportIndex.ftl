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
                                     <option value="${item!}" <#if year?default('a')==item?default('b')> selected <#else ><#if nowYear?default('a')==item?default('b')> selected </#if></#if>>${item!}</option>
                                </#list>
                            <#else>
                                <option value="">未设置</option>
                            </#if>
                        </select>
                    </div>
                </div>
                <div class="filter-item">
                    <span class="filter-name">访亲轮次名称 ：</span>
                    <div class="filter-content">
                        <select type="text" class="form-control" id="activityId" onChange="activityChange()">
                        </select>
                    </div>
                </div>
                <div class="filter-item">
                    <div class="filter-content">
                        <div class="input-group input-group-search">
                            <span class="filter-name">结亲村：</span>
                            <div class="pos-rel pull-left">
                                <select name="village" id="village" class="form-control" notnull="false" onChange="showList1();" style="width:130px;">
                                    ${mcodeSetting.getMcodeSelect("DM-XJJQC","${villageVal!}", "1")}
                                </select>
                                <#--<input type="text" name="village" id="village" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入结亲村" value="${village!}">-->
                            </div>
                            <div class="input-group-btn">
                                <button type="button" class="btn btn-default" onclick="showList1()">
                                    <i class="fa fa-search"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="filter-item ">
                    <div class="text-right">
                        <a class="btn btn-blue" onclick="addReport()">新增</a>
                    </div>
                </div>
                <div class="filter-item ">
                    <div class="text-right">
                        <a class="btn btn-blue" onclick="exportList()">导出</a>
                    </div>
                </div>
            </div>
            <div class="table-container" id="showList1">
            <#--<div class="table-container-body" id="showList1">-->
            <#--</div>-->
            </div>
        </div>
    </div>
</div>
<script>
    $(function () {
        //showList1();
        yearChange();
    });
    function showList1(){
        var year = $("#searchYear1").val();
        var activityId = $("#activityId").val();
        var options=$("#village option:selected");
        var village = "";
        if(options.val()) {
            village = options.text();
        }
        var url =  '${request.contextPath}/familydear/famdearActualReport/famdearActualReportList?year='+year+"&activityId="+activityId+"&villageName="+village;
        $("#showList1").load(url);
    }

    function yearChange(){
        var year = $("#searchYear1").val();
        var url = "${request.contextPath}/familydear/famdearActualReport/activityList";
        $.ajax({
            url:url,
            data:{'year':year},
            dataType : 'json',
            type : 'post',
            success : function(data){
                var htmlStr="";
                $("#activityId").empty();
                if(data.length>0){
                    for(var i=0;i<data.length;i++){
                        if("${activityId!}"==data[i].id){
                            htmlStr = htmlStr+"<option value='"+data[i].id+"' selected>"+data[i].name+"</option>";
                        }else{
                            htmlStr = htmlStr+"<option value='"+data[i].id+"'>"+data[i].name+"</option>";
                        }

                    }
                }
                else {
                    htmlStr = htmlStr+"<option value=''>未设置</option>";
                }
                $("#activityId").html(htmlStr);
                showList1();
            }

        })
    }

    function activityChange(){
        showList1();
    }

    function addReport(){
        var year = $("#searchYear1").val();
        var activityId = $("#activityId").val();
        var options=$("#village option:selected");
        var village = "";
        if(options.val()) {
            village = options.text();
        }
        // var village = $("#village").val();
        if(!year){
            layerTipMsg(false,"提示!","请选择年份!");
            isSubmit=false;
            return;
        }
        if(!activityId){
            layerTipMsg(false,"提示!","请选访亲轮次活动!");
            isSubmit=false;
            return;
        }
        var url = "${request.contextPath}/familydear/famdearActualReport/famdearActualReportAdd?year="+year+"&activityId="+activityId+"&village="+village;
        indexDiv = $("#myTabDiv").load(url);
    }

    function exportList() {
        var year = $("#searchYear1").val();
        var activityId = $("#activityId").val();
        var options=$("#village option:selected");
        var village = "";
        if(options.val()) {
            village = options.text();
        }
        document.location.href="${request.contextPath}/familydear/famdearActualReport/export?year="+year+"&activityId="+activityId+"&village="+village;
        // alert("导出");
    }
</script>