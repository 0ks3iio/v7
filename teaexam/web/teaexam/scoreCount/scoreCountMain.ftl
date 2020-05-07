<div class="box box-default">
	<div class="box-body clearfix">
<input type="hidden" id="type" value="0">
        <div class="filter">
			  <div class="filter-item">
                  <span class="filter-name">年份：</span>
                  <div class="filter-content">
	                  <select name="year" id="year" class="form-control" onchange="searchExamList()">
                            <#list minYear..maxYear as item>
                                <option value="${item!}" <#if '${year!}'=='${item!}'>selected="selected"</#if>>${item!}年</option>
                            </#list>
	                  </select>
                  </div>
              </div>
		</div>	
		<table class="table table-striped table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th width="20%" style="word-break:break-all;">考试名称</th>										
					<th width="30%" style="word-break:break-all;">考试科目</th>
					<th>考试时间</th>
					<th>状态</th>
					<th>操作</th>
				</tr>
				</thead>
				<tbody>
					<#if teaexamInfoList?exists && teaexamInfoList?size gt 0>
						<#list teaexamInfoList as item>
						    <tr>
						       <td>${item_index+1!}</td>
						       <td width="20%" style="word-break:break-all;">${item.examName!}</td>
						       <td width="30%" style="word-break:break-all;">${item.subNames!}</td>
						       <td>${item.examStart?string("yyyy-MM-dd")!}~${item.examEnd?string("yyyy-MM-dd")!}</td>
						       <td>
						           <#if item.status == '1'>
						           未开始考试
						           <#elseif item.status == '2'>
						           正在考试中
						           <#elseif item.status == '3'>
						           未统计成绩
						           <#elseif item.status == '4'>
						           已统计成绩
						           </#if>
						       </td>
						       <td>
						       <#if item.status == '1'>
						          
						       <#elseif item.status == '2'>
						           
						       <#elseif item.status == '3'>
						           <a class="color-blue mr10" href="javascript:void(0);" onClick="countScore('${item.id!}')">开始统计</a>
						       <#elseif item.status == '4'>
						           <a class="color-blue mr10" href="javascript:void(0);" onClick="countScore('${item.id!}');">重新统计</a>
						           <a class="color-blue mr10" href="javascript:void(0);" onClick="showDetailTab('${item.id!}');">查看结果</a>
						           <a class="color-blue mr10" href="javascript:void(0);" onClick="downloadScore('${item.id!}')">导出结果</a>
						       </#if>
						       </td>
						    </tr>		
						</#list>
					<#else>
						<tr>
							<td colspan="6" align="center">暂无数据</td>
						</tr>
					</#if>
				</tbody>
		</table>			
	</div>
</div>
<script>
function searchExamList(){
    var acadyear = $('#year').val();
    var semester = $('#type').val();
    var url = "${request.contextPath}/teaexam/scoreCount/index/page?year="+acadyear+"&type="+semester;
    $(".model-div").load(url);
}

var isSubmit=false;
function countScore(examId){
    if(isSubmit){
    	isSubmit = true;
		return;
	}
	$.ajax({
		url:'${request.contextPath}/teaexam/scoreCount/countScore',
		data:{'examId':examId},
		type:'post', 
		dataType:'json',
		success:function(data){
	    	if(data.success){
	 			layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
				searchExamList();
	 		}else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 		}	
		}
	});
}

function showDetailTab(examId){
    var acadyear = $('#year').val();
    var semester = $('#type').val();
    var url = "${request.contextPath}/teaexam/scoreCount/showDetailTab?examId="+examId+"&year="+acadyear+"&type="+semester;
    $(".model-div").load(url);
}

function downloadScore(examId){
    document.location.href="${request.contextPath}/teaexam/scoreCount/exportAll?examId="+examId;
}
</script>