<form id="">
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
				    <th>
                        <label class="pos-rel js-select">
		                <input id="allCheck" type="checkbox" class="wp" value=""/>
		                <span class="lbl"></span>
                        </label>
	              
				                        选择
				    </th>
					<th>姓名</th>
					<th>学号</th>
					<th>性别</th>
					<th>出生日期</th>
                    <th>操作</th>
				</tr>
		</thead>
		<tbody>
			<#if stuList?exists && (stuList?size > 0)>
				<#list stuList as item>
					<tr>
					    <td>
					       <label class="pos-rel js-select">
								<input name="stu-checkbox" type="checkbox" class="wp" value="${item.id!}"/>
								<span class="lbl"></span>
							</label>
					    </td>
						<td>${item.studentName!}</td>
                        <td>${item.studentCode!}</td>
                        <td>${mcodeSetting.getMcode("DM-XB",item.sex?default(0)?string)}</td>
						<td>${(item.birthday?string('yyyy-MM-dd'))!}</td>
						<td>
				           	<a href="javascript:void(0);" onclick="searchReport('${item.id!}');" class="table-btn color-red">查看报告单</a>
			            	<a href="javascript:void(0);" id="stuPdf_${item.id!}" style="display:none;" onclick="showPdf('${item.id!}');" class="table-btn color-red">查看pdf</a>
			            </td>					
					</tr>
				</#list>
			</#if>
		</tbody>
	</table>
</form>
<script>
$(function(){
	<#if type?default("")=="start">
		 $("#btnPdfId").addClass("disabled");
	<#else>
		$("#btnPdfId").removeClass("disabled");
	</#if>
	stuHavePdf();
	$("#allCheck").click(function(){
          if($("#allCheck").is(':checked')){
             $("#allCheck").prop("checked",true);
             $('.wp').prop("checked",true);
          }else{
             $("#allCheck").removeAttr("checked");
             $(".wp").removeAttr("checked");
          }
     });	
});

function searchReport(studentId){
   var acadyear = $('#acadyear').val();
   var semester = $('#semester').val();
	var classId = $("#classId").val();
   var str = "?acadyear="+acadyear+"&semester="+semester+ "&classId="+classId+"&studentId="+studentId;
   var url = "${request.contextPath}/studevelop/stuQualityReport/report"+str;
   $("#showList").load(url);
}


</script>