<#import "/fw/macro/htmlcomponent.ftl" as html>
<form id="submitForm">
	<div class="table-container-body">
		<table class="table table-striped table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<#if type?default(0)==0>
					<th>考试名称</th>										
					<th>考试科目</th>
					<#else>
					<th>培训名称</th>										
					<th>培训项目</th>
					</#if>
					<th>发布状态</th>
					<th>报名开始时间</th>
					<th>报名结束时间</th>
					<th>操作</th>
				</tr>
				</thead>
				<tbody>
					<#if teaexamInfoList?exists && teaexamInfoList?size gt 0>
						<#list teaexamInfoList as item>
						    <tr>
						       <td width="5%">${item_index+1!}</td>
						       <td width="20%" style="word-break:break-all;">${item.examName!}</td>
						       <td style="word-break:break-all;"><@html.cutOff4List str='${item.subNames!}' length=50 /></td>
						       <td style="white-space: nowrap"><#if item.state == 1>未发布<#else>已发布</#if></td>
						       <td style="white-space: nowrap">${item.registerBegin?string("yyyy-MM-dd")!}</td>
						       <td style="white-space: nowrap">${item.registerEnd?string("yyyy-MM-dd")!}</td>
						       <td style="white-space: nowrap">
						       <#if item.state == 1>
						          <a class="color-blue mr10" href="javascript:void(0);" onClick="editExam('${item.id!}','1');">编辑</a>
						       <#else>
						          <a class="color-blue mr10" href="javascript:void(0);" onClick="editExam('${item.id!}','2');">详情</a>
						       </#if>						       
						       <a class="color-red mr10" href="javascript:void(0);" onClick="deleteExam('${item.id!}');">删除</a>
						       </td>
						    </tr>		
						</#list>
					<#else>
						<tr>
							<td colspan="7" align="center">暂无数据</td>
						</tr>
					</#if>
				</tbody>
		</table>
	</div>
</form>
<script>
function editExam(examId, state){
    var acadyear = $('#year').val();
    var semester = $('#type').val();
    var url = "${request.contextPath}/teaexam/examInfo/examInfoEdit?year="+acadyear+"&type="+semester+"&examId="+examId+"&state="+state;
    $(".model-div").load(url);
}

function deleteExam(examId){
    var acadyear = $('#year').val();
    var semester = $('#type').val();
    var msg = '删除考试会同时删除该考试的考场编排以及教师考试成绩，请慎重！';
    if(semester == 1){
    	msg = '确定要删除培训吗？';
    }
    var index = layer.confirm(msg, {
			btn: ["确定", "取消"]
		}, function(){
		    $.ajax({
		        url:"${request.contextPath}/teaexam/examInfo/examInfoDelete",
		        data:{id:examId},
		        dataType : 'json',
		        success : function(data){
		 	       if(!data.success){
		 		      layerTipMsg(data.success,"删除失败",data.msg);
		 		      return;
		 	       }else{
		 		      layer.closeAll();
				      layerTipMsg(data.success,"删除成功","");
                      var url = "${request.contextPath}/teaexam/examInfo/index/page?year="+acadyear+"&type="+semester;
                      $(".model-div").load(url);
    		       }
		        },
	        });
		})
}
</script>