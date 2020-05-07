<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="box print">
    <div class="tab-pane">
        <div class="box-body">
            <div class="filter filter-f16">
                <div class="filter-item">
                    <div class="filter-content">
                        <div class="input-group input-group-search">
                            <span class="filter-name">学生姓名：</span>
                            <div class="pos-rel pull-left">
                                <input type="text" name="stuName" id="stuName" value="${stuName!}" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入学生姓名">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="filter-item">
                    <div class="filter-content">
                        <div class="input-group input-group-search">
                            <span class="filter-name">干部姓名：</span>
                            <div class="pos-rel pull-left">
                                <input type="text" name="ganbName" id="ganbName" value="${ganbName!}" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入干部姓名">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="filter-item">
                    <div class="filter-content">
                        <div class="input-group input-group-search">
                            <span class="filter-name">学生电话：</span>
                            <div class="pos-rel pull-left">
                                <input type="text" name="stuPhone" id="stuPhone" value="${stuPhone!}" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入学生电话">
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
                        <#if hasPermission>
                        	<a class="btn btn-blue" onclick="addTwo()">新增</a>
                        	<a href="javascript:doStuImport();" class="btn btn-blue btn-seach">导入</a>
                        </#if>
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
	function doStuImport(){
     	$(".model-div").load("${request.contextPath}/familydear/stuImport/import/main");
    }

    function addTwo(){
        var url = "${request.contextPath}/familydear/threeInTwoStu/edu/edit/page";
        $(".model-div").load(url);
    }
    $(function(){
        showList();
    });
    function showList(){
        var stuName = $("#stuName").val();
        var ganbName = $("#ganbName").val();
        var stuPhone = $("#stuPhone").val();
        var url =  '${request.contextPath}/familydear/threeInTwoStu/edu/stuManage/List?stuName='+encodeURIComponent(encodeURIComponent(stuName))+"&ganbName="+encodeURIComponent(encodeURIComponent(ganbName))+"&stuPhone="+stuPhone+"&currentPageIndex="+'${currentPageIndex!}'+"&currentPageSize="+'${currentPageSize!}';
        $("#showList1").load(url);
    }

</script>

