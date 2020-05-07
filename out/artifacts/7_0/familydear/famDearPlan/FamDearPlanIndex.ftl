<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="box print">
    <div class="tab-pane">
        <div class="box-body">
            <div class="filter filter-f16">
                <div class="filter-item">
                    <span class="filter-name">年度：</span>
                    <div class="filter-content">
                        <select class="form-control" id="searchYear" name="searchYear" onChange="showList1()">
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
                    <div class="filter-content">
                        <div class="input-group input-group-search">
                            <span class="filter-name">标题：</span>
                            <div class="pos-rel pull-left">
                                <input type="text" name="title" id="title" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入标题" <#--onkeydown="dispResInvigilate()"-->>
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
                        <#if hasPermission><a class="btn btn-blue" onclick="addPlan()">新增</a></#if>
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

<!-- page specific plugin scripts -->
<script type="text/javascript">
    function addPlan(){
        var year = $("#searchYear").val();
        var url = "${request.contextPath}/familydear/plan/edu/add?year="+year;
        indexDiv = layerDivUrl(url,{title: "新增结亲计划",width:750,height:500});
    }
    $(function(){
        showList1();
    });
    function showList1(){
        var year = $("#searchYear").val();
        var title = $("#title").val();
        var url =  '${request.contextPath}/familydear/plan/famDearPlanList?year='+year+"&title="+title;
        $("#showList1").load(url);
    }

</script>

