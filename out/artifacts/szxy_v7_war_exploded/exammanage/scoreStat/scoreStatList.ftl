<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover" id="showtable${viewType!}">
	<thead>
		<tr>
			<th class="text-center">序号</th>
			<th class="text-center">考试名称</th>
			<th class="text-center">年级</th>
			<th class="text-center">设置统计参数</th>
			<th class="text-center">统计</th>
		</tr>
	</thead>
	<tbody>
	<#if examInfoList?exists>
	    <#list examInfoList as item>
		<tr>
			<td class="text-center">${item_index+1}</td>
			<td class="text-center">${item.examName!}</td>
			<td class="text-center">${item.gradeCodeName!}</td>
			<#if item.isStat?default('0')=='0'>
				<td class="text-center"><a href="javascript:" onclick="doSetStatParm('${item.id}','0')" class="color-blue">去设置</a></td>
				<td class="text-center"><a href="javascript:" onclick="doSetStat('${item.id}')" class="color-blue">去统计</a></td>
			<#else>
				<td class="text-center"><a href="javascript:" onclick="doSetStatParm('${item.id}','1')" class="color-blue">去查看</a></td>
				<td class="text-center"><a href="javascript:" onclick="doReSetStat('${item.id}')" class="color-blue">重新统计</a>
				<!--<a href="javascript:" onclick="doShowResult('${item.id}')" class="table-btn show-details-btn">查看</a>-->
				
				</td>
			</#if>
			
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
function doSetStatParm(examId,isView){
	var url =  '${request.contextPath}/exammanage/scoreStat/setStatParm/page?examId='+examId+"&isView="+isView;
	$("#scoreStatDiv").load(url);	
}
function doSetStat(examId){
	var url =  '${request.contextPath}/exammanage/scoreStat/setStat/page?examId='+examId;
	$("#scoreStatDiv").load(url);	
}
function doReSetStat(examId){
	var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
			showConfirm("是否确定重新统计，一旦点击确认，将删除已有统计结果",options,function(){
			$.ajax({
				url:'${request.contextPath}/exammanage/scoreStat/removeStat',
				data:{"examId":examId},
				dataType : 'json',
				type:'post',
				success:function(data) {
					var jsonO = data;
			 		if(jsonO.success){
			 			layer.closeAll();
					  	//doSetStat(examId);
					  	showScoreStatIndex();
			 		}
			 		else{
			 			layer.closeAll();
			 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
					}
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
				
			},function(){});
}
function doShowResult(examId){
	
}
</script>
