<!-- 图表查询首页 -->
<div class="box">
    <div class="box-header">
        <div class="filter-item block">
            <div class="position-relative select-height">
                <select multiple name="" id="select" <#--onchange="tagsChange();"-->
                        class="form-control chosen-select"
                        data-placeholder="输入标签查询">
                    <#if tags?exists && tags?size gt 0>
                        <#list tags as tag>
                            <option value="${tag.id!}">${tag.name!}</option>
                        </#list>
                    </#if>
                </select>
            </div>
            <div class="filter-item power" style="left: 330px;">
                <div class="pos-rel pull-left">
                    <input type="text" id="chartName" placeholder="输入名称查询" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" style="width:234px;margin-right:0;">
                </div>
                <div class="input-group-btn">
                    <button type="button" class="btn btn-default" onclick="searchChart()" >
                        <i class="fa fa-search"></i>
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div class="box-body padding-top-20">
        <div class="card-list clearfix report">
            <div id="chart-load-list">

            </div>
        </div>
        <div class="text-center" >
            <a href="javascript:void(0);" id="_get_more">查看更多 > ></a>
        </div>
    </div>
    <input type="hidden" id="_pageSize"  />
    <input type="hidden" id="_pageIndex"  />
    <input type="hidden" id="_maxPageIndex"  />
</div>
<script src="${request.contextPath}/bigdata/v3/static/js/chartQuery.index.js"/>
<script>
    function searchChart() {
        var tagIds = $('#select').val();
        var tags = "";
        if (tagIds != null) {
            $.each(tagIds, function (n, v) {
                if (n == 0){
                    tags = v;
                } else {
                    tags = tags + "," + v;
                }
            })
        }
        $('#chart-load-list').load(_contextPath + '/bigdata/chartQuery/chartList?tags=' + tags
                + "&chartName=" + $('#chartName').val() + "&_pageSize=20&_pageIndex=1");
    }
</script>