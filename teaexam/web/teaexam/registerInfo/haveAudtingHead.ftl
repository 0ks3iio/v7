<div class="picker-table">
	<table class="table">
		<tbody>			
			<tr>
				<th width="150">考试：</th>
				<td>
					<div class="float-left mt3">年份：</div>
					<div class="float-left mr10">
						<select name="year" id="year" class="form-control" onchange="changeExam()">
                            <#list minYear..maxYear as item>
                                <option value="${item!}" <#if '${year!}'=='${item!}'>selected="selected"</#if>>${item!}年</option>
                            </#list>
	                    </select>
					</div>
					<div class="float-left mt3">类型：</div>
					<div class="float-left mr10">
						<select name="infoType" id="infoType" class="form-control" onchange="changeExam()">
	                        <option value="0" <#if '${infoType!}'=='0'>selected="selected"</#if>>考试</option>	
	                        <option value="1" <#if '${infoType!}'=='1'>selected="selected"</#if>>培训</option>			                       
	                    </select>
					</div>
					<div class="float-left mt3">名称：</div>
					<div class="float-left mr10">
						<select name="" id="examId" class="form-control" onChange="searchList();">
						<#if examList?exists && examList?size gt 0>
				            <#list examList as exam>
				                <option value="${exam.id!}" <#if examId?exists && examId==exam.id>selected="selected"</#if>>${exam.examName!}</option>
				            </#list>
				        <#else>
				            <option value="">--请选择--</option>
				        </#if>
						</select>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<div id="haveAudtionDiv"></div>
<script>
$(function(){
    searchList();
})

function searchList(){
   var examId = $('#examId').val();
   var acadyear = $('#year').val();
   var semester = $('#infoType').val();
   var url = "${request.contextPath}/teaexam/registerAudit/haveAudtingList?examId="+examId+"&year="+acadyear+"&infoType="+semester;
   $('#haveAudtionDiv').load(url);
}

function changeExam(){
  var acadyear = $('#year').val();
  var semester = $('#infoType').val();
  var examId=$("#examId");
  $.ajax({
		url:"${request.contextPath}/teaexam/registerAudit/examList?year="+acadyear+"&infoType="+semester,
		dataType: "json",
		success: function(data){
		   examId.html("");
		   examId.chosen("destroy");
		   if(data==null || data.length==0){
			  examId.append("<option value='' >---请选择---</option>");
		   }else{
			  for(var m=0;m<data.length;m++){
			     examId.append("<option value='"+data[m].id+"'>"+data[m].examName+"</option>");
		      }
		   }
		   searchList();
		}
	});		

}
</script>