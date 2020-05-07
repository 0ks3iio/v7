		<div class="table-container">
           <div class="table-container-header">共${studentList?size!}份结果</div>
           <div class="table-container-body print">
              <table class="table table-bordered table-striped table-hover">
                  <thead>
	                    <tr>
		                     <th>序号</th>
		                     <th>姓名</th>
		                     <th>学号</th>
		                     <th>性别</th>
		                     <th>原行政班</th>
		                     <th width="30%">选考班</th>
		                     <th width="30%">学考班</th>
	                     </tr>
                 </thead>
                 <tbody>
                 <#if studentList?exists && studentList?size gt 0>
                      <#list studentList as item>
	                     <tr>
		                     <td>${item_index+1!}</td>
		                     <td>${item.studentName!}</td>
		                     <td>${item.studentCode!}</td>
		                     <td><#if item.sex?default(0) == 1>男<#else>女</#if></td>
		                     <td>${item.className!}</td>
		                     <td>${item.email!}</td>
		                     <td>${item.homepage!}</td>
	                     </tr>
	                  </#list>	
	             </#if>											
                 </tbody>
           	</table>
       	</div>
 	</div>
<script>
</script>