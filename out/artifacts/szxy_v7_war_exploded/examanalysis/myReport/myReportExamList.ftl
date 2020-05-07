<table class="table table-striped table-hover no-margin" style="border-top: 1px solid #ddd;">
    <tbody>
    	<tr>
            <th>考试名称</th>
            <th>年级</th>
            <#if type?default("")=="2" || type?default("")=="4">
            	<th>科目</th>
            </#if>
            <#if type?default("")=="3" || type?default("")=="4">
            	<th>班级</th>
            </#if>
            <th>操作</th>
        </tr>
        <#if examDtoList?exists && examDtoList?size gt 0>
        <#list examDtoList as item>
	        <tr>
	            <td>${item.examName!}</td>
	            <td>${item.gradeName!}</td>
	            <#if type?default("")=="2" || type?default("")=="4">
	            	<td>${item.subjectName!}</td>
	            </#if>
	            <#if type?default("")=="3" || type?default("")=="4">
	            	<td>${item.className!}</td>
	            </#if>
	            <td>
	                <a class="table-btn color-blue" href="javascript:void(0)" onclick="goDetailReport('${item.id!}','${type!}','${item.subjectId!}','${item.subType!}','${item.classId!}','${item.teachClassType!}')">查看</a>
	            </td>
	        </tr>
	    </#list>
        </#if>
    </tbody>
</table>
<script>
	function goDetailReport(examId,type,subjectId,subType,classId,teachClassType){
		if(type){
			if(type=="1"){
				window.open("${request.contextPath}/examanalysis/emReport/goSchoolReport?unitId=${unitId!}&examId="+examId);
			}else if(type=="2"){
				window.open("${request.contextPath}/examanalysis/emReport/goGradeReport?unitId=${unitId!}&examId="+examId+"&subjectId="+subjectId+"&subType="+subType);
			}else if(type=="3"){
				window.open("${request.contextPath}/examanalysis/emReport/goClassReport?unitId=${unitId!}&examId="+examId+"&classId="+classId);
			}else if(type=="4"){
				window.open("${request.contextPath}/examanalysis/emReport/goTeachClassReport?unitId=${unitId!}&examId="+examId+"&classId="+classId+"&teachClassType="+teachClassType+"&subjectId="+subjectId+"&subType="+subType);
			}
		}
	}
</script>
