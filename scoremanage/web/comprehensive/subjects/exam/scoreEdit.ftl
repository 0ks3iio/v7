<div class="main-content">
	<div class="main-content-inner">
		<div class="page-content">
			<a href="javascript:toBack();" class="page-back-btn"><i class="fa fa-arrow-left"></i>返回</a>
			<div class="box box-default">
				<div class="box-body">
					<ul class="nav nav-tabs" role="tablist">
						<#if compreRelationshipList?exists && compreRelationshipList?size gt 0>
						   <#list compreRelationshipList as item>
						       <li role="presentaion"
						       <#if '${subjectId!}' != '' && '${subjectId!}' == '${item.relationshipValue!}'>
						             class="active"
						       <#elseif '${subjectId!}' == '' && item_index == 0>
						             class="active"
						       </#if>>
						       <a href="#bb" role="tab" data-toggle="tab" onclick="showScore(this)">${item.relationshipName!}</a>
						       <input type="hidden"  class="examId"  value="${item.relationshipId!}"/>
						       <input type="hidden"  class="subjectId"  value="${item.relationshipValue!}"/>
						       </li>
						   </#list>
						</#if>
					</ul>
					<div class="tab-content" id="showCompreScore">			       			       
			        </div>
		       </div>
			</div>
		</div> <!-- /.page-content -->
	</div>
</div>

<script>
  $this = $('li[class="active"]');
  $(function(){
     
     showScore("");
  });

  function showSynScore(examId,subjectId,gradeId){
    var url =  '${request.contextPath}/comprehensive/subjects/score/showpage?examId='+examId+'&subjectId='+subjectId+'&gradeId='+gradeId;
    $('#showCompreScore').load(url);
  }
  
  function showScore(obj){
    if(obj != ""){
      $this = $(obj).parent();
    }
    var gradeId = '${gradeId!}';
    var examId =$this.find('.examId').val();
    var subjectId = $this.find('.subjectId').val();
	var url =  '${request.contextPath}/comprehensive/subjects/score/showpage?examId='+examId+'&subjectId='+subjectId+'&gradeId='+gradeId;
    $('#showCompreScore').load(url);
  }
  

  function toBack(){
    var url =  '${request.contextPath}/comprehensive/subjects/score/head/page';
	$("#exammanageDiv").load(url);
  }
</script>