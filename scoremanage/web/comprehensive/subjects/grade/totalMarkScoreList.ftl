<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/dataTables.rowsGroup.js"></script>
<div class="table-container">
<div class="table-container-header">
	共有${dataSize!}个数据
</div>
<div class="table-container-body">
	<form id="tableForm">
	<table id="example" class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>序号</th>
				<th>&emsp;&emsp;姓名&emsp;&emsp;</th>
				<th>学号</th>
				<th>&emsp;&emsp;班级&emsp;&emsp;</th>
				<#if compreRelationshipList?exists && (compreRelationshipList?size > 0)>
					<#list compreRelationshipList as comList>
						<th>${comList.relationshipName!}</th>
					</#list>
				</#if>
				<th>总成绩</th>
				<th>年级排名</th>
				<#if needEnglishRank == true>
				<th>英语排名</th>
				</#if>
				<#if englishOralTestRank == true>
				<th>英语口试排名</th>
				</#if>
			</tr>
		</thead>
		<tbody>
		<#if compreScores?exists && (compreScores?size > 0)>
			<#list compreScores as comScores>
			<tr>
				<input type="hidden" name="compreScores[${comScores_index}].studentId" value='${comScores.studentId!}'>
				<input type="hidden" name="compreScores[${comScores_index}].classId" value='${comScores.classId!}'>
				<td>${comScores_index+1}</td>
				<td>${comScores.studentName!}</td>
				<td>${comScores.studentCode!}</td>
				<td>${comScores.className!}</td>
				<#assign map = comScores.scoreMap/>
				<#list map?keys as key>
					<td><input type="text" class="form-control number" name="compreScores[${comScores_index}].scoreMap[${key}]" nullable="true" value='${map[key]}'></td>
				</#list>
				<td><#if comScores.totalScore?? >${(comScores.totalScore)?string("#.##")}</#if></td>
				<td>${comScores.ranking!}</td>
				<#if needEnglishRank == true>
				<td><#if comScores.englishRank != 0>${comScores.englishRank!}</#if></td>
				</#if>
				<#if englishOralTestRank == true>
				<td><#if comScores.englishOralTestRank != 0>${comScores.englishOralTestRank!}</#if></td>
				</#if>
			</tr>
			</#list>
		</#if>
		</tbody>
	</table>
	</form>
</div>
</div>
<#if compreScores?exists && (compreScores?size > 0) && classId == "all">
<@htmlcom.pageToolBar container="#totalMarkScoreDiv"/>
</#if>
<script>
	$(function(){
		// 回车获取焦点
    	var $inp = $('input:text');
    	$inp.on('keydown', function (e) {
       		var key = e.which;
       	 	if (key == 13) {
            e.preventDefault();
            var nxtIdx = $inp.index(this) + 1;
            $(":input:text:eq(" + nxtIdx + ")").focus().select();
        }
   	 	});    
		
		<#if compreScores?exists && (compreScores?size > 0)>
		$('#example').DataTable( {
			scrollY: "600px",
			info: false,
			searching: false,
			autoWidth: false,
			sort: true,
			paging: false,
			scrollX: true,
			fixedColumns: {
				leftColumns: 4
			}
		});
		</#if>
	});
</script>
