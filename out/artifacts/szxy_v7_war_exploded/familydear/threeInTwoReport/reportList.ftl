<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#if famDearThreeInTwoReports?exists&& (famDearThreeInTwoReports?size > 0)>
<table class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th width="17%">活动时间</th>
			<th width="30%">对象学生</th>
			<th width="12%">主题</th>
			<th width="26%">类型</th>
            <th width="15%">操作</th>
		</tr>
	</thead>
	<tbody>
	    <#list famDearThreeInTwoReports as item>
		<tr>
			<td >${((item.startTime)?string('yyyy-MM-dd'))?if_exists}至${((item.endTime)?string('yyyy-MM-dd'))?if_exists}</td>
			<td >${item.stuNames!}</td>
			<td >${item.titleStr!}</td>
			<td >${item.typeStr!}</td>
			<td><a class="table-btn color-blue" style="cursor: pointer"  onclick="edit('${item.id}')">修改</a>
				<a class="table-btn color-red" style="cursor: pointer"  onclick="del('${item.id}')"> 删除</a>
            </td>
		</tr>
		</#list>
	</tbody>
</table>
	<@htmlcom.pageToolBar container="#showList1" class="noprint"/>
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

function edit(id,type) {
    var url = "${request.contextPath}/familydear/threeInTwo/edu/report/edit?id="+id;
    $(".model-div").load(url);
}
function del(id) {
    showConfirmMsg('确认删除？','提示',function(){
        var ii = layer.load();
        $.ajax({
            url: '${request.contextPath}/familydear/threeInTwo/edu/report/delete',
            data: {'id':id},
            type:'post',
            success:function(data) {
                layer.closeAll();
                var jsonO = JSON.parse(data);
                if(jsonO.success){
                    doSearch();
                }else{
                    layerTipMsg(jsonO.success,"失败",jsonO.msg);
                }
                layer.close(ii);
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    });

}
</script>