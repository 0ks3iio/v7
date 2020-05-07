<div class="box box-default">
	<div class="box-body clearfix">
        <input type="hidden" id="type" value="0">
        <#-- div class="filter">
			  <div class="filter-item">
                  <span class="filter-name">学年：</span>
                  <div class="filter-content">
	                  <select name="year" id="year" class="form-control" onchange="searchExamList()" style="width:135px">
		                    <#list minYear..maxYear as item>
		                        <option value="${item!}" <#if '${year!}'=='${item!}'>selected="selected"</#if>>${item!}年</option>
		                    </#list>			                       
		               </select>
                  </div>
              </div>
		</div-->	
		<table class="table table-striped table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>考试名称</th>										
					<th>考试时间</th>
					<th>报考科目</th>
					<th>考试成绩</th>
					<th>考试评价</th>
				</tr>
				</thead>
				<tbody>
                   <#if teaexamInfoList?exists && teaexamInfoList?size gt 0>
						<#list teaexamInfoList as item>
						    <#assign t=0>
						    <#if regList?exists && regList?size gt 0>
						        <#list regList as reg>
						           <#if item.id == reg.examId>
						               <#assign t=t+1>
						           </#if>
						        </#list>
						    </#if>
						    <#assign a=0>
						    <#if regList?exists && regList?size gt 0>
						        <#list regList as reg>
						           <#if item.id == reg.examId>
						              <tr>
						                 <#if a==0><td rowspan="${t!}">${item_index+1!}</td></#if>
						                 <#if a==0><td rowspan="${t!}" width="20%" style="word-break:break-all;">${item.examName!}</td></#if>
						                 <#if a==0><td rowspan="${t!}">${item.examStart?string("yyyy-MM-dd")!}~${item.examEnd?string("yyyy-MM-dd")!}</td></#if>
						                 <td width="20%" style="word-break:break-all;">${reg.subName!}</td>
						                 <td>${reg.score!}</td>
						                 <td>
						                 <#if '${reg.gradeCode!}'=='1'>
						                 优秀
						                 <#elseif '${reg.gradeCode!}'=='2'>
						                 合格
						                 <#elseif '${reg.gradeCode!}'=='3'>
						                 不合格
						                 </#if>
						                 </td>
						              </tr>
						              <#assign a=a+1>
						           </#if>
						        </#list>	
						    </#if>	
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
    var url = "${request.contextPath}/teaexam/scoreQuery/index/page?year="+acadyear+"&type="+semester;
    $('.model-div').load(url);
}
</script>