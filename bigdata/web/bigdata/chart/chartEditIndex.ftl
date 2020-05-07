<script src="${request.contextPath}/bigdata/v3/static/plugs/chosen/chosen.jquery.min.js"></script>
<!-- 图表设置编辑页面 -->
<div class="layer layer-remove">
    <div class="layer-content text-center">
        <div>
            确定删除?
        </div>
    </div>
</div>
<div class="box">
    <div class="box-header clearfix">
        <div class="block">
            <div class="position-relative select-height">
                <button type="button" class="btn btn-blue js-add-chart">新建图表</button>
                <button type="button" class="btn btn-default js-subscription">批量操作</button>
                <div class="form-group no-margin">
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
                <#--<select multiple name="" id="forCockpit" class="form-control chosen-select  chosen-width" data-placeholder="所有">-->
                    <#--<option value="false">非大屏展示</option>-->
                    <#--<option value="true">大屏展示</option>-->
                <#--</select>-->
                <#--<div class="filter-item power" style="left: 496px;">-->
                    <#--<div class="pos-rel pull-left">-->
                        <#--<input type="text" id="chartName" placeholder="输入名称查询" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" style="width:260px;margin-right:0;">-->
                    <#--</div>-->
                    <#--<div class="input-group-btn">-->
                        <#--<button type="button" class="btn btn-default" onclick="searchChart()" >-->
                            <#--<i class="fa fa-search"></i>-->
                        <#--</button>-->
                    <#--</div>-->
                <#--</div>-->
                <div class="form-group no-margin power">
                    <div class="input-group">
                        <input type="text"  class="form-control" id="chartName" placeholder="输入名称查询" />
                        <a href="javascript:;" onclick="searchChart();" class="input-group-addon"><i class="wpfont icon-search"></i></a>
                    </div>
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
        var forCockpit = "";
        $('#chart-load-list').load(_contextPath + '/bigdata/chart/editList?tags=' + tags
                + "&chartName=" + $('#chartName').val() + "&_pageSize=20&_pageIndex=1&forCockpit="+forCockpit);
    }
</script>
<script src="${request.contextPath}/bigdata/v3/static/js/chartEdit.index.js"/>
