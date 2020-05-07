<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover" id="showtable${viewType!}">
	<thead>
		<tr>
			<th class="text-center" width="5%">序号</th>
			<th class="text-center">考试名称</th>
			<th class="text-center">考试类型</th>
			<th class="text-center">年级</th>
			<th class="text-center" width="5%">已报考</th>
			<th class="text-center" width="5%">已通过审核</th>
			<th class="text-center" width="5%">待审核</th>
			<th class="text-center" width="5%">已编排学生数</th>
			<th class="text-center" width="5%">未编排学生数</th>
			<th class="text-center" width="20%">操作</th>
		</tr>
	</thead>
	<tbody>
	<#if examInfoList?exists>
	    <#list examInfoList as item>
		<tr>
			<td class="text-center">${item_index+1}</td>
			<td class="text-center">${item.examName!}</td>
			<td class="text-center">${mcodeSetting.getMcode("DM-KSLB", item.examType?default(1)?string)}</td>
			<td class="text-center">${item.gradeCodeName!}</td>
			<td class="text-center">${item.countNum!}</td>
			<td class="text-center">${item.passNum!}</td>
			<td class="text-center">${item.auditNum!}</td>
			<td class="text-center">${item.arrangeNum!}</td>
			<td class="text-center">${item.noArrangeNum!}</td>
			
			<td class="text-center">
			<#-- <a class="color-orange" href="javascript:void(0);" onclick="arrangeResult('${item.id!}')">自动编排</a> -->
				<#if item.arrangeResult == 1>
					<a class="color-orange" href="javascript:void(0);">正在编排中...</a>
				<#else>
					<#if item.arrangeResult == -1>
						<p><font color="#FF0000" size="1">没有编排设置,无法编排</font></p>
					<#else>
						<#if item.arrangeNum?exists && item.arrangeNum gt 0>
							<a class="color-orange" href="javascript:void(0);" onclick="arrangeResult2('${item.id!}')">自动编排</a>&emsp;
							<a href="javascript:void(0);" onclick="toResultDetail('${item.id!}','${item.noArrangeNum!}')">查看结果</a>
						<#else>
							<a class="color-orange" href="javascript:void(0);" onclick="arrangeResult('${item.id!}')">自动编排</a>
						</#if>
					</#if>
				</#if>
			</td>
		</tr>
		</#list>
	</#if>												
	</tbody>
</table>	
<script type="text/javascript">
<#if viewType=="1"> 
 $(function(){
   	  var table = $('#showtable1').DataTable( {
	        scrollY: "148px",
			info: false,
			searching: false,
			autoWidth: false,
			sort: false,
	        scrollCollapse: true,
	        paging: false,
	        language:{
	        		emptyTable: '没有数据',
	        		loadingRecords: '加载中...', 
	        	}
	    } );
   });
</#if>
function arrangeResult2(examId){
	showConfirmMsg('该考试已生成考场与考生的编排数据，若重新进行自动编排将重新生成一份编排数据（即各考场的考生名单以及考生座位号将发生变化）','提示',function(ii){
         	arrangeResult(examId);
         	layer.close(ii);
		});
}

function arrangeResult(examId){
	$.ajax({
		url:"${request.contextPath}/exammanage/edu/examArrange/arrangeResult",
		data: {'examId':examId},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
				if(jsonO.success){
					layerTipMsg(jsonO.success,jsonO.msg,"");
					$(".model-div").load("${request.contextPath}/exammanage/edu/examResult/index/page");
				}else{
					layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
		},error : function(XMLHttpRequest, textStatus, errorThrown) {
 			
		}
	});
}
function toResultDetail(examId,noArrangeNum){
	var url =  '${request.contextPath}/exammanage/edu/examResult/detailList/page?examId='+examId+"&noArrangeNum="+noArrangeNum;
	$("#examResultDiv").load(url);
}
</script>

