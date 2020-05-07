<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="box print">
    <div class="tab-pane">
        <div class="box-body">
            <div class="filter filter-f16">
                <div class="filter-item">
                    <span class="filter-name">年度：</span>
                    <div class="filter-content">
                        <select class="form-control" id="searchYear" name="searchYear" onChange="showList()">
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
                    <div class="filter-content">
                        <div class="input-group input-group-search">
                            <span class="filter-name">标题：</span>
                            <div class="pos-rel pull-left">
                                <input type="text" name="title" id="title" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入标题" <#--onkeydown="dispResInvigilate()"-->>
                            </div>
                            <div class="input-group-btn">
                                <button type="button" class="btn btn-default" onclick="showList()">
                                    <i class="fa fa-search"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="filter-item ">
                    <div class="text-right">
                        <#if hasPermission><a class="btn btn-blue" onclick="addTwo()">新增</a></#if>
                    </div>
                </div>
            </div>
            <div class="table-container" id="showList1">
            </div>
        </div>
    </div>
</div>
<!-- page specific plugin scripts -->
<script type="text/javascript">
    function addTwo(){
        var year = $("#searchYear").val();
        var title = $("#title").val();
        var url = "${request.contextPath}/familydear/threeInTwo/edu/edit/page";
        $(".model-div").load(url);
    }
    $(function(){
        showList();
    });
    function showList(){
        var year = $("#searchYear").val();
        var title = $("#title").val();
        var url =  '${request.contextPath}/familydear/threeInTwo/edu/List/page?year='+year+"&title="+encodeURIComponent(encodeURIComponent(title));
        $("#showList1").load(url);
    }

</script>

