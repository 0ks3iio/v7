<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#if registerInfoList?exists && registerInfoList?size gt 0>
<div class="filter">
	<div class="filter-item">
		<#if hasNoCard?default(false)>
		<a class="btn btn-blue" href="javascript:;" onclick="reCreate();">自动生成</a>
		<#else>
		<span class="filter-name">已自动生成</span>
		</#if>
		<#--
		if hasCard?default(false)>
		<a class="btn btn-blue" href="javascript:;" onclick="reCreate();">重新生成</a>
		</#if-->
	</div>
</div>
<div class="table-container">
	<div class="table-container-body">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th width="5%">序号</th>
					<th>教师姓名</th>
					<th width="5%">性别</th>
					<th>民族</th>
					<th width="15%">身份证号</th>
					<th>单位</th>
					<th>报名科目</th>
					<th>考号</th>
				</tr>
			</thead>
			<tbody>
				<#list registerInfoList as item>
				<tr>
					<td>${item_index+1!}</td>
				    <td>${item.teacherName!}</td>
				    <td>${item.sex!}</td>
				    <td>${item.nation!}</td>
				    <td>${item.identityCard!}</td>
				    <td>${item.unitName!}</td>
				    <td width="20%" style="word-break:break-all;">${item.subName!}</td>
				    <td>${item.cardNo!}</td>
				</tr>
				</#list>
			</tbody>
		</table>
		<@htmlcom.pageToolBar container="#showList">
	    </@htmlcom.pageToolBar>
	</div>
</div>
<#else>
<div class="no-data-container">
	<div class="no-data">
		<span class="no-data-img">
			<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
		</span>
		<div class="no-data-body">
			<p class="no-data-txt">没有相关数据</p>
		</div>
	</div>
</div>
</#if>
<script>
function autoCreate(){
	var url = '${request.contextPath}/teaexam/siteSet/setIndex/${examId!}/cardno/generate';
	$.ajax({
            type: "POST",
            url: url,
            data: {},
            success: function(msg){
                if(msg.success){
                    layer.msg('生成成功！', {
						offset: 't',
						time: 2000
					});
                    toNo();
                }else{
                    layerTipMsg(false,"失败",msg.msg);
                }
            },
            dataType: "JSON"
        });
}

function reCreate(){
	showConfirmMsg("已有考号的考生，也会重新生成，确定要生成考号吗？","提示",function(){
		var url = '${request.contextPath}/teaexam/siteSet/setIndex/${examId!}/cardno/generate';
		$.ajax({
	            type: "POST",
	            url: url,
	            data: {'type':'1'},
	            success: function(msg){
	                if(msg.success){
	                    layer.msg('生成成功！', {
							offset: 't',
							time: 2000
						});
	                    toNo();
	                }else{
	                    layerTipMsg(false,"失败",msg.msg);
	                }
	            },
	            dataType: "JSON"
	        });
	});
}
</script>