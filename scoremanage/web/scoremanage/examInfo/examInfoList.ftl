<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th>考试编号</th>
			<th>考试名称</th>
			<th>考试类型</th>
			<th>统考类型</th>
			<th>是否新高考</th>
			<#--<th>非正常成绩是否统分</th>-->
			<th>起始日期</th>
			<th>结束日期</th>
			<#if searchType=='1'>
			<th>操作</th>
			</#if>
		</tr>
	</thead>
	<tbody>
	<#if examInfoList?exists>
	    <#list examInfoList as item>
		<tr>
			<td>${item.examCode!}</td>
			<td>${item.examName!}</td>
			<td>${mcodeSetting.getMcode("DM-KSLB", item.examType?default(1)?string)}</td>
			<td>${item.examUeTypeName!}</td>
			<td>${mcodeSetting.getMcode("DM-BOOLEAN", item.isgkExamType?default("1"))}</td>
			<#--<td>${mcodeSetting.getMcode("DM-BOOLEAN", item.isTotalScore?default("1"))}</td>-->
			<td>${(item.examStartDate?string('yyyy-MM-dd'))!}</td>
			<td>${(item.examEndDate?string('yyyy-MM-dd'))!}</td>
			<#if searchType=='1'>
			<td>
				<a href="javascript:doEdit('${item.id!}','${item.examUeType!}');" class="table-btn color-red">编辑</a>
				<a href="javascript:doDelete('${item.id!}');" class="table-btn color-red">删除</a>
				<a href="javascript:toSynch('${item.id!}');" class="table-btn color-red">同步</a>
			</td>
			<#else>
				<a href="javascript:toSynch('${item.id!}');" class="table-btn color-red">同步</a>n
			</#if>
		</tr>
		</#list>
	</#if>												
	</tbody>
</table>
<#if examInfoList?exists>
<@htmlcom.pageToolBar container="#showList"/>
</#if>
<script>
function toSynch(id){
	showConfirmMsg('您确认要同步？','提示',function(){
		var ii = layer.load();
	     $.ajax({
			url:'${request.contextPath}/scoremanage/examInfo/synch',
			data: {'id':id},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
                    layer.closeAll();
					layer.msg(jsonO.msg,{
						offset: 't',
						time: 2000
					});
				  	showList();
		 		}
		 		else{
		 			layerTipMsg(jsonO.success,"同步失败",jsonO.msg);
		 			isSubmit=false;
				}
				layer.close(ii);
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	});
}
function doDelete(id){
	if(confirm('您确认要删除？')){
	     var ii = layer.load();
	     $.ajax({
			url:'${request.contextPath}/scoremanage/examInfo/delete',
			data: {'id':id},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
                    layer.closeAll();
					layer.msg(jsonO.msg,{
						offset: 't',
						time: 2000
					});
				  	showList();
		 		}
		 		else{
		 			layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
		 			//$("#arrange-commit").removeClass("disabled");
		 			isSubmit=false;
				}
				layer.close(ii);
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
}

function doEdit(id,examUeType){
   var searchAcadyear = $('#searchAcadyear').val();
   var searchSemester = $('#searchSemester').val();
   var searchType = $('#searchType').val();
   var str = "?searchAcadyear="+searchAcadyear+"&searchSemester="+searchSemester+"&searchType="+searchType+"&id="+id;
   var url = "${request.contextPath}/scoremanage/examInfo/edit/page"+str;
   indexDiv = layerDivUrl(url,{title: "信息",width:750,height:600}); 
}
</script>	
	</body>
</html>
