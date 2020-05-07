<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#if enrollmentList?exists&& (enrollmentList?size > 0)>
<table class="table table-bordered table-striped table-hover">
    <thead>
    <tr>
        <th width="20%">学校</th>
        <th width="20%">上级单位</th>
        <th width="15%">统招计划人数</th>
        <th width="15%">提招计划人数</th>
        <th width="15%">特招计划人数</th>
        <th width="15%">操作</th>
    </tr>
    </thead>
    <tbody>
	    <#list enrollmentList as item>
        <tr>
            <td >${item.schoolName!}</td>
            <td >${item.parentSchName!}</td>
            <td >${item.unifiedNum?default(0)}</td>
            <td>${item.trickNum?default(0)}</td>
            <td >${item.specialNum?default(0)}</td>
            <td ><a href="javascript:void(0);" onclick="doEdit('${item.id}')">编辑</a></td>
        </tr>
        </#list>
    </tbody>
</table>
<#else >
    <div class="no-data-container">
        <div class="no-data">
                    <span class="no-data-img">
                        <img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
                    </span>
            <div class="no-data-body">
                <p class="no-data-txt">暂无相关数据</p>
            </div>
        </div>
    </div>
</#if>
<#--<@htmlcom.pageToolBar container="#tabDiv" class="noprint">-->
<#--</@htmlcom.pageToolBar>-->
<script type="text/javascript">
    function doEdit(id) {
        var url = "${request.contextPath}/exammanage/edu/enrollment/editEnrollmentNumSet?id="+id;
        indexDiv = layerDivUrl(url,{title: "信息",width:500,height:300});
    }

</script>