<table class="table table-striped table-hover no-margin" style="border-top: 1px solid #ddd;">
    <tbody>
    	<tr>
            <th>考试名称</th>
            <th>年级</th>
            <th>操作</th>
        </tr>
        <#if examDtoList?exists && examDtoList?size gt 0>
        <#list examDtoList as item>
	        <tr>
	            <td>${item.examName!}</td>
	            <td>${item.gradeName!}</td>
	            <td>
	                <a class="table-btn color-blue" href="javascript:void(0)" onclick="goDetailReport('${item.id!}')">查看</a>
	            </td>
	        </tr>
	    </#list>
        </#if>
    </tbody>
</table>
<script>
	function goDetailReport(examId){
		window.open("${request.contextPath}/examanalysis/emReport/goStuReport?unitId=${unitId!}&studentId=${studentId!}&examId="+examId);
	}
</script>
