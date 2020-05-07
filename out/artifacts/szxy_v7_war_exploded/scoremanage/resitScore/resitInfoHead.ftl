<ul class="nav nav-tabs nav-tabs-1">
    <#if resitInfoList?exists && resitInfoList?size gt 0>
         <#list resitInfoList as item>
              <li role="presentation" <#if item_index==0>class="active"</#if>>
				   <a role="tab" data-toggle="tab" href="javascript:void(0)" onclick="chooseTab('${item.subjectId!}')">${item.subjectName!}</a>
		       </li>
         </#list>
     <#else>
          <div class="no-data-container">
				<div class="no-data no-data-hor">
					<span class="no-data-img">
						<img src="${request.contextPath}/scoremanage/images/no-tutor-project.png" alt="">
					</span>
					<div class="no-data-body">
					    <h3>暂无数据</h3>
					</div>
			   </div>
		 </div>
     </#if>
</ul>
<div class="tab-content" id="resitScoreList">
</div>
<input type="hidden" id="courseId">
<script>
function chooseTab(subjectId){
    var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var examId = $('#examId').val();
	var gradeId = $('#gradeId').val();
	$('#courseId').val(subjectId);
	var url="${request.contextPath}/scoremanage/resitScore/resitScoreList?acadyear="+acadyear+"&semester="+semester+"&examId="+examId+"&gradeId="+gradeId+"&subjectId="+subjectId;
	$('#resitScoreList').load(url);
}


$(function(){
     hidenBreadBack();
	<#if subjectId?exists && subjectId!="">
	      chooseTab('${subjectId!}');
	      $('#courseId').val('${subjectId!}');
	</#if>
});
</script>