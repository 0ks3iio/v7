<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#if examInfoList?exists&& (examInfoList?size > 0)>
<table class="table table-bordered table-striped table-hover" id="showtable${viewType!}">
    <thead>
    <tr>
        <th class="text-center">序号</th>
        <th class="text-center">考试名称</th>
        <th class="text-center">年级</th>
        <#if isXhzx?default(false)><th class="text-center">考场安排</th></#if>
        <th class="text-center">考场门贴</th>
        <th class="text-center">考生桌帖</th>
        <th class="text-center">班级清单</th>
        <th class="text-center">监考老师安排</th>
        <th class="text-center">考试安排总表</th>
    </tr>
    </thead>
    <tbody>
        <#list examInfoList as item>
        <tr>
            <td class="text-center">${item_index+1}</td>
            <td class="text-center">${item.examName!}</td>
            <td class="text-center">${item.gradeCodeName!}</td>
            <#if isXhzx?default(false)><td class="text-center"><a href="javascript:" onclick="doShowItem('${item.id}','1')"
                                       class="color-blue">去查看</a></td></#if>
            <td class="text-center"><a href="javascript:" onclick="doShowItem('${item.id}','2')"
                                       class="color-blue">去查看</a></td>
            <td class="text-center"><a href="javascript:" onclick="doShowItem('${item.id}','3')"
                                       class="color-blue">去查看</a></td>
            <td class="text-center"><a href="javascript:" onclick="doShowItem('${item.id}','4')"
                                       class="color-blue">去查看</a></td>
            <td class="text-center"><a href="javascript:" onclick="doShowItem('${item.id}','5')"
                                       class="color-blue">去查看</a></td>
            <td class="text-center"><a href="javascript:" onclick="doShowItem('${item.id}','6')"
                                       class="color-blue">去查看</a></td>
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
<script type="text/javascript">
    <#if viewType=="1">
    $(function () {
        var table = $('#showtable1').DataTable({
            scrollY: "148px",
            info: false,
            searching: false,
            autoWidth: false,
            sort: false,
            scrollCollapse: true,
            paging: false,
            language: {
                emptyTable: '没有数据',
                loadingRecords: '加载中...',
            }
        });
    });

    </#if>
    function doShowItem(examId, type) {
        var url = '${request.contextPath}/exammanage/examReport/showItemIndex/page?examId=' + examId + "&type=" + type;
        $("#examReportDiv").load(url);
    }
</script>
