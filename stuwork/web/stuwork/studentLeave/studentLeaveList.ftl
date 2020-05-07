<table class="table table-striped layout-fixed">
	<thead>
		<tr>
			<th>请假类型</th>
			<th>请假人</th>
			<th>开始时间</th>
			<th>结束时间</th>
			<th>请假天数</th>
			<th>请假状态</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<#if dyStudentLeaveList?exists && dyStudentLeaveList?size gt 0>
		   <#list dyStudentLeaveList as item>
		       <tr>
		          <td>${item.leaveTypeName!}</td>
		          <td>${item.stuName!}</td>
		          <td>${(item.startTime?string('yyyy-MM-dd HH:mm'))!}</td>
		          <td>${(item.endTime?string('yyyy-MM-dd HH:mm'))!}</td>
		          <td>${item.days!}</td>
		          <td>
		             <#if item.state == 1>
		                                                  未提交
		             <#elseif item.state == 2>
		                                                 待审核
		             <#elseif item.state == 3>
		                                                 通过
		             <#elseif item.state == 4>
		                                                未通过
		             </#if>
		          </td>
		          <td>
		          <#if item.state == 1>
		              <a class="color-lightblue" href="javascript:doEdit('${item.id!}');">修改</a>&nbsp;
		              <input type="hidden" id="id" value="${item.id!}">
		              <a class="color-lightblue" href="javascript:doDelete('${item.id!}');">删除</a>
		          <#elseif item.state == 2>
		              <a class="color-lightblue" href="javascript:doEdit('${item.id!}');">查看</a>&nbsp;
		          </#if>
		          </td>
		       </tr>
		   </#list>
		</#if>									
	</tbody>
</table>
<#if dyStudentLeaveList?exists && dyStudentLeaveList?size gt 0>
	<@w.pagination  container="#showList" pagination=pagination page_index=1/>
</#if>
<script>
function doEdit(id){
   var url =  '${request.contextPath}/stuwork/studentLeave/edit?id='+id;
   indexDiv = layerDivUrl(url,{title: "修改学生请假",width:380,height:420});
}

//function doEdit(id){
   //var url =  '${request.contextPath}/stuwork/studentLeave/edit?id='+id;
      //  $("#cardEditLayer").load(url,function() {
		//layerShow();
	//});
//}

//function layerShow(){
   // layer.open({
	   // type: 1,
	   // shade: 0.5,
	   // title: '修改学生请假',
	    //area: ['380px','420px'],
	    //content: $('.layer-edit')
	//})
//}

var isSubmit=false;
function doDelete(id){
if (confirm("确认删除该条记录吗？")){
   $.ajax({
		url:'${request.contextPath}/stuwork/studentLeave/delete',
		data: {'id':id},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
	 			layerTipMsg(jsonO.success,jsonO.msg,"");
	 			searchList();
	 		}else{
	 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
			}
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}
}
</script>